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
import sv.gob.mined.siges.web.dto.SgEstudioSuperior;
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
public class EstudioSuperiorRestClient implements Serializable {

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

    public EstudioSuperiorRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgEstudioSuperior> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiossuperiores/buscar");
        SgEstudioSuperior[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgEstudioSuperior[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiossuperiores/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgEstudioSuperior guardar(SgEstudioSuperior capacitacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (capacitacion == null) {
            return null;
        }
        if (capacitacion.getEsuPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiossuperiores");
            return restClient.invokePost(webTarget, capacitacion, SgEstudioSuperior.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiossuperiores");
            webTarget = webTarget.path(capacitacion.getEsuPk().toString());
            return restClient.invokePut(webTarget, capacitacion, SgEstudioSuperior.class);
        }
    }
    
    public SgEstudioSuperior validarRealizado(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiossuperiores/validarrealizado");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokePost(webTarget, pk, SgEstudioSuperior.class);
    }
    
    public SgEstudioSuperior invalidarRealizado(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiossuperiores/invalidarrealizado");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokePost(webTarget, pk, SgEstudioSuperior.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgEstudioSuperior obtenerPorId(Long capacitacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (capacitacionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiossuperiores");
        webTarget = webTarget.path(capacitacionPk.toString());
        return restClient.invokeGet(webTarget, SgEstudioSuperior.class);
    }

    public void eliminar(Long capacitacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (capacitacionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiossuperiores");
        webTarget = webTarget.path(capacitacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long capacitacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (capacitacionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiossuperiores/historial");
        webTarget = webTarget.path(capacitacionPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public Boolean validar(SgEstudioSuperior telefono) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (telefono == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/estudiossuperiores/validar");
        return restClient.invokePost(webTarget, telefono, Boolean.class);
    }

}
