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
import sv.gob.mined.siges.web.dto.SgOcupacion;
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
public class OcupacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public OcupacionRestClient() {
    }


    public List<SgOcupacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ocupaciones/buscar");
        SgOcupacion[] ocupaciones = RestClient.invokePost(webTarget, filtro, SgOcupacion[].class, userToken);
        return Arrays.asList(ocupaciones);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ocupaciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgOcupacion guardar(SgOcupacion ocupacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ocupacion == null || userToken == null) {
            return null;
        }
        if (ocupacion.getOcuPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ocupaciones");
            return RestClient.invokePost(webTarget, ocupacion, SgOcupacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ocupaciones");
            webTarget = webTarget.path(ocupacion.getOcuPk().toString());
            return RestClient.invokePut(webTarget, ocupacion, SgOcupacion.class, userToken);
        }
    }

    public SgOcupacion obtenerPorId(Long ocupacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ocupacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ocupaciones");
        webTarget = webTarget.path(ocupacionPk.toString());
        return RestClient.invokeGet(webTarget, SgOcupacion.class, userToken);
    }

    public void eliminar(Long ocupacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ocupacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ocupaciones");
        webTarget = webTarget.path(ocupacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgOcupacion> obtenerHistorialPorId(Long ocupacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ocupacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ocupaciones/historial");
        webTarget = webTarget.path(ocupacionPk.toString());
        SgOcupacion[] ocupaciones = RestClient.invokeGet(webTarget, SgOcupacion[].class, userToken);
        return Arrays.asList(ocupaciones);
    }
    

}
