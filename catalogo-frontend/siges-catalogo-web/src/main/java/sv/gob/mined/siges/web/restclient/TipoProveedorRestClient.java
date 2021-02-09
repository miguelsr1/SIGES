/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgTipoProveedor;
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
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 3000L)
public class TipoProveedorRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoProveedorRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoProveedor> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposproveedor/buscar");
        SgTipoProveedor[] tiposProveedor = RestClient.invokePost(webTarget, filtro, SgTipoProveedor[].class, userToken);
        return Arrays.asList(tiposProveedor);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposproveedor/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoProveedor guardar(SgTipoProveedor tipoProveedor) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoProveedor == null || userToken == null) {
            return null;
        }
        if (tipoProveedor.getTprPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposproveedor");
            return RestClient.invokePost(webTarget, tipoProveedor, SgTipoProveedor.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposproveedor");
            webTarget = webTarget.path(tipoProveedor.getTprPk().toString());
            return RestClient.invokePut(webTarget, tipoProveedor, SgTipoProveedor.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTipoProveedor obtenerPorId(Long tipoProveedorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoProveedorPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposproveedor");
        webTarget = webTarget.path(tipoProveedorPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoProveedor.class, userToken);
    }

    public void eliminar(Long tipoProveedorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoProveedorPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposproveedor");
        webTarget = webTarget.path(tipoProveedorPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoProveedor> obtenerHistorialPorId(Long tipoProveedorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoProveedorPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposproveedor/historial");
        webTarget = webTarget.path(tipoProveedorPk.toString());
        SgTipoProveedor[] tiposProveedor = RestClient.invokeGet(webTarget, SgTipoProveedor[].class, userToken);
        return Arrays.asList(tiposProveedor);
    }
    

}
