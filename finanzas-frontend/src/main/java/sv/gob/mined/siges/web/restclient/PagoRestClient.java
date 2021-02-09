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
import sv.gob.mined.siges.web.dto.SgPago;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPago;

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
@Timeout(value = 3000L)
public class PagoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    @Inject
    private RestClient restClient;

    private Client client = null;

    public PagoRestClient() {
    }

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

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgPago> buscar(FiltroPago filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/pagos/buscar");
        SgPago[] pagos = restClient.invokePost(webTarget, filtro, SgPago[].class);
        return Arrays.asList(pagos);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroPago filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/pagos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgPago guardar(SgPago pago) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pago == null || userToken == null) {
            return null;
        }
        if (pago.getPgsPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/pagos");
            return restClient.invokePost(webTarget, pago, SgPago.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/pagos");
            webTarget = webTarget.path(pago.getPgsPk().toString());
            return restClient.invokePut(webTarget, pago, SgPago.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgPago obtenerPorId(Long pagoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pagoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/pagos");
        webTarget = webTarget.path(pagoPk.toString());
        return restClient.invokeGet(webTarget, SgPago.class);
    }

    public void eliminar(Long pagoPk, String razon) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pagoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/pagos");
        webTarget = webTarget.path(pagoPk.toString());
        webTarget = webTarget.path(razon);
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long pagosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pagosPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/pagos/historial");
        webTarget = webTarget.path(pagosPk.toString());
        RevHistorico[] pagos = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(pagos);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgPago obtenerEnRevision(Long pagos, Long pagosRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pagos == null || pagosRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/pagos/revision");
        webTarget = webTarget.path(pagos.toString());
        webTarget = webTarget.path(pagosRev.toString());
        return restClient.invokeGet(webTarget, SgPago.class);
    }

    public SgPago guardarRazonAnulacion(SgPago pago) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pago == null || userToken == null) {
            return null;
        }
        if (pago.getPgsPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/pagos/guardarRazonAnulacion");
            return restClient.invokePost(webTarget, pago, SgPago.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/pagos/guardarRazonAnulacion");
            webTarget = webTarget.path(pago.getPgsPk().toString());
            return restClient.invokePut(webTarget, pago, SgPago.class);
        }
    }

}
