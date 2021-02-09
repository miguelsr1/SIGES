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
import sv.gob.mined.siges.web.dto.SgConfiguracion;
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
@Timeout(value = 10000L)
public class ConfiguracionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ConfiguracionRestClient() {
    }


    public List<SgConfiguracion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuraciones/buscar");
        SgConfiguracion[] configuraciones = RestClient.invokePost(webTarget, filtro, SgConfiguracion[].class, userToken);
        return Arrays.asList(configuraciones);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuraciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgConfiguracion obtenerPorId(Long configuracionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (configuracionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuraciones");
        webTarget = webTarget.path(configuracionPk.toString());
        return RestClient.invokeGet(webTarget, SgConfiguracion.class, userToken);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public SgConfiguracion obtenerPorCodigo(String codigo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigo == null) {
            return null;
        }
        FiltroCodiguera filtro = new FiltroCodiguera();
        filtro.setCodigoExacto(codigo);
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuraciones/buscar");
        List<SgConfiguracion> configs =  Arrays.asList(RestClient.invokePost(webTarget, filtro, SgConfiguracion[].class, userToken));
        if (!configs.isEmpty()){
            return configs.get(0);
        }
        return null;
    }

  
    

}
