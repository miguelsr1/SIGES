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
import sv.gob.mined.siges.web.dto.SgCategoriaEmpleado;
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
public class CategoriaEmpleadoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CategoriaEmpleadoRestClient() {
    }


    public List<SgCategoriaEmpleado> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaempleado/buscar");
        SgCategoriaEmpleado[] categoriaEmpleado = RestClient.invokePost(webTarget, filtro, SgCategoriaEmpleado[].class, userToken);
        return Arrays.asList(categoriaEmpleado);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaempleado/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCategoriaEmpleado guardar(SgCategoriaEmpleado categoriaEmpleado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaEmpleado == null || userToken == null) {
            return null;
        }
        if (categoriaEmpleado.getCemPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaempleado");
            return RestClient.invokePost(webTarget, categoriaEmpleado, SgCategoriaEmpleado.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaempleado");
            webTarget = webTarget.path(categoriaEmpleado.getCemPk().toString());
            return RestClient.invokePut(webTarget, categoriaEmpleado, SgCategoriaEmpleado.class, userToken);
        }
    }

    public SgCategoriaEmpleado obtenerPorId(Long categoriaEmpleadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaEmpleadoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaempleado");
        webTarget = webTarget.path(categoriaEmpleadoPk.toString());
        return RestClient.invokeGet(webTarget, SgCategoriaEmpleado.class, userToken);
    }

    public void eliminar(Long categoriaEmpleadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaEmpleadoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaempleado");
        webTarget = webTarget.path(categoriaEmpleadoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgCategoriaEmpleado> obtenerHistorialPorId(Long categoriaEmpleadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaEmpleadoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaempleado/historial");
        webTarget = webTarget.path(categoriaEmpleadoPk.toString());
        SgCategoriaEmpleado[] categoriaEmpleado = RestClient.invokeGet(webTarget, SgCategoriaEmpleado[].class, userToken);
        return Arrays.asList(categoriaEmpleado);
    }
    

}
