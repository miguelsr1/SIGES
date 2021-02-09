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
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAfTraslado;
import sv.gob.mined.siges.web.dto.SolicitudTraslado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 10000L)
public class TrasladosRestClient implements Serializable {

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

    public TrasladosRestClient() {
    }
    
    @Timeout(value = 10000L)
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAfTraslado> buscar(FiltroBienesDepreciables filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/traslados/buscar");
        SgAfTraslado[] traslados = restClient.invokePost(webTarget, filtro, SgAfTraslado[].class);
        return Arrays.asList(traslados);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroBienesDepreciables filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/traslados/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }
    
    @Timeout(value = 100000L)
    public SgAfTraslado guardar(SolicitudTraslado solicitud) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitud == null) {
            return null;
        }
        if (solicitud.getTraslado().getTraPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/traslados");
            return restClient.invokePost(webTarget, solicitud, SgAfTraslado.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/traslados");
            webTarget = webTarget.path(solicitud.getTraslado().getTraPk().toString());
            return restClient.invokePut(webTarget, solicitud, SgAfTraslado.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAfTraslado obtenerPorId(Long trasladoPk, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (trasladoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/traslados");
        webTarget = webTarget.path(trasladoPk.toString());
        webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
        return restClient.invokeGet(webTarget, SgAfTraslado.class);
    }

    public void eliminar(SolicitudTraslado solicitud) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitud == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/traslados/eliminarSolicitud");
        restClient.invokePost(webTarget, solicitud, null);
    }
    
    public List<RevHistorico> obtenerHistorialPorId(Long trasladoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (trasladoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/traslados/historial");
        webTarget = webTarget.path(trasladoPk.toString());
        RevHistorico[] traslados = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(traslados);
    }
    
    public SgAfTraslado obtenerEnRevision(Long trasladoPk, Long trasladoRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (trasladoPk == null || trasladoRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/traslados/revision");
        webTarget = webTarget.path(trasladoPk.toString());
        webTarget = webTarget.path(trasladoRev.toString());
        return restClient.invokeGet(webTarget, SgAfTraslado.class);
    }
}
