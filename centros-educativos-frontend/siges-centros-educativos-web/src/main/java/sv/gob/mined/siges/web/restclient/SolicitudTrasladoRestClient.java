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
import sv.gob.mined.siges.web.dto.SgConfirmacionSolicitudTraslado;
import sv.gob.mined.siges.web.dto.SgSolicitudTraslado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudTraslado;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class SolicitudTrasladoRestClient implements Serializable {

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

    public SolicitudTrasladoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgSolicitudTraslado> buscar(FiltroSolicitudTraslado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudestraslado/buscar");
        SgSolicitudTraslado[] lista = restClient.invokePost(webTarget, filtro, SgSolicitudTraslado[].class);
        return Arrays.asList(lista);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroSolicitudTraslado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudestraslado/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgSolicitudTraslado obtenerPorId(Long solicitudTrasladoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudTrasladoId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudestraslado");
        webTarget = webTarget.path(solicitudTrasladoId.toString());
        return restClient.invokeGet(webTarget, SgSolicitudTraslado.class);
    }

    @Timeout(value = 12000L)
    public SgSolicitudTraslado guardar(SgSolicitudTraslado solicitudTraslado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudTraslado == null) {
            return null;
        }
        if (solicitudTraslado.getSotPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudestraslado");
            return restClient.invokePost(webTarget, solicitudTraslado, SgSolicitudTraslado.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudestraslado");
            webTarget = webTarget.path(solicitudTraslado.getSotPk().toString());
            return restClient.invokePut(webTarget, solicitudTraslado, SgSolicitudTraslado.class);
        }
    }

    public Boolean validar(SgSolicitudTraslado solicitudTraslado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudTraslado == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudestraslado/validar");
        return restClient.invokePost(webTarget, solicitudTraslado, Boolean.class);
    }

    public void eliminar(Long solicitudTrasladoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudTrasladoId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudestraslado");
        webTarget = webTarget.path(solicitudTrasladoId.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long solicitudTrasladoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (solicitudTrasladoId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/solicitudestraslado/historial");
        webTarget = webTarget.path(solicitudTrasladoId.toString());
        RevHistorico[] lista = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(lista);
    }

    @Timeout(value = 20000L)
    public SgConfirmacionSolicitudTraslado confirmar(String firmaTransactionId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (firmaTransactionId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/solicitudestraslado/confirmar");
        webTarget = webTarget.path(firmaTransactionId);
        return restClient.invokePost(webTarget, null, SgConfirmacionSolicitudTraslado.class);
    }

    @Timeout(value = 40000L)
    public SgConfirmacionSolicitudTraslado preconfirmar(SgConfirmacionSolicitudTraslado conf) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (conf == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/solicitudestraslado/preconfirmar");
        return restClient.invokePost(webTarget, conf, SgConfirmacionSolicitudTraslado.class);
    }

}
