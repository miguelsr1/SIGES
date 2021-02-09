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
import sv.gob.mined.siges.web.dto.SgAreaAsignaturaModulo;
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
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 3000L)
public class AreaAsignaturaModuloRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public AreaAsignaturaModuloRestClient() {
    }


    public List<SgAreaAsignaturaModulo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasasignaturamodulo/buscar");
        SgAreaAsignaturaModulo[] areasAsignaturaModulo = RestClient.invokePost(webTarget, filtro, SgAreaAsignaturaModulo[].class, userToken);
        return Arrays.asList(areasAsignaturaModulo);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasasignaturamodulo/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAreaAsignaturaModulo guardar(SgAreaAsignaturaModulo areaAsignaturaModulo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (areaAsignaturaModulo == null || userToken == null) {
            return null;
        }
        if (areaAsignaturaModulo.getAamPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasasignaturamodulo");
            return RestClient.invokePost(webTarget, areaAsignaturaModulo, SgAreaAsignaturaModulo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasasignaturamodulo");
            webTarget = webTarget.path(areaAsignaturaModulo.getAamPk().toString());
            return RestClient.invokePut(webTarget, areaAsignaturaModulo, SgAreaAsignaturaModulo.class, userToken);
        }
    }

    public SgAreaAsignaturaModulo obtenerPorId(Long areaAsignaturaModuloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (areaAsignaturaModuloPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasasignaturamodulo");
        webTarget = webTarget.path(areaAsignaturaModuloPk.toString());
        return RestClient.invokeGet(webTarget, SgAreaAsignaturaModulo.class, userToken);
    }

    public void eliminar(Long areaAsignaturaModuloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (areaAsignaturaModuloPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasasignaturamodulo");
        webTarget = webTarget.path(areaAsignaturaModuloPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAreaAsignaturaModulo> obtenerHistorialPorId(Long areaAsignaturaModuloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (areaAsignaturaModuloPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasasignaturamodulo/historial");
        webTarget = webTarget.path(areaAsignaturaModuloPk.toString());
        SgAreaAsignaturaModulo[] areasAsignaturaModulo = RestClient.invokeGet(webTarget, SgAreaAsignaturaModulo[].class, userToken);
        return Arrays.asList(areasAsignaturaModulo);
    }
    

}
