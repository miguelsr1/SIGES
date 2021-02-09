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
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.siap2.SgAreaInversion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAreaInversion;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class AreaInversionRestClient implements Serializable {

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

    public AreaInversionRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAreaInversion obtenerPorId(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/areasinversion");
        webTarget = webTarget.path(personaId.toString());
        return restClient.invokeGet(webTarget, SgAreaInversion.class);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/areasinversion/historial");
        webTarget = webTarget.path(personaId.toString());
        RevHistorico[] sede = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(sede);
    }

    @Timeout(value = 20000L)
    public SgAreaInversion obtenerEnRevision(Long personaPk, Long personaRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaPk == null || personaRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/areasinversion/revision");
        webTarget = webTarget.path(personaPk.toString());
        webTarget = webTarget.path(personaRev.toString());
        return restClient.invokeGet(webTarget, SgAreaInversion.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAreaInversion> buscar(FiltroAreaInversion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/areasinversion/buscar");
        SgAreaInversion[] detalleAreasInversion = restClient.invokePost(webTarget, filtro, SgAreaInversion[].class);
        return Arrays.asList(detalleAreasInversion);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAreaInversion> buscarAreaPrincipal(FiltroAreaInversion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/areasinversion/buscarAreaPrincipal");
        SgAreaInversion[] detalleAreasInversion = restClient.invokePost(webTarget, filtro, SgAreaInversion[].class);
        return Arrays.asList(detalleAreasInversion);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAreaInversion> buscarSubAreaPrincipal(FiltroAreaInversion filtro, Long subArea) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }

        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/areasinversion/buscarSubAreaPrincipal");
        SgAreaInversion[] detalleAreasInversion = restClient.invokePost(webTarget, filtro, SgAreaInversion[].class);
        return Arrays.asList(detalleAreasInversion);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAreaInversion> buscarSubAreaTransferencia(Long filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/areasinversion/buscarSubAreaTransferencia");
        SgAreaInversion[] detalleAreasInversion = restClient.invokePost(webTarget, filtro, SgAreaInversion[].class);
        return Arrays.asList(detalleAreasInversion);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.AREAS_LAZY_CACHE) //Solamente utilizado para setear la sede en otra entidad
    public SgAreaInversion obtenerPorIdLazy(Long sedeId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sedeId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/areasinversion/lazy");
        webTarget = webTarget.path(sedeId.toString());
        return restClient.invokeGet(webTarget, SgAreaInversion.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAreaInversion> buscarAreaTransferencia(Long filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/areasinversion/buscarAreaTransferencia");
        SgAreaInversion[] detalleAreasInversion = restClient.invokePost(webTarget, filtro, SgAreaInversion[].class);
        return Arrays.asList(detalleAreasInversion);
    }

}
