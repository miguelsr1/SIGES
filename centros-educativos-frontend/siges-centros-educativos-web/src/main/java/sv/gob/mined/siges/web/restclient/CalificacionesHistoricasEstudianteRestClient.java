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
import sv.gob.mined.siges.web.dto.SgCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgArchivo;

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
@Timeout(value = 30000L)
public class CalificacionesHistoricasEstudianteRestClient implements Serializable {

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

    public CalificacionesHistoricasEstudianteRestClient() {
    }


    @Timeout(value = 5000000L)
    public List<SgCalificacionesHistoricasEstudiante> buscar(FiltroCalificacionesHistoricasEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificacioneshistoricasestudiante/buscar");
        SgCalificacionesHistoricasEstudiante[] calificacionesHistoricasEstudiante = restClient.invokePost(webTarget, filtro, SgCalificacionesHistoricasEstudiante[].class);
        return Arrays.asList(calificacionesHistoricasEstudiante);
    }

    @Timeout(value = 5000000L)
    public Long buscarTotal(FiltroCalificacionesHistoricasEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificacioneshistoricasestudiante/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCalificacionesHistoricasEstudiante guardar(SgCalificacionesHistoricasEstudiante calificacionesHistoricasEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionesHistoricasEstudiante == null ) {
            return null;
        }
        if (calificacionesHistoricasEstudiante.getChePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificacioneshistoricasestudiante");
            return restClient.invokePost(webTarget, calificacionesHistoricasEstudiante, SgCalificacionesHistoricasEstudiante.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificacioneshistoricasestudiante");
            webTarget = webTarget.path(calificacionesHistoricasEstudiante.getChePk().toString());
            return restClient.invokePut(webTarget, calificacionesHistoricasEstudiante, SgCalificacionesHistoricasEstudiante.class);
        }
    }
    
    @Timeout(value = 30000L)
    public void guardarCalificacionesEstudiante(List<SgCalificacionesHistoricasEstudiante> calificacionesEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionesEstudiante == null) {
            return ;
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificacioneshistoricasestudiante/calificacionesestudiantelist");
        restClient.invokePost(webTarget, calificacionesEstudiante, null);
        
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCalificacionesHistoricasEstudiante obtenerPorId(Long calificacionesHistoricasEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionesHistoricasEstudiantePk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificacioneshistoricasestudiante");
        webTarget = webTarget.path(calificacionesHistoricasEstudiantePk.toString());
        return restClient.invokeGet(webTarget, SgCalificacionesHistoricasEstudiante.class);
    }

    public void eliminar(Long calificacionesHistoricasEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionesHistoricasEstudiantePk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificacioneshistoricasestudiante");
        webTarget = webTarget.path(calificacionesHistoricasEstudiantePk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long calificacionesHistoricasEstudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionesHistoricasEstudiantePk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificacioneshistoricasestudiante/historial");
        webTarget = webTarget.path(calificacionesHistoricasEstudiantePk.toString());
        RevHistorico[] calificacionesHistoricasEstudiante = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(calificacionesHistoricasEstudiante);
    }
    
    @Timeout(value = 300000L)
    public void editarEstudiante(SgCalificacionesHistoricasEstudiante calificacionesHistoricasEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionesHistoricasEstudiante == null ) {
            return;
        }
     
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificacioneshistoricasestudiante/editarEstudiante");
            restClient.invokePost(webTarget, calificacionesHistoricasEstudiante, null);
      
    }
    @Timeout(value = 300000L)
    public void calificarConArchivo(SgArchivo calificacionesHistoricasEstudiante) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionesHistoricasEstudiante == null ) {
            return ;
        }
       
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificacioneshistoricasestudiante/calificarConArchivo");
            restClient.invokePost(webTarget, calificacionesHistoricasEstudiante, null);
       
    }
    

}
