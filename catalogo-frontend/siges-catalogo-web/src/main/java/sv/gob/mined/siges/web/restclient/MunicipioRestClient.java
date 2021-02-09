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
import sv.gob.mined.siges.web.dto.SgMunicipio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;

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
public class MunicipioRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MunicipioRestClient() {
    }


    public List<SgMunicipio> buscar(FiltroMunicipio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios/buscar");
        SgMunicipio[] municipios = RestClient.invokePost(webTarget, filtro, SgMunicipio[].class, userToken);
        return Arrays.asList(municipios);
    }

    public Long buscarTotal(FiltroMunicipio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMunicipio guardar(SgMunicipio municipio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (municipio == null || userToken == null) {
            return null;
        }
        if (municipio.getMunPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios");
            return RestClient.invokePost(webTarget, municipio, SgMunicipio.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios");
            webTarget = webTarget.path(municipio.getMunPk().toString());
            return RestClient.invokePut(webTarget, municipio, SgMunicipio.class, userToken);
        }
    }

    public SgMunicipio obtenerPorId(Long municipioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (municipioPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios");
        webTarget = webTarget.path(municipioPk.toString());
        return RestClient.invokeGet(webTarget, SgMunicipio.class, userToken);
    }

    public void eliminar(Long municipioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (municipioPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios");
        webTarget = webTarget.path(municipioPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgMunicipio> obtenerHistorialPorId(Long municipioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (municipioPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios/historial");
        webTarget = webTarget.path(municipioPk.toString());
        SgMunicipio[] municipios = RestClient.invokeGet(webTarget, SgMunicipio[].class, userToken);
        return Arrays.asList(municipios);
    }
    

}
