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
import sv.gob.mined.siges.web.dto.SgEstCategoriaIndicador;
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
public class EstCategoriaIndicadorRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EstCategoriaIndicadorRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstCategoriaIndicador> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriasindicador/buscar");
        SgEstCategoriaIndicador[] categoriasIndicador = RestClient.invokePost(webTarget, filtro, SgEstCategoriaIndicador[].class, userToken);
        return Arrays.asList(categoriasIndicador);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriasindicador/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEstCategoriaIndicador guardar(SgEstCategoriaIndicador estCategoriaIndicador) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estCategoriaIndicador == null || userToken == null) {
            return null;
        }
        if (estCategoriaIndicador.getCinPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriasindicador");
            return RestClient.invokePost(webTarget, estCategoriaIndicador, SgEstCategoriaIndicador.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriasindicador");
            webTarget = webTarget.path(estCategoriaIndicador.getCinPk().toString());
            return RestClient.invokePut(webTarget, estCategoriaIndicador, SgEstCategoriaIndicador.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgEstCategoriaIndicador obtenerPorId(Long estCategoriaIndicadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estCategoriaIndicadorPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriasindicador");
        webTarget = webTarget.path(estCategoriaIndicadorPk.toString());
        return RestClient.invokeGet(webTarget, SgEstCategoriaIndicador.class, userToken);
    }

    public void eliminar(Long estCategoriaIndicadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estCategoriaIndicadorPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriasindicador");
        webTarget = webTarget.path(estCategoriaIndicadorPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgEstCategoriaIndicador> obtenerHistorialPorId(Long estCategoriaIndicadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estCategoriaIndicadorPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriasindicador/historial");
        webTarget = webTarget.path(estCategoriaIndicadorPk.toString());
        SgEstCategoriaIndicador[] categoriasIndicador = RestClient.invokeGet(webTarget, SgEstCategoriaIndicador[].class, userToken);
        return Arrays.asList(categoriasIndicador);
    }
    

}
