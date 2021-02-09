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
import sv.gob.mined.siges.web.dto.SgEstadoCivil;
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
public class EstadoCivilRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EstadoCivilRestClient() {
    }


    public List<SgEstadoCivil> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoscivil/buscar");
        SgEstadoCivil[] estadosCivil = RestClient.invokePost(webTarget, filtro, SgEstadoCivil[].class, userToken);
        return Arrays.asList(estadosCivil);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoscivil/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEstadoCivil guardar(SgEstadoCivil estadoCivil) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoCivil == null || userToken == null) {
            return null;
        }
        if (estadoCivil.getEciPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoscivil");
            return RestClient.invokePost(webTarget, estadoCivil, SgEstadoCivil.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoscivil");
            webTarget = webTarget.path(estadoCivil.getEciPk().toString());
            return RestClient.invokePut(webTarget, estadoCivil, SgEstadoCivil.class, userToken);
        }
    }

    public SgEstadoCivil obtenerPorId(Long estadoCivilPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoCivilPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoscivil");
        webTarget = webTarget.path(estadoCivilPk.toString());
        return RestClient.invokeGet(webTarget, SgEstadoCivil.class, userToken);
    }

    public void eliminar(Long estadoCivilPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoCivilPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoscivil");
        webTarget = webTarget.path(estadoCivilPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgEstadoCivil> obtenerHistorialPorId(Long estadoCivilPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoCivilPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoscivil/historial");
        webTarget = webTarget.path(estadoCivilPk.toString());
        SgEstadoCivil[] estadosCivil = RestClient.invokeGet(webTarget, SgEstadoCivil[].class, userToken);
        return Arrays.asList(estadosCivil);
    }
    

}
