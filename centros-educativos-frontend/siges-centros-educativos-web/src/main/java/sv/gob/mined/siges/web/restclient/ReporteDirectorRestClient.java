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
import sv.gob.mined.siges.web.dto.SgReporteDirector;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReporteDirector;

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
public class ReporteDirectorRestClient implements Serializable {

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

    public ReporteDirectorRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgReporteDirector> buscar(FiltroReporteDirector filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reportedirector/buscar");
        SgReporteDirector[] reporteDirector = restClient.invokePost(webTarget, filtro, SgReporteDirector[].class);
        return Arrays.asList(reporteDirector);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroReporteDirector filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reportedirector/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgReporteDirector guardar(SgReporteDirector reporteDirector) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reporteDirector == null) {
            return null;
        }
        if (reporteDirector.getRdiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reportedirector");
            return restClient.invokePost(webTarget, reporteDirector, SgReporteDirector.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reportedirector");
            webTarget = webTarget.path(reporteDirector.getRdiPk().toString());
            return restClient.invokePut(webTarget, reporteDirector, SgReporteDirector.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgReporteDirector obtenerPorId(Long reporteDirectorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reporteDirectorPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reportedirector");
        webTarget = webTarget.path(reporteDirectorPk.toString());
        return restClient.invokeGet(webTarget, SgReporteDirector.class);
    }

    public void eliminar(Long reporteDirectorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reporteDirectorPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reportedirector");
        webTarget = webTarget.path(reporteDirectorPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<SgReporteDirector> obtenerHistorialPorId(Long reporteDirectorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reporteDirectorPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reportedirector/historial");
        webTarget = webTarget.path(reporteDirectorPk.toString());
        SgReporteDirector[] reporteDirector = restClient.invokeGet(webTarget, SgReporteDirector[].class);
        return Arrays.asList(reporteDirector);
    }

}
