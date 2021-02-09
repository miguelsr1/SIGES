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
import sv.gob.mined.siges.web.dto.SgFactura;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFactura;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 30000L)
public class FacturaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

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

    public FacturaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgFactura> buscar(FiltroFactura filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas/buscar");
        SgFactura[] fac = restClient.invokePost(webTarget, filtro, SgFactura[].class);
        return new ArrayList<>(Arrays.asList(fac));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroFactura filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgFactura obtenerPorId(Long facturaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (facturaId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas");
        webTarget = webTarget.path(facturaId.toString());
        return restClient.invokeGet(webTarget, SgFactura.class);
    }

    public SgFactura guardar(SgFactura factura) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (factura == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(factura);

        if (factura.getFacPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas");
            return restClient.invokePost(webTarget, factura, SgFactura.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas");
            webTarget = webTarget.path(factura.getFacPk().toString());
            return restClient.invokePut(webTarget, factura, SgFactura.class);
        }
    }

    public void eliminar(Long facturaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (facturaId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas");
        webTarget = webTarget.path(facturaId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long facturaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (facturaId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas/historial");
        webTarget = webTarget.path(facturaId.toString());
        RevHistorico[] rev = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(rev);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgFactura obtenerEnRevision(Long facturaPk, Long revFacturaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (facturaPk == null || revFacturaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas/revision");
        webTarget = webTarget.path(facturaPk.toString());
        webTarget = webTarget.path(revFacturaPk.toString());
        return restClient.invokeGet(webTarget, SgFactura.class);
    }

    public SgFactura anular(SgFactura factura) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (factura == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(factura);
        if (factura.getFacPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas/anular");
            return restClient.invokePost(webTarget, factura, SgFactura.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas/anular");
            webTarget = webTarget.path(factura.getFacPk().toString());
            return restClient.invokePut(webTarget, factura, SgFactura.class);
        }
    }

    public SgFactura pagar(SgFactura factura) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (factura == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(factura);
        if (factura.getFacPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas/pagar");
            return restClient.invokePost(webTarget, factura, SgFactura.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas/pagar");
            webTarget = webTarget.path(factura.getFacPk().toString());
            return restClient.invokePut(webTarget, factura, SgFactura.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgFactura obtenerPorIdLazy(Long facturaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (facturaId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/facturas/lazy");
        webTarget = webTarget.path(facturaId.toString());
        return restClient.invokeGet(webTarget, SgFactura.class);
    }

}
