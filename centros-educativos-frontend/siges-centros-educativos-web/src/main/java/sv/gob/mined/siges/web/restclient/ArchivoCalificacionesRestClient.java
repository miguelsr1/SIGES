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
import sv.gob.mined.siges.web.dto.SgArchivoCalificaciones;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroArchivoCalificaciones;

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
public class ArchivoCalificacionesRestClient implements Serializable {

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

    public ArchivoCalificacionesRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgCiclo> buscar(FiltroArchivoCalificaciones filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/archivoCalificaciones/buscar");
        SgCiclo[] ciclos = restClient.invokePost(webTarget, filtro, SgCiclo[].class);
        return Arrays.asList(ciclos);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroArchivoCalificaciones filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/archivoCalificaciones/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCiclo obtenerPorId(Long cicloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cicloPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/archivoCalificaciones");
        webTarget = webTarget.path(cicloPk.toString());
        return restClient.invokeGet(webTarget, SgCiclo.class);
    }

    public SgArchivoCalificaciones guardar(SgArchivoCalificaciones ciclo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ciclo == null) {
            return null;
        }
        if (ciclo.getAccPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.FILE_PROCESSOR, "v1/archivoCalificaciones");
            return restClient.invokePost(webTarget, ciclo, SgArchivoCalificaciones.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.FILE_PROCESSOR, "v1/archivoCalificaciones");
            webTarget = webTarget.path(ciclo.getAccPk().toString());
            return restClient.invokePut(webTarget, ciclo, SgArchivoCalificaciones.class);
        }
    }
    
    public void ejecutarProcesamientoArchivos() throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException{
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/archivoCalificaciones/calificarArchivos");
        restClient.invokeGet(webTarget,null);
    }

    public void eliminar(Long cicloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cicloPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/archivoCalificaciones");
        webTarget = webTarget.path(cicloPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long cicloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cicloPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/archivoCalificaciones/historial");
        webTarget = webTarget.path(cicloPk.toString());
        RevHistorico[] ciclos = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(ciclos);
    }

}
