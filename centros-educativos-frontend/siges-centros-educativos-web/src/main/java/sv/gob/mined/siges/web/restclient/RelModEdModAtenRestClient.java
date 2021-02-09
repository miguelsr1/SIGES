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
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;

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
public class RelModEdModAtenRestClient implements Serializable {

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

    public RelModEdModAtenRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRelModEdModAten> buscar(FiltroRelModEdModAten filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten/buscar");
        SgRelModEdModAten[] relModEdModAten = restClient.invokePost(webTarget, filtro, SgRelModEdModAten[].class);
        return Arrays.asList(relModEdModAten);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.REL_MOD_ED_MOD_ATEN_CACHE)
    public List<SgRelModEdModAten> buscarConCache(FiltroRelModEdModAten filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten/buscar");
        SgRelModEdModAten[] relModEdModAten = restClient.invokePost(webTarget, filtro, SgRelModEdModAten[].class);
        return Arrays.asList(relModEdModAten);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroRelModEdModAten filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelModEdModAten guardar(SgRelModEdModAten relModEdModAten) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relModEdModAten == null) {
            return null;
        }
        if (relModEdModAten.getReaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten");
            return restClient.invokePost(webTarget, relModEdModAten, SgRelModEdModAten.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten");
            webTarget = webTarget.path(relModEdModAten.getReaPk().toString());
            return restClient.invokePut(webTarget, relModEdModAten, SgRelModEdModAten.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgRelModEdModAten obtenerPorId(Long relModEdModAtenPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relModEdModAtenPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten");
        webTarget = webTarget.path(relModEdModAtenPk.toString());
        return restClient.invokeGet(webTarget, SgRelModEdModAten.class);
    }

    public void eliminar(Long relModEdModAtenPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relModEdModAtenPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten");
        webTarget = webTarget.path(relModEdModAtenPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long relModEdModAtenPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relModEdModAtenPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten/historial");
        webTarget = webTarget.path(relModEdModAtenPk.toString());
        RevHistorico[] relModEdModAten = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relModEdModAten);
    }

    public SgRelModEdModAten obtenerEnRevision(Long relModEdModAtenPk, Long relModEdModAtenRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relModEdModAtenPk == null || relModEdModAtenRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten/revision");
        webTarget = webTarget.path(relModEdModAtenPk.toString());
        webTarget = webTarget.path(relModEdModAtenRev.toString());
        return restClient.invokeGet(webTarget, SgRelModEdModAten.class);
    }

}
