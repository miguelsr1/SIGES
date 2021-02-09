/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgProgramaInstitucional;
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
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 3000L)
public class ProgramaInstitucionalRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ProgramaInstitucionalRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgProgramaInstitucional> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programasinstitucional/buscar");
        SgProgramaInstitucional[] programasInstitucional = RestClient.invokePost(webTarget, filtro, SgProgramaInstitucional[].class, userToken);
        return Arrays.asList(programasInstitucional);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programasinstitucional/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgProgramaInstitucional guardar(SgProgramaInstitucional programaInstitucional) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (programaInstitucional == null || userToken == null) {
            return null;
        }
        if (programaInstitucional.getPinPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programasinstitucional");
            return RestClient.invokePost(webTarget, programaInstitucional, SgProgramaInstitucional.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programasinstitucional");
            webTarget = webTarget.path(programaInstitucional.getPinPk().toString());
            return RestClient.invokePut(webTarget, programaInstitucional, SgProgramaInstitucional.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgProgramaInstitucional obtenerPorId(Long programaInstitucionalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (programaInstitucionalPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programasinstitucional");
        webTarget = webTarget.path(programaInstitucionalPk.toString());
        return RestClient.invokeGet(webTarget, SgProgramaInstitucional.class, userToken);
    }

    public void eliminar(Long programaInstitucionalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (programaInstitucionalPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programasinstitucional");
        webTarget = webTarget.path(programaInstitucionalPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgProgramaInstitucional> obtenerHistorialPorId(Long programaInstitucionalPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (programaInstitucionalPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programasinstitucional/historial");
        webTarget = webTarget.path(programaInstitucionalPk.toString());
        SgProgramaInstitucional[] programasInstitucional = RestClient.invokeGet(webTarget, SgProgramaInstitucional[].class, userToken);
        return Arrays.asList(programasInstitucional);
    }
    

}
