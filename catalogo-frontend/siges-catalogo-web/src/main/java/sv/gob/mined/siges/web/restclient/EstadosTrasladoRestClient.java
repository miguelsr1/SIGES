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
import sv.gob.mined.siges.web.dto.SgAfEstadosTraslado;
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
public class EstadosTrasladoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EstadosTrasladoRestClient() {
    }


    public List<SgAfEstadosTraslado> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadostraslado/buscar");
        SgAfEstadosTraslado[] estadosCalidad = RestClient.invokePost(webTarget, filtro, SgAfEstadosTraslado[].class, userToken);
        return Arrays.asList(estadosCalidad);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadostraslado/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAfEstadosTraslado guardar(SgAfEstadosTraslado estado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estado == null || userToken == null) {
            return null;
        }
        if (estado.getEtrPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadostraslado");
            return RestClient.invokePost(webTarget, estado, SgAfEstadosTraslado.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadostraslado");
            webTarget = webTarget.path(estado.getEtrPk().toString());
            return RestClient.invokePut(webTarget, estado, SgAfEstadosTraslado.class, userToken);
        }
    }

    public SgAfEstadosTraslado obtenerPorId(Long estadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadostraslado");
        webTarget = webTarget.path(estadoPk.toString());
        return RestClient.invokeGet(webTarget, SgAfEstadosTraslado.class, userToken);
    }

    public void eliminar(Long estadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadostraslado");
        webTarget = webTarget.path(estadoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAfEstadosTraslado> obtenerHistorialPorId(Long estadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estadoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadostraslado/historial");
        webTarget = webTarget.path(estadoPk.toString());
        SgAfEstadosTraslado[] estados = RestClient.invokeGet(webTarget, SgAfEstadosTraslado[].class, userToken);
        return Arrays.asList(estados);
    }
    

}

