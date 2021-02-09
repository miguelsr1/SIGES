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
import sv.gob.mined.siges.web.dto.SgTituloAnterior;
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
public class TituloAnteriorRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TituloAnteriorRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTituloAnterior> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tituloanterior/buscar");
        SgTituloAnterior[] tituloAnterior = RestClient.invokePost(webTarget, filtro, SgTituloAnterior[].class, userToken);
        return Arrays.asList(tituloAnterior);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tituloanterior/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTituloAnterior guardar(SgTituloAnterior tituloAnterior) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tituloAnterior == null || userToken == null) {
            return null;
        }
        if (tituloAnterior.getTanPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tituloanterior");
            return RestClient.invokePost(webTarget, tituloAnterior, SgTituloAnterior.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tituloanterior");
            webTarget = webTarget.path(tituloAnterior.getTanPk().toString());
            return RestClient.invokePut(webTarget, tituloAnterior, SgTituloAnterior.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTituloAnterior obtenerPorId(Long tituloAnteriorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tituloAnteriorPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tituloanterior");
        webTarget = webTarget.path(tituloAnteriorPk.toString());
        return RestClient.invokeGet(webTarget, SgTituloAnterior.class, userToken);
    }

    public void eliminar(Long tituloAnteriorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tituloAnteriorPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tituloanterior");
        webTarget = webTarget.path(tituloAnteriorPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTituloAnterior> obtenerHistorialPorId(Long tituloAnteriorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tituloAnteriorPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tituloanterior/historial");
        webTarget = webTarget.path(tituloAnteriorPk.toString());
        SgTituloAnterior[] tituloAnterior = RestClient.invokeGet(webTarget, SgTituloAnterior[].class, userToken);
        return Arrays.asList(tituloAnterior);
    }
    

}
