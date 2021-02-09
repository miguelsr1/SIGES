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
import sv.gob.mined.siges.web.dto.SgPropuestaPedagogica;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPropuestaPedagogica;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class PropuestaPedagogicaRestClient implements Serializable {

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

    public PropuestaPedagogicaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPropuestaPedagogica> buscar(FiltroPropuestaPedagogica filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/propuestaspedagogicas/buscar");
        SgPropuestaPedagogica[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgPropuestaPedagogica[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroPropuestaPedagogica filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/propuestaspedagogicas/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgPropuestaPedagogica guardar(SgPropuestaPedagogica propuestaPedagogica) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (propuestaPedagogica == null) {
            return null;
        }
        if (propuestaPedagogica.getPpePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/propuestaspedagogicas");
            return restClient.invokePost(webTarget, propuestaPedagogica, SgPropuestaPedagogica.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/propuestaspedagogicas");
            webTarget = webTarget.path(propuestaPedagogica.getPpePk().toString());
            return restClient.invokePut(webTarget, propuestaPedagogica, SgPropuestaPedagogica.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgPropuestaPedagogica obtenerPorId(Long propuestaPedagogicaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (propuestaPedagogicaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/propuestaspedagogicas");
        webTarget = webTarget.path(propuestaPedagogicaPk.toString());
        return restClient.invokeGet(webTarget, SgPropuestaPedagogica.class);
    }

    public void eliminar(Long propuestaPedagogicaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (propuestaPedagogicaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/propuestaspedagogicas");
        webTarget = webTarget.path(propuestaPedagogicaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long propuestaPedagogicaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (propuestaPedagogicaPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/propuestaspedagogicas/historial");
        webTarget = webTarget.path(propuestaPedagogicaPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgPropuestaPedagogica obtenerEnRevision(Long propuestaPedagogicaPk, Long propuestaPedagogicaRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (propuestaPedagogicaPk == null || propuestaPedagogicaRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/propuestaspedagogicas/revision");
        webTarget = webTarget.path(propuestaPedagogicaPk.toString());
        webTarget = webTarget.path(propuestaPedagogicaRev.toString());
        return restClient.invokeGet(webTarget, SgPropuestaPedagogica.class);
    }

}
