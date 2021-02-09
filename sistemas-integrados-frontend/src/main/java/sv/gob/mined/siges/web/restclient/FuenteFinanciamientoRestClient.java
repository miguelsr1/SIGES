/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
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
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.client.excepciones.HttpClientException;
import sv.gob.mined.siges.client.excepciones.HttpServerException;
import sv.gob.mined.siges.client.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 3000L)
public class FuenteFinanciamientoRestClient implements Serializable {

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

    public FuenteFinanciamientoRestClient() {
    }

    public List<SgFuenteFinanciamiento> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento/buscar");
        SgFuenteFinanciamiento[] fuenteFinanciamiento = restClient.invokePost(webTarget, filtro, SgFuenteFinanciamiento[].class);
        return Arrays.asList(fuenteFinanciamiento);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgFuenteFinanciamiento guardar(SgFuenteFinanciamiento fuenteFinanciamiento) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteFinanciamiento == null) {
            return null;
        }
        if (fuenteFinanciamiento.getFfiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento");
            return restClient.invokePost(webTarget, fuenteFinanciamiento, SgFuenteFinanciamiento.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento");
            webTarget = webTarget.path(fuenteFinanciamiento.getFfiPk().toString());
            return restClient.invokePut(webTarget, fuenteFinanciamiento, SgFuenteFinanciamiento.class);
        }
    }

    public SgFuenteFinanciamiento obtenerPorId(Long fuenteFinanciamientoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteFinanciamientoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento");
        webTarget = webTarget.path(fuenteFinanciamientoPk.toString());
        return restClient.invokeGet(webTarget, SgFuenteFinanciamiento.class);
    }

    public void eliminar(Long fuenteFinanciamientoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteFinanciamientoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento");
        webTarget = webTarget.path(fuenteFinanciamientoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<SgFuenteFinanciamiento> obtenerHistorialPorId(Long fuenteFinanciamientoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (fuenteFinanciamientoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento/historial");
        webTarget = webTarget.path(fuenteFinanciamientoPk.toString());
        SgFuenteFinanciamiento[] fuenteFinanciamiento = restClient.invokeGet(webTarget, SgFuenteFinanciamiento[].class);
        return Arrays.asList(fuenteFinanciamiento);
    }

}
