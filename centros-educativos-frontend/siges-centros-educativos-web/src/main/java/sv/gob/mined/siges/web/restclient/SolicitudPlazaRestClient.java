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
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgSolicitudPlaza;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudPlaza;

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
public class SolicitudPlazaRestClient implements Serializable {

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

    public SolicitudPlazaRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgSolicitudPlaza> buscar(FiltroSolicitudPlaza filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudesplazas/buscar");
        SgSolicitudPlaza[] plaza = restClient.invokePost(webTarget, filtro, SgSolicitudPlaza[].class);
        return Arrays.asList(plaza);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroSolicitudPlaza filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudesplazas/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgSolicitudPlaza guardar(SgSolicitudPlaza plaza) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plaza == null) {
            return null;
        }
        if (plaza.getSplPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudesplazas");
            return restClient.invokePost(webTarget, plaza, SgSolicitudPlaza.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudesplazas");
            webTarget = webTarget.path(plaza.getSplPk().toString());
            return restClient.invokePut(webTarget, plaza, SgSolicitudPlaza.class);
        }
    }
    
    public SgSolicitudPlaza publicarSolicitud(SgSolicitudPlaza plaza) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plaza == null) {
            return null;
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudesplazas/publicar");
        return restClient.invokePost(webTarget, plaza, SgSolicitudPlaza.class);
        
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgSolicitudPlaza obtenerPorId(Long plazaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plazaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudesplazas");
        webTarget = webTarget.path(plazaPk.toString());
        return restClient.invokeGet(webTarget, SgSolicitudPlaza.class);
    }

    public void eliminar(Long plazaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plazaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudesplazas");
        webTarget = webTarget.path(plazaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long plazaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plazaPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudesplazas/historial");
        webTarget = webTarget.path(plazaPk.toString());
        RevHistorico[] plaza = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(plaza);
    }

    public SgSolicitudPlaza obtenerEnRevision(Long plazaPk, Long plazaRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plazaPk == null || plazaRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudesplazas/revision");
        webTarget = webTarget.path(plazaPk.toString());
        webTarget = webTarget.path(plazaRev.toString());
        return restClient.invokeGet(webTarget, SgSolicitudPlaza.class);
    }

}
