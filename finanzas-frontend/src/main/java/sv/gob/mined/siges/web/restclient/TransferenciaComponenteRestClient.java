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
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.siap2.SsTransferenciaComponente;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
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
public class TransferenciaComponenteRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    @Inject
    private RestClient restClient;
    
    private Client client = null;
    
   
    public TransferenciaComponenteRestClient() {
    }
    
    @PostConstruct
    public void init() {
        client = restClient.getClient();
    }


    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SsTransferenciaComponente> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferencias/componentes/buscar");
        SsTransferenciaComponente[] Transferencias = restClient.invokePost(webTarget, filtro, SsTransferenciaComponente[].class);
        return Arrays.asList(Transferencias);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/transferencias/componentes/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SsTransferenciaComponente guardar(SsTransferenciaComponente transferencia) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferencia == null || userToken == null) {
            return null;
        }
        if (transferencia.getTcId() == null) {
            WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/transferencias/componentes");
            return restClient.invokePost(webTarget, transferencia, SsTransferenciaComponente.class);
        } else {
            WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/transferencias/componentes");
            webTarget = webTarget.path(transferencia.getTcId().toString());
            return restClient.invokePut(webTarget, transferencia, SsTransferenciaComponente.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SsTransferenciaComponente obtenerPorId(Long transferenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/transferencias/componentes");
        webTarget = webTarget.path(transferenciaPk.toString());
        return restClient.invokeGet(webTarget, SsTransferenciaComponente.class);
    }

    public void eliminar(Long transferenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/transferencias/componentes");
        webTarget = webTarget.path(transferenciaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SsTransferenciaComponente> obtenerHistorialPorId(Long transferenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/transferencias/componentes/historial");
        webTarget = webTarget.path(transferenciaPk.toString());
        SsTransferenciaComponente[] Transferencias = restClient.invokeGet(webTarget, SsTransferenciaComponente[].class);
        return Arrays.asList(Transferencias);
    }
    

}
