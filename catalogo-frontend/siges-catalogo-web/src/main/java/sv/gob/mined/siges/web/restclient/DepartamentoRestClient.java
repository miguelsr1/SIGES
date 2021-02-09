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
import sv.gob.mined.siges.web.dto.SgDepartamento;
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
public class DepartamentoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public DepartamentoRestClient() {
    }


    public List<SgDepartamento> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos/buscar");
        SgDepartamento[] departamentos = RestClient.invokePost(webTarget, filtro, SgDepartamento[].class, userToken);
        return Arrays.asList(departamentos);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgDepartamento guardar(SgDepartamento departamento) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (departamento == null || userToken == null) {
            return null;
        }
        if (departamento.getDepPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos");
            return RestClient.invokePost(webTarget, departamento, SgDepartamento.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos");
            webTarget = webTarget.path(departamento.getDepPk().toString());
            return RestClient.invokePut(webTarget, departamento, SgDepartamento.class, userToken);
        }
    }

    public SgDepartamento obtenerPorId(Long departamentoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (departamentoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos");
        webTarget = webTarget.path(departamentoPk.toString());
        return RestClient.invokeGet(webTarget, SgDepartamento.class, userToken);
    }

    public void eliminar(Long departamentoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (departamentoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos");
        webTarget = webTarget.path(departamentoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgDepartamento> obtenerHistorialPorId(Long departamentoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (departamentoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos/historial");
        webTarget = webTarget.path(departamentoPk.toString());
        SgDepartamento[] departamentos = RestClient.invokeGet(webTarget, SgDepartamento[].class, userToken);
        return Arrays.asList(departamentos);
    }    
    

}
