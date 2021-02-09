/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
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
import sv.gob.mined.siges.web.beans.PresupuestoEscolarEdicionBean;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 30000L)
public class PresupuestoEscolarRestCliente implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName());

    @Inject
    @Named("userToken")
    private String userToken;

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

    public PresupuestoEscolarRestCliente() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPresupuestoEscolar> buscar(FiltroPresupuestoEscolar filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares/buscar");
        SgPresupuestoEscolar[] pres = restClient.invokePost(webTarget, filtro, SgPresupuestoEscolar[].class);
        return new ArrayList<>(Arrays.asList(pres));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroPresupuestoEscolar filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgPresupuestoEscolar obtenerPorId(Long presupuestoEscolarId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (presupuestoEscolarId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares");
        webTarget = webTarget.path(presupuestoEscolarId.toString());
        return restClient.invokeGet(webTarget, SgPresupuestoEscolar.class);
    }

    public SgPresupuestoEscolar guardar(SgPresupuestoEscolar presupuestoescolar) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (presupuestoescolar == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(presupuestoescolar);
        ViewIdUtils.clearChildViewIds(presupuestoescolar.getPesHabilitado());
        if (presupuestoescolar.getPesPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares");
            return restClient.invokePost(webTarget, presupuestoescolar, SgPresupuestoEscolar.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares");
            webTarget = webTarget.path(presupuestoescolar.getPesPk().toString());
            return restClient.invokePut(webTarget, presupuestoescolar, SgPresupuestoEscolar.class);
        }
    }

    public void eliminar(Long presupuestoEscolarId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (presupuestoEscolarId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares");
        webTarget = webTarget.path(presupuestoEscolarId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long presupuestoEscolarId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (presupuestoEscolarId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares/historial");
        webTarget = webTarget.path(presupuestoEscolarId.toString());
        RevHistorico[] presupuesto = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(presupuesto);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgPresupuestoEscolar obtenerEnRevision(Long presupuestoEscolarPk, Long presupuestoEscolarRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (presupuestoEscolarPk == null || presupuestoEscolarRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares/revision");
        webTarget = webTarget.path(presupuestoEscolarPk.toString());
        webTarget = webTarget.path(presupuestoEscolarRev.toString());
        return restClient.invokeGet(webTarget, SgPresupuestoEscolar.class);
    }

    public SgPresupuestoEscolar prepararAnulacion(SgPresupuestoEscolar presupuestoescolar) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (presupuestoescolar == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(presupuestoescolar);
        ViewIdUtils.clearChildViewIds(presupuestoescolar.getPesHabilitado());
        if (presupuestoescolar.getPesPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares/prepararAnulacion");
            return restClient.invokePost(webTarget, presupuestoescolar, SgPresupuestoEscolar.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares/prepararAnulacion");
            webTarget = webTarget.path(presupuestoescolar.getPesPk().toString());
            return restClient.invokePut(webTarget, presupuestoescolar, SgPresupuestoEscolar.class);
        }
    }

    public void eliminandoMovEditados(Long presupuestoEscolarId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (presupuestoEscolarId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares/eliminandoMovEditados");
        webTarget = webTarget.path(presupuestoEscolarId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgSede> buscarFinanzas(Long filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares/buscarFinanzas");
        SgSede[] sede = restClient.invokePost(webTarget, filtro, SgSede[].class);
        return new ArrayList<>(Arrays.asList(sede));
    }
}
