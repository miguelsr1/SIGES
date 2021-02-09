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
import sv.gob.mined.siges.web.dto.SgNominaDocente;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativaNoJsonIdentity;
import sv.gob.mined.siges.web.dto.SgUnirPersonal;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class PersonalSedeEducativaRestClient implements Serializable {

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

    public PersonalSedeEducativaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPersonalSedeEducativa> buscar(FiltroPersonalSedeEducativa filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa/buscar");
        SgPersonalSedeEducativa[] lista = restClient.invokePost(webTarget, filtro, SgPersonalSedeEducativa[].class);
        return Arrays.asList(lista);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPersonalSedeEducativaNoJsonIdentity> buscarLite(FiltroPersonalSedeEducativa filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa/buscar");
        SgPersonalSedeEducativaNoJsonIdentity[] lista = restClient.invokePost(webTarget, filtro, SgPersonalSedeEducativaNoJsonIdentity[].class);
        return Arrays.asList(lista);
    }
    
    @Timeout(value = 20000L)
    public SgPersonalSedeEducativa guardar(SgPersonalSedeEducativa docente, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (docente == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(docente);
        ViewIdUtils.clearChildViewIds(docente.getPsePersona());
        if (docente.getPsePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa");
            return restClient.invokePost(webTarget, docente, SgPersonalSedeEducativa.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa");
            webTarget = webTarget.path(docente.getPsePk().toString());
            webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
            return restClient.invokePut(webTarget, docente, SgPersonalSedeEducativa.class);
        }
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgNominaDocente> buscarNominaDocente(FiltroPersonalSedeEducativa filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa/buscarnominadocente");
        SgNominaDocente[] lista = restClient.invokePost(webTarget, filtro, SgNominaDocente[].class);
        return Arrays.asList(lista);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroPersonalSedeEducativa filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgPersonalSedeEducativa obtenerPorId(Long sedeId, Boolean dataSecurity) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sedeId == null) {
            return null;
        }
        WebTarget webTarget = webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa");
        webTarget = webTarget.path(sedeId.toString());
        webTarget = webTarget.queryParam("dataSecurity", dataSecurity);
        return restClient.invokeGet(webTarget, SgPersonalSedeEducativa.class);
    }

    public void eliminar(Long sedeId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sedeId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa");
        webTarget = webTarget.path(sedeId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long sedeId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sedeId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa/historial");
        webTarget = webTarget.path(sedeId.toString());
        RevHistorico[] sede = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(sede);
    }
    public void unirPersonal(SgUnirPersonal dto) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (dto == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa/unir");
        restClient.invokePost(webTarget, dto, null);
    }

    @Timeout(value = 100000L)
    public SgPersonalSedeEducativa obtenerEnRevision(Long sedeId, Long sedeRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sedeId == null || sedeRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa/revision");
        webTarget = webTarget.path(sedeId.toString());
        webTarget = webTarget.path(sedeRev.toString());
        return restClient.invokeGet(webTarget, SgPersonalSedeEducativa.class);
    }
    
    public Boolean validar(SgPersonalSedeEducativa docente) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (docente == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personalsedeeducativa/validar");
        return restClient.invokePost(webTarget, docente, Boolean.class);
    }

}
