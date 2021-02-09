/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.servlets;

import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import sv.gob.mined.siges.web.dto.SgAllegado;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "carnetEstudiantesServlet ", urlPatterns = "/wa/carnetEstudiantes")
public class CarnetEstudiantes extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CarnetEstudiantes.class.getName());

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private EstudianteRestClient estudianteClient;

    @Inject
    private SeccionRestClient seccionClient;

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

            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_CARNET_ESTUDIANTE)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }

            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_CARNET_ESTUDIANTE)) {
                ServletUtils.printMessage("Carnet estudiante", "Permisos insuficientes", response);
                return;
            }

            FiltroSeccion filSec = new FiltroSeccion();
            filSec.setIncluirCampos(new String[]{"secNombre", "secAnioLectivo.aleAnio",
            "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrganizacionCurricular.ocuPk"});
            
            
            FiltroMatricula filtroMat = new FiltroMatricula();
            filtroMat.setMatRetirada(Boolean.FALSE);
            filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", 
                "matEstudiante.estPk",
                "matEstudiante.estPersona.perPk", 
                "matEstudiante.estPersona.perPrimerNombre", 
                "matEstudiante.estPersona.perSegundoNombre", 
                "matEstudiante.estPersona.perNombreBusqueda",
                "matEstudiante.estPersona.perPrimerApellido", 
                "matEstudiante.estPersona.perSegundoApellido", 
                "matEstudiante.estPersona.perFechaNacimiento", 
                "matEstudiante.estPersona.perFoto.achPk",
                "matEstudiante.estVersion"});
            filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellidoBusqueda", 
                "matEstudiante.estPersona.perSegundoApellidoBusqueda", 
                "matEstudiante.estPersona.perPrimerNombreBusqueda", 
                "matEstudiante.estPersona.perSegundoNombreBusqueda"});
            filtroMat.setAscending(new boolean[]{true, true, true, true});
            filtroMat.setIncluirResponsableOContactoEmergencia(Boolean.TRUE);
            

            if (request.getParameter("seccionId") != null) {
                Long seccionId = Long.parseLong(request.getParameter("seccionId"));
                filtroMat.setSecPk(seccionId);
                filSec.setSecPk(seccionId);
            } else {
                return;
            }
            if (request.getParameter("estudianteId") != null) {
                Long estudianteId = Long.parseLong(request.getParameter("estudianteId"));
                filtroMat.setMatEstudiantePk(estudianteId);
            }

            List<SgSeccion> secs = seccionClient.buscar(filSec);
            if (secs.isEmpty()) {
                ServletUtils.printMessage("Carnet estudiante", Mensajes.obtenerMensaje(Mensajes.SECCION_NO_ENCONTRADA), response);
                return;
            }
            
            SgSeccion sec = secs.get(0);
            
             // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CARNET_ESTUDIANTE_CODIGO,
                        sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivOrganizacionCurricular().getOcuPk());
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    ServletUtils.printMessage("Carnet estudiante", "Plantilla " + Constantes.PLANTILLA_CARNET_ESTUDIANTE_CODIGO + " inexistente.", response);
                    return;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("carnet_estudiante.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            
            List<SgMatricula> matriculas = matriculaClient.buscar(filtroMat);
            List<SgEstudiante> estudiantes = matriculas.stream().map(c -> c.getMatEstudiante()).collect(Collectors.toList());
            if (estudiantes.isEmpty()) {
                ServletUtils.printMessage("Carnet estudiante", Mensajes.obtenerMensaje(Mensajes.SECCION_SIN_ESTUDIANTES), response);
                return;
            }

            report.setDataFactory(this.getDataFactory(estudiantes, sec));

            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(report, response.getOutputStream());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private DataFactory getDataFactory(List<SgEstudiante> estudiantes, SgSeccion seccion) {
        TypedTableModel model = new TypedTableModel();
        model.addColumn("nie", Long.class);
        model.addColumn("nombre", String.class);
        model.addColumn("responsable", String.class);
        model.addColumn("contacto_emergencia", String.class);
        model.addColumn("anio_lectivo", Integer.class);
        model.addColumn("foto_estudiante_path", String.class);
        for (SgEstudiante est : estudiantes) {
            String nombreResponsable = null;
            String nombreContactoEmergencia = null;
            if(est.getEstPersona().getPerAllegados()!=null){
                for (SgAllegado all : est.getEstPersona().getPerAllegados()) {
                    if (BooleanUtils.isTrue(all.getAllReferente())) {
                        nombreResponsable = all.getAllPersona().getPerNombreCompleto();
                    }
                    if (BooleanUtils.isTrue(all.getAllContactoEmergencia())) {
                        nombreContactoEmergencia = all.getAllPersona().getPerNombreCompleto();
                    }
                }
            }
            if (nombreResponsable != null) {
                nombreContactoEmergencia = null;
            }
            model.addRow(est.getEstPersona().getPerNie(),
                    est.getEstPersona().getPerNombreCompleto(),
                    nombreResponsable,
                    nombreContactoEmergencia,
                    seccion.getSecAnioLectivo().getAleAnio(),
                    est.getEstPersona().getPerFoto() != null ? (this.basePath + SofisFileUtils.getPathFromPk(est.getEstPersona().getPerFoto().getAchPk())) : null);
        }
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }
}
