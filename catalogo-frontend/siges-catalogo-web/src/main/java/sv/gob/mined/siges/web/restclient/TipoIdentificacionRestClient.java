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
import sv.gob.mined.siges.web.dto.SgTipoIdentificacion;
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
public class TipoIdentificacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoIdentificacionRestClient() {
    }


    public List<SgTipoIdentificacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposidentificacion/buscar");
        SgTipoIdentificacion[] tiposIdentificacion = RestClient.invokePost(webTarget, filtro, SgTipoIdentificacion[].class, userToken);
        return Arrays.asList(tiposIdentificacion);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposidentificacion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoIdentificacion guardar(SgTipoIdentificacion tipoIdentificacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoIdentificacion == null || userToken == null) {
            return null;
        }
        if (tipoIdentificacion.getTinPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposidentificacion");
            return RestClient.invokePost(webTarget, tipoIdentificacion, SgTipoIdentificacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposidentificacion");
            webTarget = webTarget.path(tipoIdentificacion.getTinPk().toString());
            return RestClient.invokePut(webTarget, tipoIdentificacion, SgTipoIdentificacion.class, userToken);
        }
    }

    public SgTipoIdentificacion obtenerPorId(Long tipoIdentificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoIdentificacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposidentificacion");
        webTarget = webTarget.path(tipoIdentificacionPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoIdentificacion.class, userToken);
    }

    public void eliminar(Long tipoIdentificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoIdentificacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposidentificacion");
        webTarget = webTarget.path(tipoIdentificacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoIdentificacion> obtenerHistorialPorId(Long tipoIdentificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoIdentificacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposidentificacion/historial");
        webTarget = webTarget.path(tipoIdentificacionPk.toString());
        SgTipoIdentificacion[] tiposIdentificacion = RestClient.invokeGet(webTarget, SgTipoIdentificacion[].class, userToken);
        return Arrays.asList(tiposIdentificacion);
    }
    

}
