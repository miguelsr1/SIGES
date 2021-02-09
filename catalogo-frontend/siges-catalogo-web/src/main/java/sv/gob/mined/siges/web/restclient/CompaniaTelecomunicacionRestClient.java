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
import sv.gob.mined.siges.web.dto.SgCompaniaTelecomunicacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

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
public class CompaniaTelecomunicacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CompaniaTelecomunicacionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCompaniaTelecomunicacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/companiastelecomunicacion/buscar");
        SgCompaniaTelecomunicacion[] companiasTelecomunicacion = RestClient.invokePost(webTarget, filtro, SgCompaniaTelecomunicacion[].class, userToken);
        return Arrays.asList(companiasTelecomunicacion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/companiastelecomunicacion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCompaniaTelecomunicacion guardar(SgCompaniaTelecomunicacion companiaTelecomunicacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (companiaTelecomunicacion == null || userToken == null) {
            return null;
        }
        if (companiaTelecomunicacion.getCtePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/companiastelecomunicacion");
            return RestClient.invokePost(webTarget, companiaTelecomunicacion, SgCompaniaTelecomunicacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/companiastelecomunicacion");
            webTarget = webTarget.path(companiaTelecomunicacion.getCtePk().toString());
            return RestClient.invokePut(webTarget, companiaTelecomunicacion, SgCompaniaTelecomunicacion.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCompaniaTelecomunicacion obtenerPorId(Long companiaTelecomunicacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (companiaTelecomunicacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/companiastelecomunicacion");
        webTarget = webTarget.path(companiaTelecomunicacionPk.toString());
        return RestClient.invokeGet(webTarget, SgCompaniaTelecomunicacion.class, userToken);
    }

    public void eliminar(Long companiaTelecomunicacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (companiaTelecomunicacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/companiastelecomunicacion");
        webTarget = webTarget.path(companiaTelecomunicacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCompaniaTelecomunicacion> obtenerHistorialPorId(Long companiaTelecomunicacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (companiaTelecomunicacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/companiastelecomunicacion/historial");
        webTarget = webTarget.path(companiaTelecomunicacionPk.toString());
        SgCompaniaTelecomunicacion[] companiasTelecomunicacion = RestClient.invokeGet(webTarget, SgCompaniaTelecomunicacion[].class, userToken);
        return Arrays.asList(companiasTelecomunicacion);
    }
    

}
