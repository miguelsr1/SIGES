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
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgModuloDiplomado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModuloDiplomado;

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
public class ModuloDiplomadoRestClient implements Serializable {

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

    public ModuloDiplomadoRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgModuloDiplomado> buscar(FiltroModuloDiplomado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosdiploma/buscar");
        SgModuloDiplomado[] diplomas = restClient.invokePost(webTarget, filtro, SgModuloDiplomado[].class);
        return Arrays.asList(diplomas);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroModuloDiplomado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosdiploma/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgModuloDiplomado guardar(SgModuloDiplomado diplomado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (diplomado == null) {
            return null;
        }
        if (diplomado.getMdiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosdiploma");
            return restClient.invokePost(webTarget, diplomado, SgModuloDiplomado.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosdiploma");
            webTarget = webTarget.path(diplomado.getMdiPk().toString());
            return restClient.invokePut(webTarget, diplomado, SgModuloDiplomado.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgModuloDiplomado obtenerPorId(Long diplomadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (diplomadoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosdiploma");
        webTarget = webTarget.path(diplomadoPk.toString());
        return restClient.invokeGet(webTarget, SgModuloDiplomado.class);
    }

    public void eliminar(Long diplomadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (diplomadoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosdiploma");
        webTarget = webTarget.path(diplomadoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long diplomadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (diplomadoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/modulosdiploma/historial");
        webTarget = webTarget.path(diplomadoPk.toString());
        RevHistorico[] diplomas = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(diplomas);
    }

}
