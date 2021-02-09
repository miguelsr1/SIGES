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
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonaOrganismoAdministrativo;

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
@Timeout(value = 10000L)
public class PersonaOrganismoAdministracionRestClient implements Serializable {

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
    public PersonaOrganismoAdministracionRestClient() {
    }


	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPersonaOrganismoAdministracion> buscar(FiltroPersonaOrganismoAdministrativo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personaorganismoadministracion/buscar");
        SgPersonaOrganismoAdministracion[] personaOrganismoAdministracion = restClient.invokePost(webTarget, filtro, SgPersonaOrganismoAdministracion[].class);
        return Arrays.asList(personaOrganismoAdministracion);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroPersonaOrganismoAdministrativo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personaorganismoadministracion/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgPersonaOrganismoAdministracion guardar(SgPersonaOrganismoAdministracion personaOrganismoAdministracion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaOrganismoAdministracion == null) {
            return null;
        }
        if (personaOrganismoAdministracion.getPoaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personaorganismoadministracion");
            return restClient.invokePost(webTarget, personaOrganismoAdministracion, SgPersonaOrganismoAdministracion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personaorganismoadministracion");
            webTarget = webTarget.path(personaOrganismoAdministracion.getPoaPk().toString());
            return restClient.invokePut(webTarget, personaOrganismoAdministracion, SgPersonaOrganismoAdministracion.class);
        }
    }
    
    public Boolean validar(SgPersonaOrganismoAdministracion familiar) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (familiar == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personaorganismoadministracion/validar");
        return restClient.invokePost(webTarget, familiar, Boolean.class);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgPersonaOrganismoAdministracion obtenerPorId(Long personaOrganismoAdministracionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaOrganismoAdministracionPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personaorganismoadministracion");
        webTarget = webTarget.path(personaOrganismoAdministracionPk.toString());
        return restClient.invokeGet(webTarget, SgPersonaOrganismoAdministracion.class);
    }

    public void eliminar(Long personaOrganismoAdministracionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaOrganismoAdministracionPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personaorganismoadministracion");
        webTarget = webTarget.path(personaOrganismoAdministracionPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPersonaOrganismoAdministracion> obtenerHistorialPorId(Long personaOrganismoAdministracionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaOrganismoAdministracionPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personaorganismoadministracion/historial");
        webTarget = webTarget.path(personaOrganismoAdministracionPk.toString());
        SgPersonaOrganismoAdministracion[] personaOrganismoAdministracion = restClient.invokeGet(webTarget, SgPersonaOrganismoAdministracion[].class);
        return Arrays.asList(personaOrganismoAdministracion);
    }
    

}
