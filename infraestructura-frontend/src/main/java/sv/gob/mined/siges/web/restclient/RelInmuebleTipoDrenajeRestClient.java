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
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgRelInmuebleTipoDrenaje;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleTipoDrenaje;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class RelInmuebleTipoDrenajeRestClient implements Serializable {

    @Inject
    private RestClient restClient;

    private Client client = null;

    @PostConstruct
    public void init() {
        client = RestClient.getClient();
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            client.close();
        }
    };
    
    public RelInmuebleTipoDrenajeRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelInmuebleTipoDrenaje> buscar(FiltroRelInmuebleTipoDrenaje filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebletipodrenaje/buscar");
        SgRelInmuebleTipoDrenaje[] relInmuebleTipoDrenaje = restClient.invokePost(webTarget, filtro, SgRelInmuebleTipoDrenaje[].class);
        return Arrays.asList(relInmuebleTipoDrenaje);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelInmuebleTipoDrenaje filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebletipodrenaje/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelInmuebleTipoDrenaje guardar(SgRelInmuebleTipoDrenaje relInmuebleTipoDrenaje) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleTipoDrenaje == null ) {
            return null;
        }
        if (relInmuebleTipoDrenaje.getItdPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebletipodrenaje");
            return restClient.invokePost(webTarget, relInmuebleTipoDrenaje, SgRelInmuebleTipoDrenaje.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebletipodrenaje");
            webTarget = webTarget.path(relInmuebleTipoDrenaje.getItdPk().toString());
            return restClient.invokePut(webTarget, relInmuebleTipoDrenaje, SgRelInmuebleTipoDrenaje.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelInmuebleTipoDrenaje obtenerPorId(Long relInmuebleTipoDrenajePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleTipoDrenajePk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebletipodrenaje");
        webTarget = webTarget.path(relInmuebleTipoDrenajePk.toString());
        return restClient.invokeGet(webTarget, SgRelInmuebleTipoDrenaje.class);
    }

    public void eliminar(Long relInmuebleTipoDrenajePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleTipoDrenajePk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebletipodrenaje");
        webTarget = webTarget.path(relInmuebleTipoDrenajePk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relInmuebleTipoDrenajePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleTipoDrenajePk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget =  RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebletipodrenaje/historial");
        webTarget = webTarget.path(relInmuebleTipoDrenajePk.toString());
        RevHistorico[] relInmuebleTipoDrenaje = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relInmuebleTipoDrenaje);
    }
    

}
