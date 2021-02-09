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
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgAyuda;
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.restclient.AyudaRestClient;
import sv.gob.mined.siges.web.restclient.SeguridadRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;

@Named
@SessionScoped
public class SessionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SessionBean.class.getName());

    @Inject
    private SeguridadRestClient seguridadClient;

    @Inject
    private AyudaRestClient ayudaRestClient;

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

    public SgAyuda getAyuda(String codigo) {
        try {
            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setCodigoExacto(codigo);
            filtro.setHabilitado(Boolean.TRUE);
            List<SgAyuda> ayudas = ayudaRestClient.buscar(filtro);
            if (!ayudas.isEmpty()) {
                return ayudas.get(0);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public String getLanguage() {
        return this.getLocale().getLanguage();
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

    public Principal getUser() {
        return user;
    }

    public void setUser(Principal user) throws Exception {
        this.user = user;
        cargarOperaciones();
        generarTokenUsuario();
        entidadUsuario = usuarioClient.obtenerPorCodigo(this.getUser().getName());
    }

    public SgUsuario getEntidadUsuario() {
        return entidadUsuario;
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
            FacesContext.getCurrentInstance().getExternalContext().redirect(
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
            operaciones.addAll(seguridadClient.obtenerOperacionesPorCodigoUsuario(this.user.getName()));
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

    public List<String> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(List<String> operaciones) {
        this.operaciones = operaciones;
    }

}
