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
import sv.gob.mined.siges.web.dto.SgInfAccesibilidad;
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
public class InfAccesibilidadRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InfAccesibilidadRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfAccesibilidad> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/accesibilidad/buscar");
        SgInfAccesibilidad[] accesibilidad = RestClient.invokePost(webTarget, filtro, SgInfAccesibilidad[].class, userToken);
        return Arrays.asList(accesibilidad);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/accesibilidad/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInfAccesibilidad guardar(SgInfAccesibilidad infAccesibilidad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infAccesibilidad == null || userToken == null) {
            return null;
        }
        if (infAccesibilidad.getAccPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/accesibilidad");
            return RestClient.invokePost(webTarget, infAccesibilidad, SgInfAccesibilidad.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/accesibilidad");
            webTarget = webTarget.path(infAccesibilidad.getAccPk().toString());
            return RestClient.invokePut(webTarget, infAccesibilidad, SgInfAccesibilidad.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgInfAccesibilidad obtenerPorId(Long infAccesibilidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infAccesibilidadPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/accesibilidad");
        webTarget = webTarget.path(infAccesibilidadPk.toString());
        return RestClient.invokeGet(webTarget, SgInfAccesibilidad.class, userToken);
    }

    public void eliminar(Long infAccesibilidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infAccesibilidadPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/accesibilidad");
        webTarget = webTarget.path(infAccesibilidadPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfAccesibilidad> obtenerHistorialPorId(Long infAccesibilidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infAccesibilidadPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/accesibilidad/historial");
        webTarget = webTarget.path(infAccesibilidadPk.toString());
        SgInfAccesibilidad[] accesibilidad = RestClient.invokeGet(webTarget, SgInfAccesibilidad[].class, userToken);
        return Arrays.asList(accesibilidad);
    }
    

}
