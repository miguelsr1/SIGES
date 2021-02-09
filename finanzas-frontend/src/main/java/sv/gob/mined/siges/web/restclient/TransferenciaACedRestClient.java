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
import sv.gob.mined.siges.web.dto.SgTransferenciaACed;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferenciaACed;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.TransferenciaCedAgrup;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@Traced
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class TransferenciaACedRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    @Inject
    private RestClient restClient;
    
    private Client client = null;
    
    public TransferenciaACedRestClient() {
    }
    
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


    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTransferenciaACed> buscar(FiltroTransferenciaACed filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasaced/buscar");
        SgTransferenciaACed[] transferenciasACed = restClient.invokePost(webTarget, filtro, SgTransferenciaACed[].class);
        return new ArrayList<>(Arrays.asList(transferenciasACed));
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroTransferenciaACed filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasaced/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgTransferenciaACed guardar(SgTransferenciaACed transferenciaACed) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaACed == null || userToken == null) {
            return null;
        }
        if (transferenciaACed.getTacPk() == null) {
            WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasaced");
            return restClient.invokePost(webTarget, transferenciaACed, SgTransferenciaACed.class);
        } else {
            WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasaced");
            webTarget = webTarget.path(transferenciaACed.getTacPk().toString());
            return restClient.invokePut(webTarget, transferenciaACed, SgTransferenciaACed.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTransferenciaACed obtenerPorId(Long transferenciaACedPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaACedPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasaced");
        webTarget = webTarget.path(transferenciaACedPk.toString());
        return restClient.invokeGet(webTarget, SgTransferenciaACed.class);
    }

    public void eliminar(Long transferenciaACedPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaACedPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasaced");
        webTarget = webTarget.path(transferenciaACedPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTransferenciaACed> obtenerHistorialPorId(Long transferenciaACedPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaACedPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasaced/historial");
        webTarget = webTarget.path(transferenciaACedPk.toString());
        SgTransferenciaACed[] transferenciasACed = restClient.invokeGet(webTarget, SgTransferenciaACed[].class);
        return Arrays.asList(transferenciasACed);
    }
    
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<TransferenciaCedAgrup> obtenerTransferenciasAgrupadas(Long transferenciaId,Long departamentoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaId == null && departamentoId==null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasaced/agrupadas");
        webTarget = webTarget.path(transferenciaId.toString());
        webTarget = webTarget.path(departamentoId.toString());
        TransferenciaCedAgrup[] transferencias = restClient.invokeGet(webTarget, TransferenciaCedAgrup[].class);
        return Arrays.asList(transferencias);
    }

}
