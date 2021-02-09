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
import sv.gob.mined.siges.web.dto.SgMotivoCierreSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class MotivoCierreSedeRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MotivoCierreSedeRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMotivoCierreSede> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoscierresede/buscar");
        SgMotivoCierreSede[] motivosCierreSede = RestClient.invokePost(webTarget, filtro, SgMotivoCierreSede[].class, userToken);
        return Arrays.asList(motivosCierreSede);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoscierresede/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMotivoCierreSede guardar(SgMotivoCierreSede motivoCierreSede) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoCierreSede == null || userToken == null) {
            return null;
        }
        if (motivoCierreSede.getMcsPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoscierresede");
            return RestClient.invokePost(webTarget, motivoCierreSede, SgMotivoCierreSede.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoscierresede");
            webTarget = webTarget.path(motivoCierreSede.getMcsPk().toString());
            return RestClient.invokePut(webTarget, motivoCierreSede, SgMotivoCierreSede.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMotivoCierreSede obtenerPorId(Long motivoCierreSedePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoCierreSedePk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoscierresede");
        webTarget = webTarget.path(motivoCierreSedePk.toString());
        return RestClient.invokeGet(webTarget, SgMotivoCierreSede.class, userToken);
    }

    public void eliminar(Long motivoCierreSedePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoCierreSedePk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoscierresede");
        webTarget = webTarget.path(motivoCierreSedePk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMotivoCierreSede> obtenerHistorialPorId(Long motivoCierreSedePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoCierreSedePk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoscierresede/historial");
        webTarget = webTarget.path(motivoCierreSedePk.toString());
        SgMotivoCierreSede[] motivosCierreSede = RestClient.invokeGet(webTarget, SgMotivoCierreSede[].class, userToken);
        return Arrays.asList(motivosCierreSede);
    }
    

}
