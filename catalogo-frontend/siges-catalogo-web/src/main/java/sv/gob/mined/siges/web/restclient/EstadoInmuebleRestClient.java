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
import sv.gob.mined.siges.web.dto.SgEstadoInmueble;
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
public class EstadoInmuebleRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EstadoInmuebleRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstadoInmueble> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosinmuebles/buscar");
        SgEstadoInmueble[] estadosInmuebles = RestClient.invokePost(webTarget, filtro, SgEstadoInmueble[].class, userToken);
        return Arrays.asList(estadosInmuebles);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosinmuebles/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEstadoInmueble guardar(SgEstadoInmueble estadoInmueble) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoInmueble == null || userToken == null) {
            return null;
        }
        if (estadoInmueble.getEinPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosinmuebles");
            return RestClient.invokePost(webTarget, estadoInmueble, SgEstadoInmueble.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosinmuebles");
            webTarget = webTarget.path(estadoInmueble.getEinPk().toString());
            return RestClient.invokePut(webTarget, estadoInmueble, SgEstadoInmueble.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEstadoInmueble obtenerPorId(Long estadoInmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoInmueblePk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosinmuebles");
        webTarget = webTarget.path(estadoInmueblePk.toString());
        return RestClient.invokeGet(webTarget, SgEstadoInmueble.class, userToken);
    }

    public void eliminar(Long estadoInmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoInmueblePk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosinmuebles");
        webTarget = webTarget.path(estadoInmueblePk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstadoInmueble> obtenerHistorialPorId(Long estadoInmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoInmueblePk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosinmuebles/historial");
        webTarget = webTarget.path(estadoInmueblePk.toString());
        SgEstadoInmueble[] estadosInmuebles = RestClient.invokeGet(webTarget, SgEstadoInmueble[].class, userToken);
        return Arrays.asList(estadosInmuebles);
    }
    

}
