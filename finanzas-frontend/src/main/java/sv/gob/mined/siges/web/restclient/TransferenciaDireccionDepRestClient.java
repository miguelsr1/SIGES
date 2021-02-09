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
import sv.gob.mined.siges.web.dto.SgTransferenciaDireccionDep;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferenciaDireccionDep;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;

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
public class TransferenciaDireccionDepRestClient implements Serializable {
    
    
    @Inject
    @Named("userToken")
    private String userToken;
    
    @Inject
    private RestClient restClient;
    
    private Client client = null;
    
    public TransferenciaDireccionDepRestClient() {
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
    public List<SgTransferenciaDireccionDep> buscar(FiltroTransferenciaDireccionDep filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasdirecciondepartamental/buscar");
        SgTransferenciaDireccionDep[] TransferenciasDirecciónDepartamental = restClient.invokePost(webTarget, filtro, SgTransferenciaDireccionDep[].class);
        return Arrays.asList(TransferenciasDirecciónDepartamental);
    }
    
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroTransferenciaDireccionDep filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasdirecciondepartamental/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgTransferenciaDireccionDep guardar(SgTransferenciaDireccionDep transferenciaDireccionDep) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaDireccionDep == null || userToken == null) {
            return null;
        }
        if (transferenciaDireccionDep.getTddPk() == null) {
            WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasdirecciondepartamental");
            return restClient.invokePost(webTarget, transferenciaDireccionDep, SgTransferenciaDireccionDep.class);
        } else {
            WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasdirecciondepartamental");
            webTarget = webTarget.path(transferenciaDireccionDep.getTddPk().toString());
            return restClient.invokePut(webTarget, transferenciaDireccionDep, SgTransferenciaDireccionDep.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTransferenciaDireccionDep obtenerPorId(Long transferenciaDireccionDepPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaDireccionDepPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasdirecciondepartamental");
        webTarget = webTarget.path(transferenciaDireccionDepPk.toString());
        return restClient.invokeGet(webTarget, SgTransferenciaDireccionDep.class);
    }

    public void eliminar(Long transferenciaDireccionDepPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaDireccionDepPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasdirecciondepartamental");
        webTarget = webTarget.path(transferenciaDireccionDepPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTransferenciaDireccionDep> obtenerHistorialPorId(Long transferenciaDireccionDepPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaDireccionDepPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferenciasdirecciondepartamental/historial");
        webTarget = webTarget.path(transferenciaDireccionDepPk.toString());
        SgTransferenciaDireccionDep[] TransferenciasDirecciónDepartamental = restClient.invokeGet(webTarget, SgTransferenciaDireccionDep[].class);
        return Arrays.asList(TransferenciasDirecciónDepartamental);
    }
    

}
