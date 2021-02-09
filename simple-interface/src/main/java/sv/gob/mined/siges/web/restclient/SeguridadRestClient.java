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
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 5000L)
public class SeguridadRestClient implements Serializable {

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
    
    public SeguridadRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 250)
    @CacheResult(cacheName = Constantes.USUARIO_OPERACIONES_CACHE)
    public List<String> obtenerOperacionesPorCodigoUsuario(@CacheKey String codigoUsuario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigoUsuario == null) {
            return new ArrayList<>();
        }  
        //Al momento de cargar las operaciones el token todavía no está creado
        String token = JWTUtils.generarToken(codigoUsuario, "", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SEGURIDAD, "v1/usuarios/operaciones");
        webTarget = webTarget.path(codigoUsuario.toString());
        String[] operaciones = RestClient.invokeGet(webTarget, String[].class, token);
        return Arrays.asList(operaciones);
    }
    
}
