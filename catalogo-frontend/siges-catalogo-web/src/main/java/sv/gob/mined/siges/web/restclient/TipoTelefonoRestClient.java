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
import sv.gob.mined.siges.web.dto.SgTipoTelefono;
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
public class TipoTelefonoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoTelefonoRestClient() {
    }


    public List<SgTipoTelefono> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostelefono/buscar");
        SgTipoTelefono[] tiposTelefono = RestClient.invokePost(webTarget, filtro, SgTipoTelefono[].class, userToken);
        return Arrays.asList(tiposTelefono);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostelefono/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoTelefono guardar(SgTipoTelefono tipoTelefono) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoTelefono == null || userToken == null) {
            return null;
        }
        if (tipoTelefono.getTtoPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostelefono");
            return RestClient.invokePost(webTarget, tipoTelefono, SgTipoTelefono.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostelefono");
            webTarget = webTarget.path(tipoTelefono.getTtoPk().toString());
            return RestClient.invokePut(webTarget, tipoTelefono, SgTipoTelefono.class, userToken);
        }
    }

    public SgTipoTelefono obtenerPorId(Long tipoTelefonoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoTelefonoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostelefono");
        webTarget = webTarget.path(tipoTelefonoPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoTelefono.class, userToken);
    }

    public void eliminar(Long tipoTelefonoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoTelefonoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostelefono");
        webTarget = webTarget.path(tipoTelefonoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoTelefono> obtenerHistorialPorId(Long tipoTelefonoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoTelefonoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostelefono/historial");
        webTarget = webTarget.path(tipoTelefonoPk.toString());
        SgTipoTelefono[] tiposTelefono = RestClient.invokeGet(webTarget, SgTipoTelefono[].class, userToken);
        return Arrays.asList(tiposTelefono);
    }
    

}
