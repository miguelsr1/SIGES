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
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.SgCompromisoPresupuestario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCompromisoPresupuestario;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class CompromisoPresupuestarioRestClient implements Serializable {

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

    public CompromisoPresupuestarioRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgCompromisoPresupuestario> buscar(FiltroCompromisoPresupuestario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/compromisos_presupuestarios/buscar");
        SgCompromisoPresupuestario[] categorias = restClient.invokePost(webTarget, filtro, SgCompromisoPresupuestario[].class);
        return Arrays.asList(categorias);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCompromisoPresupuestario obtenerPorIdLazy(Long compromisoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (compromisoId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/compromisos_presupuestarios/lazy");
        webTarget = webTarget.path(compromisoId.toString());
        return restClient.invokeGet(webTarget, SgCompromisoPresupuestario.class);
    }

    public SgCompromisoPresupuestario guardar(SgCompromisoPresupuestario compromiso) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (compromiso == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(compromiso);
        if (compromiso.getCprPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/compromisos_presupuestarios");
            return restClient.invokePost(webTarget, compromiso, SgCompromisoPresupuestario.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/compromisos_presupuestarios");
            webTarget = webTarget.path(compromiso.getCprPk().toString());
            return restClient.invokePut(webTarget, compromiso, SgCompromisoPresupuestario.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCompromisoPresupuestario obtenerPorId(Long compromisoId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (compromisoId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/compromisos_presupuestarios");
        webTarget = webTarget.path(compromisoId.toString());
        return restClient.invokeGet(webTarget, SgCompromisoPresupuestario.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCompromisoPresupuestario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_FINANZAS, "v1/compromisos_presupuestarios/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

}
