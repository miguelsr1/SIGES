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
import sv.gob.mined.siges.web.dto.SgAfCategoriaBienes;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaBienes;

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
public class CategoriaBienesRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CategoriaBienesRestClient() {
    }


    public List<SgAfCategoriaBienes> buscar(FiltroCategoriaBienes filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriabienes/buscar");
        SgAfCategoriaBienes[] categoriaBienes = RestClient.invokePost(webTarget, filtro, SgAfCategoriaBienes[].class, userToken);
        return Arrays.asList(categoriaBienes);
    }

    public Long buscarTotal(FiltroCategoriaBienes filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriabienes/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAfCategoriaBienes guardar(SgAfCategoriaBienes catBienes) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (catBienes == null || userToken == null) {
            return null;
        }
        if (catBienes.getCabPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriabienes");
            return RestClient.invokePost(webTarget, catBienes, SgAfCategoriaBienes.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriabienes");
            webTarget = webTarget.path(catBienes.getCabPk().toString());
            return RestClient.invokePut(webTarget, catBienes, SgAfCategoriaBienes.class, userToken);
        }
    }

    public SgAfCategoriaBienes obtenerPorId(Long clasBienesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (clasBienesPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriabienes");
        webTarget = webTarget.path(clasBienesPk.toString());
        return RestClient.invokeGet(webTarget, SgAfCategoriaBienes.class, userToken);
    }

    public void eliminar(Long clasBienesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (clasBienesPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriabienes");
        webTarget = webTarget.path(clasBienesPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAfCategoriaBienes> obtenerHistorialPorId(Long catBienesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (catBienesPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriabienes/historial");
        webTarget = webTarget.path(catBienesPk.toString());
        SgAfCategoriaBienes[] categoriaBienes = RestClient.invokeGet(webTarget, SgAfCategoriaBienes[].class, userToken);
        return Arrays.asList(categoriaBienes);
    }
    

}
