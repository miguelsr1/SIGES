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
import sv.gob.mined.siges.web.dto.SgDiscapacidad;
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
public class DiscapacidadRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public DiscapacidadRestClient() {
    }


    public List<SgDiscapacidad> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/discapacidades/buscar");
        SgDiscapacidad[] discapacidades = RestClient.invokePost(webTarget, filtro, SgDiscapacidad[].class, userToken);
        return Arrays.asList(discapacidades);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/discapacidades/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgDiscapacidad guardar(SgDiscapacidad discapacidad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (discapacidad == null || userToken == null) {
            return null;
        }
        if (discapacidad.getDisPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/discapacidades");
            return RestClient.invokePost(webTarget, discapacidad, SgDiscapacidad.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/discapacidades");
            webTarget = webTarget.path(discapacidad.getDisPk().toString());
            return RestClient.invokePut(webTarget, discapacidad, SgDiscapacidad.class, userToken);
        }
    }

    public SgDiscapacidad obtenerPorId(Long discapacidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (discapacidadPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/discapacidades");
        webTarget = webTarget.path(discapacidadPk.toString());
        return RestClient.invokeGet(webTarget, SgDiscapacidad.class, userToken);
    }

    public void eliminar(Long discapacidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (discapacidadPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/discapacidades");
        webTarget = webTarget.path(discapacidadPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgDiscapacidad> obtenerHistorialPorId(Long discapacidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (discapacidadPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/discapacidades/historial");
        webTarget = webTarget.path(discapacidadPk.toString());
        SgDiscapacidad[] discapacidades = RestClient.invokeGet(webTarget, SgDiscapacidad[].class, userToken);
        return Arrays.asList(discapacidades);
    }
    

}
