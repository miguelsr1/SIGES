/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgOrganismoCoordinacionEscolar;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 3000L)
public class OrganismoCoordinacionEscolarRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public OrganismoCoordinacionEscolarRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgOrganismoCoordinacionEscolar> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/organismoscoordinacionescolar/buscar");
        SgOrganismoCoordinacionEscolar[] organismosCoordinacionEscolar = RestClient.invokePost(webTarget, filtro, SgOrganismoCoordinacionEscolar[].class, userToken);
        return Arrays.asList(organismosCoordinacionEscolar);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/organismoscoordinacionescolar/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgOrganismoCoordinacionEscolar guardar(SgOrganismoCoordinacionEscolar organismoCoordinacionEscolar) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organismoCoordinacionEscolar == null || userToken == null) {
            return null;
        }
        if (organismoCoordinacionEscolar.getOcePk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/organismoscoordinacionescolar");
            return RestClient.invokePost(webTarget, organismoCoordinacionEscolar, SgOrganismoCoordinacionEscolar.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/organismoscoordinacionescolar");
            webTarget = webTarget.path(organismoCoordinacionEscolar.getOcePk().toString());
            return RestClient.invokePut(webTarget, organismoCoordinacionEscolar, SgOrganismoCoordinacionEscolar.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgOrganismoCoordinacionEscolar obtenerPorId(Long organismoCoordinacionEscolarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organismoCoordinacionEscolarPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/organismoscoordinacionescolar");
        webTarget = webTarget.path(organismoCoordinacionEscolarPk.toString());
        return RestClient.invokeGet(webTarget, SgOrganismoCoordinacionEscolar.class, userToken);
    }

    public void eliminar(Long organismoCoordinacionEscolarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organismoCoordinacionEscolarPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/organismoscoordinacionescolar");
        webTarget = webTarget.path(organismoCoordinacionEscolarPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgOrganismoCoordinacionEscolar> obtenerHistorialPorId(Long organismoCoordinacionEscolarPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organismoCoordinacionEscolarPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/organismoscoordinacionescolar/historial");
        webTarget = webTarget.path(organismoCoordinacionEscolarPk.toString());
        SgOrganismoCoordinacionEscolar[] organismosCoordinacionEscolar = RestClient.invokeGet(webTarget, SgOrganismoCoordinacionEscolar[].class, userToken);
        return Arrays.asList(organismosCoordinacionEscolar);
    }
    

}
