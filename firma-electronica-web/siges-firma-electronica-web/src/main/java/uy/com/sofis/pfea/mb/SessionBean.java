package uy.com.sofis.pfea.mb;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import uy.com.sofis.pfea.sb.ConfiguracionSB;

/**
 * @author Sofis Solutions
 */
@Named
@SessionScoped
public class SessionBean implements Serializable {

    private static final Logger logger = Logger.getLogger(SessionBean.class.getName());

    @EJB
    ConfiguracionSB configuracionSB;

    private final static String patternDate = "dd/MM/yyyy";

    
    private Principal user;
    private Locale locale;
    private Locale localeNumber;

    public SessionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            locale = new Locale("es");
            localeNumber = new Locale("es_SV");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public Principal getUser() {
        return user;
    }

    public void setUser(Principal user) {
        this.user = user;
    }
   
    public String getPatternDate() {
        return patternDate;
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

    public Locale getLocaleNumber() {
        return localeNumber;
    }

    public void setLocaleNumber(Locale localeNumber) {
        this.localeNumber = localeNumber;
    }

    public String salir() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(
                    System.getProperty("casServerLoginUrl")
                            .replace("/login", "/logout")
                            .concat("?service=" + System.getProperty("service.welcome.baseUrl") + "/pp/inicio.xhtml"));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

}
