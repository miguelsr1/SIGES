/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import sv.gob.mined.siges.web.utilidades.JWTUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.restclient.SeguridadRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TokenGenerator implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TokenGenerator.class.getName());
    
    @Inject
    private SeguridadRestClient seguridadClient;
    
    private List<String> operaciones;
    private String usuario = "casuser"; 
    private String ip = "192.168.1.5";
    private Integer userTokenExpirationTimeMinutes = 20;
    private String token;
    
    public TokenGenerator() {
    }

    @PostConstruct
    private void init() {
        try {
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void cargarOperaciones() {
        operaciones = new ArrayList<>();
        operaciones.add(ConstantesOperaciones.AUTENTICADO);
        try {
            operaciones.addAll(seguridadClient.obtenerOperacionesPorCodigoUsuario(usuario));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    
    public void generarToken(){
        try {
            cargarOperaciones();
            token = JWTUtils.generarToken(usuario, ip, "/privateKey.pem", operaciones, userTokenExpirationTimeMinutes);
        } catch (Exception ex){
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getUserTokenExpirationTimeMinutes() {
        return userTokenExpirationTimeMinutes;
    }

    public void setUserTokenExpirationTimeMinutes(Integer userTokenExpirationTimeMinutes) {
        this.userTokenExpirationTimeMinutes = userTokenExpirationTimeMinutes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
        
}
