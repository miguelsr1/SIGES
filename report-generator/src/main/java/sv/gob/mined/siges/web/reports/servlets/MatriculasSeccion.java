/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.servlets;

import java.net.URL;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgDireccion;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirma;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "matriculasSeccionesServlet ", urlPatterns = "/wa/matriculasSecciones")
public class MatriculasSeccion extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(MatriculasSeccion.class.getName());

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SeccionRestClient seccionClient;

    @Inject
    private SelloFirmaRestClient selloFirmaClient;

    @Inject
    private PlantillaRestClient plantillaClient;

    @Inject
    private MatriculaRestClient matriculaClient;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ClassicEngineBoot.getInstance().start();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_MATRICULA_SECCION)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }

            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_MATRICULA_SECCION)) {
                ServletUtils.printMessage("Matriculas sección", "Permisos insuficientes", response);
                return;
            }

            FiltroSeccion filSec = new FiltroSeccion();
            filSec.setIncluirCampos(new String[]{
                "secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                "secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                "secServicioEducativo.sduSede.sedDireccion.dirDireccion",
                "secServicioEducativo.sduSede.sedPk",
                "secServicioEducativo.sduSede.sedNombre",
                "secServicioEducativo.sduSede.sedCodigo",
                "secServicioEducativo.sduSede.sedTipo",
                "secServicioEducativo.sduSede.sedTelefono",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrganizacionCurricular.ocuPk",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                "secServicioEducativo.sduOpcion.opcNombre",
                "secServicioEducativo.sduProgramaEducativo.pedNombre",
                "secServicioEducativo.sduGrado.graNombre",
                "secAnioLectivo.aleAnio",
                "secNombre",
                "secJornadaLaboral.jlaNombre",
                "secPlanEstudio.pesNombre"
            });

            FiltroMatricula fm = new FiltroMatricula();
            fm.setMatRetirada(Boolean.FALSE);
            fm.setIncluirCampos(new String[]{
                "matSeccion.secPk",
                "matProvisional",
                "matValidacionAcademica",
                "matObservacioneProvisional",
                "matEstudiante.estPersona.perNie",
                "matEstudiante.estPersona.perPrimerNombre",
                "matEstudiante.estPersona.perSegundoNombre",
                "matEstudiante.estPersona.perPrimerApellido",
                "matEstudiante.estPersona.perSegundoApellido"});
            fm.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellidoBusqueda", "matEstudiante.estPersona.perSegundoApellidoBusqueda",
                "matEstudiante.estPersona.perPrimerNombreBusqueda", "matEstudiante.estPersona.perSegundoNombreBusqueda"});
            fm.setAscending(new boolean[]{true, true, true, true});

            //Actualmente funciona solamente para una sección, pero podría modificarse fácilmente para un conjunto de secciones.
            if (request.getParameter("seccionId") != null) {
                Long seccionId = Long.parseLong(request.getParameter("seccionId"));
                fm.setSecPk(seccionId);
                filSec.setSecPk(seccionId);
            } else {
                return;
            }

            List<SgSeccion> secciones = seccionClient.buscar(filSec);
            if (secciones.isEmpty()) {
                ServletUtils.printMessage("Matriculas sección", Mensajes.obtenerMensaje(Mensajes.SECCION_NO_ENCONTRADA), response);
                return;
            }
            
            SgSeccion sec = secciones.get(0);
            
            
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_MATRICULAS_SECCIONES_CODIGO,
                    sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivOrganizacionCurricular().getOcuPk());
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    ServletUtils.printMessage("Título estudiante", "Plantilla " + Constantes.PLANTILLA_MATRICULAS_SECCIONES_CODIGO + " inexistente.", response);
                    return;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("matriculas_seccion.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            
            
            List<SgMatricula> matriculas = matriculaClient.buscar(fm);

            Collator primaryCollator = Collator.getInstance(new Locale("es"));
            primaryCollator.setStrength(Collator.SECONDARY);
            Collections.sort(matriculas,(o1,o2)->primaryCollator.compare(o1.getMatEstudiante().getEstPersona().getPerPrimerApellido(), o2.getMatEstudiante().getEstPersona().getPerPrimerApellido()));
            
            HashMap<SgSeccion, SgSelloFirma> sellosFirmaDirector = new HashMap<>();
            for (SgSeccion s : secciones) {
                FiltroSelloFirma filtroSelloFirma = new FiltroSelloFirma();
                filtroSelloFirma.setIncluirCampos(new String[]{
                    "sfiTipoRepresentante.treCodigo",
                    "sfiFirmaSello.achPk",
                    "sfiPersona.perNie",
                    "sfiPersona.perPrimerNombre", "sfiPersona.perSegundoNombre",
                    "sfiPersona.perPrimerApellido", "sfiPersona.perSegundoApellido",
                    "sfiPersona.perFechaNacimiento"
                });
                filtroSelloFirma.setSfiHabilitado(Boolean.TRUE);
                filtroSelloFirma.setSedPk(s.getSecServicioEducativo().getSduSede().getSedPk());
                filtroSelloFirma.setSfiTipoRepresentanteCodigo(Constantes.TIPO_REPRESENTANTE_DIRECTOR_CODIGO);
                List<SgSelloFirma> sellos = selloFirmaClient.buscar(filtroSelloFirma);
                if (!sellos.isEmpty()) {
                    sellosFirmaDirector.put(s, sellos.get(0));
                }
            }

            report.setDataFactory(this.getDataFactory(matriculas, secciones, sellosFirmaDirector));

            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(report, response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage("Matriculas sección", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private DataFactory getDataFactory(List<SgMatricula> matriculas, List<SgSeccion> secciones, HashMap<SgSeccion, SgSelloFirma> sellosFirmaDirector) throws Exception {

        //Preprocesamiento. Agrupar matriculas por sección
        HashMap<SgSeccion, List<SgMatricula>> secMats = new HashMap<>();
        for (SgMatricula mat : matriculas) {
            secMats.computeIfAbsent(mat.getMatSeccion(), s -> new ArrayList<>()).add(mat);
        }

        TypedTableModel model = new TypedTableModel();
        model.addColumn("direccion", String.class);
        model.addColumn("sede", String.class);
        model.addColumn("plan_estudio", String.class);
        model.addColumn("anio_lectivo", String.class);
        model.addColumn("seccion", String.class);
        model.addColumn("telefono", String.class);
        model.addColumn("servicio", String.class);
        model.addColumn("estudiantes", TypedTableModel.class);
        model.addColumn("firma_director_path", String.class);
        model.addColumn("nombre_director", String.class);

        for (SgSeccion seccion : secciones) {
            SgDireccion dirSede = seccion.getSecServicioEducativo().getSduSede().getSedDireccion();
            String telefono = seccion.getSecServicioEducativo().getSduSede().getSedTelefono();
            SgSelloFirma sfDirector = sellosFirmaDirector.get(seccion);

            model.addRow(dirSede != null ? dirSede.getDirCompleta() : "",
                    seccion.getSecServicioEducativo().getSduSede().getSedCodigoNombre(),
                    seccion.getSecPlanEstudio().getPesNombre(),
                    seccion.getSecAnioLectivo().getAleAnio(),
                    seccion.getNombreSeccion(),
                    telefono != null ? telefono : "",
                    seccion.getSecServicioEducativo().getSduNombre(),
                    this.getMatriculasTableModel(secMats.get(seccion)),
                    (sfDirector != null && sfDirector.getSfiFirmaSello() != null) ? (this.basePath + SofisFileUtils.getPathFromPk(sfDirector.getSfiFirmaSello().getAchPk())) : null,
                    (sfDirector != null && sfDirector.getSfiPersona() != null) ? sfDirector.getSfiPersona().getPerNombreCompleto() : ""
            );
        }

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    private TypedTableModel getMatriculasTableModel(List<SgMatricula> matriculas) throws Exception {

        if (matriculas == null) {
            matriculas = new ArrayList<>();
        }

        TypedTableModel a = new TypedTableModel();
        a.addColumn("nro", String.class);
        a.addColumn("nie", String.class);
        a.addColumn("nombre_estudiante", String.class);
        a.addColumn("estado", String.class);
        a.addColumn("observacion", String.class);
        a.addColumn("validada", String.class);

        Integer count = 1;
        for (SgMatricula mat : matriculas) {

            Object[] row = new Object[7];
            Arrays.fill(row, "");

            row[0] = count;
            row[1] = mat.getMatEstudiante().getEstPersona().getPerNie() != null ? mat.getMatEstudiante().getEstPersona().getPerNie() : "";
            row[2] = mat.getMatEstudiante().getEstPersona().getPerNombreCompleto();
            if (BooleanUtils.isTrue(mat.getMatProvisional())) {
                row[3] = "Provisional";
            } else {
                row[3] = "Matriculado";
            }
            if (BooleanUtils.isTrue(mat.getMatProvisional()) && mat.getMatObservacioneProvisional() != null) {
                row[4] = mat.getMatObservacioneProvisional();
            }
            row[5] = BooleanUtils.isTrue(mat.getMatValidacionAcademica()) ? "Sí" : "No";

            a.addRow(row);
            count++;
        }
        return a;
    }
}
