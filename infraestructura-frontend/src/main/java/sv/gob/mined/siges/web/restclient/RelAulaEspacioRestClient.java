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
import sv.gob.mined.siges.web.dto.SgRelAulaEspacio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelAulaEspacio;

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
public class RelAulaEspacioRestClient implements Serializable {

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
    
    public RelAulaEspacioRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelAulaEspacio> buscar(FiltroRelAulaEspacio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaespacio/buscar");
        SgRelAulaEspacio[] relAulaEspacio = restClient.invokePost(webTarget, filtro, SgRelAulaEspacio[].class);
        return Arrays.asList(relAulaEspacio);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelAulaEspacio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaespacio/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelAulaEspacio guardar(SgRelAulaEspacio relAulaEspacio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relAulaEspacio == null ) {
            return null;
        }
        if (relAulaEspacio.getRaePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaespacio");
            return restClient.invokePost(webTarget, relAulaEspacio, SgRelAulaEspacio.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaespacio");
            webTarget = webTarget.path(relAulaEspacio.getRaePk().toString());
            return restClient.invokePut(webTarget, relAulaEspacio, SgRelAulaEspacio.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelAulaEspacio obtenerPorId(Long relAulaEspacioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relAulaEspacioPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaespacio");
        webTarget = webTarget.path(relAulaEspacioPk.toString());
        return restClient.invokeGet(webTarget, SgRelAulaEspacio.class);
    }

    public void eliminar(Long relAulaEspacioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relAulaEspacioPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaespacio");
        webTarget = webTarget.path(relAulaEspacioPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relAulaEspacioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relAulaEspacioPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaespacio/historial");
        webTarget = webTarget.path(relAulaEspacioPk.toString());
        RevHistorico[] relAulaEspacio = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relAulaEspacio);
    }
    

}
