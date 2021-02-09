/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgGradoAfectacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

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
public class GradoAfectacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public GradoAfectacionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgGradoAfectacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/gradoafectacion/buscar");
        SgGradoAfectacion[] gradoAfectacion = RestClient.invokePost(webTarget, filtro, SgGradoAfectacion[].class, userToken);
        return Arrays.asList(gradoAfectacion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/gradoafectacion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgGradoAfectacion guardar(SgGradoAfectacion gradoAfectacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoAfectacion == null || userToken == null) {
            return null;
        }
        if (gradoAfectacion.getGafPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/gradoafectacion");
            return RestClient.invokePost(webTarget, gradoAfectacion, SgGradoAfectacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/gradoafectacion");
            webTarget = webTarget.path(gradoAfectacion.getGafPk().toString());
            return RestClient.invokePut(webTarget, gradoAfectacion, SgGradoAfectacion.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgGradoAfectacion obtenerPorId(Long gradoAfectacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoAfectacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/gradoafectacion");
        webTarget = webTarget.path(gradoAfectacionPk.toString());
        return RestClient.invokeGet(webTarget, SgGradoAfectacion.class, userToken);
    }

    public void eliminar(Long gradoAfectacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoAfectacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/gradoafectacion");
        webTarget = webTarget.path(gradoAfectacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgGradoAfectacion> obtenerHistorialPorId(Long gradoAfectacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoAfectacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/gradoafectacion/historial");
        webTarget = webTarget.path(gradoAfectacionPk.toString());
        SgGradoAfectacion[] gradoAfectacion = RestClient.invokeGet(webTarget, SgGradoAfectacion[].class, userToken);
        return Arrays.asList(gradoAfectacion);
    }
    

}
