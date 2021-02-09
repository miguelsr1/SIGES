/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient.centros_educativos;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSeccion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroSeccion;
import sv.gob.mined.siges.web.restclient.RestClient;

@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class SeccionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public SeccionRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgSeccion> buscar(FiltroSeccion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones/buscar");
        SgSeccion[] organizacionesCurricular = RestClient.invokePost(webTarget, filtro, SgSeccion[].class, userToken);
        return new ArrayList<>(Arrays.asList(organizacionesCurricular));
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroSeccion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgSeccion obtenerPorId(Long seccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (seccionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones");
        webTarget = webTarget.path(seccionPk.toString());
        return RestClient.invokeGet(webTarget, SgSeccion.class, userToken);
    }

    public SgSeccion guardar(SgSeccion seccion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (seccion == null) {
            return null;
        }
        if (seccion.getSecPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones");
            return RestClient.invokePost(webTarget, seccion, SgSeccion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones");
            webTarget = webTarget.path(seccion.getSecPk().toString());
            return RestClient.invokePut(webTarget, seccion, SgSeccion.class, userToken);
        }
    }

    public void eliminar(Long seccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (seccionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones");
        webTarget = webTarget.path(seccionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long seccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (seccionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones/historial");
        webTarget = webTarget.path(seccionPk.toString());
        RevHistorico[] organizacionesCurricular = RestClient.invokeGet(webTarget, RevHistorico[].class, userToken);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgSeccion obtenerEnRevision(Long seccionPk, Long seccionRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (seccionPk == null || seccionRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/secciones/revision");
        webTarget = webTarget.path(seccionPk.toString());
        webTarget = webTarget.path(seccionRev.toString());
        return RestClient.invokeGet(webTarget, SgSeccion.class, userToken);
    }

}
