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
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.client.excepciones.HttpClientException;
import sv.gob.mined.siges.client.excepciones.HttpServerException;
import sv.gob.mined.siges.client.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.dto.catalogo.SgProyectoFinanciamientoSistemaIntegrado;

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
public class ProyectoFinanciamientoSistemaIntegradoRestClient implements Serializable {

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

    public ProyectoFinanciamientoSistemaIntegradoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgProyectoFinanciamientoSistemaIntegrado> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado/buscar");
        SgProyectoFinanciamientoSistemaIntegrado[] proyectosFinanciamientoSistemaIntegrado = restClient.invokePost(webTarget, filtro, SgProyectoFinanciamientoSistemaIntegrado[].class);
        return Arrays.asList(proyectosFinanciamientoSistemaIntegrado);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgProyectoFinanciamientoSistemaIntegrado guardar(SgProyectoFinanciamientoSistemaIntegrado proyectoFinanciamientoSistemaIntegrado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegrado == null) {
            return null;
        }
        if (proyectoFinanciamientoSistemaIntegrado.getPfsPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado");
            return restClient.invokePost(webTarget, proyectoFinanciamientoSistemaIntegrado, SgProyectoFinanciamientoSistemaIntegrado.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado");
            webTarget = webTarget.path(proyectoFinanciamientoSistemaIntegrado.getPfsPk().toString());
            return restClient.invokePut(webTarget, proyectoFinanciamientoSistemaIntegrado, SgProyectoFinanciamientoSistemaIntegrado.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgProyectoFinanciamientoSistemaIntegrado obtenerPorId(Long proyectoFinanciamientoSistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegradoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado");
        webTarget = webTarget.path(proyectoFinanciamientoSistemaIntegradoPk.toString());
        return restClient.invokeGet(webTarget, SgProyectoFinanciamientoSistemaIntegrado.class);
    }

    public void eliminar(Long proyectoFinanciamientoSistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegradoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado");
        webTarget = webTarget.path(proyectoFinanciamientoSistemaIntegradoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgProyectoFinanciamientoSistemaIntegrado> obtenerHistorialPorId(Long proyectoFinanciamientoSistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoFinanciamientoSistemaIntegradoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosfinanciamientosistemaintegrado/historial");
        webTarget = webTarget.path(proyectoFinanciamientoSistemaIntegradoPk.toString());
        SgProyectoFinanciamientoSistemaIntegrado[] proyectosFinanciamientoSistemaIntegrado = restClient.invokeGet(webTarget, SgProyectoFinanciamientoSistemaIntegrado[].class);
        return Arrays.asList(proyectosFinanciamientoSistemaIntegrado);
    }

}
