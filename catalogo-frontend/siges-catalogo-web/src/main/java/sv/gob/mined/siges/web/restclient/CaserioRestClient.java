/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
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
import sv.gob.mined.siges.web.dto.SgCaserio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCaserio;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 3000L)
public class CaserioRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CaserioRestClient() {
    }


    public List<SgCaserio> buscar(FiltroCaserio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/caserios/buscar");
        SgCaserio[] caserios = RestClient.invokePost(webTarget, filtro, SgCaserio[].class, userToken);
        return Arrays.asList(caserios);
    }

    public Long buscarTotal(FiltroCaserio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/caserios/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCaserio guardar(SgCaserio caserio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (caserio == null || userToken == null) {
            return null;
        }
        if (caserio.getCasPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/caserios");
            return RestClient.invokePost(webTarget, caserio, SgCaserio.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/caserios");
            webTarget = webTarget.path(caserio.getCasPk().toString());
            return RestClient.invokePut(webTarget, caserio, SgCaserio.class, userToken);
        }
    }

    public SgCaserio obtenerPorId(Long caserioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (caserioPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/caserios");
        webTarget = webTarget.path(caserioPk.toString());
        return RestClient.invokeGet(webTarget, SgCaserio.class, userToken);
    }

    public void eliminar(Long caserioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (caserioPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/caserios");
        webTarget = webTarget.path(caserioPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgCaserio> obtenerHistorialPorId(Long caserioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (caserioPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/caserios/historial");
        webTarget = webTarget.path(caserioPk.toString());
        SgCaserio[] caserios = RestClient.invokeGet(webTarget, SgCaserio[].class, userToken);
        return Arrays.asList(caserios);
    }
    

}
