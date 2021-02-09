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
import sv.gob.mined.siges.web.dto.SgCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionDiplomadoEstudiante;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@Traced
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class CalificacionDiplomadoEstudianteRestClient implements Serializable {

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
    
    public CalificacionDiplomadoEstudianteRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCalificacionDiplomadoEstudiante> buscar(FiltroCalificacionDiplomadoEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomadoestudiante/buscar");
        SgCalificacionDiplomadoEstudiante[] calificacionDiplomadoEstudiante = restClient.invokePost(webTarget, filtro, SgCalificacionDiplomadoEstudiante[].class);
        return Arrays.asList(calificacionDiplomadoEstudiante);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCalificacionDiplomadoEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomadoestudiante/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCalificacionDiplomadoEstudiante guardar(SgCalificacionDiplomadoEstudiante calificacionDiplomadoEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionDiplomadoEstudiante == null ) {
            return null;
        }
        if (calificacionDiplomadoEstudiante.getCdePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomadoestudiante");
            return restClient.invokePost(webTarget, calificacionDiplomadoEstudiante, SgCalificacionDiplomadoEstudiante.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomadoestudiante");
            webTarget = webTarget.path(calificacionDiplomadoEstudiante.getCdePk().toString());
            return restClient.invokePut(webTarget, calificacionDiplomadoEstudiante, SgCalificacionDiplomadoEstudiante.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCalificacionDiplomadoEstudiante obtenerPorId(Long calificacionDiplomadoEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionDiplomadoEstudiantePk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomadoestudiante");
        webTarget = webTarget.path(calificacionDiplomadoEstudiantePk.toString());
        return restClient.invokeGet(webTarget, SgCalificacionDiplomadoEstudiante.class);
    }

    public void eliminar(Long calificacionDiplomadoEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionDiplomadoEstudiantePk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomadoestudiante");
        webTarget = webTarget.path(calificacionDiplomadoEstudiantePk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCalificacionDiplomadoEstudiante> obtenerHistorialPorId(Long calificacionDiplomadoEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionDiplomadoEstudiantePk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomadoestudiante/historial");
        webTarget = webTarget.path(calificacionDiplomadoEstudiantePk.toString());
        SgCalificacionDiplomadoEstudiante[] calificacionDiplomadoEstudiante = restClient.invokeGet(webTarget, SgCalificacionDiplomadoEstudiante[].class);
        return Arrays.asList(calificacionDiplomadoEstudiante);
    }
    

}
