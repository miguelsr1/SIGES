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
import sv.gob.mined.siges.web.dto.SgImplementadora;
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
public class ImplementadoraRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ImplementadoraRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgImplementadora> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/implementadoras/buscar");
        SgImplementadora[] implementadoras = RestClient.invokePost(webTarget, filtro, SgImplementadora[].class, userToken);
        return Arrays.asList(implementadoras);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/implementadoras/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgImplementadora guardar(SgImplementadora implementadoras) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (implementadoras == null || userToken == null) {
            return null;
        }
        if (implementadoras.getImpPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/implementadoras");
            return RestClient.invokePost(webTarget, implementadoras, SgImplementadora.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/implementadoras");
            webTarget = webTarget.path(implementadoras.getImpPk().toString());
            return RestClient.invokePut(webTarget, implementadoras, SgImplementadora.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgImplementadora obtenerPorId(Long implementadorasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (implementadorasPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/implementadoras");
        webTarget = webTarget.path(implementadorasPk.toString());
        return RestClient.invokeGet(webTarget, SgImplementadora.class, userToken);
    }

    public void eliminar(Long implementadorasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (implementadorasPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/implementadoras");
        webTarget = webTarget.path(implementadorasPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgImplementadora> obtenerHistorialPorId(Long implementadorasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (implementadorasPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/implementadoras/historial");
        webTarget = webTarget.path(implementadorasPk.toString());
        SgImplementadora[] implementadoras = RestClient.invokeGet(webTarget, SgImplementadora[].class, userToken);
        return Arrays.asList(implementadoras);
    }
    

}
