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
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRangoFecha;

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
public class RangoFechaRestClient implements Serializable {

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

    public RangoFechaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRangoFecha> buscar(FiltroRangoFecha filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/rangosfecha/buscar");
        SgRangoFecha[] rangosFecha = restClient.invokePost(webTarget, filtro, SgRangoFecha[].class);
        return new ArrayList(Arrays.asList(rangosFecha));
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.RANGO_FECHA_CACHE)
    public List<SgRangoFecha> buscarConCache(FiltroRangoFecha filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/rangosfecha/buscar");
        SgRangoFecha[] rangosFecha = restClient.invokePost(webTarget, filtro, SgRangoFecha[].class);
        return new ArrayList(Arrays.asList(rangosFecha));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroRangoFecha filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/rangosfecha/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRangoFecha guardar(SgRangoFecha rangoFecha) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rangoFecha == null) {
            return null;
        }
        if (rangoFecha.getRfePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/rangosfecha");
            return restClient.invokePost(webTarget, rangoFecha, SgRangoFecha.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/rangosfecha");
            webTarget = webTarget.path(rangoFecha.getRfePk().toString());
            return restClient.invokePut(webTarget, rangoFecha, SgRangoFecha.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgRangoFecha obtenerPorId(Long rangoFechaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rangoFechaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/rangosfecha");
        webTarget = webTarget.path(rangoFechaPk.toString());
        return restClient.invokeGet(webTarget, SgRangoFecha.class);
    }

    public void eliminar(Long rangoFechaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rangoFechaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/rangosfecha");
        webTarget = webTarget.path(rangoFechaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<SgRangoFecha> obtenerHistorialPorId(Long rangoFechaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rangoFechaPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/rangosfecha/historial");
        webTarget = webTarget.path(rangoFechaPk.toString());
        SgRangoFecha[] rangosFecha = restClient.invokeGet(webTarget, SgRangoFecha[].class);
        return Arrays.asList(rangosFecha);
    }

}
