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
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgDocenteDocumento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDocenteDocumento;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class DocenteDocumentoRestClient implements Serializable {

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

    public DocenteDocumentoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgDocenteDocumento> buscar(FiltroDocenteDocumento filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/docentedocumentos/buscar");
        SgDocenteDocumento[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgDocenteDocumento[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroDocenteDocumento filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/docentedocumentos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgDocenteDocumento guardar(SgDocenteDocumento documento) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (documento == null) {
            return null;
        }
        if (documento.getDdoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/docentedocumentos");
            return restClient.invokePost(webTarget, documento, SgDocenteDocumento.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/docentedocumentos");
            webTarget = webTarget.path(documento.getDdoPk().toString());
            return restClient.invokePut(webTarget, documento, SgDocenteDocumento.class);
        }
    }
    
    public SgDocenteDocumento validarRealizado(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/docentedocumentos/validarrealizado");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokePost(webTarget, pk, SgDocenteDocumento.class);
    }
    
    public SgDocenteDocumento invalidarRealizado(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/docentedocumentos/invalidarrealizado");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokePost(webTarget, pk, SgDocenteDocumento.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgDocenteDocumento obtenerPorId(Long documentoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (documentoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/docentedocumentos");
        webTarget = webTarget.path(documentoPk.toString());
        return restClient.invokeGet(webTarget, SgDocenteDocumento.class);
    }

    @Timeout(value = 20000L)
    public void eliminar(Long documentoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (documentoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/docentedocumentos");
        webTarget = webTarget.path(documentoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long documentoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (documentoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/docentedocumentos/historial");
        webTarget = webTarget.path(documentoPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }
    
    public Boolean validar(SgDocenteDocumento documento) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (documento == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/docentedocumentos/validar");
        return restClient.invokePost(webTarget, documento, Boolean.class);
    }
}
