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
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.DetalleDescargo;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAfActaDescargo;
import sv.gob.mined.siges.web.dto.SolicitudDescargo;
import sv.gob.mined.siges.web.dto.SgAfDescargo;
import sv.gob.mined.siges.web.dto.SgAfDescargoDetalle;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDescargosDetalle;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 20000L)
public class DescargosRestClient implements Serializable {

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

    public DescargosRestClient() {
    }

    @Timeout(value = 20000L)
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAfDescargo> buscar(FiltroBienesDepreciables filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/buscar");
        SgAfDescargo[] descargos = restClient.invokePost(webTarget, filtro, SgAfDescargo[].class);
        return Arrays.asList(descargos);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroBienesDepreciables filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Timeout(value = 100000L)
    public SgAfDescargo guardar(SolicitudDescargo solicitud) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitud == null) {
            return null;
        }
        if (solicitud.getDescargo().getDesPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos");
            return restClient.invokePost(webTarget, solicitud, SgAfDescargo.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos");
            webTarget = webTarget.path(solicitud.getDescargo().getDesPk().toString());
            return restClient.invokePut(webTarget, solicitud, SgAfDescargo.class);
        }
    }

    @Timeout(value = 100000L)
    public SgAfDescargo guardarActaDescargo(Long descragoId, SgAfActaDescargo entity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (entity == null || descragoId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/actaDescargo");
        webTarget = webTarget.path(descragoId.toString());
        return restClient.invokePut(webTarget, entity, SgAfDescargo.class);
    }
    
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAfDescargo obtenerPorId(Long descargoPk, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (descargoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos");
        webTarget = webTarget.path(descargoPk.toString());
        webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
        return restClient.invokeGet(webTarget, SgAfDescargo.class);
    }

    public void eliminar(SolicitudDescargo solicitud) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitud == null || solicitud.getId() == null || solicitud.getEstado() == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/eliminarSolicitud");
        restClient.invokePost(webTarget, solicitud, null);
    }
    
    public List<RevHistorico> obtenerHistorialPorId(Long descargoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (descargoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/historial");
        webTarget = webTarget.path(descargoPk.toString());
        RevHistorico[] bienes = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(bienes);
    }

    public SgAfDescargo obtenerEnRevision(Long descargoPk, Long descargoRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (descargoPk == null || descargoRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/revision");
        webTarget = webTarget.path(descargoPk.toString());
        webTarget = webTarget.path(descargoRev.toString());
        return restClient.invokeGet(webTarget, SgAfDescargo.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAfDescargoDetalle obtenerDetallePorId(Long descargoPk, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (descargoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/detalle");
        webTarget = webTarget.path(descargoPk.toString());
        webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
        return restClient.invokeGet(webTarget, SgAfDescargoDetalle.class);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAfDescargoDetalle> buscarDetalle(FiltroDescargosDetalle filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/detalle/buscar");
        SgAfDescargoDetalle[] detalle = restClient.invokePost(webTarget, filtro, SgAfDescargoDetalle[].class);
        return Arrays.asList(detalle);
    }
    
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotalDetalle(FiltroDescargosDetalle filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/detalle/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }
 
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotalDetalleDTO(FiltroDescargosDetalle filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/detalle/dto/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<DetalleDescargo> buscarDetalleDTO(FiltroDescargosDetalle filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/detalle/dto/buscar");
        DetalleDescargo[] detalle = restClient.invokePost(webTarget, filtro, DetalleDescargo[].class);
        return Arrays.asList(detalle);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public DetalleDescargo obtenerDetallePorIdDTO(Long descargoPk, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (descargoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/descargos/detalle/dto");
        webTarget = webTarget.path(descargoPk.toString());
        webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
        return restClient.invokeGet(webTarget, DetalleDescargo.class);
    }
}
