/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient.centros_educativos;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.dto.centros_educativos.SgPersona;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroPersona;
import sv.gob.mined.siges.web.restclient.RestClient;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class PersonaRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    @Inject
    @ConfigProperty(name = "lucene", defaultValue = "false")
    private Boolean luceneEnabled;
    
    public PersonaRestClient() {
    }

    public List<SgPersona> buscar(FiltroPersona filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscar");
        SgPersona[] sede = RestClient.invokePost(webTarget, filtro, SgPersona[].class, userToken);
        return new ArrayList<>(Arrays.asList(sede));
    }
    
    public Long buscarTotal(FiltroPersona filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgPersona obtenerPorId(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas");
        webTarget = webTarget.path(personaId.toString());        
        return RestClient.invokeGet(webTarget, SgPersona.class, userToken);
    }
    
    public SgPersona guardar(SgPersona persona) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (persona == null || userToken == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(persona);
        if (persona.getPerPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas");
            return RestClient.invokePost(webTarget, persona, SgPersona.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas");
            webTarget = webTarget.path(persona.getPerPk().toString());
            return RestClient.invokePut(webTarget, persona, SgPersona.class, userToken);
        }
    }    
    
    public Boolean validar(SgPersona persona) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (persona == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/validar");
        return RestClient.invokePost(webTarget, persona, Boolean.class, userToken);
    }    

    public void eliminar(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas");
        webTarget = webTarget.path(personaId.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/historial");
        webTarget = webTarget.path(personaId.toString());
        RevHistorico[] sede = RestClient.invokeGet(webTarget, RevHistorico[].class, userToken);
        return Arrays.asList(sede);
    }
    
    public SgPersona obtenerEnRevision(Long personaPk, Long personaRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaPk == null || personaRev == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/revision");
        webTarget = webTarget.path(personaPk.toString());
        webTarget = webTarget.path(personaRev.toString());
        return RestClient.invokeGet(webTarget, SgPersona.class, userToken);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPersona> buscarLucene(FiltroPersona filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = null;
        if (luceneEnabled){
            webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscarlucene");
        } else {
            webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscar");
        }
        SgPersona[] sede = RestClient.invokePost(webTarget, filtro, SgPersona[].class, userToken);
        return new ArrayList<>(Arrays.asList(sede));
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotalLucene(FiltroPersona filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = null;
        if (luceneEnabled){
            webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscarlucene/total");
        } else {
            webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscar/total");
        }
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }
}
