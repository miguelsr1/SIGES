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
import sv.gob.mined.siges.web.dto.SgVelocidadInternet;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

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
public class VelocidadInternetRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public VelocidadInternetRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgVelocidadInternet> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/velocidadesinternet/buscar");
        SgVelocidadInternet[] velocidadesInternet = RestClient.invokePost(webTarget, filtro, SgVelocidadInternet[].class, userToken);
        return Arrays.asList(velocidadesInternet);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/velocidadesinternet/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgVelocidadInternet guardar(SgVelocidadInternet velocidadInternet) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (velocidadInternet == null || userToken == null) {
            return null;
        }
        if (velocidadInternet.getVinPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/velocidadesinternet");
            return RestClient.invokePost(webTarget, velocidadInternet, SgVelocidadInternet.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/velocidadesinternet");
            webTarget = webTarget.path(velocidadInternet.getVinPk().toString());
            return RestClient.invokePut(webTarget, velocidadInternet, SgVelocidadInternet.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgVelocidadInternet obtenerPorId(Long velocidadInternetPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (velocidadInternetPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/velocidadesinternet");
        webTarget = webTarget.path(velocidadInternetPk.toString());
        return RestClient.invokeGet(webTarget, SgVelocidadInternet.class, userToken);
    }

    public void eliminar(Long velocidadInternetPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (velocidadInternetPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/velocidadesinternet");
        webTarget = webTarget.path(velocidadInternetPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgVelocidadInternet> obtenerHistorialPorId(Long velocidadInternetPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (velocidadInternetPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/velocidadesinternet/historial");
        webTarget = webTarget.path(velocidadInternetPk.toString());
        SgVelocidadInternet[] velocidadesInternet = RestClient.invokeGet(webTarget, SgVelocidadInternet[].class, userToken);
        return Arrays.asList(velocidadesInternet);
    }
    

}
