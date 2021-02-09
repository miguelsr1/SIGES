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
import sv.gob.mined.siges.web.dto.SgArchivoCalificacionPAES;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroArchivoCalificacionPAES;

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
public class ArchivoCalificacionPAESRestClient implements Serializable {

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

    public ArchivoCalificacionPAESRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgArchivoCalificacionPAES> buscar(FiltroArchivoCalificacionPAES filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/archivoscalificacionespaes/buscar");
        SgArchivoCalificacionPAES[] ciclos = restClient.invokePost(webTarget, filtro, SgArchivoCalificacionPAES[].class);
        return Arrays.asList(ciclos);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroArchivoCalificacionPAES filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/archivoscalificacionespaes/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgArchivoCalificacionPAES obtenerPorId(Long cicloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cicloPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/archivoscalificacionespaes");
        webTarget = webTarget.path(cicloPk.toString());
        return restClient.invokeGet(webTarget, SgArchivoCalificacionPAES.class);
    }

    @Timeout(value = 5000000L)
    @Retry(maxRetries = 0)
    public SgArchivoCalificacionPAES guardar(SgArchivoCalificacionPAES ciclo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (ciclo == null) {
            return null;
        }
        if (ciclo.getAcpPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.FILE_PROCESSOR, "v1/archivoscalificacionespaes");
            return restClient.invokePost(webTarget, ciclo, SgArchivoCalificacionPAES.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.FILE_PROCESSOR, "v1/archivoscalificacionespaes");
            webTarget = webTarget.path(ciclo.getAcpPk().toString());
            return restClient.invokePut(webTarget, ciclo, SgArchivoCalificacionPAES.class);
        }
    }
    @Timeout(value = 5000000L)
    public void ejecutarProcesamientoArchivos() throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException{
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.FILE_PROCESSOR, "v1/archivoscalificacionespaes/calificarArchivosPAES");
        restClient.invokeGet(webTarget,null);
    }

    @Timeout(value = 10000L)
    public void eliminar(Long paesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (paesPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/archivoscalificacionespaes");
        webTarget = webTarget.path(paesPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long cicloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cicloPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/archivoscalificacionespaes/historial");
        webTarget = webTarget.path(cicloPk.toString());
        RevHistorico[] ciclos = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(ciclos);
    }

}
