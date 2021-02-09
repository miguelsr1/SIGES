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
import sv.gob.mined.siges.web.dto.SgAfBienDepreciable;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 20000L)
public class BienesDepreciablesRestClient implements Serializable {

   @Inject
    private RestClient restClient;

    private Client client = null;

    @PostConstruct
    public void init() {
        client = RestClient.getClient();
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            client.close();
        }
    }

    public BienesDepreciablesRestClient() {
    }

    @Timeout(value = 10000L)
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAfBienDepreciable> buscar(FiltroBienesDepreciables filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/bienesdepreciables/buscar");
        SgAfBienDepreciable[] bienes = restClient.invokePost(webTarget, filtro, SgAfBienDepreciable[].class);
        return Arrays.asList(bienes);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroBienesDepreciables filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/bienesdepreciables/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Integer buscarUltimoCorrelativo(FiltroBienesDepreciables filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/bienesdepreciables/buscar/ultimocorrelativo");
        return restClient.invokePost(webTarget, filtro, Integer.class);
    }
    
    @Timeout(value = 50000L)
    public SgAfBienDepreciable guardar(SgAfBienDepreciable bien) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bien == null) {
            return null;
        }
        if (bien.getBdePk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/bienesdepreciables");
            return restClient.invokePost(webTarget, bien, SgAfBienDepreciable.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/bienesdepreciables");
            webTarget = webTarget.path(bien.getBdePk().toString());
            return restClient.invokePut(webTarget, bien, SgAfBienDepreciable.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAfBienDepreciable obtenerPorId(Long bienPk, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bienPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/bienesdepreciables");
        webTarget = webTarget.path(bienPk.toString());
        webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
        return restClient.invokeGet(webTarget, SgAfBienDepreciable.class);
    }

    public void eliminar(Long bienPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bienPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/bienesdepreciables");
        webTarget = webTarget.path(bienPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public void eliminarLista(List<SgAfBienDepreciable> lista) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (lista == null || lista.isEmpty()) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/bienesdepreciables/eliminar/lista");
        restClient.invokePost(webTarget, lista, null);
    }
    
    public List<RevHistorico> obtenerHistorialPorId(Long bienPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bienPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/bienesdepreciables/historial");
        webTarget = webTarget.path(bienPk.toString());
        RevHistorico[] bienes = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(bienes);
    }

    public SgAfBienDepreciable obtenerEnRevision(Long bienPk, Long bienRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bienPk == null || bienRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/bienesdepreciables/revision");
        webTarget = webTarget.path(bienPk.toString());
        webTarget = webTarget.path(bienRev.toString());
        return restClient.invokeGet(webTarget, SgAfBienDepreciable.class);
    }

}
