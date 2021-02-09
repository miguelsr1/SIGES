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
import sv.gob.mined.siges.web.dto.SgTipoRepresentante;
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
public class TipoRepresentanteRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoRepresentanteRestClient() {
    }


    public List<SgTipoRepresentante> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporepresentante/buscar");
        SgTipoRepresentante[] tipoRepresentante = RestClient.invokePost(webTarget, filtro, SgTipoRepresentante[].class, userToken);
        return Arrays.asList(tipoRepresentante);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporepresentante/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoRepresentante guardar(SgTipoRepresentante tipoRepresentante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoRepresentante == null || userToken == null) {
            return null;
        }
        if (tipoRepresentante.getTrePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporepresentante");
            return RestClient.invokePost(webTarget, tipoRepresentante, SgTipoRepresentante.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporepresentante");
            webTarget = webTarget.path(tipoRepresentante.getTrePk().toString());
            return RestClient.invokePut(webTarget, tipoRepresentante, SgTipoRepresentante.class, userToken);
        }
    }

    public SgTipoRepresentante obtenerPorId(Long tipoRepresentantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoRepresentantePk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporepresentante");
        webTarget = webTarget.path(tipoRepresentantePk.toString());
        return RestClient.invokeGet(webTarget, SgTipoRepresentante.class, userToken);
    }

    public void eliminar(Long tipoRepresentantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoRepresentantePk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporepresentante");
        webTarget = webTarget.path(tipoRepresentantePk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoRepresentante> obtenerHistorialPorId(Long tipoRepresentantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoRepresentantePk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporepresentante/historial");
        webTarget = webTarget.path(tipoRepresentantePk.toString());
        SgTipoRepresentante[] tipoRepresentante = RestClient.invokeGet(webTarget, SgTipoRepresentante[].class, userToken);
        return Arrays.asList(tipoRepresentante);
    }
    

}
