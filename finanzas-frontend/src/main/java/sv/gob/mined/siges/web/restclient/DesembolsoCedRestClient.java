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
import sv.gob.mined.siges.web.dto.SgDesembolsoCed;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.DesembolsoCE;
import sv.gob.mined.siges.web.dto.DesembolsoMovimiento;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDesembolsoCed;

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
public class DesembolsoCedRestClient implements Serializable {

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
    
    
    public DesembolsoCedRestClient() {
    }


    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgDesembolsoCed> buscar(FiltroDesembolsoCed filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsoced/buscar");
        SgDesembolsoCed[] desembolsoCed = restClient.invokePost(webTarget, filtro, SgDesembolsoCed[].class);
        return Arrays.asList(desembolsoCed);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroDesembolsoCed filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsoced/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgDesembolsoCed guardar(SgDesembolsoCed desembolsoCed) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsoCed == null || userToken == null) {
            return null;
        }
        if (desembolsoCed.getDcePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsoced");
            return restClient.invokePost(webTarget, desembolsoCed, SgDesembolsoCed.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsoced");
            webTarget = webTarget.path(desembolsoCed.getDcePk().toString());
            return restClient.invokePut(webTarget, desembolsoCed, SgDesembolsoCed.class);
        }
    }
    
    public void guardarLitado(List<DesembolsoCE> desembolsos) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsos == null || desembolsos.isEmpty() || userToken == null) {
            return;
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsoced/guardar/desembolsos");
        restClient.invokePost(webTarget, desembolsos, null);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgDesembolsoCed obtenerPorId(Long desembolsoCedPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsoCedPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsoced");
        webTarget = webTarget.path(desembolsoCedPk.toString());
        return restClient.invokeGet(webTarget, SgDesembolsoCed.class);
    }

    public void eliminar(Long desembolsoCedPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsoCedPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsoced");
        webTarget = webTarget.path(desembolsoCedPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgDesembolsoCed> obtenerHistorialPorId(Long desembolsoCedPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desembolsoCedPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsoced/historial");
        webTarget = webTarget.path(desembolsoCedPk.toString());
        SgDesembolsoCed[] desembolsoCed = restClient.invokeGet(webTarget, SgDesembolsoCed[].class);
        return Arrays.asList(desembolsoCed);
    }
    
    
    public void generarMovimientos(DesembolsoMovimiento desMov) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (desMov == null || userToken == null) {
            return;
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsoced/generar/movimientos");
        //webTarget = webTarget.path(detDesembolsoPk.toString());
        restClient.invokePost(webTarget, desMov, null);
        
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<DesembolsoCE> obtenerDesembolsosNoDep(Long reqId, Long fuenteId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reqId == null  || fuenteId==null || userToken == null) {
            return new ArrayList<>();
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/desembolsoced/nodepositados");
        webTarget = webTarget.path(reqId.toString());
        webTarget = webTarget.path(fuenteId.toString());
        DesembolsoCE[] desembolsoCed = restClient.invokeGet(webTarget, DesembolsoCE[].class);
        return Arrays.asList(desembolsoCed);
        
    }

}
