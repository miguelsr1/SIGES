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
import sv.gob.mined.siges.web.dto.SgTiposRiesgo;
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
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class TiposRiesgoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TiposRiesgoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTiposRiesgo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporiesgo/buscar");
        SgTiposRiesgo[] tipoRiesgo = RestClient.invokePost(webTarget, filtro, SgTiposRiesgo[].class, userToken);
        return Arrays.asList(tipoRiesgo);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporiesgo/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTiposRiesgo guardar(SgTiposRiesgo tiposRiesgo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tiposRiesgo == null || userToken == null) {
            return null;
        }
        if (tiposRiesgo.getTriPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporiesgo");
            return RestClient.invokePost(webTarget, tiposRiesgo, SgTiposRiesgo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporiesgo");
            webTarget = webTarget.path(tiposRiesgo.getTriPk().toString());
            return RestClient.invokePut(webTarget, tiposRiesgo, SgTiposRiesgo.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTiposRiesgo obtenerPorId(Long tiposRiesgoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tiposRiesgoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporiesgo");
        webTarget = webTarget.path(tiposRiesgoPk.toString());
        return RestClient.invokeGet(webTarget, SgTiposRiesgo.class, userToken);
    }

    public void eliminar(Long tiposRiesgoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tiposRiesgoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporiesgo");
        webTarget = webTarget.path(tiposRiesgoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTiposRiesgo> obtenerHistorialPorId(Long tiposRiesgoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tiposRiesgoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporiesgo/historial");
        webTarget = webTarget.path(tiposRiesgoPk.toString());
        SgTiposRiesgo[] tipoRiesgo = RestClient.invokeGet(webTarget, SgTiposRiesgo[].class, userToken);
        return Arrays.asList(tipoRiesgo);
    }
    

}
