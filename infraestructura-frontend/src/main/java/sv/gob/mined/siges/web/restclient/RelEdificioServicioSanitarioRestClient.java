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
import sv.gob.mined.siges.web.dto.SgRelEdificioServicioSanitario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelEdificioServicioSanitario;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class RelEdificioServicioSanitarioRestClient implements Serializable {

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
    
    public RelEdificioServicioSanitarioRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgRelEdificioServicioSanitario> buscar(FiltroRelEdificioServicioSanitario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioserviciosanitario/buscar");
        SgRelEdificioServicioSanitario[] relEdificioServicio = restClient.invokePost(webTarget, filtro, SgRelEdificioServicioSanitario[].class);
        return Arrays.asList(relEdificioServicio);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroRelEdificioServicioSanitario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioserviciosanitario/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgRelEdificioServicioSanitario guardar(SgRelEdificioServicioSanitario relEdificioServicio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relEdificioServicio == null ) {
            return null;
        }
        if (relEdificioServicio.getRetPk()== null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioserviciosanitario");
            return restClient.invokePost(webTarget, relEdificioServicio, SgRelEdificioServicioSanitario.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioserviciosanitario");
            webTarget = webTarget.path(relEdificioServicio.getRetPk().toString());
            return restClient.invokePut(webTarget, relEdificioServicio, SgRelEdificioServicioSanitario.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgRelEdificioServicioSanitario obtenerPorId(Long relEdificioServicioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relEdificioServicioPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioserviciosanitario");
        webTarget = webTarget.path(relEdificioServicioPk.toString());
        return restClient.invokeGet(webTarget, SgRelEdificioServicioSanitario.class);
    }

    public void eliminar(Long relEdificioServicioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relEdificioServicioPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioserviciosanitario");
        webTarget = webTarget.path(relEdificioServicioPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long relEdificioServicioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (relEdificioServicioPk == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/reledificioserviciosanitario/historial");
        webTarget = webTarget.path(relEdificioServicioPk.toString());
        RevHistorico[] relEdificioServicio = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(relEdificioServicio);
    }
    

}
