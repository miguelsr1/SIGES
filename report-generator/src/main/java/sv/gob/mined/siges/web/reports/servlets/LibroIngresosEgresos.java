/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.reports.generator.LibroIngresosEgresosGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "libroIngresosEgresosServlet", urlPatterns = "/wa/libroIngresosEgresos")
public class LibroIngresosEgresos extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(LibroIngresosEgresos.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private LibroIngresosEgresosGenerator reportGenerator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ClassicEngineBoot.getInstance().start();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_LIBRO_INGRESOS_EGRESOS)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }

            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_LIBRO_INGRESOS_EGRESOS)) {
                ServletUtils.printMessage("Libro de Ingresos y Egresos", "Permisos insuficientes", response);
                return;
            }

            Long sedeId = null;
            Integer anio = null;
            Long movTraId = null;
            Long componente = null;
            Long subComponente = null;

            if (request.getParameter("ids") != null && request.getParameter("ida") != null && request.getParameter("idt") != null) {
                sedeId = new Long(request.getParameter("ids"));
                anio = new Integer(request.getParameter("ida"));
                movTraId = new Long(request.getParameter("idt"));
            }

            if (request.getParameter("idcom") != null && request.getParameter("idsubcom") != null) {
                componente = new Long(request.getParameter("idcom"));
                subComponente = new Long(request.getParameter("idsubcom"));
            }

            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(sedeId, anio, movTraId, componente, subComponente), response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage("Libro Ingresos Egresos.", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
