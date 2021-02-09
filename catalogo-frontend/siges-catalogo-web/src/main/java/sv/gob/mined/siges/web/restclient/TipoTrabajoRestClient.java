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
import sv.gob.mined.siges.web.dto.SgTipoTrabajo;
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
public class TipoTrabajoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoTrabajoRestClient() {
    }


    public List<SgTipoTrabajo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostrabajo/buscar");
        SgTipoTrabajo[] tiposTrabajo = RestClient.invokePost(webTarget, filtro, SgTipoTrabajo[].class, userToken);
        return Arrays.asList(tiposTrabajo);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostrabajo/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoTrabajo guardar(SgTipoTrabajo tipoTrabajo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoTrabajo == null || userToken == null) {
            return null;
        }
        if (tipoTrabajo.getTtrPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostrabajo");
            return RestClient.invokePost(webTarget, tipoTrabajo, SgTipoTrabajo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostrabajo");
            webTarget = webTarget.path(tipoTrabajo.getTtrPk().toString());
            return RestClient.invokePut(webTarget, tipoTrabajo, SgTipoTrabajo.class, userToken);
        }
    }

    public SgTipoTrabajo obtenerPorId(Long tipoTrabajoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoTrabajoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostrabajo");
        webTarget = webTarget.path(tipoTrabajoPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoTrabajo.class, userToken);
    }

    public void eliminar(Long tipoTrabajoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoTrabajoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostrabajo");
        webTarget = webTarget.path(tipoTrabajoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoTrabajo> obtenerHistorialPorId(Long tipoTrabajoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoTrabajoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostrabajo/historial");
        webTarget = webTarget.path(tipoTrabajoPk.toString());
        SgTipoTrabajo[] tiposTrabajo = RestClient.invokeGet(webTarget, SgTipoTrabajo[].class, userToken);
        return Arrays.asList(tiposTrabajo);
    }
    

}
