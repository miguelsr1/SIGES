/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.utilidades.JWTUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
@Timeout(value = 5000L)
public class SeguridadRestClient implements Serializable {

//    @Inject
//    @Named("userToken")
//    private String userToken;
    
    public SeguridadRestClient() {
    }

    public List<String> obtenerOperacionesPorCodigoUsuario(String codigoUsuario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigoUsuario == null) {
            return new ArrayList<>();
        }  
        //Al momento de cargar las operaciones el token todavía no está creado
        String token = JWTUtils.generarToken(codigoUsuario, "", "/privateKey.pem", Arrays.asList(new String[]{"AUTH"}), 20);
        WebTarget webTarget = RestClient.getWebTarget("seguridad", "v1/usuarios/operaciones");
        webTarget = webTarget.path(codigoUsuario.toString());
        String[] operaciones = RestClient.invokeGet(webTarget, String[].class, token);
        return Arrays.asList(operaciones);
    }
    
}
