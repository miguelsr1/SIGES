/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.rest;

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
import sv.gob.mined.siges.web.dto.SgNacionalidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.restclient.RestClient;
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
public class NacionalidadRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public NacionalidadRestClient() {
    }


    public List<SgNacionalidad> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nacionalidades/buscar");
        SgNacionalidad[] nacionalidades = RestClient.invokePost(webTarget, filtro, SgNacionalidad[].class, userToken);
        return Arrays.asList(nacionalidades);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nacionalidades/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgNacionalidad guardar(SgNacionalidad nacionalidad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nacionalidad == null || userToken == null) {
            return null;
        }
        if (nacionalidad.getNacPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nacionalidades");
            return RestClient.invokePost(webTarget, nacionalidad, SgNacionalidad.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nacionalidades");
            webTarget = webTarget.path(nacionalidad.getNacPk().toString());
            return RestClient.invokePut(webTarget, nacionalidad, SgNacionalidad.class, userToken);
        }
    }

    public SgNacionalidad obtenerPorId(Long nacionalidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nacionalidadPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nacionalidades");
        webTarget = webTarget.path(nacionalidadPk.toString());
        return RestClient.invokeGet(webTarget, SgNacionalidad.class, userToken);
    }

    public void eliminar(Long nacionalidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nacionalidadPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nacionalidades");
        webTarget = webTarget.path(nacionalidadPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgNacionalidad> obtenerHistorialPorId(Long nacionalidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nacionalidadPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nacionalidades/historial");
        webTarget = webTarget.path(nacionalidadPk.toString());
        SgNacionalidad[] nacionalidades = RestClient.invokeGet(webTarget, SgNacionalidad[].class, userToken);
        return Arrays.asList(nacionalidades);
    }
    

}
