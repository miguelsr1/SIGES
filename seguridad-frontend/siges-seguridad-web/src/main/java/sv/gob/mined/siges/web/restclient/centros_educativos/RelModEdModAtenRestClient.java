/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient.centros_educativos;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.dto.centros_educativos.SgRelModEdModAten;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.restclient.RestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class RelModEdModAtenRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public RelModEdModAtenRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRelModEdModAten> buscar(FiltroRelModEdModAten filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten/buscar");
        SgRelModEdModAten[] relModEdModAten = RestClient.invokePost(webTarget, filtro, SgRelModEdModAten[].class, userToken);
        return Arrays.asList(relModEdModAten);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroRelModEdModAten filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgRelModEdModAten guardar(SgRelModEdModAten relModEdModAten) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relModEdModAten == null) {
            return null;
        }
        if (relModEdModAten.getReaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten");
            return RestClient.invokePost(webTarget, relModEdModAten, SgRelModEdModAten.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten");
            webTarget = webTarget.path(relModEdModAten.getReaPk().toString());
            return RestClient.invokePut(webTarget, relModEdModAten, SgRelModEdModAten.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgRelModEdModAten obtenerPorId(Long relModEdModAtenPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relModEdModAtenPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten");
        webTarget = webTarget.path(relModEdModAtenPk.toString());
        return RestClient.invokeGet(webTarget, SgRelModEdModAten.class, userToken);
    }

    public void eliminar(Long relModEdModAtenPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relModEdModAtenPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten");
        webTarget = webTarget.path(relModEdModAtenPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long relModEdModAtenPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relModEdModAtenPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten/historial");
        webTarget = webTarget.path(relModEdModAtenPk.toString());
        RevHistorico[] relModEdModAten = RestClient.invokeGet(webTarget, RevHistorico[].class, userToken);
        return Arrays.asList(relModEdModAten);
    }

    public SgRelModEdModAten obtenerEnRevision(Long relModEdModAtenPk, Long relModEdModAtenRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relModEdModAtenPk == null || relModEdModAtenRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relmodedmodaten/revision");
        webTarget = webTarget.path(relModEdModAtenPk.toString());
        webTarget = webTarget.path(relModEdModAtenRev.toString());
        return RestClient.invokeGet(webTarget, SgRelModEdModAten.class, userToken);
    }

}
