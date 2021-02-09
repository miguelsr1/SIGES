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
import sv.gob.mined.siges.web.dto.SgAfFormaAdquisicion;
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
public class FormaAdquisicionRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public FormaAdquisicionRestClient() {
    }


    public List<SgAfFormaAdquisicion> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formaadquisicion/buscar");
        SgAfFormaAdquisicion[] formasadquisicion = RestClient.invokePost(webTarget, filtro, SgAfFormaAdquisicion[].class, userToken);
        return Arrays.asList(formasadquisicion);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formaadquisicion/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAfFormaAdquisicion guardar(SgAfFormaAdquisicion formAdq) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formAdq == null || userToken == null) {
            return null;
        }
        if (formAdq.getFadPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formaadquisicion");
            return RestClient.invokePost(webTarget, formAdq, SgAfFormaAdquisicion.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formaadquisicion");
            webTarget = webTarget.path(formAdq.getFadPk().toString());
            return RestClient.invokePut(webTarget, formAdq, SgAfFormaAdquisicion.class, userToken);
        }
    }

    public SgAfFormaAdquisicion obtenerPorId(Long formaAdPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formaAdPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formaadquisicion");
        webTarget = webTarget.path(formaAdPk.toString());
        return RestClient.invokeGet(webTarget, SgAfFormaAdquisicion.class, userToken);
    }

    public void eliminar(Long formaAdPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formaAdPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formaadquisicion");
        webTarget = webTarget.path(formaAdPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAfFormaAdquisicion> obtenerHistorialPorId(Long formaAdPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formaAdPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formaadquisicion/historial");
        webTarget = webTarget.path(formaAdPk.toString());
        SgAfFormaAdquisicion[] formasadquisicion = RestClient.invokeGet(webTarget, SgAfFormaAdquisicion[].class, userToken);
        return Arrays.asList(formasadquisicion);
    }
    

}
