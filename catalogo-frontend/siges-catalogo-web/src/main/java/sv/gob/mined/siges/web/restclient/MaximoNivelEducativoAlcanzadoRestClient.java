/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgMaximoNivelEducativoAlcanzado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class MaximoNivelEducativoAlcanzadoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MaximoNivelEducativoAlcanzadoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMaximoNivelEducativoAlcanzado> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/maximoniveleducativoalcanzado/buscar");
        SgMaximoNivelEducativoAlcanzado[] maximoNivelEducativoAlcanzado = RestClient.invokePost(webTarget, filtro, SgMaximoNivelEducativoAlcanzado[].class, userToken);
        return Arrays.asList(maximoNivelEducativoAlcanzado);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/maximoniveleducativoalcanzado/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMaximoNivelEducativoAlcanzado guardar(SgMaximoNivelEducativoAlcanzado maximoNivelEducativoAlcanzado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (maximoNivelEducativoAlcanzado == null || userToken == null) {
            return null;
        }
        if (maximoNivelEducativoAlcanzado.getMnePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/maximoniveleducativoalcanzado");
            return RestClient.invokePost(webTarget, maximoNivelEducativoAlcanzado, SgMaximoNivelEducativoAlcanzado.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/maximoniveleducativoalcanzado");
            webTarget = webTarget.path(maximoNivelEducativoAlcanzado.getMnePk().toString());
            return RestClient.invokePut(webTarget, maximoNivelEducativoAlcanzado, SgMaximoNivelEducativoAlcanzado.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMaximoNivelEducativoAlcanzado obtenerPorId(Long maximoNivelEducativoAlcanzadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (maximoNivelEducativoAlcanzadoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/maximoniveleducativoalcanzado");
        webTarget = webTarget.path(maximoNivelEducativoAlcanzadoPk.toString());
        return RestClient.invokeGet(webTarget, SgMaximoNivelEducativoAlcanzado.class, userToken);
    }

    public void eliminar(Long maximoNivelEducativoAlcanzadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (maximoNivelEducativoAlcanzadoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/maximoniveleducativoalcanzado");
        webTarget = webTarget.path(maximoNivelEducativoAlcanzadoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMaximoNivelEducativoAlcanzado> obtenerHistorialPorId(Long maximoNivelEducativoAlcanzadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (maximoNivelEducativoAlcanzadoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/maximoniveleducativoalcanzado/historial");
        webTarget = webTarget.path(maximoNivelEducativoAlcanzadoPk.toString());
        SgMaximoNivelEducativoAlcanzado[] maximoNivelEducativoAlcanzado = RestClient.invokeGet(webTarget, SgMaximoNivelEducativoAlcanzado[].class, userToken);
        return Arrays.asList(maximoNivelEducativoAlcanzado);
    }
    

}
