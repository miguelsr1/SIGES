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
import sv.gob.mined.siges.web.dto.SgInfMinisterioOtorgante;
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
public class InfMinisterioOtorganteRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InfMinisterioOtorganteRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfMinisterioOtorgante> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ministeriootorgante/buscar");
        SgInfMinisterioOtorgante[] ministerioOtorgante = RestClient.invokePost(webTarget, filtro, SgInfMinisterioOtorgante[].class, userToken);
        return Arrays.asList(ministerioOtorgante);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ministeriootorgante/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInfMinisterioOtorgante guardar(SgInfMinisterioOtorgante infMinisterioOtorgante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infMinisterioOtorgante == null || userToken == null) {
            return null;
        }
        if (infMinisterioOtorgante.getMioPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ministeriootorgante");
            return RestClient.invokePost(webTarget, infMinisterioOtorgante, SgInfMinisterioOtorgante.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ministeriootorgante");
            webTarget = webTarget.path(infMinisterioOtorgante.getMioPk().toString());
            return RestClient.invokePut(webTarget, infMinisterioOtorgante, SgInfMinisterioOtorgante.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgInfMinisterioOtorgante obtenerPorId(Long infMinisterioOtorgantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infMinisterioOtorgantePk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ministeriootorgante");
        webTarget = webTarget.path(infMinisterioOtorgantePk.toString());
        return RestClient.invokeGet(webTarget, SgInfMinisterioOtorgante.class, userToken);
    }

    public void eliminar(Long infMinisterioOtorgantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infMinisterioOtorgantePk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ministeriootorgante");
        webTarget = webTarget.path(infMinisterioOtorgantePk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfMinisterioOtorgante> obtenerHistorialPorId(Long infMinisterioOtorgantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infMinisterioOtorgantePk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ministeriootorgante/historial");
        webTarget = webTarget.path(infMinisterioOtorgantePk.toString());
        SgInfMinisterioOtorgante[] ministerioOtorgante = RestClient.invokeGet(webTarget, SgInfMinisterioOtorgante[].class, userToken);
        return Arrays.asList(ministerioOtorgante);
    }
    

}
