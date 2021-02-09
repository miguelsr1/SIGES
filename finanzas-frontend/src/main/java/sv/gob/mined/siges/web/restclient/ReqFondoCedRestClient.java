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
import sv.gob.mined.siges.web.dto.SgReqFondoCed;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReqFondoCed;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class ReqFondoCedRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    @Inject
    private RestClient restClient;
    
    private Client client = null;
    
    public ReqFondoCedRestClient() {
    }
    
    @PostConstruct
    public void init() {
        client = restClient.getClient();
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            //client.close();
        }
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgReqFondoCed> buscar(FiltroReqFondoCed filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/reqfondosced/buscar");
        SgReqFondoCed[] ReqFondosCed = restClient.invokePost(webTarget, filtro, SgReqFondoCed[].class);
        return Arrays.asList(ReqFondosCed);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroReqFondoCed filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/reqfondosced/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgReqFondoCed guardar(SgReqFondoCed reqFondoCed) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reqFondoCed == null || userToken == null) {
            return null;
        }
        if (reqFondoCed.getRfcPk() == null) {
            WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/reqfondosced");
            return restClient.invokePost(webTarget, reqFondoCed, SgReqFondoCed.class);
        } else {
            WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/reqfondosced");
            webTarget = webTarget.path(reqFondoCed.getRfcPk().toString());
            return restClient.invokePut(webTarget, reqFondoCed, SgReqFondoCed.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgReqFondoCed obtenerPorId(Long reqFondoCedPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reqFondoCedPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/reqfondosced");
        webTarget = webTarget.path(reqFondoCedPk.toString());
        return restClient.invokeGet(webTarget, SgReqFondoCed.class);
    }

    public void eliminar(Long reqFondoCedPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reqFondoCedPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/reqfondosced");
        webTarget = webTarget.path(reqFondoCedPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgReqFondoCed> obtenerHistorialPorId(Long reqFondoCedPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reqFondoCedPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/reqfondosced/historial");
        webTarget = webTarget.path(reqFondoCedPk.toString());
        SgReqFondoCed[] ReqFondosCed = restClient.invokeGet(webTarget, SgReqFondoCed[].class);
        return Arrays.asList(ReqFondosCed);
    }
    

}
