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
import sv.gob.mined.siges.web.dto.SgMensaje;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMensaje;

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
public class MensajeRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MensajeRestClient() {
    }


    public List<SgMensaje> buscar(FiltroMensaje filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/mensajes/buscar");
        SgMensaje[] mensajes = RestClient.invokePost(webTarget, filtro, SgMensaje[].class, userToken);
        return Arrays.asList(mensajes);
    }

    public Long buscarTotal(FiltroMensaje filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/mensajes/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMensaje guardar(SgMensaje mensaje) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (mensaje == null || userToken == null) {
            return null;
        }
        if (mensaje.getMsjPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/mensajes");
            return RestClient.invokePost(webTarget, mensaje, SgMensaje.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/mensajes");
            webTarget = webTarget.path(mensaje.getMsjPk().toString());
            return RestClient.invokePut(webTarget, mensaje, SgMensaje.class, userToken);
        }
    }

    public SgMensaje obtenerPorId(Long mensajePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (mensajePk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/mensajes");
        webTarget = webTarget.path(mensajePk.toString());
        return RestClient.invokeGet(webTarget, SgMensaje.class, userToken);
    }

    public void eliminar(Long mensajePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (mensajePk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/mensajes");
        webTarget = webTarget.path(mensajePk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgMensaje> obtenerHistorialPorId(Long mensajePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (mensajePk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/mensajes/historial");
        webTarget = webTarget.path(mensajePk.toString());
        SgMensaje[] mensajes = RestClient.invokeGet(webTarget, SgMensaje[].class, userToken);
        return Arrays.asList(mensajes);
    }
    

}
