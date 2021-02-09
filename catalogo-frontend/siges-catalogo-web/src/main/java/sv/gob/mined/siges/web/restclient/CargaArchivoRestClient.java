/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgCargaArchivo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 3000L)
public class CargaArchivoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CargaArchivoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCargaArchivo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargasarchivos/buscar");
        SgCargaArchivo[] cargasArchivos = RestClient.invokePost(webTarget, filtro, SgCargaArchivo[].class, userToken);
        return Arrays.asList(cargasArchivos);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargasarchivos/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCargaArchivo guardar(SgCargaArchivo cargaArchivo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargaArchivo == null || userToken == null) {
            return null;
        }
        if (cargaArchivo.getCgaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargasarchivos");
            return RestClient.invokePost(webTarget, cargaArchivo, SgCargaArchivo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargasarchivos");
            webTarget = webTarget.path(cargaArchivo.getCgaPk().toString());
            return RestClient.invokePut(webTarget, cargaArchivo, SgCargaArchivo.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCargaArchivo obtenerPorId(Long cargaArchivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargaArchivoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargasarchivos");
        webTarget = webTarget.path(cargaArchivoPk.toString());
        return RestClient.invokeGet(webTarget, SgCargaArchivo.class, userToken);
    }

    public void eliminar(Long cargaArchivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargaArchivoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargasarchivos");
        webTarget = webTarget.path(cargaArchivoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCargaArchivo> obtenerHistorialPorId(Long cargaArchivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargaArchivoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargasarchivos/historial");
        webTarget = webTarget.path(cargaArchivoPk.toString());
        SgCargaArchivo[] cargasArchivos = RestClient.invokeGet(webTarget, SgCargaArchivo[].class, userToken);
        return Arrays.asList(cargasArchivos);
    }
    

}
