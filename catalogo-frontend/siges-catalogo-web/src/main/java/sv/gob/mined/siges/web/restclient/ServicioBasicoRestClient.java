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
import sv.gob.mined.siges.web.dto.SgServicioBasico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioBasico;

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
public class ServicioBasicoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ServicioBasicoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgServicioBasico> buscar(FiltroServicioBasico filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/serviciosbasicos/buscar");
        SgServicioBasico[] serviciosBasicos = RestClient.invokePost(webTarget, filtro, SgServicioBasico[].class, userToken);
        return Arrays.asList(serviciosBasicos);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroServicioBasico filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/serviciosbasicos/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgServicioBasico guardar(SgServicioBasico servicioBasico) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioBasico == null || userToken == null) {
            return null;
        }
        if (servicioBasico.getSbaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/serviciosbasicos");
            return RestClient.invokePost(webTarget, servicioBasico, SgServicioBasico.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/serviciosbasicos");
            webTarget = webTarget.path(servicioBasico.getSbaPk().toString());
            return RestClient.invokePut(webTarget, servicioBasico, SgServicioBasico.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgServicioBasico obtenerPorId(Long servicioBasicoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioBasicoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/serviciosbasicos");
        webTarget = webTarget.path(servicioBasicoPk.toString());
        return RestClient.invokeGet(webTarget, SgServicioBasico.class, userToken);
    }

    public void eliminar(Long servicioBasicoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioBasicoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/serviciosbasicos");
        webTarget = webTarget.path(servicioBasicoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgServicioBasico> obtenerHistorialPorId(Long servicioBasicoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioBasicoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/serviciosbasicos/historial");
        webTarget = webTarget.path(servicioBasicoPk.toString());
        SgServicioBasico[] serviciosBasicos = RestClient.invokeGet(webTarget, SgServicioBasico[].class, userToken);
        return Arrays.asList(serviciosBasicos);
    }
    

}
