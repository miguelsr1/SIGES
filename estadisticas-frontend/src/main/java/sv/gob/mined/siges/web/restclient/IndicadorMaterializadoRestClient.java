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
import sv.gob.mined.siges.web.dto.SgIndicadorMaterializado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.EstGenerica;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstadisticas;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIndicadorMaterializado;

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
public class IndicadorMaterializadoRestClient implements Serializable {

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

    public IndicadorMaterializadoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgIndicadorMaterializado> buscar(FiltroIndicadorMaterializado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/indicadoresmaterializados/buscar");
        SgIndicadorMaterializado[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, SgIndicadorMaterializado[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroIndicadorMaterializado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/indicadoresmaterializados/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Timeout(value = 500000L)
    public void crear(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/indicadoresmaterializados");
        restClient.invokePost(webTarget, filtro, null);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgIndicadorMaterializado obtenerPorId(Long estadisticaEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadisticaEstudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/indicadoresmaterializados");
        webTarget = webTarget.path(estadisticaEstudiantePk.toString());
        return restClient.invokeGet(webTarget, SgIndicadorMaterializado.class);
    }

    public void eliminar(Long indicadorPk, Integer anio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (indicadorPk == null || anio == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/indicadoresmaterializados");
        webTarget = webTarget.path(indicadorPk.toString());
        webTarget = webTarget.path(anio.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long estadisticaEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadisticaEstudiantePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/indicadoresmaterializados/historial");
        webTarget = webTarget.path(estadisticaEstudiantePk.toString());
        RevHistorico[] estadisticaEstudiante = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    @Timeout(value = 45000L)
    public List<EstGenerica> obtenerEstadisticaDeIndicadorMaterializado(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/indicadoresmaterializados/obtenerestadistica");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

}
