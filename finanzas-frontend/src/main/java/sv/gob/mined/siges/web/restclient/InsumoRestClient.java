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
import sv.gob.mined.siges.web.dto.siap2.SgInsumo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroInsumo;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class InsumoRestClient implements Serializable {

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

    public InsumoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgInsumo> buscar(FiltroInsumo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/insumos/buscar");
        SgInsumo[] sede = restClient.invokePost(webTarget, filtro, SgInsumo[].class);
        return new ArrayList<>(Arrays.asList(sede));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroInsumo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/insumos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgInsumo guardar(SgInsumo insumo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (insumo == null) {
            return null;
        }
        if (insumo.getInsPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/insumos");
            return restClient.invokePost(webTarget, insumo, SgInsumo.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/insumos");
            webTarget = webTarget.path(insumo.getInsPk().toString());
            return restClient.invokePut(webTarget, insumo, SgInsumo.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgInsumo obtenerPorId(Long insumoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (insumoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/insumos");
        webTarget = webTarget.path(insumoPk.toString());
        return restClient.invokeGet(webTarget, SgInsumo.class);
    }

    public void eliminar(Long insumoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (insumoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/insumos");
        webTarget = webTarget.path(insumoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long insumoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (insumoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/insumos/historial");
        webTarget = webTarget.path(insumoPk.toString());
        RevHistorico[] insumo = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(insumo);
    }

    public SgInsumo obtenerEnRevision(Long insumoPk, Long insumoRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (insumoPk == null || insumoRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/insumos/revision");
        webTarget = webTarget.path(insumoPk.toString());
        webTarget = webTarget.path(insumoRev.toString());
        return restClient.invokeGet(webTarget, SgInsumo.class);
    }

}
