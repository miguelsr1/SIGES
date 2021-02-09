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
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDesembolso;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.DesembolsoDDE;
import sv.gob.mined.siges.web.dto.DesembolsoMovimiento;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgDesembolso;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 30000L)
public class DesembolsoRestClient implements Serializable {
    
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

    public DesembolsoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgDesembolso> buscar(FiltroDesembolso filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsos/buscar");
        SgDesembolso[] mov = restClient.invokePost(webTarget, filtro, SgDesembolso[].class);
        return new ArrayList<>(Arrays.asList(mov));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroDesembolso filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgDesembolso obtenerPorId(Long desembolsoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsoId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsos");
        webTarget = webTarget.path(desembolsoId.toString());
        return restClient.invokeGet(webTarget, SgDesembolso.class);
    }

    public SgDesembolso guardar(SgDesembolso desembolsoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsoId == null) {
            return null;
        }

        if (desembolsoId.getDsbPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsos");
            return restClient.invokePost(webTarget, desembolsoId, SgDesembolso.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsos");
            webTarget = webTarget.path(desembolsoId.getDsbPk().toString());
            return restClient.invokePut(webTarget, desembolsoId, SgDesembolso.class);
        }
    }
    
    public SgDesembolso guardarConDetalle(DesembolsoDDE desDde) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desDde == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsos/guardar");
        return restClient.invokePost(webTarget, desDde, SgDesembolso.class);
    }
    
    public SgDesembolso crearMovimientos(DesembolsoMovimiento des) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (des == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsos/crear/movimientos");
        return restClient.invokePost(webTarget, des, SgDesembolso.class);
    }

    public void eliminar(Long desembolsoIdId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsoIdId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsos");
        webTarget = webTarget.path(desembolsoIdId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long desembolsoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsoId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsos/historial");
        webTarget = webTarget.path(desembolsoId.toString());
        RevHistorico[] pres = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(pres);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgDesembolso obtenerEnRevision(Long dsbPk, Long dsbRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (dsbPk == null || dsbRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsos/revision");
        webTarget = webTarget.path(dsbPk.toString());
        webTarget = webTarget.path(dsbRev.toString());
        return restClient.invokeGet(webTarget, SgDesembolso.class);
    }

}
