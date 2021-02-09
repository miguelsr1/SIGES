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
import sv.gob.mined.siges.web.dto.SgTelefono;
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
public class TelefonoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TelefonoRestClient() {
    }


    public List<SgTelefono> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/telefonos/buscar");
        SgTelefono[] telefonos = RestClient.invokePost(webTarget, filtro, SgTelefono[].class, userToken);
        return Arrays.asList(telefonos);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/telefonos/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTelefono guardar(SgTelefono telefono) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (telefono == null || userToken == null) {
            return null;
        }
        if (telefono.getTelPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/telefonos");
            return RestClient.invokePost(webTarget, telefono, SgTelefono.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/telefonos");
            webTarget = webTarget.path(telefono.getTelPk().toString());
            return RestClient.invokePut(webTarget, telefono, SgTelefono.class, userToken);
        }
    }

    public SgTelefono obtenerPorId(Long telefonoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (telefonoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/telefonos");
        webTarget = webTarget.path(telefonoPk.toString());
        return RestClient.invokeGet(webTarget, SgTelefono.class, userToken);
    }

    public void eliminar(Long telefonoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (telefonoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/telefonos");
        webTarget = webTarget.path(telefonoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTelefono> obtenerHistorialPorId(Long telefonoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (telefonoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/telefonos/historial");
        webTarget = webTarget.path(telefonoPk.toString());
        SgTelefono[] telefonos = RestClient.invokeGet(webTarget, SgTelefono[].class, userToken);
        return Arrays.asList(telefonos);
    }
    

}
