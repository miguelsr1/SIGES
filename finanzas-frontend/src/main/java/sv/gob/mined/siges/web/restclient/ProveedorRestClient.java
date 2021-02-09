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
import sv.gob.mined.siges.web.dto.siap2.SsProveedor;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProveedor;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class ProveedorRestClient implements Serializable {

    @Inject
    private RestClient restClient;

    private Client client = null;

    @Inject
    @Named("userToken")
    private String userToken;

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

    public ProveedorRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SsProveedor> buscar(FiltroProveedor filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/proveedores/buscar");
        SsProveedor[] proveedor = restClient.invokePost(webTarget, filtro, SsProveedor[].class);
        return new ArrayList<>(Arrays.asList(proveedor));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SsProveedor obtenerPorIdLazy(Long proveedorId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proveedorId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/proveedores/lazy");
        webTarget = webTarget.path(proveedorId.toString());
        return restClient.invokeGet(webTarget, SsProveedor.class);
    }

    public SsProveedor guardar(SsProveedor proveedor) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proveedor == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(proveedor);
        if (proveedor.getProId() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/proveedores");
            return restClient.invokePost(webTarget, proveedor, SsProveedor.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/proveedores");
            webTarget = webTarget.path(proveedor.getProId().toString());
            return restClient.invokePut(webTarget, proveedor, SsProveedor.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SsProveedor obtenerPorId(Long facturaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (facturaId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/proveedores");
        webTarget = webTarget.path(facturaId.toString());
        return restClient.invokeGet(webTarget, SsProveedor.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroProveedor filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/proveedores/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public void eliminar(Long pagoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pagoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/proveedores");
        webTarget = webTarget.path(pagoPk.toString());
        restClient.invokeDelete(webTarget);
    }

}
