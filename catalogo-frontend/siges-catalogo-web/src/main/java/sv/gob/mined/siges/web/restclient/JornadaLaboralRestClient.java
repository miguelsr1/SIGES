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
import sv.gob.mined.siges.web.dto.SgJornadaLaboral;
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
public class JornadaLaboralRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public JornadaLaboralRestClient() {
    }


    public List<SgJornadaLaboral> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/jornadaslaborales/buscar");
        SgJornadaLaboral[] jornadasLaborales = RestClient.invokePost(webTarget, filtro, SgJornadaLaboral[].class, userToken);
        return Arrays.asList(jornadasLaborales);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/jornadaslaborales/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgJornadaLaboral guardar(SgJornadaLaboral jornadaLaboral) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (jornadaLaboral == null || userToken == null) {
            return null;
        }
        if (jornadaLaboral.getJlaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/jornadaslaborales");
            return RestClient.invokePost(webTarget, jornadaLaboral, SgJornadaLaboral.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/jornadaslaborales");
            webTarget = webTarget.path(jornadaLaboral.getJlaPk().toString());
            return RestClient.invokePut(webTarget, jornadaLaboral, SgJornadaLaboral.class, userToken);
        }
    }

    public SgJornadaLaboral obtenerPorId(Long jornadaLaboralPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (jornadaLaboralPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/jornadaslaborales");
        webTarget = webTarget.path(jornadaLaboralPk.toString());
        return RestClient.invokeGet(webTarget, SgJornadaLaboral.class, userToken);
    }

    public void eliminar(Long jornadaLaboralPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (jornadaLaboralPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/jornadaslaborales");
        webTarget = webTarget.path(jornadaLaboralPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgJornadaLaboral> obtenerHistorialPorId(Long jornadaLaboralPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (jornadaLaboralPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/jornadaslaborales/historial");
        webTarget = webTarget.path(jornadaLaboralPk.toString());
        SgJornadaLaboral[] jornadasLaborales = RestClient.invokeGet(webTarget, SgJornadaLaboral[].class, userToken);
        return Arrays.asList(jornadasLaborales);
    }
    

}
