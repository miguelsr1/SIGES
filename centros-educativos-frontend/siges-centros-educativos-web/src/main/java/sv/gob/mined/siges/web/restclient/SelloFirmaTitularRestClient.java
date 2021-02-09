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
import sv.gob.mined.siges.web.dto.SgSelloFirmaTitular;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirmaTitular;

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
public class SelloFirmaTitularRestClient implements Serializable {

    private Client client = null;
    
    @Inject
    private RestClient restClient;
    
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
    
    public SelloFirmaTitularRestClient() {
    }


    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgSelloFirmaTitular> buscar(FiltroSelloFirmaTitular filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sellofirmatitular/buscar");
        SgSelloFirmaTitular[] selloFirmaTitular = restClient.invokePost(webTarget, filtro, SgSelloFirmaTitular[].class);
        return Arrays.asList(selloFirmaTitular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroSelloFirmaTitular filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sellofirmatitular/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgSelloFirmaTitular guardar(SgSelloFirmaTitular selloFirmaTitular) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (selloFirmaTitular == null ) {
            return null;
        }
        if (selloFirmaTitular.getSftPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sellofirmatitular");
            return restClient.invokePost(webTarget, selloFirmaTitular, SgSelloFirmaTitular.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sellofirmatitular");
            webTarget = webTarget.path(selloFirmaTitular.getSftPk().toString());
            return restClient.invokePut(webTarget, selloFirmaTitular, SgSelloFirmaTitular.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgSelloFirmaTitular obtenerPorId(Long selloFirmaTitularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (selloFirmaTitularPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sellofirmatitular");
        webTarget = webTarget.path(selloFirmaTitularPk.toString());
        return restClient.invokeGet(webTarget, SgSelloFirmaTitular.class);
    }

    public void eliminar(Long selloFirmaTitularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (selloFirmaTitularPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sellofirmatitular");
        webTarget = webTarget.path(selloFirmaTitularPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long selloFirmaTitularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (selloFirmaTitularPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/sellofirmatitular/historial");
        webTarget = webTarget.path(selloFirmaTitularPk.toString());
        RevHistorico[] selloFirmaTitular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(selloFirmaTitular);
    }
    

}
