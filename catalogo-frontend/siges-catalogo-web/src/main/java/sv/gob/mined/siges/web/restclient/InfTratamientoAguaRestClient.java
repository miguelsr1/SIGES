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
import sv.gob.mined.siges.web.dto.SgInfTratamientoAgua;
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
public class InfTratamientoAguaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InfTratamientoAguaRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfTratamientoAgua> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tratamientoagua/buscar");
        SgInfTratamientoAgua[] tratamientoAgua = RestClient.invokePost(webTarget, filtro, SgInfTratamientoAgua[].class, userToken);
        return Arrays.asList(tratamientoAgua);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tratamientoagua/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInfTratamientoAgua guardar(SgInfTratamientoAgua infTratamientoAgua) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTratamientoAgua == null || userToken == null) {
            return null;
        }
        if (infTratamientoAgua.getTraPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tratamientoagua");
            return RestClient.invokePost(webTarget, infTratamientoAgua, SgInfTratamientoAgua.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tratamientoagua");
            webTarget = webTarget.path(infTratamientoAgua.getTraPk().toString());
            return RestClient.invokePut(webTarget, infTratamientoAgua, SgInfTratamientoAgua.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgInfTratamientoAgua obtenerPorId(Long infTratamientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTratamientoAguaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tratamientoagua");
        webTarget = webTarget.path(infTratamientoAguaPk.toString());
        return RestClient.invokeGet(webTarget, SgInfTratamientoAgua.class, userToken);
    }

    public void eliminar(Long infTratamientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTratamientoAguaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tratamientoagua");
        webTarget = webTarget.path(infTratamientoAguaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfTratamientoAgua> obtenerHistorialPorId(Long infTratamientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTratamientoAguaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tratamientoagua/historial");
        webTarget = webTarget.path(infTratamientoAguaPk.toString());
        SgInfTratamientoAgua[] tratamientoAgua = RestClient.invokeGet(webTarget, SgInfTratamientoAgua[].class, userToken);
        return Arrays.asList(tratamientoAgua);
    }
    

}
