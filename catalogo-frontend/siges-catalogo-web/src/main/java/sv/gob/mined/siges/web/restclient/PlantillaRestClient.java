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
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlantilla;

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
public class PlantillaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public PlantillaRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgPlantilla> buscar(FiltroPlantilla filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla/buscar");
        SgPlantilla[] plantilla = RestClient.invokePost(webTarget, filtro, SgPlantilla[].class, userToken);
        return Arrays.asList(plantilla);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroPlantilla filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgPlantilla guardar(SgPlantilla plantilla) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plantilla == null || userToken == null) {
            return null;
        }
        if (plantilla.getPlaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla");
            return RestClient.invokePost(webTarget, plantilla, SgPlantilla.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla");
            webTarget = webTarget.path(plantilla.getPlaPk().toString());
            return RestClient.invokePut(webTarget, plantilla, SgPlantilla.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgPlantilla obtenerPorId(Long plantillaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plantillaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla");
        webTarget = webTarget.path(plantillaPk.toString());
        return RestClient.invokeGet(webTarget, SgPlantilla.class, userToken);
    }

    public void eliminar(Long plantillaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plantillaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla");
        webTarget = webTarget.path(plantillaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgPlantilla> obtenerHistorialPorId(Long plantillaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plantillaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla/historial");
        webTarget = webTarget.path(plantillaPk.toString());
        SgPlantilla[] plantilla = RestClient.invokeGet(webTarget, SgPlantilla[].class, userToken);
        return Arrays.asList(plantilla);
    }

}
