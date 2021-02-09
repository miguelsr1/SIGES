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
import sv.gob.mined.siges.web.dto.SgTipoRequerimientoInfra;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 3000L)
public class TipoRequerimientoInfraRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoRequerimientoInfraRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoRequerimientoInfra> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposrequerimientoinfra/buscar");
        SgTipoRequerimientoInfra[] tiposRequerimientoInfra = RestClient.invokePost(webTarget, filtro, SgTipoRequerimientoInfra[].class, userToken);
        return Arrays.asList(tiposRequerimientoInfra);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposrequerimientoinfra/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoRequerimientoInfra guardar(SgTipoRequerimientoInfra tipoRequerimientoInfra) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoRequerimientoInfra == null || userToken == null) {
            return null;
        }
        if (tipoRequerimientoInfra.getTriPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposrequerimientoinfra");
            return RestClient.invokePost(webTarget, tipoRequerimientoInfra, SgTipoRequerimientoInfra.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposrequerimientoinfra");
            webTarget = webTarget.path(tipoRequerimientoInfra.getTriPk().toString());
            return RestClient.invokePut(webTarget, tipoRequerimientoInfra, SgTipoRequerimientoInfra.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTipoRequerimientoInfra obtenerPorId(Long tipoRequerimientoInfraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoRequerimientoInfraPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposrequerimientoinfra");
        webTarget = webTarget.path(tipoRequerimientoInfraPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoRequerimientoInfra.class, userToken);
    }

    public void eliminar(Long tipoRequerimientoInfraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoRequerimientoInfraPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposrequerimientoinfra");
        webTarget = webTarget.path(tipoRequerimientoInfraPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoRequerimientoInfra> obtenerHistorialPorId(Long tipoRequerimientoInfraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoRequerimientoInfraPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposrequerimientoinfra/historial");
        webTarget = webTarget.path(tipoRequerimientoInfraPk.toString());
        SgTipoRequerimientoInfra[] tiposRequerimientoInfra = RestClient.invokeGet(webTarget, SgTipoRequerimientoInfra[].class, userToken);
        return Arrays.asList(tiposRequerimientoInfra);
    }
    

}
