/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
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
import sv.gob.mined.siges.web.dto.SgAsociacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsociacion;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 3000L)
public class AsociacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public AsociacionRestClient() {
    }


    public List<SgAsociacion> buscar(FiltroAsociacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/asociaciones/buscar");
        SgAsociacion[] asociacion = RestClient.invokePost(webTarget, filtro, SgAsociacion[].class, userToken);
        return Arrays.asList(asociacion);
    }

    public Long buscarTotal(FiltroAsociacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/asociaciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAsociacion guardar(SgAsociacion asociacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asociacion == null || userToken == null) {
            return null;
        }
        if (asociacion.getAsoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/asociaciones");
            return RestClient.invokePost(webTarget, asociacion, SgAsociacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/asociaciones");
            webTarget = webTarget.path(asociacion.getAsoPk().toString());
            return RestClient.invokePut(webTarget, asociacion, SgAsociacion.class, userToken);
        }
    }

    public SgAsociacion obtenerPorId(Long asociacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asociacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/asociaciones");
        webTarget = webTarget.path(asociacionPk.toString());
        return RestClient.invokeGet(webTarget, SgAsociacion.class, userToken);
    }

    public void eliminar(Long asociacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asociacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/asociaciones");
        webTarget = webTarget.path(asociacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAsociacion> obtenerHistorialPorId(Long asociacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asociacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/asociaciones/historial");
        webTarget = webTarget.path(asociacionPk.toString());
        SgAsociacion[] asociacion = RestClient.invokeGet(webTarget, SgAsociacion[].class, userToken);
        return Arrays.asList(asociacion);
    }
    

}
