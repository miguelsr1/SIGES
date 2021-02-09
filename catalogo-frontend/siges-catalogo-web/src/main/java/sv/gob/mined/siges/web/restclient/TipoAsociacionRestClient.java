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
import sv.gob.mined.siges.web.dto.SgTipoAsociacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;

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
public class TipoAsociacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoAsociacionRestClient() {
    }


    public List<SgTipoAsociacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposasociaciones/buscar");
        SgTipoAsociacion[] tipoAsociacion = RestClient.invokePost(webTarget, filtro, SgTipoAsociacion[].class, userToken);
        return Arrays.asList(tipoAsociacion);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposasociaciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoAsociacion guardar(SgTipoAsociacion tipoAsociacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAsociacion == null || userToken == null) {
            return null;
        }
        if (tipoAsociacion.getTasPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposasociaciones");
            return RestClient.invokePost(webTarget, tipoAsociacion, SgTipoAsociacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposasociaciones");
            webTarget = webTarget.path(tipoAsociacion.getTasPk().toString());
            return RestClient.invokePut(webTarget, tipoAsociacion, SgTipoAsociacion.class, userToken);
        }
    }

    public SgTipoAsociacion obtenerPorId(Long tipoAsociacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAsociacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposasociaciones");
        webTarget = webTarget.path(tipoAsociacionPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoAsociacion.class, userToken);
    }

    public void eliminar(Long tipoAsociacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAsociacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposasociaciones");
        webTarget = webTarget.path(tipoAsociacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoAsociacion> obtenerHistorialPorId(Long tipoAsociacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAsociacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposasociaciones/historial");
        webTarget = webTarget.path(tipoAsociacionPk.toString());
        SgTipoAsociacion[] tipoAsociacion = RestClient.invokeGet(webTarget, SgTipoAsociacion[].class, userToken);
        return Arrays.asList(tipoAsociacion);
    }
    

}
