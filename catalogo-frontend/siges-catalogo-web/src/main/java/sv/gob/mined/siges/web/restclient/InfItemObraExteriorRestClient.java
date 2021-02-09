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
import sv.gob.mined.siges.web.dto.SgInfItemObraExterior;
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
public class InfItemObraExteriorRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InfItemObraExteriorRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfItemObraExterior> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemobraexterior/buscar");
        SgInfItemObraExterior[] itemObraExterior = RestClient.invokePost(webTarget, filtro, SgInfItemObraExterior[].class, userToken);
        return Arrays.asList(itemObraExterior);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemobraexterior/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInfItemObraExterior guardar(SgInfItemObraExterior infItemObraExterior) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infItemObraExterior == null || userToken == null) {
            return null;
        }
        if (infItemObraExterior.getIoePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemobraexterior");
            return RestClient.invokePost(webTarget, infItemObraExterior, SgInfItemObraExterior.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemobraexterior");
            webTarget = webTarget.path(infItemObraExterior.getIoePk().toString());
            return RestClient.invokePut(webTarget, infItemObraExterior, SgInfItemObraExterior.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgInfItemObraExterior obtenerPorId(Long infItemObraExteriorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infItemObraExteriorPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemobraexterior");
        webTarget = webTarget.path(infItemObraExteriorPk.toString());
        return RestClient.invokeGet(webTarget, SgInfItemObraExterior.class, userToken);
    }

    public void eliminar(Long infItemObraExteriorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infItemObraExteriorPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemobraexterior");
        webTarget = webTarget.path(infItemObraExteriorPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfItemObraExterior> obtenerHistorialPorId(Long infItemObraExteriorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infItemObraExteriorPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemobraexterior/historial");
        webTarget = webTarget.path(infItemObraExteriorPk.toString());
        SgInfItemObraExterior[] itemObraExterior = RestClient.invokeGet(webTarget, SgInfItemObraExterior[].class, userToken);
        return Arrays.asList(itemObraExterior);
    }
    

}
