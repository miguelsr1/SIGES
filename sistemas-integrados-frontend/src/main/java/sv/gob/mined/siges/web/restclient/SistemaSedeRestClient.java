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
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.SgSistemaSede;
import sv.gob.mined.siges.client.excepciones.HttpClientException;
import sv.gob.mined.siges.client.excepciones.HttpServerException;
import sv.gob.mined.siges.client.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSistemaSede;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 6000L)
public class SistemaSedeRestClient implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SistemaSedeRestClient.class.getName());

    @Inject
    private RestClient restClient;

    private Client client = null;

    @PostConstruct
    public void init() {
        client = RestClient.getClient();
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            client.close();
        }
    }

    public SistemaSedeRestClient() {
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgSede obtenerPorId(Long sedPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sedPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemassedes");
        webTarget = webTarget.path(sedPk.toString());
        return restClient.invokeGet(webTarget, SgSede.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgSede> buscar(FiltroSistemaSede filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemassedes/buscar");
        SgSede[] sistemaIntegrado = restClient.invokePost(webTarget, filtro, SgSede[].class);
        return new ArrayList<>(Arrays.asList(sistemaIntegrado));
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroSistemaSede filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemassedes/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public Boolean guardar(SgSistemaSede sistemaIntegrado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sistemaIntegrado == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemassedes");
        return restClient.invokePost(webTarget, sistemaIntegrado, Boolean.class);
    }

    public void eliminar(Long sistema, Long sede) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sistema == null || sede == null) {
            return;
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemassedes");
        webTarget = webTarget.path(sistema.toString()).path(sede.toString());
        restClient.invokeDelete(webTarget);
    }

}
