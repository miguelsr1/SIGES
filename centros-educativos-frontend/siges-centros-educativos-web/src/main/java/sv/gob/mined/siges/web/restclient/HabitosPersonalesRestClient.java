/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgHabitosPersonales;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHabitosPersonales;

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
public class HabitosPersonalesRestClient implements Serializable {

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

    public HabitosPersonalesRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgHabitosPersonales> buscar(FiltroHabitosPersonales filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitospersonales/buscar");
        SgHabitosPersonales[] habitosPersonales = restClient.invokePost(webTarget, filtro, SgHabitosPersonales[].class);
        return Arrays.asList(habitosPersonales);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroHabitosPersonales filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitospersonales/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgHabitosPersonales guardar(SgHabitosPersonales habitosPersonales) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habitosPersonales == null ) {
            return null;
        }
        if (habitosPersonales.getHapPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitospersonales");
            return restClient.invokePost(webTarget, habitosPersonales, SgHabitosPersonales.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitospersonales");
            webTarget = webTarget.path(habitosPersonales.getHapPk().toString());
            return restClient.invokePut(webTarget, habitosPersonales, SgHabitosPersonales.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgHabitosPersonales obtenerPorId(Long habitosPersonalesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habitosPersonalesPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitospersonales");
        webTarget = webTarget.path(habitosPersonalesPk.toString());
        return restClient.invokeGet(webTarget, SgHabitosPersonales.class);
    }

    public void eliminar(Long habitosPersonalesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habitosPersonalesPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitospersonales");
        webTarget = webTarget.path(habitosPersonalesPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long habitosPersonalesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habitosPersonalesPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitospersonales/historial");
        webTarget = webTarget.path(habitosPersonalesPk.toString());
        RevHistorico[] habitosPersonales = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(habitosPersonales);
    }
    

}
