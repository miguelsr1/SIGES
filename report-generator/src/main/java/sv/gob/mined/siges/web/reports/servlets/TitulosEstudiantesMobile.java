/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.servlets;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jose4j.jwt.JwtClaims;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.reports.generator.TituloEstudianteGenerator;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "titulosEstudiantesMobileServlet ", urlPatterns = "/ma/titulosEstudiantes")
public class TitulosEstudiantesMobile extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(TitulosEstudiantesMobile.class.getName());

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;
    
    @Inject
    @ConfigProperty(name = "service.portal.baseUrl")
    private String portalURL;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private TituloEstudianteGenerator reportGenerator;
    
    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";

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
            
            String token = getBearerToken(request);
            JwtClaims claims =JWTUtils.validarToken(token, "/publicKey.pem", "SIGES", "SIGES");
            if (!claims.getStringListClaimValue("groups").contains(ConstantesOperaciones.GENERAR_TITULO_ESTUDIANTE)){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            sessionBean.setToken(token);
          
            String hash = request.getParameter("hashEstudiante");
            String estudianteImp = request.getParameter("estudianteImpresionId");
            String solicitudImp = request.getParameter("solicitudImp");            
            

            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(hash,estudianteImp,solicitudImp), response.getOutputStream());
            
        } catch (BusinessException be) {
            response.setStatus(201); //Aplicativo de impresion espera 201. TODO: cambiar por 422
            ServletUtils.printMessage("Titulo estudiante", be, response);
        } catch (Exception e) {
            response.setStatus(201); //Aplicativo de impresion espera 201. TODO: cambiar por 500
            ServletUtils.printMessage("Error general","Error general",response);
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    private String getBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER_KEY);
        if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
            return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
        }
        return null;
    }
}
