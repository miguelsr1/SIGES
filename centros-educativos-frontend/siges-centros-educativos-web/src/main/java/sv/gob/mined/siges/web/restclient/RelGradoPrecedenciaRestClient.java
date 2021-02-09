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
import sv.gob.mined.siges.web.dto.SgRelGradoPrecedencia;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPrecedencia;

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
public class RelGradoPrecedenciaRestClient implements Serializable {

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

    public RelGradoPrecedenciaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRelGradoPrecedencia> buscar(FiltroRelGradoPrecedencia filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoprecedencia/buscar");
        SgRelGradoPrecedencia[] relGradoPlanEstudio = restClient.invokePost(webTarget, filtro, SgRelGradoPrecedencia[].class);
        return new ArrayList<>(Arrays.asList(relGradoPlanEstudio));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoprecedencia/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelGradoPrecedencia guardar(SgRelGradoPrecedencia relGradoPlanEstudio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relGradoPlanEstudio == null) {
            return null;
        }
        if (relGradoPlanEstudio.getRgpPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoprecedencia");
            return restClient.invokePost(webTarget, relGradoPlanEstudio, SgRelGradoPrecedencia.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoprecedencia");
            webTarget = webTarget.path(relGradoPlanEstudio.getRgpPk().toString());
            return restClient.invokePut(webTarget, relGradoPlanEstudio, SgRelGradoPrecedencia.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgRelGradoPrecedencia obtenerPorId(Long relGradoPrecedenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relGradoPrecedenciaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoprecedencia");
        webTarget = webTarget.path(relGradoPrecedenciaPk.toString());
        return restClient.invokeGet(webTarget, SgRelGradoPrecedencia.class);
    }

    public void eliminar(Long relGradoPrecedenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relGradoPrecedenciaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoprecedencia");
        webTarget = webTarget.path(relGradoPrecedenciaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRelGradoPrecedencia> obtenerHistorialPorId(Long relGradoPrecedenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relGradoPrecedenciaPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relgradoprecedencia/historial");
        webTarget = webTarget.path(relGradoPrecedenciaPk.toString());
        SgRelGradoPrecedencia[] relGradoPlanEstudio = restClient.invokeGet(webTarget, SgRelGradoPrecedencia[].class);
        return Arrays.asList(relGradoPlanEstudio);
    }

}
