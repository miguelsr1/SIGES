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
import sv.gob.mined.siges.web.dto.SgIdiomaRealizado;
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
public class IdiomaRealizadoRestClient implements Serializable {

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

    public IdiomaRealizadoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgIdiomaRealizado> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/idiomasrealizados/buscar");
        SgIdiomaRealizado[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgIdiomaRealizado[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/idiomasrealizados/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgIdiomaRealizado guardar(SgIdiomaRealizado idiomaRealizado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (idiomaRealizado == null) {
            return null;
        }
        if (idiomaRealizado.getIrePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/idiomasrealizados");
            return restClient.invokePost(webTarget, idiomaRealizado, SgIdiomaRealizado.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/idiomasrealizados");
            webTarget = webTarget.path(idiomaRealizado.getIrePk().toString());
            return restClient.invokePut(webTarget, idiomaRealizado, SgIdiomaRealizado.class);
        }
    }

    public SgIdiomaRealizado validarRealizado(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/idiomasrealizados/validarrealizado");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokePost(webTarget, pk, SgIdiomaRealizado.class);
    }
    
    public SgIdiomaRealizado invalidarRealizado(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/idiomasrealizados/invalidarrealizado");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokePost(webTarget, pk, SgIdiomaRealizado.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgIdiomaRealizado obtenerPorId(Long idiomaRealizadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (idiomaRealizadoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/idiomasrealizados");
        webTarget = webTarget.path(idiomaRealizadoPk.toString());
        return restClient.invokeGet(webTarget, SgIdiomaRealizado.class);
    }

    public void eliminar(Long idiomaRealizadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (idiomaRealizadoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/idiomasrealizados");
        webTarget = webTarget.path(idiomaRealizadoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long idiomaRealizadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (idiomaRealizadoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/idiomasrealizados/historial");
        webTarget = webTarget.path(idiomaRealizadoPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public Boolean validar(SgIdiomaRealizado idiomaRealizado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (idiomaRealizado == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/idiomasrealizados/validar");
        return restClient.invokePost(webTarget, idiomaRealizado, Boolean.class);
    }

}
