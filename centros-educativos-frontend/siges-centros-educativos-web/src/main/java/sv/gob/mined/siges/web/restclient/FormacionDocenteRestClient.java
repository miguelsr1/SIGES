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
import sv.gob.mined.siges.web.dto.SgFormacionDocente;
import sv.gob.mined.siges.web.dto.SgOpcion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFormacionDocente;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class FormacionDocenteRestClient implements Serializable {

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

    public FormacionDocenteRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgFormacionDocente> buscar(FiltroFormacionDocente filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/formacionesdocente/buscar");
        SgFormacionDocente[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgFormacionDocente[].class);
        return new ArrayList<>(Arrays.asList(organizacionesCurricular));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroFormacionDocente filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/formacionesdocente/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgFormacionDocente guardar(SgFormacionDocente formacionDocente) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formacionDocente == null) {
            return null;
        }
        if (formacionDocente.getFdoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/formacionesdocente");
            return restClient.invokePost(webTarget, formacionDocente, SgFormacionDocente.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/formacionesdocente");
            webTarget = webTarget.path(formacionDocente.getFdoPk().toString());
            return restClient.invokePut(webTarget, formacionDocente, SgFormacionDocente.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgFormacionDocente obtenerPorId(Long formacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formacionDocentePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/formacionesdocente");
        webTarget = webTarget.path(formacionDocentePk.toString());
        return restClient.invokeGet(webTarget, SgFormacionDocente.class);
    }

    public void eliminar(Long formacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formacionDocentePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/formacionesdocente");
        webTarget = webTarget.path(formacionDocentePk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long formacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formacionDocentePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/formacionesdocente/historial");
        webTarget = webTarget.path(formacionDocentePk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgOpcion obtenerEnRevision(Long formacionDocentePk, Long formacionDocenteRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formacionDocentePk == null || formacionDocenteRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/formacionesdocente/revision");
        webTarget = webTarget.path(formacionDocentePk.toString());
        webTarget = webTarget.path(formacionDocenteRev.toString());
        return restClient.invokeGet(webTarget, SgOpcion.class);
    }

}
