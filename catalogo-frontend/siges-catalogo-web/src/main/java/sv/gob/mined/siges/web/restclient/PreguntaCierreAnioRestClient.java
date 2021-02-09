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
import sv.gob.mined.siges.web.dto.SgPreguntaCierreAnio;
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
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 3000L)
public class PreguntaCierreAnioRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public PreguntaCierreAnioRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgPreguntaCierreAnio> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/preguntacierreanio/buscar");
        SgPreguntaCierreAnio[] preguntaCierreAnio = RestClient.invokePost(webTarget, filtro, SgPreguntaCierreAnio[].class, userToken);
        return Arrays.asList(preguntaCierreAnio);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/preguntacierreanio/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgPreguntaCierreAnio guardar(SgPreguntaCierreAnio preguntaCierreAnio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (preguntaCierreAnio == null || userToken == null) {
            return null;
        }
        if (preguntaCierreAnio.getPcaPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/preguntacierreanio");
            return RestClient.invokePost(webTarget, preguntaCierreAnio, SgPreguntaCierreAnio.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/preguntacierreanio");
            webTarget = webTarget.path(preguntaCierreAnio.getPcaPk().toString());
            return RestClient.invokePut(webTarget, preguntaCierreAnio, SgPreguntaCierreAnio.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgPreguntaCierreAnio obtenerPorId(Long preguntaCierreAnioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (preguntaCierreAnioPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/preguntacierreanio");
        webTarget = webTarget.path(preguntaCierreAnioPk.toString());
        return RestClient.invokeGet(webTarget, SgPreguntaCierreAnio.class, userToken);
    }

    public void eliminar(Long preguntaCierreAnioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (preguntaCierreAnioPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/preguntacierreanio");
        webTarget = webTarget.path(preguntaCierreAnioPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgPreguntaCierreAnio> obtenerHistorialPorId(Long preguntaCierreAnioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (preguntaCierreAnioPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/preguntacierreanio/historial");
        webTarget = webTarget.path(preguntaCierreAnioPk.toString());
        SgPreguntaCierreAnio[] preguntaCierreAnio = RestClient.invokeGet(webTarget, SgPreguntaCierreAnio[].class, userToken);
        return Arrays.asList(preguntaCierreAnio);
    }
    

}
