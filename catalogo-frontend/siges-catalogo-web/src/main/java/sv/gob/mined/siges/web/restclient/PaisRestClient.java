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
import sv.gob.mined.siges.web.dto.SgPais;
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
public class PaisRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public PaisRestClient() {
    }


    public List<SgPais> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/paises/buscar");
        SgPais[] paises = RestClient.invokePost(webTarget, filtro, SgPais[].class, userToken);
        return Arrays.asList(paises);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/paises/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgPais guardar(SgPais pais) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pais == null || userToken == null) {
            return null;
        }
        if (pais.getPaiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/paises");
            return RestClient.invokePost(webTarget, pais, SgPais.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/paises");
            webTarget = webTarget.path(pais.getPaiPk().toString());
            return RestClient.invokePut(webTarget, pais, SgPais.class, userToken);
        }
    }

    public SgPais obtenerPorId(Long paisPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (paisPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/paises");
        webTarget = webTarget.path(paisPk.toString());
        return RestClient.invokeGet(webTarget, SgPais.class, userToken);
    }

    public void eliminar(Long paisPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (paisPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/paises");
        webTarget = webTarget.path(paisPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgPais> obtenerHistorialPorId(Long paisPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (paisPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/paises/historial");
        webTarget = webTarget.path(paisPk.toString());
        SgPais[] paises = RestClient.invokeGet(webTarget, SgPais[].class, userToken);
        return Arrays.asList(paises);
    }
    

}
