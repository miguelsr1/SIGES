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
import sv.gob.mined.siges.web.dto.SgIncorporacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIncorporacion;

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
public class IncorporacionRestClient implements Serializable {

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
    
    public IncorporacionRestClient() {
    }


    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgIncorporacion> buscar(FiltroIncorporacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/incorporaciones/buscar");
        SgIncorporacion[] incorporacion = restClient.invokePost(webTarget, filtro, SgIncorporacion[].class);
        return Arrays.asList(incorporacion);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroIncorporacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/incorporaciones/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgIncorporacion guardar(SgIncorporacion incorporacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (incorporacion == null) {
            return null;
        }
        if (incorporacion.getIncPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/incorporaciones");
            return restClient.invokePost(webTarget, incorporacion, SgIncorporacion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/incorporaciones");
            webTarget = webTarget.path(incorporacion.getIncPk().toString());
            return restClient.invokePut(webTarget, incorporacion, SgIncorporacion.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgIncorporacion obtenerPorId(Long incorporacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (incorporacionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/incorporaciones");
        webTarget = webTarget.path(incorporacionPk.toString());
        return restClient.invokeGet(webTarget, SgIncorporacion.class);
    }

    public void eliminar(Long incorporacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (incorporacionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/incorporaciones");
        webTarget = webTarget.path(incorporacionPk.toString());
        restClient.invokeDelete(webTarget);
    }


   @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long sedeId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sedeId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/incorporaciones/historial");
        webTarget = webTarget.path(sedeId.toString());
        RevHistorico[] revs = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(revs);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 12000L)
    public SgSede obtenerEnRevision(Long sedeId, Long sedeRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sedeId == null || sedeRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/incorporaciones/revision");
        webTarget = webTarget.path(sedeId.toString());
        webTarget = webTarget.path(sedeRev.toString());
        return restClient.invokeGet(webTarget, SgSede.class);
    }

    

}
