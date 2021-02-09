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
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgControlAsistenciaPersonalCabezal;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroControlAsistenciaPersonalCabezal;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class ControlAsistenciaPersonalCabezalRestClient implements Serializable {

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

    public ControlAsistenciaPersonalCabezalRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgControlAsistenciaPersonalCabezal> buscar(FiltroControlAsistenciaPersonalCabezal filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlesasistenciapersonalcabezal/buscar");
        SgControlAsistenciaPersonalCabezal[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgControlAsistenciaPersonalCabezal[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroControlAsistenciaPersonalCabezal filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlesasistenciapersonalcabezal/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgControlAsistenciaPersonalCabezal guardar(SgControlAsistenciaPersonalCabezal organizacionCurricular) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricular == null) {
            return null;
        }
        if (organizacionCurricular.getCpcPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlesasistenciapersonalcabezal");
            return restClient.invokePost(webTarget, organizacionCurricular, SgControlAsistenciaPersonalCabezal.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlesasistenciapersonalcabezal");
            webTarget = webTarget.path(organizacionCurricular.getCpcPk().toString());
            return restClient.invokePut(webTarget, organizacionCurricular, SgControlAsistenciaPersonalCabezal.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgControlAsistenciaPersonalCabezal obtenerPorId(Long controlAsistenciaPersonalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPersonalPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlesasistenciapersonalcabezal");
        webTarget = webTarget.path(controlAsistenciaPersonalPk.toString());
        return restClient.invokeGet(webTarget, SgControlAsistenciaPersonalCabezal.class);
    }

    public void eliminar(Long controlAsistenciaPersonalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPersonalPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlesasistenciapersonalcabezal");
        webTarget = webTarget.path(controlAsistenciaPersonalPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long controlAsistenciaPersonalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPersonalPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlesasistenciapersonalcabezal/historial");
        webTarget = webTarget.path(controlAsistenciaPersonalPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgControlAsistenciaPersonalCabezal obtenerEnRevision(Long controlAsistenciaPersonalPk, Long controlAsistenciaPersonalRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPersonalPk == null || controlAsistenciaPersonalRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlesasistenciapersonalcabezal/revision");
        webTarget = webTarget.path(controlAsistenciaPersonalPk.toString());
        webTarget = webTarget.path(controlAsistenciaPersonalRev.toString());
        return restClient.invokeGet(webTarget, SgControlAsistenciaPersonalCabezal.class);
    }

}
