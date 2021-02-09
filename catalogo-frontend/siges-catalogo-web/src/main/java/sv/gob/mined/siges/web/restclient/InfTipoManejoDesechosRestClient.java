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
import sv.gob.mined.siges.web.dto.SgInfTipoManejoDesechos;
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
public class InfTipoManejoDesechosRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InfTipoManejoDesechosRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfTipoManejoDesechos> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomanejodesechos/buscar");
        SgInfTipoManejoDesechos[] tipoManejoDesechos = RestClient.invokePost(webTarget, filtro, SgInfTipoManejoDesechos[].class, userToken);
        return Arrays.asList(tipoManejoDesechos);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomanejodesechos/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInfTipoManejoDesechos guardar(SgInfTipoManejoDesechos infTipoManejoDesechos) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoManejoDesechos == null || userToken == null) {
            return null;
        }
        if (infTipoManejoDesechos.getTmdPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomanejodesechos");
            return RestClient.invokePost(webTarget, infTipoManejoDesechos, SgInfTipoManejoDesechos.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomanejodesechos");
            webTarget = webTarget.path(infTipoManejoDesechos.getTmdPk().toString());
            return RestClient.invokePut(webTarget, infTipoManejoDesechos, SgInfTipoManejoDesechos.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgInfTipoManejoDesechos obtenerPorId(Long infTipoManejoDesechosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoManejoDesechosPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomanejodesechos");
        webTarget = webTarget.path(infTipoManejoDesechosPk.toString());
        return RestClient.invokeGet(webTarget, SgInfTipoManejoDesechos.class, userToken);
    }

    public void eliminar(Long infTipoManejoDesechosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoManejoDesechosPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomanejodesechos");
        webTarget = webTarget.path(infTipoManejoDesechosPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgInfTipoManejoDesechos> obtenerHistorialPorId(Long infTipoManejoDesechosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoManejoDesechosPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomanejodesechos/historial");
        webTarget = webTarget.path(infTipoManejoDesechosPk.toString());
        SgInfTipoManejoDesechos[] tipoManejoDesechos = RestClient.invokeGet(webTarget, SgInfTipoManejoDesechos[].class, userToken);
        return Arrays.asList(tipoManejoDesechos);
    }
    

}
