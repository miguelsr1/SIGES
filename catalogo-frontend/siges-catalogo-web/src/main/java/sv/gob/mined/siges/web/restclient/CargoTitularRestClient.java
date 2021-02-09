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
import sv.gob.mined.siges.web.dto.SgCargoTitular;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCargoTitular;

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
public class CargoTitularRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CargoTitularRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCargoTitular> buscar(FiltroCargoTitular filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargotitular/buscar");
        SgCargoTitular[] cargoTitular = RestClient.invokePost(webTarget, filtro, SgCargoTitular[].class, userToken);
        return Arrays.asList(cargoTitular);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCargoTitular filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargotitular/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCargoTitular guardar(SgCargoTitular cargoTitular) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargoTitular == null || userToken == null) {
            return null;
        }
        if (cargoTitular.getCtiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargotitular");
            return RestClient.invokePost(webTarget, cargoTitular, SgCargoTitular.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargotitular");
            webTarget = webTarget.path(cargoTitular.getCtiPk().toString());
            return RestClient.invokePut(webTarget, cargoTitular, SgCargoTitular.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCargoTitular obtenerPorId(Long cargoTitularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargoTitularPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargotitular");
        webTarget = webTarget.path(cargoTitularPk.toString());
        return RestClient.invokeGet(webTarget, SgCargoTitular.class, userToken);
    }

    public void eliminar(Long cargoTitularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargoTitularPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargotitular");
        webTarget = webTarget.path(cargoTitularPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCargoTitular> obtenerHistorialPorId(Long cargoTitularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargoTitularPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargotitular/historial");
        webTarget = webTarget.path(cargoTitularPk.toString());
        SgCargoTitular[] cargoTitular = RestClient.invokeGet(webTarget, SgCargoTitular[].class, userToken);
        return Arrays.asList(cargoTitular);
    }
    

}
