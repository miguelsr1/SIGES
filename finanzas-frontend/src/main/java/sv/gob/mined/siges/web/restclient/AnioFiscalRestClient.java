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
import sv.gob.mined.siges.web.dto.SgAnioFiscal;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioFiscal;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class AnioFiscalRestClient implements Serializable {

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

    public AnioFiscalRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAnioFiscal> buscar(FiltroAnioFiscal filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/aniosfiscales/buscar");
        SgAnioFiscal[] anioFiscal = restClient.invokePost(webTarget, filtro, SgAnioFiscal[].class);
        return new ArrayList<>(Arrays.asList(anioFiscal));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroAnioFiscal filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/aniosfiscales/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAnioFiscal guardar(SgAnioFiscal anioFiscal) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioFiscal == null) {
            return null;
        }
        if (anioFiscal.getAniPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/aniosfiscales");
            return restClient.invokePost(webTarget, anioFiscal, SgAnioFiscal.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/aniosfiscales");
            webTarget = webTarget.path(anioFiscal.getAniPk().toString());
            return restClient.invokePut(webTarget, anioFiscal, SgAnioFiscal.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAnioFiscal obtenerPorId(Long anioFiscalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioFiscalPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/aniosfiscales");
        webTarget = webTarget.path(anioFiscalPk.toString());
        return restClient.invokeGet(webTarget, SgAnioFiscal.class);
    }

    public void eliminar(Long anioFiscalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioFiscalPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/aniosfiscales");
        webTarget = webTarget.path(anioFiscalPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long anioFiscalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioFiscalPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/aniosfiscales/historial");
        webTarget = webTarget.path(anioFiscalPk.toString());
        RevHistorico[] anioFiscal = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(anioFiscal);
    }

    public SgAnioFiscal obtenerEnRevision(Long anioFiscalPk, Long anioFiscalRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioFiscalPk == null || anioFiscalRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/aniosfiscales/revision");
        webTarget = webTarget.path(anioFiscalPk.toString());
        webTarget = webTarget.path(anioFiscalRev.toString());
        return restClient.invokeGet(webTarget, SgAnioFiscal.class);
    }

}
