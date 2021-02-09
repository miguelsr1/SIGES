/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
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
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.SgSolicitudImpresion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 15000L)
public class SolicitudImpresionRestClient implements Serializable {

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

    public SolicitudImpresionRestClient() {
    }

 



    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgSolicitudImpresion obtenerPorId(Long solicitudImpresionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudImpresionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudimpresion");
        webTarget = webTarget.path(solicitudImpresionPk.toString());
        return restClient.invokeGet(webTarget, SgSolicitudImpresion.class);
    }

   
    public SgSolicitudImpresion cambioEstadoImpresa(SgSolicitudImpresion solicitudImpresion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudImpresion == null) {
            return null;
        }
     
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudimpresion/cambioEstadoImpresa");
        webTarget = webTarget.path(solicitudImpresion.getSimPk().toString());
        return restClient.invokePut(webTarget, solicitudImpresion, SgSolicitudImpresion.class);
       
    }

   

}
