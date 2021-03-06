/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
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
import sv.gob.mined.siges.web.dto.SgModoPago;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 3000L)
public class ModoPagoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ModoPagoRestClient() {
    }


    public List<SgModoPago> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modopago/buscar");
        SgModoPago[] modoPago = RestClient.invokePost(webTarget, filtro, SgModoPago[].class, userToken);
        return Arrays.asList(modoPago);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modopago/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgModoPago guardar(SgModoPago modoPago) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modoPago == null || userToken == null) {
            return null;
        }
        if (modoPago.getMpaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modopago");
            return RestClient.invokePost(webTarget, modoPago, SgModoPago.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modopago");
            webTarget = webTarget.path(modoPago.getMpaPk().toString());
            return RestClient.invokePut(webTarget, modoPago, SgModoPago.class, userToken);
        }
    }

    public SgModoPago obtenerPorId(Long modoPagoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modoPagoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modopago");
        webTarget = webTarget.path(modoPagoPk.toString());
        return RestClient.invokeGet(webTarget, SgModoPago.class, userToken);
    }

    public void eliminar(Long modoPagoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modoPagoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modopago");
        webTarget = webTarget.path(modoPagoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgModoPago> obtenerHistorialPorId(Long modoPagoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modoPagoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modopago/historial");
        webTarget = webTarget.path(modoPagoPk.toString());
        SgModoPago[] modoPago = RestClient.invokeGet(webTarget, SgModoPago[].class, userToken);
        return Arrays.asList(modoPago);
    }
    

}
