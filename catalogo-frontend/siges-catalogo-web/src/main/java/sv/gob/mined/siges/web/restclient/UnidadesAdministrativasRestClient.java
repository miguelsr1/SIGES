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
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;

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
public class UnidadesAdministrativasRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public UnidadesAdministrativasRestClient() {
    }


    public List<SgAfUnidadesAdministrativas> buscar(FiltroUnidadesAdministrativas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAD/buscar");
        SgAfUnidadesAdministrativas[] unidades = RestClient.invokePost(webTarget, filtro, SgAfUnidadesAdministrativas[].class, userToken);
        return Arrays.asList(unidades);
    }

    public Long buscarTotal(FiltroUnidadesAdministrativas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAD/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAfUnidadesAdministrativas guardar(SgAfUnidadesAdministrativas unidad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (unidad == null || userToken == null) {
            return null;
        }
        if (unidad.getUadPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAD");
            return RestClient.invokePost(webTarget, unidad, SgAfUnidadesAdministrativas.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAD");
            webTarget = webTarget.path(unidad.getUadPk().toString());
            return RestClient.invokePut(webTarget, unidad, SgAfUnidadesAdministrativas.class, userToken);
        }
    }

    public SgAfUnidadesAdministrativas obtenerPorId(Long unidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (unidadPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAD");
        webTarget = webTarget.path(unidadPk.toString());
        return RestClient.invokeGet(webTarget, SgAfUnidadesAdministrativas.class, userToken);
    }

    public void eliminar(Long unidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (unidadPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAD");
        webTarget = webTarget.path(unidadPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAfUnidadesAdministrativas> obtenerHistorialPorId(Long unidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (unidadPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAD/historial");
        webTarget = webTarget.path(unidadPk.toString());
        SgAfUnidadesAdministrativas[] estados = RestClient.invokeGet(webTarget, SgAfUnidadesAdministrativas[].class, userToken);
        return Arrays.asList(estados);
    }
    

}