/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
@SessionScoped
public class SessionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SessionBean.class.getName());

    private String ambiente;     
    private Locale locale;
    private Principal user;
    
    public SessionBean() {
    }

    @PostConstruct
    private void init() {
        
        try {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            this.user = ec.getUserPrincipal();
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }
    
    public String getLanguage() {
        return locale.getLanguage();
    }
    
    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Principal getUser() {
        return user;
    }    
       
    
}
