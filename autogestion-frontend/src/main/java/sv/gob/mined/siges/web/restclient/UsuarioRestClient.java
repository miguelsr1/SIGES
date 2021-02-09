/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.web.dto.UserChangePassword;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUsuario;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 5000L)
public class UsuarioRestClient implements Serializable {

    @Inject
    private RestClient restClient;

    private Client client = null;

    @PostConstruct
    public void init() {
        client = RestClient.getClient();
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            client.close();
        }
    }

    public UsuarioRestClient() {
    }

    public void cambiarpassword(String codigoUsuario, String passwordActual, String passwordNueva) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (codigoUsuario == null || passwordActual == null || passwordNueva == null) {
            return;
        }
        UserChangePassword ucp = new UserChangePassword();
        ucp.setPasswordActual(passwordActual);
        ucp.setPasswordNueva(passwordNueva);
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SEGURIDAD, "v1/usuarios/cambiarpassword");
        webTarget = webTarget.path(codigoUsuario);
        restClient.invokePut(webTarget, ucp, null);
    }

// Método no existe más en backend
//    public SgUsuario actualizarMiPerfil(SgActualizarPerfil perfil) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
//        if (perfil == null || userToken == null) {
//            return null;
//        }
//        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SEGURIDAD, "v1/usuarios/me/actualizarperfil");
//        return RestClient.invokePost(webTarget, perfil, SgUsuario.class, userToken);
//    }
    
    @Timeout(value = 30000L)
    public byte[] generarMiCertificado(String password) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (password == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SEGURIDAD, "v1/usuarios/me/generarcertificado");
        return restClient.invokeGetFile(webTarget, password, byte[].class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public SgUsuario obtenerPorCodigo(String codigo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (codigo == null) {
            return null;
        }
        FiltroUsuario filtro = new FiltroUsuario();
        filtro.setCodigoExacto(codigo);
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SEGURIDAD, "v1/usuarios/buscar");
        List<SgUsuario> usuarios = Arrays.asList(restClient.invokePost(webTarget, filtro, SgUsuario[].class));
        if (!usuarios.isEmpty()) {
            return usuarios.get(0);
        }
        return null;
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public List<SgUsuario> buscar(FiltroUsuario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SEGURIDAD, "v1/usuarios/buscar");
        SgUsuario[] usuarios = restClient.invokePost(webTarget, filtro, SgUsuario[].class);
        return Arrays.asList(usuarios);
    }

}
