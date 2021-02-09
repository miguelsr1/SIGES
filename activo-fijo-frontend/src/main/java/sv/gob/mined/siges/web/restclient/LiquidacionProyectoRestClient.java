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
import sv.gob.mined.siges.web.dto.SgAfLiquidacionProyecto;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacionProyectos;

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
@Timeout(value = 10000L)
public class LiquidacionProyectoRestClient implements Serializable {

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

    public LiquidacionProyectoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAfLiquidacionProyecto> buscar(FiltroLiquidacionProyectos filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/liquidacionproyectos/buscar");
        SgAfLiquidacionProyecto[] maestroList = restClient.invokePost(webTarget, filtro, SgAfLiquidacionProyecto[].class);
        return Arrays.asList(maestroList);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroLiquidacionProyectos filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/liquidacionproyectos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAfLiquidacionProyecto guardar(SgAfLiquidacionProyecto liqProyecto) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liqProyecto == null) {
            return null;
        }
        if (liqProyecto.getLprPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/liquidacionproyectos");
            return restClient.invokePost(webTarget, liqProyecto, SgAfLiquidacionProyecto.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/liquidacionproyectos");
            webTarget = webTarget.path(liqProyecto.getLprPk().toString());
            return restClient.invokePut(webTarget, liqProyecto, SgAfLiquidacionProyecto.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAfLiquidacionProyecto obtenerPorId(Long liqProyectoPk, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liqProyectoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/liquidacionproyectos");
        webTarget = webTarget.path(liqProyectoPk.toString());
        webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
        return restClient.invokeGet(webTarget, SgAfLiquidacionProyecto.class);
    }

    public void eliminar(Long liqProyectoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liqProyectoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/liquidacionproyectos");
        webTarget = webTarget.path(liqProyectoPk.toString());
        restClient.invokeDelete(webTarget);
    }      
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public void procesarTodas()throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/liquidacionproyectos/procesartareas");
        restClient.invokePost(webTarget, null, null);
    }        
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public void procesarTarea(SgAfLiquidacionProyecto maestro)throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/liquidacionproyectos/procesartarea");
        restClient.invokePost(webTarget, maestro, null);
    } 
    
    public List<RevHistorico> obtenerHistorialPorId(Long liqProyectoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liqProyectoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/liquidacionproyectos/historial");
        webTarget = webTarget.path(liqProyectoPk.toString());
        RevHistorico[] bienes = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(bienes);
    }

    public SgAfLiquidacionProyecto obtenerEnRevision(Long liqProyectoPk, Long liqProyectoRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (liqProyectoPk == null || liqProyectoRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/liquidacionproyectos/revision");
        webTarget = webTarget.path(liqProyectoPk.toString());
        webTarget = webTarget.path(liqProyectoRev.toString());
        return restClient.invokeGet(webTarget, SgAfLiquidacionProyecto.class);
    }

}
