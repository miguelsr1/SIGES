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
import sv.gob.mined.siges.web.dto.SgAfp;
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
public class AfpRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public AfpRestClient() {
    }


    public List<SgAfp> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/afp/buscar");
        SgAfp[] afp = RestClient.invokePost(webTarget, filtro, SgAfp[].class, userToken);
        return Arrays.asList(afp);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/afp/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAfp guardar(SgAfp afp) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (afp == null || userToken == null) {
            return null;
        }
        if (afp.getAfpPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/afp");
            return RestClient.invokePost(webTarget, afp, SgAfp.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/afp");
            webTarget = webTarget.path(afp.getAfpPk().toString());
            return RestClient.invokePut(webTarget, afp, SgAfp.class, userToken);
        }
    }

    public SgAfp obtenerPorId(Long afpPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (afpPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/afp");
        webTarget = webTarget.path(afpPk.toString());
        return RestClient.invokeGet(webTarget, SgAfp.class, userToken);
    }

    public void eliminar(Long afpPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (afpPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/afp");
        webTarget = webTarget.path(afpPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAfp> obtenerHistorialPorId(Long afpPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (afpPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/afp/historial");
        webTarget = webTarget.path(afpPk.toString());
        SgAfp[] afp = RestClient.invokeGet(webTarget, SgAfp[].class, userToken);
        return Arrays.asList(afp);
    }
    

}
