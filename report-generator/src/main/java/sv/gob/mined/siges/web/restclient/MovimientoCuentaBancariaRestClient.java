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
import sv.gob.mined.siges.web.dto.CuentaBancariaReporte;
import sv.gob.mined.siges.web.dto.LibroBanco;
import sv.gob.mined.siges.web.dto.finanzas.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCuentaBancaria;

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
public class MovimientoCuentaBancariaRestClient implements Serializable {

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

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMovimientoCuentaBancaria obtenerPorId(Long movimientoCuentaBancariaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (movimientoCuentaBancariaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria");
        webTarget = webTarget.path(movimientoCuentaBancariaPk.toString());
        return restClient.invokeGet(webTarget, SgMovimientoCuentaBancaria.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<CuentaBancariaReporte> buscarParaReporte(LibroBanco libBac) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (libBac == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria/buscarParaReporte");
        CuentaBancariaReporte[] movimientoCuentaBancaria = restClient.invokePost(webTarget, libBac, CuentaBancariaReporte[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<CuentaBancariaReporte> buscarComponentes(LibroBanco libBac) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (libBac == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria/buscarComponentes");
        CuentaBancariaReporte[] movimientoCuentaBancaria = restClient.invokePost(webTarget, libBac, CuentaBancariaReporte[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<CuentaBancariaReporte> buscarParaReporteComponente(LibroBanco libBac) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (libBac == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/movimientocuentabancaria/buscarParaReporteComponente");
        CuentaBancariaReporte[] movimientoCuentaBancaria = restClient.invokePost(webTarget, libBac, CuentaBancariaReporte[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }

}
