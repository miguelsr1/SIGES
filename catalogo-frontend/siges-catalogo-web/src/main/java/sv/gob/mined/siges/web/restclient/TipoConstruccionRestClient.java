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
import sv.gob.mined.siges.web.dto.SgTipoConstruccion;
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
public class TipoConstruccionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoConstruccionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoConstruccion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoconstruccion/buscar");
        SgTipoConstruccion[] tipoConstruccion = RestClient.invokePost(webTarget, filtro, SgTipoConstruccion[].class, userToken);
        return Arrays.asList(tipoConstruccion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoconstruccion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoConstruccion guardar(SgTipoConstruccion tipoConstruccion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoConstruccion == null || userToken == null) {
            return null;
        }
        if (tipoConstruccion.getTcoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoconstruccion");
            return RestClient.invokePost(webTarget, tipoConstruccion, SgTipoConstruccion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoconstruccion");
            webTarget = webTarget.path(tipoConstruccion.getTcoPk().toString());
            return RestClient.invokePut(webTarget, tipoConstruccion, SgTipoConstruccion.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTipoConstruccion obtenerPorId(Long tipoConstruccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoConstruccionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoconstruccion");
        webTarget = webTarget.path(tipoConstruccionPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoConstruccion.class, userToken);
    }

    public void eliminar(Long tipoConstruccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoConstruccionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoconstruccion");
        webTarget = webTarget.path(tipoConstruccionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoConstruccion> obtenerHistorialPorId(Long tipoConstruccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoConstruccionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoconstruccion/historial");
        webTarget = webTarget.path(tipoConstruccionPk.toString());
        SgTipoConstruccion[] tipoConstruccion = RestClient.invokeGet(webTarget, SgTipoConstruccion[].class, userToken);
        return Arrays.asList(tipoConstruccion);
    }
    

}
