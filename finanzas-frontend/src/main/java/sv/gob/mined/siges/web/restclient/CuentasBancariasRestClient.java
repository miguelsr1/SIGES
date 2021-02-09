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
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Traced
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class CuentasBancariasRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    @Inject
    private RestClient restClient;

    private Client client = null;

    public CuentasBancariasRestClient() {
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
    public List<SgCuentasBancarias> buscar(FiltroCuentasBancarias filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentas_bancarias/buscar");
        SgCuentasBancarias[] cuentas_bancarias = restClient.invokePost(webTarget, filtro, SgCuentasBancarias[].class);
        return Arrays.asList(cuentas_bancarias);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCuentasBancarias filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentas_bancarias/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCuentasBancarias guardar(SgCuentasBancarias cuentasBancarias) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancarias == null || userToken == null) {
            return null;
        }
        if (cuentasBancarias.getCbcPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentas_bancarias");
            return restClient.invokePost(webTarget, cuentasBancarias, SgCuentasBancarias.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentas_bancarias");
            webTarget = webTarget.path(cuentasBancarias.getCbcPk().toString());
            return restClient.invokePut(webTarget, cuentasBancarias, SgCuentasBancarias.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCuentasBancarias obtenerPorId(Long cuentasBancariasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentas_bancarias");
        webTarget = webTarget.path(cuentasBancariasPk.toString());
        return restClient.invokeGet(webTarget, SgCuentasBancarias.class);
    }

    public void eliminar(Long cuentasBancariasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentas_bancarias");
        webTarget = webTarget.path(cuentasBancariasPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long cuentasBancariasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentas_bancarias/historial");
        webTarget = webTarget.path(cuentasBancariasPk.toString());
        RevHistorico[] cuentas_bancarias = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(cuentas_bancarias);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgCuentasBancarias obtenerEnRevision(Long cuentasBancariasPk, Long cuentasBancariasRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasPk == null || cuentasBancariasRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentas_bancarias/revision");
        webTarget = webTarget.path(cuentasBancariasPk.toString());
        webTarget = webTarget.path(cuentasBancariasRev.toString());
        return restClient.invokeGet(webTarget, SgCuentasBancarias.class);
    }

}
