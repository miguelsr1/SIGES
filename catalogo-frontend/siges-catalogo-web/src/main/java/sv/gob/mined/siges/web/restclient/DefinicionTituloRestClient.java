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
import sv.gob.mined.siges.web.dto.SgDefinicionTitulo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDefinicionTitulo;

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
public class DefinicionTituloRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public DefinicionTituloRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgDefinicionTitulo> buscar(FiltroDefinicionTitulo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/definiciontitulo/buscar");
        SgDefinicionTitulo[] definicionTitulo = RestClient.invokePost(webTarget, filtro, SgDefinicionTitulo[].class, userToken);
        return Arrays.asList(definicionTitulo);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroDefinicionTitulo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/definiciontitulo/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgDefinicionTitulo guardar(SgDefinicionTitulo definicionTitulo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (definicionTitulo == null || userToken == null) {
            return null;
        }
        if (definicionTitulo.getDtiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/definiciontitulo");
            return RestClient.invokePost(webTarget, definicionTitulo, SgDefinicionTitulo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/definiciontitulo");
            webTarget = webTarget.path(definicionTitulo.getDtiPk().toString());
            return RestClient.invokePut(webTarget, definicionTitulo, SgDefinicionTitulo.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgDefinicionTitulo obtenerPorId(Long definicionTituloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (definicionTituloPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/definiciontitulo");
        webTarget = webTarget.path(definicionTituloPk.toString());
        return RestClient.invokeGet(webTarget, SgDefinicionTitulo.class, userToken);
    }

    public void eliminar(Long definicionTituloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (definicionTituloPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/definiciontitulo");
        webTarget = webTarget.path(definicionTituloPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgDefinicionTitulo> obtenerHistorialPorId(Long definicionTituloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (definicionTituloPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/definiciontitulo/historial");
        webTarget = webTarget.path(definicionTituloPk.toString());
        SgDefinicionTitulo[] definicionTitulo = RestClient.invokeGet(webTarget, SgDefinicionTitulo[].class, userToken);
        return Arrays.asList(definicionTitulo);
    }
    

}
