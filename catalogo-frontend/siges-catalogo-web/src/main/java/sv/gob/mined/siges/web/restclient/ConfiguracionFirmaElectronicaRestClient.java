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
import sv.gob.mined.siges.web.dto.SgConfiguracionFirmaElectronica;
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
@Timeout(value = 6000L)
public class ConfiguracionFirmaElectronicaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ConfiguracionFirmaElectronicaRestClient() {
    }


    public List<SgConfiguracionFirmaElectronica> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuracionesfirmaelectronica/buscar");
        SgConfiguracionFirmaElectronica[] configuraciones = RestClient.invokePost(webTarget, filtro, SgConfiguracionFirmaElectronica[].class, userToken);
        return Arrays.asList(configuraciones);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuracionesfirmaelectronica/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgConfiguracionFirmaElectronica guardar(SgConfiguracionFirmaElectronica configuracion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (configuracion == null || userToken == null) {
            return null;
        }
        if (configuracion.getConPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuracionesfirmaelectronica");
            return RestClient.invokePost(webTarget, configuracion, SgConfiguracionFirmaElectronica.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuracionesfirmaelectronica");
            webTarget = webTarget.path(configuracion.getConPk().toString());
            return RestClient.invokePut(webTarget, configuracion, SgConfiguracionFirmaElectronica.class, userToken);
        }
    }

    public SgConfiguracionFirmaElectronica obtenerPorId(Long configuracionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (configuracionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuracionesfirmaelectronica");
        webTarget = webTarget.path(configuracionPk.toString());
        return RestClient.invokeGet(webTarget, SgConfiguracionFirmaElectronica.class, userToken);
    }


    public List<SgConfiguracionFirmaElectronica> obtenerHistorialPorId(Long configuracionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (configuracionPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuracionesfirmaelectronica/historial");
        webTarget = webTarget.path(configuracionPk.toString());
        SgConfiguracionFirmaElectronica[] configuraciones = RestClient.invokeGet(webTarget, SgConfiguracionFirmaElectronica[].class, userToken);
        return Arrays.asList(configuraciones);
    }
    

}
