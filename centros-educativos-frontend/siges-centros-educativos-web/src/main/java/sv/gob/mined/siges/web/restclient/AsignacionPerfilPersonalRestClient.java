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
import sv.gob.mined.siges.web.dto.SgAsignacionPerfilPersonal;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsignacionPerfilPersonal;

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
public class AsignacionPerfilPersonalRestClient implements Serializable {

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

    public AsignacionPerfilPersonalRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAsignacionPerfilPersonal> buscar(FiltroAsignacionPerfilPersonal filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfilpersonal/buscar");
        SgAsignacionPerfilPersonal[] asignacionPerfilPersonal = restClient.invokePost(webTarget, filtro, SgAsignacionPerfilPersonal[].class);
        return Arrays.asList(asignacionPerfilPersonal);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroAsignacionPerfilPersonal filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfilpersonal/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAsignacionPerfilPersonal guardar(SgAsignacionPerfilPersonal asignacionPerfilPersonal) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asignacionPerfilPersonal == null ) {
            return null;
        }
        if (asignacionPerfilPersonal.getAppPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfilpersonal");
            return restClient.invokePost(webTarget, asignacionPerfilPersonal, SgAsignacionPerfilPersonal.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfilpersonal");
            webTarget = webTarget.path(asignacionPerfilPersonal.getAppPk().toString());
            return restClient.invokePut(webTarget, asignacionPerfilPersonal, SgAsignacionPerfilPersonal.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAsignacionPerfilPersonal obtenerPorId(Long asignacionPerfilPersonalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asignacionPerfilPersonalPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfilpersonal");
        webTarget = webTarget.path(asignacionPerfilPersonalPk.toString());
        return restClient.invokeGet(webTarget, SgAsignacionPerfilPersonal.class);
    }

    public void eliminar(Long asignacionPerfilPersonalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (asignacionPerfilPersonalPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/asignacionperfilpersonal");
        webTarget = webTarget.path(asignacionPerfilPersonalPk.toString());
        restClient.invokeDelete(webTarget);
    }


}
