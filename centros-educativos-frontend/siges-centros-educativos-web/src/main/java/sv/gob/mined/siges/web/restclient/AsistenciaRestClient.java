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
import sv.gob.mined.siges.web.dto.SgAsistencia;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsistencia;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class AsistenciaRestClient implements Serializable {

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

    public AsistenciaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAsistencia> buscar(FiltroAsistencia filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistencias/buscar");
        SgAsistencia[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgAsistencia[].class);
        return new ArrayList(Arrays.asList(organizacionesCurricular));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroAsistencia filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistencias/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAsistencia guardar(SgAsistencia organizacionCurricular) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricular == null) {
            return null;
        }
        if (organizacionCurricular.getAsiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistencias");
            return restClient.invokePost(webTarget, organizacionCurricular, SgAsistencia.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistencias");
            webTarget = webTarget.path(organizacionCurricular.getAsiPk().toString());
            return restClient.invokePut(webTarget, organizacionCurricular, SgAsistencia.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAsistencia obtenerPorId(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistencias");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        return restClient.invokeGet(webTarget, SgAsistencia.class);
    }

    public void eliminar(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistencias");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<SgAsistencia> obtenerHistorialPorId(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistencias/historial");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        SgAsistencia[] organizacionesCurricular = restClient.invokeGet(webTarget, SgAsistencia[].class);
        return Arrays.asList(organizacionesCurricular);
    }

}
