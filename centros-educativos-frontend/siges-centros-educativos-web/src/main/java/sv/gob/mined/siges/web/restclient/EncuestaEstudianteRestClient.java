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
import sv.gob.mined.siges.web.dto.SgEncuestaEstudiante;
import sv.gob.mined.siges.web.dto.SgMenorEncuestaEstudiante;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEncuestaEstudiante;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class EncuestaEstudianteRestClient implements Serializable {

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

    public EncuestaEstudianteRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgEncuestaEstudiante> buscar(FiltroEncuestaEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/encuestasestudiantes/buscar");
        SgEncuestaEstudiante[] sede = restClient.invokePost(webTarget, filtro, SgEncuestaEstudiante[].class);
        return new ArrayList<>(Arrays.asList(sede));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroEncuestaEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/encuestasestudiantes/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }
    

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgEncuestaEstudiante obtenerPorId(Long encuestaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (encuestaId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/encuestasestudiantes");
        webTarget = webTarget.path(encuestaId.toString());
        return restClient.invokeGet(webTarget, SgEncuestaEstudiante.class);
    }

    public SgEncuestaEstudiante guardar(SgEncuestaEstudiante persona) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (persona == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(persona);
        if (persona.getEncPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/encuestasestudiantes");
            return restClient.invokePost(webTarget, persona, SgEncuestaEstudiante.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/encuestasestudiantes");
            webTarget = webTarget.path(persona.getEncPk().toString());
            return restClient.invokePut(webTarget, persona, SgEncuestaEstudiante.class);
        }
    }

    public Boolean validar(SgEncuestaEstudiante persona) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (persona == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/encuestasestudiantes/validar");
        return restClient.invokePost(webTarget, persona, Boolean.class);
    }
    
    public Boolean validar(SgMenorEncuestaEstudiante menor) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (menor == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/encuestasestudiantes/menores/validar");
        return restClient.invokePost(webTarget, menor, Boolean.class);
    }


    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public void eliminar(Long encuestaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (encuestaId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/encuestasestudiantes");
        webTarget = webTarget.path(encuestaId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long encuestaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (encuestaId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/encuestasestudiantes/historial");
        webTarget = webTarget.path(encuestaId.toString());
        RevHistorico[] sede = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(sede);
    }

    @Timeout(value = 20000L)
    public SgEncuestaEstudiante obtenerEnRevision(Long encuestaPk, Long encuestaRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (encuestaPk == null || encuestaRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/encuestasestudiantes/revision");
        webTarget = webTarget.path(encuestaPk.toString());
        webTarget = webTarget.path(encuestaRev.toString());
        return restClient.invokeGet(webTarget, SgEncuestaEstudiante.class);
    }

    
    @Timeout(value = 80000L)
    public byte[] exportarObtenerPorFiltroExcel(FiltroEncuestaEstudiante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/encuestasestudiantes/exportarconsultaexcel");
        return restClient.invokePostReturnFile(webTarget, filtro, byte[].class);
    }
    
}
