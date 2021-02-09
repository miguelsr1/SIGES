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
import sv.gob.mined.siges.web.dto.SgItemLiquidacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class ItemLiquidacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ItemLiquidacionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgItemLiquidacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemsliquidacion/buscar");
        SgItemLiquidacion[] itemsLiquidacion = RestClient.invokePost(webTarget, filtro, SgItemLiquidacion[].class, userToken);
        return Arrays.asList(itemsLiquidacion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemsliquidacion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgItemLiquidacion guardar(SgItemLiquidacion itemLiquidacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (itemLiquidacion == null || userToken == null) {
            return null;
        }
        if (itemLiquidacion.getIliPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemsliquidacion");
            return RestClient.invokePost(webTarget, itemLiquidacion, SgItemLiquidacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemsliquidacion");
            webTarget = webTarget.path(itemLiquidacion.getIliPk().toString());
            return RestClient.invokePut(webTarget, itemLiquidacion, SgItemLiquidacion.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgItemLiquidacion obtenerPorId(Long itemLiquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (itemLiquidacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemsliquidacion");
        webTarget = webTarget.path(itemLiquidacionPk.toString());
        return RestClient.invokeGet(webTarget, SgItemLiquidacion.class, userToken);
    }

    public void eliminar(Long itemLiquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (itemLiquidacionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemsliquidacion");
        webTarget = webTarget.path(itemLiquidacionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgItemLiquidacion> obtenerHistorialPorId(Long itemLiquidacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (itemLiquidacionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemsliquidacion/historial");
        webTarget = webTarget.path(itemLiquidacionPk.toString());
        SgItemLiquidacion[] itemsLiquidacion = RestClient.invokeGet(webTarget, SgItemLiquidacion[].class, userToken);
        return Arrays.asList(itemsLiquidacion);
    }
    

}
