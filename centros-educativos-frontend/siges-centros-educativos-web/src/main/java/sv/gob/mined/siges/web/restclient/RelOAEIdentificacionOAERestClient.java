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
import sv.gob.mined.siges.web.dto.SgRelOAEIdentificacionOAE;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelOAEIdentificacionOAE;

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
public class RelOAEIdentificacionOAERestClient implements Serializable {

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
    
    public RelOAEIdentificacionOAERestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelOAEIdentificacionOAE> buscar(FiltroRelOAEIdentificacionOAE filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relacionoaeidentificacionoae/buscar");
        SgRelOAEIdentificacionOAE[] relacionOAEidentificacionOAE = restClient.invokePost(webTarget, filtro, SgRelOAEIdentificacionOAE[].class);
        return Arrays.asList(relacionOAEidentificacionOAE);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelOAEIdentificacionOAE filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relacionoaeidentificacionoae/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelOAEIdentificacionOAE guardar(SgRelOAEIdentificacionOAE relOAEIdentificacionOAE) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOAEIdentificacionOAE == null ) {
            return null;
        }
        if (relOAEIdentificacionOAE.getRioPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relacionoaeidentificacionoae");
            return restClient.invokePost(webTarget, relOAEIdentificacionOAE, SgRelOAEIdentificacionOAE.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relacionoaeidentificacionoae");
            webTarget = webTarget.path(relOAEIdentificacionOAE.getRioPk().toString());
            return restClient.invokePut(webTarget, relOAEIdentificacionOAE, SgRelOAEIdentificacionOAE.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelOAEIdentificacionOAE obtenerPorId(Long relOAEIdentificacionOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOAEIdentificacionOAEPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relacionoaeidentificacionoae");
        webTarget = webTarget.path(relOAEIdentificacionOAEPk.toString());
        return restClient.invokeGet(webTarget, SgRelOAEIdentificacionOAE.class);
    }

    public void eliminar(Long relOAEIdentificacionOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOAEIdentificacionOAEPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relacionoaeidentificacionoae");
        webTarget = webTarget.path(relOAEIdentificacionOAEPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relOAEIdentificacionOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOAEIdentificacionOAEPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relacionoaeidentificacionoae/historial");
        webTarget = webTarget.path(relOAEIdentificacionOAEPk.toString());
        RevHistorico[] relacionOAEidentificacionOAE = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relacionOAEidentificacionOAE);
    }
    

}
