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
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroInmueblesSedes;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class InmueblesSedesRestClient implements Serializable {

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

    public InmueblesSedesRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgInmueblesSedes> buscar(FiltroInmueblesSedes filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/inmueblesede/buscar");
        SgInmueblesSedes[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgInmueblesSedes[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroInmueblesSedes filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/inmueblesede/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgInmueblesSedes guardar(SgInmueblesSedes inmueble) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueble == null) {
            return null;
        }
        if (inmueble.getTisPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/inmueblesede");
            return restClient.invokePost(webTarget, inmueble, SgInmueblesSedes.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/inmueblesede");
            webTarget = webTarget.path(inmueble.getTisPk().toString());
            return restClient.invokePut(webTarget, inmueble, SgInmueblesSedes.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgInmueblesSedes obtenerPorId(Long inmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueblePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/inmueblesede");
        webTarget = webTarget.path(inmueblePk.toString());
        return restClient.invokeGet(webTarget, SgInmueblesSedes.class);
    }

    public void eliminar(Long inmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueblePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/inmueblesede");
        webTarget = webTarget.path(inmueblePk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long inmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueblePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/inmueblesede/historial");
        webTarget = webTarget.path(inmueblePk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public Boolean validar(SgInmueblesSedes inmueble) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueble == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/inmueblesede/validar");
        return restClient.invokePost(webTarget, inmueble, Boolean.class);
    }
    
    public SgInmueblesSedes guardarListaEspacioComunes(SgInmueblesSedes inmueble) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueble == null) {
            return null;
        }
       if (inmueble.getTisPk()!= null){
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/inmueblesede/actualizarListaEspacio");
            webTarget = webTarget.path(inmueble.getTisPk().toString());
            return restClient.invokePut(webTarget, inmueble, SgInmueblesSedes.class);
        }
        return inmueble;
    }
    
    public SgInmueblesSedes guardarListaServiciosBasicos(SgInmueblesSedes inmueble) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueble == null) {
            return null;
        }
       if (inmueble.getTisPk()!= null){
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/inmueblesede/actualizarListaServicio");
            webTarget = webTarget.path(inmueble.getTisPk().toString());
            return restClient.invokePut(webTarget, inmueble, SgInmueblesSedes.class);
        }
        return inmueble;
    }
    
      

}
