/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package uy.com.sofis.pfea.utilidades;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JSFUtils {

    private static final Logger LOGGER = Logger.getLogger(JSFUtils.class.getName());
    public static final String STYLE_CLASS_DATO_CON_ERROR = "form-group-con-error";

    public static void mostrarMensaje(String clientId, String mensaje, FacesMessage.Severity sev) {
        FacesMessage msg = new FacesMessage(sev, mensaje, "");
        FacesContext.getCurrentInstance().addMessage(clientId, msg);
    }

    public static void mostrarMensajes(String clientId, List<String> mensajes, FacesMessage.Severity sev) {
        for (String mensaje : mensajes) {
            FacesMessage msg = new FacesMessage(sev, mensaje, "");
            FacesContext.getCurrentInstance().addMessage(clientId, msg);
        }
    }

    public static void agregarError(String clientId, String mensaje, String detail) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, detail));
    }

    public static void agregarInfo(String clientId, String mensaje, String detail) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, detail));
    }

    public static void agregarWarn(String clientId, String mensaje, String detail) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, mensaje, detail));
    }



    public static Object executeExpressionInElContext(Application application, ELContext elContext, String expression) {
        try {
            ExpressionFactory expressionFactory = application.getExpressionFactory();
            ValueExpression exp = expressionFactory.createValueExpression(elContext, expression, Object.class);
            return exp.getValue(elContext);
        } catch (Exception ex){
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
    
    public static void redirectNotFound(){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "");
            context.responseComplete();
        } catch (Exception ex){
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void redirectToIndex() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(externalContext.getRequestContextPath());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    public static void redirectToPage(String pag){
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(pag);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void refreshCurrentPage() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(((HttpServletRequest) externalContext.getRequest()).getRequestURI());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static String getRemoteAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress != null) {
            // cares only about the first IP if there is a list
            ipAddress = ipAddress.replaceFirst(",.*", "");
        } else {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

}
