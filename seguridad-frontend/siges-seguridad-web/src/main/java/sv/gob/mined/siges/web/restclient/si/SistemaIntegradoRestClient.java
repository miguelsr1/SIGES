/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient.si;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.si.SgSistemaIntegrado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSistemaIntegrado;
import sv.gob.mined.siges.web.restclient.RestClient;

@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class SistemaIntegradoRestClient implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SistemaIntegradoRestClient.class.getName());

    @Inject
    @Named("userToken") 
    private String userToken;

    public SistemaIntegradoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgSistemaIntegrado> buscar(FiltroSistemaIntegrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados/buscar");
        SgSistemaIntegrado[] sistemaIntegrado = RestClient.invokePost(webTarget, filtro, SgSistemaIntegrado[].class, userToken);
        return new ArrayList<>(Arrays.asList(sistemaIntegrado));
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroSistemaIntegrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgSistemaIntegrado obtenerPorId(Long sistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sistemaIntegradoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados");
        webTarget = webTarget.path(sistemaIntegradoPk.toString());
        return RestClient.invokeGet(webTarget, SgSistemaIntegrado.class, userToken);
    }

   

}
