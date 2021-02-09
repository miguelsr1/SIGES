/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import sv.gob.mined.siges.web.mensajes.Mensajes;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.PrimeFaces;
import org.primefaces.component.outputpanel.OutputPanel;
import sv.gob.mined.siges.web.excepciones.BusinessError;

public class JSFUtils {

    private static final Logger LOGGER = Logger.getLogger(JSFUtils.class.getName());
    public static final String STYLE_CLASS_DATO_CON_ERROR = "form-group-con-error";

    public static void mostrarMensaje(String clientId, String mensaje, FacesMessage.Severity sev) {
        limpiarMensajesError();
        FacesMessage msg = new FacesMessage(sev, mensaje, "");
        FacesContext.getCurrentInstance().addMessage(clientId, msg);
    }

    public static void mostrarMensajes(String clientId, List<String> mensajes, FacesMessage.Severity sev) {
        limpiarMensajesError();
        for (String mensaje : mensajes) {
            FacesMessage msg = new FacesMessage(sev, mensaje, "");
            FacesContext.getCurrentInstance().addMessage(clientId, msg);
        }
    }

    public static void agregarError(String clientId, String mensaje, String detail) {
        limpiarMensajesError();
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, detail));
        setFocus(clientId);
    }

    public static void agregarInfo(String clientId, String mensaje, String detail) {
        limpiarMensajesError();
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, detail));
        setFocus(clientId);
    }

    public static void agregarWarn(String clientId, String mensaje, String detail) {
        limpiarMensajesError();
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, mensaje, detail));
        setFocus(clientId);
    }

    public static void agregarMensajes(String globalId, FacesMessage.Severity sev, List<BusinessError> errores) {
        limpiarMensajesError();
        for (int i = 0; i < errores.size(); i++) {
            BusinessError error = errores.get(i);
            String mensaje = error.getMensaje();
            mensaje = findMessage(mensaje);
            FacesMessage msg = new FacesMessage(sev, mensaje, "");
            FacesContext.getCurrentInstance().addMessage(globalId, msg);

            if (error.getCampo() != null) {
                String campo = error.getCampo();
                addErrorStyle("form:input_" + campo); //paiNombre -> form:input_paiNombre
            }
        }
        setFocus(globalId);
    }

    public static void agregarMensajes(String clientId, List<String> summary) {
        for (String mensaje : summary) {
            agregarMsg(clientId, mensaje, "");
        }
        setFocus(clientId);
    }

    public static void setFocus(String id) {
        try {
            PrimeFaces.current().executeScript("document.getElementById('" + id + "').focus();");
            id = id.replace(":", "\\\\:"); //Para jQuery
            PrimeFaces.current().executeScript("scrollToDivIfNotVisible('" + id + "')");
        } catch (Exception ex) {
            PrimeFaces.current().executeScript("scrollToTop()");
        }
    }

    /**
     * Dado el summary controla si el comienzo empieza con 'ERROR', 'WARN' o
     * 'INFO' y despliega el mensaje según uno de estas severidades.
     *
     * @param clientId String
     * @param summary String
     * @param detail String
     */
    public static void agregarMsg(String clientId, String summary, String detail) {
        String[] msgSplit = summary.split("_");
        String mensaje = findMessage(summary);

        if (msgSplit[0].equalsIgnoreCase("ERROR")) {
            agregarError(clientId, mensaje, detail);
        } else if (msgSplit[0].equalsIgnoreCase("WARN")) {
            agregarWarn(clientId, mensaje, detail);
        } else {
            agregarInfo(clientId, mensaje, detail);
        }
    }

    public static void removerMensages() {
        Iterator<FacesMessage> msgIterator = FacesContext.getCurrentInstance().getMessages();
        while (msgIterator.hasNext()) {
            msgIterator.next();
            msgIterator.remove();
        }
    }

    /**
     * Si el labelKey es una clave en Labels retorna el texto, si no retorna
     * labelKey.
     *
     * @param labelKey String
     * @return String
     */
    private static String findMessage(String labelKey) {
        return Mensajes.obtenerMensaje(labelKey);
    }

    public static void addErrorStyle(String idComponente) {
        //Ver si se puede poner la clase al contenedor (el parent inmediato o el siguiente debe ser un outputPanel
        //o panelGroup con la clase form-group
        try {
            if (idComponente != null && idComponente.trim().length() > 0) {
                UIComponent comp = FacesContext.getCurrentInstance().getViewRoot().findComponent(idComponente);
                if (comp != null) {
                    comp = comp.getParent();
                    if (comp != null) {
                        comp = comp.getParent();
                        if (comp != null && comp instanceof OutputPanel) {
                            OutputPanel panel = (OutputPanel) comp;
                            if (panel.getStyleClass() != null && panel.getStyleClass().contains("form-group")) {
                                addErrorSytleClassToFormGroup(panel);
                            }
                        } else if (comp instanceof HtmlPanelGroup) {
                            HtmlPanelGroup panel = (HtmlPanelGroup) comp;
                            if (panel.getStyleClass() != null && panel.getStyleClass().contains("form-group")) {
                                addErrorSytleClassToFormGroup(panel);
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public static void limpiarMensajesError() {
        for (UIComponent comp : FacesContext.getCurrentInstance().getViewRoot().getChildren()) {
            limpiarMensajeError(comp);
        }
    }

    public static void limpiarMensajeError(UIComponent comp) {
        if (comp instanceof OutputPanel) {
            OutputPanel panel = (OutputPanel) comp;
            if (panel.getStyleClass() != null && panel.getStyleClass().contains(STYLE_CLASS_DATO_CON_ERROR)) {
                panel.setStyleClass(panel.getStyleClass().replace(STYLE_CLASS_DATO_CON_ERROR, ""));
            }
        } else if (comp instanceof HtmlPanelGroup) {
            HtmlPanelGroup panel = (HtmlPanelGroup) comp;
            if (panel.getStyleClass() != null && panel.getStyleClass().contains(STYLE_CLASS_DATO_CON_ERROR)) {
                panel.setStyleClass(panel.getStyleClass().replace(STYLE_CLASS_DATO_CON_ERROR, ""));
            }
        }

        if (comp.getChildCount() > 0) {
            for (UIComponent comp1 : comp.getChildren()) {
                limpiarMensajeError(comp1);
            }
        }
    }

    public static void addErrorSytleClassToFormGroup(OutputPanel panel) {
        String styleClass = panel.getStyleClass();
        if (!styleClass.contains(STYLE_CLASS_DATO_CON_ERROR)) {
            styleClass = styleClass + " " + STYLE_CLASS_DATO_CON_ERROR;
            panel.setStyleClass(styleClass);
        }
    }

    public static void addErrorSytleClassToFormGroup(HtmlPanelGroup panel) {
        String styleClass = panel.getStyleClass();
        if (!styleClass.contains(STYLE_CLASS_DATO_CON_ERROR)) {
            styleClass = styleClass + " " + STYLE_CLASS_DATO_CON_ERROR;
            panel.setStyleClass(styleClass);
        }
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
