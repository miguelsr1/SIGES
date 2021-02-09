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
import sv.gob.mined.siges.web.dto.SgEstadoProcesoLegalizacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstadoProcesoLegalizacion;

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
public class EstadoProcesoLegalizacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EstadoProcesoLegalizacionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstadoProcesoLegalizacion> buscar(FiltroEstadoProcesoLegalizacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoprocesolegalizacion/buscar");
        SgEstadoProcesoLegalizacion[] estadoProcesoLegalizacion = RestClient.invokePost(webTarget, filtro, SgEstadoProcesoLegalizacion[].class, userToken);
        return Arrays.asList(estadoProcesoLegalizacion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroEstadoProcesoLegalizacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoprocesolegalizacion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEstadoProcesoLegalizacion guardar(SgEstadoProcesoLegalizacion estadoProcesoLegalizacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoProcesoLegalizacion == null || userToken == null) {
            return null;
        }
        if (estadoProcesoLegalizacion.getEplPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoprocesolegalizacion");
            return RestClient.invokePost(webTarget, estadoProcesoLegalizacion, SgEstadoProcesoLegalizacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoprocesolegalizacion");
            webTarget = webTarget.path(estadoProcesoLegalizacion.getEplPk().toString());
            return RestClient.invokePut(webTarget, estadoProcesoLegalizacion, SgEstadoProcesoLegalizacion.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEstadoProcesoLegalizacion obtenerPorId(Long estadoProcesoLegalizacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoProcesoLegalizacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoprocesolegalizacion");
        webTarget = webTarget.path(estadoProcesoLegalizacionPk.toString());
        return RestClient.invokeGet(webTarget, SgEstadoProcesoLegalizacion.class, userToken);
    }

    public void eliminar(Long estadoProcesoLegalizacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoProcesoLegalizacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoprocesolegalizacion");
        webTarget = webTarget.path(estadoProcesoLegalizacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstadoProcesoLegalizacion> obtenerHistorialPorId(Long estadoProcesoLegalizacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoProcesoLegalizacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoprocesolegalizacion/historial");
        webTarget = webTarget.path(estadoProcesoLegalizacionPk.toString());
        SgEstadoProcesoLegalizacion[] estadoProcesoLegalizacion = RestClient.invokeGet(webTarget, SgEstadoProcesoLegalizacion[].class, userToken);
        return Arrays.asList(estadoProcesoLegalizacion);
    }
    

}
