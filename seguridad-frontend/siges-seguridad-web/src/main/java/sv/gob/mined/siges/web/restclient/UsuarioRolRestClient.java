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
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgUsuarioRol;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUsuarioRol;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

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
public class UsuarioRolRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public UsuarioRolRestClient() {
    }


    
    public List<SgUsuarioRol> buscarFiltroUsuarioRol(FiltroUsuarioRol filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarioroles/buscarfiltrousuariorol");
        SgUsuarioRol[] roles = RestClient.invokePost(webTarget, filtro, SgUsuarioRol[].class, userToken);
        return Arrays.asList(roles);
    }


    public Long buscarTotal(FiltroUsuarioRol filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarioroles/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }
   
    public SgUsuarioRol guardar(SgUsuarioRol rol) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rol == null || userToken == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(rol);
        if (rol.getPurPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarioroles");
            return RestClient.invokePost(webTarget, rol, SgUsuarioRol.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarioroles");
            webTarget = webTarget.path(rol.getPurPk().toString());
            return RestClient.invokePut(webTarget, rol, SgUsuarioRol.class, userToken);
        }
    }

    public SgUsuarioRol obtenerPorId(Long rolPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rolPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarioroles");
        webTarget = webTarget.path(rolPk.toString());
        return RestClient.invokeGet(webTarget, SgUsuarioRol.class, userToken);
    }

    public void eliminar(Long rolPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rolPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarioroles");
        webTarget = webTarget.path(rolPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgUsuarioRol> obtenerHistorialPorId(Long rolPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rolPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarioroles/historial");
        webTarget = webTarget.path(rolPk.toString());
        SgUsuarioRol[] roles = RestClient.invokeGet(webTarget, SgUsuarioRol[].class, userToken);
        return Arrays.asList(roles);
    }
    

}
