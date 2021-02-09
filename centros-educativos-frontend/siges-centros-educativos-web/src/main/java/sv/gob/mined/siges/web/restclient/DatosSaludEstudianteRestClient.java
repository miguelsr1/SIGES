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
import sv.gob.mined.siges.web.dto.SgDatoSaludEstudiante;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDatoSaludEstudiante;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class DatosSaludEstudianteRestClient implements Serializable {

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

    public DatosSaludEstudianteRestClient() {
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgDatoSaludEstudiante> buscar(FiltroDatoSaludEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/datosaludestudiante/buscar");
        SgDatoSaludEstudiante[] datosSaludEstudiante = restClient.invokePost(webTarget, filtro, SgDatoSaludEstudiante[].class);
        return Arrays.asList(datosSaludEstudiante);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroDatoSaludEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/datosaludestudiante/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgDatoSaludEstudiante guardar(SgDatoSaludEstudiante datoSaludEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (datoSaludEstudiante == null) {
            return null;
        }
        if (datoSaludEstudiante.getDsePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/datosaludestudiante");
            return restClient.invokePost(webTarget, datoSaludEstudiante, SgDatoSaludEstudiante.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/datosaludestudiante");
            webTarget = webTarget.path(datoSaludEstudiante.getDsePk().toString());
            return restClient.invokePut(webTarget, datoSaludEstudiante, SgDatoSaludEstudiante.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgDatoSaludEstudiante obtenerPorId(Long datoSaludEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (datoSaludEstudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/datosaludestudiante");
        webTarget = webTarget.path(datoSaludEstudiantePk.toString());
        return restClient.invokeGet(webTarget, SgDatoSaludEstudiante.class);
    }

    public void eliminar(Long datoSaludEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (datoSaludEstudiantePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/datosaludestudiante");
        webTarget = webTarget.path(datoSaludEstudiantePk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long datoSaludEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (datoSaludEstudiantePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/datosaludestudiante/historial");
        webTarget = webTarget.path(datoSaludEstudiantePk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }
}
