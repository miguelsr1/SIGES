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
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.CajaChicaReporte;
import sv.gob.mined.siges.web.dto.LibroCajaChica;
import sv.gob.mined.siges.web.dto.finanzas.SgMovimientoCajaChica;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCajaChica;

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
public class MovimientoCajaChicaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    @Inject
    private RestClient restClient;

    private Client client = null;

    public MovimientoCajaChicaRestClient() {
    }

    @PostConstruct
    public void init() {
        client = restClient.getClient();
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            //client.close();
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMovimientoCajaChica> buscar(FiltroMovimientoCajaChica filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientoscajachica/buscar");
        SgMovimientoCajaChica[] movimientoCuentaBancaria = restClient.invokePost(webTarget, filtro, SgMovimientoCajaChica[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroMovimientoCajaChica filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientoscajachica/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMovimientoCajaChica obtenerPorId(Long movimientoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoCuentaBancariaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientoscajachica");
        webTarget = webTarget.path(movimientoCuentaBancariaPk.toString());
        return restClient.invokeGet(webTarget, SgMovimientoCajaChica.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMovimientoCajaChica> obtenerHistorialPorId(Long movimientoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoCuentaBancariaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientoscajachica/historial");
        webTarget = webTarget.path(movimientoCuentaBancariaPk.toString());
        SgMovimientoCajaChica[] movimientoCuentaBancaria = restClient.invokeGet(webTarget, SgMovimientoCajaChica[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<CajaChicaReporte> buscarParaReporte(LibroCajaChica libCaja) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (libCaja == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientoscajachica/buscarParaReporte");
        CajaChicaReporte[] movimientoCuentaBancaria = restClient.invokePost(webTarget, libCaja, CajaChicaReporte[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }

}
