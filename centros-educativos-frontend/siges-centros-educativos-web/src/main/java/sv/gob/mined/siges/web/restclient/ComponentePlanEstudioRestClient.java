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
import javax.cache.annotation.CacheKey;
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
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanEstudio;

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
public class ComponentePlanEstudioRestClient implements Serializable {

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

    public ComponentePlanEstudioRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgComponentePlanEstudio> buscar(FiltroComponentePlanEstudio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplanestudio/buscar");
        SgComponentePlanEstudio[] componentePlanEstudio = restClient.invokePost(webTarget, filtro, SgComponentePlanEstudio[].class);
        return Arrays.asList(componentePlanEstudio);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroComponentePlanEstudio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplanestudio/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgComponentePlanEstudio obtenerPorId(Long componentePlanEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componentePlanEstudioPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplanestudio");
        webTarget = webTarget.path(componentePlanEstudioPk.toString());
        return restClient.invokeGet(webTarget, SgComponentePlanEstudio.class);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.COMPONENTE_PLAN_ESTUDIO_NUMERICO_CURSADO_ESTUDIANTE_CACHE)
    public List<SgComponentePlanEstudio> obtenerComponentesPlanEstudioNumericosCursadosEstudiante(@CacheKey Long estudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplanestudio/buscar/cursadosestudiantenumericos");
        webTarget = webTarget.path(estudiantePk.toString());
        SgComponentePlanEstudio[] componentes = restClient.invokeGet(webTarget, SgComponentePlanEstudio[].class);
        return Arrays.asList(componentes);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.COMPONENTE_PLAN_ESTUDIO_NUMERICO_ACTUALES_ORD_ESTUDIANTE_CACHE)
    public List<SgComponentePlanEstudio> obtenerComponentesPlanEstudioNumericosActualesEstudiante(@CacheKey Long estudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplanestudio/buscar/actualesordestudiantenumericos");
        webTarget = webTarget.path(estudiantePk.toString());
        SgComponentePlanEstudio[] componentes = restClient.invokeGet(webTarget, SgComponentePlanEstudio[].class);
        return Arrays.asList(componentes);
    }
    
    
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.COMPONENTE_PLAN_ESTUDIO_CONCEPTUAL_CURSADO_ESTUDIANTE_CACHE)
    public List<SgComponentePlanEstudio> obtenerComponentesPlanEstudioConceptualesCursadosEstudiante(@CacheKey Long estudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplanestudio/buscar/cursadosestudianteconceptuales");
        webTarget = webTarget.path(estudiantePk.toString());
        SgComponentePlanEstudio[] componentes = restClient.invokeGet(webTarget, SgComponentePlanEstudio[].class);
        return Arrays.asList(componentes);
    }
    

    public List<RevHistorico> obtenerHistorialPorId(Long componentePlanEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componentePlanEstudioPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplanestudio/historial");
        webTarget = webTarget.path(componentePlanEstudioPk.toString());
        RevHistorico[] componentePlanEstudio = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(componentePlanEstudio);
    }

    public void eliminar(Long componentePlanEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componentePlanEstudioPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplanestudio");
        webTarget = webTarget.path(componentePlanEstudioPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public SgComponentePlanEstudio obtenerEnRevision(Long componentePlanEstudioPk, Long componentePlanEstudioRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componentePlanEstudioPk == null || componentePlanEstudioRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesplanestudio/revision");
        webTarget = webTarget.path(componentePlanEstudioPk.toString());
        webTarget = webTarget.path(componentePlanEstudioRev.toString());
        return restClient.invokeGet(webTarget, SgComponentePlanEstudio.class);
    }

    
}
