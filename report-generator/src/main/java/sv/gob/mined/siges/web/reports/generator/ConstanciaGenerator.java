/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class ConstanciaGenerator {
       
    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    @Inject
    private PlantillaRestClient plantillaClient;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;
    
    public MasterReport generarReporte(TipoUnidad tipoUnidad, Long unidadAF, Long unidadAD, Integer periodo) throws Exception {
        String responsable = StringUtils.EMPTY;
        try {
            BusinessException be = new BusinessException();
            if (tipoUnidad == null) {
                be.addError("Debes ingresar el tipo de unidad.");
                throw be;
            }
            
            if (periodo == null) {
                be.addError("Debes ingresar el periodo de la constancia.");
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
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CONSTANCIA_UNIDAD);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CONSTANCIA_UNIDAD + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("constancia.prpt");
            } 
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            SgSede sede = null;
            SgAfUnidadesAdministrativas unidad = null;
            SgAfUnidadesActivoFijo unidadAFijo = null;
            
            FiltroUnidadesActivoFijo filtroUAF = new FiltroUnidadesActivoFijo();
            filtroUAF.setId(unidadAF);
            filtroUAF.setMaxResults(1L);
            filtroUAF.setIncluirCampos(new String[] {"uafResponsableAF","uafVersion"});
            
            List<SgAfUnidadesActivoFijo> unidadesAF = unidadesActivoFijoRestClient.buscar(filtroUAF);
                    
            if(unidadesAF != null && !unidadesAF.isEmpty()) {
                unidadAFijo = unidadesAF.get(0);
            }
            if (unidadAFijo == null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.UNIDAD_ACTIVO_FIJO_VACIA));
                throw be;
            }
            
            responsable = unidadAFijo.getUafResponsableAF()!= null && StringUtils.isNotBlank(unidadAFijo.getUafResponsableAF()) ? unidadAFijo.getUafResponsableAF().trim().toUpperCase() : "";
            if (responsable == null || StringUtils.isBlank(responsable)) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.RESPONSABLE_AF_NO_CONFIGURADO));
                throw be;
            }
            
            
            if(tipoUnidad != null && tipoUnidad.equals(TipoUnidad.CENTRO_ESCOLAR)) {
                FiltroSedes filtroSede = new FiltroSedes();
                filtroSede.setSedPk(unidadAD);
                filtroSede.setMaxResults(1L);
                filtroSede.setIncluirCampos(new String[]{"sedCodigo","sedNombre","sedTipo","sedDireccion.dirMunicipio.munNombre","sedVersion"});
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
            
            report.setDataFactory(this.getDataFactory(sede, unidad, periodo, responsable));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(SgSede sede,SgAfUnidadesAdministrativas unidad, Integer periodo, String responsable) throws Exception {
        TypedTableModel model = new TypedTableModel();
        model.addColumn("nombre", String.class);
        model.addColumn("nombreUnidad", String.class);
        model.addColumn("codigoUnidad", String.class);
        model.addColumn("tipoUnidad", String.class);
        model.addColumn("tipoUnidad1", String.class);
        model.addColumn("tipoUbicacion", String.class);
        model.addColumn("ubicacionUnidad", String.class);
        model.addColumn("periodo", String.class);
        model.addColumn("dia", String.class);
        model.addColumn("mes", String.class);
        model.addColumn("anio", String.class);
        if(sede != null) {
            model.addRow(
                responsable,//nombre
                sede.getSedNombre().trim().toUpperCase(),//nombreunidad
                sede.getSedCodigo().trim().toUpperCase(),//codigoUnidad
                "escolar",//tipoUnidad
                "Escolar",//tipoUnidad1
                "Municicipio",//tipoUbicacion
                (sede.getSedDireccion() != null && sede.getSedDireccion().getDirMunicipio() != null) ? sede.getSedDireccion().getDirMunicipio().getMunNombre().trim().toUpperCase() : "",
                periodo,//periodo
                LocalDate.now().getDayOfMonth(),//dia
                Generales.getMesDescripcion(LocalDate.now().getMonth().getValue()),//mes
                LocalDate.now().getYear()//anio
            );
        } else if(unidad != null) {
            model.addRow(
                responsable,//nombre
                unidad.getUadNombre().trim().toUpperCase(),//nombreunidad
                unidad.getUadCodigo().trim().toUpperCase(),//codigoUnidad
                "administrativo",//tipoUnidad
                "",//tipoUnidad1
                "Departamento",//tipoUbicacion
                (unidad.getUadUnidadActivoFijoFk() != null && unidad.getUadUnidadActivoFijoFk().getUafDepartamento() != null) ? unidad.getUadUnidadActivoFijoFk().getUafDepartamento().getDepNombre().trim().toUpperCase() : "", 
                periodo,//periodo
                LocalDate.now().getDayOfMonth(),//dia
                Generales.getMesDescripcion(LocalDate.now().getMonth().getValue()),//mes
                LocalDate.now().getYear()//anio
            );
        }
        
        
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);

        return dataFactory;
    }
}