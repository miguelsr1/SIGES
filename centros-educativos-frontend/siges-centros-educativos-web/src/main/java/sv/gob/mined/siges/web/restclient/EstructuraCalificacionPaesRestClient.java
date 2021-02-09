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
import sv.gob.mined.siges.web.dto.SgEstructuraCalificacionPaes;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstructuraCalificacionPaes;

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
@Timeout(value = 10000L)
public class EstructuraCalificacionPaesRestClient implements Serializable {

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
    
    public EstructuraCalificacionPaesRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstructuraCalificacionPaes> buscar(FiltroEstructuraCalificacionPaes filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estructuracalificacionpaes/buscar");
        SgEstructuraCalificacionPaes[] estructuraCalificacionPAES = restClient.invokePost(webTarget, filtro, SgEstructuraCalificacionPaes[].class);
        return Arrays.asList(estructuraCalificacionPAES);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroEstructuraCalificacionPaes filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estructuracalificacionpaes/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgEstructuraCalificacionPaes guardar(SgEstructuraCalificacionPaes estructuraCalificacionPaes) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estructuraCalificacionPaes == null ) {
            return null;
        }
        if (estructuraCalificacionPaes.getEcpPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estructuracalificacionpaes");
            return restClient.invokePost(webTarget, estructuraCalificacionPaes, SgEstructuraCalificacionPaes.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estructuracalificacionpaes");
            webTarget = webTarget.path(estructuraCalificacionPaes.getEcpPk().toString());
            return restClient.invokePut(webTarget, estructuraCalificacionPaes, SgEstructuraCalificacionPaes.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEstructuraCalificacionPaes obtenerPorId(Long estructuraCalificacionPaesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estructuraCalificacionPaesPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estructuracalificacionpaes");
        webTarget = webTarget.path(estructuraCalificacionPaesPk.toString());
        return restClient.invokeGet(webTarget, SgEstructuraCalificacionPaes.class);
    }

    public void eliminar(Long estructuraCalificacionPaesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estructuraCalificacionPaesPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estructuracalificacionpaes");
        webTarget = webTarget.path(estructuraCalificacionPaesPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstructuraCalificacionPaes> obtenerHistorialPorId(Long estructuraCalificacionPaesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estructuraCalificacionPaesPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estructuracalificacionpaes/historial");
        webTarget = webTarget.path(estructuraCalificacionPaesPk.toString());
        SgEstructuraCalificacionPaes[] estructuraCalificacionPAES = restClient.invokeGet(webTarget, SgEstructuraCalificacionPaes[].class);
        return Arrays.asList(estructuraCalificacionPAES);
        
        
    }
    

}
