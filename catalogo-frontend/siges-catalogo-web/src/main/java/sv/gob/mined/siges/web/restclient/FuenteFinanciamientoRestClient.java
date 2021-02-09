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
import sv.gob.mined.siges.web.dto.SgFuenteFinanciamiento;
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
public class FuenteFinanciamientoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public FuenteFinanciamientoRestClient() {
    }


    public List<SgFuenteFinanciamiento> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento/buscar");
        SgFuenteFinanciamiento[] fuenteFinanciamiento = RestClient.invokePost(webTarget, filtro, SgFuenteFinanciamiento[].class, userToken);
        return Arrays.asList(fuenteFinanciamiento);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgFuenteFinanciamiento guardar(SgFuenteFinanciamiento fuenteFinanciamiento) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteFinanciamiento == null || userToken == null) {
            return null;
        }
        if (fuenteFinanciamiento.getFfiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento");
            return RestClient.invokePost(webTarget, fuenteFinanciamiento, SgFuenteFinanciamiento.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento");
            webTarget = webTarget.path(fuenteFinanciamiento.getFfiPk().toString());
            return RestClient.invokePut(webTarget, fuenteFinanciamiento, SgFuenteFinanciamiento.class, userToken);
        }
    }

    public SgFuenteFinanciamiento obtenerPorId(Long fuenteFinanciamientoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteFinanciamientoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento");
        webTarget = webTarget.path(fuenteFinanciamientoPk.toString());
        return RestClient.invokeGet(webTarget, SgFuenteFinanciamiento.class, userToken);
    }

    public void eliminar(Long fuenteFinanciamientoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteFinanciamientoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento");
        webTarget = webTarget.path(fuenteFinanciamientoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgFuenteFinanciamiento> obtenerHistorialPorId(Long fuenteFinanciamientoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteFinanciamientoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento/historial");
        webTarget = webTarget.path(fuenteFinanciamientoPk.toString());
        SgFuenteFinanciamiento[] fuenteFinanciamiento = RestClient.invokeGet(webTarget, SgFuenteFinanciamiento[].class, userToken);
        return Arrays.asList(fuenteFinanciamiento);
    }
    

}
