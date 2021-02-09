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
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgChequera;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroChequera;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class ChequeraRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    @Inject
    private RestClient restClient;

    private Client client = null;

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

    public ChequeraRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgChequera> buscar(FiltroChequera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/chequeras/buscar");
        SgChequera[] chequera = restClient.invokePost(webTarget, filtro, SgChequera[].class);
        return new ArrayList<>(Arrays.asList(chequera));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgChequera obtenerPorIdLazy(Long chequeraId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (chequeraId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/chequeras/lazy");
        webTarget = webTarget.path(chequeraId.toString());
        return restClient.invokeGet(webTarget, SgChequera.class);
    }

    public SgChequera guardar(SgChequera chequera) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (chequera == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(chequera);
        if (chequera.getChePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/chequeras");
            return restClient.invokePost(webTarget, chequera, SgChequera.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/chequeras");
            webTarget = webTarget.path(chequera.getChePk().toString());
            return restClient.invokePut(webTarget, chequera, SgChequera.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgChequera obtenerPorId(Long chequeraId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (chequeraId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/chequeras");
        webTarget = webTarget.path(chequeraId.toString());
        return restClient.invokeGet(webTarget, SgChequera.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroChequera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/chequeras/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public void eliminar(Long pagoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pagoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/chequeras");
        webTarget = webTarget.path(pagoPk.toString());
        restClient.invokeDelete(webTarget);
    }

}
