/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
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
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCapacitacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class CapacitacionRestClient implements Serializable {

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

    public CapacitacionRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgCapacitacion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/capacitaciones/buscar");
        SgCapacitacion[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgCapacitacion[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/capacitaciones/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCapacitacion guardar(SgCapacitacion capacitacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (capacitacion == null) {
            return null;
        }
        if (capacitacion.getCapPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/capacitaciones");
            return restClient.invokePost(webTarget, capacitacion, SgCapacitacion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/capacitaciones");
            webTarget = webTarget.path(capacitacion.getCapPk().toString());
            return restClient.invokePut(webTarget, capacitacion, SgCapacitacion.class);
        }
    }
    
    public SgCapacitacion validarRealizado(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/capacitaciones/validarrealizado");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokePost(webTarget, pk, SgCapacitacion.class);
    }
    
    public SgCapacitacion invalidarRealizado(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/capacitaciones/invalidarrealizado");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokePost(webTarget, pk, SgCapacitacion.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCapacitacion obtenerPorId(Long capacitacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (capacitacionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/capacitaciones");
        webTarget = webTarget.path(capacitacionPk.toString());
        return restClient.invokeGet(webTarget, SgCapacitacion.class);
    }

    public void eliminar(Long capacitacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (capacitacionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/capacitaciones");
        webTarget = webTarget.path(capacitacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long capacitacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (capacitacionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/capacitaciones/historial");
        webTarget = webTarget.path(capacitacionPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public Boolean validar(SgCapacitacion telefono) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (telefono == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/capacitaciones/validar");
        return restClient.invokePost(webTarget, telefono, Boolean.class);
    }

}
