/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@SessionScoped
public class SessionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SessionBean.class.getName());

    @Inject
    private UsuarioRestClient usuarioClient;
    
    private String piePagina = "";
    private String ambiente;     
    private Locale locale;
    private Principal user;
    private String userToken;
    private LocalDateTime userTokenGeneratedDate;
    private String userIp;
    private Integer userTokenExpirationTimeMinutes = 20;
    private SgUsuario entidadUsuario;
    
    public SessionBean() {
    }

    @PostConstruct
    private void init() {
        try {
            locale = new Locale("es");       
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Produces
    @Named("userToken")
    public String getUserToken() {
        try {
            if (userTokenGeneratedDate == null || !userTokenGeneratedDate.plusMinutes(userTokenExpirationTimeMinutes - 2L).isAfter(LocalDateTime.now())) {
                LOGGER.log(Level.INFO, "Token cerca de expirar. Generando nuevo token. Usuario: " + this.user.getName());
                generarTokenUsuario();
            }
            return this.userToken;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
    
    public void generarTokenUsuario() throws Exception {
        this.userToken = JWTUtils.generarToken(this.user.getName(), this.userIp, "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO, ConstantesOperaciones.ACTUALIZAR_USUARIO}), userTokenExpirationTimeMinutes);
        this.userTokenGeneratedDate = LocalDateTime.now();
    }
    
    public SgUsuario getEntidadUsuario() {
        return entidadUsuario;
    }
    
    public String salir() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(
                    System.getProperty("casServerLoginUrl")
                            .replace("/login", "/logout")
                            .concat("?service=" + System.getProperty("service.welcome.baseUrl") + "/pp/inicio.xhtml"));
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
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
    
    public void setUser(Principal user) throws Exception {
        this.user = user;
        generarTokenUsuario();
        entidadUsuario = usuarioClient.obtenerPorCodigo(this.getUser().getName());
    }

    public Principal getUser() {
        return user;
    }   

    public String getPiePagina() {
        return piePagina;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
    
       
    
}
