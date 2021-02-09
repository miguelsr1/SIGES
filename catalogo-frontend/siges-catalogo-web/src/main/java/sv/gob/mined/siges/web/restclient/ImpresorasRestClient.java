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
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgImpresora;

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
public class ImpresorasRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ImpresorasRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgImpresora> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/impresoras/buscar");
        SgImpresora[] impresoras = RestClient.invokePost(webTarget, filtro, SgImpresora[].class, userToken);
        return Arrays.asList(impresoras);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/impresoras/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgImpresora guardar(SgImpresora gradoRiesgo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoRiesgo == null || userToken == null) {
            return null;
        }
        if (gradoRiesgo.getImpPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/impresoras");
            return RestClient.invokePost(webTarget, gradoRiesgo, SgImpresora.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/impresoras");
            webTarget = webTarget.path(gradoRiesgo.getImpPk().toString());
            return RestClient.invokePut(webTarget, gradoRiesgo, SgImpresora.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgImpresora obtenerPorId(Long gradoRiesgoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoRiesgoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/impresoras");
        webTarget = webTarget.path(gradoRiesgoPk.toString());
        return RestClient.invokeGet(webTarget, SgImpresora.class, userToken);
    }

    public void eliminar(Long impresoraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (impresoraPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/impresoras");
        webTarget = webTarget.path(impresoraPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgImpresora> obtenerHistorialPorId(Long gradoRiesgoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoRiesgoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/impresoras/historial");
        webTarget = webTarget.path(gradoRiesgoPk.toString());
        SgImpresora[] impresoras = RestClient.invokeGet(webTarget, SgImpresora[].class, userToken);
        return Arrays.asList(impresoras);
    }
    

}
