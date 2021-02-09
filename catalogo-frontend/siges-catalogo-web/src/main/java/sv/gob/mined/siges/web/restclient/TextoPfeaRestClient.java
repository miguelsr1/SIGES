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
import sv.gob.mined.siges.web.dto.SgTextoPfea;
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
@Timeout(value = 6000L)
public class TextoPfeaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TextoPfeaRestClient() {
    }


    public List<SgTextoPfea> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/textospfea/buscar");
        SgTextoPfea[] configuraciones = RestClient.invokePost(webTarget, filtro, SgTextoPfea[].class, userToken);
        return Arrays.asList(configuraciones);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/textospfea/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTextoPfea guardar(SgTextoPfea configuracion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (configuracion == null || userToken == null) {
            return null;
        }
        if (configuracion.getTexPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/textospfea");
            return RestClient.invokePost(webTarget, configuracion, SgTextoPfea.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/textospfea");
            webTarget = webTarget.path(configuracion.getTexPk().toString());
            return RestClient.invokePut(webTarget, configuracion, SgTextoPfea.class, userToken);
        }
    }

    public SgTextoPfea obtenerPorId(Long configuracionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (configuracionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/textospfea");
        webTarget = webTarget.path(configuracionPk.toString());
        return RestClient.invokeGet(webTarget, SgTextoPfea.class, userToken);
    }

    public void eliminar(Long configuracionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (configuracionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/textospfea");
        webTarget = webTarget.path(configuracionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTextoPfea> obtenerHistorialPorId(Long configuracionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (configuracionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/textospfea/historial");
        webTarget = webTarget.path(configuracionPk.toString());
        SgTextoPfea[] configuraciones = RestClient.invokeGet(webTarget, SgTextoPfea[].class, userToken);
        return Arrays.asList(configuraciones);
    }
    

}
