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
import sv.gob.mined.siges.web.dto.catalogo.SgAyuda;
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
@Timeout(value = 10000L)
public class AyudaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public AyudaRestClient() {
    }

    // clase de ejemplo que se comunica con catalogo-backend. Para hacer una que se comunique con seguridad-backend, hay que cambiar la ConstantesServiciosRest.

    public List<SgAyuda> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
//        if (filtro == null || userToken == null) {
//            new ArrayList<>();
//        }
//        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas/buscar");
//        SgAyuda[] ayudas = RestClient.invokePost(webTarget, filtro, SgAyuda[].class, userToken);
//        return Arrays.asList(ayudas);
return new ArrayList<>();
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
//        if (filtro == null || userToken == null) {
//            return 0L;
//        }
//        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas/buscar/total");
//        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
return 0L;
    }

    public SgAyuda guardar(SgAyuda ayuda) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ayuda == null || userToken == null) {
            return null;
        }
        if (ayuda.getAyuPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas");
            return RestClient.invokePost(webTarget, ayuda, SgAyuda.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas");
            webTarget = webTarget.path(ayuda.getAyuPk().toString());
            return RestClient.invokePut(webTarget, ayuda, SgAyuda.class, userToken);
        }
    }

    public SgAyuda obtenerPorId(Long ayudaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ayudaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas");
        webTarget = webTarget.path(ayudaPk.toString());
        return RestClient.invokeGet(webTarget, SgAyuda.class, userToken);
    }

    public void eliminar(Long ayudaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ayudaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas");
        webTarget = webTarget.path(ayudaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAyuda> obtenerHistorialPorId(Long ayudaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ayudaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas/historial");
        webTarget = webTarget.path(ayudaPk.toString());
        SgAyuda[] ayudas = RestClient.invokeGet(webTarget, SgAyuda[].class, userToken);
        return Arrays.asList(ayudas);
    }
    

}
