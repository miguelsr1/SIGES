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
import sv.gob.mined.siges.web.dto.SgInfTipologiaConstruccion;
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
public class InfTipologiaConstruccionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InfTipologiaConstruccionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfTipologiaConstruccion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipologiaconstruccion/buscar");
        SgInfTipologiaConstruccion[] tipologiaConstruccion = RestClient.invokePost(webTarget, filtro, SgInfTipologiaConstruccion[].class, userToken);
        return Arrays.asList(tipologiaConstruccion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipologiaconstruccion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInfTipologiaConstruccion guardar(SgInfTipologiaConstruccion infTipologiaConstruccion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipologiaConstruccion == null || userToken == null) {
            return null;
        }
        if (infTipologiaConstruccion.getTicPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipologiaconstruccion");
            return RestClient.invokePost(webTarget, infTipologiaConstruccion, SgInfTipologiaConstruccion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipologiaconstruccion");
            webTarget = webTarget.path(infTipologiaConstruccion.getTicPk().toString());
            return RestClient.invokePut(webTarget, infTipologiaConstruccion, SgInfTipologiaConstruccion.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgInfTipologiaConstruccion obtenerPorId(Long infTipologiaConstruccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipologiaConstruccionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipologiaconstruccion");
        webTarget = webTarget.path(infTipologiaConstruccionPk.toString());
        return RestClient.invokeGet(webTarget, SgInfTipologiaConstruccion.class, userToken);
    }

    public void eliminar(Long infTipologiaConstruccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipologiaConstruccionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipologiaconstruccion");
        webTarget = webTarget.path(infTipologiaConstruccionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfTipologiaConstruccion> obtenerHistorialPorId(Long infTipologiaConstruccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipologiaConstruccionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipologiaconstruccion/historial");
        webTarget = webTarget.path(infTipologiaConstruccionPk.toString());
        SgInfTipologiaConstruccion[] tipologiaConstruccion = RestClient.invokeGet(webTarget, SgInfTipologiaConstruccion[].class, userToken);
        return Arrays.asList(tipologiaConstruccion);
    }
    

}
