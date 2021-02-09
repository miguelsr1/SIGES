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
import sv.gob.mined.siges.web.dto.SgRelInmuebleManejoDesechos;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleManejoDesechos;

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
public class RelInmuebleManejoDesechosRestClient implements Serializable {

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
    
    public RelInmuebleManejoDesechosRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelInmuebleManejoDesechos> buscar(FiltroRelInmuebleManejoDesechos filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblemanejodesechos/buscar");
        SgRelInmuebleManejoDesechos[] relInmuebleManejoDesechos = restClient.invokePost(webTarget, filtro, SgRelInmuebleManejoDesechos[].class);
        return Arrays.asList(relInmuebleManejoDesechos);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelInmuebleManejoDesechos filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblemanejodesechos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelInmuebleManejoDesechos guardar(SgRelInmuebleManejoDesechos relInmuebleManejoDesechos) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleManejoDesechos == null ) {
            return null;
        }
        if (relInmuebleManejoDesechos.getImdPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblemanejodesechos");
            return restClient.invokePost(webTarget, relInmuebleManejoDesechos, SgRelInmuebleManejoDesechos.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblemanejodesechos");
            webTarget = webTarget.path(relInmuebleManejoDesechos.getImdPk().toString());
            return restClient.invokePut(webTarget, relInmuebleManejoDesechos, SgRelInmuebleManejoDesechos.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelInmuebleManejoDesechos obtenerPorId(Long relInmuebleManejoDesechosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleManejoDesechosPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblemanejodesechos");
        webTarget = webTarget.path(relInmuebleManejoDesechosPk.toString());
        return restClient.invokeGet(webTarget, SgRelInmuebleManejoDesechos.class);
    }

    public void eliminar(Long relInmuebleManejoDesechosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleManejoDesechosPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblemanejodesechos");
        webTarget = webTarget.path(relInmuebleManejoDesechosPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relInmuebleManejoDesechosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relInmuebleManejoDesechosPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget =  RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmueblemanejodesechos/historial");
        webTarget = webTarget.path(relInmuebleManejoDesechosPk.toString());
        RevHistorico[] relInmuebleManejoDesechos = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relInmuebleManejoDesechos);
    }
    

}
