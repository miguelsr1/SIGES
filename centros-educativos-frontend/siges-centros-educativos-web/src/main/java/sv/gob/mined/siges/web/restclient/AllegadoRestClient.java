
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
import sv.gob.mined.siges.web.dto.SgAllegado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAllegado;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class AllegadoRestClient implements Serializable {

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

    public AllegadoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAllegado> buscar(FiltroAllegado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/allegados/buscar");
        SgAllegado[] sede = restClient.invokePost(webTarget, filtro, SgAllegado[].class);
        return Arrays.asList(sede);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroAllegado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/allegados/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAllegado obtenerPorId(Long familiarId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (familiarId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/allegados");
        webTarget = webTarget.path(familiarId.toString());
        return restClient.invokeGet(webTarget, SgAllegado.class);
    }

    public SgAllegado guardar(SgAllegado familiar) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (familiar == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(familiar);
        ViewIdUtils.clearChildViewIds(familiar.getAllPersona());

        if (familiar.getAllPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/allegados");
            return restClient.invokePost(webTarget, familiar, SgAllegado.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/allegados");
            webTarget = webTarget.path(familiar.getAllPk().toString());
            return restClient.invokePut(webTarget, familiar, SgAllegado.class);
        }
    }

    public Boolean validar(SgAllegado familiar) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (familiar == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/allegados/validar");
        return restClient.invokePost(webTarget, familiar, Boolean.class);
    }

    public void eliminar(Long familiarId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (familiarId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/allegados");
        webTarget = webTarget.path(familiarId.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long familiarId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (familiarId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/allegados/historial");
        webTarget = webTarget.path(familiarId.toString());
        RevHistorico[] sede = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(sede);
    }

    public SgAllegado obtenerEnRevision(Long estudiantePk, Long estudianteRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null || estudianteRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/allegados/revision");
        webTarget = webTarget.path(estudiantePk.toString());
        webTarget = webTarget.path(estudianteRev.toString());
        return restClient.invokeGet(webTarget, SgAllegado.class);
    }

}
