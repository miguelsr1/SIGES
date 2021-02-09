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
import sv.gob.mined.siges.web.dto.SgRelPeriodosHabilitacionPeriodo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelPeriodosHabilitacionPeriodo;

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
public class RelPeriodosHabilitacionPeriodoRestClient implements Serializable {

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
    
    public RelPeriodosHabilitacionPeriodoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelPeriodosHabilitacionPeriodo> buscar(FiltroRelPeriodosHabilitacionPeriodo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relperiodoshabilitacionperiodo/buscar");
        SgRelPeriodosHabilitacionPeriodo[] relPeriodosHabilitacionPeriodo = restClient.invokePost(webTarget, filtro, SgRelPeriodosHabilitacionPeriodo[].class);
        return Arrays.asList(relPeriodosHabilitacionPeriodo);
    }
    
    
            
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRangoFecha> buscarRangosConSolicitud(FiltroRelPeriodosHabilitacionPeriodo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relperiodoshabilitacionperiodo/buscarRangosConSolicitud");
        SgRangoFecha[] relPeriodosHabilitacionPeriodo = restClient.invokePost(webTarget, filtro, SgRangoFecha[].class);
        return Arrays.asList(relPeriodosHabilitacionPeriodo);
    }
            

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<EnumCalendarioEscolar> buscarPeriodoExtraordinarioConSolicitud(FiltroRelPeriodosHabilitacionPeriodo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relperiodoshabilitacionperiodo/buscarPeriodoExtraordinarioConSolicitud");
        EnumCalendarioEscolar[] relPeriodosHabilitacionPeriodo = restClient.invokePost(webTarget, filtro, EnumCalendarioEscolar[].class);
        return Arrays.asList(relPeriodosHabilitacionPeriodo);
    }        
            
	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelPeriodosHabilitacionPeriodo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relperiodoshabilitacionperiodo/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelPeriodosHabilitacionPeriodo guardar(SgRelPeriodosHabilitacionPeriodo relPeriodosHabilitacionPeriodo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relPeriodosHabilitacionPeriodo == null ) {
            return null;
        }
        if (relPeriodosHabilitacionPeriodo.getRphPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relperiodoshabilitacionperiodo");
            return restClient.invokePost(webTarget, relPeriodosHabilitacionPeriodo, SgRelPeriodosHabilitacionPeriodo.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relperiodoshabilitacionperiodo");
            webTarget = webTarget.path(relPeriodosHabilitacionPeriodo.getRphPk().toString());
            return restClient.invokePut(webTarget, relPeriodosHabilitacionPeriodo, SgRelPeriodosHabilitacionPeriodo.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelPeriodosHabilitacionPeriodo obtenerPorId(Long relPeriodosHabilitacionPeriodoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relPeriodosHabilitacionPeriodoPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relperiodoshabilitacionperiodo");
        webTarget = webTarget.path(relPeriodosHabilitacionPeriodoPk.toString());
        return restClient.invokeGet(webTarget, SgRelPeriodosHabilitacionPeriodo.class);
    }

    public void eliminar(Long relPeriodosHabilitacionPeriodoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relPeriodosHabilitacionPeriodoPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relperiodoshabilitacionperiodo");
        webTarget = webTarget.path(relPeriodosHabilitacionPeriodoPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relPeriodosHabilitacionPeriodoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relPeriodosHabilitacionPeriodoPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relperiodoshabilitacionperiodo/historial");
        webTarget = webTarget.path(relPeriodosHabilitacionPeriodoPk.toString());
        RevHistorico[] relPeriodosHabilitacionPeriodo = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relPeriodosHabilitacionPeriodo);
    }
    

}
