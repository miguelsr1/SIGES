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
import sv.gob.mined.siges.web.dto.SgInstitucion;
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
public class InstitucionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InstitucionRestClient() {
    }


    public List<SgInstitucion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/instituciones/buscar");
        SgInstitucion[] instituciones = RestClient.invokePost(webTarget, filtro, SgInstitucion[].class, userToken);
        return Arrays.asList(instituciones);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/instituciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInstitucion guardar(SgInstitucion institucion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (institucion == null || userToken == null) {
            return null;
        }
        if (institucion.getInsPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/instituciones");
            return RestClient.invokePost(webTarget, institucion, SgInstitucion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/instituciones");
            webTarget = webTarget.path(institucion.getInsPk().toString());
            return RestClient.invokePut(webTarget, institucion, SgInstitucion.class, userToken);
        }
    }

    public SgInstitucion obtenerPorId(Long institucionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (institucionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/instituciones");
        webTarget = webTarget.path(institucionPk.toString());
        return RestClient.invokeGet(webTarget, SgInstitucion.class, userToken);
    }

    public void eliminar(Long institucionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (institucionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/instituciones");
        webTarget = webTarget.path(institucionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgInstitucion> obtenerHistorialPorId(Long institucionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (institucionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/instituciones/historial");
        webTarget = webTarget.path(institucionPk.toString());
        SgInstitucion[] instituciones = RestClient.invokeGet(webTarget, SgInstitucion[].class, userToken);
        return Arrays.asList(instituciones);
    }
    

}
