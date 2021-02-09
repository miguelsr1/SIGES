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
import sv.gob.mined.siges.web.dto.SgNivelEscalafon;
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
public class NivelEscalafonRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public NivelEscalafonRestClient() {
    }


    public List<SgNivelEscalafon> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelescalafon/buscar");
        SgNivelEscalafon[] nivelEscalafon = RestClient.invokePost(webTarget, filtro, SgNivelEscalafon[].class, userToken);
        return Arrays.asList(nivelEscalafon);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelescalafon/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgNivelEscalafon guardar(SgNivelEscalafon nivelEscalafon) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelEscalafon == null || userToken == null) {
            return null;
        }
        if (nivelEscalafon.getNesPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelescalafon");
            return RestClient.invokePost(webTarget, nivelEscalafon, SgNivelEscalafon.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelescalafon");
            webTarget = webTarget.path(nivelEscalafon.getNesPk().toString());
            return RestClient.invokePut(webTarget, nivelEscalafon, SgNivelEscalafon.class, userToken);
        }
    }

    public SgNivelEscalafon obtenerPorId(Long nivelEscalafonPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelEscalafonPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelescalafon");
        webTarget = webTarget.path(nivelEscalafonPk.toString());
        return RestClient.invokeGet(webTarget, SgNivelEscalafon.class, userToken);
    }

    public void eliminar(Long nivelEscalafonPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelEscalafonPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelescalafon");
        webTarget = webTarget.path(nivelEscalafonPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgNivelEscalafon> obtenerHistorialPorId(Long nivelEscalafonPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelEscalafonPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelescalafon/historial");
        webTarget = webTarget.path(nivelEscalafonPk.toString());
        SgNivelEscalafon[] nivelEscalafon = RestClient.invokeGet(webTarget, SgNivelEscalafon[].class, userToken);
        return Arrays.asList(nivelEscalafon);
    }
    

}
