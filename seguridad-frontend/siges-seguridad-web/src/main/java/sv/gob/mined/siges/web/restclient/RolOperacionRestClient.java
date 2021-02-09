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
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgRolOperacion;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class RolOperacionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public RolOperacionRestClient() {
    }


    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public List<SgRolOperacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/rolesoperacion/buscar");
        SgRolOperacion[] rolesOperaciones = RestClient.invokePost(webTarget, filtro, SgRolOperacion[].class, userToken);
        return Arrays.asList(rolesOperaciones);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/rolesoperacion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgRolOperacion guardar(SgRolOperacion rolOperacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rolOperacion == null || userToken == null) {
            return null;
        }
        if (rolOperacion.getRopPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/rolesoperacion");
            return RestClient.invokePost(webTarget, rolOperacion, SgRolOperacion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/rolesoperacion");
            webTarget = webTarget.path(rolOperacion.getRopPk().toString());
            return RestClient.invokePut(webTarget, rolOperacion, SgRolOperacion.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public SgRolOperacion obtenerPorId(Long rolOperacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rolOperacionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/rolesoperacion");
        webTarget = webTarget.path(rolOperacionPk.toString());
        return RestClient.invokeGet(webTarget, SgRolOperacion.class, userToken);
    }

    public void eliminar(Long rolOperacionPK) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rolOperacionPK == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/rolesoperacion");
        webTarget = webTarget.path(rolOperacionPK.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgRolOperacion> obtenerHistorialPorId(Long rolOperacionPK) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rolOperacionPK == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/rolesoperacion/historial");
        webTarget = webTarget.path(rolOperacionPK.toString());
        SgRolOperacion[] rolesOperaciones = RestClient.invokeGet(webTarget, SgRolOperacion[].class, userToken);
        return Arrays.asList(rolesOperaciones);
    }
    

}
