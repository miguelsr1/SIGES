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
import sv.gob.mined.siges.web.dto.SgModalidadAtencion;
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
public class ModalidadAtencionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ModalidadAtencionRestClient() {
    }


    public List<SgModalidadAtencion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion/buscar");
        SgModalidadAtencion[] modalidadAtencion = RestClient.invokePost(webTarget, filtro, SgModalidadAtencion[].class, userToken);
        return Arrays.asList(modalidadAtencion);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgModalidadAtencion guardar(SgModalidadAtencion modalidadAtencion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadAtencion == null || userToken == null) {
            return null;
        }
        if (modalidadAtencion.getMatPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion");
            return RestClient.invokePost(webTarget, modalidadAtencion, SgModalidadAtencion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion");
            webTarget = webTarget.path(modalidadAtencion.getMatPk().toString());
            return RestClient.invokePut(webTarget, modalidadAtencion, SgModalidadAtencion.class, userToken);
        }
    }

    public SgModalidadAtencion obtenerPorId(Long modalidadAtencionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadAtencionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion");
        webTarget = webTarget.path(modalidadAtencionPk.toString());
        return RestClient.invokeGet(webTarget, SgModalidadAtencion.class, userToken);
    }

    public void eliminar(Long modalidadAtencionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadAtencionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion");
        webTarget = webTarget.path(modalidadAtencionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgModalidadAtencion> obtenerHistorialPorId(Long modalidadAtencionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadAtencionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion/historial");
        webTarget = webTarget.path(modalidadAtencionPk.toString());
        SgModalidadAtencion[] modalidadAtencion = RestClient.invokeGet(webTarget, SgModalidadAtencion[].class, userToken);
        return Arrays.asList(modalidadAtencion);
    }
    

}
