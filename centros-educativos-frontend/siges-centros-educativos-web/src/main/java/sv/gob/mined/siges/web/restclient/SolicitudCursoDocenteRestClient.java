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
import sv.gob.mined.siges.web.dto.SgSolicitudCursoDocente;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudCursoDocente;

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
public class SolicitudCursoDocenteRestClient implements Serializable {

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

    public SolicitudCursoDocenteRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgSolicitudCursoDocente> buscar(FiltroSolicitudCursoDocente filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudescursodocente/buscar");
        SgSolicitudCursoDocente[] solicitudCursoDocente = restClient.invokePost(webTarget, filtro, SgSolicitudCursoDocente[].class);
        return Arrays.asList(solicitudCursoDocente);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroSolicitudCursoDocente filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudescursodocente/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgSolicitudCursoDocente guardar(SgSolicitudCursoDocente solicitudCursoDocente) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudCursoDocente == null) {
            return null;
        }
        if (solicitudCursoDocente.getScdPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudescursodocente");
            return restClient.invokePost(webTarget, solicitudCursoDocente, SgSolicitudCursoDocente.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudescursodocente");
            webTarget = webTarget.path(solicitudCursoDocente.getScdPk().toString());
            return restClient.invokePut(webTarget, solicitudCursoDocente, SgSolicitudCursoDocente.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgSolicitudCursoDocente obtenerPorId(Long solicitudCursoDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudCursoDocentePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudescursodocente");
        webTarget = webTarget.path(solicitudCursoDocentePk.toString());
        return restClient.invokeGet(webTarget, SgSolicitudCursoDocente.class);
    }

    public void eliminar(Long solicitudCursoDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudCursoDocentePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudescursodocente");
        webTarget = webTarget.path(solicitudCursoDocentePk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long solicitudCursoDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudCursoDocentePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudescursodocente/historial");
        webTarget = webTarget.path(solicitudCursoDocentePk.toString());
        RevHistorico[] solicitudCursoDocente = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(solicitudCursoDocente);
    }

    public SgSolicitudCursoDocente obtenerEnRevision(Long solicitudCursoDocentePk, Long solicitudCursoDocenteRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudCursoDocentePk == null || solicitudCursoDocenteRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudescursodocente/revision");
        webTarget = webTarget.path(solicitudCursoDocentePk.toString());
        webTarget = webTarget.path(solicitudCursoDocenteRev.toString());
        return restClient.invokeGet(webTarget, SgSolicitudCursoDocente.class);
    }


    public SgSolicitudCursoDocente aprobar(SgSolicitudCursoDocente solicitudCursoDocente) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudCursoDocente == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudescursodocente/aprobar");
        webTarget = webTarget.path(solicitudCursoDocente.getScdPk().toString());
        return restClient.invokePut(webTarget, solicitudCursoDocente, SgSolicitudCursoDocente.class);
    }


    public SgSolicitudCursoDocente rechazar(SgSolicitudCursoDocente solicitudCursoDocente) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudCursoDocente == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudescursodocente/rechazar");
        webTarget = webTarget.path(solicitudCursoDocente.getScdPk().toString());
        return restClient.invokePut(webTarget, solicitudCursoDocente, SgSolicitudCursoDocente.class);
    }
}
