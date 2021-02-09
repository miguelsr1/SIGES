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
import sv.gob.mined.siges.web.dto.RequerimientoPorRecurso;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgRequerimientoFondo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRequerimientosFondo;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 30000L)
public class RequerimientoFondoRestClient implements Serializable {

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

    public RequerimientoFondoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRequerimientoFondo> buscar(FiltroRequerimientosFondo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/requerimientosFondo/buscar");
        SgRequerimientoFondo[] mov = restClient.invokePost(webTarget, filtro, SgRequerimientoFondo[].class);
        return new ArrayList<>(Arrays.asList(mov));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroRequerimientosFondo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/requerimientosFondo/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRequerimientoFondo obtenerPorId(Long reqPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reqPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/requerimientosFondo");
        webTarget = webTarget.path(reqPk.toString());
        return restClient.invokeGet(webTarget, SgRequerimientoFondo.class);
    }

    public SgRequerimientoFondo guardar(SgRequerimientoFondo desembolsoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsoId == null) {
            return null;
        }

        if (desembolsoId.getStrPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/requerimientosFondo");
            return restClient.invokePost(webTarget, desembolsoId, SgRequerimientoFondo.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/requerimientosFondo");
            webTarget = webTarget.path(desembolsoId.getStrPk().toString());
            return restClient.invokePut(webTarget, desembolsoId, SgRequerimientoFondo.class);
        }
    }

    public void eliminar(Long desembolsoIdId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsoIdId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/requerimientosFondo");
        webTarget = webTarget.path(desembolsoIdId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long desembolsoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsoId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/requerimientosFondo/historial");
        webTarget = webTarget.path(desembolsoId.toString());
        RevHistorico[] pres = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(pres);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public SgRequerimientoFondo obtenerEnRevision(Long strPk, Long dsbRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (strPk == null || dsbRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/requerimientosFondo/revision");
        webTarget = webTarget.path(strPk.toString());
        webTarget = webTarget.path(dsbRev.toString());
        return restClient.invokeGet(webTarget, SgRequerimientoFondo.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RequerimientoPorRecurso> obtenerReqsPorRecurso() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/requerimientosFondo/acumuladas/recurso");
        RequerimientoPorRecurso[] requerimientos = restClient.invokeGet(webTarget, RequerimientoPorRecurso[].class);
        return Arrays.asList(requerimientos);
    }

}
