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
import sv.gob.mined.siges.web.dto.SgGestionAnioLectivo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGestionAnioLectivo;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class GestionAnioLectivoRestClient implements Serializable {

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

    public GestionAnioLectivoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgGestionAnioLectivo> buscar(FiltroGestionAnioLectivo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/gestionaniolectivo/buscar");
        SgGestionAnioLectivo[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgGestionAnioLectivo[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroGestionAnioLectivo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/gestionaniolectivo/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgGestionAnioLectivo guardar(SgGestionAnioLectivo gestionAnioLectivo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gestionAnioLectivo == null) {
            return null;
        }
        if (gestionAnioLectivo.getGalPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/gestionaniolectivo");
            return restClient.invokePost(webTarget, gestionAnioLectivo, SgGestionAnioLectivo.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/gestionaniolectivo");
            webTarget = webTarget.path(gestionAnioLectivo.getGalPk().toString());
            return restClient.invokePut(webTarget, gestionAnioLectivo, SgGestionAnioLectivo.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgGestionAnioLectivo obtenerPorId(Long gestionAnioLectivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gestionAnioLectivoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/gestionaniolectivo");
        webTarget = webTarget.path(gestionAnioLectivoPk.toString());
        return restClient.invokeGet(webTarget, SgGestionAnioLectivo.class);
    }

    public void eliminar(Long gestionAnioLectivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gestionAnioLectivoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/gestionaniolectivo");
        webTarget = webTarget.path(gestionAnioLectivoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long gestionAnioLectivoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (gestionAnioLectivoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/gestionaniolectivo/historial");
        webTarget = webTarget.path(gestionAnioLectivoPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

}
