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
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.catalogo.SgPlantilla;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroPlantilla;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class PlantillaRestClient implements Serializable {

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

    public PlantillaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgPlantilla obtenerPorId(Long plantillaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plantillaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla");
        webTarget = webTarget.path(plantillaPk.toString());
        return restClient.invokeGet(webTarget, SgPlantilla.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.PLANTILLAS_CACHE)
    public SgPlantilla obtenerPorCodigo(@CacheKey String codigo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigo == null) {
            return null;
        }
        FiltroPlantilla filtro = new FiltroPlantilla();
        filtro.setCodigoExacto(codigo);
        List<SgPlantilla> plantillas = this.buscarPlantillas(filtro);
        if (!plantillas.isEmpty()) {
            return plantillas.get(0);
        }
        return null;
    }

    //No poner cache, ya que se usa para recargar plantillas de organizacion luego de guardar
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPlantilla> buscarPlantillas(FiltroPlantilla filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla/buscar");
        SgPlantilla[] plantilla = restClient.invokePost(webTarget, filtro, SgPlantilla[].class);
        return Arrays.asList(plantilla);
    }

    public SgPlantilla guardar(SgPlantilla plantilla) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plantilla == null) {
            return null;
        }
        if (plantilla.getPlaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla");
            return restClient.invokePost(webTarget, plantilla, SgPlantilla.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla");
            webTarget = webTarget.path(plantilla.getPlaPk().toString());
            return restClient.invokePut(webTarget, plantilla, SgPlantilla.class);
        }
    }
    
    public void eliminar(Long plantillaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plantillaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla");
        webTarget = webTarget.path(plantillaPk.toString());
        restClient.invokeDelete(webTarget);
    }
    

}
