/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSede;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.centros_educativos.SedeRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;
import sv.gob.mined.siges.web.restclient.SeguridadRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;


@Named
@SessionScoped
public class SessionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SessionBean.class.getName());
    
    @Inject
    private SeguridadRestClient seguridadClient;
    
    @Inject
    private UsuarioRestClient usuarioClient;

    @Inject
    private SedeRestClient sedesClient;

    private String piePagina = "";
    private String nombreAplicacion = "Sistema de Información para la Gestión Educativa Salvadoreña";
    private Locale locale;
    private String userToken;
    private LocalDateTime userTokenGeneratedDate;
    private Integer userTokenExpirationTimeMinutes = 20;
    private String userIp;
    private Principal user;
    private List<String> operaciones;
    private SgUsuario entidadUsuario;
    private SgSede sedeDefecto;
    private Boolean seCargoSedeDefecto = Boolean.FALSE;
    private HashMap<Long, SgSede> sedes = new HashMap<>();

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

    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    public Principal getUser() {
        return user;
    }

    public void setNombreAplicacion(String nombreAplicacion) {
        this.nombreAplicacion = nombreAplicacion;
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
    
    public SgUsuario getEntidadUsuario() {      
        return entidadUsuario;
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

    public void setUser(Principal user) throws Exception {
        this.user = user;
        cargarOperaciones();
        generarTokenUsuario();
        entidadUsuario = usuarioClient.obtenerPorCodigo(this.getUser().getName());
    }
    

    public SgSede getSedeDefecto() {
        if (!this.seCargoSedeDefecto) {
            cargarSedePorDefecto();
            seCargoSedeDefecto = Boolean.TRUE;
        }
        return sedeDefecto;
    }

    public void cargarSedePorDefecto() {
        try {
            List<SgSede> sedes = buscarSedes(null);
            if (sedes != null && sedes.size() == 1) {
                sedeDefecto = sedes.get(0);
                this.sedes.put(sedeDefecto.getSedPk(), sedeDefecto);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgSede> buscarSedes(Long sedPk) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedPk(sedPk);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(2L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo",
                "sedNombre",
                "sedTipo",
                "sedVersion",
                "sedDireccion.dirDepartamento.depPk",
                "sedDireccion.dirDepartamento.depNombre",
                "sedDireccion.dirDepartamento.depVersion"});
            return sedesClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
}
