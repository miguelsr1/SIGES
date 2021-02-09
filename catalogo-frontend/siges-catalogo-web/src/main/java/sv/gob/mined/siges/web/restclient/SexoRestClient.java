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
import sv.gob.mined.siges.web.dto.SgSexo;
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
public class SexoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public SexoRestClient() {
    }


    public List<SgSexo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sexos/buscar");
        SgSexo[] sexos = RestClient.invokePost(webTarget, filtro, SgSexo[].class, userToken);
        return Arrays.asList(sexos);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sexos/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgSexo guardar(SgSexo sexo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sexo == null || userToken == null) {
            return null;
        }
        if (sexo.getSexPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sexos");
            return RestClient.invokePost(webTarget, sexo, SgSexo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sexos");
            webTarget = webTarget.path(sexo.getSexPk().toString());
            return RestClient.invokePut(webTarget, sexo, SgSexo.class, userToken);
        }
    }

    public SgSexo obtenerPorId(Long sexoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sexoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sexos");
        webTarget = webTarget.path(sexoPk.toString());
        return RestClient.invokeGet(webTarget, SgSexo.class, userToken);
    }

    public void eliminar(Long sexoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sexoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sexos");
        webTarget = webTarget.path(sexoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgSexo> obtenerHistorialPorId(Long sexoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sexoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sexos/historial");
        webTarget = webTarget.path(sexoPk.toString());
        SgSexo[] sexos = RestClient.invokeGet(webTarget, SgSexo[].class, userToken);
        return Arrays.asList(sexos);
    }
    

}
