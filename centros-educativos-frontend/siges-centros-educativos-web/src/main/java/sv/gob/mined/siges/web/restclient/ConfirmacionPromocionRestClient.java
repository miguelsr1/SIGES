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
import sv.gob.mined.siges.web.dto.SgConfirmacionPromocion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroConfirmacionPromocion;

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
public class ConfirmacionPromocionRestClient implements Serializable {

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

    public ConfirmacionPromocionRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgConfirmacionPromocion> buscar(FiltroConfirmacionPromocion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionespromociones/buscar");
        SgConfirmacionPromocion[] confirmacionesMatriculas = restClient.invokePost(webTarget, filtro, SgConfirmacionPromocion[].class);
        return Arrays.asList(confirmacionesMatriculas);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroConfirmacionPromocion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionespromociones/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgConfirmacionPromocion confirmar(String firmaTransactionId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (firmaTransactionId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionespromociones/confirmar");
        webTarget = webTarget.path(firmaTransactionId);
        return restClient.invokePost(webTarget, null, SgConfirmacionPromocion.class);
    }
    
    @Timeout(value = 40000L)
    public SgConfirmacionPromocion preconfirmar(SgConfirmacionPromocion confirmacionMatriculas) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (confirmacionMatriculas == null) {
            return null;
        }

        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionespromociones/preconfirmar");
        return restClient.invokePost(webTarget, confirmacionMatriculas, SgConfirmacionPromocion.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgConfirmacionPromocion obtenerPorId(Long confirmacionMatriculasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (confirmacionMatriculasPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionespromociones");
        webTarget = webTarget.path(confirmacionMatriculasPk.toString());
        return restClient.invokeGet(webTarget, SgConfirmacionPromocion.class);
    }

    public void eliminar(Long confirmacionMatriculasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (confirmacionMatriculasPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionespromociones");
        webTarget = webTarget.path(confirmacionMatriculasPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long confirmacionMatriculasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (confirmacionMatriculasPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/confirmacionespromociones/historial");
        webTarget = webTarget.path(confirmacionMatriculasPk.toString());
        RevHistorico[] confirmacionesMatriculas = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(confirmacionesMatriculas);
    }

}
