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
import sv.gob.mined.siges.web.dto.SgAutorizacionEdicionPresupuesto;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAutorizacionEdicionPresupuesto;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class AutorizacionEdicionPresupuestoRestClient implements Serializable {

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

    public AutorizacionEdicionPresupuestoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAutorizacionEdicionPresupuesto> buscar(FiltroAutorizacionEdicionPresupuesto filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/autorizaciones_edicion/buscar");
        SgAutorizacionEdicionPresupuesto[] autorizaciones = restClient.invokePost(webTarget, filtro, SgAutorizacionEdicionPresupuesto[].class);
        return Arrays.asList(autorizaciones);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAutorizacionEdicionPresupuesto obtenerPorIdLazy(Long autorizacionesId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (autorizacionesId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/autorizaciones_edicion/lazy");
        webTarget = webTarget.path(autorizacionesId.toString());
        return restClient.invokeGet(webTarget, SgAutorizacionEdicionPresupuesto.class);
    }

    public SgAutorizacionEdicionPresupuesto guardar(SgAutorizacionEdicionPresupuesto autorizacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (autorizacion == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(autorizacion);
        if (autorizacion.getAutPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/autorizaciones_edicion");
            return restClient.invokePost(webTarget, autorizacion, SgAutorizacionEdicionPresupuesto.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/autorizaciones_edicion");
            webTarget = webTarget.path(autorizacion.getAutPk().toString());
            return restClient.invokePut(webTarget, autorizacion, SgAutorizacionEdicionPresupuesto.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAutorizacionEdicionPresupuesto obtenerPorId(Long autorizacionId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (autorizacionId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/autorizaciones_edicion");
        webTarget = webTarget.path(autorizacionId.toString());
        return restClient.invokeGet(webTarget, SgAutorizacionEdicionPresupuesto.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroAutorizacionEdicionPresupuesto filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/autorizaciones_edicion/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public void eliminar(Long autorizacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (autorizacionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/autorizaciones_edicion");
        webTarget = webTarget.path(autorizacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public void eliminarUsuarios(Long presId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (presId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/autorizaciones_edicion/eliminarUsuarios");
        webTarget = webTarget.path(presId.toString());
        restClient.invokeDelete(webTarget);
    }

}
