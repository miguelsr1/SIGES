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
import sv.gob.mined.siges.web.dto.SgMotivoInasistencia;
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
public class MotivoInasistenciaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MotivoInasistenciaRestClient() {
    }


    public List<SgMotivoInasistencia> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistencia/buscar");
        SgMotivoInasistencia[] motivosInasistencia = RestClient.invokePost(webTarget, filtro, SgMotivoInasistencia[].class, userToken);
        return Arrays.asList(motivosInasistencia);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistencia/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMotivoInasistencia guardar(SgMotivoInasistencia motivoInasistencia) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoInasistencia == null || userToken == null) {
            return null;
        }
        if (motivoInasistencia.getMinPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistencia");
            return RestClient.invokePost(webTarget, motivoInasistencia, SgMotivoInasistencia.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistencia");
            webTarget = webTarget.path(motivoInasistencia.getMinPk().toString());
            return RestClient.invokePut(webTarget, motivoInasistencia, SgMotivoInasistencia.class, userToken);
        }
    }

    public SgMotivoInasistencia obtenerPorId(Long motivoInasistenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoInasistenciaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistencia");
        webTarget = webTarget.path(motivoInasistenciaPk.toString());
        return RestClient.invokeGet(webTarget, SgMotivoInasistencia.class, userToken);
    }

    public void eliminar(Long motivoInasistenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoInasistenciaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistencia");
        webTarget = webTarget.path(motivoInasistenciaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgMotivoInasistencia> obtenerHistorialPorId(Long motivoInasistenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoInasistenciaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistencia/historial");
        webTarget = webTarget.path(motivoInasistenciaPk.toString());
        SgMotivoInasistencia[] motivosInasistencia = RestClient.invokeGet(webTarget, SgMotivoInasistencia[].class, userToken);
        return Arrays.asList(motivosInasistencia);
    }
    

}
