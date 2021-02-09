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
import sv.gob.mined.siges.web.dto.SgLiquidacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacion;

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
public class LiquidacionRestClient implements Serializable {
    
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
    
    public LiquidacionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgLiquidacion> buscar(FiltroLiquidacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacion/buscar");
        SgLiquidacion[] liquidacion = restClient.invokePost(webTarget, filtro, SgLiquidacion[].class);
        return Arrays.asList(liquidacion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroLiquidacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacion/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgLiquidacion guardar(SgLiquidacion liquidacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liquidacion == null || userToken == null) {
            return null;
        }
        if (liquidacion.getLiqPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacion");
            return restClient.invokePost(webTarget, liquidacion, SgLiquidacion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacion");
            webTarget = webTarget.path(liquidacion.getLiqPk().toString());
            return restClient.invokePut(webTarget, liquidacion, SgLiquidacion.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgLiquidacion obtenerPorId(Long liquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liquidacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacion");
        webTarget = webTarget.path(liquidacionPk.toString());
        return restClient.invokeGet(webTarget, SgLiquidacion.class);
    }

    public void eliminar(Long liquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liquidacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacion");
        webTarget = webTarget.path(liquidacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgLiquidacion> obtenerHistorialPorId(Long liquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liquidacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacion/historial");
        webTarget = webTarget.path(liquidacionPk.toString());
        SgLiquidacion[] liquidacion = restClient.invokeGet(webTarget, SgLiquidacion[].class);
        return Arrays.asList(liquidacion);
    }
    
    public SgLiquidacion guardarConMovimientos(SgLiquidacion liquidacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liquidacion == null || userToken == null) {
            return null;
        }
        if (liquidacion.getLiqPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacion/guardar");
            return restClient.invokePost(webTarget, liquidacion, SgLiquidacion.class);
        }
        else{
            return null;
        }
    }

}
