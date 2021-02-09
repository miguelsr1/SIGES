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
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgOperacion;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOperacion;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class OperacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public OperacionRestClient() {
    }


    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public List<SgOperacion> buscar(FiltroOperacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/operaciones/buscar");
        SgOperacion[] operaciones = RestClient.invokePost(webTarget, filtro, SgOperacion[].class, userToken);
        return Arrays.asList(operaciones);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public Long buscarTotal(FiltroOperacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/operaciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgOperacion guardar(SgOperacion operacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (operacion == null || userToken == null) {
            return null;
        }
        if (operacion.getOpePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/operaciones");
            return RestClient.invokePost(webTarget, operacion, SgOperacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/operaciones");
            webTarget = webTarget.path(operacion.getOpePk().toString());
            return RestClient.invokePut(webTarget, operacion, SgOperacion.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public SgOperacion obtenerPorId(Long operacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (operacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/operaciones");
        webTarget = webTarget.path(operacionPk.toString());
        return RestClient.invokeGet(webTarget, SgOperacion.class, userToken);
    }

    public void eliminar(Long operacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (operacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/operaciones");
        webTarget = webTarget.path(operacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long operacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (operacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/operaciones/historial");
        webTarget = webTarget.path(operacionPk.toString());
        RevHistorico[] operaciones = RestClient.invokeGet(webTarget, RevHistorico[].class, userToken);
        return Arrays.asList(operaciones);
    }
    

}
