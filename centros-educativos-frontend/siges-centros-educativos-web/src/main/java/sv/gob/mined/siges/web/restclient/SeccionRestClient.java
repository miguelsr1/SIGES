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
import sv.gob.mined.siges.web.dto.SgCopiarSecciones;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 30000L)
public class SeccionRestClient implements Serializable {

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

    public SeccionRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgSeccion> buscar(FiltroSeccion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones/buscar");
        SgSeccion[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgSeccion[].class);
        return new ArrayList<>(Arrays.asList(organizacionesCurricular));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroSeccion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgSeccion obtenerPorId(Long seccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (seccionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones");
        webTarget = webTarget.path(seccionPk.toString());
        return restClient.invokeGet(webTarget, SgSeccion.class);
    }

    public SgSeccion guardar(SgSeccion seccion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (seccion == null) {
            return null;
        }
        if (seccion.getSecPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones");
            return restClient.invokePost(webTarget, seccion, SgSeccion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones");
            webTarget = webTarget.path(seccion.getSecPk().toString());
            return restClient.invokePut(webTarget, seccion, SgSeccion.class);
        }
    }

    public void eliminar(Long seccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (seccionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones");
        webTarget = webTarget.path(seccionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long seccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (seccionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones/historial");
        webTarget = webTarget.path(seccionPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgSeccion obtenerEnRevision(Long seccionPk, Long seccionRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (seccionPk == null || seccionRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones/revision");
        webTarget = webTarget.path(seccionPk.toString());
        webTarget = webTarget.path(seccionRev.toString());
        return restClient.invokeGet(webTarget, SgSeccion.class);
    }

    public Boolean copiarSecciones(SgCopiarSecciones dto) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (dto == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones/copiarsecciones");
        return restClient.invokePost(webTarget, dto, Boolean.class);
    }
    
    public void guardarSeccionAula(List<SgSeccion> secciones) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (secciones == null) {
            return ;
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones/seccionAula");
        restClient.invokePost(webTarget, secciones, null);
        
    }

}
