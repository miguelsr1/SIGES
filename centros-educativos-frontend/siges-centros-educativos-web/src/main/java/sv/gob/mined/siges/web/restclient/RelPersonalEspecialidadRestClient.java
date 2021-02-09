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
import sv.gob.mined.siges.web.dto.SgRelPersonalEspecialidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelPersonalEspecialidad;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class RelPersonalEspecialidadRestClient implements Serializable {

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

    public RelPersonalEspecialidadRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRelPersonalEspecialidad> buscar(FiltroRelPersonalEspecialidad filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relpersonalespecialidades/buscar");
        SgRelPersonalEspecialidad[] relPersonalEspecialidad = restClient.invokePost(webTarget, filtro, SgRelPersonalEspecialidad[].class);
        return new ArrayList<>(Arrays.asList(relPersonalEspecialidad));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroRelPersonalEspecialidad filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relpersonalespecialidades/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelPersonalEspecialidad guardar(SgRelPersonalEspecialidad relPersonalEspecialidad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relPersonalEspecialidad == null) {
            return null;
        }
        if (relPersonalEspecialidad.getRpePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relpersonalespecialidades");
            return restClient.invokePost(webTarget, relPersonalEspecialidad, SgRelPersonalEspecialidad.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relpersonalespecialidades");
            webTarget = webTarget.path(relPersonalEspecialidad.getRpePk().toString());
            return restClient.invokePut(webTarget, relPersonalEspecialidad, SgRelPersonalEspecialidad.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgRelPersonalEspecialidad obtenerPorId(Long relPersonalEspecialidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relPersonalEspecialidadPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relpersonalespecialidades");
        webTarget = webTarget.path(relPersonalEspecialidadPk.toString());
        return restClient.invokeGet(webTarget, SgRelPersonalEspecialidad.class);
    }

    public void eliminar(Long relPersonalEspecialidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relPersonalEspecialidadPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relpersonalespecialidades");
        webTarget = webTarget.path(relPersonalEspecialidadPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long relPersonalEspecialidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relPersonalEspecialidadPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relpersonalespecialidades/historial");
        webTarget = webTarget.path(relPersonalEspecialidadPk.toString());
        RevHistorico[] relPersonalEspecialidad = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relPersonalEspecialidad);
    }

    public SgRelPersonalEspecialidad obtenerEnRevision(Long relPersonalEspecialidadPk, Long relPersonalEspecialidadRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relPersonalEspecialidadPk == null || relPersonalEspecialidadRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relpersonalespecialidades/revision");
        webTarget = webTarget.path(relPersonalEspecialidadPk.toString());
        webTarget = webTarget.path(relPersonalEspecialidadRev.toString());
        return restClient.invokeGet(webTarget, SgRelPersonalEspecialidad.class);
    }

}
