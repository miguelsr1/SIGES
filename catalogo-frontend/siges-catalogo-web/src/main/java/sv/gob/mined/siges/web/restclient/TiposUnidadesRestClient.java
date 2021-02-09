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
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgAfTiposUnidades;
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
public class TiposUnidadesRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TiposUnidadesRestClient() {
    }


    public List<SgAfTiposUnidades> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipounidades/buscar");
        SgAfTiposUnidades[] unidades = RestClient.invokePost(webTarget, filtro, SgAfTiposUnidades[].class, userToken);
        return Arrays.asList(unidades);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipounidades/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAfTiposUnidades guardar(SgAfTiposUnidades tipoUnidad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoUnidad == null || userToken == null) {
            return null;
        }
        if (tipoUnidad.getTunPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipounidades");
            return RestClient.invokePost(webTarget, tipoUnidad, SgAfTiposUnidades.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipounidades");
            webTarget = webTarget.path(tipoUnidad.getTunPk().toString());
            return RestClient.invokePut(webTarget, tipoUnidad, SgAfTiposUnidades.class, userToken);
        }
    }

    public SgAfTiposUnidades obtenerPorId(Long tipoUnidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoUnidadPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipounidades");
        webTarget = webTarget.path(tipoUnidadPk.toString());
        return RestClient.invokeGet(webTarget, SgAfTiposUnidades.class, userToken);
    }

    public void eliminar(Long tipoUnidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoUnidadPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipounidades");
        webTarget = webTarget.path(tipoUnidadPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAfTiposUnidades> obtenerHistorialPorId(Long tipoUnidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoUnidadPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipounidades/historial");
        webTarget = webTarget.path(tipoUnidadPk.toString());
        SgAfTiposUnidades[] estados = RestClient.invokeGet(webTarget, SgAfTiposUnidades[].class, userToken);
        return Arrays.asList(estados);
    }
}