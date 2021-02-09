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
import sv.gob.mined.siges.web.dto.SgProyectoInstEstudiante;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyectoInstEstudiante;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 998000L)
public class ProyectoInstEstudianteRestClient implements Serializable {

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

    public ProyectoInstEstudianteRestClient() {
    }

    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgProyectoInstEstudiante> buscar(FiltroProyectoInstEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstestudiantes/buscar");
        SgProyectoInstEstudiante[] lista = restClient.invokePost(webTarget, filtro, SgProyectoInstEstudiante[].class);
        return Arrays.asList(lista);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroProyectoInstEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstestudiantes/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgProyectoInstEstudiante obtenerPorId(Long proyectoInstEstudianteId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstEstudianteId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstestudiantes");
        webTarget = webTarget.path(proyectoInstEstudianteId.toString());
        return restClient.invokeGet(webTarget, SgProyectoInstEstudiante.class);
    }

    public SgProyectoInstEstudiante guardar(SgProyectoInstEstudiante proyectoInstitucionalSede) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstitucionalSede == null) {
            return null;
        }
        if (proyectoInstitucionalSede.getPiePk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstestudiantes");
            return restClient.invokePost(webTarget, proyectoInstitucionalSede, SgProyectoInstEstudiante.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstestudiantes");
            webTarget = webTarget.path(proyectoInstitucionalSede.getPiePk().toString());
            return restClient.invokePut(webTarget, proyectoInstitucionalSede, SgProyectoInstEstudiante.class);
        }
    }
    
    
    public void guardarEstudiantes(SgProyectoInstEstudiante[] estudiantes) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantes == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstestudiantes/guardarlista");
        restClient.invokePost(webTarget, estudiantes, null);
    }


    public void eliminar(Long proyectoInstEstudianteId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstEstudianteId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstestudiantes");
        webTarget = webTarget.path(proyectoInstEstudianteId.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long proyectoInstEstudianteId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstEstudianteId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstestudiantes/historial");
        webTarget = webTarget.path(proyectoInstEstudianteId.toString());
        RevHistorico[] lista = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(lista);
    }

}
