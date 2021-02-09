/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgTipoAccion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 3000L)
public class TipoAccionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoAccionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoAccion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposaccion/buscar");
        SgTipoAccion[] tiposAccion = RestClient.invokePost(webTarget, filtro, SgTipoAccion[].class, userToken);
        return Arrays.asList(tiposAccion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposaccion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoAccion guardar(SgTipoAccion tipoAccion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAccion == null || userToken == null) {
            return null;
        }
        if (tipoAccion.getTacPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposaccion");
            return RestClient.invokePost(webTarget, tipoAccion, SgTipoAccion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposaccion");
            webTarget = webTarget.path(tipoAccion.getTacPk().toString());
            return RestClient.invokePut(webTarget, tipoAccion, SgTipoAccion.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTipoAccion obtenerPorId(Long tipoAccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAccionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposaccion");
        webTarget = webTarget.path(tipoAccionPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoAccion.class, userToken);
    }

    public void eliminar(Long tipoAccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAccionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposaccion");
        webTarget = webTarget.path(tipoAccionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoAccion> obtenerHistorialPorId(Long tipoAccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAccionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposaccion/historial");
        webTarget = webTarget.path(tipoAccionPk.toString());
        SgTipoAccion[] tiposAccion = RestClient.invokeGet(webTarget, SgTipoAccion[].class, userToken);
        return Arrays.asList(tiposAccion);
    }
    

}
