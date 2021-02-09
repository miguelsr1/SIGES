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
import sv.gob.mined.siges.web.dto.SgRelInmuebleUnidadResp;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtros.FiltroRelInmuebleUnidadResp;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@Traced
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class RelInmuebleUnidadRespRestClient implements Serializable {

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
    };
    
    
    public RelInmuebleUnidadRespRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelInmuebleUnidadResp> buscar(FiltroRelInmuebleUnidadResp filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleunidadresp/buscar");
        SgRelInmuebleUnidadResp[] relInmuebleUnidadResp = restClient.invokePost(webTarget, filtro, SgRelInmuebleUnidadResp[].class);
        return Arrays.asList(relInmuebleUnidadResp);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelInmuebleUnidadResp filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleunidadresp/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelInmuebleUnidadResp guardar(SgRelInmuebleUnidadResp relInmuebleUnidadResp) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleUnidadResp == null ) {
            return null;
        }
        if (relInmuebleUnidadResp.getRiuPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleunidadresp");
            return restClient.invokePost(webTarget, relInmuebleUnidadResp, SgRelInmuebleUnidadResp.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleunidadresp");
            webTarget = webTarget.path(relInmuebleUnidadResp.getRiuPk().toString());
            return restClient.invokePut(webTarget, relInmuebleUnidadResp, SgRelInmuebleUnidadResp.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelInmuebleUnidadResp obtenerPorId(Long relInmuebleUnidadRespPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleUnidadRespPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleunidadresp");
        webTarget = webTarget.path(relInmuebleUnidadRespPk.toString());
        return restClient.invokeGet(webTarget, SgRelInmuebleUnidadResp.class);
    }

    public void eliminar(Long relInmuebleUnidadRespPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleUnidadRespPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleunidadresp");
        webTarget = webTarget.path(relInmuebleUnidadRespPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relInmuebleUnidadRespPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleUnidadRespPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleunidadresp/historial");
        webTarget = webTarget.path(relInmuebleUnidadRespPk.toString());
        RevHistorico[] relInmuebleUnidadResp = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relInmuebleUnidadResp);
    }
    

}
