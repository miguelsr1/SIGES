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
import sv.gob.mined.siges.web.dto.SgMotivosSeleccionPLaza;
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
public class MotivosSeleccionPLazaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MotivosSeleccionPLazaRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMotivosSeleccionPLaza> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosseleccionplaza/buscar");
        SgMotivosSeleccionPLaza[] motivosseleccionplaza = RestClient.invokePost(webTarget, filtro, SgMotivosSeleccionPLaza[].class, userToken);
        return Arrays.asList(motivosseleccionplaza);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosseleccionplaza/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMotivosSeleccionPLaza guardar(SgMotivosSeleccionPLaza motivosSeleccionPLaza) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivosSeleccionPLaza == null || userToken == null) {
            return null;
        }
        if (motivosSeleccionPLaza.getMspPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosseleccionplaza");
            return RestClient.invokePost(webTarget, motivosSeleccionPLaza, SgMotivosSeleccionPLaza.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosseleccionplaza");
            webTarget = webTarget.path(motivosSeleccionPLaza.getMspPk().toString());
            return RestClient.invokePut(webTarget, motivosSeleccionPLaza, SgMotivosSeleccionPLaza.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMotivosSeleccionPLaza obtenerPorId(Long motivosSeleccionPLazaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivosSeleccionPLazaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosseleccionplaza");
        webTarget = webTarget.path(motivosSeleccionPLazaPk.toString());
        return RestClient.invokeGet(webTarget, SgMotivosSeleccionPLaza.class, userToken);
    }

    public void eliminar(Long motivosSeleccionPLazaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivosSeleccionPLazaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosseleccionplaza");
        webTarget = webTarget.path(motivosSeleccionPLazaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMotivosSeleccionPLaza> obtenerHistorialPorId(Long motivosSeleccionPLazaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivosSeleccionPLazaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosseleccionplaza/historial");
        webTarget = webTarget.path(motivosSeleccionPLazaPk.toString());
        SgMotivosSeleccionPLaza[] motivosseleccionplaza = RestClient.invokeGet(webTarget, SgMotivosSeleccionPLaza[].class, userToken);
        return Arrays.asList(motivosseleccionplaza);
    }
    

}
