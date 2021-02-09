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
import sv.gob.mined.siges.web.dto.SgTipoAlfabetizador;
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
public class TipoAlfabetizadorRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoAlfabetizadorRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoAlfabetizador> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoalfabetizador/buscar");
        SgTipoAlfabetizador[] tipoAlfabetizador = RestClient.invokePost(webTarget, filtro, SgTipoAlfabetizador[].class, userToken);
        return Arrays.asList(tipoAlfabetizador);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoalfabetizador/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoAlfabetizador guardar(SgTipoAlfabetizador tipoAlfabetizador) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAlfabetizador == null || userToken == null) {
            return null;
        }
        if (tipoAlfabetizador.getTalPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoalfabetizador");
            return RestClient.invokePost(webTarget, tipoAlfabetizador, SgTipoAlfabetizador.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoalfabetizador");
            webTarget = webTarget.path(tipoAlfabetizador.getTalPk().toString());
            return RestClient.invokePut(webTarget, tipoAlfabetizador, SgTipoAlfabetizador.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTipoAlfabetizador obtenerPorId(Long tipoAlfabetizadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAlfabetizadorPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoalfabetizador");
        webTarget = webTarget.path(tipoAlfabetizadorPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoAlfabetizador.class, userToken);
    }

    public void eliminar(Long tipoAlfabetizadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAlfabetizadorPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoalfabetizador");
        webTarget = webTarget.path(tipoAlfabetizadorPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoAlfabetizador> obtenerHistorialPorId(Long tipoAlfabetizadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAlfabetizadorPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoalfabetizador/historial");
        webTarget = webTarget.path(tipoAlfabetizadorPk.toString());
        SgTipoAlfabetizador[] tipoAlfabetizador = RestClient.invokeGet(webTarget, SgTipoAlfabetizador[].class, userToken);
        return Arrays.asList(tipoAlfabetizador);
    }
    

}
