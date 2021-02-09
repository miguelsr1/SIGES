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
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.utils.JWTUtils;
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
@Retry(maxRetries = 3, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class, Exception.class}, delay = 1000)
@Timeout(value = 10000L)
public class SeguridadRestClient implements Serializable {

    public SeguridadRestClient() {
    }

    private Client client;

    @PostConstruct
    public void init() {
        client = StaticRestClient.getClient();
    }

    public List<String> obtenerOperacionesPorCodigoUsuario(String codigoUsuario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigoUsuario == null) {
            return new ArrayList<>();
        }
        //Al momento de cargar las operaciones el token todavía no está creado
        String token = JWTUtils.generarToken(codigoUsuario, "", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        WebTarget webTarget = StaticRestClient.getWebTarget(client, ConstantesServiciosRest.SEGURIDAD, "v1/usuarios/operaciones");
        webTarget = webTarget.path(codigoUsuario.toString());
        String[] operaciones = StaticRestClient.invokeGet(webTarget, String[].class, token);
        return Arrays.asList(operaciones);
    }


    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            client.close();
        }
    }

}
