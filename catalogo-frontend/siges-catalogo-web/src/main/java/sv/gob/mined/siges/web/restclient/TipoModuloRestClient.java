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
import sv.gob.mined.siges.web.dto.SgTipoModulo;
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
public class TipoModuloRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoModuloRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoModulo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmodulo/buscar");
        SgTipoModulo[] tiposModulo = RestClient.invokePost(webTarget, filtro, SgTipoModulo[].class, userToken);
        return Arrays.asList(tiposModulo);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmodulo/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoModulo guardar(SgTipoModulo tipoModulo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoModulo == null || userToken == null) {
            return null;
        }
        if (tipoModulo.getTmoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmodulo");
            return RestClient.invokePost(webTarget, tipoModulo, SgTipoModulo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmodulo");
            webTarget = webTarget.path(tipoModulo.getTmoPk().toString());
            return RestClient.invokePut(webTarget, tipoModulo, SgTipoModulo.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTipoModulo obtenerPorId(Long tipoModuloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoModuloPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmodulo");
        webTarget = webTarget.path(tipoModuloPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoModulo.class, userToken);
    }

    public void eliminar(Long tipoModuloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoModuloPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmodulo");
        webTarget = webTarget.path(tipoModuloPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoModulo> obtenerHistorialPorId(Long tipoModuloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoModuloPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmodulo/historial");
        webTarget = webTarget.path(tipoModuloPk.toString());
        SgTipoModulo[] tiposModulo = RestClient.invokeGet(webTarget, SgTipoModulo[].class, userToken);
        return Arrays.asList(tiposModulo);
    }
    

}
