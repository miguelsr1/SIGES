/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.rest;

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
import sv.gob.mined.siges.web.dto.SgCategoriaViolencia;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.restclient.RestClient;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

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
public class CategoriaViolenciaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CategoriaViolenciaRestClient() {
    }


    public List<SgCategoriaViolencia> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaviolencia/buscar");
        SgCategoriaViolencia[] categoriaViolencia = RestClient.invokePost(webTarget, filtro, SgCategoriaViolencia[].class, userToken);
        return Arrays.asList(categoriaViolencia);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaviolencia/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCategoriaViolencia guardar(SgCategoriaViolencia categoriaViolencia) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaViolencia == null || userToken == null) {
            return null;
        }
        if (categoriaViolencia.getCavPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaviolencia");
            return RestClient.invokePost(webTarget, categoriaViolencia, SgCategoriaViolencia.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaviolencia");
            webTarget = webTarget.path(categoriaViolencia.getCavPk().toString());
            return RestClient.invokePut(webTarget, categoriaViolencia, SgCategoriaViolencia.class, userToken);
        }
    }

    public SgCategoriaViolencia obtenerPorId(Long categoriaViolenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaViolenciaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaviolencia");
        webTarget = webTarget.path(categoriaViolenciaPk.toString());
        return RestClient.invokeGet(webTarget, SgCategoriaViolencia.class, userToken);
    }

    public void eliminar(Long categoriaViolenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaViolenciaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaviolencia");
        webTarget = webTarget.path(categoriaViolenciaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgCategoriaViolencia> obtenerHistorialPorId(Long categoriaViolenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaViolenciaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaviolencia/historial");
        webTarget = webTarget.path(categoriaViolenciaPk.toString());
        SgCategoriaViolencia[] categoriaViolencia = RestClient.invokeGet(webTarget, SgCategoriaViolencia[].class, userToken);
        return Arrays.asList(categoriaViolencia);
    }
    

}
