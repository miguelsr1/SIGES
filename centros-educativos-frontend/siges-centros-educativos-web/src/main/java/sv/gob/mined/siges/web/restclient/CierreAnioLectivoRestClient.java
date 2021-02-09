package sv.gob.mined.siges.web.restclient;

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
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgCierreAnioLectivoSede;
import sv.gob.mined.siges.web.dto.SgResumenCierreSedeRequest;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCierreAnioLectivoSede;

/**
 *
 * @author bruno
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class CierreAnioLectivoRestClient {

    @Inject
    private RestClient restClient;

    private Client client = null;

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

    public CierreAnioLectivoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgCierreAnioLectivoSede> buscar(FiltroCierreAnioLectivoSede filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cierresanioslectivos/buscar");
        SgCierreAnioLectivoSede[] ciclos = restClient.invokePost(webTarget, filtro, SgCierreAnioLectivoSede[].class);
        return Arrays.asList(ciclos);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCierreAnioLectivoSede filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cierresanioslectivos/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCierreAnioLectivoSede guardar(SgCierreAnioLectivoSede cierreAnioLectivoSede) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cierreAnioLectivoSede == null) {
            return null;
        }
        if (cierreAnioLectivoSede.getCalPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cierresanioslectivos");
            return restClient.invokePost(webTarget, cierreAnioLectivoSede, SgCierreAnioLectivoSede.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cierresanioslectivos");
            webTarget = webTarget.path(cierreAnioLectivoSede.getCalPk().toString());
            return restClient.invokePut(webTarget, cierreAnioLectivoSede, SgCierreAnioLectivoSede.class);
        }
    }

    public SgCierreAnioLectivoSede confirmar(String firmaTransactionId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (firmaTransactionId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/cierresanioslectivos/confirmar");
        webTarget = webTarget.path(firmaTransactionId);
        return restClient.invokePost(webTarget, null, SgCierreAnioLectivoSede.class);
    }

    @Timeout(value = 40000L)
    public SgCierreAnioLectivoSede preconfirmar(SgCierreAnioLectivoSede cierre) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cierre == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/cierresanioslectivos/preconfirmar");
        return restClient.invokePost(webTarget, cierre, SgCierreAnioLectivoSede.class);
    }

    @Timeout(value = 30000L)
    public void eliminar(Long calsPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calsPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cierresanioslectivos");
        webTarget = webTarget.path(calsPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCierreAnioLectivoSede obtenerResumenCierreSedeAnio(SgResumenCierreSedeRequest req) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (req == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/cierresanioslectivos/resumencierreanio");
        return restClient.invokePost(webTarget, req, SgCierreAnioLectivoSede.class);
    }
}
