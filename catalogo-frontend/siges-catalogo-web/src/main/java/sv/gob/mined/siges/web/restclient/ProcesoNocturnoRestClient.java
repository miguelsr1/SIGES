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
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgProcesoNocturno;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 70000)
@Timeout(value = 7000L)
public class ProcesoNocturnoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public ProcesoNocturnoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgProcesoNocturno> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/procesonocturno/buscar");
        SgProcesoNocturno[] procesoNocturno = RestClient.invokePost(webTarget, filtro, SgProcesoNocturno[].class, userToken);
        return Arrays.asList(procesoNocturno);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/procesonocturno/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgProcesoNocturno guardar(SgProcesoNocturno procesoNocturno) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (procesoNocturno == null || userToken == null) {
            return null;
        }
        if (procesoNocturno.getPrnPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/procesonocturno");
            return RestClient.invokePost(webTarget, procesoNocturno, SgProcesoNocturno.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/procesonocturno");
            webTarget = webTarget.path(procesoNocturno.getPrnPk().toString());
            return RestClient.invokePut(webTarget, procesoNocturno, SgProcesoNocturno.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgProcesoNocturno obtenerPorId(Long procesoNocturnoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (procesoNocturnoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/procesonocturno");
        webTarget = webTarget.path(procesoNocturnoPk.toString());
        return RestClient.invokeGet(webTarget, SgProcesoNocturno.class, userToken);
    }

    public void eliminar(Long procesoNocturnoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (procesoNocturnoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/procesonocturno");
        webTarget = webTarget.path(procesoNocturnoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgProcesoNocturno> obtenerHistorialPorId(Long procesoNocturnoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (procesoNocturnoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/procesonocturno/historial");
        webTarget = webTarget.path(procesoNocturnoPk.toString());
        SgProcesoNocturno[] procesoNocturno = RestClient.invokeGet(webTarget, SgProcesoNocturno[].class, userToken);
        return Arrays.asList(procesoNocturno);
    }
    
    @Timeout(value = 5000000L)
    public Boolean ejecutarProcesamiento(SgProcesoNocturno proceso) throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException{
        if (proceso == null || userToken == null) {
            return Boolean.FALSE;
        }

        WebTarget webTarget = RestClient.getWebTargetProcesoNocturno( proceso.getPrnServicio().getText(), proceso.getPrnUrl());
        return RestClient.invokeGet(webTarget,Boolean.class,userToken);
    }
}
