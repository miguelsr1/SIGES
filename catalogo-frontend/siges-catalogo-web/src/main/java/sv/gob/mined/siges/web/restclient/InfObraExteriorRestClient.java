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
import sv.gob.mined.siges.web.dto.SgInfObraExterior;
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
public class InfObraExteriorRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InfObraExteriorRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfObraExterior> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/obraexterior/buscar");
        SgInfObraExterior[] obraExterior = RestClient.invokePost(webTarget, filtro, SgInfObraExterior[].class, userToken);
        return Arrays.asList(obraExterior);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/obraexterior/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInfObraExterior guardar(SgInfObraExterior infObraExterior) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infObraExterior == null || userToken == null) {
            return null;
        }
        if (infObraExterior.getOexPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/obraexterior");
            return RestClient.invokePost(webTarget, infObraExterior, SgInfObraExterior.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/obraexterior");
            webTarget = webTarget.path(infObraExterior.getOexPk().toString());
            return RestClient.invokePut(webTarget, infObraExterior, SgInfObraExterior.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgInfObraExterior obtenerPorId(Long infObraExteriorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infObraExteriorPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/obraexterior");
        webTarget = webTarget.path(infObraExteriorPk.toString());
        return RestClient.invokeGet(webTarget, SgInfObraExterior.class, userToken);
    }

    public void eliminar(Long infObraExteriorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infObraExteriorPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/obraexterior");
        webTarget = webTarget.path(infObraExteriorPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfObraExterior> obtenerHistorialPorId(Long infObraExteriorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infObraExteriorPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/obraexterior/historial");
        webTarget = webTarget.path(infObraExteriorPk.toString());
        SgInfObraExterior[] obraExterior = RestClient.invokeGet(webTarget, SgInfObraExterior[].class, userToken);
        return Arrays.asList(obraExterior);
    }
    

}
