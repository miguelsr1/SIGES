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
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;

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
public class GradoRestClient implements Serializable {

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

    public GradoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgGrado> buscar(FiltroGrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/buscar");
        SgGrado[] grados = restClient.invokePost(webTarget, filtro, SgGrado[].class);
        return Arrays.asList(grados);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRADO_CACHE)
    public List<SgGrado> buscarConCache(FiltroGrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/buscar");
        SgGrado[] grados = restClient.invokePost(webTarget, filtro, SgGrado[].class);
        return Arrays.asList(grados);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroGrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgGrado guardar(SgGrado grado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (grado == null) {
            return null;
        }
        if (grado.getGraPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados");
            return restClient.invokePost(webTarget, grado, SgGrado.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados");
            webTarget = webTarget.path(grado.getGraPk().toString());
            return restClient.invokePut(webTarget, grado, SgGrado.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgGrado obtenerPorId(Long gradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados");
        webTarget = webTarget.path(gradoPk.toString());
        return restClient.invokeGet(webTarget, SgGrado.class);
    }

    public void eliminar(Long gradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados");
        webTarget = webTarget.path(gradoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long gradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/historial");
        webTarget = webTarget.path(gradoPk.toString());
        RevHistorico[] grados = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(grados);
    }

    public SgGrado obtenerEnRevision(Long gradoPk, Long gradoRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoPk == null || gradoRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/revision");
        webTarget = webTarget.path(gradoPk.toString());
        webTarget = webTarget.path(gradoRev.toString());
        return restClient.invokeGet(webTarget, SgGrado.class);
    }

    public Boolean validar(SgGrado grado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (grado == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/validar");
        return restClient.invokePost(webTarget, grado, Boolean.class);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgGrado buscarDefinicionTitulo(Long gradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/buscarDefinicionTitulo");
        webTarget = webTarget.path(gradoPk.toString());
        return restClient.invokeGet(webTarget, SgGrado.class);
    }

}
