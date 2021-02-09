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
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.SgRelSedeRiesgoAfectacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelSedeRiesgoAfectacion;

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
public class RelSedeRiesgoAfectacionRestClient implements Serializable {

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
    
    public RelSedeRiesgoAfectacionRestClient() {
    }


	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgRelSedeRiesgoAfectacion> buscar(FiltroRelSedeRiesgoAfectacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsederiesgoafectacion/buscar");
        SgRelSedeRiesgoAfectacion[] relSedeRiesgoAfectacion = restClient.invokePost(webTarget, filtro, SgRelSedeRiesgoAfectacion[].class);
        return Arrays.asList(relSedeRiesgoAfectacion);
    }



    public SgRelSedeRiesgoAfectacion guardar(SgRelSedeRiesgoAfectacion relSedeRiesgoAfectacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relSedeRiesgoAfectacion == null ) {
            return null;
        }
        if (relSedeRiesgoAfectacion.getRarPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsederiesgoafectacion");
            return restClient.invokePost(webTarget, relSedeRiesgoAfectacion, SgRelSedeRiesgoAfectacion.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsederiesgoafectacion");
            webTarget = webTarget.path(relSedeRiesgoAfectacion.getRarPk().toString());
            return restClient.invokePut(webTarget, relSedeRiesgoAfectacion, SgRelSedeRiesgoAfectacion.class);
        }
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgRelSedeRiesgoAfectacion obtenerPorId(Long relSedeRiesgoAfectacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relSedeRiesgoAfectacionPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS,  "v1/relsederiesgoafectacion");
        webTarget = webTarget.path(relSedeRiesgoAfectacionPk.toString());
        return restClient.invokeGet(webTarget, SgRelSedeRiesgoAfectacion.class);
    }

    public void eliminar(Long relSedeRiesgoAfectacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relSedeRiesgoAfectacionPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsederiesgoafectacion");
        webTarget = webTarget.path(relSedeRiesgoAfectacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    

}
