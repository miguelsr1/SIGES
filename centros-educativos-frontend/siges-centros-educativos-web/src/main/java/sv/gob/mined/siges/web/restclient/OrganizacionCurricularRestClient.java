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
import sv.gob.mined.siges.web.dto.SgOrganizacionCurricular;
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
public class OrganizacionCurricularRestClient implements Serializable {

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

    public OrganizacionCurricularRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgOrganizacionCurricular> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organizacionescurricular/buscar");
        SgOrganizacionCurricular[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgOrganizacionCurricular[].class);
        return Arrays.asList(organizacionesCurricular);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.ORG_CURRICULAR_CACHE)
    public List<SgOrganizacionCurricular> buscarConCache(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organizacionescurricular/buscar");
        SgOrganizacionCurricular[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgOrganizacionCurricular[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organizacionescurricular/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgOrganizacionCurricular obtenerPorId(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organizacionescurricular");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        return restClient.invokeGet(webTarget, SgOrganizacionCurricular.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.ORG_LAZY_CACHE) //Solamente utilizado para setear la Organizacion Curricular en otra entidad
    public SgOrganizacionCurricular obtenerPorIdLazy(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organizacionescurricular/lazy");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        return restClient.invokeGet(webTarget, SgOrganizacionCurricular.class);
    }
    
    public SgOrganizacionCurricular guardar(SgOrganizacionCurricular organizacionCurricular) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricular == null) {
            return null;
        }
        if (organizacionCurricular.getOcuPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organizacionescurricular");
            return restClient.invokePost(webTarget, organizacionCurricular, SgOrganizacionCurricular.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organizacionescurricular");
            webTarget = webTarget.path(organizacionCurricular.getOcuPk().toString());
            return restClient.invokePut(webTarget, organizacionCurricular, SgOrganizacionCurricular.class);
        }
    }

    public void eliminar(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organizacionescurricular");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organizacionescurricular/historial");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgOrganizacionCurricular obtenerEnRevision(Long ocuPk, Long ocuRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ocuPk == null || ocuRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organizacionescurricular/revision");
        webTarget = webTarget.path(ocuPk.toString());
        webTarget = webTarget.path(ocuRev.toString());
        return restClient.invokeGet(webTarget, SgOrganizacionCurricular.class);
    }

}
