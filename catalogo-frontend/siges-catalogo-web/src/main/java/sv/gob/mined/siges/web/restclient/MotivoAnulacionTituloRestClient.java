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
import sv.gob.mined.siges.web.dto.SgMotivoAnulacionTitulo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class MotivoAnulacionTituloRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MotivoAnulacionTituloRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMotivoAnulacionTitulo> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoanulaciontitulo/buscar");
        SgMotivoAnulacionTitulo[] motivoAnulacionTitulo = RestClient.invokePost(webTarget, filtro, SgMotivoAnulacionTitulo[].class, userToken);
        return Arrays.asList(motivoAnulacionTitulo);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoanulaciontitulo/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMotivoAnulacionTitulo guardar(SgMotivoAnulacionTitulo motivoAnulacionTitulo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoAnulacionTitulo == null || userToken == null) {
            return null;
        }
        if (motivoAnulacionTitulo.getMatPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoanulaciontitulo");
            return RestClient.invokePost(webTarget, motivoAnulacionTitulo, SgMotivoAnulacionTitulo.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoanulaciontitulo");
            webTarget = webTarget.path(motivoAnulacionTitulo.getMatPk().toString());
            return RestClient.invokePut(webTarget, motivoAnulacionTitulo, SgMotivoAnulacionTitulo.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMotivoAnulacionTitulo obtenerPorId(Long motivoAnulacionTituloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoAnulacionTituloPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoanulaciontitulo");
        webTarget = webTarget.path(motivoAnulacionTituloPk.toString());
        return RestClient.invokeGet(webTarget, SgMotivoAnulacionTitulo.class, userToken);
    }

    public void eliminar(Long motivoAnulacionTituloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoAnulacionTituloPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoanulaciontitulo");
        webTarget = webTarget.path(motivoAnulacionTituloPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMotivoAnulacionTitulo> obtenerHistorialPorId(Long motivoAnulacionTituloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (motivoAnulacionTituloPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoanulaciontitulo/historial");
        webTarget = webTarget.path(motivoAnulacionTituloPk.toString());
        SgMotivoAnulacionTitulo[] motivoAnulacionTitulo = RestClient.invokeGet(webTarget, SgMotivoAnulacionTitulo[].class, userToken);
        return Arrays.asList(motivoAnulacionTitulo);
    }
    

}
