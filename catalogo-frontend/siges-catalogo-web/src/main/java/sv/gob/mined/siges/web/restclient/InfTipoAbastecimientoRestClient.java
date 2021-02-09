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
import sv.gob.mined.siges.web.dto.SgInfTipoAbastecimiento;
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
public class InfTipoAbastecimientoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InfTipoAbastecimientoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfTipoAbastecimiento> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoabastecimiento/buscar");
        SgInfTipoAbastecimiento[] tipoAbastecimiento = RestClient.invokePost(webTarget, filtro, SgInfTipoAbastecimiento[].class, userToken);
        return Arrays.asList(tipoAbastecimiento);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoabastecimiento/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInfTipoAbastecimiento guardar(SgInfTipoAbastecimiento infTipoAbastecimiento) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoAbastecimiento == null || userToken == null) {
            return null;
        }
        if (infTipoAbastecimiento.getTabPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoabastecimiento");
            return RestClient.invokePost(webTarget, infTipoAbastecimiento, SgInfTipoAbastecimiento.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoabastecimiento");
            webTarget = webTarget.path(infTipoAbastecimiento.getTabPk().toString());
            return RestClient.invokePut(webTarget, infTipoAbastecimiento, SgInfTipoAbastecimiento.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgInfTipoAbastecimiento obtenerPorId(Long infTipoAbastecimientoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoAbastecimientoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoabastecimiento");
        webTarget = webTarget.path(infTipoAbastecimientoPk.toString());
        return RestClient.invokeGet(webTarget, SgInfTipoAbastecimiento.class, userToken);
    }

    public void eliminar(Long infTipoAbastecimientoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoAbastecimientoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoabastecimiento");
        webTarget = webTarget.path(infTipoAbastecimientoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfTipoAbastecimiento> obtenerHistorialPorId(Long infTipoAbastecimientoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoAbastecimientoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoabastecimiento/historial");
        webTarget = webTarget.path(infTipoAbastecimientoPk.toString());
        SgInfTipoAbastecimiento[] tipoAbastecimiento = RestClient.invokeGet(webTarget, SgInfTipoAbastecimiento[].class, userToken);
        return Arrays.asList(tipoAbastecimiento);
    }
    

}
