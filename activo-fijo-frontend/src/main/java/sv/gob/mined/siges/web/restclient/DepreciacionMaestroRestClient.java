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
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAfDepreciacionMaestro;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;

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
public class DepreciacionMaestroRestClient implements Serializable {

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

    public DepreciacionMaestroRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAfDepreciacionMaestro> buscar(FiltroBienesDepreciables filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/maestrodepreciacion/buscar");
        SgAfDepreciacionMaestro[] maestroList = restClient.invokePost(webTarget, filtro, SgAfDepreciacionMaestro[].class);
        return Arrays.asList(maestroList);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroBienesDepreciables filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/maestrodepreciacion/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAfDepreciacionMaestro guardar(SgAfDepreciacionMaestro maDepreciacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (maDepreciacion == null) {
            return null;
        }
        if (maDepreciacion.getDmaPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/maestrodepreciacion");
            return restClient.invokePost(webTarget, maDepreciacion, SgAfDepreciacionMaestro.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/maestrodepreciacion");
            webTarget = webTarget.path(maDepreciacion.getDmaPk().toString());
            return restClient.invokePut(webTarget, maDepreciacion, SgAfDepreciacionMaestro.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAfDepreciacionMaestro obtenerPorId(Long maDepPk, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (maDepPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/maestrodepreciacion");
        webTarget = webTarget.path(maDepPk.toString());
        webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
        return restClient.invokeGet(webTarget, SgAfDepreciacionMaestro.class);
    }

    public void eliminar(Long maDepPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (maDepPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/maestrodepreciacion");
        webTarget = webTarget.path(maDepPk.toString());
        restClient.invokeDelete(webTarget);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public void procesarTodas()throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/maestrodepreciacion/procesartareas");
        restClient.invokePost(webTarget, null, null);
    }        
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public void procesarTarea(SgAfDepreciacionMaestro maestro)throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/maestrodepreciacion/procesartarea");
        restClient.invokePost(webTarget, maestro, null);
    } 
    
    public List<RevHistorico> obtenerHistorialPorId(Long maDepPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (maDepPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/maestrodepreciacion/historial");
        webTarget = webTarget.path(maDepPk.toString());
        RevHistorico[] bienes = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(bienes);
    }

    public SgAfDepreciacionMaestro obtenerEnRevision(Long maDepPk, Long maDepRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (maDepPk == null || maDepRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/maestrodepreciacion/revision");
        webTarget = webTarget.path(maDepPk.toString());
        webTarget = webTarget.path(maDepRev.toString());
        return restClient.invokeGet(webTarget, SgAfDepreciacionMaestro.class);
    }

}
