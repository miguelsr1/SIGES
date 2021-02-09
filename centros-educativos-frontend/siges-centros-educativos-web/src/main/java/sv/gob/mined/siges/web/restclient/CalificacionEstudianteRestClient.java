/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

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
import sv.gob.mined.siges.web.dto.SgCalificacionEstudiante;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionEstudiante;

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
@Timeout(value = 30000L)
public class CalificacionEstudianteRestClient implements Serializable {

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

    public CalificacionEstudianteRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgCalificacionEstudiante> buscar(FiltroCalificacionEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificacionesestudiante/buscar");
        SgCalificacionEstudiante[] calificacionesEstudiante = restClient.invokePost(webTarget, filtro, SgCalificacionEstudiante[].class);
        return new ArrayList(Arrays.asList(calificacionesEstudiante));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCalificacionEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificacionesestudiante/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCalificacionEstudiante guardar(SgCalificacionEstudiante calificacionEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionEstudiante == null) {
            return null;
        }
        if (calificacionEstudiante.getCaePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificacionesestudiante");
            return restClient.invokePost(webTarget, calificacionEstudiante, SgCalificacionEstudiante.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificacionesestudiante");
            webTarget = webTarget.path(calificacionEstudiante.getCaePk().toString());
            return restClient.invokePut(webTarget, calificacionEstudiante, SgCalificacionEstudiante.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCalificacionEstudiante obtenerPorId(Long calificacionEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionEstudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificacionesestudiante");
        webTarget = webTarget.path(calificacionEstudiantePk.toString());
        return restClient.invokeGet(webTarget, SgCalificacionEstudiante.class);
    }

    public void eliminar(Long calificacionEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionEstudiantePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificacionesestudiante");
        webTarget = webTarget.path(calificacionEstudiantePk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<SgCalificacionEstudiante> obtenerHistorialPorId(Long calificacionEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionEstudiantePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificacionesestudiante/historial");
        webTarget = webTarget.path(calificacionEstudiantePk.toString());
        SgCalificacionEstudiante[] calificacionesEstudiante = restClient.invokeGet(webTarget, SgCalificacionEstudiante[].class);
        return Arrays.asList(calificacionesEstudiante);
    }
    
    @Timeout(value = 30000L)
    public void guardarCalificacionesEstudiante(List<SgCalificacionEstudiante> calificacionesEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionesEstudiante == null) {
            return ;
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificacionesestudiante/calificacionesestudiantelist");
        restClient.invokePost(webTarget, calificacionesEstudiante, null);
        
    }

    public SgCalificacionEstudiante convertirEstadoPendiente(SgCalificacionEstudiante calificacionEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionEstudiante == null) {
            return null;
        }
       
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificacionesestudiante/convertirpromocionpendiente");
        return restClient.invokePost(webTarget, calificacionEstudiante, SgCalificacionEstudiante.class);
     
    }
    
}
