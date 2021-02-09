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
import sv.gob.mined.siges.web.dto.SgTipoServicioSanitario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTipoServicioSanitario;

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
public class TipoServicioSanitarioRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoServicioSanitarioRestClient() {
    }


    public List<SgTipoServicioSanitario> buscar(FiltroTipoServicioSanitario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposserviciosanitario/buscar");
        SgTipoServicioSanitario[] tipoServicioSanitario = RestClient.invokePost(webTarget, filtro, SgTipoServicioSanitario[].class, userToken);
        return Arrays.asList(tipoServicioSanitario);
    }

    public Long buscarTotal(FiltroTipoServicioSanitario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposserviciosanitario/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoServicioSanitario guardar(SgTipoServicioSanitario tipoServicioSanitario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoServicioSanitario == null || userToken == null) {
            return null;
        }
        if (tipoServicioSanitario.getTssPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposserviciosanitario");
            return RestClient.invokePost(webTarget, tipoServicioSanitario, SgTipoServicioSanitario.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposserviciosanitario");
            webTarget = webTarget.path(tipoServicioSanitario.getTssPk().toString());
            return RestClient.invokePut(webTarget, tipoServicioSanitario, SgTipoServicioSanitario.class, userToken);
        }
    }

    public SgTipoServicioSanitario obtenerPorId(Long tipoServicioSanitarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoServicioSanitarioPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposserviciosanitario");
        webTarget = webTarget.path(tipoServicioSanitarioPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoServicioSanitario.class, userToken);
    }

    public void eliminar(Long tipoServicioSanitarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoServicioSanitarioPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposserviciosanitario");
        webTarget = webTarget.path(tipoServicioSanitarioPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoServicioSanitario> obtenerHistorialPorId(Long tipoServicioSanitarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoServicioSanitarioPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposserviciosanitario/historial");
        webTarget = webTarget.path(tipoServicioSanitarioPk.toString());
        SgTipoServicioSanitario[] tipoServicioSanitario = RestClient.invokeGet(webTarget, SgTipoServicioSanitario[].class, userToken);
        return Arrays.asList(tipoServicioSanitario);
    }
    

}
