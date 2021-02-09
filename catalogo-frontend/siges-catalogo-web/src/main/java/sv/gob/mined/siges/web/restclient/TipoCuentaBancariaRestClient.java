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
import sv.gob.mined.siges.web.dto.SgTipoCuentaBancaria;
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
public class TipoCuentaBancariaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoCuentaBancariaRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoCuentaBancaria> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcuentasbancarias/buscar");
        SgTipoCuentaBancaria[] tiposcuentasbancarias = RestClient.invokePost(webTarget, filtro, SgTipoCuentaBancaria[].class, userToken);
        return Arrays.asList(tiposcuentasbancarias);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcuentasbancarias/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoCuentaBancaria guardar(SgTipoCuentaBancaria tipoCuentaBancaria) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoCuentaBancaria == null || userToken == null) {
            return null;
        }
        if (tipoCuentaBancaria.getTcbPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcuentasbancarias");
            return RestClient.invokePost(webTarget, tipoCuentaBancaria, SgTipoCuentaBancaria.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcuentasbancarias");
            webTarget = webTarget.path(tipoCuentaBancaria.getTcbPk().toString());
            return RestClient.invokePut(webTarget, tipoCuentaBancaria, SgTipoCuentaBancaria.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTipoCuentaBancaria obtenerPorId(Long tipoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoCuentaBancariaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcuentasbancarias");
        webTarget = webTarget.path(tipoCuentaBancariaPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoCuentaBancaria.class, userToken);
    }

    public void eliminar(Long tipoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoCuentaBancariaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcuentasbancarias");
        webTarget = webTarget.path(tipoCuentaBancariaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoCuentaBancaria> obtenerHistorialPorId(Long tipoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoCuentaBancariaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcuentasbancarias/historial");
        webTarget = webTarget.path(tipoCuentaBancariaPk.toString());
        SgTipoCuentaBancaria[] tiposcuentasbancarias = RestClient.invokeGet(webTarget, SgTipoCuentaBancaria[].class, userToken);
        return Arrays.asList(tiposcuentasbancarias);
    }
    

}
