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
import sv.gob.mined.siges.web.dto.SgReposicionTitulo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgReposicionSolicitud;
import sv.gob.mined.siges.web.dto.SgSolicitudImpresion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReposicionTitulo;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Traced
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class ReposicionTituloRestClient implements Serializable {

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

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgReposicionTitulo> buscar(FiltroReposicionTitulo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reposiciontitulo/buscar");
        SgReposicionTitulo[] reposicionTitulo = restClient.invokePost(webTarget, filtro, SgReposicionTitulo[].class);
        return Arrays.asList(reposicionTitulo);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroReposicionTitulo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reposiciontitulo/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgReposicionTitulo guardar(SgReposicionTitulo reposicionTitulo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reposicionTitulo == null ) {
            return null;
        }
        if (reposicionTitulo.getRetPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reposiciontitulo");
            return restClient.invokePost(webTarget, reposicionTitulo, SgReposicionTitulo.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reposiciontitulo");
            webTarget = webTarget.path(reposicionTitulo.getRetPk().toString());
            return restClient.invokePut(webTarget, reposicionTitulo, SgReposicionTitulo.class);
        }
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgReposicionTitulo obtenerPorId(Long reposicionTituloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reposicionTituloPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reposiciontitulo");
        webTarget = webTarget.path(reposicionTituloPk.toString());
        return restClient.invokeGet(webTarget, SgReposicionTitulo.class);
    }

    public void eliminar(Long reposicionTituloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reposicionTituloPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reposiciontitulo");
        webTarget = webTarget.path(reposicionTituloPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long reposicionTituloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reposicionTituloPk == null ) {
            return new ArrayList<>();
        }
    
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sedeseducame/historial");
        webTarget = webTarget.path(reposicionTituloPk.toString());
        RevHistorico[] sede = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(sede);
    }
    
    public SgSolicitudImpresion reponerSolicitud(SgReposicionSolicitud reposicionTitulo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reposicionTitulo == null ) {
            return null;
        }
   
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reposiciontitulo/reponersolicitud");
        return restClient.invokePost(webTarget, reposicionTitulo, SgSolicitudImpresion.class);
       
    }

}
