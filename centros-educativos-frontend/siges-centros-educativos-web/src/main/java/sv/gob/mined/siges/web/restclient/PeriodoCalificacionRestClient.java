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
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalificacion;

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
public class PeriodoCalificacionRestClient implements Serializable {

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

    public PeriodoCalificacionRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPeriodoCalificacion> buscar(FiltroPeriodoCalificacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/periodoscalificacion/buscar");
        SgPeriodoCalificacion[] periodosCalificacion = restClient.invokePost(webTarget, filtro, SgPeriodoCalificacion[].class);
        return Arrays.asList(periodosCalificacion);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroPeriodoCalificacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/periodoscalificacion/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgPeriodoCalificacion guardar(SgPeriodoCalificacion periodoCalificacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (periodoCalificacion == null) {
            return null;
        }
        if (periodoCalificacion.getPcaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/periodoscalificacion");
            return restClient.invokePost(webTarget, periodoCalificacion, SgPeriodoCalificacion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/periodoscalificacion");
            webTarget = webTarget.path(periodoCalificacion.getPcaPk().toString());
            return restClient.invokePut(webTarget, periodoCalificacion, SgPeriodoCalificacion.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgPeriodoCalificacion obtenerPorId(Long periodoCalificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (periodoCalificacionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/periodoscalificacion");
        webTarget = webTarget.path(periodoCalificacionPk.toString());
        return restClient.invokeGet(webTarget, SgPeriodoCalificacion.class);
    }

    public void eliminar(Long periodoCalificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (periodoCalificacionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/periodoscalificacion");
        webTarget = webTarget.path(periodoCalificacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<SgPeriodoCalificacion> obtenerHistorialPorId(Long periodoCalificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (periodoCalificacionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/periodoscalificacion/historial");
        webTarget = webTarget.path(periodoCalificacionPk.toString());
        SgPeriodoCalificacion[] periodosCalificacion = restClient.invokeGet(webTarget, SgPeriodoCalificacion[].class);
        return Arrays.asList(periodosCalificacion);
    }

}
