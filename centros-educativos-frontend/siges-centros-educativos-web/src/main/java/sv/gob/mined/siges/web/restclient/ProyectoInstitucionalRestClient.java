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
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgProyectoInstitucionalSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyectoInstitucionalSede;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 999000L)
public class ProyectoInstitucionalRestClient implements Serializable {

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

    public ProyectoInstitucionalRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgProyectoInstitucionalSede> buscar(FiltroProyectoInstitucionalSede filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstitucionalessede/buscar");
        SgProyectoInstitucionalSede[] lista = restClient.invokePost(webTarget, filtro, SgProyectoInstitucionalSede[].class);
        return Arrays.asList(lista);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroProyectoInstitucionalSede filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstitucionalessede/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgProyectoInstitucionalSede obtenerPorId(Long proyectoInstitucionalSedeId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstitucionalSedeId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstitucionalessede");
        webTarget = webTarget.path(proyectoInstitucionalSedeId.toString());
        return restClient.invokeGet(webTarget, SgProyectoInstitucionalSede.class);
    }

    public SgProyectoInstitucionalSede guardar(SgProyectoInstitucionalSede proyectoInstitucionalSede) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstitucionalSede == null) {
            return null;
        }
        if (proyectoInstitucionalSede.getProPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstitucionalessede");
            return restClient.invokePost(webTarget, proyectoInstitucionalSede, SgProyectoInstitucionalSede.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstitucionalessede");
            webTarget = webTarget.path(proyectoInstitucionalSede.getProPk().toString());
            return restClient.invokePut(webTarget, proyectoInstitucionalSede, SgProyectoInstitucionalSede.class);
        }
    }

    public void eliminar(Long proyectoInstitucionalSedeId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstitucionalSedeId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstitucionalessede");
        webTarget = webTarget.path(proyectoInstitucionalSedeId.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long proyectoInstitucionalSedeId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoInstitucionalSedeId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/proyectosinstitucionalessede/historial");
        webTarget = webTarget.path(proyectoInstitucionalSedeId.toString());
        RevHistorico[] lista = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(lista);
    }

}
