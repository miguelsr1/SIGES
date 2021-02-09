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
import sv.gob.mined.siges.web.dto.SgRelSedeDiplomado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelSedeDiplomado;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Traced
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class RelSedeDiplomadoRestClient implements Serializable {

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
    
    public RelSedeDiplomadoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelSedeDiplomado> buscar(FiltroRelSedeDiplomado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsedediplomado/buscar");
        SgRelSedeDiplomado[] relSedeDiplomado = restClient.invokePost(webTarget, filtro, SgRelSedeDiplomado[].class);
        return Arrays.asList(relSedeDiplomado);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelSedeDiplomado filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsedediplomado/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelSedeDiplomado guardar(SgRelSedeDiplomado relSedeDiplomado) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relSedeDiplomado == null ) {
            return null;
        }
        if (relSedeDiplomado.getRsdPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsedediplomado");
            return restClient.invokePost(webTarget, relSedeDiplomado, SgRelSedeDiplomado.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsedediplomado");
            webTarget = webTarget.path(relSedeDiplomado.getRsdPk().toString());
            return restClient.invokePut(webTarget, relSedeDiplomado, SgRelSedeDiplomado.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelSedeDiplomado obtenerPorId(Long relSedeDiplomadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relSedeDiplomadoPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsedediplomado");
        webTarget = webTarget.path(relSedeDiplomadoPk.toString());
        return restClient.invokeGet(webTarget, SgRelSedeDiplomado.class);
    }

    public void eliminar(Long relSedeDiplomadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relSedeDiplomadoPk == null ) {
            return;
        }        
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsedediplomado");
        webTarget = webTarget.path(relSedeDiplomadoPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relSedeDiplomadoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relSedeDiplomadoPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget =  RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/relsedediplomado/historial");
        webTarget = webTarget.path(relSedeDiplomadoPk.toString());
        RevHistorico[] relSedeDiplomado = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relSedeDiplomado);
    }
    

}
