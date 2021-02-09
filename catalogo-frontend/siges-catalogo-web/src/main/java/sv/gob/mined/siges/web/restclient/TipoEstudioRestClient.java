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
import sv.gob.mined.siges.web.dto.SgTipoEstudio;
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
public class TipoEstudioRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoEstudioRestClient() {
    }


    public List<SgTipoEstudio> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposestudio/buscar");
        SgTipoEstudio[] tipoEstudio = RestClient.invokePost(webTarget, filtro, SgTipoEstudio[].class, userToken);
        return Arrays.asList(tipoEstudio);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposestudio/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoEstudio guardar(SgTipoEstudio tipoEstudio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoEstudio == null || userToken == null) {
            return null;
        }
        if (tipoEstudio.getTesPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposestudio");
            return RestClient.invokePost(webTarget, tipoEstudio, SgTipoEstudio.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposestudio");
            webTarget = webTarget.path(tipoEstudio.getTesPk().toString());
            return RestClient.invokePut(webTarget, tipoEstudio, SgTipoEstudio.class, userToken);
        }
    }

    public SgTipoEstudio obtenerPorId(Long tipoEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoEstudioPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposestudio");
        webTarget = webTarget.path(tipoEstudioPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoEstudio.class, userToken);
    }

    public void eliminar(Long tipoEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoEstudioPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposestudio");
        webTarget = webTarget.path(tipoEstudioPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoEstudio> obtenerHistorialPorId(Long tipoEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoEstudioPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposestudio/historial");
        webTarget = webTarget.path(tipoEstudioPk.toString());
        SgTipoEstudio[] tipoEstudio = RestClient.invokeGet(webTarget, SgTipoEstudio[].class, userToken);
        return Arrays.asList(tipoEstudio);
    }
    

}
