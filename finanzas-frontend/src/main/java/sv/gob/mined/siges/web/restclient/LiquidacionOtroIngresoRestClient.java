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
import sv.gob.mined.siges.web.dto.SgLiquidacionOtroIngreso;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacionOtroIngreso;

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
public class LiquidacionOtroIngresoRestClient implements Serializable {

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
    
    public LiquidacionOtroIngresoRestClient() {
    }


    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgLiquidacionOtroIngreso> buscar(FiltroLiquidacionOtroIngreso filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacionsotrosingresos/buscar");
        SgLiquidacionOtroIngreso[] liquidacionsOtrosIngresos = restClient.invokePost(webTarget, filtro, SgLiquidacionOtroIngreso[].class);
        return Arrays.asList(liquidacionsOtrosIngresos);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroLiquidacionOtroIngreso filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacionsotrosingresos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgLiquidacionOtroIngreso guardar(SgLiquidacionOtroIngreso liquidacionOtroIngreso) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liquidacionOtroIngreso == null || userToken == null) {
            return null;
        }
        if (liquidacionOtroIngreso.getLoiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacionsotrosingresos");
            return restClient.invokePost(webTarget, liquidacionOtroIngreso, SgLiquidacionOtroIngreso.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacionsotrosingresos");
            webTarget = webTarget.path(liquidacionOtroIngreso.getLoiPk().toString());
            return restClient.invokePut(webTarget, liquidacionOtroIngreso, SgLiquidacionOtroIngreso.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgLiquidacionOtroIngreso obtenerPorId(Long liquidacionOtroIngresoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liquidacionOtroIngresoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacionsotrosingresos");
        webTarget = webTarget.path(liquidacionOtroIngresoPk.toString());
        return restClient.invokeGet(webTarget, SgLiquidacionOtroIngreso.class);
    }

    public void eliminar(Long liquidacionOtroIngresoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liquidacionOtroIngresoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacionsotrosingresos");
        webTarget = webTarget.path(liquidacionOtroIngresoPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgLiquidacionOtroIngreso> obtenerHistorialPorId(Long liquidacionOtroIngresoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liquidacionOtroIngresoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacionsotrosingresos/historial");
        webTarget = webTarget.path(liquidacionOtroIngresoPk.toString());
        SgLiquidacionOtroIngreso[] liquidacionsOtrosIngresos = restClient.invokeGet(webTarget, SgLiquidacionOtroIngreso[].class);
        return Arrays.asList(liquidacionsOtrosIngresos);
    }
    
    
    public SgLiquidacionOtroIngreso guardarConMovimientos(SgLiquidacionOtroIngreso liquidacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liquidacion == null || userToken == null) {
            return null;
        }
        if (liquidacion.getLoiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/liquidacionsotrosingresos/guardar");
            return restClient.invokePost(webTarget, liquidacion, SgLiquidacionOtroIngreso.class);
        }
        else{
            return null;
        }
    }
    
}