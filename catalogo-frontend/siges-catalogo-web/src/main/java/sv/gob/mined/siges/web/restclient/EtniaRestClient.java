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
import sv.gob.mined.siges.web.dto.SgEtnia;
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
public class EtniaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EtniaRestClient() {
    }


    public List<SgEtnia> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etnias/buscar");
        SgEtnia[] etnias = RestClient.invokePost(webTarget, filtro, SgEtnia[].class, userToken);
        return Arrays.asList(etnias);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etnias/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEtnia guardar(SgEtnia etnia) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (etnia == null || userToken == null) {
            return null;
        }
        if (etnia.getEtnPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etnias");
            return RestClient.invokePost(webTarget, etnia, SgEtnia.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etnias");
            webTarget = webTarget.path(etnia.getEtnPk().toString());
            return RestClient.invokePut(webTarget, etnia, SgEtnia.class, userToken);
        }
    }

    public SgEtnia obtenerPorId(Long etniaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (etniaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etnias");
        webTarget = webTarget.path(etniaPk.toString());
        return RestClient.invokeGet(webTarget, SgEtnia.class, userToken);
    }

    public void eliminar(Long etniaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (etniaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etnias");
        webTarget = webTarget.path(etniaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgEtnia> obtenerHistorialPorId(Long etniaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (etniaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etnias/historial");
        webTarget = webTarget.path(etniaPk.toString());
        SgEtnia[] etnias = RestClient.invokeGet(webTarget, SgEtnia[].class, userToken);
        return Arrays.asList(etnias);
    }
    

}
