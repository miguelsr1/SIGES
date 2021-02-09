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
import sv.gob.mined.siges.web.dto.centros_educativos.SgRelOpcionProgEd;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroRelOpcionProgEd;
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
public class RelOpcionProgEdRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public RelOpcionProgEdRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRelOpcionProgEd> buscar(FiltroRelOpcionProgEd filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged/buscar");
        SgRelOpcionProgEd[] relOpcionProgEd = RestClient.invokePost(webTarget, filtro, SgRelOpcionProgEd[].class, userToken);
        return Arrays.asList(relOpcionProgEd);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroRelOpcionProgEd filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgRelOpcionProgEd guardar(SgRelOpcionProgEd relOpcionProgEd) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOpcionProgEd == null) {
            return null;
        }
        if (relOpcionProgEd.getRoePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged");
            return RestClient.invokePost(webTarget, relOpcionProgEd, SgRelOpcionProgEd.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged");
            webTarget = webTarget.path(relOpcionProgEd.getRoePk().toString());
            return RestClient.invokePut(webTarget, relOpcionProgEd, SgRelOpcionProgEd.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgRelOpcionProgEd obtenerPorId(Long relOpcionProgEdPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOpcionProgEdPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged");
        webTarget = webTarget.path(relOpcionProgEdPk.toString());
        return RestClient.invokeGet(webTarget, SgRelOpcionProgEd.class, userToken);
    }

    public void eliminar(Long relOpcionProgEdPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOpcionProgEdPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged");
        webTarget = webTarget.path(relOpcionProgEdPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long relOpcionProgEdPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOpcionProgEdPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged/historial");
        webTarget = webTarget.path(relOpcionProgEdPk.toString());
        RevHistorico[] relOpcionProgEd = RestClient.invokeGet(webTarget, RevHistorico[].class, userToken);
        return Arrays.asList(relOpcionProgEd);
    }

    public SgRelOpcionProgEd obtenerEnRevision(Long relOpcionProgEdPk, Long relOpcionProgEdRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relOpcionProgEdPk == null || relOpcionProgEdRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relopcionproged/revision");
        webTarget = webTarget.path(relOpcionProgEdPk.toString());
        webTarget = webTarget.path(relOpcionProgEdRev.toString());
        return RestClient.invokeGet(webTarget, SgRelOpcionProgEd.class, userToken);
    }

}
