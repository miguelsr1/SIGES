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
import sv.gob.mined.siges.web.dto.SgAfNotificacionCumplimiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNotificacionCumplimiento;

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
public class NotificacionCumplimientoRestClient implements Serializable {

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

    public NotificacionCumplimientoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAfNotificacionCumplimiento> buscar(FiltroNotificacionCumplimiento filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/notificacionescumplimiento/buscar");
        SgAfNotificacionCumplimiento[] notificaciones = restClient.invokePost(webTarget, filtro, SgAfNotificacionCumplimiento[].class);
        return Arrays.asList(notificaciones);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroNotificacionCumplimiento filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/notificacionescumplimiento/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAfNotificacionCumplimiento guardar(SgAfNotificacionCumplimiento notificacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (notificacion == null) {
            return null;
        }
        if (notificacion.getNcuId()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/notificacionescumplimiento");
            return restClient.invokePost(webTarget, notificacion, SgAfNotificacionCumplimiento.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/notificacionescumplimiento");
            webTarget = webTarget.path(notificacion.getNcuId().toString());
            return restClient.invokePut(webTarget, notificacion, SgAfNotificacionCumplimiento.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAfNotificacionCumplimiento obtenerPorId(Long bienPk, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bienPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/notificacionescumplimiento");
        webTarget = webTarget.path(bienPk.toString());
        webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
        return restClient.invokeGet(webTarget, SgAfNotificacionCumplimiento.class);
    }

    public void eliminar(Long bienPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bienPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/notificacionescumplimiento");
        webTarget = webTarget.path(bienPk.toString());
        restClient.invokeDelete(webTarget);
    }
    
    public List<RevHistorico> obtenerHistorialPorId(Long bienPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bienPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/notificacionescumplimiento/historial");
        webTarget = webTarget.path(bienPk.toString());
        RevHistorico[] bienes = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(bienes);
    }

    public SgAfNotificacionCumplimiento obtenerEnRevision(Long bienPk, Long bienRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bienPk == null || bienRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/notificacionescumplimiento/revision");
        webTarget = webTarget.path(bienPk.toString());
        webTarget = webTarget.path(bienRev.toString());
        return restClient.invokeGet(webTarget, SgAfNotificacionCumplimiento.class);
    }

}
