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
import sv.gob.mined.siges.web.dto.SgEscolaridadEstudiante;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.SgCabezalEscolaridadEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEscolaridadEstudiante;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@Traced
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class EscolaridadEstudianteRestClient implements Serializable {

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
    
    public EscolaridadEstudianteRestClient() {
    }


	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgEscolaridadEstudiante> buscar(FiltroEscolaridadEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/escolaridadestudiante/buscar");
        SgEscolaridadEstudiante[] escolaridadEstudiante = restClient.invokePost(webTarget, filtro, SgEscolaridadEstudiante[].class);
        return Arrays.asList(escolaridadEstudiante);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroEscolaridadEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/escolaridadestudiante/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgEscolaridadEstudiante guardar(SgEscolaridadEstudiante escolaridadEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escolaridadEstudiante == null) {
            return null;
        }
        if (escolaridadEstudiante.getEscPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/escolaridadestudiante");
            return restClient.invokePost(webTarget, escolaridadEstudiante, SgEscolaridadEstudiante.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/escolaridadestudiante");
            webTarget = webTarget.path(escolaridadEstudiante.getEscPk().toString());
            return restClient.invokePut(webTarget, escolaridadEstudiante, SgEscolaridadEstudiante.class);
        }
    }
    
    @Timeout(value = 60000L)
    public SgCabezalEscolaridadEstudiante guardarEscolaridades(SgCabezalEscolaridadEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/escolaridadestudiante/guardarEscolaridades");
        SgCabezalEscolaridadEstudiante pruebas = restClient.invokePost(webTarget, filtro, SgCabezalEscolaridadEstudiante.class);
        return pruebas;
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgEscolaridadEstudiante obtenerPorId(Long escolaridadEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escolaridadEstudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/escolaridadestudiante");
        webTarget = webTarget.path(escolaridadEstudiantePk.toString());
        return restClient.invokeGet(webTarget, SgEscolaridadEstudiante.class);
    }

    public void eliminar(Long escolaridadEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escolaridadEstudiantePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/escolaridadestudiante");
        webTarget = webTarget.path(escolaridadEstudiantePk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgEscolaridadEstudiante> obtenerHistorialPorId(Long escolaridadEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (escolaridadEstudiantePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/escolaridadestudiante/historial");
        webTarget = webTarget.path(escolaridadEstudiantePk.toString());
        SgEscolaridadEstudiante[] escolaridadEstudiante = restClient.invokeGet(webTarget, SgEscolaridadEstudiante[].class);
        return Arrays.asList(escolaridadEstudiante);
    }
    

}
