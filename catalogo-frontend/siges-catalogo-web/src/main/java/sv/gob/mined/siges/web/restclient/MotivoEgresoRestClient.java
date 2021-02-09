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
import sv.gob.mined.siges.web.dto.SgMotivoEgreso;
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
public class MotivoEgresoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MotivoEgresoRestClient() {
    }


    public List<SgMotivoEgreso> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosegreso/buscar");
        SgMotivoEgreso[] motivosEgreso = RestClient.invokePost(webTarget, filtro, SgMotivoEgreso[].class, userToken);
        return Arrays.asList(motivosEgreso);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosegreso/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMotivoEgreso guardar(SgMotivoEgreso motivoEgreso) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoEgreso == null || userToken == null) {
            return null;
        }
        if (motivoEgreso.getMegPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosegreso");
            return RestClient.invokePost(webTarget, motivoEgreso, SgMotivoEgreso.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosegreso");
            webTarget = webTarget.path(motivoEgreso.getMegPk().toString());
            return RestClient.invokePut(webTarget, motivoEgreso, SgMotivoEgreso.class, userToken);
        }
    }

    public SgMotivoEgreso obtenerPorId(Long motivoEgresoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoEgresoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosegreso");
        webTarget = webTarget.path(motivoEgresoPk.toString());
        return RestClient.invokeGet(webTarget, SgMotivoEgreso.class, userToken);
    }

    public void eliminar(Long motivoEgresoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoEgresoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosegreso");
        webTarget = webTarget.path(motivoEgresoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgMotivoEgreso> obtenerHistorialPorId(Long motivoEgresoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoEgresoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosegreso/historial");
        webTarget = webTarget.path(motivoEgresoPk.toString());
        SgMotivoEgreso[] motivosEgreso = RestClient.invokeGet(webTarget, SgMotivoEgreso[].class, userToken);
        return Arrays.asList(motivosEgreso);
    }
    

}
