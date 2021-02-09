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
import javax.ws.rs.client.Client;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.annotation.CacheResult;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgPrediccionDesercionResponse;
import sv.gob.mined.siges.web.dto.SgUnirEstudiantes;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 30000L)
public class EstudianteRestClient implements Serializable {

    @Inject
    private RestClient restClient;

    private Client client = null;

    @PostConstruct
    public void init() {
        client = restClient.getClient();
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            //client.close();
        }
    }

    public EstudianteRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgEstudiante> buscar(FiltroEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes/buscar");
        SgEstudiante[] sede = restClient.invokePost(webTarget, filtro, SgEstudiante[].class);
        return new ArrayList<>(Arrays.asList(sede));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgEstudiante obtenerPorId(Long estudianteId, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudianteId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes");
        webTarget = webTarget.path(estudianteId.toString());
        webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
        return restClient.invokeGet(webTarget, SgEstudiante.class);
    }

    public SgEstudiante guardar(SgEstudiante estudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiante == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(estudiante);
        ViewIdUtils.clearChildViewIds(estudiante.getEstPersona());
        if (estudiante.getEstPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes");
            return restClient.invokePost(webTarget, estudiante, SgEstudiante.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes");
            webTarget = webTarget.path(estudiante.getEstPk().toString());
            return restClient.invokePut(webTarget, estudiante, SgEstudiante.class);
        }
    }

    public void unirEstudiantes(SgUnirEstudiantes dto) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (dto == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes/unir");
        restClient.invokePost(webTarget, dto, null);
    }

    public void actualizarServicioSocial(List<SgEstudiante> estudiantes) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantes == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes/actualizarServicioSocial");
        restClient.invokePut(webTarget, estudiantes, null);
    }

    public Boolean validar(SgEstudiante estudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiante == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes/validar");
        return restClient.invokePost(webTarget, estudiante, Boolean.class);
    }

    @Timeout(value = 300000L)
    public void eliminar(Long estudianteId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudianteId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes");
        webTarget = webTarget.path(estudianteId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long estudianteId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudianteId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes/historial");
        webTarget = webTarget.path(estudianteId.toString());
        RevHistorico[] sede = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(sede);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgEstudiante obtenerEnRevision(Long estudiantePk, Long estudianteRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null || estudianteRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes/revision");
        webTarget = webTarget.path(estudiantePk.toString());
        webTarget = webTarget.path(estudianteRev.toString());
        return restClient.invokeGet(webTarget, SgEstudiante.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.PROBABILIDAD_DESERCION_ESTUDIANTE_CACHE)
    public SgPrediccionDesercionResponse prediccionDesercion(Long estudianteId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudianteId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiantes/predicciondesercion");
        webTarget = webTarget.path(estudianteId.toString());
        return restClient.invokeGet(webTarget, SgPrediccionDesercionResponse.class);
    }

}
