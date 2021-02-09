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
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.OperationSecurity;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 3, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class, Exception.class}, delay = 1000)
@Timeout(value = 10000L)
public class SeguridadRestClient implements Serializable {

    public SeguridadRestClient() {
    }

    public List<String> obtenerOperacionesPorCodigoUsuario(String codigoUsuario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigoUsuario == null) {
            return new ArrayList<>();
        }
        //Al momento de cargar las operaciones el token todavía no está creado
        String token = JWTUtils.generarToken(codigoUsuario, "", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        WebTarget webTarget = StaticRestClient.getWebTarget(ConstantesServiciosRest.SEGURIDAD, "v1/usuarios/operaciones");
        webTarget = webTarget.path(codigoUsuario.toString());
        String[] operaciones = StaticRestClient.invokeGet(webTarget, String[].class, token);
        return Arrays.asList(operaciones);
    }
    
    
    public List<OperationSecurity> obtenerOperacionesSeguridad(String codigoUsuario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigoUsuario == null) {
            return new ArrayList<>();
        }
        String token = JWTUtils.generarToken(codigoUsuario, "", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        WebTarget webTarget = StaticRestClient.getWebTarget(ConstantesServiciosRest.SEGURIDAD, "v1/usuarios/operacionesSeguridad");
        webTarget = webTarget.path(codigoUsuario);
        OperationSecurity[] operaciones = StaticRestClient.invokeGet(webTarget, OperationSecurity[].class, token);
        return Arrays.asList(operaciones);
    }
    
    
    public List<OperationSecurity> obtenerOperacionesSeguridad(String codigoUsuario, String operacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigoUsuario == null || operacion == null) {
            return new ArrayList<>();
        }
        String token = JWTUtils.generarToken(codigoUsuario, "", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        WebTarget webTarget = StaticRestClient.getWebTarget(ConstantesServiciosRest.SEGURIDAD, "v1/usuarios/operacionesSeguridad");
        webTarget = webTarget.path(codigoUsuario);
        webTarget = webTarget.queryParam("operacionCodigo", operacion);
        OperationSecurity[] operaciones = StaticRestClient.invokeGet(webTarget, OperationSecurity[].class, token);
        return Arrays.asList(operaciones);
    }
    
    public List<OperationSecurity> obtenerOperacionesSeguridad(String codigoUsuario, List<Long> categoriasOp) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigoUsuario == null) {
            return new ArrayList<>();
        }
        String token = JWTUtils.generarToken(codigoUsuario, "", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        WebTarget webTarget = StaticRestClient.getWebTarget(ConstantesServiciosRest.SEGURIDAD, "v1/usuarios/operacionesSeguridad");
        webTarget = webTarget.path(codigoUsuario);
        if (categoriasOp != null && !categoriasOp.isEmpty()){
            for (Long l : categoriasOp){
                webTarget = webTarget.queryParam("categoriasOperacionPk", l);
            }
        }
        OperationSecurity[] operaciones = StaticRestClient.invokeGet(webTarget, OperationSecurity[].class, token);
        return Arrays.asList(operaciones);
    }
    

}
