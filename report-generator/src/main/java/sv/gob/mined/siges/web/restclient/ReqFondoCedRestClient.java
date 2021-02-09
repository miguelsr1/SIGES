/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

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
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.DetalleRequerimientoFondo;
import sv.gob.mined.siges.web.dto.finanzas.SgReqFondoCed;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReqFondoCed;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class ReqFondoCedRestClient implements Serializable {

    @Inject
    private RestClient restClient;

    private Client client = null;

    public ReqFondoCedRestClient() {
    }

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

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgReqFondoCed> buscar(FiltroReqFondoCed filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/reqfondosced/buscar");
        SgReqFondoCed[] ReqFondosCed = restClient.invokePost(webTarget, filtro, SgReqFondoCed[].class);
        return Arrays.asList(ReqFondosCed);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroReqFondoCed filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/reqfondosced/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgReqFondoCed obtenerPorId(Long reqFondoCedPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reqFondoCedPk == null) {
            return null;
        }
        WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/reqfondosced");
        webTarget = webTarget.path(reqFondoCedPk.toString());
        return restClient.invokeGet(webTarget, SgReqFondoCed.class);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<DetalleRequerimientoFondo> obtenerDetalleReq(Long reqId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reqId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/reqfondosced/detalle/requerimiento");
        webTarget = webTarget.path(reqId.toString());
        DetalleRequerimientoFondo[] detalleReq = restClient.invokeGet(webTarget, DetalleRequerimientoFondo[].class);
        return Arrays.asList(detalleReq);
    }

}
