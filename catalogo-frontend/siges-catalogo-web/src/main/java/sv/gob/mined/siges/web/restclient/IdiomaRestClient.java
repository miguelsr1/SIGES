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
import sv.gob.mined.siges.web.dto.SgIdioma;
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
public class IdiomaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public IdiomaRestClient() {
    }


    public List<SgIdioma> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/idiomas/buscar");
        SgIdioma[] idiomas = RestClient.invokePost(webTarget, filtro, SgIdioma[].class, userToken);
        return Arrays.asList(idiomas);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/idiomas/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgIdioma guardar(SgIdioma idioma) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (idioma == null || userToken == null) {
            return null;
        }
        if (idioma.getIdiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/idiomas");
            return RestClient.invokePost(webTarget, idioma, SgIdioma.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/idiomas");
            webTarget = webTarget.path(idioma.getIdiPk().toString());
            return RestClient.invokePut(webTarget, idioma, SgIdioma.class, userToken);
        }
    }

    public SgIdioma obtenerPorId(Long idiomaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (idiomaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/idiomas");
        webTarget = webTarget.path(idiomaPk.toString());
        return RestClient.invokeGet(webTarget, SgIdioma.class, userToken);
    }

    public void eliminar(Long idiomaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (idiomaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/idiomas");
        webTarget = webTarget.path(idiomaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgIdioma> obtenerHistorialPorId(Long idiomaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (idiomaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/idiomas/historial");
        webTarget = webTarget.path(idiomaPk.toString());
        SgIdioma[] idiomas = RestClient.invokeGet(webTarget, SgIdioma[].class, userToken);
        return Arrays.asList(idiomas);
    }
    

}
