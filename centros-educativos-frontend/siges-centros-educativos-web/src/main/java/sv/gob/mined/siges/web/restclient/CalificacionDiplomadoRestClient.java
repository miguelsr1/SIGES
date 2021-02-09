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
import sv.gob.mined.siges.web.dto.SgCalificacionDiplomado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCalificacionDiplomadoAux;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionDiplomado;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Traced
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class CalificacionDiplomadoRestClient implements Serializable {

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
    
    public CalificacionDiplomadoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCalificacionDiplomado> buscar(FiltroCalificacionDiplomado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomado/buscar");
        SgCalificacionDiplomado[] calificacionDiplomado = restClient.invokePost(webTarget, filtro, SgCalificacionDiplomado[].class);
        return Arrays.asList(calificacionDiplomado);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCalificacionDiplomado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomado/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCalificacionDiplomado guardar(SgCalificacionDiplomado calificacionDiplomado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionDiplomado == null ) {
            return null;
        }
        if (calificacionDiplomado.getCadPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomado");
            return restClient.invokePost(webTarget, calificacionDiplomado, SgCalificacionDiplomado.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomado");
            webTarget = webTarget.path(calificacionDiplomado.getCadPk().toString());
            return restClient.invokePut(webTarget, calificacionDiplomado, SgCalificacionDiplomado.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCalificacionDiplomado obtenerPorId(Long calificacionDiplomadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionDiplomadoPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomado");
        webTarget = webTarget.path(calificacionDiplomadoPk.toString());
        return restClient.invokeGet(webTarget, SgCalificacionDiplomado.class);
    }

    public void eliminar(Long calificacionDiplomadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionDiplomadoPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomado");
        webTarget = webTarget.path(calificacionDiplomadoPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long calificacionDiplomadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionDiplomadoPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomado/historial");
        webTarget = webTarget.path(calificacionDiplomadoPk.toString());
        RevHistorico[] calificacionDiplomado = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(calificacionDiplomado);
    }
    
    
    public Boolean calcularNotaInstitucional(SgCalificacionDiplomadoAux calificacionDiplomado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionDiplomado == null ) {
            return null;
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/calificaciondiplomado/calcularNotaInstitucional");
        return restClient.invokePost(webTarget, calificacionDiplomado, Boolean.class);
       
    }

}
