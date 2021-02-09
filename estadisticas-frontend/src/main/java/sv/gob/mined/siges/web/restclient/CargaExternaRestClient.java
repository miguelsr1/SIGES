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
import sv.gob.mined.siges.web.dto.SgCargaExterna;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.EstGenerica;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCargaExterna;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstadisticas;

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
public class CargaExternaRestClient implements Serializable {

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

    public CargaExternaRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCargaExterna> buscar(FiltroCargaExterna filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/cargasexternas/buscar");
        SgCargaExterna[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, SgCargaExterna[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCargaExterna filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/cargasexternas/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Timeout(value = 35000L)
    public SgCargaExterna guardar(SgCargaExterna estadisticaEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadisticaEstudiante == null) {
            return null;
        }
        if (estadisticaEstudiante.getCarPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/cargasexternas");
            return restClient.invokePost(webTarget, estadisticaEstudiante, SgCargaExterna.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/cargasexternas");
            webTarget = webTarget.path(estadisticaEstudiante.getCarPk().toString());
            return restClient.invokePut(webTarget, estadisticaEstudiante, SgCargaExterna.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCargaExterna obtenerPorId(Long estadisticaEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadisticaEstudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/cargasexternas");
        webTarget = webTarget.path(estadisticaEstudiantePk.toString());
        return restClient.invokeGet(webTarget, SgCargaExterna.class);
    }

    public void eliminar(Long estadisticaEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadisticaEstudiantePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/cargasexternas");
        webTarget = webTarget.path(estadisticaEstudiantePk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long estadisticaEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadisticaEstudiantePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/cargasexternas/historial");
        webTarget = webTarget.path(estadisticaEstudiantePk.toString());
        RevHistorico[] estadisticaEstudiante = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    @Timeout(value = 45000L)
    public List<EstGenerica> obtenerEstadisticaDeCargaExterna(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/cargasexternas/obtenerestadistica");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

}
