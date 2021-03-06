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
import sv.gob.mined.siges.web.dto.SgCategoriaOperacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
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
@Timeout(value = 10000L)
public class CategoriaOperacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CategoriaOperacionRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public List<SgCategoriaOperacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/categoriasoperacion/buscar");
        SgCategoriaOperacion[] categoriasOperacion = RestClient.invokePost(webTarget, filtro, SgCategoriaOperacion[].class, userToken);
        return Arrays.asList(categoriasOperacion);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/categoriasoperacion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCategoriaOperacion guardar(SgCategoriaOperacion categoriaOperacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaOperacion == null || userToken == null) {
            return null;
        }
        if (categoriaOperacion.getCopPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/categoriasoperacion");
            return RestClient.invokePost(webTarget, categoriaOperacion, SgCategoriaOperacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/categoriasoperacion");
            webTarget = webTarget.path(categoriaOperacion.getCopPk().toString());
            return RestClient.invokePut(webTarget, categoriaOperacion, SgCategoriaOperacion.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public SgCategoriaOperacion obtenerPorId(Long categoriaOperacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaOperacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/categoriasoperacion");
        webTarget = webTarget.path(categoriaOperacionPk.toString());
        return RestClient.invokeGet(webTarget, SgCategoriaOperacion.class, userToken);
    }

    public void eliminar(Long categoriaOperacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaOperacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/categoriasoperacion");
        webTarget = webTarget.path(categoriaOperacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgCategoriaOperacion> obtenerHistorialPorId(Long categoriaOperacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaOperacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/categoriasoperacion/historial");
        webTarget = webTarget.path(categoriaOperacionPk.toString());
        SgCategoriaOperacion[] categoriasOperacion = RestClient.invokeGet(webTarget, SgCategoriaOperacion[].class, userToken);
        return Arrays.asList(categoriasOperacion);
    }
    

}
