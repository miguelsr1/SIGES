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
import sv.gob.mined.siges.web.dto.SgCargo;
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
public class CargoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CargoRestClient() {
    }


    public List<SgCargo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargo/buscar");
        SgCargo[] cargo = RestClient.invokePost(webTarget, filtro, SgCargo[].class, userToken);
        return Arrays.asList(cargo);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargo/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCargo guardar(SgCargo cargo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargo == null || userToken == null) {
            return null;
        }
        if (cargo.getCarPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargo");
            return RestClient.invokePost(webTarget, cargo, SgCargo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargo");
            webTarget = webTarget.path(cargo.getCarPk().toString());
            return RestClient.invokePut(webTarget, cargo, SgCargo.class, userToken);
        }
    }

    public SgCargo obtenerPorId(Long cargoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargo");
        webTarget = webTarget.path(cargoPk.toString());
        return RestClient.invokeGet(webTarget, SgCargo.class, userToken);
    }

    public void eliminar(Long cargoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargo");
        webTarget = webTarget.path(cargoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgCargo> obtenerHistorialPorId(Long cargoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cargoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargo/historial");
        webTarget = webTarget.path(cargoPk.toString());
        SgCargo[] cargo = RestClient.invokeGet(webTarget, SgCargo[].class, userToken);
        return Arrays.asList(cargo);
    }
    

}
