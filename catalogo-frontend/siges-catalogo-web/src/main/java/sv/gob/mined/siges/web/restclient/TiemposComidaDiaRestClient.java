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
import sv.gob.mined.siges.web.dto.SgTiemposComidaDia;
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
public class TiemposComidaDiaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TiemposComidaDiaRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTiemposComidaDia> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiempocomidadia/buscar");
        SgTiemposComidaDia[] tiempoComidaDia = RestClient.invokePost(webTarget, filtro, SgTiemposComidaDia[].class, userToken);
        return Arrays.asList(tiempoComidaDia);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiempocomidadia/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTiemposComidaDia guardar(SgTiemposComidaDia tiemposComidaDia) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tiemposComidaDia == null || userToken == null) {
            return null;
        }
        if (tiemposComidaDia.getTcdPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiempocomidadia");
            return RestClient.invokePost(webTarget, tiemposComidaDia, SgTiemposComidaDia.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiempocomidadia");
            webTarget = webTarget.path(tiemposComidaDia.getTcdPk().toString());
            return RestClient.invokePut(webTarget, tiemposComidaDia, SgTiemposComidaDia.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTiemposComidaDia obtenerPorId(Long tiemposComidaDiaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tiemposComidaDiaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiempocomidadia");
        webTarget = webTarget.path(tiemposComidaDiaPk.toString());
        return RestClient.invokeGet(webTarget, SgTiemposComidaDia.class, userToken);
    }

    public void eliminar(Long tiemposComidaDiaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tiemposComidaDiaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiempocomidadia");
        webTarget = webTarget.path(tiemposComidaDiaPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTiemposComidaDia> obtenerHistorialPorId(Long tiemposComidaDiaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tiemposComidaDiaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiempocomidadia/historial");
        webTarget = webTarget.path(tiemposComidaDiaPk.toString());
        SgTiemposComidaDia[] tiempoComidaDia = RestClient.invokeGet(webTarget, SgTiemposComidaDia[].class, userToken);
        return Arrays.asList(tiempoComidaDia);
    }
    

}
