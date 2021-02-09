/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
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
import sv.gob.mined.siges.web.dto.SgCierreAnioLectivoSede;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgRelPreguntaCierreAnioSede;
import sv.gob.mined.siges.web.dto.SgResumenCierreSeccion;
import sv.gob.mined.siges.web.dto.SgResumenCierreSedeRequest;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.restclient.CierreAnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class CierreAnioGenerator {

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    private PlantillaRestClient plantillaClient;

    @Inject
    private CierreAnioLectivoRestClient cierreAnioClient;

    public MasterReport generarReporte(Long cierrePk) throws Exception {

        try {

            BusinessException be = new BusinessException();
            if (cierrePk == null) {
                be.addError("Debes ingresar un cierre.");
                throw be;
            }

            SgResumenCierreSedeRequest req = new SgResumenCierreSedeRequest();
            req.setCierreAnioPk(cierrePk);
            SgCierreAnioLectivoSede cierre = cierreAnioClient.obtenerResumenCierreSedeAnio(req);

//            if (cierre == null) {
//                be.addError(Mensajes.obtenerMensaje(Mensajes.SECCION_NO_ENCONTRADA));
//                throw be;
//            }
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CIERRE_ANIO_CODIGO);

                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CIERRE_ANIO_CODIGO + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("cierre_anio.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            report.getParameterValues().put("sede", cierre.getCalSede().getSedCodigoNombre());
            report.getParameterValues().put("anio", cierre.getCalAnioLectivo().getAleAnio());
            report.getParameterValues().put("total_asistencias", cierre.getCalAsistencias());
            report.getParameterValues().put("total_inasistencias", cierre.getCalInasistencias());
            report.getParameterValues().put("total_promovidos_m", cierre.getCalPromovidosMasc());
            report.getParameterValues().put("total_promovidos_f", cierre.getCalPromovidosFem());
            report.getParameterValues().put("total_promovidos_t", cierre.getCalPromovidosMasc() + cierre.getCalPromovidosFem());
            report.getParameterValues().put("total_no_promovidos_m", cierre.getCalNoPromovidosMasc());
            report.getParameterValues().put("total_no_promovidos_f", cierre.getCalNoPromovidosFem());
            report.getParameterValues().put("total_no_promovidos_t", cierre.getCalNoPromovidosMasc() + cierre.getCalNoPromovidosFem());
            report.getParameterValues().put("total_promovidos_pend_m", cierre.getCalPromocionesPendientesMasc());
            report.getParameterValues().put("total_promovidos_pend_f", cierre.getCalPromocionesPendientesFem());
            report.getParameterValues().put("total_promovidos_pend_t", cierre.getCalPromocionesPendientesMasc() + cierre.getCalPromocionesPendientesFem());
            report.getParameterValues().put("total_sol_titulo_m", cierre.getCalSolicitudesTitulosMasc());
            report.getParameterValues().put("total_sol_titulo_f", cierre.getCalSolicitudesTitulosFem());
            report.getParameterValues().put("total_sol_titulo_t", cierre.getCalSolicitudesTitulosMasc() + cierre.getCalSolicitudesTitulosFem());

            //End Set report parameters
            report.setDataFactory(this.getDataFactory(cierre));

            return report;

        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw e;
        }
    }

    private DataFactory getDataFactory(SgCierreAnioLectivoSede cierre) throws Exception {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("secciones", TypedTableModel.class);
        model.addColumn("preguntas", TypedTableModel.class);

        TypedTableModel secciones = new TypedTableModel();

        secciones.addColumn("grado", String.class);
        secciones.addColumn("seccion", String.class);
        secciones.addColumn("seccion_estado", String.class);
        secciones.addColumn("opcion", String.class);
        secciones.addColumn("asistencias", Integer.class);
        secciones.addColumn("inasistencias", Integer.class);
        secciones.addColumn("pm", Integer.class);
        secciones.addColumn("pf", Integer.class);
        secciones.addColumn("pt", Integer.class);
        secciones.addColumn("nm", Integer.class);
        secciones.addColumn("nf", Integer.class);
        secciones.addColumn("nt", Integer.class);
        secciones.addColumn("ppm", Integer.class);
        secciones.addColumn("ppf", Integer.class);
        secciones.addColumn("ppt", Integer.class);
        secciones.addColumn("stm", Integer.class);
        secciones.addColumn("stf", Integer.class);
        secciones.addColumn("stt", Integer.class);

        for (SgResumenCierreSeccion rs : cierre.getCalResumenSecciones()) {
            secciones.addRow(
                    rs.getRcsSeccion().getSecServicioEducativo().getSduGrado().getGraNombre(),
                    rs.getRcsSeccion().getNombreSeccion(),
                    rs.getRcsSeccion().getSecEstado().getText(),
                    rs.getRcsSeccion().getSecServicioEducativo().getSduOpcion() != null ? rs.getRcsSeccion().getSecServicioEducativo().getSduOpcion().getOpcNombre() : "",
                    rs.getRcsAsistencias(),
                    rs.getRcsInasistencias(),
                    rs.getRcsPromovidosMasc(),
                    rs.getRcsPromovidosFem(),
                    rs.getRcsPromovidosMasc() + rs.getRcsPromovidosFem(),
                    rs.getRcsNoPromovidosMasc(),
                    rs.getRcsNoPromovidosFem(),
                    rs.getRcsNoPromovidosMasc() + rs.getRcsNoPromovidosFem(),
                    rs.getRcsPromocionesPendientesMasc(),
                    rs.getRcsPromocionesPendientesFem(),
                    rs.getRcsPromocionesPendientesMasc() + rs.getRcsPromocionesPendientesFem(),
                    rs.getRcsSolicitudesTitulosMasc(),
                    rs.getRcsSolicitudesTitulosFem(),
                    rs.getRcsSolicitudesTitulosMasc() + rs.getRcsSolicitudesTitulosFem()
            );

        }

        TypedTableModel preguntas = new TypedTableModel();
        preguntas.addColumn("pregunta", String.class);
        preguntas.addColumn("respuesta", String.class);

        for (SgRelPreguntaCierreAnioSede rp : cierre.getCalRelPreguntaCierreAnio()) {
            preguntas.addRow(
                    rp.getRpcPreguntaCierreAnio().getPcaPregunta(),
                    rp.getRpcPreguntaValidada() == null ? "No se sabe" : rp.getRpcPreguntaValidada() ? "Sí" : "No"
            );

        }

        model.addRow(secciones, preguntas);

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

}
