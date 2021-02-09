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
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.utils.ValidationUtils;
import sv.gob.mined.siges.web.dto.centroseducativos.SgPersona;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersona;
import sv.gob.mined.siges.web.mensajes.Mensajes;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 8000L)
public class PersonaRestClient implements Serializable {

    @Inject
    private RestClient restClient;

    private Client client = null;
    
    @Inject
    @ConfigProperty(name = "lucene", defaultValue = "true")
    private Boolean luceneEnabled;

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

    public PersonaRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgPersona> buscar(FiltroPersona filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscar");
        SgPersona[] sede = restClient.invokePost(webTarget, filtro, SgPersona[].class);
        return new ArrayList<>(Arrays.asList(sede));
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgPersona obtenerPorId(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas");
        webTarget = webTarget.path(personaId.toString());
        return restClient.invokeGet(webTarget, SgPersona.class);
    }


    public Boolean validarIdentificaciones(SgPersona persona) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (persona == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/validaridentificaciones");
        return restClient.invokePost(webTarget, persona, Boolean.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgPersona> obtenerPersonasPorIdentificacion(FiltroPersona filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        /**FiltroPersona fp = new FiltroPersona();
        fp.setMaxResults(1L);
        fp.setDui(p.getPerDui());
        **/
        if (!StringUtils.isBlank(filtro.getDui())) {
            if (!ValidationUtils.validarDUI(filtro.getDui())) {
                BusinessException be = new BusinessException();
                be.addError("perDui", Mensajes.ERROR_DUI_INCORRECTO);
                throw be;
            }
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscar");
        SgPersona[] personas = restClient.invokePost(webTarget, filtro, SgPersona[].class);
        return new ArrayList<>(Arrays.asList(personas));

    }

}
