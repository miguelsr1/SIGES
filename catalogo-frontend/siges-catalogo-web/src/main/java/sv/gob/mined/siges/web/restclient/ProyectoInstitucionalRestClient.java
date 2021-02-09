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
import sv.gob.mined.siges.web.dto.SgProyectoInstitucional;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyectoInstitucional;

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
public class ProyectoInstitucionalRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ProyectoInstitucionalRestClient() {
    }


    public List<SgProyectoInstitucional> buscar(FiltroProyectoInstitucional filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosinstitucionales/buscar");
        SgProyectoInstitucional[] proyectoInstitucional = RestClient.invokePost(webTarget, filtro, SgProyectoInstitucional[].class, userToken);
        return Arrays.asList(proyectoInstitucional);
    }

    public Long buscarTotal(FiltroProyectoInstitucional filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosinstitucionales/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgProyectoInstitucional guardar(SgProyectoInstitucional proyectoInstitucional) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstitucional == null || userToken == null) {
            return null;
        }
        if (proyectoInstitucional.getPinPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosinstitucionales");
            return RestClient.invokePost(webTarget, proyectoInstitucional, SgProyectoInstitucional.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosinstitucionales");
            webTarget = webTarget.path(proyectoInstitucional.getPinPk().toString());
            return RestClient.invokePut(webTarget, proyectoInstitucional, SgProyectoInstitucional.class, userToken);
        }
    }

    public SgProyectoInstitucional obtenerPorId(Long proyectoInstitucionalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstitucionalPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosinstitucionales");
        webTarget = webTarget.path(proyectoInstitucionalPk.toString());
        return RestClient.invokeGet(webTarget, SgProyectoInstitucional.class, userToken);
    }

    public void eliminar(Long proyectoInstitucionalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstitucionalPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosinstitucionales");
        webTarget = webTarget.path(proyectoInstitucionalPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgProyectoInstitucional> obtenerHistorialPorId(Long proyectoInstitucionalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstitucionalPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosinstitucionales/historial");
        webTarget = webTarget.path(proyectoInstitucionalPk.toString());
        SgProyectoInstitucional[] proyectoInstitucional = RestClient.invokeGet(webTarget, SgProyectoInstitucional[].class, userToken);
        return Arrays.asList(proyectoInstitucional);
    }
    

}
