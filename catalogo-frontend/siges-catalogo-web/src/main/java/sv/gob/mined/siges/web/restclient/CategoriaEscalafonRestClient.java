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
import sv.gob.mined.siges.web.dto.SgCategoriaEscalafon;
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
public class CategoriaEscalafonRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CategoriaEscalafonRestClient() {
    }


    public List<SgCategoriaEscalafon> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaescalafon/buscar");
        SgCategoriaEscalafon[] categoriaEscalafon = RestClient.invokePost(webTarget, filtro, SgCategoriaEscalafon[].class, userToken);
        return Arrays.asList(categoriaEscalafon);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaescalafon/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCategoriaEscalafon guardar(SgCategoriaEscalafon categoriaEscalafon) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaEscalafon == null || userToken == null) {
            return null;
        }
        if (categoriaEscalafon.getCesPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaescalafon");
            return RestClient.invokePost(webTarget, categoriaEscalafon, SgCategoriaEscalafon.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaescalafon");
            webTarget = webTarget.path(categoriaEscalafon.getCesPk().toString());
            return RestClient.invokePut(webTarget, categoriaEscalafon, SgCategoriaEscalafon.class, userToken);
        }
    }

    public SgCategoriaEscalafon obtenerPorId(Long categoriaEscalafonPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaEscalafonPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaescalafon");
        webTarget = webTarget.path(categoriaEscalafonPk.toString());
        return RestClient.invokeGet(webTarget, SgCategoriaEscalafon.class, userToken);
    }

    public void eliminar(Long categoriaEscalafonPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaEscalafonPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaescalafon");
        webTarget = webTarget.path(categoriaEscalafonPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgCategoriaEscalafon> obtenerHistorialPorId(Long categoriaEscalafonPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaEscalafonPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaescalafon/historial");
        webTarget = webTarget.path(categoriaEscalafonPk.toString());
        SgCategoriaEscalafon[] categoriaEscalafon = RestClient.invokeGet(webTarget, SgCategoriaEscalafon[].class, userToken);
        return Arrays.asList(categoriaEscalafon);
    }
    

}
