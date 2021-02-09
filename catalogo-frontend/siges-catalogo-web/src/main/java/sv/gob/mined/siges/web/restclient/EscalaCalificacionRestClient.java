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
import sv.gob.mined.siges.web.dto.SgEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEscalaCalificacion;

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
public class EscalaCalificacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EscalaCalificacionRestClient() {
    }


    public List<SgEscalaCalificacion> buscar(FiltroEscalaCalificacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escalacalificacion/buscar");
        SgEscalaCalificacion[] escalaCalificacion = RestClient.invokePost(webTarget, filtro, SgEscalaCalificacion[].class, userToken);
        return Arrays.asList(escalaCalificacion);
    }

    public Long buscarTotal(FiltroEscalaCalificacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escalacalificacion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEscalaCalificacion guardar(SgEscalaCalificacion escalaCalificacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escalaCalificacion == null || userToken == null) {
            return null;
        }
        if (escalaCalificacion.getEcaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escalacalificacion");
            return RestClient.invokePost(webTarget, escalaCalificacion, SgEscalaCalificacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escalacalificacion");
            webTarget = webTarget.path(escalaCalificacion.getEcaPk().toString());
            return RestClient.invokePut(webTarget, escalaCalificacion, SgEscalaCalificacion.class, userToken);
        }
    }

    public SgEscalaCalificacion obtenerPorId(Long escalaCalificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escalaCalificacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escalacalificacion");
        webTarget = webTarget.path(escalaCalificacionPk.toString());
        return RestClient.invokeGet(webTarget, SgEscalaCalificacion.class, userToken);
    }

    public void eliminar(Long escalaCalificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escalaCalificacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escalacalificacion");
        webTarget = webTarget.path(escalaCalificacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgEscalaCalificacion> obtenerHistorialPorId(Long escalaCalificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escalaCalificacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escalacalificacion/historial");
        webTarget = webTarget.path(escalaCalificacionPk.toString());
        SgEscalaCalificacion[] escalaCalificacion = RestClient.invokeGet(webTarget, SgEscalaCalificacion[].class, userToken);
        return Arrays.asList(escalaCalificacion);
    }
    

}
