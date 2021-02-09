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
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgAfComisionDescargo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;

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
public class ComisionUnidadActivoFijoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ComisionUnidadActivoFijoRestClient() {
    }


    public List<SgAfComisionDescargo> buscar(FiltroUnidadesActivoFijo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/comisionAF/buscar");
        SgAfComisionDescargo[] comision = RestClient.invokePost(webTarget, filtro, SgAfComisionDescargo[].class, userToken);
        return Arrays.asList(comision);
    }

    public Long buscarTotal(FiltroUnidadesActivoFijo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/comisionAF/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAfComisionDescargo guardar(SgAfComisionDescargo comision) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (comision == null || userToken == null) {
            return null;
        }
        if (comision.getCdePk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/comisionAF");
            return RestClient.invokePost(webTarget, comision, SgAfComisionDescargo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/comisionAF");
            webTarget = webTarget.path(comision.getCdePk().toString());
            return RestClient.invokePut(webTarget, comision, SgAfComisionDescargo.class, userToken);
        }
    }
    
    public SgAfComisionDescargo obtenerPorId(Long comisionId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (comisionId == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/comisionAF");
        webTarget = webTarget.path(comisionId.toString());
        return RestClient.invokeGet(webTarget, SgAfComisionDescargo.class, userToken);
    }

    public void eliminar(Long comisionId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (comisionId == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/comisionAF");
        webTarget = webTarget.path(comisionId.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAfComisionDescargo> obtenerHistorialPorId(Long comisionId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (comisionId == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/comisionAF/historial");
        webTarget = webTarget.path(comisionId.toString());
        SgAfComisionDescargo[] estados = RestClient.invokeGet(webTarget, SgAfComisionDescargo[].class, userToken);
        return Arrays.asList(estados);
    }
}