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
import sv.gob.mined.siges.web.dto.SgTipoDocumentoDocente;
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
public class TipoDocumentoDocenteRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoDocumentoDocenteRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoDocumentoDocente> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposdocumentosdocentes/buscar");
        SgTipoDocumentoDocente[] tiposDocumentosDocentes = RestClient.invokePost(webTarget, filtro, SgTipoDocumentoDocente[].class, userToken);
        return Arrays.asList(tiposDocumentosDocentes);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposdocumentosdocentes/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoDocumentoDocente guardar(SgTipoDocumentoDocente tipoDocumentoDocente) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoDocumentoDocente == null || userToken == null) {
            return null;
        }
        if (tipoDocumentoDocente.getTddPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposdocumentosdocentes");
            return RestClient.invokePost(webTarget, tipoDocumentoDocente, SgTipoDocumentoDocente.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposdocumentosdocentes");
            webTarget = webTarget.path(tipoDocumentoDocente.getTddPk().toString());
            return RestClient.invokePut(webTarget, tipoDocumentoDocente, SgTipoDocumentoDocente.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTipoDocumentoDocente obtenerPorId(Long tipoDocumentoDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoDocumentoDocentePk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposdocumentosdocentes");
        webTarget = webTarget.path(tipoDocumentoDocentePk.toString());
        return RestClient.invokeGet(webTarget, SgTipoDocumentoDocente.class, userToken);
    }

    public void eliminar(Long tipoDocumentoDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoDocumentoDocentePk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposdocumentosdocentes");
        webTarget = webTarget.path(tipoDocumentoDocentePk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoDocumentoDocente> obtenerHistorialPorId(Long tipoDocumentoDocentePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoDocumentoDocentePk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposdocumentosdocentes/historial");
        webTarget = webTarget.path(tipoDocumentoDocentePk.toString());
        SgTipoDocumentoDocente[] tiposDocumentosDocentes = RestClient.invokeGet(webTarget, SgTipoDocumentoDocente[].class, userToken);
        return Arrays.asList(tiposDocumentosDocentes);
    }
    

}
