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
import sv.gob.mined.siges.web.dto.SgMovimientoDireccionDepartamental;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoDireccionDepartamental;

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
public class MovimientoDireccionDepartamentalRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    @Inject
    private RestClient restClient;
    
    private Client client = null;
    
    public MovimientoDireccionDepartamentalRestClient() {
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
    public List<SgMovimientoDireccionDepartamental> buscar(FiltroMovimientoDireccionDepartamental filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientodirecciondepartamental/buscar");
        SgMovimientoDireccionDepartamental[] movimientoCuentaBancaria = restClient.invokePost(webTarget, filtro, SgMovimientoDireccionDepartamental[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroMovimientoDireccionDepartamental filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientodirecciondepartamental/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgMovimientoDireccionDepartamental guardar(SgMovimientoDireccionDepartamental movimientoCuentaBancaria) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoCuentaBancaria == null || userToken == null) {
            return null;
        }
        if (movimientoCuentaBancaria.getMddPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientodirecciondepartamental");
            return restClient.invokePost(webTarget, movimientoCuentaBancaria, SgMovimientoDireccionDepartamental.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientodirecciondepartamental");
            webTarget = webTarget.path(movimientoCuentaBancaria.getMddPk().toString());
            return restClient.invokePut(webTarget, movimientoCuentaBancaria, SgMovimientoDireccionDepartamental.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMovimientoDireccionDepartamental obtenerPorId(Long movimientoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoCuentaBancariaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientodirecciondepartamental");
        webTarget = webTarget.path(movimientoCuentaBancariaPk.toString());
        return restClient.invokeGet(webTarget, SgMovimientoDireccionDepartamental.class);
    }

    public void eliminar(Long movimientoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoCuentaBancariaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientodirecciondepartamental");
        webTarget = webTarget.path(movimientoCuentaBancariaPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMovimientoDireccionDepartamental> obtenerHistorialPorId(Long movimientoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoCuentaBancariaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientodirecciondepartamental/historial");
        webTarget = webTarget.path(movimientoCuentaBancariaPk.toString());
        SgMovimientoDireccionDepartamental[] movimientoCuentaBancaria = restClient.invokeGet(webTarget, SgMovimientoDireccionDepartamental[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }
    

}
