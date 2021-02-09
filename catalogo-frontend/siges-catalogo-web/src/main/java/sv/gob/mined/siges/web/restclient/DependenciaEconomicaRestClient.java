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
import sv.gob.mined.siges.web.dto.SgDependenciaEconomica;
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
public class DependenciaEconomicaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public DependenciaEconomicaRestClient() {
    }


    public List<SgDependenciaEconomica> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/dependenciaseconomica/buscar");
        SgDependenciaEconomica[] dependenciasEconomica = RestClient.invokePost(webTarget, filtro, SgDependenciaEconomica[].class, userToken);
        return Arrays.asList(dependenciasEconomica);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/dependenciaseconomica/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgDependenciaEconomica guardar(SgDependenciaEconomica dependenciaEconomica) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (dependenciaEconomica == null || userToken == null) {
            return null;
        }
        if (dependenciaEconomica.getDecPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/dependenciaseconomica");
            return RestClient.invokePost(webTarget, dependenciaEconomica, SgDependenciaEconomica.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/dependenciaseconomica");
            webTarget = webTarget.path(dependenciaEconomica.getDecPk().toString());
            return RestClient.invokePut(webTarget, dependenciaEconomica, SgDependenciaEconomica.class, userToken);
        }
    }

    public SgDependenciaEconomica obtenerPorId(Long dependenciaEconomicaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (dependenciaEconomicaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/dependenciaseconomica");
        webTarget = webTarget.path(dependenciaEconomicaPk.toString());
        return RestClient.invokeGet(webTarget, SgDependenciaEconomica.class, userToken);
    }

    public void eliminar(Long dependenciaEconomicaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (dependenciaEconomicaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/dependenciaseconomica");
        webTarget = webTarget.path(dependenciaEconomicaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgDependenciaEconomica> obtenerHistorialPorId(Long dependenciaEconomicaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (dependenciaEconomicaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/dependenciaseconomica/historial");
        webTarget = webTarget.path(dependenciaEconomicaPk.toString());
        SgDependenciaEconomica[] dependenciasEconomica = RestClient.invokeGet(webTarget, SgDependenciaEconomica[].class, userToken);
        return Arrays.asList(dependenciasEconomica);
    }
    

}
