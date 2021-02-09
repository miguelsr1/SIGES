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
import sv.gob.mined.siges.web.dto.SgEstNombreExtraccion;
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
public class NombreExtraccionesRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public NombreExtraccionesRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstNombreExtraccion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nombresextracciones/buscar");
        SgEstNombreExtraccion[] nombresExtracciones = RestClient.invokePost(webTarget, filtro, SgEstNombreExtraccion[].class, userToken);
        return Arrays.asList(nombresExtracciones);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nombresextracciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEstNombreExtraccion guardar(SgEstNombreExtraccion nombreExtracciones) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nombreExtracciones == null || userToken == null) {
            return null;
        }
        if (nombreExtracciones.getNexPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nombresextracciones");
            return RestClient.invokePost(webTarget, nombreExtracciones, SgEstNombreExtraccion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nombresextracciones");
            webTarget = webTarget.path(nombreExtracciones.getNexPk().toString());
            return RestClient.invokePut(webTarget, nombreExtracciones, SgEstNombreExtraccion.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEstNombreExtraccion obtenerPorId(Long nombreExtraccionesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nombreExtraccionesPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nombresextracciones");
        webTarget = webTarget.path(nombreExtraccionesPk.toString());
        return RestClient.invokeGet(webTarget, SgEstNombreExtraccion.class, userToken);
    }

    public void eliminar(Long nombreExtraccionesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nombreExtraccionesPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nombresextracciones");
        webTarget = webTarget.path(nombreExtraccionesPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstNombreExtraccion> obtenerHistorialPorId(Long nombreExtraccionesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nombreExtraccionesPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nombresextracciones/historial");
        webTarget = webTarget.path(nombreExtraccionesPk.toString());
        SgEstNombreExtraccion[] nombresExtracciones = RestClient.invokeGet(webTarget, SgEstNombreExtraccion[].class, userToken);
        return Arrays.asList(nombresExtracciones);
    }
    

}
