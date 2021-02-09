/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
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
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientosEdicion;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 30000L)
public class MovimientosEdicionRestClient implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MovimientosEdicionRestClient.class.getName());

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

    public MovimientosEdicionRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgMovimientos> buscar(FiltroMovimientosEdicion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos/buscar");
        SgMovimientos[] mov = restClient.invokePost(webTarget, filtro, SgMovimientos[].class);
        return new ArrayList<>(Arrays.asList(mov));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroMovimientosEdicion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgMovimientos obtenerPorId(Long movimientoId, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos");
        webTarget = webTarget.path(movimientoId.toString());
        webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
        return restClient.invokeGet(webTarget, SgMovimientos.class);
    }

    public SgMovimientos guardar(SgMovimientos movimiento) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimiento == null) {
            return null;
        }

        if (movimiento.getMovPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos");
            return restClient.invokePost(webTarget, movimiento, SgMovimientos.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos");
            webTarget = webTarget.path(movimiento.getMovPk().toString());
            return restClient.invokePut(webTarget, movimiento, SgMovimientos.class);
        }
    }

    public void eliminar(Long movimientoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos");
        webTarget = webTarget.path(movimientoId.toString());
        restClient.invokeDelete(webTarget);
    }

    public void eliminarAnular(Long movimientoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos/eliminarAnular");
        webTarget = webTarget.path(movimientoId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long movimientoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos/historial");
        webTarget = webTarget.path(movimientoId.toString());
        RevHistorico[] pres = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(pres);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgMovimientos obtenerEnRevision(Long movPk, Long movRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movPk == null || movRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos/revision");
        webTarget = webTarget.path(movPk.toString());
        webTarget = webTarget.path(movRev.toString());
        return restClient.invokeGet(webTarget, SgMovimientos.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgMovimientos> buscarDescripcion(FiltroMovimientosEdicion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos/buscarDescripcion");
        SgMovimientos[] detalleDescripcion = restClient.invokePost(webTarget, filtro, SgMovimientos[].class);
        return Arrays.asList(detalleDescripcion);
    }

    public void eliminarIngreso(Long movimientoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos/eliminarIngreso");
        webTarget = webTarget.path(movimientoId.toString());

        restClient.invokeDelete(webTarget);
    }

    public void eliminarEditados(Long movimientoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/edicion_movimientos/eliminarEditados");
        webTarget = webTarget.path(movimientoId.toString());
        restClient.invokeDelete(webTarget);
    }

}
