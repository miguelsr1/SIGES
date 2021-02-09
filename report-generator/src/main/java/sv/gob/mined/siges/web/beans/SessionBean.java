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
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.OperationSecurity;
import sv.gob.mined.siges.web.restclient.SeguridadRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;

@Named
@SessionScoped
public class SessionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SessionBean.class.getName());

    @Inject
    private SeguridadRestClient seguridadClient;

    @Inject
    private UsuarioRestClient usuarioClient;
    
    private String timeZone = "GMT-6";
    private Locale locale;
    private String userToken;
    private LocalDateTime userTokenGeneratedDate;
    private Integer userTokenExpirationTimeMinutes = 20;
    private String userIp;
    private String userCode;
    private Set<String> operaciones;
    private SgUsuario entidadUsuario;
    
    private Principal user;
    private HashMap<String, List<OperationSecurity>> operacionesSeguridad = new HashMap<>();    


    public SessionBean() {
    }

    @PostConstruct
    public void init() {
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
            if (userCode != null && (userTokenGeneratedDate == null || !userTokenGeneratedDate.plusMinutes(userTokenExpirationTimeMinutes - 2L).isAfter(LocalDateTime.now()))) {
                LOGGER.log(Level.INFO, "Token cerca de expirar. Generando nuevo token. Usuario: " + userCode);
                generarTokenUsuario();
            }
            return this.userToken;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
    
    @Produces
    @Named("user")
    public String getUserCode() {
        return userCode;
    }

    public void generarTokenUsuario() throws Exception {
        this.userToken = JWTUtils.generarToken(this.user.getName(), this.userIp, "/privateKey.pem", new ArrayList<>(operaciones), userTokenExpirationTimeMinutes);
        this.userTokenGeneratedDate = LocalDateTime.now();
    }
    
    public void setUser(Principal user) throws Exception {
        this.userCode = user.getName();
        this.user = user;
        cargarOperaciones();
        generarTokenUsuario();
        entidadUsuario = usuarioClient.obtenerPorCodigo(user.getName());
    }
    
    //Utilizado por app. No setea usuario ni operaciones
    public void setToken(String token) throws Exception {
        this.userToken = token;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
    public void cargarOperaciones() {    
        operaciones = new HashSet<>();
        operaciones.add(ConstantesOperaciones.AUTENTICADO);
        try {
            List<Long> categoriasOp = new ArrayList<>();
            categoriasOp.add(1L); //Centros
            categoriasOp.add(3L); //Seguridad
            categoriasOp.add(4L); //Acceso a servicios
            categoriasOp.add(5L); //Activo fijo TODO:
            categoriasOp.add(12L); //Finanzas TODO:
            categoriasOp.add(13L);//Títulos
            List<OperationSecurity> ops = seguridadClient.obtenerOperacionesSeguridad(this.user.getName(), categoriasOp);
            for (OperationSecurity o : ops) {
                operacionesSeguridad.computeIfAbsent(o.getOperation(), s -> new ArrayList<>()).add(o);
            }
            operaciones.addAll(operacionesSeguridad.keySet());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.user, ex), ex);
        }    
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

    public SgUsuario getEntidadUsuario() {
        return entidadUsuario;
    }

    public void setEntidadUsuario(SgUsuario entidadUsuario) {
        this.entidadUsuario = entidadUsuario;
    }
}
