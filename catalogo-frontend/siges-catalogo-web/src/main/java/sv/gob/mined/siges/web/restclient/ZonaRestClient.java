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
import sv.gob.mined.siges.web.dto.SgZona;
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
public class ZonaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ZonaRestClient() {
    }


    public List<SgZona> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas/buscar");
        SgZona[] zonas = RestClient.invokePost(webTarget, filtro, SgZona[].class, userToken);
        return Arrays.asList(zonas);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgZona guardar(SgZona zona) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (zona == null || userToken == null) {
            return null;
        }
        if (zona.getZonPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas");
            return RestClient.invokePost(webTarget, zona, SgZona.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas");
            webTarget = webTarget.path(zona.getZonPk().toString());
            return RestClient.invokePut(webTarget, zona, SgZona.class, userToken);
        }
    }

    public SgZona obtenerPorId(Long zonaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (zonaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas");
        webTarget = webTarget.path(zonaPk.toString());
        return RestClient.invokeGet(webTarget, SgZona.class, userToken);
    }

    public void eliminar(Long zonaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (zonaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas");
        webTarget = webTarget.path(zonaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgZona> obtenerHistorialPorId(Long zonaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (zonaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas/historial");
        webTarget = webTarget.path(zonaPk.toString());
        SgZona[] zonas = RestClient.invokeGet(webTarget, SgZona[].class, userToken);
        return Arrays.asList(zonas);
    }
    

}
