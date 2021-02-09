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
import sv.gob.mined.siges.web.dto.SgEstadoDatoContratacion;
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
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 3000L)
public class EstadoDatoContratacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EstadoDatoContratacionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstadoDatoContratacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosdatocontratacion/buscar");
        SgEstadoDatoContratacion[] estadosDatoContratacion = RestClient.invokePost(webTarget, filtro, SgEstadoDatoContratacion[].class, userToken);
        return Arrays.asList(estadosDatoContratacion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosdatocontratacion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEstadoDatoContratacion guardar(SgEstadoDatoContratacion estadoDatoContratacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoDatoContratacion == null || userToken == null) {
            return null;
        }
        if (estadoDatoContratacion.getEdcPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosdatocontratacion");
            return RestClient.invokePost(webTarget, estadoDatoContratacion, SgEstadoDatoContratacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosdatocontratacion");
            webTarget = webTarget.path(estadoDatoContratacion.getEdcPk().toString());
            return RestClient.invokePut(webTarget, estadoDatoContratacion, SgEstadoDatoContratacion.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEstadoDatoContratacion obtenerPorId(Long estadoDatoContratacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoDatoContratacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosdatocontratacion");
        webTarget = webTarget.path(estadoDatoContratacionPk.toString());
        return RestClient.invokeGet(webTarget, SgEstadoDatoContratacion.class, userToken);
    }

    public void eliminar(Long estadoDatoContratacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoDatoContratacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosdatocontratacion");
        webTarget = webTarget.path(estadoDatoContratacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstadoDatoContratacion> obtenerHistorialPorId(Long estadoDatoContratacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoDatoContratacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosdatocontratacion/historial");
        webTarget = webTarget.path(estadoDatoContratacionPk.toString());
        SgEstadoDatoContratacion[] estadosDatoContratacion = RestClient.invokeGet(webTarget, SgEstadoDatoContratacion[].class, userToken);
        return Arrays.asList(estadosDatoContratacion);
    }
    

}
