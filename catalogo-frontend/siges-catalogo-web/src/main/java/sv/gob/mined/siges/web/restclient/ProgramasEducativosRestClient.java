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
import sv.gob.mined.siges.web.dto.SgProgramaEducativo;
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
public class ProgramasEducativosRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ProgramasEducativosRestClient() {
    }


    public List<SgProgramaEducativo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programaseducativos/buscar");
        SgProgramaEducativo[] programasEducativos = RestClient.invokePost(webTarget, filtro, SgProgramaEducativo[].class, userToken);
        return Arrays.asList(programasEducativos);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programaseducativos/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgProgramaEducativo guardar(SgProgramaEducativo programasEducativos) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (programasEducativos == null || userToken == null) {
            return null;
        }
        if (programasEducativos.getPedPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programaseducativos");
            return RestClient.invokePost(webTarget, programasEducativos, SgProgramaEducativo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programaseducativos");
            webTarget = webTarget.path(programasEducativos.getPedPk().toString());
            return RestClient.invokePut(webTarget, programasEducativos, SgProgramaEducativo.class, userToken);
        }
    }

    public SgProgramaEducativo obtenerPorId(Long programasEducativosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (programasEducativosPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programaseducativos");
        webTarget = webTarget.path(programasEducativosPk.toString());
        return RestClient.invokeGet(webTarget, SgProgramaEducativo.class, userToken);
    }

    public void eliminar(Long programasEducativosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (programasEducativosPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programaseducativos");
        webTarget = webTarget.path(programasEducativosPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgProgramaEducativo> obtenerHistorialPorId(Long programasEducativosPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (programasEducativosPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programaseducativos/historial");
        webTarget = webTarget.path(programasEducativosPk.toString());
        SgProgramaEducativo[] programasEducativos = RestClient.invokeGet(webTarget, SgProgramaEducativo[].class, userToken);
        return Arrays.asList(programasEducativos);
    }
    

}
