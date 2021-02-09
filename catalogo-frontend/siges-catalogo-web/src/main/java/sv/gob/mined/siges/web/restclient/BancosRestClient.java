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
import sv.gob.mined.siges.web.dto.SgBancos;
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
public class BancosRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public BancosRestClient() {
    }


    public List<SgBancos> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos/buscar");
        SgBancos[] areasIndicadores = RestClient.invokePost(webTarget, filtro, SgBancos[].class, userToken);
        return Arrays.asList(areasIndicadores);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgBancos guardar(SgBancos banco) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (banco == null || userToken == null) {
            return null;
        }
        if (banco.getBncPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos");
            return RestClient.invokePost(webTarget, banco, SgBancos.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos");
            webTarget = webTarget.path(banco.getBncPk().toString());
            return RestClient.invokePut(webTarget, banco, SgBancos.class, userToken);
        }
    }

    public SgBancos obtenerPorId(Long bancoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bancoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos");
        webTarget = webTarget.path(bancoPk.toString());
        return RestClient.invokeGet(webTarget, SgBancos.class, userToken);
    }

    public void eliminar(Long bancoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bancoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos");
        webTarget = webTarget.path(bancoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgBancos> obtenerHistorialPorId(Long bancoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (bancoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/bancos/historial");
        webTarget = webTarget.path(bancoPk.toString());
        SgBancos[] areasIndicadores = RestClient.invokeGet(webTarget, SgBancos[].class, userToken);
        return Arrays.asList(areasIndicadores);
    }
    

}
