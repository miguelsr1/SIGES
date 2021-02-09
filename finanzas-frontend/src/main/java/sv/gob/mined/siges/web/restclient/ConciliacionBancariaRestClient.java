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
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgConciliacionesBancarias;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroConciliacionBancaria;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 30000L)
public class ConciliacionBancariaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

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

    public ConciliacionBancariaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgConciliacionesBancarias> buscar(FiltroConciliacionBancaria filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/conciliaciones/buscar");
        SgConciliacionesBancarias[] conc = restClient.invokePost(webTarget, filtro, SgConciliacionesBancarias[].class);
        return new ArrayList<>(Arrays.asList(conc));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroConciliacionBancaria filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/conciliaciones/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgConciliacionesBancarias obtenerPorId(Long conciliacionesId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (conciliacionesId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/conciliaciones");
        webTarget = webTarget.path(conciliacionesId.toString());
        return restClient.invokeGet(webTarget, SgConciliacionesBancarias.class);
    }

    public SgConciliacionesBancarias guardar(SgConciliacionesBancarias conciliaciones) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (conciliaciones == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(conciliaciones);

        if (conciliaciones.getConPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/conciliaciones");
            return restClient.invokePost(webTarget, conciliaciones, SgConciliacionesBancarias.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/conciliaciones");
            webTarget = webTarget.path(conciliaciones.getConPk().toString());
            return restClient.invokePut(webTarget, conciliaciones, SgConciliacionesBancarias.class);
        }
    }

    public void eliminar(Long conciliacionesId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (conciliacionesId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/conciliaciones");
        webTarget = webTarget.path(conciliacionesId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long conciliacionesId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (conciliacionesId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/conciliaciones/historial");
        webTarget = webTarget.path(conciliacionesId.toString());
        RevHistorico[] rev = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(rev);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgConciliacionesBancarias obtenerEnRevision(Long conciliacionesPk, Long revConciliacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (conciliacionesPk == null || revConciliacionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/conciliaciones/revision");
        webTarget = webTarget.path(conciliacionesPk.toString());
        webTarget = webTarget.path(revConciliacionPk.toString());
        return restClient.invokeGet(webTarget, SgConciliacionesBancarias.class);
    }

}
