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
import sv.gob.mined.siges.web.dto.SgConsultaGuardada;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroConsultaGuardada;

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
public class ConsultaGuardadaRestClient implements Serializable {

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

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgConsultaGuardada> buscar(FiltroConsultaGuardada filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/consultaguardad/buscar");
        SgConsultaGuardada[] consultaGuardad = restClient.invokePost(webTarget, filtro, SgConsultaGuardada[].class);
        return Arrays.asList(consultaGuardad);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroConsultaGuardada filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/consultaguardad/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgConsultaGuardada guardar(SgConsultaGuardada consultaGuardada) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (consultaGuardada == null) {
            return null;
        }
        if (consultaGuardada.getCgrPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/consultaguardad");
            return restClient.invokePost(webTarget, consultaGuardada, SgConsultaGuardada.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/consultaguardad");
            webTarget = webTarget.path(consultaGuardada.getCgrPk().toString());
            return restClient.invokePut(webTarget, consultaGuardada, SgConsultaGuardada.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgConsultaGuardada obtenerPorId(Long consultaGuardadaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (consultaGuardadaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/consultaguardad");
        webTarget = webTarget.path(consultaGuardadaPk.toString());
        return restClient.invokeGet(webTarget, SgConsultaGuardada.class);
    }

    public void eliminar(Long consultaGuardadaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (consultaGuardadaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/consultaguardad");
        webTarget = webTarget.path(consultaGuardadaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgConsultaGuardada> obtenerHistorialPorId(Long consultaGuardadaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (consultaGuardadaPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/consultaguardad/historial");
        webTarget = webTarget.path(consultaGuardadaPk.toString());
        SgConsultaGuardada[] consultaGuardad = restClient.invokeGet(webTarget, SgConsultaGuardada[].class);
        return Arrays.asList(consultaGuardad);
    }

}
