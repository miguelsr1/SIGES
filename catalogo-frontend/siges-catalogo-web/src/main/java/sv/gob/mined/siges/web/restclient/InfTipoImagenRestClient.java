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
import sv.gob.mined.siges.web.dto.SgInfTipoImagen;
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
public class InfTipoImagenRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InfTipoImagenRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfTipoImagen> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoimagen/buscar");
        SgInfTipoImagen[] tipoImagen = RestClient.invokePost(webTarget, filtro, SgInfTipoImagen[].class, userToken);
        return Arrays.asList(tipoImagen);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoimagen/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInfTipoImagen guardar(SgInfTipoImagen infTipoImagen) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoImagen == null || userToken == null) {
            return null;
        }
        if (infTipoImagen.getTiiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoimagen");
            return RestClient.invokePost(webTarget, infTipoImagen, SgInfTipoImagen.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoimagen");
            webTarget = webTarget.path(infTipoImagen.getTiiPk().toString());
            return RestClient.invokePut(webTarget, infTipoImagen, SgInfTipoImagen.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgInfTipoImagen obtenerPorId(Long infTipoImagenPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoImagenPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoimagen");
        webTarget = webTarget.path(infTipoImagenPk.toString());
        return RestClient.invokeGet(webTarget, SgInfTipoImagen.class, userToken);
    }

    public void eliminar(Long infTipoImagenPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoImagenPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoimagen");
        webTarget = webTarget.path(infTipoImagenPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfTipoImagen> obtenerHistorialPorId(Long infTipoImagenPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoImagenPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoimagen/historial");
        webTarget = webTarget.path(infTipoImagenPk.toString());
        SgInfTipoImagen[] tipoImagen = RestClient.invokeGet(webTarget, SgInfTipoImagen[].class, userToken);
        return Arrays.asList(tipoImagen);
    }
    

}
