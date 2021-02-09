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
import sv.gob.mined.siges.web.dto.SgEscolaridad;
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
public class EscolaridadRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EscolaridadRestClient() {
    }


    public List<SgEscolaridad> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escolaridades/buscar");
        SgEscolaridad[] escolaridades = RestClient.invokePost(webTarget, filtro, SgEscolaridad[].class, userToken);
        return Arrays.asList(escolaridades);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escolaridades/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEscolaridad guardar(SgEscolaridad escolaridad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escolaridad == null || userToken == null) {
            return null;
        }
        if (escolaridad.getEscPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escolaridades");
            return RestClient.invokePost(webTarget, escolaridad, SgEscolaridad.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escolaridades");
            webTarget = webTarget.path(escolaridad.getEscPk().toString());
            return RestClient.invokePut(webTarget, escolaridad, SgEscolaridad.class, userToken);
        }
    }

    public SgEscolaridad obtenerPorId(Long escolaridadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escolaridadPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escolaridades");
        webTarget = webTarget.path(escolaridadPk.toString());
        return RestClient.invokeGet(webTarget, SgEscolaridad.class, userToken);
    }

    public void eliminar(Long escolaridadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escolaridadPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escolaridades");
        webTarget = webTarget.path(escolaridadPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgEscolaridad> obtenerHistorialPorId(Long escolaridadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escolaridadPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escolaridades/historial");
        webTarget = webTarget.path(escolaridadPk.toString());
        SgEscolaridad[] escolaridades = RestClient.invokeGet(webTarget, SgEscolaridad[].class, userToken);
        return Arrays.asList(escolaridades);
    }
    

}
