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
import sv.gob.mined.siges.web.dto.SgDetallePlanEscolar;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDetallePlanEscolar;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class DetallePlanEscolarRestClient implements Serializable {

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

    public DetallePlanEscolarRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgDetallePlanEscolar obtenerPorId(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detalleplanescolar");
        webTarget = webTarget.path(personaId.toString());
        return restClient.invokeGet(webTarget, SgDetallePlanEscolar.class);
    }

    public SgDetallePlanEscolar guardar(SgDetallePlanEscolar persona) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (persona == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(persona);
        if (persona.getDpePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detalleplanescolar");
            return restClient.invokePost(webTarget, persona, SgDetallePlanEscolar.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detalleplanescolar");
            webTarget = webTarget.path(persona.getDpePk().toString());
            return restClient.invokePut(webTarget, persona, SgDetallePlanEscolar.class);
        }
    }

    public void eliminar(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detalleplanescolar");
        webTarget = webTarget.path(personaId.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detalleplanescolar/historial");
        webTarget = webTarget.path(personaId.toString());
        RevHistorico[] sede = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(sede);
    }

    @Timeout(value = 20000L)
    public SgDetallePlanEscolar obtenerEnRevision(Long personaPk, Long personaRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaPk == null || personaRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detalleplanescolar/revision");
        webTarget = webTarget.path(personaPk.toString());
        webTarget = webTarget.path(personaRev.toString());
        return restClient.invokeGet(webTarget, SgDetallePlanEscolar.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgDetallePlanEscolar> buscar(FiltroDetallePlanEscolar filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detalleplanescolar/buscar");
        SgDetallePlanEscolar[] detallePlanEscolar = restClient.invokePost(webTarget, filtro, SgDetallePlanEscolar[].class);
        return Arrays.asList(detallePlanEscolar);
    }
}
