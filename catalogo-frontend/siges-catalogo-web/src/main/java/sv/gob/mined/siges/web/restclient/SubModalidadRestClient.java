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
import sv.gob.mined.siges.web.dto.SgSubModalidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSubModalidad;

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
public class SubModalidadRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public SubModalidadRestClient() {
    }


    public List<SgSubModalidad> buscar(FiltroSubModalidad filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades/buscar");
        SgSubModalidad[] submodalidades = RestClient.invokePost(webTarget, filtro, SgSubModalidad[].class, userToken);
        return Arrays.asList(submodalidades);
    }

    public Long buscarTotal(FiltroSubModalidad filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgSubModalidad guardar(SgSubModalidad subModalidad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (subModalidad == null || userToken == null) {
            return null;
        }
        if (subModalidad.getSmoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades");
            return RestClient.invokePost(webTarget, subModalidad, SgSubModalidad.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades");
            webTarget = webTarget.path(subModalidad.getSmoPk().toString());
            return RestClient.invokePut(webTarget, subModalidad, SgSubModalidad.class, userToken);
        }
    }

    public SgSubModalidad obtenerPorId(Long subModalidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (subModalidadPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades");
        webTarget = webTarget.path(subModalidadPk.toString());
        return RestClient.invokeGet(webTarget, SgSubModalidad.class, userToken);
    }

    public void eliminar(Long subModalidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (subModalidadPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades");
        webTarget = webTarget.path(subModalidadPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgSubModalidad> obtenerHistorialPorId(Long subModalidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (subModalidadPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades/historial");
        webTarget = webTarget.path(subModalidadPk.toString());
        SgSubModalidad[] submodalidades = RestClient.invokeGet(webTarget, SgSubModalidad[].class, userToken);
        return Arrays.asList(submodalidades);
    }
    

}
