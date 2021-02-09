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
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOrganismoAdministrativoEscolar;

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
public class OrganismoAdministracionEscolarRestClient implements Serializable {

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
    public OrganismoAdministracionEscolarRestClient() {
    }


	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgOrganismoAdministracionEscolar> buscar(FiltroOrganismoAdministrativoEscolar filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoadministracionescolar/buscar");
        SgOrganismoAdministracionEscolar[] organismoAdministracionEscolar = restClient.invokePost(webTarget, filtro, SgOrganismoAdministracionEscolar[].class);
        return Arrays.asList(organismoAdministracionEscolar);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroOrganismoAdministrativoEscolar filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoadministracionescolar/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgOrganismoAdministracionEscolar guardar(SgOrganismoAdministracionEscolar organismoAdministracionEscolar) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organismoAdministracionEscolar == null) {
            return null;
        }
        if (organismoAdministracionEscolar.getOaePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoadministracionescolar");
            return restClient.invokePost(webTarget, organismoAdministracionEscolar, SgOrganismoAdministracionEscolar.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoadministracionescolar");
            webTarget = webTarget.path(organismoAdministracionEscolar.getOaePk().toString());
            return restClient.invokePut(webTarget, organismoAdministracionEscolar, SgOrganismoAdministracionEscolar.class);
        }
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgOrganismoAdministracionEscolar obtenerPorId(Long organismoAdministracionEscolarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organismoAdministracionEscolarPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoadministracionescolar");
        webTarget = webTarget.path(organismoAdministracionEscolarPk.toString());
        return restClient.invokeGet(webTarget, SgOrganismoAdministracionEscolar.class);
    }

    public void eliminar(Long organismoAdministracionEscolarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organismoAdministracionEscolarPk == null ) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoadministracionescolar");
        webTarget = webTarget.path(organismoAdministracionEscolarPk.toString());
        restClient.invokeDelete(webTarget);
    }

	@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long organismoAdministracionEscolarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organismoAdministracionEscolarPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/organismoadministracionescolar/historial");
        webTarget = webTarget.path(organismoAdministracionEscolarPk.toString());
        RevHistorico[] organismoAdministracionEscolar = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organismoAdministracionEscolar);
    }
    

}
