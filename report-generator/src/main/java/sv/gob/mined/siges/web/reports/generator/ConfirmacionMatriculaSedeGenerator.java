/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
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
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgConsultaMatriculasSede;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatriculasEnSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class ConfirmacionMatriculaSedeGenerator {

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    private MatriculaRestClient matriculaClient;
    
    @Inject
    private PlantillaRestClient plantillaClient;
    
    @Inject
    private SedeRestClient sedeClient;



    private static final Logger LOGGER = Logger.getLogger(ConfirmacionMatriculaSedeGenerator.class.getName());

    public MasterReport generarReporte(Long sedPk, Integer anio) throws Exception {

        try {
            BusinessException be = new BusinessException();
            if (sedPk == null) {
                be.addError("Debes ingresar una sede.");
                throw be;
            }
            
            if (anio == null) {
                be.addError("Debes ingresar una año.");
                throw be;
            }
            
           
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CONFIRMACION_MATRICULA_SEDE_CODIGO);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CONFIRMACION_MATRICULA_SEDE_CODIGO + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("matriculas_en_sede.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            
            FiltroMatriculasEnSede filtro = new FiltroMatriculasEnSede();
            filtro.setAnio(anio);
            filtro.setSedPk(sedPk);
            List<SgConsultaMatriculasSede> ret = matriculaClient.obtenerConfirmacionMatriculaEnSede(filtro);
            
            FiltroSedes filSede = new FiltroSedes();
            filSede.setSedPk(sedPk);
            filSede.setIncluirCampos(new String[]{"sedTipo", "sedCodigo", "sedNombre", "sedDireccion.dirDepartamento.depNombre", "sedDireccion.dirMunicipio.munNombre"});
            List<SgSede> sedes = sedeClient.buscar(filSede);
            if (sedes.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SEDE_NO_ENCONTRADA));
                throw be;
            }


            SgSede sede = sedes.get(0);
            
            report.setDataFactory(this.getDataFactory(ret));
            report.getParameterValues().put("sede", sede.getSedCodigoNombre());
            report.getParameterValues().put("departamento", sede.getSedDireccion().getDirDepartamento().getDepNombre());
            report.getParameterValues().put("municipio", sede.getSedDireccion().getDirMunicipio().getMunNombre());
            report.getParameterValues().put("anio_lectivo", anio.toString());
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }



    private DataFactory getDataFactory(List<SgConsultaMatriculasSede> consulta) throws Exception {

        TypedTableModel grados = new TypedTableModel();
        grados.addColumn("nivel", String.class);
        grados.addColumn("ciclo", String.class);
        grados.addColumn("modalidad_educativa", String.class);
        grados.addColumn("modalidad_atencion", String.class);
        grados.addColumn("grado", String.class);
        grados.addColumn("cant_masculino", Long.class);
        grados.addColumn("cant_femenino", Long.class);
        grados.addColumn("total", Long.class);
        
        
        
        for (SgConsultaMatriculasSede c : consulta){
            
            Long cantMasc = c.getCantMatMasculino() != null ? c.getCantMatMasculino() : 0L;
            Long cantFem = c.getCantMatFemenino() != null ? c.getCantMatFemenino() : 0L;
            
            grados.addRow(c.getNivelNombre(),
                    c.getCicloNombre(),
                    c.getModalidadEduNombre(),
                    c.getModalidadAtenNombre(),
                    c.getGradoNombre(),
                    cantMasc,
                    cantFem,
                    cantMasc + cantFem);
        }

               
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", grados);
        return dataFactory;
    }

}
