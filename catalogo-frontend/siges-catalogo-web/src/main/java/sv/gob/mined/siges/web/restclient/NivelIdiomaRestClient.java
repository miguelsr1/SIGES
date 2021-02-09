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
import sv.gob.mined.siges.web.dto.SgNivelIdioma;
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
public class NivelIdiomaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public NivelIdiomaRestClient() {
    }


    public List<SgNivelIdioma> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesidiomas/buscar");
        SgNivelIdioma[] nivelIdioma = RestClient.invokePost(webTarget, filtro, SgNivelIdioma[].class, userToken);
        return Arrays.asList(nivelIdioma);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesidiomas/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgNivelIdioma guardar(SgNivelIdioma nivelIdioma) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelIdioma == null || userToken == null) {
            return null;
        }
        if (nivelIdioma.getNidPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesidiomas");
            return RestClient.invokePost(webTarget, nivelIdioma, SgNivelIdioma.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesidiomas");
            webTarget = webTarget.path(nivelIdioma.getNidPk().toString());
            return RestClient.invokePut(webTarget, nivelIdioma, SgNivelIdioma.class, userToken);
        }
    }

    public SgNivelIdioma obtenerPorId(Long nivelIdiomaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelIdiomaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesidiomas");
        webTarget = webTarget.path(nivelIdiomaPk.toString());
        return RestClient.invokeGet(webTarget, SgNivelIdioma.class, userToken);
    }

    public void eliminar(Long nivelIdiomaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelIdiomaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesidiomas");
        webTarget = webTarget.path(nivelIdiomaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgNivelIdioma> obtenerHistorialPorId(Long nivelIdiomaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelIdiomaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesidiomas/historial");
        webTarget = webTarget.path(nivelIdiomaPk.toString());
        SgNivelIdioma[] nivelIdioma = RestClient.invokeGet(webTarget, SgNivelIdioma[].class, userToken);
        return Arrays.asList(nivelIdioma);
    }
    

}
