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
import sv.gob.mined.siges.web.dto.SgExperienciaLaboral;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroExperienciaLaboral;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class ExperienciaLaboralRestClient implements Serializable {

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

    public ExperienciaLaboralRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgExperienciaLaboral> buscar(FiltroExperienciaLaboral filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/experiencialaboral/buscar");
        SgExperienciaLaboral[] experiencias = restClient.invokePost(webTarget, filtro, SgExperienciaLaboral[].class);
        return new ArrayList<>(Arrays.asList(experiencias));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroExperienciaLaboral filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/experiencialaboral/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgExperienciaLaboral guardar(SgExperienciaLaboral experienciaLaboral) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (experienciaLaboral == null) {
            return null;
        }    
        if (experienciaLaboral.getElaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/experiencialaboral");
            return restClient.invokePost(webTarget, experienciaLaboral, SgExperienciaLaboral.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/experiencialaboral");
            webTarget = webTarget.path(experienciaLaboral.getElaPk().toString());
            return restClient.invokePut(webTarget, experienciaLaboral, SgExperienciaLaboral.class);
        }
    }
    
    public SgExperienciaLaboral validarRealizado(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/experiencialaboral/validarrealizado");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokePost(webTarget, pk, SgExperienciaLaboral.class);
    }
    
    public SgExperienciaLaboral invalidarRealizado(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/experiencialaboral/invalidarrealizado");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokePost(webTarget, pk, SgExperienciaLaboral.class);
    }


    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgExperienciaLaboral obtenerPorId(Long experienciaLaboralPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (experienciaLaboralPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/experiencialaboral");
        webTarget = webTarget.path(experienciaLaboralPk.toString());
        return restClient.invokeGet(webTarget, SgExperienciaLaboral.class);
    }

    public void eliminar(Long experienciaLaboralPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (experienciaLaboralPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/experiencialaboral");
        webTarget = webTarget.path(experienciaLaboralPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long experienciaLaboralPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (experienciaLaboralPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/experiencialaboral/historial");
        webTarget = webTarget.path(experienciaLaboralPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    public Boolean validar(SgExperienciaLaboral experienciaLaboral) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (experienciaLaboral == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/experiencialaboral/validar");
        return restClient.invokePost(webTarget, experienciaLaboral, Boolean.class);
    }

}
