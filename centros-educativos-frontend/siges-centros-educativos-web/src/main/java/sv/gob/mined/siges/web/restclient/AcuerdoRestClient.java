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
import sv.gob.mined.siges.web.dto.SgAcuerdo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtros.FiltroAcuerdo;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@Traced
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class AcuerdoRestClient implements Serializable {

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
    
    public AcuerdoRestClient() {
    }


	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAcuerdo> buscar(FiltroAcuerdo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo/buscar");
        SgAcuerdo[] acuerdo = restClient.invokePost(webTarget, filtro, SgAcuerdo[].class);
        return Arrays.asList(acuerdo);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroAcuerdo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAcuerdo guardar(SgAcuerdo acuerdo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (acuerdo == null) {
            return null;
        }
        if (acuerdo.getAcuPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo");
            return restClient.invokePost(webTarget, acuerdo, SgAcuerdo.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo");
            webTarget = webTarget.path(acuerdo.getAcuPk().toString());
            return restClient.invokePut(webTarget, acuerdo, SgAcuerdo.class);
        }
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAcuerdo obtenerPorId(Long acuerdoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (acuerdoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo");
        webTarget = webTarget.path(acuerdoPk.toString());
        return restClient.invokeGet(webTarget, SgAcuerdo.class);
    }

    public void eliminar(Long acuerdoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (acuerdoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo");
        webTarget = webTarget.path(acuerdoPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAcuerdo> obtenerHistorialPorId(Long acuerdoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (acuerdoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/acuerdo/historial");
        webTarget = webTarget.path(acuerdoPk.toString());
        SgAcuerdo[] acuerdo = restClient.invokeGet(webTarget, SgAcuerdo[].class);
        return Arrays.asList(acuerdo);
    }
    

}
