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
import sv.gob.mined.siges.web.dto.SgTipoInstitucionPaga;
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
public class TipoInstitucionPagaSalarioRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoInstitucionPagaSalarioRestClient() {
    }


    public List<SgTipoInstitucionPaga> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoinstitucionpago/buscar");
        SgTipoInstitucionPaga[] tipoInstitucionPaga = RestClient.invokePost(webTarget, filtro, SgTipoInstitucionPaga[].class, userToken);
        return Arrays.asList(tipoInstitucionPaga);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoinstitucionpago/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoInstitucionPaga guardar(SgTipoInstitucionPaga tipoInstitucionPaga) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoInstitucionPaga == null || userToken == null) {
            return null;
        }
        if (tipoInstitucionPaga.getTipPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoinstitucionpago");
            return RestClient.invokePost(webTarget, tipoInstitucionPaga, SgTipoInstitucionPaga.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoinstitucionpago");
            webTarget = webTarget.path(tipoInstitucionPaga.getTipPk().toString());
            return RestClient.invokePut(webTarget, tipoInstitucionPaga, SgTipoInstitucionPaga.class, userToken);
        }
    }

    public SgTipoInstitucionPaga obtenerPorId(Long tipoInstitucionPagaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoInstitucionPagaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoinstitucionpago");
        webTarget = webTarget.path(tipoInstitucionPagaPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoInstitucionPaga.class, userToken);
    }

    public void eliminar(Long tipoInstitucionPagaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoInstitucionPagaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoinstitucionpago");
        webTarget = webTarget.path(tipoInstitucionPagaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoInstitucionPaga> obtenerHistorialPorId(Long tipoInstitucionPagaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoInstitucionPagaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoinstitucionpago/historial");
        webTarget = webTarget.path(tipoInstitucionPagaPk.toString());
        SgTipoInstitucionPaga[] tipoInstitucionPaga = RestClient.invokeGet(webTarget, SgTipoInstitucionPaga[].class, userToken);
        return Arrays.asList(tipoInstitucionPaga);
    }
    

}
