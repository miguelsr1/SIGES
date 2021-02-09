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
import sv.gob.mined.siges.web.dto.centros_educativos.SgOpcion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroOpciones;
import sv.gob.mined.siges.web.restclient.RestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class OpcionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public OpcionRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgOpcion> buscar(FiltroOpciones filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones/buscar");
        SgOpcion[] opciones = RestClient.invokePost(webTarget, filtro, SgOpcion[].class, userToken);
        return Arrays.asList(opciones);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroOpciones filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgOpcion guardar(SgOpcion opcion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opcion == null) {
            return null;
        }
        if (opcion.getOpcPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones");
            return RestClient.invokePost(webTarget, opcion, SgOpcion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones");
            webTarget = webTarget.path(opcion.getOpcPk().toString());
            return RestClient.invokePut(webTarget, opcion, SgOpcion.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgOpcion obtenerPorId(Long opcionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opcionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones");
        webTarget = webTarget.path(opcionPk.toString());
        return RestClient.invokeGet(webTarget, SgOpcion.class, userToken);
    }

    public void eliminar(Long opcionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opcionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones");
        webTarget = webTarget.path(opcionPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long opcionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opcionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones/historial");
        webTarget = webTarget.path(opcionPk.toString());
        RevHistorico[] opciones = RestClient.invokeGet(webTarget, RevHistorico[].class, userToken);
        return Arrays.asList(opciones);
    }

    public SgOpcion obtenerEnRevision(Long opcionPk, Long opcionRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (opcionPk == null || opcionRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/opciones/revision");
        webTarget = webTarget.path(opcionPk.toString());
        webTarget = webTarget.path(opcionRev.toString());
        return RestClient.invokeGet(webTarget, SgOpcion.class, userToken);
    }

}
