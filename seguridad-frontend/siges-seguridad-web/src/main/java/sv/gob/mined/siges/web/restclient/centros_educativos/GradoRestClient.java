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
import sv.gob.mined.siges.web.dto.centros_educativos.SgGrado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroGrado;
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
public class GradoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public GradoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgGrado> buscar(FiltroGrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/buscar");
        SgGrado[] grados = RestClient.invokePost(webTarget, filtro, SgGrado[].class, userToken);
        return Arrays.asList(grados);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroGrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgGrado guardar(SgGrado grado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (grado == null) {
            return null;
        }
        if (grado.getGraPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados");
            return RestClient.invokePost(webTarget, grado, SgGrado.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados");
            webTarget = webTarget.path(grado.getGraPk().toString());
            return RestClient.invokePut(webTarget, grado, SgGrado.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgGrado obtenerPorId(Long gradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados");
        webTarget = webTarget.path(gradoPk.toString());
        return RestClient.invokeGet(webTarget, SgGrado.class, userToken);
    }

    public void eliminar(Long gradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados");
        webTarget = webTarget.path(gradoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long gradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/historial");
        webTarget = webTarget.path(gradoPk.toString());
        RevHistorico[] grados = RestClient.invokeGet(webTarget, RevHistorico[].class, userToken);
        return Arrays.asList(grados);
    }

    public SgGrado obtenerEnRevision(Long gradoPk, Long gradoRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gradoPk == null || gradoRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/revision");
        webTarget = webTarget.path(gradoPk.toString());
        webTarget = webTarget.path(gradoRev.toString());
        return RestClient.invokeGet(webTarget, SgGrado.class, userToken);
    }

    public Boolean validar(SgGrado grado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (grado == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/grados/validar");
        return RestClient.invokePost(webTarget, grado, Boolean.class, userToken);
    }

}
