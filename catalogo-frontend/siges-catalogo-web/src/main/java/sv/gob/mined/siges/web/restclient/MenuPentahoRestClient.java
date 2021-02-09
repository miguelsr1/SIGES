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
import sv.gob.mined.siges.web.dto.SgMenuPentaho;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMenuPentaho;

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
public class MenuPentahoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public MenuPentahoRestClient() {
    }


    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMenuPentaho> buscar(FiltroMenuPentaho filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/menupentaho/buscar");
        SgMenuPentaho[] menuPentaho = RestClient.invokePost(webTarget, filtro, SgMenuPentaho[].class, userToken);
        return Arrays.asList(menuPentaho);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroMenuPentaho filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/menupentaho/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMenuPentaho guardar(SgMenuPentaho menuPetnaho) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (menuPetnaho == null || userToken == null) {
            return null;
        }
        if (menuPetnaho.getMpePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/menupentaho");
            return RestClient.invokePost(webTarget, menuPetnaho, SgMenuPentaho.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/menupentaho");
            webTarget = webTarget.path(menuPetnaho.getMpePk().toString());
            return RestClient.invokePut(webTarget, menuPetnaho, SgMenuPentaho.class, userToken);
        }
    }

   @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMenuPentaho obtenerPorId(Long menuPentahoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (menuPentahoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/menupentaho");
        webTarget = webTarget.path(menuPentahoPk.toString());
        return RestClient.invokeGet(webTarget, SgMenuPentaho.class, userToken);
    }

    public void eliminar(Long menuPentahoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (menuPentahoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/menupentaho");
        webTarget = webTarget.path(menuPentahoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMenuPentaho> obtenerHistorialPorId(Long menuPentahoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (menuPentahoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/menupentaho/historial");
        webTarget = webTarget.path(menuPentahoPk.toString());
        SgMenuPentaho[] menuPentaho = RestClient.invokeGet(webTarget, SgMenuPentaho[].class, userToken);
        return Arrays.asList(menuPentaho);
    }
    

}
