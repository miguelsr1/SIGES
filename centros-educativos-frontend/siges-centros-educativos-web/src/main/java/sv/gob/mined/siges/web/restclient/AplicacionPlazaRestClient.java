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
import sv.gob.mined.siges.web.dto.SgAplicacionPlaza;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAplicacionPlaza;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 10000L)
public class AplicacionPlazaRestClient implements Serializable {

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

    public AplicacionPlazaRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAplicacionPlaza> buscar(FiltroAplicacionPlaza filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/aplicacionesplaza/buscar");
        SgAplicacionPlaza[] organizacionesCurricular = restClient.invokePost(webTarget, filtro, SgAplicacionPlaza[].class);
        return new ArrayList(Arrays.asList(organizacionesCurricular));
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroAplicacionPlaza filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/aplicacionesplaza/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAplicacionPlaza guardar(SgAplicacionPlaza aplicacionPlaza) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (aplicacionPlaza == null) {
            return null;
        }
        if (aplicacionPlaza.getAplPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/aplicacionesplaza");
            return restClient.invokePost(webTarget, aplicacionPlaza, SgAplicacionPlaza.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/aplicacionesplaza");
            webTarget = webTarget.path(aplicacionPlaza.getAplPk().toString());
            return restClient.invokePut(webTarget, aplicacionPlaza, SgAplicacionPlaza.class);
        }
    }
    

    public SgAplicacionPlaza guardarSeleccionadoMatriz(SgAplicacionPlaza aplicacionPlaza) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (aplicacionPlaza == null) {
            return null;
        }
     
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/aplicacionesplaza/seleccionadoMatriz");
        webTarget = webTarget.path(aplicacionPlaza.getAplPk().toString());
        return restClient.invokePut(webTarget, aplicacionPlaza, SgAplicacionPlaza.class);
        
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAplicacionPlaza obtenerPorId(Long aplicacionPlazaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (aplicacionPlazaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/aplicacionesplaza");
        webTarget = webTarget.path(aplicacionPlazaPk.toString());
        return restClient.invokeGet(webTarget, SgAplicacionPlaza.class);
    }

    public void eliminar(Long aplicacionPlazaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (aplicacionPlazaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/aplicacionesplaza");
        webTarget = webTarget.path(aplicacionPlazaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<SgAplicacionPlaza> obtenerHistorialPorId(Long aplicacionPlazaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (aplicacionPlazaPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/aplicacionesplaza/historial");
        webTarget = webTarget.path(aplicacionPlazaPk.toString());
        SgAplicacionPlaza[] organizacionesCurricular = restClient.invokeGet(webTarget, SgAplicacionPlaza[].class);
        return Arrays.asList(organizacionesCurricular);
    }

}
