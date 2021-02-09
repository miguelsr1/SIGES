/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.rest;

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
import sv.gob.mined.siges.web.dto.SgServicioInfraestructura;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.restclient.RestClient;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

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
public class ServicioInfraestructuraRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ServicioInfraestructuraRestClient() {
    }


    public List<SgServicioInfraestructura> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/servicioinfraestructura/buscar");
        SgServicioInfraestructura[] servicioInfraestructura = RestClient.invokePost(webTarget, filtro, SgServicioInfraestructura[].class, userToken);
        return Arrays.asList(servicioInfraestructura);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/servicioinfraestructura/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgServicioInfraestructura guardar(SgServicioInfraestructura servicioInfraestructura) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioInfraestructura == null || userToken == null) {
            return null;
        }
        if (servicioInfraestructura.getSinPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/servicioinfraestructura");
            return RestClient.invokePost(webTarget, servicioInfraestructura, SgServicioInfraestructura.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/servicioinfraestructura");
            webTarget = webTarget.path(servicioInfraestructura.getSinPk().toString());
            return RestClient.invokePut(webTarget, servicioInfraestructura, SgServicioInfraestructura.class, userToken);
        }
    }

    public SgServicioInfraestructura obtenerPorId(Long servicioInfraestructuraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioInfraestructuraPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/servicioinfraestructura");
        webTarget = webTarget.path(servicioInfraestructuraPk.toString());
        return RestClient.invokeGet(webTarget, SgServicioInfraestructura.class, userToken);
    }

    public void eliminar(Long servicioInfraestructuraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioInfraestructuraPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/servicioinfraestructura");
        webTarget = webTarget.path(servicioInfraestructuraPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgServicioInfraestructura> obtenerHistorialPorId(Long servicioInfraestructuraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (servicioInfraestructuraPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/servicioinfraestructura/historial");
        webTarget = webTarget.path(servicioInfraestructuraPk.toString());
        SgServicioInfraestructura[] servicioInfraestructura = RestClient.invokeGet(webTarget, SgServicioInfraestructura[].class, userToken);
        return Arrays.asList(servicioInfraestructura);
    }
    

}
