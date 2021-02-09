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
import sv.gob.mined.siges.web.dto.SgTipoOrganismoAdministrativo;
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
public class TipoOrganismoAdministrativoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public TipoOrganismoAdministrativoRestClient() {
    }


    public List<SgTipoOrganismoAdministrativo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposorganismoadministrativo/buscar");
        SgTipoOrganismoAdministrativo[] tiposOrganismoAdministrativo = RestClient.invokePost(webTarget, filtro, SgTipoOrganismoAdministrativo[].class, userToken);
        return Arrays.asList(tiposOrganismoAdministrativo);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposorganismoadministrativo/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgTipoOrganismoAdministrativo guardar(SgTipoOrganismoAdministrativo tipoOrganismoAdministrativo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoOrganismoAdministrativo == null || userToken == null) {
            return null;
        }
        if (tipoOrganismoAdministrativo.getToaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposorganismoadministrativo");
            return RestClient.invokePost(webTarget, tipoOrganismoAdministrativo, SgTipoOrganismoAdministrativo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposorganismoadministrativo");
            webTarget = webTarget.path(tipoOrganismoAdministrativo.getToaPk().toString());
            return RestClient.invokePut(webTarget, tipoOrganismoAdministrativo, SgTipoOrganismoAdministrativo.class, userToken);
        }
    }

    public SgTipoOrganismoAdministrativo obtenerPorId(Long tipoOrganismoAdministrativoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoOrganismoAdministrativoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposorganismoadministrativo");
        webTarget = webTarget.path(tipoOrganismoAdministrativoPk.toString());
        return RestClient.invokeGet(webTarget, SgTipoOrganismoAdministrativo.class, userToken);
    }

    public void eliminar(Long tipoOrganismoAdministrativoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoOrganismoAdministrativoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposorganismoadministrativo");
        webTarget = webTarget.path(tipoOrganismoAdministrativoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgTipoOrganismoAdministrativo> obtenerHistorialPorId(Long tipoOrganismoAdministrativoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoOrganismoAdministrativoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposorganismoadministrativo/historial");
        webTarget = webTarget.path(tipoOrganismoAdministrativoPk.toString());
        SgTipoOrganismoAdministrativo[] tiposOrganismoAdministrativo = RestClient.invokeGet(webTarget, SgTipoOrganismoAdministrativo[].class, userToken);
        return Arrays.asList(tiposOrganismoAdministrativo);
    }
    

}
