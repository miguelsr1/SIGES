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
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgActividadTiempoExtendido;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;

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
public class ActividadTiempoExtendidoRestClient implements Serializable {

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

    public ActividadTiempoExtendidoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgActividadTiempoExtendido> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadestiempoextendido/buscar");
        SgActividadTiempoExtendido[] actividadesTiempoExtendido = restClient.invokePost(webTarget, filtro, SgActividadTiempoExtendido[].class);
        return Arrays.asList(actividadesTiempoExtendido);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadestiempoextendido/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgComponentePlanEstudio guardar(SgComponentePlanEstudio actividadTiempoExtendido) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (actividadTiempoExtendido == null) {
            return null;
        }
        if (actividadTiempoExtendido.getCpePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadestiempoextendido");
            return restClient.invokePost(webTarget, actividadTiempoExtendido, SgActividadTiempoExtendido.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadestiempoextendido");
            webTarget = webTarget.path(actividadTiempoExtendido.getCpePk().toString());
            return restClient.invokePut(webTarget, actividadTiempoExtendido, SgActividadTiempoExtendido.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgActividadTiempoExtendido obtenerPorId(Long actividadTiempoExtendidoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (actividadTiempoExtendidoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadestiempoextendido");
        webTarget = webTarget.path(actividadTiempoExtendidoPk.toString());
        return restClient.invokeGet(webTarget, SgActividadTiempoExtendido.class);
    }

    public void eliminar(Long actividadTiempoExtendidoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (actividadTiempoExtendidoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadestiempoextendido");
        webTarget = webTarget.path(actividadTiempoExtendidoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long actividadTiempoExtendidoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (actividadTiempoExtendidoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadestiempoextendido/historial");
        webTarget = webTarget.path(actividadTiempoExtendidoPk.toString());
        RevHistorico[] actividadesTiempoExtendido = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(actividadesTiempoExtendido);
    }

}
