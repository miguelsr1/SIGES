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
import sv.gob.mined.siges.web.dto.SgEspacioComun;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEspacioComplementario;

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
public class EspacioComunRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EspacioComunRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEspacioComun> buscar(FiltroEspacioComplementario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/espaciocomun/buscar");
        SgEspacioComun[] espacioComun = RestClient.invokePost(webTarget, filtro, SgEspacioComun[].class, userToken);
        return Arrays.asList(espacioComun);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroEspacioComplementario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/espaciocomun/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEspacioComun guardar(SgEspacioComun espacioComun) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (espacioComun == null || userToken == null) {
            return null;
        }
        if (espacioComun.getEcoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/espaciocomun");
            return RestClient.invokePost(webTarget, espacioComun, SgEspacioComun.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/espaciocomun");
            webTarget = webTarget.path(espacioComun.getEcoPk().toString());
            return RestClient.invokePut(webTarget, espacioComun, SgEspacioComun.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEspacioComun obtenerPorId(Long espacioComunPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (espacioComunPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/espaciocomun");
        webTarget = webTarget.path(espacioComunPk.toString());
        return RestClient.invokeGet(webTarget, SgEspacioComun.class, userToken);
    }

    public void eliminar(Long espacioComunPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (espacioComunPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/espaciocomun");
        webTarget = webTarget.path(espacioComunPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEspacioComun> obtenerHistorialPorId(Long espacioComunPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (espacioComunPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/espaciocomun/historial");
        webTarget = webTarget.path(espacioComunPk.toString());
        SgEspacioComun[] espacioComun = RestClient.invokeGet(webTarget, SgEspacioComun[].class, userToken);
        return Arrays.asList(espacioComun);
    }
    

}
