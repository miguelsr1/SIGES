/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

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
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgOpcionesDescargo;
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
public class OpcionesDescargoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public OpcionesDescargoRestClient() {
    }


    public List<SgOpcionesDescargo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/opcionesdescargo/buscar");
        SgOpcionesDescargo[] opsdescargo = RestClient.invokePost(webTarget, filtro, SgOpcionesDescargo[].class, userToken);
        return Arrays.asList(opsdescargo);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/opcionesdescargo/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgOpcionesDescargo guardar(SgOpcionesDescargo opsDescargo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opsDescargo == null || userToken == null) {
            return null;
        }
        if (opsDescargo.getOdePk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/opcionesdescargo");
            return RestClient.invokePost(webTarget, opsDescargo, SgOpcionesDescargo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/opcionesdescargo");
            webTarget = webTarget.path(opsDescargo.getOdePk().toString());
            return RestClient.invokePut(webTarget, opsDescargo, SgOpcionesDescargo.class, userToken);
        }
    }

    public SgOpcionesDescargo obtenerPorId(Long opsPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opsPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/opcionesdescargo");
        webTarget = webTarget.path(opsPk.toString());
        return RestClient.invokeGet(webTarget, SgOpcionesDescargo.class, userToken);
    }

    public void eliminar(Long opsPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opsPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/opcionesdescargo");
        webTarget = webTarget.path(opsPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgOpcionesDescargo> obtenerHistorialPorId(Long estadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/opcionesdescargo/historial");
        webTarget = webTarget.path(estadoPk.toString());
        SgOpcionesDescargo[] opsDescargo = RestClient.invokeGet(webTarget, SgOpcionesDescargo[].class, userToken);
        return Arrays.asList(opsDescargo);
    }
    

}

