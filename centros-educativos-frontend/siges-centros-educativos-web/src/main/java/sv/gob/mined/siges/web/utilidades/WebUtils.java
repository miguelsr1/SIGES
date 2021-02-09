/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import javax.faces.context.FacesContext;

public class WebUtils {

    public static void putObjectOnFlashContext(String attName, Object obj) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put(attName, obj);
    }

    public static Object getFlashContext(String attName) {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(attName);
    }

    public static void keepMessages() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }
}
