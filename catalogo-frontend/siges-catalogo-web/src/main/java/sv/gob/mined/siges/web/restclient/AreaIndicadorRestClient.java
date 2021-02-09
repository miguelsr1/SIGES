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
import sv.gob.mined.siges.web.dto.SgAreaIndicador;
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
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 3000L)
public class AreaIndicadorRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public AreaIndicadorRestClient() {
    }


    public List<SgAreaIndicador> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasindicadores/buscar");
        SgAreaIndicador[] areasIndicadores = RestClient.invokePost(webTarget, filtro, SgAreaIndicador[].class, userToken);
        return Arrays.asList(areasIndicadores);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasindicadores/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgAreaIndicador guardar(SgAreaIndicador areaIndicador) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (areaIndicador == null || userToken == null) {
            return null;
        }
        if (areaIndicador.getAriPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasindicadores");
            return RestClient.invokePost(webTarget, areaIndicador, SgAreaIndicador.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasindicadores");
            webTarget = webTarget.path(areaIndicador.getAriPk().toString());
            return RestClient.invokePut(webTarget, areaIndicador, SgAreaIndicador.class, userToken);
        }
    }

    public SgAreaIndicador obtenerPorId(Long areaIndicadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (areaIndicadorPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasindicadores");
        webTarget = webTarget.path(areaIndicadorPk.toString());
        return RestClient.invokeGet(webTarget, SgAreaIndicador.class, userToken);
    }

    public void eliminar(Long areaIndicadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (areaIndicadorPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasindicadores");
        webTarget = webTarget.path(areaIndicadorPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgAreaIndicador> obtenerHistorialPorId(Long areaIndicadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (areaIndicadorPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasindicadores/historial");
        webTarget = webTarget.path(areaIndicadorPk.toString());
        SgAreaIndicador[] areasIndicadores = RestClient.invokeGet(webTarget, SgAreaIndicador[].class, userToken);
        return Arrays.asList(areasIndicadores);
    }
    

}
