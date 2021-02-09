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
import sv.gob.mined.siges.web.dto.SgServicioEducativo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioEducativo;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 14000L)
public class ServicioEducativoRestClient implements Serializable {

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

    public ServicioEducativoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgServicioEducativo> buscar(FiltroServicioEducativo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/servicioseducativos/buscar");
        SgServicioEducativo[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgServicioEducativo[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroServicioEducativo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/servicioseducativos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgServicioEducativo guardar(SgServicioEducativo servicioEducativo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioEducativo == null) {
            return null;
        }
        if (servicioEducativo.getSduPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/servicioseducativos");
            return restClient.invokePost(webTarget, servicioEducativo, SgServicioEducativo.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/servicioseducativos");
            webTarget = webTarget.path(servicioEducativo.getSduPk().toString());
            return restClient.invokePut(webTarget, servicioEducativo, SgServicioEducativo.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgServicioEducativo obtenerPorId(Long servicioEducativoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioEducativoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/servicioseducativos");
        webTarget = webTarget.path(servicioEducativoPk.toString());
        return restClient.invokeGet(webTarget, SgServicioEducativo.class);
    }

    public void eliminar(Long servicioEducativoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioEducativoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/servicioseducativos");
        webTarget = webTarget.path(servicioEducativoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long servicioEducativoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioEducativoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/servicioseducativos/historial");
        webTarget = webTarget.path(servicioEducativoPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgServicioEducativo obtenerEnRevision(Long servicioEducativoPk, Long servicioEducativoRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioEducativoPk == null || servicioEducativoRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/servicioseducativos/revision");
        webTarget = webTarget.path(servicioEducativoPk.toString());
        webTarget = webTarget.path(servicioEducativoRev.toString());
        return restClient.invokeGet(webTarget, SgServicioEducativo.class);
    }

}
