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
import sv.gob.mined.siges.web.dto.SgTipoNombramiento;
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
public class TipoNombramientoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoNombramientoRestClient() {
    }


    public List<SgTipoNombramiento> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiponombramiento/buscar");
        SgTipoNombramiento[] tipoContrato = RestClient.invokePost(webTarget, filtro, SgTipoNombramiento[].class, userToken);
        return Arrays.asList(tipoContrato);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiponombramiento/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoNombramiento guardar(SgTipoNombramiento tipoContrato) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoContrato == null || userToken == null) {
            return null;
        }
        if (tipoContrato.getTnoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiponombramiento");
            return RestClient.invokePost(webTarget, tipoContrato, SgTipoNombramiento.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiponombramiento");
            webTarget = webTarget.path(tipoContrato.getTnoPk().toString());
            return RestClient.invokePut(webTarget, tipoContrato, SgTipoNombramiento.class, userToken);
        }
    }

    public SgTipoNombramiento obtenerPorId(Long tipoContratoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoContratoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiponombramiento");
        webTarget = webTarget.path(tipoContratoPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoNombramiento.class, userToken);
    }

    public void eliminar(Long tipoContratoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoContratoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiponombramiento");
        webTarget = webTarget.path(tipoContratoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoNombramiento> obtenerHistorialPorId(Long tipoContratoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoContratoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiponombramiento/historial");
        webTarget = webTarget.path(tipoContratoPk.toString());
        SgTipoNombramiento[] tipoContrato = RestClient.invokeGet(webTarget, SgTipoNombramiento[].class, userToken);
        return Arrays.asList(tipoContrato);
    }
    

}
