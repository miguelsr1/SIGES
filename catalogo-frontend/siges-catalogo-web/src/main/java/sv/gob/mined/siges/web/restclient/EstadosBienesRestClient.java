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
import sv.gob.mined.siges.web.dto.SgAfEstadosBienes;
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
public class EstadosBienesRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EstadosBienesRestClient() {
    }


    public List<SgAfEstadosBienes> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosbienes/buscar");
        SgAfEstadosBienes[] estados = RestClient.invokePost(webTarget, filtro, SgAfEstadosBienes[].class, userToken);
        return Arrays.asList(estados);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosbienes/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAfEstadosBienes guardar(SgAfEstadosBienes estado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estado == null || userToken == null) {
            return null;
        }
        if (estado.getEbiPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosbienes");
            return RestClient.invokePost(webTarget, estado, SgAfEstadosBienes.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosbienes");
            webTarget = webTarget.path(estado.getEbiPk().toString());
            return RestClient.invokePut(webTarget, estado, SgAfEstadosBienes.class, userToken);
        }
    }

    public SgAfEstadosBienes obtenerPorId(Long estadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosbienes");
        webTarget = webTarget.path(estadoPk.toString());
        return RestClient.invokeGet(webTarget, SgAfEstadosBienes.class, userToken);
    }

    public void eliminar(Long estadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosbienes");
        webTarget = webTarget.path(estadoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAfEstadosBienes> obtenerHistorialPorId(Long estadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosbienes/historial");
        webTarget = webTarget.path(estadoPk.toString());
        SgAfEstadosBienes[] estados = RestClient.invokeGet(webTarget, SgAfEstadosBienes[].class, userToken);
        return Arrays.asList(estados);
    }
    

}
