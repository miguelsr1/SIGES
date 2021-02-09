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
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgEnfermedadNoTransmisible;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 3000L)
public class EnfermedadNoTransmisibleRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public EnfermedadNoTransmisibleRestClient() {
    }


    public List<SgEnfermedadNoTransmisible> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/enfermedadesnotransmisibles/buscar");
        SgEnfermedadNoTransmisible[] enfermedadesNoTransmisibles = RestClient.invokePost(webTarget, filtro, SgEnfermedadNoTransmisible[].class, userToken);
        return Arrays.asList(enfermedadesNoTransmisibles);
    }

    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/enfermedadesnotransmisibles/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgEnfermedadNoTransmisible guardar(SgEnfermedadNoTransmisible enfermedadNoTransmisible) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (enfermedadNoTransmisible == null || userToken == null) {
            return null;
        }
        if (enfermedadNoTransmisible.getEntPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/enfermedadesnotransmisibles");
            return RestClient.invokePost(webTarget, enfermedadNoTransmisible, SgEnfermedadNoTransmisible.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/enfermedadesnotransmisibles");
            webTarget = webTarget.path(enfermedadNoTransmisible.getEntPk().toString());
            return RestClient.invokePut(webTarget, enfermedadNoTransmisible, SgEnfermedadNoTransmisible.class, userToken);
        }
    }

    public SgEnfermedadNoTransmisible obtenerPorId(Long enfermedadNoTransmisiblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (enfermedadNoTransmisiblePk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/enfermedadesnotransmisibles");
        webTarget = webTarget.path(enfermedadNoTransmisiblePk.toString());
        return RestClient.invokeGet(webTarget, SgEnfermedadNoTransmisible.class, userToken);
    }

    public void eliminar(Long enfermedadNoTransmisiblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (enfermedadNoTransmisiblePk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/enfermedadesnotransmisibles");
        webTarget = webTarget.path(enfermedadNoTransmisiblePk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

    public List<SgEnfermedadNoTransmisible> obtenerHistorialPorId(Long enfermedadNoTransmisiblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (enfermedadNoTransmisiblePk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/enfermedadesnotransmisibles/historial");
        webTarget = webTarget.path(enfermedadNoTransmisiblePk.toString());
        SgEnfermedadNoTransmisible[] enfermedadesNoTransmisibles = RestClient.invokeGet(webTarget, SgEnfermedadNoTransmisible[].class, userToken);
        return Arrays.asList(enfermedadesNoTransmisibles);
    }
    

}