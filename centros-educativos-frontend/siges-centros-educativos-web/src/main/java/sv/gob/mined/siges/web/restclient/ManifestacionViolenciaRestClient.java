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
import sv.gob.mined.siges.web.dto.SgManifestacionViolencia;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroManifestacionViolencia;

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
public class ManifestacionViolenciaRestClient implements Serializable {

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

    public ManifestacionViolenciaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgManifestacionViolencia> buscar(FiltroManifestacionViolencia filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/manifestacionesviolencia/buscar");
        SgManifestacionViolencia[] manifestacionesviolencia = restClient.invokePost(webTarget, filtro, SgManifestacionViolencia[].class);
        return Arrays.asList(manifestacionesviolencia);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroManifestacionViolencia filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/manifestacionesviolencia/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgManifestacionViolencia guardar(SgManifestacionViolencia manifestacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (manifestacion == null) {
            return null;
        }
        if (manifestacion.getMviPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/manifestacionesviolencia");
            return restClient.invokePost(webTarget, manifestacion, SgManifestacionViolencia.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/manifestacionesviolencia");
            webTarget = webTarget.path(manifestacion.getMviPk().toString());
            return restClient.invokePut(webTarget, manifestacion, SgManifestacionViolencia.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgManifestacionViolencia obtenerPorId(Long manifestacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (manifestacionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/manifestacionesviolencia");
        webTarget = webTarget.path(manifestacionPk.toString());
        return restClient.invokeGet(webTarget, SgManifestacionViolencia.class);
    }

    public SgManifestacionViolencia obtenerPorIdLazy(Long manifestacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (manifestacionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/manifestacionesviolencia/lazy/");
        webTarget = webTarget.path(manifestacionPk.toString());
        return restClient.invokeGet(webTarget, SgManifestacionViolencia.class);
    }

    public void eliminar(Long manifestacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (manifestacionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/manifestacionesviolencia");
        webTarget = webTarget.path(manifestacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long manifestacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (manifestacionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/manifestacionesviolencia/historial");
        webTarget = webTarget.path(manifestacionPk.toString());
        RevHistorico[] manifestacionesviolencia = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(manifestacionesviolencia);
    }

}
