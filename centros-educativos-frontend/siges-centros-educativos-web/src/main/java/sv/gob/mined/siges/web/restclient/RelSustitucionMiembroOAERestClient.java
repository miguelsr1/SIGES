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
import sv.gob.mined.siges.web.dto.SgRelSustitucionMiembroOAE;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelSustitucionMiembroOAE;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;

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
public class RelSustitucionMiembroOAERestClient implements Serializable {

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

    public RelSustitucionMiembroOAERestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelSustitucionMiembroOAE> buscar(FiltroRelSustitucionMiembroOAE filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsustitucionmiembrooae/buscar");
        SgRelSustitucionMiembroOAE[] relSustitucionMiembroOAE = restClient.invokePost(webTarget, filtro, SgRelSustitucionMiembroOAE[].class);
        return Arrays.asList(relSustitucionMiembroOAE);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelSustitucionMiembroOAE filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsustitucionmiembrooae/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelSustitucionMiembroOAE guardar(SgRelSustitucionMiembroOAE relSustitucionMiembroOAE) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relSustitucionMiembroOAE == null ) {
            return null;
        }
        if (relSustitucionMiembroOAE.getRsmPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsustitucionmiembrooae");
            return restClient.invokePost(webTarget, relSustitucionMiembroOAE, SgRelSustitucionMiembroOAE.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsustitucionmiembrooae");
            webTarget = webTarget.path(relSustitucionMiembroOAE.getRsmPk().toString());
            return restClient.invokePut(webTarget, relSustitucionMiembroOAE, SgRelSustitucionMiembroOAE.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelSustitucionMiembroOAE obtenerPorId(Long relSustitucionMiembroOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relSustitucionMiembroOAEPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsustitucionmiembrooae");
        webTarget = webTarget.path(relSustitucionMiembroOAEPk.toString());
        return restClient.invokeGet(webTarget, SgRelSustitucionMiembroOAE.class);
    }

    public void eliminar(Long relSustitucionMiembroOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relSustitucionMiembroOAEPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsustitucionmiembrooae");
        webTarget = webTarget.path(relSustitucionMiembroOAEPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relSustitucionMiembroOAEPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relSustitucionMiembroOAEPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsustitucionmiembrooae/historial");
        webTarget = webTarget.path(relSustitucionMiembroOAEPk.toString());
        RevHistorico[] relSustitucionMiembroOAE = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relSustitucionMiembroOAE);
    }
    

}
