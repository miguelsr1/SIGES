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
import sv.gob.mined.siges.web.dto.SgCalidadIngresoAplicantes;
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
public class CalidadIngresoAplicantesRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CalidadIngresoAplicantesRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCalidadIngresoAplicantes> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calidadesingresoaplicantesplaza/buscar");
        SgCalidadIngresoAplicantes[] calidadesingresoaplicantesplaza = RestClient.invokePost(webTarget, filtro, SgCalidadIngresoAplicantes[].class, userToken);
        return Arrays.asList(calidadesingresoaplicantesplaza);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calidadesingresoaplicantesplaza/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCalidadIngresoAplicantes guardar(SgCalidadIngresoAplicantes calidadIngresoAplicantes) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calidadIngresoAplicantes == null || userToken == null) {
            return null;
        }
        if (calidadIngresoAplicantes.getCiaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calidadesingresoaplicantesplaza");
            return RestClient.invokePost(webTarget, calidadIngresoAplicantes, SgCalidadIngresoAplicantes.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calidadesingresoaplicantesplaza");
            webTarget = webTarget.path(calidadIngresoAplicantes.getCiaPk().toString());
            return RestClient.invokePut(webTarget, calidadIngresoAplicantes, SgCalidadIngresoAplicantes.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCalidadIngresoAplicantes obtenerPorId(Long calidadIngresoAplicantesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calidadIngresoAplicantesPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calidadesingresoaplicantesplaza");
        webTarget = webTarget.path(calidadIngresoAplicantesPk.toString());
        return RestClient.invokeGet(webTarget, SgCalidadIngresoAplicantes.class, userToken);
    }

    public void eliminar(Long calidadIngresoAplicantesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calidadIngresoAplicantesPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calidadesingresoaplicantesplaza");
        webTarget = webTarget.path(calidadIngresoAplicantesPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCalidadIngresoAplicantes> obtenerHistorialPorId(Long calidadIngresoAplicantesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calidadIngresoAplicantesPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calidadesingresoaplicantesplaza/historial");
        webTarget = webTarget.path(calidadIngresoAplicantesPk.toString());
        SgCalidadIngresoAplicantes[] calidadesingresoaplicantesplaza = RestClient.invokeGet(webTarget, SgCalidadIngresoAplicantes[].class, userToken);
        return Arrays.asList(calidadesingresoaplicantesplaza);
    }
    

}
