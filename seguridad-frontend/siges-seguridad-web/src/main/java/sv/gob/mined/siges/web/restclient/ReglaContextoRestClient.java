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
import sv.gob.mined.siges.web.dto.SgReglaContexto;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
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
public class ReglaContextoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ReglaContextoRestClient() {
    }


    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public List<SgReglaContexto> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/reglascontexto/buscar");
        SgReglaContexto[] roles = RestClient.invokePost(webTarget, filtro, SgReglaContexto[].class, userToken);
        return Arrays.asList(roles);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/reglascontexto/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgReglaContexto guardar(SgReglaContexto rol) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rol == null || userToken == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(rol);
        if (rol.getRecPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/reglascontexto");
            return RestClient.invokePost(webTarget, rol, SgReglaContexto.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/reglascontexto");
            webTarget = webTarget.path(rol.getRecPk().toString());
            return RestClient.invokePut(webTarget, rol, SgReglaContexto.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public SgReglaContexto obtenerPorId(Long rolPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rolPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/reglascontexto");
        webTarget = webTarget.path(rolPk.toString());
        return RestClient.invokeGet(webTarget, SgReglaContexto.class, userToken);
    }

    public void eliminar(Long rolPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rolPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/reglascontexto");
        webTarget = webTarget.path(rolPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgReglaContexto> obtenerHistorialPorId(Long rolPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (rolPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_SEGURIDAD, "v1/reglascontexto/historial");
        webTarget = webTarget.path(rolPk.toString());
        SgReglaContexto[] roles = RestClient.invokeGet(webTarget, SgReglaContexto[].class, userToken);
        return Arrays.asList(roles);
    }
    

}
