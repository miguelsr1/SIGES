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
import sv.gob.mined.siges.web.dto.SgMaterialPiso;
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
public class MaterialPisoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public MaterialPisoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMaterialPiso> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/materialespiso/buscar");
        SgMaterialPiso[] materialesPiso = RestClient.invokePost(webTarget, filtro, SgMaterialPiso[].class, userToken);
        return Arrays.asList(materialesPiso);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/materialespiso/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgMaterialPiso guardar(SgMaterialPiso materialPiso) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (materialPiso == null || userToken == null) {
            return null;
        }
        if (materialPiso.getMapPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/materialespiso");
            return RestClient.invokePost(webTarget, materialPiso, SgMaterialPiso.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/materialespiso");
            webTarget = webTarget.path(materialPiso.getMapPk().toString());
            return RestClient.invokePut(webTarget, materialPiso, SgMaterialPiso.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMaterialPiso obtenerPorId(Long materialPisoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (materialPisoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/materialespiso");
        webTarget = webTarget.path(materialPisoPk.toString());
        return RestClient.invokeGet(webTarget, SgMaterialPiso.class, userToken);
    }

    public void eliminar(Long materialPisoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (materialPisoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/materialespiso");
        webTarget = webTarget.path(materialPisoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMaterialPiso> obtenerHistorialPorId(Long materialPisoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (materialPisoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/materialespiso/historial");
        webTarget = webTarget.path(materialPisoPk.toString());
        SgMaterialPiso[] materialesPiso = RestClient.invokeGet(webTarget, SgMaterialPiso[].class, userToken);
        return Arrays.asList(materialesPiso);
    }
    

}
