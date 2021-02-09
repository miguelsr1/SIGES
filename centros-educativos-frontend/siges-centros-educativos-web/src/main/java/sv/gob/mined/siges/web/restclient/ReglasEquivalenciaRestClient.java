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
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgGradoPlanOrigenDestino;
import sv.gob.mined.siges.web.dto.SgReglaEquivalencia;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReglaEquivalencia;

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
public class ReglasEquivalenciaRestClient implements Serializable {

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

    public ReglasEquivalenciaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgReglaEquivalencia> buscar(FiltroReglaEquivalencia filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaequivalencia/buscar");
        SgReglaEquivalencia[] rangosFecha = restClient.invokePost(webTarget, filtro, SgReglaEquivalencia[].class);
        return Arrays.asList(rangosFecha);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroReglaEquivalencia filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaequivalencia/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgReglaEquivalencia guardar(SgReglaEquivalencia regla) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (regla == null) {
            return null;
        }
        if (regla.getReqPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaequivalencia");
            return restClient.invokePost(webTarget, regla, SgReglaEquivalencia.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaequivalencia");
            webTarget = webTarget.path(regla.getReqPk().toString());
            return restClient.invokePut(webTarget, regla, SgReglaEquivalencia.class);
        }
    }

    public Boolean cumple(SgGradoPlanOrigenDestino obj) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (obj == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaequivalencia/cumple");
        return restClient.invokePost(webTarget, obj, Boolean.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgReglaEquivalencia obtenerPorId(Long reglaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reglaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaequivalencia");
        webTarget = webTarget.path(reglaPk.toString());
        return restClient.invokeGet(webTarget, SgReglaEquivalencia.class);
    }

    public void eliminar(Long reglaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reglaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaequivalencia");
        webTarget = webTarget.path(reglaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long reglaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reglaPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaequivalencia/historial");
        webTarget = webTarget.path(reglaPk.toString());
        RevHistorico[] reglas = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(reglas);
    }
}
