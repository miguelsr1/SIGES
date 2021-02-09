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
import sv.gob.mined.siges.web.dto.SgEvaluacionLiquidacionOtro;
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
public class EvaluacionLiquidacionOtroRestClient implements Serializable {

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
    
    public EvaluacionLiquidacionOtroRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEvaluacionLiquidacionOtro> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionliquidacionotro/buscar");
        SgEvaluacionLiquidacionOtro[] evaluacionLiquidacionOtro = restClient.invokePost(webTarget, filtro, SgEvaluacionLiquidacionOtro[].class);
        return Arrays.asList(evaluacionLiquidacionOtro);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionliquidacionotro/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgEvaluacionLiquidacionOtro guardar(SgEvaluacionLiquidacionOtro evaluacionLiquidacionOtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionLiquidacionOtro == null || userToken == null) {
            return null;
        }
        if (evaluacionLiquidacionOtro.getEliPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionliquidacionotro");
            return restClient.invokePost(webTarget, evaluacionLiquidacionOtro, SgEvaluacionLiquidacionOtro.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionliquidacionotro");
            webTarget = webTarget.path(evaluacionLiquidacionOtro.getEliPk().toString());
            return restClient.invokePut(webTarget, evaluacionLiquidacionOtro, SgEvaluacionLiquidacionOtro.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEvaluacionLiquidacionOtro obtenerPorId(Long evaluacionLiquidacionOtroPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionLiquidacionOtroPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionliquidacionotro");
        webTarget = webTarget.path(evaluacionLiquidacionOtroPk.toString());
        return restClient.invokeGet(webTarget, SgEvaluacionLiquidacionOtro.class);
    }

    public void eliminar(Long evaluacionLiquidacionOtroPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionLiquidacionOtroPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionliquidacionotro");
        webTarget = webTarget.path(evaluacionLiquidacionOtroPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEvaluacionLiquidacionOtro> obtenerHistorialPorId(Long evaluacionLiquidacionOtroPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (evaluacionLiquidacionOtroPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/evaluacionliquidacionotro/historial");
        webTarget = webTarget.path(evaluacionLiquidacionOtroPk.toString());
        SgEvaluacionLiquidacionOtro[] evaluacionLiquidacionOtro = restClient.invokeGet(webTarget, SgEvaluacionLiquidacionOtro[].class);
        return Arrays.asList(evaluacionLiquidacionOtro);
    }
    

}
