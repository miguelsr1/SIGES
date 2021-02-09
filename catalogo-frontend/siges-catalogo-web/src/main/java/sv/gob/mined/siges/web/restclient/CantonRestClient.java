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
import sv.gob.mined.siges.web.dto.SgCanton;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCanton;

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
public class CantonRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CantonRestClient() {
    }


    public List<SgCanton> buscar(FiltroCanton filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones/buscar");
        SgCanton[] cantones = RestClient.invokePost(webTarget, filtro, SgCanton[].class, userToken);
        return Arrays.asList(cantones);
    }

    public Long buscarTotal(FiltroCanton filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCanton guardar(SgCanton canton) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (canton == null || userToken == null) {
            return null;
        }
        if (canton.getCanPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones");
            return RestClient.invokePost(webTarget, canton, SgCanton.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones");
            webTarget = webTarget.path(canton.getCanPk().toString());
            return RestClient.invokePut(webTarget, canton, SgCanton.class, userToken);
        }
    }

    public SgCanton obtenerPorId(Long cantonPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cantonPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones");
        webTarget = webTarget.path(cantonPk.toString());
        return RestClient.invokeGet(webTarget, SgCanton.class, userToken);
    }

    public void eliminar(Long cantonPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cantonPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones");
        webTarget = webTarget.path(cantonPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgCanton> obtenerHistorialPorId(Long cantonPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cantonPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones/historial");
        webTarget = webTarget.path(cantonPk.toString());
        SgCanton[] cantones = RestClient.invokeGet(webTarget, SgCanton[].class, userToken);
        return Arrays.asList(cantones);
    }
    

}
