/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgAfSolvencias;
import sv.gob.mined.siges.web.dto.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolvencias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.SolvenciasRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class SolvenciaGenerator {
    
    private static final Logger LOGGER = Logger.getLogger(SolvenciaGenerator.class.getName());
    
    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    @Inject
    private SolvenciasRestClient solvenciasRestClient;
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;  
     
    @Inject
    private PlantillaRestClient plantillaClient;
    
    public MasterReport generarReporte(TipoUnidad tipoUnidad, Long unidadAF, Long unidadAD, Integer periodo) throws Exception {

        try {
            BusinessException be = new BusinessException();
            if (tipoUnidad == null) {
                be.addError("Debes ingresar el tipo de unidad.");
                throw be;
            }
            
            if (periodo == null) {
                be.addError("Debes ingresar el periodo de la solvencia.");
                throw be;
            }
            
            if (unidadAF == null) {
                be.addError("Debes ingresar la unidad de activo fijo.");
                throw be;
            }
            
            if (unidadAD == null) {
                be.addError("Debes ingresar la unidad administrativa.");
                throw be;
            }
            
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_SOLVENCIA_UNIDAD);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_SOLVENCIA_UNIDAD + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("solvencia.prpt");
            }
            
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            SgAfUnidadesActivoFijo unidadAFijo = null;
            SgSede sede = null;
            SgAfUnidadesAdministrativas unidad = null;        
                    
            FiltroUnidadesActivoFijo filtroUAF = new FiltroUnidadesActivoFijo();
            filtroUAF.setId(unidadAF);
            filtroUAF.setMaxResults(1L);
            filtroUAF.setIncluirCampos(new String[] {"uafNombre","uafVersion"});
            
            List<SgAfUnidadesActivoFijo> unidadesAF = unidadesActivoFijoRestClient.buscar(filtroUAF);
                    
            if(unidadesAF != null && !unidadesAF.isEmpty()) {
                unidadAFijo = unidadesAF.get(0);
            }
                    
            if(tipoUnidad.equals(TipoUnidad.CENTRO_ESCOLAR)) {
                FiltroSedes filtroSede = new FiltroSedes();
                filtroSede.setSedPk(unidadAD);
                filtroSede.setMaxResults(1L);
                filtroSede.setIncluirCampos(new String[]{"sedCodigo","sedNombre","sedTipo","sedDireccion.dirMunicipio.munNombre","sedDireccion.dirDepartamento.depNombre","sedVersion"});
                List<SgSede> sedes = sedeRestClient.buscar(filtroSede);
                if(sedes != null && !sedes.isEmpty()) {
                    sede = sedes.get(0);
                }
                if (sede == null) {
                    be.addError(Mensajes.obtenerMensaje(Mensajes.SEDE_VACIA));
                    throw be;
                }
            } else if(tipoUnidad.equals(TipoUnidad.UNIDAD_ADMINISTRATIVA)) {
                FiltroUnidadesAdministrativas filtroUAD = new FiltroUnidadesAdministrativas();
                filtroUAD.setId(unidadAD);
                filtroUAD.setMaxResults(1L);
                filtroUAD.setIncluirCampos(new String[] {"uadNombre","uadCodigo","uadUnidadActivoFijoFk.uafDepartamento.depNombre","uadVersion"});
                List<SgAfUnidadesAdministrativas> unidadesAD = unidadesAdministrativasRestClient.buscar(filtroUAD);
                if(unidadesAD != null && !unidadesAD.isEmpty()) {
                    unidad = unidadesAD.get(0);
                }
                if (unidad == null) {
                    be.addError(Mensajes.obtenerMensaje(Mensajes.UNIDAD_ADMINISTRATIVA_VACIA));
                    throw be;
                }
            }
            
            
            report.setDataFactory(this.getDataFactory(unidadAFijo, sede, unidad, periodo));
            
            SgAfSolvencias sol= new SgAfSolvencias();
            
            FiltroSolvencias filtroSolvencias = new FiltroSolvencias();
            filtroSolvencias.setTipoUnidad(tipoUnidad);
            filtroSolvencias.setUnidadAdministrativaId(unidadAD);
            filtroSolvencias.setAnio(periodo);
            //Se buscan las existentes para sobreescribirla
            List<SgAfSolvencias> solvencias = solvenciasRestClient.buscar(filtroSolvencias);
            if(solvencias != null && !solvencias.isEmpty()) {
                sol = solvencias.get(0);
            }
            sol.setSolAnio(periodo);
            sol.setSolFechaSolvencia(LocalDate.now());
            sol.setSolSedeFk(sede);
            sol.setSolUnidadAdministrativaFk(unidad);
            solvenciasRestClient.guardar(sol);
            return report;
        } catch (BusinessException ge) {
            LOGGER.log(Level.SEVERE, ge.getMessage(), ge);
            throw ge;
        }
    }

    private DataFactory getDataFactory(SgAfUnidadesActivoFijo unidadAF, SgSede sede,SgAfUnidadesAdministrativas unidad, Integer periodo) throws Exception {
        String fecha = LocalDate.now().getDayOfMonth() + " de " + Generales.getMesDescripcion(LocalDate.now().getMonth().getValue()) + " de " + LocalDate.now().getYear();
        TypedTableModel model = new TypedTableModel();
        model.addColumn("tecnicoAF", String.class);
        model.addColumn("nombreUnidad", String.class);
        model.addColumn("codigoAcreditacion", String.class);
        model.addColumn("ubicacion", String.class);
        //model.addColumn("departamento", String.class);
        //model.addColumn("municipio", String.class);
        model.addColumn("departamental", String.class);
        model.addColumn("fecha", String.class);
        model.addColumn("anioInventario", String.class);
        model.addColumn("cargoAF", String.class); 
        String ubicacion = "";
        if(sede != null) {
            ubicacion = "DEL MUNICIPIO DE " + ((sede.getSedDireccion() != null && sede.getSedDireccion().getDirMunicipio() != null) ? sede.getSedDireccion().getDirMunicipio().getMunNombre().trim().toUpperCase() : "");
            ubicacion += ", DEPARTAMENTO DE " + ((sede.getSedDireccion() != null && sede.getSedDireccion().getDirDepartamento() != null) ? sede.getSedDireccion().getDirDepartamento().getDepNombre().trim().toUpperCase() : "");
            
            model.addRow(
                sessionBean.getEntidadUsuario() != null ? sessionBean.getEntidadUsuario().getUsuNombre().trim() : sessionBean.getUserCode() ,
                sede.getSedNombre().trim().toUpperCase(),
                sede.getSedCodigo().trim().toUpperCase(),
                ubicacion.trim(),
                unidadAF != null ? unidadAF.getUafNombre().trim().toUpperCase() : "",
                fecha,
                periodo,
                "TÉCNICO DE ACTIVO FIJO"
            );
        } else if(unidad != null) {
            ubicacion = "DEL DEPARTAMENTO DE " + ((unidad.getUadUnidadActivoFijoFk() != null && unidad.getUadUnidadActivoFijoFk().getUafDepartamento() != null) ? unidad.getUadUnidadActivoFijoFk().getUafDepartamento().getDepNombre().trim().toUpperCase() : "");
            model.addRow(
                sessionBean.getEntidadUsuario() != null ? sessionBean.getEntidadUsuario().getUsuNombre().toUpperCase().trim() : sessionBean.getUserCode().toUpperCase() ,
                unidad.getUadNombre().trim().toUpperCase(),
                unidad.getUadCodigo().trim().toUpperCase(),
                ubicacion, 
                unidadAF != null ? unidadAF.getUafNombre().trim().toUpperCase() : "",
                fecha,
                periodo,
                "TÉCNICO DE ACTIVO FIJO"
            );
        }
        
        
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);

        return dataFactory;
    }
}