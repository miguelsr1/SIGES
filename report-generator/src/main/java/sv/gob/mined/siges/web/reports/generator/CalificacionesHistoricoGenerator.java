/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.restclient.CalificacionesHistoricasEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class CalificacionesHistoricoGenerator {

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    private CalificacionesHistoricasEstudianteRestClient calEstudianteClient;

    @Inject
    private PlantillaRestClient plantillaClient;
    
    @Inject
    private EstudianteRestClient estudianteRestClient;

    private static final Logger LOGGER = Logger.getLogger(CalificacionesHistoricoGenerator.class.getName());

    Boolean respetaOrden;

    public MasterReport generarReporte(Integer anio, Long estudiantePk) throws Exception {

        try {
            BusinessException be = new BusinessException();
            if (estudiantePk == null) {
                be.addError("Debes ingresar un estudiante.");
                throw be;
            }

            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CALIFICACIONES_HISTORICO_CODIGO);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CALIFICACIONES_HISTORICO_CODIGO + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("calificaciones_historicas_2.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            
            FiltroEstudiante fe=new FiltroEstudiante();
            fe.setEstPk(estudiantePk);
            fe.setIncluirCampos(new String[]{"estPersona.perNie"});
            List<SgEstudiante> estList=estudianteRestClient.buscar(fe);
            
            if(estList==null || estList.isEmpty() || estList.get(0).getEstPersona()==null || estList.get(0).getEstPersona().getPerNie()==null){
                be.addError("No existe estudiante");
                throw be;
            }

            FiltroCalificacionesHistoricasEstudiante filtro = new FiltroCalificacionesHistoricasEstudiante();
            filtro.setEstNie(estList.get(0).getEstPersona().getPerNie());
            filtro.setAnio(anio);
            filtro.setIncluirCampos(new String[]{"cheMatriculaExternaId",
                        "cheAnioMatricula",
                        "cheObservacion",
                        "cheFechaRealizado",
                        "cheProcesoDeCreacion",
                        "cheEstudianteFk.estPk",
                        "cheEstudianteFk.estVersion",
                        "cheEstudianteFk.estPersona.perPk",
                        "cheEstudianteFk.estPersona.perVersion",
                        "cheEstudianteFk.estPersona.perPrimerNombre",
                        "cheEstudianteFk.estPersona.perSegundoNombre",
                        "cheEstudianteFk.estPersona.perPrimerApellido",
                        "cheEstudianteFk.estPersona.perSegundoApellido",
                        "cheEstudianteFk.estPersona.perNie",
                        "chePersonaFk.perPk",
                        "chePersonaFk.perVersion",
                        "cheEstudianteNie",
                        "cheSedeFk.sedPk",
                        "cheSedeFk.sedVersion",
                        "cheSedeFk.sedCodigo",
                        "cheSedeFk.sedNombre",
                        "cheSedeFk.sedTipo",
                        "cheSedeFk.sedTelefono",
                        "cheSedeExternaCodigo",
                        "cheSedeExternaNombre",
                        "cheServicioEducativoExternoDescripcion",
                        "cheServicioEducativoExternoEtiquetaImpresion",
                        "chePlanEstudioExternoId",
                        "chePlanEstudioExternoDescripcion",
                        "cheComponente",
                        "chePI",
                        "cheNFI",
                        "cheNPAESI",
                        "cheRIX",
                        "cheRIR",
                        "cheRIRE",
                        "cheNF",
                        "cheNPAES",
                        "chePIModif",
                        "chePI_r",
                        "chePIR",
                        "cheUltModFecha",
                        "cheUltModUsuario",
                        "cheVersion", "cheEvaluacionExternaId",
                        "cheServicioEducativoExternoId"});
            filtro.setOrderBy(new String[]{"cheAnioMatricula"});
            filtro.setAscending(new boolean[]{true});
            List<SgCalificacionesHistoricasEstudiante> listCalificacion = calEstudianteClient.buscar(filtro);
            if (!listCalificacion.isEmpty()) {
                SgEstudiante est = listCalificacion.get(0).getCheEstudianteFk();

                TypedTableModel model = new TypedTableModel();
                model.addColumn("nie_estudiante", String.class);
                model.addColumn("nombre_estudiante", String.class);
                model.addColumn("calificaciones", TypedTableModel.class);

                List<String> sedesExt = listCalificacion.stream().filter(c->c.getCheSedeFk()==null).map(c -> c.getCheSedeExternaCodigo()).distinct().collect(Collectors.toList());
                List<SgSede> sede = listCalificacion.stream().filter(c->c.getCheSedeFk()!=null).map(c -> c.getCheSedeFk()).distinct().collect(Collectors.toList());
            
                model.addRow(
                        est.getEstPersona().getPerNie(), est.getEstPersona().getPerNombreCompleto(),
                        this.getEstudianteCalificationsTableModel(listCalificacion, sedesExt, sede)
                );
                TableDataFactory dataFactory = new TableDataFactory();
                dataFactory.addTable("param-query", model);
                report.setDataFactory(dataFactory);
                return report;
            } else {
                be.addError("No existen calificaciones para el estudiante seleccionado.");
                throw be;
            }

        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private TypedTableModel getEstudianteCalificationsTableModel(List<SgCalificacionesHistoricasEstudiante> calList, List<String> sedesExt, List<SgSede> sede) {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("sede_codigo_nombre", String.class);
        model.addColumn("cal", TypedTableModel.class);

        for (String sedCodigo : sedesExt) {
            String codigoNombre=sedCodigo;
            TypedTableModel model2 = new TypedTableModel();
            model2.addColumn("anio", String.class);
            model2.addColumn("servicio_educativo", String.class);
            model2.addColumn("componente", String.class);
            model2.addColumn("nota_final", String.class);
            List<SgCalificacionesHistoricasEstudiante> calListSede=calList.stream().filter(c->sedCodigo.equals(c.getCheSedeExternaCodigo())).collect(Collectors.toList());
            for (SgCalificacionesHistoricasEstudiante calEsto : calListSede) {
                codigoNombre=calEsto.getSedeExternaNombreCompleto();
                model2.addRow(calEsto.getCheAnioMatricula(),
                        calEsto.getCheServicioEducativoExternoDescripcion(),
                        calEsto.getCheComponente(),
                        calEsto.getCheNF()
                );

            }

            model.addRow(codigoNombre,model2);
        }
        
        for (SgSede sedFk : sede) {
            TypedTableModel model2 = new TypedTableModel();
            model2.addColumn("anio", String.class);
            model2.addColumn("servicio_educativo", String.class);
            model2.addColumn("componente", String.class);
            model2.addColumn("nota_final", String.class);
            List<SgCalificacionesHistoricasEstudiante> calListSede=calList.stream().filter(c->sedFk.equals(c.getCheSedeFk())).collect(Collectors.toList());
            for (SgCalificacionesHistoricasEstudiante calEsto : calListSede) {
                model2.addRow(calEsto.getCheAnioMatricula(),
                        calEsto.getCheServicioEducativoExternoEtiquetaImpresion(),
                        calEsto.getCheComponente(),
                        calEsto.getCheNF()
                );

            }

            model.addRow(sedFk.getSedCodigoNombre(),model2);
        }

        return model;
    }

}
