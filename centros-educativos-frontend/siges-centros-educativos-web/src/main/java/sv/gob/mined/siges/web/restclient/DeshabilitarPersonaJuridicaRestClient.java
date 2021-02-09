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
import sv.gob.mined.siges.web.dto.SgSolDeshabilitarPerJur;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudesOAE;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class DeshabilitarPersonaJuridicaRestClient implements Serializable {

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

    public DeshabilitarPersonaJuridicaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgSolDeshabilitarPerJur> buscar(FiltroSolicitudesOAE filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/deshabilitarPersonaJuridicaRecurso/buscar");
        SgSolDeshabilitarPerJur[] solicitudes = restClient.invokePost(webTarget, filtro, SgSolDeshabilitarPerJur[].class);
        return new ArrayList<>(Arrays.asList(solicitudes));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroSolicitudesOAE filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/deshabilitarPersonaJuridicaRecurso/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgSolDeshabilitarPerJur obtenerPorId(Long solId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/deshabilitarPersonaJuridicaRecurso");
        webTarget = webTarget.path(solId.toString());
        return restClient.invokeGet(webTarget, SgSolDeshabilitarPerJur.class);
    }
    
    public SgSolDeshabilitarPerJur guardar(SgSolDeshabilitarPerJur entity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (entity == null) {
            return null;
        }
        if (entity.getDpjPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/deshabilitarPersonaJuridicaRecurso");
            return restClient.invokePost(webTarget, entity, SgSolDeshabilitarPerJur.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/deshabilitarPersonaJuridicaRecurso");
            webTarget = webTarget.path(entity.getDpjPk().toString());
            return restClient.invokePut(webTarget, entity, SgSolDeshabilitarPerJur.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long solId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/deshabilitarPersonaJuridicaRecurso/historial");
        webTarget = webTarget.path(solId.toString());
        RevHistorico[] sede = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(sede);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgSolDeshabilitarPerJur obtenerEnRevision(Long solId, Long estudianteRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solId == null || estudianteRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/deshabilitarPersonaJuridicaRecurso/revision");
        webTarget = webTarget.path(solId.toString());
        webTarget = webTarget.path(estudianteRev.toString());
        return restClient.invokeGet(webTarget, SgSolDeshabilitarPerJur.class);
    }
}
