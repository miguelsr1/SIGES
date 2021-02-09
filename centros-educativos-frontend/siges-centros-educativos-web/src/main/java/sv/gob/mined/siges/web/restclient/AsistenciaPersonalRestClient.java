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
import sv.gob.mined.siges.web.dto.SgAsistenciaPersonal;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsistenciaPersonal;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class AsistenciaPersonalRestClient implements Serializable {

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

    public AsistenciaPersonalRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAsistenciaPersonal> buscar(FiltroAsistenciaPersonal filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciaspersonal/buscar");
        SgAsistenciaPersonal[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgAsistenciaPersonal[].class);
        return new ArrayList(Arrays.asList(organizacionesCurricular));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroAsistenciaPersonal filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciaspersonal/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAsistenciaPersonal guardar(SgAsistenciaPersonal organizacionCurricular) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricular == null) {
            return null;
        }
        if (organizacionCurricular.getApePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciaspersonal");
            return restClient.invokePost(webTarget, organizacionCurricular, SgAsistenciaPersonal.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciaspersonal");
            webTarget = webTarget.path(organizacionCurricular.getApePk().toString());
            return restClient.invokePut(webTarget, organizacionCurricular, SgAsistenciaPersonal.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAsistenciaPersonal obtenerPorId(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciaspersonal");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        return restClient.invokeGet(webTarget, SgAsistenciaPersonal.class);
    }

    public void eliminar(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciaspersonal");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<SgAsistenciaPersonal> obtenerHistorialPorId(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciaspersonal/historial");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        SgAsistenciaPersonal[] organizacionesCurricular = restClient.invokeGet(webTarget, SgAsistenciaPersonal[].class);
        return Arrays.asList(organizacionesCurricular);
    }

}
