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
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUsuario;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;

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
public class UsuarioRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public UsuarioRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public List<SgUsuario> buscar(FiltroUsuario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarios/buscar");
        SgUsuario[] usuarios = RestClient.invokePost(webTarget, filtro, SgUsuario[].class, userToken);
        return Arrays.asList(usuarios);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public Long buscarTotal(FiltroUsuario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarios/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    @Timeout(value = 30000L)
    public SgUsuario guardar(SgUsuario usuario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (usuario == null || userToken == null) {
            return null;
        }
        if (usuario.getUsuPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarios");
            return RestClient.invokePost(webTarget, usuario, SgUsuario.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarios");
            webTarget = webTarget.path(usuario.getUsuPk().toString());
            return RestClient.invokePut(webTarget, usuario, SgUsuario.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public SgUsuario obtenerPorCodigo(String codigo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigo == null) {
            return null;
        }
        FiltroUsuario filtro = new FiltroUsuario();
        filtro.setCodigoExacto(codigo);
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarios/buscar");
        List<SgUsuario> usuarios = Arrays.asList(RestClient.invokePost(webTarget, filtro, SgUsuario[].class, userToken));
        if (!usuarios.isEmpty()) {
            return usuarios.get(0);
        }
        return null;
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public SgUsuario obtenerPorId(Long usuarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (usuarioPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarios");
        webTarget = webTarget.path(usuarioPk.toString());
        return RestClient.invokeGet(webTarget, SgUsuario.class, userToken);
    }

    public void eliminar(Long usuarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (usuarioPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarios");
        webTarget = webTarget.path(usuarioPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long usuarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (usuarioPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarios/historial");
        webTarget = webTarget.path(usuarioPk.toString());
        RevHistorico[] usuarios = RestClient.invokeGet(webTarget, RevHistorico[].class, userToken);
        return Arrays.asList(usuarios);
    }    
  

    public void enviarAPentaho(SgUsuario usuario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (usuario == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarios/enviarUsuarioPentaho");
        RestClient.invokePost(webTarget, usuario, null, userToken);
    }

    public void eliminarDePentaho(SgUsuario usuario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (usuario == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarios/eliminarUsuarioPentaho");
        RestClient.invokePost(webTarget, usuario, null, userToken);
    }
    
    public void revocarCertificado(String userCode) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (userCode == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/usuarios/revocarcertificados");
        RestClient.invokePost(webTarget, userCode, null, userToken);
    }
    

}
