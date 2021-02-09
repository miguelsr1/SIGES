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
import sv.gob.mined.siges.web.dto.SgOrganismoCESede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOrganismoCESede;


@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 22250)
@Timeout(value = 60000L)
public class OrganismoCESedeRestClient implements Serializable {

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

    public OrganismoCESedeRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgOrganismoCESede> buscar(FiltroOrganismoCESede filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoscesedes/buscar");
        SgOrganismoCESede[] casoViolaciones = restClient.invokePost(webTarget, filtro, SgOrganismoCESede[].class);
        return new ArrayList<>(Arrays.asList(casoViolaciones));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroOrganismoCESede filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoscesedes/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgOrganismoCESede guardar(SgOrganismoCESede casoViolacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (casoViolacion == null) {
            return null;
        }
        if (casoViolacion.getOcsPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoscesedes");
            return restClient.invokePost(webTarget, casoViolacion, SgOrganismoCESede.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoscesedes");
            webTarget = webTarget.path(casoViolacion.getOcsPk().toString());
            return restClient.invokePut(webTarget, casoViolacion, SgOrganismoCESede.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgOrganismoCESede obtenerPorId(Long casoViolacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (casoViolacionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoscesedes");
        webTarget = webTarget.path(casoViolacionPk.toString());
        return restClient.invokeGet(webTarget, SgOrganismoCESede.class);
    }

    public void eliminar(Long casoViolacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (casoViolacionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoscesedes");
        webTarget = webTarget.path(casoViolacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long casoViolacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (casoViolacionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoscesedes/historial");
        webTarget = webTarget.path(casoViolacionPk.toString());
        RevHistorico[] casoViolaciones = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(casoViolaciones);
    }

    public SgOrganismoCESede obtenerEnRevision(Long casoViolacionPk, Long casoViolacionRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (casoViolacionPk == null || casoViolacionRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoscesedes/revision");
        webTarget = webTarget.path(casoViolacionPk.toString());
        webTarget = webTarget.path(casoViolacionRev.toString());
        return restClient.invokeGet(webTarget, SgOrganismoCESede.class);
    }

}
