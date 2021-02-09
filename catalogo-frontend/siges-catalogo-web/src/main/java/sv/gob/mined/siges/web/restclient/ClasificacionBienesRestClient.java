/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

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
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgAfClasificacionBienes;
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
public class ClasificacionBienesRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ClasificacionBienesRestClient() {
    }


    public List<SgAfClasificacionBienes> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificacionbienes/buscar");
        SgAfClasificacionBienes[] formasadquisicion = RestClient.invokePost(webTarget, filtro, SgAfClasificacionBienes[].class, userToken);
        return Arrays.asList(formasadquisicion);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificacionbienes/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAfClasificacionBienes guardar(SgAfClasificacionBienes clasBienes) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (clasBienes == null || userToken == null) {
            return null;
        }
        if (clasBienes.getCbiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificacionbienes");
            return RestClient.invokePost(webTarget, clasBienes, SgAfClasificacionBienes.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificacionbienes");
            webTarget = webTarget.path(clasBienes.getCbiPk().toString());
            return RestClient.invokePut(webTarget, clasBienes, SgAfClasificacionBienes.class, userToken);
        }
    }

    public SgAfClasificacionBienes obtenerPorId(Long clasBienesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (clasBienesPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificacionbienes");
        webTarget = webTarget.path(clasBienesPk.toString());
        return RestClient.invokeGet(webTarget, SgAfClasificacionBienes.class, userToken);
    }

    public void eliminar(Long clasBienesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (clasBienesPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificacionbienes");
        webTarget = webTarget.path(clasBienesPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAfClasificacionBienes> obtenerHistorialPorId(Long clasBienesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (clasBienesPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificacionbienes/historial");
        webTarget = webTarget.path(clasBienesPk.toString());
        SgAfClasificacionBienes[] formasadquisicion = RestClient.invokeGet(webTarget, SgAfClasificacionBienes[].class, userToken);
        return Arrays.asList(formasadquisicion);
    }
    

}
