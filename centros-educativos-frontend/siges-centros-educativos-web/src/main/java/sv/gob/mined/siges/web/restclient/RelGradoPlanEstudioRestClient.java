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
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgRelGradoPlanEstudio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPlanEstudio;

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
public class RelGradoPlanEstudioRestClient implements Serializable {

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

    
    public RelGradoPlanEstudioRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRelGradoPlanEstudio> buscar(FiltroRelGradoPlanEstudio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoplanestudio/buscar");
        SgRelGradoPlanEstudio[] relGradoPlanEstudio = restClient.invokePost(webTarget, filtro, SgRelGradoPlanEstudio[].class);
        return Arrays.asList(relGradoPlanEstudio);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.REL_GRADO_PLAN_ESTUDIO_CACHE)
    public List<SgRelGradoPlanEstudio> buscarConCache(FiltroRelGradoPlanEstudio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoplanestudio/buscar");
        SgRelGradoPlanEstudio[] relGradoPlanEstudio = restClient.invokePost(webTarget, filtro, SgRelGradoPlanEstudio[].class);
        return Arrays.asList(relGradoPlanEstudio);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoplanestudio/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelGradoPlanEstudio guardar(SgRelGradoPlanEstudio relGradoPlanEstudio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relGradoPlanEstudio == null) {
            return null;
        }
        if (relGradoPlanEstudio.getRgpPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoplanestudio");
            return restClient.invokePost(webTarget, relGradoPlanEstudio, SgRelGradoPlanEstudio.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoplanestudio");
            webTarget = webTarget.path(relGradoPlanEstudio.getRgpPk().toString());
            return restClient.invokePut(webTarget, relGradoPlanEstudio, SgRelGradoPlanEstudio.class);
        }
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgRelGradoPlanEstudio obtenerPorId(Long relGradoPlanEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relGradoPlanEstudioPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoplanestudio");
        webTarget = webTarget.path(relGradoPlanEstudioPk.toString());
        return restClient.invokeGet(webTarget, SgRelGradoPlanEstudio.class);
    }

    public void eliminar(Long relGradoPlanEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relGradoPlanEstudioPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoplanestudio");
        webTarget = webTarget.path(relGradoPlanEstudioPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRelGradoPlanEstudio> obtenerHistorialPorId(Long relGradoPlanEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relGradoPlanEstudioPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoplanestudio/historial");
        webTarget = webTarget.path(relGradoPlanEstudioPk.toString());
        SgRelGradoPlanEstudio[] relGradoPlanEstudio = restClient.invokeGet(webTarget, SgRelGradoPlanEstudio[].class);
        return Arrays.asList(relGradoPlanEstudio);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgDefinicionTitulo> buscarDefinicionTitulo(FiltroRelGradoPlanEstudio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoplanestudio/buscarDefinicionTitulo");
        SgDefinicionTitulo[] relGradoPlanEstudio = restClient.invokePost(webTarget, filtro, SgDefinicionTitulo[].class);
        return Arrays.asList(relGradoPlanEstudio);
    }
    

}
