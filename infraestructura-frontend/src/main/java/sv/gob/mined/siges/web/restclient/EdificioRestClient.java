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
import sv.gob.mined.siges.web.dto.SgEdificio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEdificio;

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
public class EdificioRestClient implements Serializable {

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
    
    public EdificioRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEdificio> buscar(FiltroEdificio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/edificio/buscar");
        SgEdificio[] edificio = restClient.invokePost(webTarget, filtro, SgEdificio[].class);
        return Arrays.asList(edificio);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroEdificio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/edificio/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgEdificio guardar(SgEdificio edificio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (edificio == null ) {
            return null;
        }
        if (edificio.getEdiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/edificio");
            return restClient.invokePost(webTarget, edificio, SgEdificio.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/edificio");
            webTarget = webTarget.path(edificio.getEdiPk().toString());
            return restClient.invokePut(webTarget, edificio, SgEdificio.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEdificio obtenerPorId(Long edificioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (edificioPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/edificio");
        webTarget = webTarget.path(edificioPk.toString());
        return restClient.invokeGet(webTarget, SgEdificio.class);
    }

    public void eliminar(Long edificioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (edificioPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/edificio");
        webTarget = webTarget.path(edificioPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long edificioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (edificioPk == null ) {
            return new ArrayList<>();
        }
        
         WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA,  "v1/edificio/historial");
        webTarget = webTarget.path(edificioPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }
    
     public SgEdificio guardarListaEspacioComunes(SgEdificio edificio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (edificio == null) {
            return null;
        }
       if (edificio.getEdiPk()!= null){
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/edificio/actualizarListaEspacio");
            webTarget = webTarget.path(edificio.getEdiPk().toString());
            return restClient.invokePut(webTarget, edificio, SgEdificio.class);
        }
        return edificio;
    }
     
     public SgEdificio guardarListaServiciosBasicos(SgEdificio edificio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (edificio == null) {
            return null;
        }
       if (edificio.getEdiPk()!= null){
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/edificio/actualizarListaServicio");
            webTarget = webTarget.path(edificio.getEdiPk().toString());
            return restClient.invokePut(webTarget, edificio, SgEdificio.class);
        }
        return edificio;
    }
    

}

