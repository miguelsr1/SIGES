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
import javax.cache.annotation.CacheResult;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanGrado;

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
@Timeout(value = 40000L)
public class ComponentePlanGradoRestClient implements Serializable {

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

    public ComponentePlanGradoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgComponentePlanGrado> buscar(FiltroComponentePlanGrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplangrado/buscar");
        SgComponentePlanGrado[] componentesPlanGrado = restClient.invokePost(webTarget, filtro, SgComponentePlanGrado[].class);
        return Arrays.asList(componentesPlanGrado);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.COMPONENTE_PLAN_GRADO_CACHE)
    public List<SgComponentePlanGrado> buscarConCache(FiltroComponentePlanGrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplangrado/buscar");
        SgComponentePlanGrado[] componentesPlanGrado = restClient.invokePost(webTarget, filtro, SgComponentePlanGrado[].class);
        return Arrays.asList(componentesPlanGrado);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroComponentePlanGrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplangrado/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgComponentePlanGrado guardar(SgComponentePlanGrado componentePlanGrado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componentePlanGrado == null) {
            return null;
        }
        if (componentePlanGrado.getCpgPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplangrado");
            return restClient.invokePost(webTarget, componentePlanGrado, SgComponentePlanGrado.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplangrado");
            webTarget = webTarget.path(componentePlanGrado.getCpgPk().toString());
            return restClient.invokePut(webTarget, componentePlanGrado, SgComponentePlanGrado.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgComponentePlanGrado obtenerPorId(Long componentePlanGradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componentePlanGradoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplangrado");
        webTarget = webTarget.path(componentePlanGradoPk.toString());
        return restClient.invokeGet(webTarget, SgComponentePlanGrado.class);
    }

    public void eliminar(Long componentePlanGradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componentePlanGradoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplangrado");
        webTarget = webTarget.path(componentePlanGradoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long componentePlanGradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componentePlanGradoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplangrado/historial");
        webTarget = webTarget.path(componentePlanGradoPk.toString());
        RevHistorico[] componentesPlanGrado = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(componentesPlanGrado);
    }
    
    
    public void eliminarComponentePlanGrado(SgComponentePlanGrado componentePlanGrado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componentePlanGrado == null) {
            return ;
        }
       
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplangrado/eliminarComponentePlanGrado");
            webTarget = webTarget.path(componentePlanGrado.getCpgPk().toString());
            restClient.invokePut(webTarget, componentePlanGrado, SgComponentePlanGrado.class);
            return;
    }

}
