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
import sv.gob.mined.siges.web.dto.SgAccionSolicitudTraslado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAccion;

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
public class AccionSolicitudTrasladoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public AccionSolicitudTrasladoRestClient() {
    }


    public List<SgAccionSolicitudTraslado> buscar(FiltroAccion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/acciones/buscar");
        SgAccionSolicitudTraslado[] acciones = RestClient.invokePost(webTarget, filtro, SgAccionSolicitudTraslado[].class, userToken);
        return Arrays.asList(acciones);
    }

    public Long buscarTotal(FiltroAccion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/acciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAccionSolicitudTraslado guardar(SgAccionSolicitudTraslado accion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (accion == null || userToken == null) {
            return null;
        }
        if (accion.getAccPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/acciones");
            return RestClient.invokePost(webTarget, accion, SgAccionSolicitudTraslado.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/acciones");
            webTarget = webTarget.path(accion.getAccPk().toString());
            return RestClient.invokePut(webTarget, accion, SgAccionSolicitudTraslado.class, userToken);
        }
    }

    public SgAccionSolicitudTraslado obtenerPorId(Long accionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (accionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/acciones");
        webTarget = webTarget.path(accionPk.toString());
        return RestClient.invokeGet(webTarget, SgAccionSolicitudTraslado.class, userToken);
    }

    public void eliminar(Long accionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (accionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/acciones");
        webTarget = webTarget.path(accionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAccionSolicitudTraslado> obtenerHistorialPorId(Long accionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (accionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/acciones/historial");
        webTarget = webTarget.path(accionPk.toString());
        SgAccionSolicitudTraslado[] acciones = RestClient.invokeGet(webTarget, SgAccionSolicitudTraslado[].class, userToken);
        return Arrays.asList(acciones);
    }
    

}
