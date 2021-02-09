/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import sv.gob.mined.siges.web.dto.SgSustitucionMiembroOAE;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSustitucionMiembroOAE;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;

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
public class SustitucionMiembroOAERestClient implements Serializable {

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

    public SustitucionMiembroOAERestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgSustitucionMiembroOAE> buscar(FiltroSustitucionMiembroOAE filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sustitucionmiembrooae/buscar");
        SgSustitucionMiembroOAE[] sustitucionMiembroOAE = restClient.invokePost(webTarget, filtro, SgSustitucionMiembroOAE[].class);
        return Arrays.asList(sustitucionMiembroOAE);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroSustitucionMiembroOAE filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sustitucionmiembrooae/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgSustitucionMiembroOAE guardar(SgSustitucionMiembroOAE sustitucionMiembroOAE) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sustitucionMiembroOAE == null) {
            return null;
        }
        if (sustitucionMiembroOAE.getSmoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sustitucionmiembrooae");
            return restClient.invokePost(webTarget, sustitucionMiembroOAE, SgSustitucionMiembroOAE.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sustitucionmiembrooae");
            webTarget = webTarget.path(sustitucionMiembroOAE.getSmoPk().toString());
            return restClient.invokePut(webTarget, sustitucionMiembroOAE, SgSustitucionMiembroOAE.class);
        }
    }
    
    @Timeout(value = 10000L)
    public SgSustitucionMiembroOAE aprobar(SgSustitucionMiembroOAE sustitucionMiembroOAE) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sustitucionMiembroOAE == null) {
            return null;
        }

        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sustitucionmiembrooae/aprobar");
        return restClient.invokePost(webTarget, sustitucionMiembroOAE, SgSustitucionMiembroOAE.class);

    }
    
    public SgSustitucionMiembroOAE rechazar(SgSustitucionMiembroOAE sustitucionMiembroOAE) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sustitucionMiembroOAE == null) {
            return null;
        }

        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sustitucionmiembrooae/rechazar");
        return restClient.invokePost(webTarget, sustitucionMiembroOAE, SgSustitucionMiembroOAE.class);

    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgSustitucionMiembroOAE obtenerPorId(Long sustitucionMiembroOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sustitucionMiembroOAEPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sustitucionmiembrooae");
        webTarget = webTarget.path(sustitucionMiembroOAEPk.toString());
        return restClient.invokeGet(webTarget, SgSustitucionMiembroOAE.class);
    }

    public void eliminar(Long sustitucionMiembroOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sustitucionMiembroOAEPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sustitucionmiembrooae");
        webTarget = webTarget.path(sustitucionMiembroOAEPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long sustitucionMiembroOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sustitucionMiembroOAEPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sustitucionmiembrooae/historial");
        webTarget = webTarget.path(sustitucionMiembroOAEPk.toString());
        RevHistorico[] sustitucionMiembroOAE = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(sustitucionMiembroOAE);
    }

}
