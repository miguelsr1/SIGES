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
import sv.gob.mined.siges.web.dto.SgModalidadEstudio;
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
public class ModalidadEstudioRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ModalidadEstudioRestClient() {
    }


    public List<SgModalidadEstudio> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesestudio/buscar");
        SgModalidadEstudio[] modalidadEstudio = RestClient.invokePost(webTarget, filtro, SgModalidadEstudio[].class, userToken);
        return Arrays.asList(modalidadEstudio);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesestudio/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgModalidadEstudio guardar(SgModalidadEstudio modalidadEstudio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadEstudio == null || userToken == null) {
            return null;
        }
        if (modalidadEstudio.getMesPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesestudio");
            return RestClient.invokePost(webTarget, modalidadEstudio, SgModalidadEstudio.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesestudio");
            webTarget = webTarget.path(modalidadEstudio.getMesPk().toString());
            return RestClient.invokePut(webTarget, modalidadEstudio, SgModalidadEstudio.class, userToken);
        }
    }

    public SgModalidadEstudio obtenerPorId(Long modalidadEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadEstudioPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesestudio");
        webTarget = webTarget.path(modalidadEstudioPk.toString());
        return RestClient.invokeGet(webTarget, SgModalidadEstudio.class, userToken);
    }

    public void eliminar(Long modalidadEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadEstudioPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesestudio");
        webTarget = webTarget.path(modalidadEstudioPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgModalidadEstudio> obtenerHistorialPorId(Long modalidadEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadEstudioPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesestudio/historial");
        webTarget = webTarget.path(modalidadEstudioPk.toString());
        SgModalidadEstudio[] modalidadEstudio = RestClient.invokeGet(webTarget, SgModalidadEstudio[].class, userToken);
        return Arrays.asList(modalidadEstudio);
    }
    

}
