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
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgNivelFormacionDocente;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 3000L)
public class NivelFormacionDocenteRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public NivelFormacionDocenteRestClient() {
    }


    public List<SgNivelFormacionDocente> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesformaciondocente/buscar");
        SgNivelFormacionDocente[] nivelFormacionDocente = RestClient.invokePost(webTarget, filtro, SgNivelFormacionDocente[].class, userToken);
        return Arrays.asList(nivelFormacionDocente);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesformaciondocente/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgNivelFormacionDocente guardar(SgNivelFormacionDocente nivelFormacionDocente) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelFormacionDocente == null || userToken == null) {
            return null;
        }
        if (nivelFormacionDocente.getNfdPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesformaciondocente");
            return RestClient.invokePost(webTarget, nivelFormacionDocente, SgNivelFormacionDocente.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesformaciondocente");
            webTarget = webTarget.path(nivelFormacionDocente.getNfdPk().toString());
            return RestClient.invokePut(webTarget, nivelFormacionDocente, SgNivelFormacionDocente.class, userToken);
        }
    }

    public SgNivelFormacionDocente obtenerPorId(Long nivelFormacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelFormacionDocentePk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesformaciondocente");
        webTarget = webTarget.path(nivelFormacionDocentePk.toString());
        return RestClient.invokeGet(webTarget, SgNivelFormacionDocente.class, userToken);
    }

    public void eliminar(Long nivelFormacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelFormacionDocentePk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesformaciondocente");
        webTarget = webTarget.path(nivelFormacionDocentePk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgNivelFormacionDocente> obtenerHistorialPorId(Long nivelFormacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (nivelFormacionDocentePk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesformaciondocente/historial");
        webTarget = webTarget.path(nivelFormacionDocentePk.toString());
        SgNivelFormacionDocente[] nivelFormacionDocente = RestClient.invokeGet(webTarget, SgNivelFormacionDocente[].class, userToken);
        return Arrays.asList(nivelFormacionDocente);
    }
    

}
