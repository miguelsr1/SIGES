/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SeguridadRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@SessionScoped
public class SessionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SessionBean.class.getName());

    @Inject
    private SeguridadRestClient seguridadClient;

    @Inject
    private UsuarioRestClient usuarioClient;

    private String piePagina = "";
    private Locale locale;
    private String userToken;
    private LocalDateTime userTokenGeneratedDate;
    private Integer userTokenExpirationTimeMinutes = 20;
    private String userIp;
    private Principal user;
    private List<String> operaciones;
    private SgUsuario entidadUsuario;

    public SessionBean() {
    }

    @PostConstruct
    private void init() {
        try {
            locale = new Locale("es");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
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
        this.userToken = JWTUtils.generarToken(this.user.getName(), this.userIp, "/privateKey.pem", operaciones, userTokenExpirationTimeMinutes);
        this.userTokenGeneratedDate = LocalDateTime.now();
    }

    public SgUsuario getEntidadUsuario() {
        return entidadUsuario;
    }

    public void handleTermsChange() {
        try {
            if (BooleanUtils.isTrue(entidadUsuario.getUsuAceptaTerminos())){
                entidadUsuario = usuarioClient.aceptarMisTerminos();
            } else {
                entidadUsuario = usuarioClient.rechazarMisTerminos();
            }
            if (BooleanUtils.isTrue(entidadUsuario.getUsuAceptaTerminos())){
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.redirect("/pp/inicio");
            } else {
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public String getPiePagina() {
        return piePagina;
    }

    public void setPiePagina(String piePagina) {
        this.piePagina = piePagina;
    }

    
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String salir() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        try { 
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ec.getRequest();
            ec.redirect(
                    System.getProperty("casServerLoginUrl")
                            .replace("/login", "/logout")
                            .concat("?service=" + System.getProperty("service.welcome.baseUrl") + "/pp/inicio.xhtml"));
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public void cargarOperaciones() {
        operaciones = new ArrayList<>();
        operaciones.add(ConstantesOperaciones.AUTENTICADO);
        try {
            List<Long> categoriasOperacionPk = new ArrayList<>();
            categoriasOperacionPk.add(Constantes.CATEGORIA_OPERACION_SEGURIDAD_PK);
            categoriasOperacionPk.add(Constantes.CATEGORIA_OPERACION_WELCOME_PK);
            operaciones.addAll(seguridadClient.obtenerOperacionesPorCodigoUsuario(this.user.getName(), categoriasOperacionPk));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public SeguridadRestClient getSeguridadClient() {
        return seguridadClient;
    }

    public void setSeguridadClient(SeguridadRestClient seguridadClient) {
        this.seguridadClient = seguridadClient;
    }

    public UsuarioRestClient getUsuarioClient() {
        return usuarioClient;
    }

    public void setUsuarioClient(UsuarioRestClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    public LocalDateTime getUserTokenGeneratedDate() {
        return userTokenGeneratedDate;
    }

    public void setUserTokenGeneratedDate(LocalDateTime userTokenGeneratedDate) {
        this.userTokenGeneratedDate = userTokenGeneratedDate;
    }

    public Integer getUserTokenExpirationTimeMinutes() {
        return userTokenExpirationTimeMinutes;
    }

    public void setUserTokenExpirationTimeMinutes(Integer userTokenExpirationTimeMinutes) {
        this.userTokenExpirationTimeMinutes = userTokenExpirationTimeMinutes;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
    
    public Principal getUser() {
        return user;
    }
    
    public void setUser(Principal user) throws Exception {
        this.user = user;
        cargarOperaciones();
        generarTokenUsuario();
        entidadUsuario = usuarioClient.obtenerPorCodigo(this.user.getName());
    }

    public List<String> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(List<String> operaciones) {
        this.operaciones = operaciones;
    }

}
