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
import sv.gob.mined.siges.web.dto.SgExtraccion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroExtraccion;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 9000L)
public class ExtraccionRestClient implements Serializable {

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

    public ExtraccionRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgExtraccion> buscar(FiltroExtraccion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/extracciones/buscar");
        SgExtraccion[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, SgExtraccion[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroExtraccion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/extracciones/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgExtraccion guardar(SgExtraccion estadisticaEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadisticaEstudiante == null) {
            return null;
        }
        if (estadisticaEstudiante.getExtPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/extracciones");
            return restClient.invokePost(webTarget, estadisticaEstudiante, SgExtraccion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/extracciones");
            webTarget = webTarget.path(estadisticaEstudiante.getExtPk().toString());
            return restClient.invokePut(webTarget, estadisticaEstudiante, SgExtraccion.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgExtraccion obtenerPorId(Long estadisticaEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadisticaEstudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/extracciones");
        webTarget = webTarget.path(estadisticaEstudiantePk.toString());
        return restClient.invokeGet(webTarget, SgExtraccion.class);
    }

    public void eliminar(Long estadisticaEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadisticaEstudiantePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/extracciones");
        webTarget = webTarget.path(estadisticaEstudiantePk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long estadisticaEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadisticaEstudiantePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/extracciones/historial");
        webTarget = webTarget.path(estadisticaEstudiantePk.toString());
        RevHistorico[] estadisticaEstudiante = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public <T> T procesarExtraccionesPendientes() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/extracciones/procesarextraccionespendientes");
        return restClient.invokeGet(webTarget, null);
    }

}
