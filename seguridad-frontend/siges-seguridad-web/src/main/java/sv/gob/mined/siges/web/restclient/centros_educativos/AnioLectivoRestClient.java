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
import sv.gob.mined.siges.web.dto.centros_educativos.SgAnioLectivo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroAnioLectivo;
import sv.gob.mined.siges.web.restclient.RestClient;

@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class AnioLectivoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public AnioLectivoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgAnioLectivo> buscar(FiltroAnioLectivo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos/buscar");
        SgAnioLectivo[] organizacionesCurricular = RestClient.invokePost(webTarget, filtro, SgAnioLectivo[].class, userToken);
        return new ArrayList<>(Arrays.asList(organizacionesCurricular));
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroAnioLectivo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAnioLectivo guardar(SgAnioLectivo anioLectivo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioLectivo == null) {
            return null;
        }
        if (anioLectivo.getAlePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos");
            return RestClient.invokePost(webTarget, anioLectivo, SgAnioLectivo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos");
            webTarget = webTarget.path(anioLectivo.getAlePk().toString());
            return RestClient.invokePut(webTarget, anioLectivo, SgAnioLectivo.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgAnioLectivo obtenerPorId(Long anioLectivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioLectivoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos");
        webTarget = webTarget.path(anioLectivoPk.toString());
        return RestClient.invokeGet(webTarget, SgAnioLectivo.class, userToken);
    }

    public void eliminar(Long anioLectivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioLectivoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos");
        webTarget = webTarget.path(anioLectivoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long anioLectivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioLectivoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos/historial");
        webTarget = webTarget.path(anioLectivoPk.toString());
        RevHistorico[] organizacionesCurricular = RestClient.invokeGet(webTarget, RevHistorico[].class, userToken);
        return Arrays.asList(organizacionesCurricular);
    }

    public SgAnioLectivo obtenerEnRevision(Long anioLectivoPk, Long anioLectivoRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (anioLectivoPk == null || anioLectivoRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/anioslectivos/revision");
        webTarget = webTarget.path(anioLectivoPk.toString());
        webTarget = webTarget.path(anioLectivoRev.toString());
        return RestClient.invokeGet(webTarget, SgAnioLectivo.class, userToken);
    }

}
