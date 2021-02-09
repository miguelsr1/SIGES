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
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalendario;

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
public class CalendarioRestClient implements Serializable {

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

    public CalendarioRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgCalendario> buscar(FiltroCalendario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendarios/buscar");
        SgCalendario[] calendarios = restClient.invokePost(webTarget, filtro, SgCalendario[].class);
        return Arrays.asList(calendarios);
    }
    
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.CALENDARIO_CACHE)
    public List<SgCalendario> buscarConCache(FiltroCalendario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendarios/buscar");
        SgCalendario[] calendarios = restClient.invokePost(webTarget, filtro, SgCalendario[].class);
        return Arrays.asList(calendarios);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCalendario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendarios/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCalendario guardar(SgCalendario calendario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calendario == null) {
            return null;
        }
        if (calendario.getCalPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendarios");
            return restClient.invokePost(webTarget, calendario, SgCalendario.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendarios");
            webTarget = webTarget.path(calendario.getCalPk().toString());
            return restClient.invokePut(webTarget, calendario, SgCalendario.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCalendario obtenerPorId(Long calendarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calendarioPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendarios");
        webTarget = webTarget.path(calendarioPk.toString());
        return restClient.invokeGet(webTarget, SgCalendario.class);
    }

    public void eliminar(Long calendarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calendarioPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendarios");
        webTarget = webTarget.path(calendarioPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long calendarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calendarioPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendarios/historial");
        webTarget = webTarget.path(calendarioPk.toString());
        RevHistorico[] calendarios = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(calendarios);
    }

    public SgCalendario obtenerEnRevision(Long calendarioPk, Long calendarioRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calendarioPk == null || calendarioRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendarios/revision");
        webTarget = webTarget.path(calendarioPk.toString());
        webTarget = webTarget.path(calendarioRev.toString());
        return restClient.invokeGet(webTarget, SgCalendario.class);
    }

}
