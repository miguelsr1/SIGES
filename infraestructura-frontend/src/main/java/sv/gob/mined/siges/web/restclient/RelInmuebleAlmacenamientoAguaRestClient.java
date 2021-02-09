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
import sv.gob.mined.siges.web.dto.SgRelInmuebleAlmacenamientoAgua;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleAlmacenamientoAgua;

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
public class RelInmuebleAlmacenamientoAguaRestClient implements Serializable {

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
    
    public RelInmuebleAlmacenamientoAguaRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelInmuebleAlmacenamientoAgua> buscar(FiltroRelInmuebleAlmacenamientoAgua filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblealmacenamientoagua/buscar");
        SgRelInmuebleAlmacenamientoAgua[] relInmuebleAlmacenamientoAgua = restClient.invokePost(webTarget, filtro, SgRelInmuebleAlmacenamientoAgua[].class);
        return Arrays.asList(relInmuebleAlmacenamientoAgua);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelInmuebleAlmacenamientoAgua filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblealmacenamientoagua/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelInmuebleAlmacenamientoAgua guardar(SgRelInmuebleAlmacenamientoAgua relInmuebleAlmacenamientoAgua) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleAlmacenamientoAgua == null ) {
            return null;
        }
        if (relInmuebleAlmacenamientoAgua.getIalPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblealmacenamientoagua");
            return restClient.invokePost(webTarget, relInmuebleAlmacenamientoAgua, SgRelInmuebleAlmacenamientoAgua.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblealmacenamientoagua");
            webTarget = webTarget.path(relInmuebleAlmacenamientoAgua.getIalPk().toString());
            return restClient.invokePut(webTarget, relInmuebleAlmacenamientoAgua, SgRelInmuebleAlmacenamientoAgua.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelInmuebleAlmacenamientoAgua obtenerPorId(Long relInmuebleAlmacenamientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleAlmacenamientoAguaPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblealmacenamientoagua");
        webTarget = webTarget.path(relInmuebleAlmacenamientoAguaPk.toString());
        return restClient.invokeGet(webTarget, SgRelInmuebleAlmacenamientoAgua.class);
    }

    public void eliminar(Long relInmuebleAlmacenamientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleAlmacenamientoAguaPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblealmacenamientoagua");
        webTarget = webTarget.path(relInmuebleAlmacenamientoAguaPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relInmuebleAlmacenamientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleAlmacenamientoAguaPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget =  RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblealmacenamientoagua/historial");
        webTarget = webTarget.path(relInmuebleAlmacenamientoAguaPk.toString());
        RevHistorico[] relInmuebleAlmacenamientoAgua = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relInmuebleAlmacenamientoAgua);
    }
    

}
