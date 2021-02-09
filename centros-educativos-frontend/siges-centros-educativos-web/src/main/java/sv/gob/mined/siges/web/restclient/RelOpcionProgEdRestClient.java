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
import sv.gob.mined.siges.web.dto.SgRelOpcionProgEd;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelOpcionProgEd;

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
public class RelOpcionProgEdRestClient implements Serializable {

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

    public RelOpcionProgEdRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRelOpcionProgEd> buscar(FiltroRelOpcionProgEd filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged/buscar");
        SgRelOpcionProgEd[] relOpcionProgEd = restClient.invokePost(webTarget, filtro, SgRelOpcionProgEd[].class);
        return Arrays.asList(relOpcionProgEd);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.REL_OPCION_PROG_CACHE)
    public List<SgRelOpcionProgEd> buscarConCache(FiltroRelOpcionProgEd filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged/buscar");
        SgRelOpcionProgEd[] relOpcionProgEd = restClient.invokePost(webTarget, filtro, SgRelOpcionProgEd[].class);
        return Arrays.asList(relOpcionProgEd);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroRelOpcionProgEd filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelOpcionProgEd guardar(SgRelOpcionProgEd relOpcionProgEd) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOpcionProgEd == null) {
            return null;
        }
        if (relOpcionProgEd.getRoePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged");
            return restClient.invokePost(webTarget, relOpcionProgEd, SgRelOpcionProgEd.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged");
            webTarget = webTarget.path(relOpcionProgEd.getRoePk().toString());
            return restClient.invokePut(webTarget, relOpcionProgEd, SgRelOpcionProgEd.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgRelOpcionProgEd obtenerPorId(Long relOpcionProgEdPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOpcionProgEdPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged");
        webTarget = webTarget.path(relOpcionProgEdPk.toString());
        return restClient.invokeGet(webTarget, SgRelOpcionProgEd.class);
    }

    public void eliminar(Long relOpcionProgEdPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOpcionProgEdPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged");
        webTarget = webTarget.path(relOpcionProgEdPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long relOpcionProgEdPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOpcionProgEdPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged/historial");
        webTarget = webTarget.path(relOpcionProgEdPk.toString());
        RevHistorico[] relOpcionProgEd = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relOpcionProgEd);
    }

    public SgRelOpcionProgEd obtenerEnRevision(Long relOpcionProgEdPk, Long relOpcionProgEdRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOpcionProgEdPk == null || relOpcionProgEdRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged/revision");
        webTarget = webTarget.path(relOpcionProgEdPk.toString());
        webTarget = webTarget.path(relOpcionProgEdRev.toString());
        return restClient.invokeGet(webTarget, SgRelOpcionProgEd.class);
    }

}
