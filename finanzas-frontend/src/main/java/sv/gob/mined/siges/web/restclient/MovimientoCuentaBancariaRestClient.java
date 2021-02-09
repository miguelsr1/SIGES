/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
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
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.beans.MovimientoCuentaBancariaBean;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class MovimientoCuentaBancariaRestClient implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MovimientoCuentaBancariaBean.class.getName());

    @Inject
    @Named("userToken")
    private String userToken;

    @Inject
    private RestClient restClient;

    private Client client = null;

    public MovimientoCuentaBancariaRestClient() {
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
    public List<SgMovimientoCuentaBancaria> buscar(FiltroMovimientoCuentaBancaria filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria/buscar");
        SgMovimientoCuentaBancaria[] movimientoCuentaBancaria = restClient.invokePost(webTarget, filtro, SgMovimientoCuentaBancaria[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroMovimientoCuentaBancaria filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgMovimientoCuentaBancaria guardar(SgMovimientoCuentaBancaria movimientoCuentaBancaria) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoCuentaBancaria == null || userToken == null) {
            return null;
        }
        if (movimientoCuentaBancaria.getMcbPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria");
            return restClient.invokePost(webTarget, movimientoCuentaBancaria, SgMovimientoCuentaBancaria.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria");
            webTarget = webTarget.path(movimientoCuentaBancaria.getMcbPk().toString());
            return restClient.invokePut(webTarget, movimientoCuentaBancaria, SgMovimientoCuentaBancaria.class);
        }
    }

    public SgMovimientoCuentaBancaria cobrar(SgMovimientoCuentaBancaria movimientoCuentaBancaria) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {

        if (movimientoCuentaBancaria == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(movimientoCuentaBancaria);
        if (movimientoCuentaBancaria.getMcbPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria/cobrar");
            return restClient.invokePost(webTarget, movimientoCuentaBancaria, SgMovimientoCuentaBancaria.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria/cobrar");
            webTarget = webTarget.path(movimientoCuentaBancaria.getMcbPk().toString());
            return restClient.invokePut(webTarget, movimientoCuentaBancaria, SgMovimientoCuentaBancaria.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMovimientoCuentaBancaria obtenerPorId(Long movimientoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoCuentaBancariaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria");
        webTarget = webTarget.path(movimientoCuentaBancariaPk.toString());
        return restClient.invokeGet(webTarget, SgMovimientoCuentaBancaria.class);
    }

    public void eliminar(Long movimientoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoCuentaBancariaPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria");
        webTarget = webTarget.path(movimientoCuentaBancariaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMovimientoCuentaBancaria> obtenerHistorialPorId(Long movimientoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoCuentaBancariaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria/historial");
        webTarget = webTarget.path(movimientoCuentaBancariaPk.toString());
        SgMovimientoCuentaBancaria[] movimientoCuentaBancaria = restClient.invokeGet(webTarget, SgMovimientoCuentaBancaria[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }

}
