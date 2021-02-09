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
import sv.gob.mined.siges.web.dto.SgLeySalario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;

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
public class LeySalarioRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public LeySalarioRestClient() {
    }


    public List<SgLeySalario> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/leysalario/buscar");
        SgLeySalario[] leySalario = RestClient.invokePost(webTarget, filtro, SgLeySalario[].class, userToken);
        return Arrays.asList(leySalario);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/leysalario/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgLeySalario guardar(SgLeySalario leySalario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (leySalario == null || userToken == null) {
            return null;
        }
        if (leySalario.getLsaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/leysalario");
            return RestClient.invokePost(webTarget, leySalario, SgLeySalario.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/leysalario");
            webTarget = webTarget.path(leySalario.getLsaPk().toString());
            return RestClient.invokePut(webTarget, leySalario, SgLeySalario.class, userToken);
        }
    }

    public SgLeySalario obtenerPorId(Long leySalarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (leySalarioPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/leysalario");
        webTarget = webTarget.path(leySalarioPk.toString());
        return RestClient.invokeGet(webTarget, SgLeySalario.class, userToken);
    }

    public void eliminar(Long leySalarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (leySalarioPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/leysalario");
        webTarget = webTarget.path(leySalarioPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgLeySalario> obtenerHistorialPorId(Long leySalarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (leySalarioPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/leysalario/historial");
        webTarget = webTarget.path(leySalarioPk.toString());
        SgLeySalario[] leySalario = RestClient.invokeGet(webTarget, SgLeySalario[].class, userToken);
        return Arrays.asList(leySalario);
    }
    

}
