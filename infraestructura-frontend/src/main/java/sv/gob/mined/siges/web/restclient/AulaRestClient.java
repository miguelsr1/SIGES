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
import sv.gob.mined.siges.web.dto.SgAula;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAula;

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
public class AulaRestClient implements Serializable {

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
    
    public AulaRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgAula> buscar(FiltroAula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/aula/buscar");
        SgAula[] aula = restClient.invokePost(webTarget, filtro, SgAula[].class);
        return Arrays.asList(aula);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroAula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/aula/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgAula guardar(SgAula aula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (aula == null) {
            return null;
        }
        if (aula.getAulPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/aula");
            return restClient.invokePost(webTarget, aula, SgAula.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/aula");
            webTarget = webTarget.path(aula.getAulPk().toString());
            return restClient.invokePut(webTarget, aula, SgAula.class);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgAula obtenerPorId(Long aulaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (aulaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/aula");
        webTarget = webTarget.path(aulaPk.toString());
        return restClient.invokeGet(webTarget, SgAula.class);
    }

    public void eliminar(Long aulaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (aulaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.INFRAESTRUCTURA, "v1/aula");
        webTarget = webTarget.path(aulaPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<RevHistorico> obtenerHistorialPorId(Long aulaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (aulaPk == null) {
            return new ArrayList<>();
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/aula/historial");
        webTarget = webTarget.path(aulaPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }
   
    public SgAula guardarListaEspacioComunes(SgAula aula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (aula == null) {
            return null;
        }
       if (aula.getAulPk()!= null){
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/aula/actualizarListaEspacio");
            webTarget = webTarget.path(aula.getAulPk().toString());
            return restClient.invokePut(webTarget, aula, SgAula.class);
        }
        return aula;
    }
     
     public SgAula guardarListaServiciosBasicos(SgAula aula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (aula == null) {
            return null;
        }
       if (aula.getAulPk()!= null){
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.INFRAESTRUCTURA, "v1/aula/actualizarListaServicio");
            webTarget = webTarget.path(aula.getAulPk().toString());
            return restClient.invokePut(webTarget, aula, SgAula.class);
        }
        return aula;
    }

}
