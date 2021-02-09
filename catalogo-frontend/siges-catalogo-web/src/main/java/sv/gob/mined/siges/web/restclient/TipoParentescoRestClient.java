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
import sv.gob.mined.siges.web.dto.SgTipoParentesco;
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
public class TipoParentescoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoParentescoRestClient() {
    }


    public List<SgTipoParentesco> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposparentesco/buscar");
        SgTipoParentesco[] tiposParentesco = RestClient.invokePost(webTarget, filtro, SgTipoParentesco[].class, userToken);
        return Arrays.asList(tiposParentesco);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposparentesco/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoParentesco guardar(SgTipoParentesco tipoParentesco) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoParentesco == null || userToken == null) {
            return null;
        }
        if (tipoParentesco.getTpaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposparentesco");
            return RestClient.invokePost(webTarget, tipoParentesco, SgTipoParentesco.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposparentesco");
            webTarget = webTarget.path(tipoParentesco.getTpaPk().toString());
            return RestClient.invokePut(webTarget, tipoParentesco, SgTipoParentesco.class, userToken);
        }
    }

    public SgTipoParentesco obtenerPorId(Long tipoParentescoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoParentescoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposparentesco");
        webTarget = webTarget.path(tipoParentescoPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoParentesco.class, userToken);
    }

    public void eliminar(Long tipoParentescoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoParentescoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposparentesco");
        webTarget = webTarget.path(tipoParentescoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoParentesco> obtenerHistorialPorId(Long tipoParentescoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoParentescoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposparentesco/historial");
        webTarget = webTarget.path(tipoParentescoPk.toString());
        SgTipoParentesco[] tiposParentesco = RestClient.invokeGet(webTarget, SgTipoParentesco[].class, userToken);
        return Arrays.asList(tiposParentesco);
    }
    

}
