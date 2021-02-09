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
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.SgRhPagina;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPagina;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class PaginaRestClient implements Serializable {

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

    public PaginaRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRhPagina> buscar(FiltroPagina filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/paginas/buscar");
        SgRhPagina[] pagina = restClient.invokePost(webTarget, filtro, SgRhPagina[].class);
        return Arrays.asList(pagina);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroPagina filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/paginas/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRhPagina guardar(SgRhPagina pagina) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pagina == null) {
            return null;
        }
        if (pagina.getPagPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/paginas");
            return restClient.invokePost(webTarget, pagina, SgRhPagina.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/paginas");
            webTarget = webTarget.path(pagina.getPagPk().toString());
            return restClient.invokePut(webTarget, pagina, SgRhPagina.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgRhPagina obtenerPorId(Long etiPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (etiPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/paginas");
        webTarget = webTarget.path(etiPk.toString());
        return restClient.invokeGet(webTarget, SgRhPagina.class);
    }

    public void eliminar(Long archivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (archivoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/paginas");
        webTarget = webTarget.path(archivoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long archivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (archivoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_REGISTRO_HISTORICO, "v1/paginas/historial");
        webTarget = webTarget.path(archivoPk.toString());
        RevHistorico[] archivo = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(archivo);
    }

}
