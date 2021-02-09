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
import sv.gob.mined.siges.web.dto.SgMotivoReimpresion;
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
public class MotivoReimpresionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MotivoReimpresionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMotivoReimpresion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoreimpresion/buscar");
        SgMotivoReimpresion[] motivoReimpresion = RestClient.invokePost(webTarget, filtro, SgMotivoReimpresion[].class, userToken);
        return Arrays.asList(motivoReimpresion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoreimpresion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMotivoReimpresion guardar(SgMotivoReimpresion motivoReimpresion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoReimpresion == null || userToken == null) {
            return null;
        }
        if (motivoReimpresion.getMriPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoreimpresion");
            return RestClient.invokePost(webTarget, motivoReimpresion, SgMotivoReimpresion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoreimpresion");
            webTarget = webTarget.path(motivoReimpresion.getMriPk().toString());
            return RestClient.invokePut(webTarget, motivoReimpresion, SgMotivoReimpresion.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMotivoReimpresion obtenerPorId(Long motivoReimpresionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoReimpresionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoreimpresion");
        webTarget = webTarget.path(motivoReimpresionPk.toString());
        return RestClient.invokeGet(webTarget, SgMotivoReimpresion.class, userToken);
    }

    public void eliminar(Long motivoReimpresionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoReimpresionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoreimpresion");
        webTarget = webTarget.path(motivoReimpresionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMotivoReimpresion> obtenerHistorialPorId(Long motivoReimpresionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoReimpresionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoreimpresion/historial");
        webTarget = webTarget.path(motivoReimpresionPk.toString());
        SgMotivoReimpresion[] motivoReimpresion = RestClient.invokeGet(webTarget, SgMotivoReimpresion[].class, userToken);
        return Arrays.asList(motivoReimpresion);
    }
    

}
