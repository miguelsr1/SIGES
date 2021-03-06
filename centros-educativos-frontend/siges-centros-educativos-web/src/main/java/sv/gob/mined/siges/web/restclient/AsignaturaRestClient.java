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
import sv.gob.mined.siges.web.dto.SgAsignatura;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;

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
public class AsignaturaRestClient implements Serializable {

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

    public AsignaturaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAsignatura> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignaturas/buscar");
        SgAsignatura[] asignaturas = restClient.invokePost(webTarget, filtro, SgAsignatura[].class);
        return Arrays.asList(asignaturas);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignaturas/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgComponentePlanEstudio guardar(SgComponentePlanEstudio asignatura) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asignatura == null) {
            return null;
        }
        if (asignatura.getCpePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignaturas");
            return restClient.invokePost(webTarget, asignatura, SgAsignatura.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignaturas");
            webTarget = webTarget.path(asignatura.getCpePk().toString());
            return restClient.invokePut(webTarget, asignatura, SgAsignatura.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAsignatura obtenerPorId(Long asignaturaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asignaturaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignaturas");
        webTarget = webTarget.path(asignaturaPk.toString());
        return restClient.invokeGet(webTarget, SgAsignatura.class);
    }

    public void eliminar(Long asignaturaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asignaturaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignaturas");
        webTarget = webTarget.path(asignaturaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long asignaturaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asignaturaPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignaturas/historial");
        webTarget = webTarget.path(asignaturaPk.toString());
        RevHistorico[] asignaturas = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(asignaturas);
    }

}
