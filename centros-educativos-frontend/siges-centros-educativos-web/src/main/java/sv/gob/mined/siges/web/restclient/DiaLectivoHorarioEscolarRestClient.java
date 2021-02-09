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
import sv.gob.mined.siges.web.dto.SgDiaLectivoHorarioEscolar;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiaLectivoHorarioEscolar;

/**
 *
 * @author Sofis Solutions
 */
@Named
@Traced
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 10000L)
public class DiaLectivoHorarioEscolarRestClient implements Serializable {

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
    
    public DiaLectivoHorarioEscolarRestClient() {
    }


	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgDiaLectivoHorarioEscolar> buscar(FiltroDiaLectivoHorarioEscolar filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/dialectivohorarioescolar/buscar");
        SgDiaLectivoHorarioEscolar[] diaLectivoHorarioEscolar = restClient.invokePost(webTarget, filtro, SgDiaLectivoHorarioEscolar[].class);
        return Arrays.asList(diaLectivoHorarioEscolar);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroDiaLectivoHorarioEscolar filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/dialectivohorarioescolar/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgDiaLectivoHorarioEscolar guardar(SgDiaLectivoHorarioEscolar diaLectivoHorarioEscolar) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (diaLectivoHorarioEscolar == null) {
            return null;
        }
        if (diaLectivoHorarioEscolar.getDlhPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/dialectivohorarioescolar");
            return restClient.invokePost(webTarget, diaLectivoHorarioEscolar, SgDiaLectivoHorarioEscolar.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/dialectivohorarioescolar");
            webTarget = webTarget.path(diaLectivoHorarioEscolar.getDlhPk().toString());
            return restClient.invokePut(webTarget, diaLectivoHorarioEscolar, SgDiaLectivoHorarioEscolar.class);
        }
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgDiaLectivoHorarioEscolar obtenerPorId(Long diaLectivoHorarioEscolarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (diaLectivoHorarioEscolarPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/dialectivohorarioescolar");
        webTarget = webTarget.path(diaLectivoHorarioEscolarPk.toString());
        return restClient.invokeGet(webTarget, SgDiaLectivoHorarioEscolar.class);
    }

    public void eliminar(Long diaLectivoHorarioEscolarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (diaLectivoHorarioEscolarPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/dialectivohorarioescolar");
        webTarget = webTarget.path(diaLectivoHorarioEscolarPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long diaLectivoHorarioEscolarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (diaLectivoHorarioEscolarPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS,"v1/dialectivohorarioescolar/historial");
        webTarget = webTarget.path(diaLectivoHorarioEscolarPk.toString());
        RevHistorico[] diplomas = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(diplomas);
        
    }
    

}
