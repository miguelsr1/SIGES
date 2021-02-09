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
import sv.gob.mined.siges.web.dto.IngresosEgresosReporte;
import sv.gob.mined.siges.web.dto.LibroIngresosEgresos;
import sv.gob.mined.siges.web.dto.siap2.SsTransferencia;
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
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class TransferenciaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    @Inject
    private RestClient restClient;

    private Client client = null;

    public TransferenciaRestClient() {
    }

    @PostConstruct
    public void init() {
        client = restClient.getClient();
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SsTransferencia> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferencias/buscar");
        SsTransferencia[] Transferencias = restClient.invokePost(webTarget, filtro, SsTransferencia[].class);
        return Arrays.asList(Transferencias);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferencias/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SsTransferencia guardar(SsTransferencia transferencia) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferencia == null || userToken == null) {
            return null;
        }
        if (transferencia.getTraId() == null) {
            WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferencias");
            return restClient.invokePost(webTarget, transferencia, SsTransferencia.class);
        } else {
            WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferencias");
            webTarget = webTarget.path(transferencia.getTraId().toString());
            return restClient.invokePut(webTarget, transferencia, SsTransferencia.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SsTransferencia obtenerPorId(Long transferenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferencias");
        webTarget = webTarget.path(transferenciaPk.toString());
        return restClient.invokeGet(webTarget, SsTransferencia.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SsTransferencia> obtenerHistorialPorId(Long transferenciaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (transferenciaPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = restClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferencias/historial");
        webTarget = webTarget.path(transferenciaPk.toString());
        SsTransferencia[] Transferencias = restClient.invokeGet(webTarget, SsTransferencia[].class);
        return Arrays.asList(Transferencias);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<IngresosEgresosReporte> buscarParaReporte(LibroIngresosEgresos libTransferencias) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (libTransferencias == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferencias/buscarParaReporte");
        IngresosEgresosReporte[] movimientoCuentaBancaria = restClient.invokePost(webTarget, libTransferencias, IngresosEgresosReporte[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<IngresosEgresosReporte> buscarParaReporteComponentes(LibroIngresosEgresos libTransferencias) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (libTransferencias == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/transferencias/buscarParaReporteComponentes");
        IngresosEgresosReporte[] movimientoCuentaBancaria = restClient.invokePost(webTarget, libTransferencias, IngresosEgresosReporte[].class);
        return Arrays.asList(movimientoCuentaBancaria);
    }

}
