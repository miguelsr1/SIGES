/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.annotation.CacheResult;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class AnioLectivoRestClient implements Serializable {

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
    }

    public AnioLectivoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAnioLectivo> buscar(FiltroAnioLectivo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos/buscar");
        SgAnioLectivo[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgAnioLectivo[].class);
        return new ArrayList<>(Arrays.asList(organizacionesCurricular));
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.ANIO_LECTIVO_CACHE)
    public List<SgAnioLectivo> buscarConCache(FiltroAnioLectivo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos/buscar");
        SgAnioLectivo[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgAnioLectivo[].class);
        return new ArrayList<>(Arrays.asList(organizacionesCurricular));
    }
    
    

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroAnioLectivo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAnioLectivo guardar(SgAnioLectivo anioLectivo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioLectivo == null) {
            return null;
        }
        if (anioLectivo.getAlePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos");
            return restClient.invokePost(webTarget, anioLectivo, SgAnioLectivo.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos");
            webTarget = webTarget.path(anioLectivo.getAlePk().toString());
            return restClient.invokePut(webTarget, anioLectivo, SgAnioLectivo.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAnioLectivo obtenerPorId(Long anioLectivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioLectivoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos");
        webTarget = webTarget.path(anioLectivoPk.toString());
        return restClient.invokeGet(webTarget, SgAnioLectivo.class);
    }

    public void eliminar(Long anioLectivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioLectivoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos");
        webTarget = webTarget.path(anioLectivoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long anioLectivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioLectivoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos/historial");
        webTarget = webTarget.path(anioLectivoPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgAnioLectivo obtenerEnRevision(Long anioLectivoPk, Long anioLectivoRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioLectivoPk == null || anioLectivoRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos/revision");
        webTarget = webTarget.path(anioLectivoPk.toString());
        webTarget = webTarget.path(anioLectivoRev.toString());
        return restClient.invokeGet(webTarget, SgAnioLectivo.class);
    }

}
