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
import java.util.logging.Logger;
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
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.dto.SgSistemaIntegrado;
import sv.gob.mined.siges.client.excepciones.HttpClientException;
import sv.gob.mined.siges.client.excepciones.HttpServerException;
import sv.gob.mined.siges.client.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.dto.SgConsultaCalifiacionesAreasBasicas;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPromedioCalificaciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSistemaIntegrado;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 6000L)
public class SistemaIntegradoRestClient implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SistemaIntegradoRestClient.class.getName());

    @Inject
    private RestClient restClient;

    private Client client = null;

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

    public SistemaIntegradoRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgSistemaIntegrado> buscar(FiltroSistemaIntegrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados/buscar");
        SgSistemaIntegrado[] sistemaIntegrado = restClient.invokePost(webTarget, filtro, SgSistemaIntegrado[].class);
        return new ArrayList<>(Arrays.asList(sistemaIntegrado));
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroSistemaIntegrado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgSistemaIntegrado guardar(SgSistemaIntegrado sistemaIntegrado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sistemaIntegrado == null) {
            return null;
        }
        if (sistemaIntegrado.getSinPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados");
            return restClient.invokePost(webTarget, sistemaIntegrado, SgSistemaIntegrado.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados");
            webTarget = webTarget.path(sistemaIntegrado.getSinPk().toString());
            return restClient.invokePut(webTarget, sistemaIntegrado, SgSistemaIntegrado.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgSistemaIntegrado obtenerPorId(Long sistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sistemaIntegradoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados");
        webTarget = webTarget.path(sistemaIntegradoPk.toString());
        return restClient.invokeGet(webTarget, SgSistemaIntegrado.class);
    }

    public void eliminar(Long sistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sistemaIntegradoPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados");
        webTarget = webTarget.path(sistemaIntegradoPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long sistemaIntegradoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sistemaIntegradoPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados/historial");
        webTarget = webTarget.path(sistemaIntegradoPk.toString());
        RevHistorico[] sistemaIntegrado = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(sistemaIntegrado);
    }

    public SgSistemaIntegrado obtenerEnRevision(Long sistemaIntegradoPk, Long sistemaIntegradoRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sistemaIntegradoPk == null || sistemaIntegradoRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados/revision");
        webTarget = webTarget.path(sistemaIntegradoPk.toString());
        webTarget = webTarget.path(sistemaIntegradoRev.toString());
        return restClient.invokeGet(webTarget, SgSistemaIntegrado.class);
    }

    public List<SgConsultaCalifiacionesAreasBasicas> obtenerAsistenciasPorSedeEnNivel(FiltroPromedioCalificaciones filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/sistemasintegrados/obtenerpromediocalificacionesporarea");
        SgConsultaCalifiacionesAreasBasicas[] calificaciones = restClient.invokePost(webTarget,  filtro, SgConsultaCalifiacionesAreasBasicas[].class);
        return Arrays.asList(calificaciones);
        
    }

}
