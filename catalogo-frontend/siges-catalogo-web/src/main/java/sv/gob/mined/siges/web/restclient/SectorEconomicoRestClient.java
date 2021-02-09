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
import sv.gob.mined.siges.web.dto.SgSectorEconomico;
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
public class SectorEconomicoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public SectorEconomicoRestClient() {
    }


    public List<SgSectorEconomico> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sectoreseconomico/buscar");
        SgSectorEconomico[] sectoresEconomicos = RestClient.invokePost(webTarget, filtro, SgSectorEconomico[].class, userToken);
        return Arrays.asList(sectoresEconomicos);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sectoreseconomico/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgSectorEconomico guardar(SgSectorEconomico sectorEconomico) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sectorEconomico == null || userToken == null) {
            return null;
        }
        if (sectorEconomico.getSecPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sectoreseconomico");
            return RestClient.invokePost(webTarget, sectorEconomico, SgSectorEconomico.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sectoreseconomico");
            webTarget = webTarget.path(sectorEconomico.getSecPk().toString());
            return RestClient.invokePut(webTarget, sectorEconomico, SgSectorEconomico.class, userToken);
        }
    }

    public SgSectorEconomico obtenerPorId(Long sectorEconomicoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sectorEconomicoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sectoreseconomico");
        webTarget = webTarget.path(sectorEconomicoPk.toString());
        return RestClient.invokeGet(webTarget, SgSectorEconomico.class, userToken);
    }

    public void eliminar(Long sectorEconomicoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sectorEconomicoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sectoreseconomico");
        webTarget = webTarget.path(sectorEconomicoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgSectorEconomico> obtenerHistorialPorId(Long sectorEconomicoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sectorEconomicoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sectoreseconomico/historial");
        webTarget = webTarget.path(sectorEconomicoPk.toString());
        SgSectorEconomico[] sectoresEconomicos = RestClient.invokeGet(webTarget, SgSectorEconomico[].class, userToken);
        return Arrays.asList(sectoresEconomicos);
    }
    

}
