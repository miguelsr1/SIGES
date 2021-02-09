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
import sv.gob.mined.siges.web.dto.SgAsignacionPerfil;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsignacionPerfil;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Traced
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class AsignacionPerfilRestClient implements Serializable {

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

    
    public AsignacionPerfilRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAsignacionPerfil> buscar(FiltroAsignacionPerfil filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfil/buscar");
        SgAsignacionPerfil[] asignacionPerfil = restClient.invokePost(webTarget, filtro, SgAsignacionPerfil[].class);
        return Arrays.asList(asignacionPerfil);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroAsignacionPerfil filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfil/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAsignacionPerfil guardar(SgAsignacionPerfil asignacionPerfil) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asignacionPerfil == null ) {
            return null;
        }
        if (asignacionPerfil.getApePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfil");
            return restClient.invokePost(webTarget, asignacionPerfil, SgAsignacionPerfil.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfil");
            webTarget = webTarget.path(asignacionPerfil.getApePk().toString());
            return restClient.invokePut(webTarget, asignacionPerfil, SgAsignacionPerfil.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAsignacionPerfil obtenerPorId(Long asignacionPerfilPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asignacionPerfilPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfil");
        webTarget = webTarget.path(asignacionPerfilPk.toString());
        return restClient.invokeGet(webTarget, SgAsignacionPerfil.class);
    }

    public void eliminar(Long asignacionPerfilPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asignacionPerfilPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfil");
        webTarget = webTarget.path(asignacionPerfilPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long asignacionPerfilPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asignacionPerfilPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfil/historial");
        webTarget = webTarget.path(asignacionPerfilPk.toString());
        RevHistorico[] asignacionPerfil = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(asignacionPerfil);
    }
    

}
