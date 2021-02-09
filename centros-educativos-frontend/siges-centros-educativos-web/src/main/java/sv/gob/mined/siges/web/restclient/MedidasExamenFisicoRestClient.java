/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

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
import sv.gob.mined.siges.web.dto.SgMedidasExamenFisico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMedidasExamenFisico;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@Traced
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class MedidasExamenFisicoRestClient implements Serializable {

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

    public MedidasExamenFisicoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgMedidasExamenFisico> buscar(FiltroMedidasExamenFisico filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/medidasexamenfisico/buscar");
        SgMedidasExamenFisico[] medidasExamenFisico = restClient.invokePost(webTarget, filtro, SgMedidasExamenFisico[].class);
        return Arrays.asList(medidasExamenFisico);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroMedidasExamenFisico filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/medidasexamenfisico/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgMedidasExamenFisico guardar(SgMedidasExamenFisico medidasExamenFisico) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (medidasExamenFisico == null ) {
            return null;
        }
        if (medidasExamenFisico.getMefPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/medidasexamenfisico");
            return restClient.invokePost(webTarget, medidasExamenFisico, SgMedidasExamenFisico.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/medidasexamenfisico");
            webTarget = webTarget.path(medidasExamenFisico.getMefPk().toString());
            return restClient.invokePut(webTarget, medidasExamenFisico, SgMedidasExamenFisico.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMedidasExamenFisico obtenerPorId(Long medidasExamenFisicoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (medidasExamenFisicoPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/medidasexamenfisico");
        webTarget = webTarget.path(medidasExamenFisicoPk.toString());
        return restClient.invokeGet(webTarget, SgMedidasExamenFisico.class);
    }

    public void eliminar(Long medidasExamenFisicoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (medidasExamenFisicoPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/medidasexamenfisico");
        webTarget = webTarget.path(medidasExamenFisicoPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long medidasExamenFisicoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (medidasExamenFisicoPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/medidasexamenfisico/historial");
        webTarget = webTarget.path(medidasExamenFisicoPk.toString());
        RevHistorico[] medidasExamenFisico = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(medidasExamenFisico);
    }
    

}
