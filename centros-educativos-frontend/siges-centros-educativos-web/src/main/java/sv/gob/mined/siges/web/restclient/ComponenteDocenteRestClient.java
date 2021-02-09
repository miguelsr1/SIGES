/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.SgComponenteDocente;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponenteDocente;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class ComponenteDocenteRestClient implements Serializable {

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

    public ComponenteDocenteRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgComponenteDocente> buscar(FiltroComponenteDocente filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesdocentes/buscar");
        SgComponenteDocente[] componentePlanEstudio = restClient.invokePost(webTarget, filtro, SgComponenteDocente[].class);
        return Arrays.asList(componentePlanEstudio);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroComponenteDocente filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesdocentes/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgComponenteDocente obtenerPorId(Long componenteDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componenteDocentePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesdocentes");
        webTarget = webTarget.path(componenteDocentePk.toString());
        return restClient.invokeGet(webTarget, SgComponenteDocente.class);
    }

    @Timeout(value = 30000L)
    public SgComponenteDocente guardar(SgComponenteDocente componenteDocente) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componenteDocente == null) {
            return null;
        }
        if (componenteDocente.getCdoPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesdocentes");
            return restClient.invokePost(webTarget, componenteDocente, SgComponenteDocente.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesdocentes");
            webTarget = webTarget.path(componenteDocente.getCdoPk().toString());
            return restClient.invokePut(webTarget, componenteDocente, SgComponenteDocente.class);
        }
    }


    public void eliminar(Long componenteDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componenteDocentePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesdocentes");
        webTarget = webTarget.path(componenteDocentePk.toString());
        restClient.invokeDelete(webTarget);
    }
    
    public void eliminarElemento(SgComponenteDocente componenteDocente) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (componenteDocente.getCdoPk() == null) {
            return ;
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesdocentes/eliminar");
        webTarget = webTarget.path(componenteDocente.getCdoPk().toString());
        restClient.invokePut(webTarget, componenteDocente, SgComponenteDocente.class);
        
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgComponentePlanEstudio> obtenerComponentesAsociadosDocenteEnSeccion(Long seccion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (seccion == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/componentesdocentes/obtenerComponentesAsociadosDocenteEnSeccion");
        SgComponentePlanEstudio[] componentePlanEstudio = restClient.invokePost(webTarget, seccion, SgComponentePlanEstudio[].class);
        return Arrays.asList(componentePlanEstudio);
    }

}
