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
import sv.gob.mined.siges.web.dto.SgElementoHogar;
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
public class ElementoHogarRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ElementoHogarRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgElementoHogar> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/elementoshogar/buscar");
        SgElementoHogar[] elementosHogar = RestClient.invokePost(webTarget, filtro, SgElementoHogar[].class, userToken);
        return Arrays.asList(elementosHogar);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/elementoshogar/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgElementoHogar guardar(SgElementoHogar elementoHogar) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (elementoHogar == null || userToken == null) {
            return null;
        }
        if (elementoHogar.getEhoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/elementoshogar");
            return RestClient.invokePost(webTarget, elementoHogar, SgElementoHogar.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/elementoshogar");
            webTarget = webTarget.path(elementoHogar.getEhoPk().toString());
            return RestClient.invokePut(webTarget, elementoHogar, SgElementoHogar.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgElementoHogar obtenerPorId(Long elementoHogarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (elementoHogarPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/elementoshogar");
        webTarget = webTarget.path(elementoHogarPk.toString());
        return RestClient.invokeGet(webTarget, SgElementoHogar.class, userToken);
    }

    public void eliminar(Long elementoHogarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (elementoHogarPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/elementoshogar");
        webTarget = webTarget.path(elementoHogarPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgElementoHogar> obtenerHistorialPorId(Long elementoHogarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (elementoHogarPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/elementoshogar/historial");
        webTarget = webTarget.path(elementoHogarPk.toString());
        SgElementoHogar[] elementosHogar = RestClient.invokeGet(webTarget, SgElementoHogar[].class, userToken);
        return Arrays.asList(elementosHogar);
    }
    

}
