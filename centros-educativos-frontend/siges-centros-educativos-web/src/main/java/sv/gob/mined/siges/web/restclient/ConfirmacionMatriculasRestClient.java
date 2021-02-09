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
import sv.gob.mined.siges.web.dto.SgConfirmacionMatriculas;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroConfirmacionMatriculas;

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
public class ConfirmacionMatriculasRestClient implements Serializable {

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

    public ConfirmacionMatriculasRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgConfirmacionMatriculas> buscar(FiltroConfirmacionMatriculas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionesmatriculas/buscar");
        SgConfirmacionMatriculas[] confirmacionesMatriculas = restClient.invokePost(webTarget, filtro, SgConfirmacionMatriculas[].class);
        return Arrays.asList(confirmacionesMatriculas);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroConfirmacionMatriculas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionesmatriculas/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgConfirmacionMatriculas confirmar(String firmaTransactionId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (firmaTransactionId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionesmatriculas/confirmar");
        webTarget = webTarget.path(firmaTransactionId);
        return restClient.invokePost(webTarget, null, SgConfirmacionMatriculas.class);
    }
    
    @Timeout(value = 40000L)
    public SgConfirmacionMatriculas preconfirmar(SgConfirmacionMatriculas confirmacionMatriculas) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (confirmacionMatriculas == null) {
            return null;
        }

        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionesmatriculas/preconfirmar");
        return restClient.invokePost(webTarget, confirmacionMatriculas, SgConfirmacionMatriculas.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgConfirmacionMatriculas obtenerPorId(Long confirmacionMatriculasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (confirmacionMatriculasPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionesmatriculas");
        webTarget = webTarget.path(confirmacionMatriculasPk.toString());
        return restClient.invokeGet(webTarget, SgConfirmacionMatriculas.class);
    }

    public void eliminar(Long confirmacionMatriculasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (confirmacionMatriculasPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionesmatriculas");
        webTarget = webTarget.path(confirmacionMatriculasPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long confirmacionMatriculasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (confirmacionMatriculasPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionesmatriculas/historial");
        webTarget = webTarget.path(confirmacionMatriculasPk.toString());
        RevHistorico[] confirmacionesMatriculas = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(confirmacionesMatriculas);
    }

}
