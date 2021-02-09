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
import sv.gob.mined.siges.web.dto.SgHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPeriodosOrdExord;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Traced
@Timeout(value = 3000L)
public class HabilitacionPeriodoCalificacionRestClient implements Serializable {

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
    public HabilitacionPeriodoCalificacionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgHabilitacionPeriodoCalificacion> buscar(FiltroHabilitacionPeriodoCalificacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habilitacionperiodocalificacion/buscar");
        SgHabilitacionPeriodoCalificacion[] habilitacionPeriodoCalificacion = restClient.invokePost(webTarget, filtro, SgHabilitacionPeriodoCalificacion[].class);
        return Arrays.asList(habilitacionPeriodoCalificacion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroHabilitacionPeriodoCalificacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habilitacionperiodocalificacion/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgHabilitacionPeriodoCalificacion guardar(SgHabilitacionPeriodoCalificacion habilitacionPeriodoCalificacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoCalificacion == null) {
            return null;
        }
        if (habilitacionPeriodoCalificacion.getHpcPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habilitacionperiodocalificacion");
            return restClient.invokePost(webTarget, habilitacionPeriodoCalificacion, SgHabilitacionPeriodoCalificacion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habilitacionperiodocalificacion");
            webTarget = webTarget.path(habilitacionPeriodoCalificacion.getHpcPk().toString());
            return restClient.invokePut(webTarget, habilitacionPeriodoCalificacion, SgHabilitacionPeriodoCalificacion.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgHabilitacionPeriodoCalificacion obtenerPorId(Long habilitacionPeriodoCalificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoCalificacionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habilitacionperiodocalificacion");
        webTarget = webTarget.path(habilitacionPeriodoCalificacionPk.toString());
        return restClient.invokeGet(webTarget, SgHabilitacionPeriodoCalificacion.class);
    }

    public void eliminar(Long habilitacionPeriodoCalificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoCalificacionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habilitacionperiodocalificacion");
        webTarget = webTarget.path(habilitacionPeriodoCalificacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long habilitacionPeriodoCalificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoCalificacionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habilitacionperiodocalificacion/historial");
        webTarget = webTarget.path(habilitacionPeriodoCalificacionPk.toString());
        RevHistorico[] habilitacionPeriodoCalificacion = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(habilitacionPeriodoCalificacion);

    }
    
    @Timeout(value = 6000L)
    public List<SgMatricula> buscarEstudiante(FiltroEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habilitacionperiodocalificacion/buscarEstudiante");
        SgMatricula[] estudiante = restClient.invokePost(webTarget, filtro, SgMatricula[].class);
        return Arrays.asList(estudiante);
    }
    
    public SgPeriodosOrdExord buscarPeriodos(SgMatricula mat) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (mat == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habilitacionperiodocalificacion/buscarPeriodos");
        return restClient.invokePost(webTarget, mat, SgPeriodosOrdExord.class);
    }
    
    @Timeout(value = 5000000L)
    public SgHabilitacionPeriodoCalificacion aprobar(SgHabilitacionPeriodoCalificacion habilitacionPeriodoCalificacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoCalificacion == null) {
            return null;
        }
       
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habilitacionperiodocalificacion/aprobar");
        return restClient.invokePost(webTarget, habilitacionPeriodoCalificacion, SgHabilitacionPeriodoCalificacion.class);
     
    }
    
    public SgHabilitacionPeriodoCalificacion rechazar(SgHabilitacionPeriodoCalificacion habilitacionPeriodoCalificacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoCalificacion == null) {
            return null;
        }
       
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habilitacionperiodocalificacion/rechazar");
        return restClient.invokePost(webTarget, habilitacionPeriodoCalificacion, SgHabilitacionPeriodoCalificacion.class);
     
    }

}
