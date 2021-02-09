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
import sv.gob.mined.siges.web.dto.SgTrastornoAprendizaje;
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
@Timeout(value = 5000L)
public class TrastornoAprendizajeRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TrastornoAprendizajeRestClient() {
    }


    public List<SgTrastornoAprendizaje> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/trastornosaprendizaje/buscar");
        SgTrastornoAprendizaje[] trastornosaprendizaje = RestClient.invokePost(webTarget, filtro, SgTrastornoAprendizaje[].class, userToken);
        return Arrays.asList(trastornosaprendizaje);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/trastornosaprendizaje/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTrastornoAprendizaje guardar(SgTrastornoAprendizaje discapacidad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (discapacidad == null || userToken == null) {
            return null;
        }
        if (discapacidad.getTraPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/trastornosaprendizaje");
            return RestClient.invokePost(webTarget, discapacidad, SgTrastornoAprendizaje.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/trastornosaprendizaje");
            webTarget = webTarget.path(discapacidad.getTraPk().toString());
            return RestClient.invokePut(webTarget, discapacidad, SgTrastornoAprendizaje.class, userToken);
        }
    }

    public SgTrastornoAprendizaje obtenerPorId(Long discapacidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (discapacidadPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/trastornosaprendizaje");
        webTarget = webTarget.path(discapacidadPk.toString());
        return RestClient.invokeGet(webTarget, SgTrastornoAprendizaje.class, userToken);
    }

    public void eliminar(Long discapacidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (discapacidadPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/trastornosaprendizaje");
        webTarget = webTarget.path(discapacidadPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTrastornoAprendizaje> obtenerHistorialPorId(Long discapacidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (discapacidadPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/trastornosaprendizaje/historial");
        webTarget = webTarget.path(discapacidadPk.toString());
        SgTrastornoAprendizaje[] trastornosaprendizaje = RestClient.invokeGet(webTarget, SgTrastornoAprendizaje[].class, userToken);
        return Arrays.asList(trastornosaprendizaje);
    }
    

}
