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
import sv.gob.mined.siges.web.dto.SgRelInmuebleServicioSanitario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleServicioSanitario;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 5000L)
public class RelInmuebleServicioSanitarioRestClient implements Serializable {

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

    public RelInmuebleServicioSanitarioRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelInmuebleServicioSanitario> buscar(FiltroRelInmuebleServicioSanitario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleserviciosanitario/buscar");
        SgRelInmuebleServicioSanitario[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgRelInmuebleServicioSanitario[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelInmuebleServicioSanitario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleserviciosanitario/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelInmuebleServicioSanitario guardar(SgRelInmuebleServicioSanitario inmueble) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueble == null) {
            return null;
        }
        if (inmueble.getRitPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleserviciosanitario");
            return restClient.invokePost(webTarget, inmueble, SgRelInmuebleServicioSanitario.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleserviciosanitario");
            webTarget = webTarget.path(inmueble.getRitPk().toString());
            return restClient.invokePut(webTarget, inmueble, SgRelInmuebleServicioSanitario.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelInmuebleServicioSanitario obtenerPorId(Long inmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueblePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleserviciosanitario");
        webTarget = webTarget.path(inmueblePk.toString());
        return restClient.invokeGet(webTarget, SgRelInmuebleServicioSanitario.class);
    }

    public void eliminar(Long inmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueblePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleserviciosanitario");
        webTarget = webTarget.path(inmueblePk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long inmueblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueblePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleserviciosanitario/historial");
        webTarget = webTarget.path(inmueblePk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public Boolean validar(SgRelInmuebleServicioSanitario inmueble) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (inmueble == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/relinmuebleserviciosanitario/validar");
        return restClient.invokePost(webTarget, inmueble, Boolean.class);
    }

}
