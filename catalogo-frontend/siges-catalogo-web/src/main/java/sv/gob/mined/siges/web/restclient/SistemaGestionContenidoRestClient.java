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
import sv.gob.mined.siges.web.dto.SgSistemaGestionContenido;
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
public class SistemaGestionContenidoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public SistemaGestionContenidoRestClient() {
    }


    public List<SgSistemaGestionContenido> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sistemasgestioncontenido/buscar");
        SgSistemaGestionContenido[] sgcontenidos = RestClient.invokePost(webTarget, filtro, SgSistemaGestionContenido[].class, userToken);
        return Arrays.asList(sgcontenidos);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sistemasgestioncontenido/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgSistemaGestionContenido guardar(SgSistemaGestionContenido sgcontenido) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sgcontenido == null || userToken == null) {
            return null;
        }
        if (sgcontenido.getSgcPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sistemasgestioncontenido");
            return RestClient.invokePost(webTarget, sgcontenido, SgSistemaGestionContenido.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sistemasgestioncontenido");
            webTarget = webTarget.path(sgcontenido.getSgcPk().toString());
            return RestClient.invokePut(webTarget, sgcontenido, SgSistemaGestionContenido.class, userToken);
        }
    }

    public SgSistemaGestionContenido obtenerPorId(Long sgcontenidoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sgcontenidoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sistemasgestioncontenido");
        webTarget = webTarget.path(sgcontenidoPk.toString());
        return RestClient.invokeGet(webTarget, SgSistemaGestionContenido.class, userToken);
    }

    public void eliminar(Long sgcontenidoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sgcontenidoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sistemasgestioncontenido");
        webTarget = webTarget.path(sgcontenidoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgSistemaGestionContenido> obtenerHistorialPorId(Long sgcontenidoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sgcontenidoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sistemasgestioncontenido/historial");
        webTarget = webTarget.path(sgcontenidoPk.toString());
        SgSistemaGestionContenido[] sgcontenidos = RestClient.invokeGet(webTarget, SgSistemaGestionContenido[].class, userToken);
        return Arrays.asList(sgcontenidos);
    }
    

}
