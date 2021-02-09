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
import sv.gob.mined.siges.web.dto.SgTipoManifestacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTiposManifestacionViolencia;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 5000L)
public class TipoManifestacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoManifestacionRestClient() {
    }


    public List<SgTipoManifestacion> buscar(FiltroTiposManifestacionViolencia filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmanifestacion/buscar");
        SgTipoManifestacion[] tiposManifestacionViolencia = RestClient.invokePost(webTarget, filtro, SgTipoManifestacion[].class, userToken);
        return Arrays.asList(tiposManifestacionViolencia);
    }

    public Long buscarTotal(FiltroTiposManifestacionViolencia filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmanifestacion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoManifestacion guardar(SgTipoManifestacion tipoManifestacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoManifestacion == null || userToken == null) {
            return null;
        }
        if (tipoManifestacion.getTmaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmanifestacion");
            return RestClient.invokePost(webTarget, tipoManifestacion, SgTipoManifestacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmanifestacion");
            webTarget = webTarget.path(tipoManifestacion.getTmaPk().toString());
            return RestClient.invokePut(webTarget, tipoManifestacion, SgTipoManifestacion.class, userToken);
        }
    }

    public SgTipoManifestacion obtenerPorId(Long tipoManifestacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoManifestacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmanifestacion");
        webTarget = webTarget.path(tipoManifestacionPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoManifestacion.class, userToken);
    }

    public void eliminar(Long tipoManifestacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoManifestacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmanifestacion");
        webTarget = webTarget.path(tipoManifestacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoManifestacion> obtenerHistorialPorId(Long tipoManifestacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoManifestacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmanifestacion/historial");
        webTarget = webTarget.path(tipoManifestacionPk.toString());
        SgTipoManifestacion[] tiposManifestacionViolencia = RestClient.invokeGet(webTarget, SgTipoManifestacion[].class, userToken);
        return Arrays.asList(tiposManifestacionViolencia);
    }
    

}
