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
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgActividadCalendario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroActividadCalendario;

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
public class ActividadCalendarioRestClient implements Serializable {

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

    public ActividadCalendarioRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgActividadCalendario> buscar(FiltroActividadCalendario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadcalendario/buscar");
        SgActividadCalendario[] actividadCalendario = restClient.invokePost(webTarget, filtro, SgActividadCalendario[].class);
        return Arrays.asList(actividadCalendario);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroActividadCalendario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadcalendario/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgActividadCalendario guardar(SgActividadCalendario actividadCalendario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (actividadCalendario == null) {
            return null;
        }
        if (actividadCalendario.getAcaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadcalendario");
            return restClient.invokePost(webTarget, actividadCalendario, SgActividadCalendario.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadcalendario");
            webTarget = webTarget.path(actividadCalendario.getAcaPk().toString());
            return restClient.invokePut(webTarget, actividadCalendario, SgActividadCalendario.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgActividadCalendario obtenerPorId(Long actividadCalendarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (actividadCalendarioPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadcalendario");
        webTarget = webTarget.path(actividadCalendarioPk.toString());
        return restClient.invokeGet(webTarget, SgActividadCalendario.class);
    }

    public void eliminar(Long actividadCalendarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (actividadCalendarioPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadcalendario");
        webTarget = webTarget.path(actividadCalendarioPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long actividadCalendarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (actividadCalendarioPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/actividadcalendario/historial");
        webTarget = webTarget.path(actividadCalendarioPk.toString());
        RevHistorico[] actividadCalendario = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(actividadCalendario);
    }

}
