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
import sv.gob.mined.siges.web.dto.SgInfNaturalezaContrato;
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
public class InfNaturalezaContratoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InfNaturalezaContratoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfNaturalezaContrato> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/naturalezacontrato/buscar");
        SgInfNaturalezaContrato[] naturalezaContrato = RestClient.invokePost(webTarget, filtro, SgInfNaturalezaContrato[].class, userToken);
        return Arrays.asList(naturalezaContrato);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/naturalezacontrato/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInfNaturalezaContrato guardar(SgInfNaturalezaContrato infNaturalezaContrato) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infNaturalezaContrato == null || userToken == null) {
            return null;
        }
        if (infNaturalezaContrato.getNacPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/naturalezacontrato");
            return RestClient.invokePost(webTarget, infNaturalezaContrato, SgInfNaturalezaContrato.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/naturalezacontrato");
            webTarget = webTarget.path(infNaturalezaContrato.getNacPk().toString());
            return RestClient.invokePut(webTarget, infNaturalezaContrato, SgInfNaturalezaContrato.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgInfNaturalezaContrato obtenerPorId(Long infNaturalezaContratoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infNaturalezaContratoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/naturalezacontrato");
        webTarget = webTarget.path(infNaturalezaContratoPk.toString());
        return RestClient.invokeGet(webTarget, SgInfNaturalezaContrato.class, userToken);
    }

    public void eliminar(Long infNaturalezaContratoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infNaturalezaContratoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/naturalezacontrato");
        webTarget = webTarget.path(infNaturalezaContratoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfNaturalezaContrato> obtenerHistorialPorId(Long infNaturalezaContratoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infNaturalezaContratoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/naturalezacontrato/historial");
        webTarget = webTarget.path(infNaturalezaContratoPk.toString());
        SgInfNaturalezaContrato[] naturalezaContrato = RestClient.invokeGet(webTarget, SgInfNaturalezaContrato[].class, userToken);
        return Arrays.asList(naturalezaContrato);
    }
    

}
