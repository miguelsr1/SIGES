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
import sv.gob.mined.siges.web.dto.SgPerfilesUsuariosIngresadosCe;
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
public class PerfilesUsuariosIngresadosCeRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public PerfilesUsuariosIngresadosCeRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgPerfilesUsuariosIngresadosCe> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/perfilesdeusuariosingresadosce/buscar");
        SgPerfilesUsuariosIngresadosCe[] perfilesdeusuariosingresadosce = RestClient.invokePost(webTarget, filtro, SgPerfilesUsuariosIngresadosCe[].class, userToken);
        return Arrays.asList(perfilesdeusuariosingresadosce);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/perfilesdeusuariosingresadosce/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgPerfilesUsuariosIngresadosCe guardar(SgPerfilesUsuariosIngresadosCe perfilesUsuariosIngresadosCe) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (perfilesUsuariosIngresadosCe == null || userToken == null) {
            return null;
        }
        if (perfilesUsuariosIngresadosCe.getPuiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/perfilesdeusuariosingresadosce");
            return RestClient.invokePost(webTarget, perfilesUsuariosIngresadosCe, SgPerfilesUsuariosIngresadosCe.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/perfilesdeusuariosingresadosce");
            webTarget = webTarget.path(perfilesUsuariosIngresadosCe.getPuiPk().toString());
            return RestClient.invokePut(webTarget, perfilesUsuariosIngresadosCe, SgPerfilesUsuariosIngresadosCe.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgPerfilesUsuariosIngresadosCe obtenerPorId(Long perfilesUsuariosIngresadosCePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (perfilesUsuariosIngresadosCePk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/perfilesdeusuariosingresadosce");
        webTarget = webTarget.path(perfilesUsuariosIngresadosCePk.toString());
        return RestClient.invokeGet(webTarget, SgPerfilesUsuariosIngresadosCe.class, userToken);
    }

    public void eliminar(Long perfilesUsuariosIngresadosCePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (perfilesUsuariosIngresadosCePk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/perfilesdeusuariosingresadosce");
        webTarget = webTarget.path(perfilesUsuariosIngresadosCePk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgPerfilesUsuariosIngresadosCe> obtenerHistorialPorId(Long perfilesUsuariosIngresadosCePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (perfilesUsuariosIngresadosCePk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/perfilesdeusuariosingresadosce/historial");
        webTarget = webTarget.path(perfilesUsuariosIngresadosCePk.toString());
        SgPerfilesUsuariosIngresadosCe[] perfilesdeusuariosingresadosce = RestClient.invokeGet(webTarget, SgPerfilesUsuariosIngresadosCe[].class, userToken);
        return Arrays.asList(perfilesdeusuariosingresadosce);
    }
    

}
