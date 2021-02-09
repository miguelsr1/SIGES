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
import sv.gob.mined.siges.web.dto.SgAlcanceCapacitacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;


@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 3000L)
public class AlcanceCapacitacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public AlcanceCapacitacionRestClient() {
    }


    public List<SgAlcanceCapacitacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/alcancescapacitacion/buscar");
        SgAlcanceCapacitacion[] alcanceCapacitacion = RestClient.invokePost(webTarget, filtro, SgAlcanceCapacitacion[].class, userToken);
        return Arrays.asList(alcanceCapacitacion);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/alcancescapacitacion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAlcanceCapacitacion guardar(SgAlcanceCapacitacion alcanceCapacitacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (alcanceCapacitacion == null || userToken == null) {
            return null;
        }
        if (alcanceCapacitacion.getAcaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/alcancescapacitacion");
            return RestClient.invokePost(webTarget, alcanceCapacitacion, SgAlcanceCapacitacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/alcancescapacitacion");
            webTarget = webTarget.path(alcanceCapacitacion.getAcaPk().toString());
            return RestClient.invokePut(webTarget, alcanceCapacitacion, SgAlcanceCapacitacion.class, userToken);
        }
    }

    public SgAlcanceCapacitacion obtenerPorId(Long alcanceCapacitacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (alcanceCapacitacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/alcancescapacitacion");
        webTarget = webTarget.path(alcanceCapacitacionPk.toString());
        return RestClient.invokeGet(webTarget, SgAlcanceCapacitacion.class, userToken);
    }

    public void eliminar(Long alcanceCapacitacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (alcanceCapacitacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/alcancescapacitacion");
        webTarget = webTarget.path(alcanceCapacitacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAlcanceCapacitacion> obtenerHistorialPorId(Long alcanceCapacitacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (alcanceCapacitacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/alcancescapacitacion/historial");
        webTarget = webTarget.path(alcanceCapacitacionPk.toString());
        SgAlcanceCapacitacion[] alcanceCapacitacion = RestClient.invokeGet(webTarget, SgAlcanceCapacitacion[].class, userToken);
        return Arrays.asList(alcanceCapacitacion);
    }
    

}
