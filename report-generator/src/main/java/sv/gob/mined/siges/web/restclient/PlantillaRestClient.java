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
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlantilla;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 1000)
@Timeout(value = 10000L)
public class PlantillaRestClient implements Serializable {

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

    public PlantillaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgPlantilla obtenerPorId(Long plantillaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (plantillaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla");
        webTarget = webTarget.path(plantillaPk.toString());
        return restClient.invokeGet(webTarget, SgPlantilla.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, Exception.class}, delay = 1000)
    public List<SgPlantilla> buscar(FiltroPlantilla filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/plantilla/buscar");
        SgPlantilla[] plantilla = restClient.invokePost(webTarget, filtro, SgPlantilla[].class);
        return Arrays.asList(plantilla);
    }

    public SgPlantilla obtenerPorCodigo(String codigo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigo == null) {
            return null;
        }
        SgPlantilla ret = null;
        FiltroPlantilla filtro = new FiltroPlantilla();
        filtro.setCodigoExacto(codigo);
        filtro.setHabilitado(Boolean.TRUE);
        filtro.setSoloPlantillasPorDefecto(Boolean.TRUE);
        List<SgPlantilla> plantillas = this.buscar(filtro);
        if (!plantillas.isEmpty()) {
            ret = plantillas.get(0);
        }
        return ret;
    }

    public SgPlantilla obtenerPorCodigo(String codigo, Long orgCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigo == null) {
            return null;
        }
        SgPlantilla ret = null;
        if (orgCurricularPk != null) {
            FiltroPlantilla filtro = new FiltroPlantilla();
            filtro.setCodigoExacto(codigo);
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setOrgCurricularPk(orgCurricularPk);
            List<SgPlantilla> plantillas = this.buscar(filtro);
            if (!plantillas.isEmpty()) {
                ret = plantillas.get(0);
            }
        }

        if (ret == null) {

            FiltroPlantilla filtro = new FiltroPlantilla();
            filtro.setCodigoExacto(codigo);
            filtro.setSoloPlantillasPorDefecto(Boolean.TRUE);
            List<SgPlantilla> plantillas = this.buscar(filtro);
            if (!plantillas.isEmpty()) {
                ret = plantillas.get(0);
            }

        }

        return ret;
    }

}
