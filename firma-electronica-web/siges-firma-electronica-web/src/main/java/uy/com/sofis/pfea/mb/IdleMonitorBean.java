/*
 * Sofis Solutions
 */
package uy.com.sofis.pfea.mb;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import uy.com.sofis.pfea.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class IdleMonitorBean implements Serializable {

    private Integer sessionTimeOut = 1800000; //30 minutos

    public void redirect() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        JSFUtils.redirectToIndex();
    }

    public Integer getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(Integer sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

}
