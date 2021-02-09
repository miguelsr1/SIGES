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
import sv.gob.mined.siges.web.dto.SgTipoMejora;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.filtros.FiltroTipoMejora;

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
public class TipoMejoraRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoMejoraRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoMejora> buscar(FiltroTipoMejora filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomejora/buscar");
        SgTipoMejora[] tipoMejora = RestClient.invokePost(webTarget, filtro, SgTipoMejora[].class, userToken);
        return Arrays.asList(tipoMejora);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroTipoMejora filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomejora/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoMejora guardar(SgTipoMejora tipoMejora) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoMejora == null || userToken == null) {
            return null;
        }
        if (tipoMejora.getTmePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomejora");
            return RestClient.invokePost(webTarget, tipoMejora, SgTipoMejora.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomejora");
            webTarget = webTarget.path(tipoMejora.getTmePk().toString());
            return RestClient.invokePut(webTarget, tipoMejora, SgTipoMejora.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTipoMejora obtenerPorId(Long tipoMejoraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoMejoraPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomejora");
        webTarget = webTarget.path(tipoMejoraPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoMejora.class, userToken);
    }

    public void eliminar(Long tipoMejoraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoMejoraPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomejora");
        webTarget = webTarget.path(tipoMejoraPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoMejora> obtenerHistorialPorId(Long tipoMejoraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoMejoraPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomejora/historial");
        webTarget = webTarget.path(tipoMejoraPk.toString());
        SgTipoMejora[] tipoMejora = RestClient.invokeGet(webTarget, SgTipoMejora[].class, userToken);
        return Arrays.asList(tipoMejora);
    }
    

}
