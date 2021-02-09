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
import sv.gob.mined.siges.web.dto.SgTipoCalle;
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
public class TipoCalleRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoCalleRestClient() {
    }


    public List<SgTipoCalle> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcalle/buscar");
        SgTipoCalle[] carrera = RestClient.invokePost(webTarget, filtro, SgTipoCalle[].class, userToken);
        return Arrays.asList(carrera);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcalle/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoCalle guardar(SgTipoCalle carrera) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (carrera == null || userToken == null) {
            return null;
        }
        if (carrera.getTllPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcalle");
            return RestClient.invokePost(webTarget, carrera, SgTipoCalle.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcalle");
            webTarget = webTarget.path(carrera.getTllPk().toString());
            return RestClient.invokePut(webTarget, carrera, SgTipoCalle.class, userToken);
        }
    }

    public SgTipoCalle obtenerPorId(Long carreraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (carreraPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcalle");
        webTarget = webTarget.path(carreraPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoCalle.class, userToken);
    }

    public void eliminar(Long carreraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (carreraPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcalle");
        webTarget = webTarget.path(carreraPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoCalle> obtenerHistorialPorId(Long carreraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (carreraPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcalle/historial");
        webTarget = webTarget.path(carreraPk.toString());
        SgTipoCalle[] carrera = RestClient.invokeGet(webTarget, SgTipoCalle[].class, userToken);
        return Arrays.asList(carrera);
    }
    

}
