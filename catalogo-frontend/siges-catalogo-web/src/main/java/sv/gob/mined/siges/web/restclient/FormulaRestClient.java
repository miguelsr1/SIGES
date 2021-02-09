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
import sv.gob.mined.siges.web.dto.SgFormula;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFormula;

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
public class FormulaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public FormulaRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgFormula> buscar(FiltroFormula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formulas/buscar");
        SgFormula[] formulas = RestClient.invokePost(webTarget, filtro, SgFormula[].class, userToken);
        return Arrays.asList(formulas);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroFormula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formulas/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgFormula guardar(SgFormula formula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formula == null || userToken == null) {
            return null;
        }
        if (formula.getFomPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formulas");
            return RestClient.invokePost(webTarget, formula, SgFormula.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formulas");
            webTarget = webTarget.path(formula.getFomPk().toString());
            return RestClient.invokePut(webTarget, formula, SgFormula.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgFormula obtenerPorId(Long formulaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formulaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formulas");
        webTarget = webTarget.path(formulaPk.toString());
        return RestClient.invokeGet(webTarget, SgFormula.class, userToken);
    }

    public void eliminar(Long formulaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formulaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formulas");
        webTarget = webTarget.path(formulaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgFormula> obtenerHistorialPorId(Long formulaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formulaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formulas/historial");
        webTarget = webTarget.path(formulaPk.toString());
        SgFormula[] formulas = RestClient.invokeGet(webTarget, SgFormula[].class, userToken);
        return Arrays.asList(formulas);
    }
    

}
