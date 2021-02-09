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
import sv.gob.mined.siges.web.dto.SgRelInmuebleAbastecimientoAgua;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleAbastecimientoAgua;

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
public class RelInmuebleAbastecimientoAguaRestClient implements Serializable {

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
    
    public RelInmuebleAbastecimientoAguaRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelInmuebleAbastecimientoAgua> buscar(FiltroRelInmuebleAbastecimientoAgua filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleabastecimientoagua/buscar");
        SgRelInmuebleAbastecimientoAgua[] relInmuebleAbastecimientoAgua = restClient.invokePost(webTarget, filtro, SgRelInmuebleAbastecimientoAgua[].class);
        return Arrays.asList(relInmuebleAbastecimientoAgua);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelInmuebleAbastecimientoAgua filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleabastecimientoagua/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelInmuebleAbastecimientoAgua guardar(SgRelInmuebleAbastecimientoAgua relInmuebleAbastecimientoAgua) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleAbastecimientoAgua == null ) {
            return null;
        }
        if (relInmuebleAbastecimientoAgua.getIaaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleabastecimientoagua");
            return restClient.invokePost(webTarget, relInmuebleAbastecimientoAgua, SgRelInmuebleAbastecimientoAgua.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleabastecimientoagua");
            webTarget = webTarget.path(relInmuebleAbastecimientoAgua.getIaaPk().toString());
            return restClient.invokePut(webTarget, relInmuebleAbastecimientoAgua, SgRelInmuebleAbastecimientoAgua.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelInmuebleAbastecimientoAgua obtenerPorId(Long relInmuebleAbastecimientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleAbastecimientoAguaPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleabastecimientoagua");
        webTarget = webTarget.path(relInmuebleAbastecimientoAguaPk.toString());
        return restClient.invokeGet(webTarget, SgRelInmuebleAbastecimientoAgua.class);
    }

    public void eliminar(Long relInmuebleAbastecimientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleAbastecimientoAguaPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleabastecimientoagua");
        webTarget = webTarget.path(relInmuebleAbastecimientoAguaPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relInmuebleAbastecimientoAguaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleAbastecimientoAguaPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget =  RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleabastecimientoagua/historial");
        webTarget = webTarget.path(relInmuebleAbastecimientoAguaPk.toString());
        RevHistorico[] relInmuebleAbastecimientoAgua = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relInmuebleAbastecimientoAgua);
    }
    

}
