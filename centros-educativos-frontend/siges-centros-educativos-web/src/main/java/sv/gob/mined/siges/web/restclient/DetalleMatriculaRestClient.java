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
import sv.gob.mined.siges.web.dto.SgDetalleMatricula;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDetalleMatricula;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 10000L)
public class DetalleMatriculaRestClient implements Serializable {

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
    
    public DetalleMatriculaRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgDetalleMatricula> buscar(FiltroDetalleMatricula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detallematricula/buscar");
        SgDetalleMatricula[] detalleMatricula = restClient.invokePost(webTarget, filtro, SgDetalleMatricula[].class);
        return Arrays.asList(detalleMatricula);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroDetalleMatricula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detallematricula/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgDetalleMatricula guardar(SgDetalleMatricula detalleMatricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (detalleMatricula == null ) {
            return null;
        }
        if (detalleMatricula.getDemPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detallematricula");
            return restClient.invokePost(webTarget, detalleMatricula, SgDetalleMatricula.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detallematricula");
            webTarget = webTarget.path(detalleMatricula.getDemPk().toString());
            return restClient.invokePut(webTarget, detalleMatricula, SgDetalleMatricula.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgDetalleMatricula obtenerPorId(Long detalleMatriculaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (detalleMatriculaPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detallematricula");
        webTarget = webTarget.path(detalleMatriculaPk.toString());
        return restClient.invokeGet(webTarget, SgDetalleMatricula.class);
    }

    public void eliminar(Long detalleMatriculaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (detalleMatriculaPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detallematricula");
        webTarget = webTarget.path(detalleMatriculaPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long detalleMatriculaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (detalleMatriculaPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/detallematricula/historial");
        webTarget = webTarget.path(detalleMatriculaPk.toString());
        RevHistorico[] detalleMatricula = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(detalleMatricula);
    }
    

}
