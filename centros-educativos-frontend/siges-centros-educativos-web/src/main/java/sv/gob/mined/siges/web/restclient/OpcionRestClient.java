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
import sv.gob.mined.siges.web.dto.SgOpcion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOpciones;

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
public class OpcionRestClient implements Serializable {

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

    public OpcionRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgOpcion> buscar(FiltroOpciones filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones/buscar");
        SgOpcion[] opciones = restClient.invokePost(webTarget, filtro, SgOpcion[].class);
        return Arrays.asList(opciones);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.OPCION_CACHE)
    public List<SgOpcion> buscarConCache(FiltroOpciones filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones/buscar");
        SgOpcion[] opciones = restClient.invokePost(webTarget, filtro, SgOpcion[].class);
        return Arrays.asList(opciones);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroOpciones filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgOpcion guardar(SgOpcion opcion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opcion == null) {
            return null;
        }
        if (opcion.getOpcPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones");
            return restClient.invokePost(webTarget, opcion, SgOpcion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones");
            webTarget = webTarget.path(opcion.getOpcPk().toString());
            return restClient.invokePut(webTarget, opcion, SgOpcion.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgOpcion obtenerPorId(Long opcionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opcionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones");
        webTarget = webTarget.path(opcionPk.toString());
        return restClient.invokeGet(webTarget, SgOpcion.class);
    }

    public void eliminar(Long opcionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opcionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones");
        webTarget = webTarget.path(opcionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long opcionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opcionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones/historial");
        webTarget = webTarget.path(opcionPk.toString());
        RevHistorico[] opciones = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(opciones);
    }

    public SgOpcion obtenerEnRevision(Long opcionPk, Long opcionRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opcionPk == null || opcionRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones/revision");
        webTarget = webTarget.path(opcionPk.toString());
        webTarget = webTarget.path(opcionRev.toString());
        return restClient.invokeGet(webTarget, SgOpcion.class);
    }

}
