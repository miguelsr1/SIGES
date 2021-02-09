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
import sv.gob.mined.siges.web.dto.SgEvaluacionLiquidacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;

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
public class EvaluacionLiquidacionRestClient implements Serializable {

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
    
    public EvaluacionLiquidacionRestClient() {
    }


    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEvaluacionLiquidacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionesliquidaciones/buscar");
        SgEvaluacionLiquidacion[] evaluacionesLiquidaciones = restClient.invokePost(webTarget, filtro, SgEvaluacionLiquidacion[].class);
        return Arrays.asList(evaluacionesLiquidaciones);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionesliquidaciones/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgEvaluacionLiquidacion guardar(SgEvaluacionLiquidacion evaluacionLiquidacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionLiquidacion == null || userToken == null) {
            return null;
        }
        if (evaluacionLiquidacion.getElqPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionesliquidaciones");
            return restClient.invokePost(webTarget, evaluacionLiquidacion, SgEvaluacionLiquidacion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionesliquidaciones");
            webTarget = webTarget.path(evaluacionLiquidacion.getElqPk().toString());
            return restClient.invokePut(webTarget, evaluacionLiquidacion, SgEvaluacionLiquidacion.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEvaluacionLiquidacion obtenerPorId(Long evaluacionLiquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionLiquidacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionesliquidaciones");
        webTarget = webTarget.path(evaluacionLiquidacionPk.toString());
        return restClient.invokeGet(webTarget, SgEvaluacionLiquidacion.class);
    }

    public void eliminar(Long evaluacionLiquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionLiquidacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionesliquidaciones");
        webTarget = webTarget.path(evaluacionLiquidacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEvaluacionLiquidacion> obtenerHistorialPorId(Long evaluacionLiquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionLiquidacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionesliquidaciones/historial");
        webTarget = webTarget.path(evaluacionLiquidacionPk.toString());
        SgEvaluacionLiquidacion[] evaluacionesLiquidaciones = restClient.invokeGet(webTarget, SgEvaluacionLiquidacion[].class);
        return Arrays.asList(evaluacionesLiquidaciones);
    }
    

}
