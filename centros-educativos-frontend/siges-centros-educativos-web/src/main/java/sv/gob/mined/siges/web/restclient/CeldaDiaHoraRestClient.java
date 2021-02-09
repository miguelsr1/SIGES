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
import sv.gob.mined.siges.web.dto.SgCeldaDiaHora;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCeldaDiaHora;

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
public class CeldaDiaHoraRestClient implements Serializable {

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

    public CeldaDiaHoraRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgCeldaDiaHora> buscar(FiltroCeldaDiaHora filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/celdasdiahora/buscar");
        SgCeldaDiaHora[] celdasDiaHora = restClient.invokePost(webTarget, filtro, SgCeldaDiaHora[].class);
        return Arrays.asList(celdasDiaHora);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/celdasdiahora/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCeldaDiaHora guardar(SgCeldaDiaHora celdaDiaHora) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (celdaDiaHora == null) {
            return null;
        }
        if (celdaDiaHora.getCdhPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/celdasdiahora");
            return restClient.invokePost(webTarget, celdaDiaHora, SgCeldaDiaHora.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/celdasdiahora");
            webTarget = webTarget.path(celdaDiaHora.getCdhPk().toString());
            return restClient.invokePut(webTarget, celdaDiaHora, SgCeldaDiaHora.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCeldaDiaHora obtenerPorId(Long celdaDiaHoraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (celdaDiaHoraPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/celdasdiahora");
        webTarget = webTarget.path(celdaDiaHoraPk.toString());
        return restClient.invokeGet(webTarget, SgCeldaDiaHora.class);
    }

    public void eliminar(Long celdaDiaHoraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (celdaDiaHoraPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/celdasdiahora");
        webTarget = webTarget.path(celdaDiaHoraPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<SgCeldaDiaHora> obtenerHistorialPorId(Long celdaDiaHoraPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (celdaDiaHoraPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/celdasdiahora/historial");
        webTarget = webTarget.path(celdaDiaHoraPk.toString());
        SgCeldaDiaHora[] celdasDiaHora = restClient.invokeGet(webTarget, SgCeldaDiaHora[].class);
        return Arrays.asList(celdasDiaHora);
    }

}
