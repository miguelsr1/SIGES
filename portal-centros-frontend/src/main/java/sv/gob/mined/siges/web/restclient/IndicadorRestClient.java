/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgEstIndicador;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIndicadores;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class IndicadorRestClient implements Serializable {

    @Inject
    private RestClient restClient;

    private Client client = null;
    
    @PostConstruct
    public void init() {
        client = RestClient.getClient();
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            client.close();
        }
    }
    
    public IndicadorRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgEstIndicador> buscar(FiltroIndicadores filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.ESTADISTICAS, "v1/indicadores/buscar");
        SgEstIndicador[] indicadores = restClient.invokePost(webTarget, filtro, SgEstIndicador[].class);
        return Arrays.asList(indicadores);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroIndicadores filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.ESTADISTICAS, "v1/indicadores/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgEstIndicador guardar(SgEstIndicador indicador) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (indicador == null ) {
            return null;
        }
        if (indicador.getIndPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.ESTADISTICAS, "v1/indicadores");
            return restClient.invokePost(webTarget, indicador, SgEstIndicador.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.ESTADISTICAS, "v1/indicadores");
            webTarget = webTarget.path(indicador.getIndPk().toString());
            return restClient.invokePut(webTarget, indicador, SgEstIndicador.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgEstIndicador obtenerPorId(Long indicadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (indicadorPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.ESTADISTICAS, "v1/indicadores");
        webTarget = webTarget.path(indicadorPk.toString());
        return restClient.invokeGet(webTarget, SgEstIndicador.class);
    }

    public void eliminar(Long indicadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (indicadorPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.ESTADISTICAS, "v1/indicadores");
        webTarget = webTarget.path(indicadorPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgEstIndicador> obtenerHistorialPorId(Long indicadorPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (indicadorPk == null ) {
            return new ArrayList<>();
        }
         WebTarget webTarget =  RestClient.getWebTarget(client,ConstantesServiciosRest.ESTADISTICAS, "v1/indicadores/historial");
        webTarget = webTarget.path(indicadorPk.toString());
        SgEstIndicador[] indicadores = restClient.invokeGet(webTarget, SgEstIndicador[].class);
        return Arrays.asList(indicadores);
    }
    

}
