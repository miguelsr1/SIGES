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
import sv.gob.mined.siges.web.dto.SgModuloFormacionDocente;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModuloFormacionDocente;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class ModuloFormacionDocenteRestClient implements Serializable {

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

    public ModuloFormacionDocenteRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgModuloFormacionDocente> buscar(FiltroModuloFormacionDocente filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosformaciondocente/buscar");
        SgModuloFormacionDocente[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgModuloFormacionDocente[].class);
        return new ArrayList<>(Arrays.asList(organizacionesCurricular));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroModuloFormacionDocente filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosformaciondocente/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgModuloFormacionDocente guardar(SgModuloFormacionDocente moduloFormacionDocente) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (moduloFormacionDocente == null) {
            return null;
        }
        if (moduloFormacionDocente.getMfdPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosformaciondocente");
            return restClient.invokePost(webTarget, moduloFormacionDocente, SgModuloFormacionDocente.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosformaciondocente");
            webTarget = webTarget.path(moduloFormacionDocente.getMfdPk().toString());
            return restClient.invokePut(webTarget, moduloFormacionDocente, SgModuloFormacionDocente.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgModuloFormacionDocente obtenerPorId(Long moduloFormacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (moduloFormacionDocentePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosformaciondocente");
        webTarget = webTarget.path(moduloFormacionDocentePk.toString());
        return restClient.invokeGet(webTarget, SgModuloFormacionDocente.class);
    }

    public void eliminar(Long moduloFormacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (moduloFormacionDocentePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosformaciondocente");
        webTarget = webTarget.path(moduloFormacionDocentePk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long moduloFormacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (moduloFormacionDocentePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosformaciondocente/historial");
        webTarget = webTarget.path(moduloFormacionDocentePk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgModuloFormacionDocente obtenerEnRevision(Long moduloFormacionDocentePk, Long moduloFormacionDocenteRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (moduloFormacionDocentePk == null || moduloFormacionDocenteRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosformaciondocente/revision");
        webTarget = webTarget.path(moduloFormacionDocentePk.toString());
        webTarget = webTarget.path(moduloFormacionDocenteRev.toString());
        return restClient.invokeGet(webTarget, SgModuloFormacionDocente.class);
    }

}
