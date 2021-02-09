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
import sv.gob.mined.siges.web.dto.SgCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacion;

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
public class CalificacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CalificacionRestClient() {
    }


    public List<SgCalificacion> buscar(FiltroCalificacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calificaciones/buscar");
        SgCalificacion[] calificaciones = RestClient.invokePost(webTarget, filtro, SgCalificacion[].class, userToken);
        return Arrays.asList(calificaciones);
    }

    public Long buscarTotal(FiltroCalificacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calificaciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCalificacion guardar(SgCalificacion calificacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacion == null || userToken == null) {
            return null;
        }
        if (calificacion.getCalPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calificaciones");
            return RestClient.invokePost(webTarget, calificacion, SgCalificacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calificaciones");
            webTarget = webTarget.path(calificacion.getCalPk().toString());
            return RestClient.invokePut(webTarget, calificacion, SgCalificacion.class, userToken);
        }
    }

    public SgCalificacion obtenerPorId(Long calificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calificaciones");
        webTarget = webTarget.path(calificacionPk.toString());
        return RestClient.invokeGet(webTarget, SgCalificacion.class, userToken);
    }

    public void eliminar(Long calificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calificaciones");
        webTarget = webTarget.path(calificacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgCalificacion> obtenerHistorialPorId(Long calificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calificaciones/historial");
        webTarget = webTarget.path(calificacionPk.toString());
        SgCalificacion[] calificaciones = RestClient.invokeGet(webTarget, SgCalificacion[].class, userToken);
        return Arrays.asList(calificaciones);
    }
    

}
