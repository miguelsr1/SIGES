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
import sv.gob.mined.siges.web.dto.SgHabitosAlimentacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHabitosAlimentacion;

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
public class HabitosAlimentacionRestClient implements Serializable {

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

    public HabitosAlimentacionRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgHabitosAlimentacion> buscar(FiltroHabitosAlimentacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitosalimentacion/buscar");
        SgHabitosAlimentacion[] habitosAlimentacion = restClient.invokePost(webTarget, filtro, SgHabitosAlimentacion[].class);
        return Arrays.asList(habitosAlimentacion);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroHabitosAlimentacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitosalimentacion/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgHabitosAlimentacion guardar(SgHabitosAlimentacion habitosAlimentacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habitosAlimentacion == null ) {
            return null;
        }
        if (habitosAlimentacion.getHalPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitosalimentacion");
            return restClient.invokePost(webTarget, habitosAlimentacion, SgHabitosAlimentacion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitosalimentacion");
            webTarget = webTarget.path(habitosAlimentacion.getHalPk().toString());
            return restClient.invokePut(webTarget, habitosAlimentacion, SgHabitosAlimentacion.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgHabitosAlimentacion obtenerPorId(Long habitosAlimentacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habitosAlimentacionPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitosalimentacion");
        webTarget = webTarget.path(habitosAlimentacionPk.toString());
        return restClient.invokeGet(webTarget, SgHabitosAlimentacion.class);
    }

    public void eliminar(Long habitosAlimentacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habitosAlimentacionPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitosalimentacion");
        webTarget = webTarget.path(habitosAlimentacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long habitosAlimentacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (habitosAlimentacionPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/habitosalimentacion/historial");
        webTarget = webTarget.path(habitosAlimentacionPk.toString());
        RevHistorico[] habitosAlimentacion = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(habitosAlimentacion);
    }
    

}
