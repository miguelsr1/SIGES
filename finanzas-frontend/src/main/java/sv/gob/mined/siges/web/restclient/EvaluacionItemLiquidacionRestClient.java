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
import sv.gob.mined.siges.web.dto.SgEvaluacionItemLiquidacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEvaluacionItemLiquidacion;

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
public class EvaluacionItemLiquidacionRestClient implements Serializable {

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
    
    public EvaluacionItemLiquidacionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEvaluacionItemLiquidacion> buscar(FiltroEvaluacionItemLiquidacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacion/buscar");
        SgEvaluacionItemLiquidacion[] evaluacionItemsLiquidacion = restClient.invokePost(webTarget, filtro, SgEvaluacionItemLiquidacion[].class);
        return Arrays.asList(evaluacionItemsLiquidacion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroEvaluacionItemLiquidacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacion/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgEvaluacionItemLiquidacion guardar(SgEvaluacionItemLiquidacion evaluacionItemLiquidacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionItemLiquidacion == null || userToken == null) {
            return null;
        }
        if (evaluacionItemLiquidacion.getEilPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacion");
            return restClient.invokePost(webTarget, evaluacionItemLiquidacion, SgEvaluacionItemLiquidacion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacion");
            webTarget = webTarget.path(evaluacionItemLiquidacion.getEilPk().toString());
            return restClient.invokePut(webTarget, evaluacionItemLiquidacion, SgEvaluacionItemLiquidacion.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEvaluacionItemLiquidacion obtenerPorId(Long evaluacionItemLiquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionItemLiquidacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacion");
        webTarget = webTarget.path(evaluacionItemLiquidacionPk.toString());
        return restClient.invokeGet(webTarget, SgEvaluacionItemLiquidacion.class);
    }

    public void eliminar(Long evaluacionItemLiquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionItemLiquidacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacion");
        webTarget = webTarget.path(evaluacionItemLiquidacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEvaluacionItemLiquidacion> obtenerHistorialPorId(Long evaluacionItemLiquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionItemLiquidacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacion/historial");
        webTarget = webTarget.path(evaluacionItemLiquidacionPk.toString());
        SgEvaluacionItemLiquidacion[] evaluacionItemsLiquidacion = restClient.invokeGet(webTarget, SgEvaluacionItemLiquidacion[].class);
        return Arrays.asList(evaluacionItemsLiquidacion);
    }
    

}
