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
import sv.gob.mined.siges.web.dto.SgNivelEmpleado;
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
public class NivelEmpleadoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public NivelEmpleadoRestClient() {
    }


    public List<SgNivelEmpleado> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelempleado/buscar");
        SgNivelEmpleado[] nivelEmpleado = RestClient.invokePost(webTarget, filtro, SgNivelEmpleado[].class, userToken);
        return Arrays.asList(nivelEmpleado);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelempleado/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgNivelEmpleado guardar(SgNivelEmpleado nivelEmpleado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelEmpleado == null || userToken == null) {
            return null;
        }
        if (nivelEmpleado.getNemPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelempleado");
            return RestClient.invokePost(webTarget, nivelEmpleado, SgNivelEmpleado.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelempleado");
            webTarget = webTarget.path(nivelEmpleado.getNemPk().toString());
            return RestClient.invokePut(webTarget, nivelEmpleado, SgNivelEmpleado.class, userToken);
        }
    }

    public SgNivelEmpleado obtenerPorId(Long nivelEmpleadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelEmpleadoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelempleado");
        webTarget = webTarget.path(nivelEmpleadoPk.toString());
        return RestClient.invokeGet(webTarget, SgNivelEmpleado.class, userToken);
    }

    public void eliminar(Long nivelEmpleadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelEmpleadoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelempleado");
        webTarget = webTarget.path(nivelEmpleadoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgNivelEmpleado> obtenerHistorialPorId(Long nivelEmpleadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelEmpleadoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelempleado/historial");
        webTarget = webTarget.path(nivelEmpleadoPk.toString());
        SgNivelEmpleado[] nivelEmpleado = RestClient.invokeGet(webTarget, SgNivelEmpleado[].class, userToken);
        return Arrays.asList(nivelEmpleado);
    }
    

}
