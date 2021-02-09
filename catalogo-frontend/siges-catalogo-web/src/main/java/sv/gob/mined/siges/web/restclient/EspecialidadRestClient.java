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
import sv.gob.mined.siges.web.dto.SgEspecialidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEspecialidad;

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
public class EspecialidadRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EspecialidadRestClient() {
    }


    public List<SgEspecialidad> buscar(FiltroEspecialidad filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/especialidades/buscar");
        SgEspecialidad[] especialidad = RestClient.invokePost(webTarget, filtro, SgEspecialidad[].class, userToken);
        return Arrays.asList(especialidad);
    }

    public Long buscarTotal(FiltroEspecialidad filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/especialidades/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEspecialidad guardar(SgEspecialidad especialidad) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (especialidad == null || userToken == null) {
            return null;
        }
        if (especialidad.getEspPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/especialidades");
            return RestClient.invokePost(webTarget, especialidad, SgEspecialidad.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/especialidades");
            webTarget = webTarget.path(especialidad.getEspPk().toString());
            return RestClient.invokePut(webTarget, especialidad, SgEspecialidad.class, userToken);
        }
    }

    public SgEspecialidad obtenerPorId(Long especialidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (especialidadPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/especialidades");
        webTarget = webTarget.path(especialidadPk.toString());
        return RestClient.invokeGet(webTarget, SgEspecialidad.class, userToken);
    }

    public void eliminar(Long especialidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (especialidadPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/especialidades");
        webTarget = webTarget.path(especialidadPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgEspecialidad> obtenerHistorialPorId(Long especialidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (especialidadPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/especialidades/historial");
        webTarget = webTarget.path(especialidadPk.toString());
        SgEspecialidad[] especialidad = RestClient.invokeGet(webTarget, SgEspecialidad[].class, userToken);
        return Arrays.asList(especialidad);
    }
    

}
