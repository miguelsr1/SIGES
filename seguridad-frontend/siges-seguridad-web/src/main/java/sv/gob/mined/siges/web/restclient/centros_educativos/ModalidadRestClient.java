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
import sv.gob.mined.siges.web.dto.centros_educativos.SgModalidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroModalidad;
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
public class ModalidadRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public ModalidadRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgModalidad> buscar(FiltroModalidad filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modalidades/buscar");
        SgModalidad[] modalidades = RestClient.invokePost(webTarget, filtro, SgModalidad[].class, userToken);
        return Arrays.asList(modalidades);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroModalidad filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modalidades/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgModalidad guardar(SgModalidad modalidad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidad == null) {
            return null;
        }
        if (modalidad.getModPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modalidades");
            return RestClient.invokePost(webTarget, modalidad, SgModalidad.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modalidades");
            webTarget = webTarget.path(modalidad.getModPk().toString());
            return RestClient.invokePut(webTarget, modalidad, SgModalidad.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgModalidad obtenerPorId(Long modalidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modalidades");
        webTarget = webTarget.path(modalidadPk.toString());
        return RestClient.invokeGet(webTarget, SgModalidad.class, userToken);
    }

    public SgModalidad obtenerPorIdLazy(Long modalidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modalidades/lazy/");
        webTarget = webTarget.path(modalidadPk.toString());
        return RestClient.invokeGet(webTarget, SgModalidad.class, userToken);
    }

    public void eliminar(Long modalidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modalidades");
        webTarget = webTarget.path(modalidadPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long modalidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modalidades/historial");
        webTarget = webTarget.path(modalidadPk.toString());
        RevHistorico[] modalidades = RestClient.invokeGet(webTarget, RevHistorico[].class, userToken);
        return Arrays.asList(modalidades);
    }

    public SgModalidad obtenerEnRevision(Long modalidadPk, Long modalidadRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadPk == null || modalidadRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modalidades/revision");
        webTarget = webTarget.path(modalidadPk.toString());
        webTarget = webTarget.path(modalidadRev.toString());
        return RestClient.invokeGet(webTarget, SgModalidad.class, userToken);
    }

}
