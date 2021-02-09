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
import sv.gob.mined.siges.web.dto.SgDetalleDesembolso;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDetalleDesembolso;

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
public class DetalleDesembolsosRestClient implements Serializable {

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
    
    public DetalleDesembolsosRestClient() {
    }


    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgDetalleDesembolso> buscar(FiltroDetalleDesembolso filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/detalledesembolso/buscar");
        SgDetalleDesembolso[] detalleDesembolso = restClient.invokePost(webTarget, filtro, SgDetalleDesembolso[].class);
        return Arrays.asList(detalleDesembolso);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroDetalleDesembolso filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/detalledesembolso/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgDetalleDesembolso guardar(SgDetalleDesembolso detalleDesembolsos) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (detalleDesembolsos == null || userToken == null) {
            return null;
        }
        if (detalleDesembolsos.getDedPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/detalledesembolso");
            return restClient.invokePost(webTarget, detalleDesembolsos, SgDetalleDesembolso.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/detalledesembolso");
            webTarget = webTarget.path(detalleDesembolsos.getDedPk().toString());
            return restClient.invokePut(webTarget, detalleDesembolsos, SgDetalleDesembolso.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgDetalleDesembolso obtenerPorId(Long detalleDesembolsosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (detalleDesembolsosPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/detalledesembolso");
        webTarget = webTarget.path(detalleDesembolsosPk.toString());
        return restClient.invokeGet(webTarget, SgDetalleDesembolso.class);
    }

    public void eliminar(Long detalleDesembolsosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (detalleDesembolsosPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/detalledesembolso");
        webTarget = webTarget.path(detalleDesembolsosPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgDetalleDesembolso> obtenerHistorialPorId(Long detalleDesembolsosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (detalleDesembolsosPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/detalledesembolso/historial");
        webTarget = webTarget.path(detalleDesembolsosPk.toString());
        SgDetalleDesembolso[] detalleDesembolso = restClient.invokeGet(webTarget, SgDetalleDesembolso[].class);
        return Arrays.asList(detalleDesembolso);
    }
    

}
