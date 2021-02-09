/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgTipoApoyo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTiposApoyo;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 3000L)
public class TipoApoyoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoApoyoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoApoyo> buscar(FiltroTiposApoyo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposapoyo/buscar");
        SgTipoApoyo[] tiposApoyo = RestClient.invokePost(webTarget, filtro, SgTipoApoyo[].class, userToken);
        return Arrays.asList(tiposApoyo);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroTiposApoyo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposapoyo/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoApoyo guardar(SgTipoApoyo tipoApoyo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoApoyo == null || userToken == null) {
            return null;
        }
        if (tipoApoyo.getTapPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposapoyo");
            return RestClient.invokePost(webTarget, tipoApoyo, SgTipoApoyo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposapoyo");
            webTarget = webTarget.path(tipoApoyo.getTapPk().toString());
            return RestClient.invokePut(webTarget, tipoApoyo, SgTipoApoyo.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTipoApoyo obtenerPorId(Long tipoApoyoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoApoyoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposapoyo");
        webTarget = webTarget.path(tipoApoyoPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoApoyo.class, userToken);
    }

    public void eliminar(Long tipoApoyoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoApoyoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposapoyo");
        webTarget = webTarget.path(tipoApoyoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoApoyo> obtenerHistorialPorId(Long tipoApoyoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoApoyoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposapoyo/historial");
        webTarget = webTarget.path(tipoApoyoPk.toString());
        SgTipoApoyo[] tiposApoyo = RestClient.invokeGet(webTarget, SgTipoApoyo[].class, userToken);
        return Arrays.asList(tiposApoyo);
    }
    

}
