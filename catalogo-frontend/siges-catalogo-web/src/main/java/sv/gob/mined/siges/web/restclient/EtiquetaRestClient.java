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
import sv.gob.mined.siges.web.dto.SgEtiqueta;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEtiqueta;

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
public class EtiquetaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EtiquetaRestClient() {
    }


    public List<SgEtiqueta> buscar(FiltroEtiqueta filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etiquetas/buscar");
        SgEtiqueta[] etiquetas = RestClient.invokePost(webTarget, filtro, SgEtiqueta[].class, userToken);
        return Arrays.asList(etiquetas);
    }

    public Long buscarTotal(FiltroEtiqueta filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etiquetas/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEtiqueta guardar(SgEtiqueta etiqueta) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (etiqueta == null || userToken == null) {
            return null;
        }
        if (etiqueta.getEtiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etiquetas");
            return RestClient.invokePost(webTarget, etiqueta, SgEtiqueta.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etiquetas");
            webTarget = webTarget.path(etiqueta.getEtiPk().toString());
            return RestClient.invokePut(webTarget, etiqueta, SgEtiqueta.class, userToken);
        }
    }

    public SgEtiqueta obtenerPorId(Long etiquetaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (etiquetaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etiquetas");
        webTarget = webTarget.path(etiquetaPk.toString());
        return RestClient.invokeGet(webTarget, SgEtiqueta.class, userToken);
    }

    public void eliminar(Long etiquetaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (etiquetaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etiquetas");
        webTarget = webTarget.path(etiquetaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgEtiqueta> obtenerHistorialPorId(Long etiquetaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (etiquetaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etiquetas/historial");
        webTarget = webTarget.path(etiquetaPk.toString());
        SgEtiqueta[] etiquetas = RestClient.invokeGet(webTarget, SgEtiqueta[].class, userToken);
        return Arrays.asList(etiquetas);
    }
    

}
