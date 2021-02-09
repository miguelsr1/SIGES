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
import sv.gob.mined.siges.web.dto.SgAsistenciaSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsistenciaSede;


@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class AsistenciaSedeRestClient implements Serializable {

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

    public AsistenciaSedeRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAsistenciaSede> buscar(FiltroAsistenciaSede filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciassede/buscar");
        SgAsistenciaSede[] accionPrevenirEmbarazoes = restClient.invokePost(webTarget, filtro, SgAsistenciaSede[].class);
        return new ArrayList<>(Arrays.asList(accionPrevenirEmbarazoes));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroAsistenciaSede filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciassede/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAsistenciaSede guardar(SgAsistenciaSede accionPrevenirEmbarazo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (accionPrevenirEmbarazo == null) {
            return null;
        }
        if (accionPrevenirEmbarazo.getAsePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciassede");
            return restClient.invokePost(webTarget, accionPrevenirEmbarazo, SgAsistenciaSede.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciassede");
            webTarget = webTarget.path(accionPrevenirEmbarazo.getAsePk().toString());
            return restClient.invokePut(webTarget, accionPrevenirEmbarazo, SgAsistenciaSede.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAsistenciaSede obtenerPorId(Long accionPrevenirEmbarazoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (accionPrevenirEmbarazoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciassede");
        webTarget = webTarget.path(accionPrevenirEmbarazoPk.toString());
        return restClient.invokeGet(webTarget, SgAsistenciaSede.class);
    }

    public void eliminar(Long accionPrevenirEmbarazoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (accionPrevenirEmbarazoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciassede");
        webTarget = webTarget.path(accionPrevenirEmbarazoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long accionPrevenirEmbarazoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (accionPrevenirEmbarazoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciassede/historial");
        webTarget = webTarget.path(accionPrevenirEmbarazoPk.toString());
        RevHistorico[] accionPrevenirEmbarazoes = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(accionPrevenirEmbarazoes);
    }

    public SgAsistenciaSede obtenerEnRevision(Long accionPrevenirEmbarazoPk, Long accionPrevenirEmbarazoRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (accionPrevenirEmbarazoPk == null || accionPrevenirEmbarazoRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_ASISTENCIAS, "v1/asistenciassede/revision");
        webTarget = webTarget.path(accionPrevenirEmbarazoPk.toString());
        webTarget = webTarget.path(accionPrevenirEmbarazoRev.toString());
        return restClient.invokeGet(webTarget, SgAsistenciaSede.class);
    }

}
