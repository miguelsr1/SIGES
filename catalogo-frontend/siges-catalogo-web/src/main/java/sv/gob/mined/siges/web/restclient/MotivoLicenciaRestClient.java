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
import sv.gob.mined.siges.web.dto.SgMotivoLicencia;
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
public class MotivoLicenciaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MotivoLicenciaRestClient() {
    }


    public List<SgMotivoLicencia> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoslicencia/buscar");
        SgMotivoLicencia[] motivoLicencia = RestClient.invokePost(webTarget, filtro, SgMotivoLicencia[].class, userToken);
        return Arrays.asList(motivoLicencia);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoslicencia/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMotivoLicencia guardar(SgMotivoLicencia motivoLicencia) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoLicencia == null || userToken == null) {
            return null;
        }
        if (motivoLicencia.getMliPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoslicencia");
            return RestClient.invokePost(webTarget, motivoLicencia, SgMotivoLicencia.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoslicencia");
            webTarget = webTarget.path(motivoLicencia.getMliPk().toString());
            return RestClient.invokePut(webTarget, motivoLicencia, SgMotivoLicencia.class, userToken);
        }
    }

    public SgMotivoLicencia obtenerPorId(Long motivoLicenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoLicenciaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoslicencia");
        webTarget = webTarget.path(motivoLicenciaPk.toString());
        return RestClient.invokeGet(webTarget, SgMotivoLicencia.class, userToken);
    }

    public void eliminar(Long motivoLicenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoLicenciaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoslicencia");
        webTarget = webTarget.path(motivoLicenciaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgMotivoLicencia> obtenerHistorialPorId(Long motivoLicenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoLicenciaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoslicencia/historial");
        webTarget = webTarget.path(motivoLicenciaPk.toString());
        SgMotivoLicencia[] motivoLicencia = RestClient.invokeGet(webTarget, SgMotivoLicencia[].class, userToken);
        return Arrays.asList(motivoLicencia);
    }
    

}
