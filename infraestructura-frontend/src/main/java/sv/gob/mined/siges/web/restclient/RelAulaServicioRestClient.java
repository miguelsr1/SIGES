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
import sv.gob.mined.siges.web.dto.SgRelAulaServicio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelAulaServicio;

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
public class RelAulaServicioRestClient implements Serializable {

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
    
    public RelAulaServicioRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelAulaServicio> buscar(FiltroRelAulaServicio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaservicio/buscar");
        SgRelAulaServicio[] relAulaServicio = restClient.invokePost(webTarget, filtro, SgRelAulaServicio[].class);
        return Arrays.asList(relAulaServicio);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelAulaServicio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaservicio/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelAulaServicio guardar(SgRelAulaServicio relAulaServicio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relAulaServicio == null ) {
            return null;
        }
        if (relAulaServicio.getRasPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaservicio");
            return restClient.invokePost(webTarget, relAulaServicio, SgRelAulaServicio.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaservicio");
            webTarget = webTarget.path(relAulaServicio.getRasPk().toString());
            return restClient.invokePut(webTarget, relAulaServicio, SgRelAulaServicio.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelAulaServicio obtenerPorId(Long relAulaServicioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relAulaServicioPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaservicio");
        webTarget = webTarget.path(relAulaServicioPk.toString());
        return restClient.invokeGet(webTarget, SgRelAulaServicio.class);
    }

    public void eliminar(Long relAulaServicioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relAulaServicioPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaservicio");
        webTarget = webTarget.path(relAulaServicioPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relAulaServicioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relAulaServicioPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relaulaservicio/historial");
        webTarget = webTarget.path(relAulaServicioPk.toString());
        RevHistorico[] relAulaServicio = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relAulaServicio);
    }
    

}
