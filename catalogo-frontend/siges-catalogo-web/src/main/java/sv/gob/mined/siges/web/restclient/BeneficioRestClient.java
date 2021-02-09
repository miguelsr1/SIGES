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
import sv.gob.mined.siges.web.dto.SgBeneficio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

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
public class BeneficioRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public BeneficioRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgBeneficio> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/beneficio/buscar");
        SgBeneficio[] beneficio = RestClient.invokePost(webTarget, filtro, SgBeneficio[].class, userToken);
        return Arrays.asList(beneficio);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/beneficio/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgBeneficio guardar(SgBeneficio beneficio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (beneficio == null || userToken == null) {
            return null;
        }
        if (beneficio.getBnfPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/beneficio");
            return RestClient.invokePost(webTarget, beneficio, SgBeneficio.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/beneficio");
            webTarget = webTarget.path(beneficio.getBnfPk().toString());
            return RestClient.invokePut(webTarget, beneficio, SgBeneficio.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgBeneficio obtenerPorId(Long beneficioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (beneficioPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/beneficio");
        webTarget = webTarget.path(beneficioPk.toString());
        return RestClient.invokeGet(webTarget, SgBeneficio.class, userToken);
    }

    public void eliminar(Long beneficioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (beneficioPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/beneficio");
        webTarget = webTarget.path(beneficioPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgBeneficio> obtenerHistorialPorId(Long beneficioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (beneficioPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/beneficio/historial");
        webTarget = webTarget.path(beneficioPk.toString());
        SgBeneficio[] beneficio = RestClient.invokeGet(webTarget, SgBeneficio[].class, userToken);
        return Arrays.asList(beneficio);
    }
    

}
