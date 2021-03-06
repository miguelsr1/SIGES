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
import sv.gob.mined.siges.web.dto.SgTelefono;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTelefono;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class TelefonoRestClient implements Serializable {

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

    public TelefonoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgTelefono> buscar(FiltroTelefono filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/telefonos/buscar");
        SgTelefono[] lista = restClient.invokePost(webTarget, filtro, SgTelefono[].class);
        return Arrays.asList(lista);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroTelefono filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/telefonos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgTelefono obtenerPorId(Long telefonoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (telefonoId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/telefonos");
        webTarget = webTarget.path(telefonoId.toString());
        return restClient.invokeGet(webTarget, SgTelefono.class);
    }

    public SgTelefono guardar(SgTelefono telefono) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (telefono == null) {
            return null;
        }
        if (telefono.getTelPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/telefonos");
            return restClient.invokePost(webTarget, telefono, SgTelefono.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/telefonos");
            webTarget = webTarget.path(telefono.getTelPk().toString());
            return restClient.invokePut(webTarget, telefono, SgTelefono.class);
        }
    }

    public Boolean validar(SgTelefono telefono) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (telefono == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/telefonos/validar");
        return restClient.invokePost(webTarget, telefono, Boolean.class);
    }

    public void eliminar(Long telefonoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (telefonoId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/telefonos");
        webTarget = webTarget.path(telefonoId.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long telefonoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (telefonoId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/telefonos/historial");
        webTarget = webTarget.path(telefonoId.toString());
        RevHistorico[] lista = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(lista);
    }

}
