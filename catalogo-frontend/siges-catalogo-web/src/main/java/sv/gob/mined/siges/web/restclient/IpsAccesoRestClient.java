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
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgIpsAcceso;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIpAcceso;

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
public class IpsAccesoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public IpsAccesoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgIpsAcceso> buscar(FiltroIpAcceso filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ipsacceso/buscar");
        SgIpsAcceso[] ipsacceso = RestClient.invokePost(webTarget, filtro, SgIpsAcceso[].class, userToken);
        return Arrays.asList(ipsacceso);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroIpAcceso filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ipsacceso/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgIpsAcceso guardar(SgIpsAcceso ipacceso) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ipacceso == null || userToken == null) {
            return null;
        }
        if (ipacceso.getIpaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ipsacceso");
            return RestClient.invokePost(webTarget, ipacceso, SgIpsAcceso.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ipsacceso");
            webTarget = webTarget.path(ipacceso.getIpaPk().toString());
            return RestClient.invokePut(webTarget, ipacceso, SgIpsAcceso.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgIpsAcceso obtenerPorId(Long ipAccesoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ipAccesoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ipsacceso");
        webTarget = webTarget.path(ipAccesoPk.toString());
        return RestClient.invokeGet(webTarget, SgIpsAcceso.class, userToken);
    }

    public void eliminar(Long impresoraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (impresoraPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ipsacceso");
        webTarget = webTarget.path(impresoraPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgIpsAcceso> obtenerHistorialPorId(Long ipAccesoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ipAccesoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ipsacceso/historial");
        webTarget = webTarget.path(ipAccesoPk.toString());
        SgIpsAcceso[] impresoras = RestClient.invokeGet(webTarget, SgIpsAcceso[].class, userToken);
        return Arrays.asList(impresoras);
    }
    

}
