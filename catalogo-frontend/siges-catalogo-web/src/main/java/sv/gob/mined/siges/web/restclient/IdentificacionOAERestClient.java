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
import sv.gob.mined.siges.web.dto.SgIdentificacionOAE;
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
public class IdentificacionOAERestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public IdentificacionOAERestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgIdentificacionOAE> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/identificacionoae/buscar");
        SgIdentificacionOAE[] identificacionOAE = RestClient.invokePost(webTarget, filtro, SgIdentificacionOAE[].class, userToken);
        return Arrays.asList(identificacionOAE);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/identificacionoae/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgIdentificacionOAE guardar(SgIdentificacionOAE identificacionOAE) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (identificacionOAE == null || userToken == null) {
            return null;
        }
        if (identificacionOAE.getIoaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/identificacionoae");
            return RestClient.invokePost(webTarget, identificacionOAE, SgIdentificacionOAE.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/identificacionoae");
            webTarget = webTarget.path(identificacionOAE.getIoaPk().toString());
            return RestClient.invokePut(webTarget, identificacionOAE, SgIdentificacionOAE.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgIdentificacionOAE obtenerPorId(Long identificacionOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (identificacionOAEPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/identificacionoae");
        webTarget = webTarget.path(identificacionOAEPk.toString());
        return RestClient.invokeGet(webTarget, SgIdentificacionOAE.class, userToken);
    }

    public void eliminar(Long identificacionOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (identificacionOAEPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/identificacionoae");
        webTarget = webTarget.path(identificacionOAEPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgIdentificacionOAE> obtenerHistorialPorId(Long identificacionOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (identificacionOAEPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/identificacionoae/historial");
        webTarget = webTarget.path(identificacionOAEPk.toString());
        SgIdentificacionOAE[] identificacionOAE = RestClient.invokeGet(webTarget, SgIdentificacionOAE[].class, userToken);
        return Arrays.asList(identificacionOAE);
    }
    

}
