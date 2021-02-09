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
import java.util.logging.Logger;
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
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.client.excepciones.HttpClientException;
import sv.gob.mined.siges.client.excepciones.HttpServerException;
import sv.gob.mined.siges.client.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.dto.centros_educativos.SgAcuerdo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAcuerdo;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 6000L)
public class AcuerdoRestClient implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AcuerdoRestClient.class.getName());

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

    public AcuerdoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAcuerdo> buscar(FiltroAcuerdo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo/buscar");
        SgAcuerdo[] entidad = restClient.invokePost(webTarget, filtro, SgAcuerdo[].class);
        return new ArrayList<>(Arrays.asList(entidad));
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroAcuerdo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAcuerdo guardar(SgAcuerdo entidad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (entidad == null) {
            return null;
        }
        if (entidad.getAcuPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo");
            return restClient.invokePost(webTarget, entidad, SgAcuerdo.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo");
            webTarget = webTarget.path(entidad.getAcuPk().toString());
            return restClient.invokePut(webTarget, entidad, SgAcuerdo.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAcuerdo obtenerPorId(Long entidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (entidadPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo");
        webTarget = webTarget.path(entidadPk.toString());
        return restClient.invokeGet(webTarget, SgAcuerdo.class);
    }

    public void eliminar(Long entidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (entidadPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo");
        webTarget = webTarget.path(entidadPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long entidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (entidadPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo/historial");
        webTarget = webTarget.path(entidadPk.toString());
        RevHistorico[] entidad = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(entidad);
    }

    public SgAcuerdo obtenerEnRevision(Long entidadPk, Long entidadRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (entidadPk == null || entidadRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo/revision");
        webTarget = webTarget.path(entidadPk.toString());
        webTarget = webTarget.path(entidadRev.toString());
        return restClient.invokeGet(webTarget, SgAcuerdo.class);
    }

}
