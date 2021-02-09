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
import sv.gob.mined.siges.web.dto.SgRelEdificioEspacio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelEdificioEspacio;

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
public class RelEdificioEspacioRestClient implements Serializable {

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
    }
    
    public RelEdificioEspacioRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelEdificioEspacio> buscar(FiltroRelEdificioEspacio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioespacio/buscar");
        SgRelEdificioEspacio[] relEdificioEspacio = restClient.invokePost(webTarget, filtro, SgRelEdificioEspacio[].class);
        return Arrays.asList(relEdificioEspacio);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelEdificioEspacio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioespacio/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelEdificioEspacio guardar(SgRelEdificioEspacio relEdificioEspacio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relEdificioEspacio == null ) {
            return null;
        }
        if (relEdificioEspacio.getReePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioespacio");
            return restClient.invokePost(webTarget, relEdificioEspacio, SgRelEdificioEspacio.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioespacio");
            webTarget = webTarget.path(relEdificioEspacio.getReePk().toString());
            return restClient.invokePut(webTarget, relEdificioEspacio, SgRelEdificioEspacio.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelEdificioEspacio obtenerPorId(Long relEdificioEspacioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relEdificioEspacioPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioespacio");
        webTarget = webTarget.path(relEdificioEspacioPk.toString());
        return restClient.invokeGet(webTarget, SgRelEdificioEspacio.class);
    }

    public void eliminar(Long relEdificioEspacioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relEdificioEspacioPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioespacio");
        webTarget = webTarget.path(relEdificioEspacioPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relEdificioEspacioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relEdificioEspacioPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioespacio/historial");
        webTarget = webTarget.path(relEdificioEspacioPk.toString());
        RevHistorico[] relEdificioEspacio = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relEdificioEspacio);
    }
    
   

}
