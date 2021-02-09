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
import sv.gob.mined.siges.web.dto.SgClasificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;

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
public class ClasificacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ClasificacionRestClient() {
    }


    public List<SgClasificacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificaciones/buscar");
        SgClasificacion[] clasificaciones = RestClient.invokePost(webTarget, filtro, SgClasificacion[].class, userToken);
        return Arrays.asList(clasificaciones);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificaciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgClasificacion guardar(SgClasificacion clasificacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (clasificacion == null || userToken == null) {
            return null;
        }
        if (clasificacion.getClaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificaciones");
            return RestClient.invokePost(webTarget, clasificacion, SgClasificacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificaciones");
            webTarget = webTarget.path(clasificacion.getClaPk().toString());
            return RestClient.invokePut(webTarget, clasificacion, SgClasificacion.class, userToken);
        }
    }

    public SgClasificacion obtenerPorId(Long clasificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (clasificacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificaciones");
        webTarget = webTarget.path(clasificacionPk.toString());
        return RestClient.invokeGet(webTarget, SgClasificacion.class, userToken);
    }

    public void eliminar(Long clasificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (clasificacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificaciones");
        webTarget = webTarget.path(clasificacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgClasificacion> obtenerHistorialPorId(Long clasificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (clasificacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificaciones/historial");
        webTarget = webTarget.path(clasificacionPk.toString());
        SgClasificacion[] clasificaciones = RestClient.invokeGet(webTarget, SgClasificacion[].class, userToken);
        return Arrays.asList(clasificaciones);
    }
    

}
