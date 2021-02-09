/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

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
import sv.gob.mined.siges.web.dto.SgSiTipoDocumento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class SiTipoDocumentoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public SiTipoDocumentoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgSiTipoDocumento> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sitipodocumento/buscar");
        SgSiTipoDocumento[] tipoImagen = RestClient.invokePost(webTarget, filtro, SgSiTipoDocumento[].class, userToken);
        return Arrays.asList(tipoImagen);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sitipodocumento/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgSiTipoDocumento guardar(SgSiTipoDocumento infTipoDocumento) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoDocumento == null || userToken == null) {
            return null;
        }
        if (infTipoDocumento.getTidPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sitipodocumento");
            return RestClient.invokePost(webTarget, infTipoDocumento, SgSiTipoDocumento.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sitipodocumento");
            webTarget = webTarget.path(infTipoDocumento.getTidPk().toString());
            return RestClient.invokePut(webTarget, infTipoDocumento, SgSiTipoDocumento.class, userToken);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgSiTipoDocumento obtenerPorId(Long infTipoDocumentoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoDocumentoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sitipodocumento");
        webTarget = webTarget.path(infTipoDocumentoPk.toString());
        return RestClient.invokeGet(webTarget, SgSiTipoDocumento.class, userToken);
    }

    public void eliminar(Long infTipoDocumentoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoDocumentoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sitipodocumento");
        webTarget = webTarget.path(infTipoDocumentoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgSiTipoDocumento> obtenerHistorialPorId(Long infTipoDocumentoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoDocumentoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sitipodocumento/historial");
        webTarget = webTarget.path(infTipoDocumentoPk.toString());
        SgSiTipoDocumento[] tipoImagen = RestClient.invokeGet(webTarget, SgSiTipoDocumento[].class, userToken);
        return Arrays.asList(tipoImagen);
    }

}
