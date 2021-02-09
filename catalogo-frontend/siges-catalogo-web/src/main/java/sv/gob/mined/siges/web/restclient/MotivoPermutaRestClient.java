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
import sv.gob.mined.siges.web.dto.SgMotivoPermuta;
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
public class MotivoPermutaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MotivoPermutaRestClient() {
    }


    public List<SgMotivoPermuta> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivospermuta/buscar");
        SgMotivoPermuta[] motivoPermuta = RestClient.invokePost(webTarget, filtro, SgMotivoPermuta[].class, userToken);
        return Arrays.asList(motivoPermuta);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivospermuta/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMotivoPermuta guardar(SgMotivoPermuta motivoPermuta) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoPermuta == null || userToken == null) {
            return null;
        }
        if (motivoPermuta.getMpePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivospermuta");
            return RestClient.invokePost(webTarget, motivoPermuta, SgMotivoPermuta.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivospermuta");
            webTarget = webTarget.path(motivoPermuta.getMpePk().toString());
            return RestClient.invokePut(webTarget, motivoPermuta, SgMotivoPermuta.class, userToken);
        }
    }

    public SgMotivoPermuta obtenerPorId(Long motivoPermutaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoPermutaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivospermuta");
        webTarget = webTarget.path(motivoPermutaPk.toString());
        return RestClient.invokeGet(webTarget, SgMotivoPermuta.class, userToken);
    }

    public void eliminar(Long motivoPermutaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoPermutaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivospermuta");
        webTarget = webTarget.path(motivoPermutaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgMotivoPermuta> obtenerHistorialPorId(Long motivoPermutaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoPermutaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivospermuta/historial");
        webTarget = webTarget.path(motivoPermutaPk.toString());
        SgMotivoPermuta[] motivoPermuta = RestClient.invokeGet(webTarget, SgMotivoPermuta[].class, userToken);
        return Arrays.asList(motivoPermuta);
    }
    

}
