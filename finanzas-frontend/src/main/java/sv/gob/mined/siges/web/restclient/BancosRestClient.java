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
import sv.gob.mined.siges.web.dto.SgBancos;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;

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
public class BancosRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    @Inject
    private RestClient restClient;
    
    private Client client = null;
    
    public BancosRestClient() {
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
    public List<SgBancos> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos/buscar");
        SgBancos[] bancos = restClient.invokePost(webTarget, filtro, SgBancos[].class);
        return Arrays.asList(bancos);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgBancos guardar(SgBancos cuentasBancarias) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancarias == null || userToken == null) {
            return null;
        }
        if (cuentasBancarias.getBncPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos");
            return restClient.invokePost(webTarget, cuentasBancarias, SgBancos.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos");
            webTarget = webTarget.path(cuentasBancarias.getBncPk().toString());
            return restClient.invokePut(webTarget, cuentasBancarias, SgBancos.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgBancos obtenerPorId(Long cuentasBancariasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos");
        webTarget = webTarget.path(cuentasBancariasPk.toString());
        return restClient.invokeGet(webTarget, SgBancos.class);
    }

    public void eliminar(Long cuentasBancariasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos");
        webTarget = webTarget.path(cuentasBancariasPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgBancos> obtenerHistorialPorId(Long cuentasBancariasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cuentasBancariasPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos/historial");
        webTarget = webTarget.path(cuentasBancariasPk.toString());
        SgBancos[] bancos = restClient.invokeGet(webTarget, SgBancos[].class);
        return Arrays.asList(bancos);
    }
    

}
