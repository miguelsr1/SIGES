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
import sv.gob.mined.siges.web.dto.SgProfesion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Traced
@Timeout(value = 3000L)
public class ProfesionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    @Inject
    private RestClient restClient;
    
    private Client client = null;
    
    public ProfesionRestClient() {
    }
    
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

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgProfesion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/profesiones/buscar");
        SgProfesion[] profesiones = restClient.invokePost(webTarget, filtro, SgProfesion[].class);
        return Arrays.asList(profesiones);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/profesiones/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgProfesion guardar(SgProfesion profesion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (profesion == null || userToken == null) {
            return null;
        }
        if (profesion.getProPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/profesiones");
            return restClient.invokePost(webTarget, profesion, SgProfesion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/profesiones");
            webTarget = webTarget.path(profesion.getProPk().toString());
            return restClient.invokePut(webTarget, profesion, SgProfesion.class);
        }
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgProfesion obtenerPorId(Long profesionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (profesionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/profesiones");
        webTarget = webTarget.path(profesionPk.toString());
        return restClient.invokeGet(webTarget, SgProfesion.class);
    }

    public void eliminar(Long profesionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (profesionPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/profesiones");
        webTarget = webTarget.path(profesionPk.toString());
        restClient.invokeDelete(webTarget);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgProfesion> obtenerHistorialPorId(Long profesionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (profesionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/profesiones/historial");
        webTarget = webTarget.path(profesionPk.toString());
        SgProfesion[] profesiones = restClient.invokeGet(webTarget, SgProfesion[].class);
        return Arrays.asList(profesiones);
    }
    

}
