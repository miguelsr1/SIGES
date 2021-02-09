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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.web.restclient.SeguridadRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.OperationSecurity;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
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
    private CatalogosRestClient catalogoClient;

    @Inject
    private SedeRestClient sedesClient;

            
    private String ambiente;
    private String piePagina = "";
    private String timeZone = "GMT-6";
    private Locale locale;
    private String userToken;
    private LocalDateTime userTokenGeneratedDate;
    private Integer userTokenExpirationTimeMinutes = 20;
    private String userIp;
    private Principal user;
    private SgUsuario entidadUsuario;
    private Locale localeNumber;

    private Set<String> operaciones;
    private HashMap<String, List<OperationSecurity>> operacionesSeguridad = new HashMap<>();
    private SgSede sedeDefecto;
    private Boolean seCargoSedeDefecto = Boolean.FALSE;
    
    public SessionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            locale = new Locale("es");
            localeNumber = new Locale("es_SV");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargarSedePorDefecto() {
        try {
            List<SgSede> sedes = buscarSedes(null);
            if (sedes != null && sedes.size() == 1) {
                sedeDefecto = sedes.get(0);
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
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
        }
        return null;
    }

    @Produces
    @Named("user")
    public Principal getUser() {
        return user;
    }

    public void generarTokenUsuario() throws Exception {
        this.userToken = JWTUtils.generarToken(this.user.getName(), this.userIp, "/privateKey.pem", new ArrayList<>(operaciones), userTokenExpirationTimeMinutes);
        this.userTokenGeneratedDate = LocalDateTime.now();
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

    public void setUser(Principal user) throws Exception {
        this.user = user;
        cargarOperaciones();
        generarTokenUsuario();
        entidadUsuario = usuarioClient.obtenerPorCodigo(this.getUser().getName());
    }

    public String getPiePagina() {
        return piePagina;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
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

    public Locale getLocaleNumber() {
        return localeNumber;
    }

    public void setLocaleNumber(Locale localeNumber) {
        this.localeNumber = localeNumber;
    }


    public Boolean getSeCargoSedeDefecto() {
        return seCargoSedeDefecto;
    }

    public void setSeCargoSedeDefecto(Boolean seCargoSedeDefecto) {
        this.seCargoSedeDefecto = seCargoSedeDefecto;
    }


    public String salir() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(
                    System.getProperty("casServerLoginUrl")
                            .replace("/login", "/logout")
                            .concat("?service=" + System.getProperty("service.welcome.baseUrl") + "/pp/inicio.xhtml"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
        }
        return null;
    }

 
        public void cargarOperaciones() {
        operaciones = new HashSet<>();
        operaciones.add(ConstantesOperaciones.AUTENTICADO);
        try {
            List<Long> categoriasOp = new ArrayList<>();            
            categoriasOp.add(1L); //Centros
            categoriasOp.add(4L); //Acceso a servicios
            categoriasOp.add(8L); //Registro historico
            List<OperationSecurity> ops = seguridadClient.obtenerOperacionesSeguridad(this.user.getName(), categoriasOp);
            for (OperationSecurity o : ops) {
                operacionesSeguridad.computeIfAbsent(o.getOperation(), s -> new ArrayList<>()).add(o);
            }
            operaciones.addAll(operacionesSeguridad.keySet());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
        }
    }

    public SgUsuario getEntidadUsuario() {
        return entidadUsuario;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Set<String> getOperaciones() {
        return operaciones;
    }

    public SgSede getSedeDefecto() {
        if (!this.seCargoSedeDefecto) {
            cargarSedePorDefecto();
            seCargoSedeDefecto = Boolean.TRUE;
        }
        return sedeDefecto;
    }
      

}
