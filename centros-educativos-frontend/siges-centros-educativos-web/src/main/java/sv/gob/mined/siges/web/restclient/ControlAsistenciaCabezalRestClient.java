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
import sv.gob.mined.siges.web.dto.SgConsultaAsistenciasSedesEnNivel;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgControlAsistenciaCabezal;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsistenciasSedeEnNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroControlAsistenciaCabezal;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 20000L)
public class ControlAsistenciaCabezalRestClient implements Serializable {

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

    public ControlAsistenciaCabezalRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgControlAsistenciaCabezal> buscar(FiltroControlAsistenciaCabezal filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlasistenciacabezal/buscar");
        SgControlAsistenciaCabezal[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgControlAsistenciaCabezal[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroControlAsistenciaCabezal filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlasistenciacabezal/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgControlAsistenciaCabezal guardar(SgControlAsistenciaCabezal organizacionCurricular) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricular == null) {
            return null;
        }
        if (organizacionCurricular.getCacPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlasistenciacabezal");
            return restClient.invokePost(webTarget, organizacionCurricular, SgControlAsistenciaCabezal.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlasistenciacabezal");
            webTarget = webTarget.path(organizacionCurricular.getCacPk().toString());
            return restClient.invokePut(webTarget, organizacionCurricular, SgControlAsistenciaCabezal.class);
        }
    }
    

    @Timeout(value = 60000L)
    public void importar(String[][] lista) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (lista == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.FILE_PROCESSOR, "v1/controlasistenciacabezal/importar");
        restClient.invokePost(webTarget, lista, null);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgControlAsistenciaCabezal obtenerPorId(Long controlAsistenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlasistenciacabezal");
        webTarget = webTarget.path(controlAsistenciaPk.toString());
        return restClient.invokeGet(webTarget, SgControlAsistenciaCabezal.class);
    }

    public void eliminar(Long controlAsistenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlasistenciacabezal");
        webTarget = webTarget.path(controlAsistenciaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long controlAsistenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlasistenciacabezal/historial");
        webTarget = webTarget.path(controlAsistenciaPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgControlAsistenciaCabezal obtenerEnRevision(Long controlAsistenciaPk, Long controlAsistenciaRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPk == null || controlAsistenciaRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlasistenciacabezal/revision");
        webTarget = webTarget.path(controlAsistenciaPk.toString());
        webTarget = webTarget.path(controlAsistenciaRev.toString());
        return restClient.invokeGet(webTarget, SgControlAsistenciaCabezal.class);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 30000L)
    public List<SgConsultaAsistenciasSedesEnNivel> obtenerAsistenciasPorSedeEnNivel(FiltroAsistenciasSedeEnNivel filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/controlasistenciacabezal/obtenerasistenciasporsedeennivel");
        SgConsultaAsistenciasSedesEnNivel[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, SgConsultaAsistenciasSedesEnNivel[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

}
