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
import sv.gob.mined.siges.web.dto.SgProyectoFinanciamientoSistemaIntegrado;
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
public class ProyectoFinanciamientoSistemaIntegradoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public ProyectoFinanciamientoSistemaIntegradoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgProyectoFinanciamientoSistemaIntegrado> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado/buscar");
        SgProyectoFinanciamientoSistemaIntegrado[] proyectosFinanciamientoSistemaIntegrado = RestClient.invokePost(webTarget, filtro, SgProyectoFinanciamientoSistemaIntegrado[].class, userToken);
        return Arrays.asList(proyectosFinanciamientoSistemaIntegrado);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgProyectoFinanciamientoSistemaIntegrado guardar(SgProyectoFinanciamientoSistemaIntegrado proyectoFinanciamientoSistemaIntegrado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegrado == null || userToken == null) {
            return null;
        }
        if (proyectoFinanciamientoSistemaIntegrado.getPfsPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado");
            return RestClient.invokePost(webTarget, proyectoFinanciamientoSistemaIntegrado, SgProyectoFinanciamientoSistemaIntegrado.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado");
            webTarget = webTarget.path(proyectoFinanciamientoSistemaIntegrado.getPfsPk().toString());
            return RestClient.invokePut(webTarget, proyectoFinanciamientoSistemaIntegrado, SgProyectoFinanciamientoSistemaIntegrado.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgProyectoFinanciamientoSistemaIntegrado obtenerPorId(Long proyectoFinanciamientoSistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegradoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado");
        webTarget = webTarget.path(proyectoFinanciamientoSistemaIntegradoPk.toString());
        return RestClient.invokeGet(webTarget, SgProyectoFinanciamientoSistemaIntegrado.class, userToken);
    }

    public void eliminar(Long proyectoFinanciamientoSistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegradoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado");
        webTarget = webTarget.path(proyectoFinanciamientoSistemaIntegradoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgProyectoFinanciamientoSistemaIntegrado> obtenerHistorialPorId(Long proyectoFinanciamientoSistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegradoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado/historial");
        webTarget = webTarget.path(proyectoFinanciamientoSistemaIntegradoPk.toString());
        SgProyectoFinanciamientoSistemaIntegrado[] proyectosFinanciamientoSistemaIntegrado = RestClient.invokeGet(webTarget, SgProyectoFinanciamientoSistemaIntegrado[].class, userToken);
        return Arrays.asList(proyectosFinanciamientoSistemaIntegrado);
    }

}
