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
import sv.gob.mined.siges.web.dto.SgCasoViolacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCasoViolacion;


@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 22250)
@Timeout(value = 60000L)
public class CasoViolacionRestClient implements Serializable {

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

    public CasoViolacionRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgCasoViolacion> buscar(FiltroCasoViolacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/casosviolacion/buscar");
        SgCasoViolacion[] casoViolaciones = restClient.invokePost(webTarget, filtro, SgCasoViolacion[].class);
        return new ArrayList<>(Arrays.asList(casoViolaciones));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCasoViolacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/casosviolacion/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCasoViolacion guardar(SgCasoViolacion casoViolacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (casoViolacion == null) {
            return null;
        }
        if (casoViolacion.getCviPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/casosviolacion");
            return restClient.invokePost(webTarget, casoViolacion, SgCasoViolacion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/casosviolacion");
            webTarget = webTarget.path(casoViolacion.getCviPk().toString());
            return restClient.invokePut(webTarget, casoViolacion, SgCasoViolacion.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCasoViolacion obtenerPorId(Long casoViolacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (casoViolacionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/casosviolacion");
        webTarget = webTarget.path(casoViolacionPk.toString());
        return restClient.invokeGet(webTarget, SgCasoViolacion.class);
    }

    public void eliminar(Long casoViolacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (casoViolacionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/casosviolacion");
        webTarget = webTarget.path(casoViolacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long casoViolacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (casoViolacionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/casosviolacion/historial");
        webTarget = webTarget.path(casoViolacionPk.toString());
        RevHistorico[] casoViolaciones = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(casoViolaciones);
    }

    public SgCasoViolacion obtenerEnRevision(Long casoViolacionPk, Long casoViolacionRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (casoViolacionPk == null || casoViolacionRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/casosviolacion/revision");
        webTarget = webTarget.path(casoViolacionPk.toString());
        webTarget = webTarget.path(casoViolacionRev.toString());
        return restClient.invokeGet(webTarget, SgCasoViolacion.class);
    }

}
