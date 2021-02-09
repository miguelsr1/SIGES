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
import sv.gob.mined.siges.web.dto.SgEjecucionProcesoNocturno;
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
public class EjecucionProcesoNocturnoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EjecucionProcesoNocturnoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEjecucionProcesoNocturno> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ejecucionprocesonocturno/buscar");
        SgEjecucionProcesoNocturno[] ejecucionProcesoNocturno = RestClient.invokePost(webTarget, filtro, SgEjecucionProcesoNocturno[].class, userToken);
        return Arrays.asList(ejecucionProcesoNocturno);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ejecucionprocesonocturno/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEjecucionProcesoNocturno guardar(SgEjecucionProcesoNocturno ejecucionProcesoNocturno) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ejecucionProcesoNocturno == null || userToken == null) {
            return null;
        }
        if (ejecucionProcesoNocturno.getEprPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ejecucionprocesonocturno");
            return RestClient.invokePost(webTarget, ejecucionProcesoNocturno, SgEjecucionProcesoNocturno.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ejecucionprocesonocturno");
            webTarget = webTarget.path(ejecucionProcesoNocturno.getEprPk().toString());
            return RestClient.invokePut(webTarget, ejecucionProcesoNocturno, SgEjecucionProcesoNocturno.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEjecucionProcesoNocturno obtenerPorId(Long ejecucionProcesoNocturnoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ejecucionProcesoNocturnoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ejecucionprocesonocturno");
        webTarget = webTarget.path(ejecucionProcesoNocturnoPk.toString());
        return RestClient.invokeGet(webTarget, SgEjecucionProcesoNocturno.class, userToken);
    }

    public void eliminar(Long ejecucionProcesoNocturnoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ejecucionProcesoNocturnoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ejecucionprocesonocturno");
        webTarget = webTarget.path(ejecucionProcesoNocturnoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEjecucionProcesoNocturno> obtenerHistorialPorId(Long ejecucionProcesoNocturnoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ejecucionProcesoNocturnoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ejecucionprocesonocturno/historial");
        webTarget = webTarget.path(ejecucionProcesoNocturnoPk.toString());
        SgEjecucionProcesoNocturno[] ejecucionProcesoNocturno = RestClient.invokeGet(webTarget, SgEjecucionProcesoNocturno[].class, userToken);
        return Arrays.asList(ejecucionProcesoNocturno);
    }
    

}
