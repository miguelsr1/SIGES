/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.client.excepciones.HttpClientException;
import sv.gob.mined.siges.client.excepciones.HttpServerException;
import sv.gob.mined.siges.client.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.dto.SgRelProyectoFinanciamientoSistemaIntegrado;
import sv.gob.mined.siges.web.filtros.FiltroRelProyectoFinanciamientoSistemaIntegrado;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@Traced
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class RelProyectoFinanciamientoSistemaIntegradoRestClient implements Serializable {

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

    public RelProyectoFinanciamientoSistemaIntegradoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelProyectoFinanciamientoSistemaIntegrado> buscar(FiltroRelProyectoFinanciamientoSistemaIntegrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/relproyectofinanciamiento/buscar");
        SgRelProyectoFinanciamientoSistemaIntegrado[] proyectosFinanciamientoSistemaIntegrado = restClient.invokePost(webTarget, filtro, SgRelProyectoFinanciamientoSistemaIntegrado[].class);
        return Arrays.asList(proyectosFinanciamientoSistemaIntegrado);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/relproyectofinanciamiento/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public Boolean guardar(List<SgRelProyectoFinanciamientoSistemaIntegrado> proyectoFinanciamientoSistemaIntegrado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegrado == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/relproyectofinanciamiento");
        return restClient.invokePost(webTarget, proyectoFinanciamientoSistemaIntegrado, Boolean.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelProyectoFinanciamientoSistemaIntegrado obtenerPorId(Long proyectoFinanciamientoSistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegradoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/relproyectofinanciamiento");
        webTarget = webTarget.path(proyectoFinanciamientoSistemaIntegradoPk.toString());
        return restClient.invokeGet(webTarget, SgRelProyectoFinanciamientoSistemaIntegrado.class);
    }

    public void eliminar(Long proyectoFinanciamientoSistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegradoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/relproyectofinanciamiento");
        webTarget = webTarget.path(proyectoFinanciamientoSistemaIntegradoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelProyectoFinanciamientoSistemaIntegrado> obtenerHistorialPorId(Long proyectoFinanciamientoSistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegradoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/relproyectofinanciamiento/historial");
        webTarget = webTarget.path(proyectoFinanciamientoSistemaIntegradoPk.toString());
        SgRelProyectoFinanciamientoSistemaIntegrado[] proyectosFinanciamientoSistemaIntegrado = restClient.invokeGet(webTarget, SgRelProyectoFinanciamientoSistemaIntegrado[].class);
        return Arrays.asList(proyectosFinanciamientoSistemaIntegrado);
    }

}
