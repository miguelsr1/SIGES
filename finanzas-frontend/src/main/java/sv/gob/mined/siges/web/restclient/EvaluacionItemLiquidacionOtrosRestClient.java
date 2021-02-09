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
import sv.gob.mined.siges.web.dto.SgEvaluacionItemLiquidacionOtros;
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
public class EvaluacionItemLiquidacionOtrosRestClient implements Serializable {

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
    
    public EvaluacionItemLiquidacionOtrosRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEvaluacionItemLiquidacionOtros> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacionotro/buscar");
        SgEvaluacionItemLiquidacionOtros[] evaluacionItemsLiquidacionOtro = restClient.invokePost(webTarget, filtro, SgEvaluacionItemLiquidacionOtros[].class);
        return Arrays.asList(evaluacionItemsLiquidacionOtro);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacionotro/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgEvaluacionItemLiquidacionOtros guardar(SgEvaluacionItemLiquidacionOtros evaluacionItemLiquidacionOtros) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionItemLiquidacionOtros == null || userToken == null) {
            return null;
        }
        if (evaluacionItemLiquidacionOtros.getEloPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacionotro");
            return restClient.invokePost(webTarget, evaluacionItemLiquidacionOtros, SgEvaluacionItemLiquidacionOtros.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacionotro");
            webTarget = webTarget.path(evaluacionItemLiquidacionOtros.getEloPk().toString());
            return restClient.invokePut(webTarget, evaluacionItemLiquidacionOtros, SgEvaluacionItemLiquidacionOtros.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEvaluacionItemLiquidacionOtros obtenerPorId(Long evaluacionItemLiquidacionOtrosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionItemLiquidacionOtrosPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacionotro");
        webTarget = webTarget.path(evaluacionItemLiquidacionOtrosPk.toString());
        return restClient.invokeGet(webTarget, SgEvaluacionItemLiquidacionOtros.class);
    }

    public void eliminar(Long evaluacionItemLiquidacionOtrosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionItemLiquidacionOtrosPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacionotro");
        webTarget = webTarget.path(evaluacionItemLiquidacionOtrosPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEvaluacionItemLiquidacionOtros> obtenerHistorialPorId(Long evaluacionItemLiquidacionOtrosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionItemLiquidacionOtrosPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionitemsliquidacionotro/historial");
        webTarget = webTarget.path(evaluacionItemLiquidacionOtrosPk.toString());
        SgEvaluacionItemLiquidacionOtros[] evaluacionItemsLiquidacionOtro = restClient.invokeGet(webTarget, SgEvaluacionItemLiquidacionOtros[].class);
        return Arrays.asList(evaluacionItemsLiquidacionOtro);
    }
    

}
