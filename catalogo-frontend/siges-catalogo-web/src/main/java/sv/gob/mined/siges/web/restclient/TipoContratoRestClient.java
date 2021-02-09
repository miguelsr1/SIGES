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
import sv.gob.mined.siges.web.dto.SgTipoContrato;
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
public class TipoContratoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoContratoRestClient() {
    }


    public List<SgTipoContrato> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipocontrato/buscar");
        SgTipoContrato[] tipoContrato = RestClient.invokePost(webTarget, filtro, SgTipoContrato[].class, userToken);
        return Arrays.asList(tipoContrato);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipocontrato/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoContrato guardar(SgTipoContrato tipoContrato) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoContrato == null || userToken == null) {
            return null;
        }
        if (tipoContrato.getTcoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipocontrato");
            return RestClient.invokePost(webTarget, tipoContrato, SgTipoContrato.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipocontrato");
            webTarget = webTarget.path(tipoContrato.getTcoPk().toString());
            return RestClient.invokePut(webTarget, tipoContrato, SgTipoContrato.class, userToken);
        }
    }

    public SgTipoContrato obtenerPorId(Long tipoContratoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoContratoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipocontrato");
        webTarget = webTarget.path(tipoContratoPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoContrato.class, userToken);
    }

    public void eliminar(Long tipoContratoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoContratoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipocontrato");
        webTarget = webTarget.path(tipoContratoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoContrato> obtenerHistorialPorId(Long tipoContratoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoContratoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipocontrato/historial");
        webTarget = webTarget.path(tipoContratoPk.toString());
        SgTipoContrato[] tipoContrato = RestClient.invokeGet(webTarget, SgTipoContrato[].class, userToken);
        return Arrays.asList(tipoContrato);
    }
    

}