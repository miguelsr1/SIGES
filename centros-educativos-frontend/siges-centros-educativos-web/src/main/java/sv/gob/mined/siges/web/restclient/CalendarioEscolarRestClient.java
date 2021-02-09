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
import sv.gob.mined.siges.web.dto.SgPeriodoCalendario;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalendario;

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
public class CalendarioEscolarRestClient implements Serializable {

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

    public CalendarioEscolarRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPeriodoCalendario> buscar(FiltroPeriodoCalendario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendariosescolares/buscar");
        SgPeriodoCalendario[] calendarios = restClient.invokePost(webTarget, filtro, SgPeriodoCalendario[].class);
        return Arrays.asList(calendarios);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroPeriodoCalendario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendariosescolares/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgPeriodoCalendario guardar(SgPeriodoCalendario calendario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calendario == null) {
            return null;
        }
        if (calendario.getCesPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendariosescolares");
            return restClient.invokePost(webTarget, calendario, SgPeriodoCalendario.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendariosescolares");
            webTarget = webTarget.path(calendario.getCesPk().toString());
            return restClient.invokePut(webTarget, calendario, SgPeriodoCalendario.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgPeriodoCalendario obtenerPorId(Long calendarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calendarioPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendariosescolares");
        webTarget = webTarget.path(calendarioPk.toString());
        return restClient.invokeGet(webTarget, SgPeriodoCalendario.class);
    }

    public void eliminar(Long calendarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calendarioPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendariosescolares");
        webTarget = webTarget.path(calendarioPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long calendarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calendarioPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calendariosescolares/historial");
        webTarget = webTarget.path(calendarioPk.toString());
        RevHistorico[] calendarios = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(calendarios);
    }

}
