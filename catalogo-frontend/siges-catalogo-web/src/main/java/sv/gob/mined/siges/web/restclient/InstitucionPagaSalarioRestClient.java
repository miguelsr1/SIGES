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
import sv.gob.mined.siges.web.dto.SgInstitucionPagaSalario;
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
public class InstitucionPagaSalarioRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public InstitucionPagaSalarioRestClient() {
    }


    public List<SgInstitucionPagaSalario> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/institucionpagosalario/buscar");
        SgInstitucionPagaSalario[] institucionPagoSalario = RestClient.invokePost(webTarget, filtro, SgInstitucionPagaSalario[].class, userToken);
        return Arrays.asList(institucionPagoSalario);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/institucionpagosalario/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgInstitucionPagaSalario guardar(SgInstitucionPagaSalario institucionPagoSalario) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (institucionPagoSalario == null || userToken == null) {
            return null;
        }
        if (institucionPagoSalario.getIpsPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/institucionpagosalario");
            return RestClient.invokePost(webTarget, institucionPagoSalario, SgInstitucionPagaSalario.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/institucionpagosalario");
            webTarget = webTarget.path(institucionPagoSalario.getIpsPk().toString());
            return RestClient.invokePut(webTarget, institucionPagoSalario, SgInstitucionPagaSalario.class, userToken);
        }
    }

    public SgInstitucionPagaSalario obtenerPorId(Long institucionPagoSalarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (institucionPagoSalarioPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/institucionpagosalario");
        webTarget = webTarget.path(institucionPagoSalarioPk.toString());
        return RestClient.invokeGet(webTarget, SgInstitucionPagaSalario.class, userToken);
    }

    public void eliminar(Long institucionPagoSalarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (institucionPagoSalarioPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/institucionpagosalario");
        webTarget = webTarget.path(institucionPagoSalarioPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgInstitucionPagaSalario> obtenerHistorialPorId(Long institucionPagoSalarioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (institucionPagoSalarioPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/institucionpagosalario/historial");
        webTarget = webTarget.path(institucionPagoSalarioPk.toString());
        SgInstitucionPagaSalario[] institucionPagoSalario = RestClient.invokeGet(webTarget, SgInstitucionPagaSalario[].class, userToken);
        return Arrays.asList(institucionPagoSalario);
    }
    

}
