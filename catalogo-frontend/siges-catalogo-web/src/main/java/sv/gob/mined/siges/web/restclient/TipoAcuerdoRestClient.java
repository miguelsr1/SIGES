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
import sv.gob.mined.siges.web.dto.SgTipoAcuerdo;
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
public class TipoAcuerdoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoAcuerdoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoAcuerdo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposacuerdos/buscar");
        SgTipoAcuerdo[] tiposAcuerdos = RestClient.invokePost(webTarget, filtro, SgTipoAcuerdo[].class, userToken);
        return Arrays.asList(tiposAcuerdos);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposacuerdos/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoAcuerdo guardar(SgTipoAcuerdo tipoAcuerdo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAcuerdo == null || userToken == null) {
            return null;
        }
        if (tipoAcuerdo.getTaoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposacuerdos");
            return RestClient.invokePost(webTarget, tipoAcuerdo, SgTipoAcuerdo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposacuerdos");
            webTarget = webTarget.path(tipoAcuerdo.getTaoPk().toString());
            return RestClient.invokePut(webTarget, tipoAcuerdo, SgTipoAcuerdo.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgTipoAcuerdo obtenerPorId(Long tipoAcuerdoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAcuerdoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposacuerdos");
        webTarget = webTarget.path(tipoAcuerdoPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoAcuerdo.class, userToken);
    }

    public void eliminar(Long tipoAcuerdoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAcuerdoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposacuerdos");
        webTarget = webTarget.path(tipoAcuerdoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgTipoAcuerdo> obtenerHistorialPorId(Long tipoAcuerdoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoAcuerdoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposacuerdos/historial");
        webTarget = webTarget.path(tipoAcuerdoPk.toString());
        SgTipoAcuerdo[] tiposAcuerdos = RestClient.invokeGet(webTarget, SgTipoAcuerdo[].class, userToken);
        return Arrays.asList(tiposAcuerdos);
    }
    

}
