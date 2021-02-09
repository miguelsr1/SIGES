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
import sv.gob.mined.siges.web.dto.SgCargoOAE;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCargoOAE;

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
public class CargoOAERestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CargoOAERestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCargoOAE> buscar(FiltroCargoOAE filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargooae/buscar");
        SgCargoOAE[] cargoOAE = RestClient.invokePost(webTarget, filtro, SgCargoOAE[].class, userToken);
        return Arrays.asList(cargoOAE);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCargoOAE filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargooae/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCargoOAE guardar(SgCargoOAE cargoOAE) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargoOAE == null || userToken == null) {
            return null;
        }
        if (cargoOAE.getCoaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargooae");
            return RestClient.invokePost(webTarget, cargoOAE, SgCargoOAE.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargooae");
            webTarget = webTarget.path(cargoOAE.getCoaPk().toString());
            return RestClient.invokePut(webTarget, cargoOAE, SgCargoOAE.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCargoOAE obtenerPorId(Long cargoOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargoOAEPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargooae");
        webTarget = webTarget.path(cargoOAEPk.toString());
        return RestClient.invokeGet(webTarget, SgCargoOAE.class, userToken);
    }

    public void eliminar(Long cargoOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargoOAEPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargooae");
        webTarget = webTarget.path(cargoOAEPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCargoOAE> obtenerHistorialPorId(Long cargoOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargoOAEPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargooae/historial");
        webTarget = webTarget.path(cargoOAEPk.toString());
        SgCargoOAE[] cargoOAE = RestClient.invokeGet(webTarget, SgCargoOAE[].class, userToken);
        return Arrays.asList(cargoOAE);
    }
    

}
