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
import sv.gob.mined.siges.web.dto.SgModuloFormacionDocente;
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
public class ModuloFormacionDocenteRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ModuloFormacionDocenteRestClient() {
    }


    public List<SgModuloFormacionDocente> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modulosformaciondocente/buscar");
        SgModuloFormacionDocente[] moduloFormacionDocente = RestClient.invokePost(webTarget, filtro, SgModuloFormacionDocente[].class, userToken);
        return Arrays.asList(moduloFormacionDocente);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modulosformaciondocente/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgModuloFormacionDocente guardar(SgModuloFormacionDocente moduloFormacionDocente) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (moduloFormacionDocente == null || userToken == null) {
            return null;
        }
        if (moduloFormacionDocente.getMfdPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modulosformaciondocente");
            return RestClient.invokePost(webTarget, moduloFormacionDocente, SgModuloFormacionDocente.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modulosformaciondocente");
            webTarget = webTarget.path(moduloFormacionDocente.getMfdPk().toString());
            return RestClient.invokePut(webTarget, moduloFormacionDocente, SgModuloFormacionDocente.class, userToken);
        }
    }

    public SgModuloFormacionDocente obtenerPorId(Long moduloFormacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (moduloFormacionDocentePk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modulosformaciondocente");
        webTarget = webTarget.path(moduloFormacionDocentePk.toString());
        return RestClient.invokeGet(webTarget, SgModuloFormacionDocente.class, userToken);
    }

    public void eliminar(Long moduloFormacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (moduloFormacionDocentePk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modulosformaciondocente");
        webTarget = webTarget.path(moduloFormacionDocentePk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgModuloFormacionDocente> obtenerHistorialPorId(Long moduloFormacionDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (moduloFormacionDocentePk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modulosformaciondocente/historial");
        webTarget = webTarget.path(moduloFormacionDocentePk.toString());
        SgModuloFormacionDocente[] moduloFormacionDocente = RestClient.invokeGet(webTarget, SgModuloFormacionDocente[].class, userToken);
        return Arrays.asList(moduloFormacionDocente);
    }
    

}
