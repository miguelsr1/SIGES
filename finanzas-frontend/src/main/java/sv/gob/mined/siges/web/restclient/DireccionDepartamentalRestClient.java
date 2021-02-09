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
import sv.gob.mined.siges.web.dto.SgDireccionDepartamental;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDireccionDepartamental;

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
@Timeout(value = 3000L)
public class DireccionDepartamentalRestClient implements Serializable {
    
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

    public DireccionDepartamentalRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgDireccionDepartamental> buscar(FiltroDireccionDepartamental filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/direcciondepartamental/buscar");
        SgDireccionDepartamental[] direccionDepartamental = restClient.invokePost(webTarget, filtro, SgDireccionDepartamental[].class);
        return Arrays.asList(direccionDepartamental);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroDireccionDepartamental filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/direcciondepartamental/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgDireccionDepartamental guardar(SgDireccionDepartamental direccionDepartamental) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (direccionDepartamental == null) {
            return null;
        }
        if (direccionDepartamental.getDedPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/direcciondepartamental");
            return restClient.invokePost(webTarget, direccionDepartamental, SgDireccionDepartamental.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/direcciondepartamental");
            webTarget = webTarget.path(direccionDepartamental.getDedPk().toString());
            return restClient.invokePut(webTarget, direccionDepartamental, SgDireccionDepartamental.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgDireccionDepartamental obtenerPorId(Long direccionDepartamentalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (direccionDepartamentalPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/direcciondepartamental");
        webTarget = webTarget.path(direccionDepartamentalPk.toString());
        return restClient.invokeGet(webTarget, SgDireccionDepartamental.class);
    }

    public void eliminar(Long direccionDepartamentalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (direccionDepartamentalPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/direcciondepartamental");
        webTarget = webTarget.path(direccionDepartamentalPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long direccionDepartamentalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (direccionDepartamentalPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/direcciondepartamental/historial");
        webTarget = webTarget.path(direccionDepartamentalPk.toString());
        RevHistorico[] direccionDepartamental = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(direccionDepartamental);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgDireccionDepartamental obtenerEnRevision(Long direccionDepPk, Long direccionDepRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (direccionDepPk == null || direccionDepRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/direcciondepartamental/revision");
        webTarget = webTarget.path(direccionDepPk.toString());
        webTarget = webTarget.path(direccionDepRev.toString());
        return restClient.invokeGet(webTarget, SgDireccionDepartamental.class);
    }

}
