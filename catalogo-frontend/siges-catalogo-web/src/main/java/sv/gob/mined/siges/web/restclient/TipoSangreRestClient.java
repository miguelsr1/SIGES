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
import sv.gob.mined.siges.web.dto.SgTipoSangre;
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
public class TipoSangreRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoSangreRestClient() {
    }


    public List<SgTipoSangre> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipossangre/buscar");
        SgTipoSangre[] tiposSangre = RestClient.invokePost(webTarget, filtro, SgTipoSangre[].class, userToken);
        return Arrays.asList(tiposSangre);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipossangre/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoSangre guardar(SgTipoSangre tipoSangre) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoSangre == null || userToken == null) {
            return null;
        }
        if (tipoSangre.getTsaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipossangre");
            return RestClient.invokePost(webTarget, tipoSangre, SgTipoSangre.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipossangre");
            webTarget = webTarget.path(tipoSangre.getTsaPk().toString());
            return RestClient.invokePut(webTarget, tipoSangre, SgTipoSangre.class, userToken);
        }
    }

    public SgTipoSangre obtenerPorId(Long tipoSangrePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoSangrePk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipossangre");
        webTarget = webTarget.path(tipoSangrePk.toString());
        return RestClient.invokeGet(webTarget, SgTipoSangre.class, userToken);
    }

    public void eliminar(Long tipoSangrePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoSangrePk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipossangre");
        webTarget = webTarget.path(tipoSangrePk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoSangre> obtenerHistorialPorId(Long tipoSangrePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoSangrePk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipossangre/historial");
        webTarget = webTarget.path(tipoSangrePk.toString());
        SgTipoSangre[] tiposSangre = RestClient.invokeGet(webTarget, SgTipoSangre[].class, userToken);
        return Arrays.asList(tiposSangre);
    }
    

}
