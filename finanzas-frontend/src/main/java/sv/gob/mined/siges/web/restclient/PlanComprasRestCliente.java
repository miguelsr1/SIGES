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
import sv.gob.mined.siges.web.dto.SgEncabezadoPlanCompras;
import sv.gob.mined.siges.web.dto.SgPlanCompras;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEncabezadoPlanCompra;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlanCompras;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 30000L)
public class PlanComprasRestCliente implements Serializable {

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

    public PlanComprasRestCliente() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPlanCompras> buscar(FiltroPlanCompras filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/planescompras/buscar");
        SgPlanCompras[] com = restClient.invokePost(webTarget, filtro, SgPlanCompras[].class);
        return new ArrayList<>(Arrays.asList(com));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroPlanCompras filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/planescompras/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgPlanCompras obtenerPorId(Long planComprasId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (planComprasId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/presupuestosescolares");
        webTarget = webTarget.path(planComprasId.toString());
        return restClient.invokeGet(webTarget, SgPlanCompras.class);
    }

    public SgPlanCompras guardar(SgPlanCompras plancompra) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plancompra == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(plancompra);

        if (plancompra.getComPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/planescompras");
            return restClient.invokePost(webTarget, plancompra, SgPlanCompras.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/planescompras");
            webTarget = webTarget.path(plancompra.getComPk().toString());
            return restClient.invokePut(webTarget, plancompra, SgPlanCompras.class);
        }
    }

    public void eliminar(Long planComprasId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (planComprasId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/planescompras");
        webTarget = webTarget.path(planComprasId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long planComprasId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (planComprasId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/planescompras/historial");
        webTarget = webTarget.path(planComprasId.toString());
        RevHistorico[] comupuesto = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(comupuesto);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgPlanCompras obtenerEnRevision(Long planComprasPk, Long compuestoPlanComprasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (planComprasPk == null || compuestoPlanComprasPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/planescompras/revision");
        webTarget = webTarget.path(planComprasPk.toString());
        webTarget = webTarget.path(compuestoPlanComprasPk.toString());
        return restClient.invokeGet(webTarget, SgPlanCompras.class);
    }

    public SgEncabezadoPlanCompras guardarPlan(SgEncabezadoPlanCompras encabezado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (encabezado == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(encabezado);

        if (encabezado.getPlaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/planescompras/crear_plan");
            return restClient.invokePost(webTarget, encabezado, SgEncabezadoPlanCompras.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/planescompras/crear_plan");
            webTarget = webTarget.path(encabezado.getPlaPk().toString());
            return restClient.invokePut(webTarget, encabezado, SgEncabezadoPlanCompras.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgEncabezadoPlanCompras> buscarPlan(FiltroEncabezadoPlanCompra filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/planescompras/buscar_plan");
        SgEncabezadoPlanCompras[] com = restClient.invokePost(webTarget, filtro, SgEncabezadoPlanCompras[].class);
        return new ArrayList<>(Arrays.asList(com));
    }

}
