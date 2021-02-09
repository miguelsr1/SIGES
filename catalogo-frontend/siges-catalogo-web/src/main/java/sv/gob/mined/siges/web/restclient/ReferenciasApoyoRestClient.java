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
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgReferenciasApoyo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

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
public class ReferenciasApoyoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ReferenciasApoyoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgReferenciasApoyo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/referenciasdeapoyo/buscar");
        SgReferenciasApoyo[] referenciasdeapoyo = RestClient.invokePost(webTarget, filtro, SgReferenciasApoyo[].class, userToken);
        return Arrays.asList(referenciasdeapoyo);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/referenciasdeapoyo/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgReferenciasApoyo guardar(SgReferenciasApoyo referenciasApoyo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (referenciasApoyo == null || userToken == null) {
            return null;
        }
        if (referenciasApoyo.getReaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/referenciasdeapoyo");
            return RestClient.invokePost(webTarget, referenciasApoyo, SgReferenciasApoyo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/referenciasdeapoyo");
            webTarget = webTarget.path(referenciasApoyo.getReaPk().toString());
            return RestClient.invokePut(webTarget, referenciasApoyo, SgReferenciasApoyo.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgReferenciasApoyo obtenerPorId(Long referenciasApoyoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (referenciasApoyoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/referenciasdeapoyo");
        webTarget = webTarget.path(referenciasApoyoPk.toString());
        return RestClient.invokeGet(webTarget, SgReferenciasApoyo.class, userToken);
    }

    public void eliminar(Long referenciasApoyoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (referenciasApoyoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/referenciasdeapoyo");
        webTarget = webTarget.path(referenciasApoyoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgReferenciasApoyo> obtenerHistorialPorId(Long referenciasApoyoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (referenciasApoyoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/referenciasdeapoyo/historial");
        webTarget = webTarget.path(referenciasApoyoPk.toString());
        SgReferenciasApoyo[] referenciasdeapoyo = RestClient.invokeGet(webTarget, SgReferenciasApoyo[].class, userToken);
        return Arrays.asList(referenciasdeapoyo);
    }
    

}
