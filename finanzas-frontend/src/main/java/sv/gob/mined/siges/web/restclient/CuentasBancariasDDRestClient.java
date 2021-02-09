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
import sv.gob.mined.siges.web.dto.SgCuentasBancariasDD;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancariasDD;

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
public class CuentasBancariasDDRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    @Inject
    private RestClient restClient;
    
    private Client client = null;
    
    public CuentasBancariasDDRestClient() {
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
    public List<SgCuentasBancariasDD> buscar(FiltroCuentasBancariasDD filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentasbancariasdd/buscar");
        SgCuentasBancariasDD[] cuentasbancariasdd = restClient.invokePost(webTarget, filtro, SgCuentasBancariasDD[].class);
        return Arrays.asList(cuentasbancariasdd);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCuentasBancariasDD filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentasbancariasdd/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCuentasBancariasDD guardar(SgCuentasBancariasDD cuentasBancariasDD) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasDD == null || userToken == null) {
            return null;
        }
        if (cuentasBancariasDD.getCbdPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentasbancariasdd");
            return restClient.invokePost(webTarget, cuentasBancariasDD, SgCuentasBancariasDD.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentasbancariasdd");
            webTarget = webTarget.path(cuentasBancariasDD.getCbdPk().toString());
            return restClient.invokePut(webTarget, cuentasBancariasDD, SgCuentasBancariasDD.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCuentasBancariasDD obtenerPorId(Long cuentasBancariasDDPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasDDPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentasbancariasdd");
        webTarget = webTarget.path(cuentasBancariasDDPk.toString());
        return restClient.invokeGet(webTarget, SgCuentasBancariasDD.class);
    }

    public void eliminar(Long cuentasBancariasDDPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasDDPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentasbancariasdd");
        webTarget = webTarget.path(cuentasBancariasDDPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long cuentasBancariasDDPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasDDPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentasbancariasdd/historial");
        webTarget = webTarget.path(cuentasBancariasDDPk.toString());
        RevHistorico[] cuentasbancariasdd = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(cuentasbancariasdd);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgCuentasBancariasDD obtenerEnRevision(Long cuentasBancariasPk, Long cuentasBancariasRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasPk == null || cuentasBancariasRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/cuentasbancariasdd/revision");
        webTarget = webTarget.path(cuentasBancariasPk.toString());
        webTarget = webTarget.path(cuentasBancariasRev.toString());
        return restClient.invokeGet(webTarget, SgCuentasBancariasDD.class);
    }
}
