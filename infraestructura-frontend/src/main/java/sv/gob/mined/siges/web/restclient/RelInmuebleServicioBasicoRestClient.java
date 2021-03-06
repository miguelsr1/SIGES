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
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgRelInmuebleServicioBasico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleServicio;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 5000L)
public class RelInmuebleServicioBasicoRestClient implements Serializable {

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

    public RelInmuebleServicioBasicoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelInmuebleServicioBasico> buscar(FiltroRelInmuebleServicio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relInmuebleServicioBasico/buscar");
        SgRelInmuebleServicioBasico[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgRelInmuebleServicioBasico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelInmuebleServicio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relInmuebleServicioBasico/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelInmuebleServicioBasico guardar(SgRelInmuebleServicioBasico inmueble) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueble == null) {
            return null;
        }
        if (inmueble.getRisPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relInmuebleServicioBasico");
            return restClient.invokePost(webTarget, inmueble, SgRelInmuebleServicioBasico.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relInmuebleServicioBasico");
            webTarget = webTarget.path(inmueble.getRisPk().toString());
            return restClient.invokePut(webTarget, inmueble, SgRelInmuebleServicioBasico.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelInmuebleServicioBasico obtenerPorId(Long inmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueblePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relInmuebleServicioBasico");
        webTarget = webTarget.path(inmueblePk.toString());
        return restClient.invokeGet(webTarget, SgRelInmuebleServicioBasico.class);
    }

    public void eliminar(Long inmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueblePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relInmuebleServicioBasico");
        webTarget = webTarget.path(inmueblePk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long inmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueblePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relInmuebleServicioBasico/historial");
        webTarget = webTarget.path(inmueblePk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public Boolean validar(SgRelInmuebleServicioBasico inmueble) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueble == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relInmuebleServicioBasico/validar");
        return restClient.invokePost(webTarget, inmueble, Boolean.class);
    }

}
