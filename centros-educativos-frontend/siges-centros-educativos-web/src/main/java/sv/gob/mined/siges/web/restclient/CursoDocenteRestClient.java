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
import sv.gob.mined.siges.web.dto.SgCursoDocente;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCursoDocente;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class CursoDocenteRestClient implements Serializable {

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

    public CursoDocenteRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgCursoDocente> buscar(FiltroCursoDocente filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cursosdocentes/buscar");
        SgCursoDocente[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgCursoDocente[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCursoDocente filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cursosdocentes/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCursoDocente guardar(SgCursoDocente organizacionCurricular) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricular == null) {
            return null;
        }
        if (organizacionCurricular.getCdsPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cursosdocentes");
            return restClient.invokePost(webTarget, organizacionCurricular, SgCursoDocente.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cursosdocentes");
            webTarget = webTarget.path(organizacionCurricular.getCdsPk().toString());
            return restClient.invokePut(webTarget, organizacionCurricular, SgCursoDocente.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCursoDocente obtenerPorId(Long controlAsistenciaPersonalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPersonalPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cursosdocentes");
        webTarget = webTarget.path(controlAsistenciaPersonalPk.toString());
        return restClient.invokeGet(webTarget, SgCursoDocente.class);
    }

    public void eliminar(Long controlAsistenciaPersonalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPersonalPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cursosdocentes");
        webTarget = webTarget.path(controlAsistenciaPersonalPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long controlAsistenciaPersonalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPersonalPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cursosdocentes/historial");
        webTarget = webTarget.path(controlAsistenciaPersonalPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgCursoDocente obtenerEnRevision(Long controlAsistenciaPersonalPk, Long controlAsistenciaPersonalRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (controlAsistenciaPersonalPk == null || controlAsistenciaPersonalRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cursosdocentes/revision");
        webTarget = webTarget.path(controlAsistenciaPersonalPk.toString());
        webTarget = webTarget.path(controlAsistenciaPersonalRev.toString());
        return restClient.invokeGet(webTarget, SgCursoDocente.class);
    }

    public SgCursoDocente publicar(SgCursoDocente organizacionCurricular) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricular == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cursosdocentes/publicar");
        webTarget = webTarget.path(organizacionCurricular.getCdsPk().toString());
        return restClient.invokePut(webTarget, organizacionCurricular, SgCursoDocente.class);
        
    }

    public SgCursoDocente cerrar(SgCursoDocente organizacionCurricular) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricular == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cursosdocentes/cerrar");
        webTarget = webTarget.path(organizacionCurricular.getCdsPk().toString());
        return restClient.invokePut(webTarget, organizacionCurricular, SgCursoDocente.class);
        
    }

}
