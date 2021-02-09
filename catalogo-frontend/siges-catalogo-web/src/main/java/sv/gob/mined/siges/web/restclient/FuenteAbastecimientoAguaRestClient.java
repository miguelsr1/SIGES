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
import sv.gob.mined.siges.web.dto.SgFuenteAbastecimientoAgua;
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
public class FuenteAbastecimientoAguaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public FuenteAbastecimientoAguaRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgFuenteAbastecimientoAgua> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentesabastecimientoagua/buscar");
        SgFuenteAbastecimientoAgua[] fuentesAbastecimientoAgua = RestClient.invokePost(webTarget, filtro, SgFuenteAbastecimientoAgua[].class, userToken);
        return Arrays.asList(fuentesAbastecimientoAgua);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentesabastecimientoagua/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgFuenteAbastecimientoAgua guardar(SgFuenteAbastecimientoAgua fuenteAbastecimientoAgua) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteAbastecimientoAgua == null || userToken == null) {
            return null;
        }
        if (fuenteAbastecimientoAgua.getFaaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentesabastecimientoagua");
            return RestClient.invokePost(webTarget, fuenteAbastecimientoAgua, SgFuenteAbastecimientoAgua.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentesabastecimientoagua");
            webTarget = webTarget.path(fuenteAbastecimientoAgua.getFaaPk().toString());
            return RestClient.invokePut(webTarget, fuenteAbastecimientoAgua, SgFuenteAbastecimientoAgua.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgFuenteAbastecimientoAgua obtenerPorId(Long fuenteAbastecimientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteAbastecimientoAguaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentesabastecimientoagua");
        webTarget = webTarget.path(fuenteAbastecimientoAguaPk.toString());
        return RestClient.invokeGet(webTarget, SgFuenteAbastecimientoAgua.class, userToken);
    }

    public void eliminar(Long fuenteAbastecimientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteAbastecimientoAguaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentesabastecimientoagua");
        webTarget = webTarget.path(fuenteAbastecimientoAguaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgFuenteAbastecimientoAgua> obtenerHistorialPorId(Long fuenteAbastecimientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteAbastecimientoAguaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentesabastecimientoagua/historial");
        webTarget = webTarget.path(fuenteAbastecimientoAguaPk.toString());
        SgFuenteAbastecimientoAgua[] fuentesAbastecimientoAgua = RestClient.invokeGet(webTarget, SgFuenteAbastecimientoAgua[].class, userToken);
        return Arrays.asList(fuentesAbastecimientoAgua);
    }
    

}
