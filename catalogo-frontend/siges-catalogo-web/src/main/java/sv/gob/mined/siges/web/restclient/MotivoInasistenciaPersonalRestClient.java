/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgMotivoInasistenciaPersonal;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 3000L)
public class MotivoInasistenciaPersonalRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MotivoInasistenciaPersonalRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMotivoInasistenciaPersonal> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistenciapersonal/buscar");
        SgMotivoInasistenciaPersonal[] motivosInasistenciaPersonal = RestClient.invokePost(webTarget, filtro, SgMotivoInasistenciaPersonal[].class, userToken);
        return Arrays.asList(motivosInasistenciaPersonal);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistenciapersonal/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMotivoInasistenciaPersonal guardar(SgMotivoInasistenciaPersonal motivoInasistenciaPersonal) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoInasistenciaPersonal == null || userToken == null) {
            return null;
        }
        if (motivoInasistenciaPersonal.getMipPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistenciapersonal");
            return RestClient.invokePost(webTarget, motivoInasistenciaPersonal, SgMotivoInasistenciaPersonal.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistenciapersonal");
            webTarget = webTarget.path(motivoInasistenciaPersonal.getMipPk().toString());
            return RestClient.invokePut(webTarget, motivoInasistenciaPersonal, SgMotivoInasistenciaPersonal.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMotivoInasistenciaPersonal obtenerPorId(Long motivoInasistenciaPersonalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoInasistenciaPersonalPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistenciapersonal");
        webTarget = webTarget.path(motivoInasistenciaPersonalPk.toString());
        return RestClient.invokeGet(webTarget, SgMotivoInasistenciaPersonal.class, userToken);
    }

    public void eliminar(Long motivoInasistenciaPersonalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoInasistenciaPersonalPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistenciapersonal");
        webTarget = webTarget.path(motivoInasistenciaPersonalPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMotivoInasistenciaPersonal> obtenerHistorialPorId(Long motivoInasistenciaPersonalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoInasistenciaPersonalPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistenciapersonal/historial");
        webTarget = webTarget.path(motivoInasistenciaPersonalPk.toString());
        SgMotivoInasistenciaPersonal[] motivosInasistenciaPersonal = RestClient.invokeGet(webTarget, SgMotivoInasistenciaPersonal[].class, userToken);
        return Arrays.asList(motivosInasistenciaPersonal);
    }
    

}
