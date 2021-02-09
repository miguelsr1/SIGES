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
import sv.gob.mined.siges.web.dto.SgHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHabilitacionPeriodoMatricula;

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
public class HabilitacionPeriodoMatriculaRestClient implements Serializable {

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
    
    public HabilitacionPeriodoMatriculaRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgHabilitacionPeriodoMatricula> buscar(FiltroHabilitacionPeriodoMatricula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget =RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habiltacionperiodomatricula/buscar");
        SgHabilitacionPeriodoMatricula[] habiltacionPeriodoMatricula = restClient.invokePost(webTarget, filtro, SgHabilitacionPeriodoMatricula[].class);
        return Arrays.asList(habiltacionPeriodoMatricula);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroHabilitacionPeriodoMatricula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget =RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habiltacionperiodomatricula/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgHabilitacionPeriodoMatricula guardar(SgHabilitacionPeriodoMatricula habilitacionPeriodoMatricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoMatricula == null ) {
            return null;
        }
        if (habilitacionPeriodoMatricula.getHmpPk() == null) {
            WebTarget webTarget =RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habiltacionperiodomatricula");
            return restClient.invokePost(webTarget, habilitacionPeriodoMatricula, SgHabilitacionPeriodoMatricula.class);
        } else {
            WebTarget webTarget =RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habiltacionperiodomatricula");
            webTarget = webTarget.path(habilitacionPeriodoMatricula.getHmpPk().toString());
            return restClient.invokePut(webTarget, habilitacionPeriodoMatricula, SgHabilitacionPeriodoMatricula.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgHabilitacionPeriodoMatricula obtenerPorId(Long habilitacionPeriodoMatriculaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoMatriculaPk == null ) {
            return null;
        }
        WebTarget webTarget =RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habiltacionperiodomatricula");
        webTarget = webTarget.path(habilitacionPeriodoMatriculaPk.toString());
        return restClient.invokeGet(webTarget, SgHabilitacionPeriodoMatricula.class);
    }

    public void eliminar(Long habilitacionPeriodoMatriculaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoMatriculaPk == null ) {
            return;
        }
        WebTarget webTarget =RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habiltacionperiodomatricula");
        webTarget = webTarget.path(habilitacionPeriodoMatriculaPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long habilitacionPeriodoMatriculaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoMatriculaPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habiltacionperiodomatricula/historial");
        webTarget = webTarget.path(habilitacionPeriodoMatriculaPk.toString());
        RevHistorico[] habiltacionPeriodoMatricula = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(habiltacionPeriodoMatricula);
    }
    
    public SgHabilitacionPeriodoMatricula aprobar(SgHabilitacionPeriodoMatricula habilitacionPeriodoMatricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoMatricula == null) {
            return null;
        }
       
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habiltacionperiodomatricula/aprobar");
        return restClient.invokePost(webTarget, habilitacionPeriodoMatricula, SgHabilitacionPeriodoMatricula.class);
     
    }
    
    public SgHabilitacionPeriodoMatricula rechazar(SgHabilitacionPeriodoMatricula habilitacionPeriodoMatricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habilitacionPeriodoMatricula == null) {
            return null;
        }
       
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habiltacionperiodomatricula/rechazar");
        return restClient.invokePost(webTarget, habilitacionPeriodoMatricula, SgHabilitacionPeriodoMatricula.class);
     
    }

}
