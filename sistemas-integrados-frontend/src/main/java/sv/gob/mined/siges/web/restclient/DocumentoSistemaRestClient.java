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
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.dto.SgDocumentoSistema;
import sv.gob.mined.siges.client.excepciones.HttpClientException;
import sv.gob.mined.siges.client.excepciones.HttpServerException;
import sv.gob.mined.siges.client.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDocumentoSistema;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class}, delay = 250)
@Timeout(value = 6000L)
public class DocumentoSistemaRestClient implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DocumentoSistemaRestClient.class.getName());

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

    public DocumentoSistemaRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgDocumentoSistema> buscar(FiltroDocumentoSistema filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/documentossistemas/buscar");
        SgDocumentoSistema[] documentoSistema = restClient.invokePost(webTarget, filtro, SgDocumentoSistema[].class);
        return new ArrayList<>(Arrays.asList(documentoSistema));
    }    

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroDocumentoSistema filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/documentossistemas/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgDocumentoSistema guardar(SgDocumentoSistema documentoSistema) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (documentoSistema == null) {
            return null;
        }
        if (documentoSistema.getDsiPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/documentossistemas");
            return restClient.invokePost(webTarget, documentoSistema, SgDocumentoSistema.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/documentossistemas");
            webTarget = webTarget.path(documentoSistema.getDsiPk().toString());
            return restClient.invokePut(webTarget, documentoSistema, SgDocumentoSistema.class);
        }
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgDocumentoSistema obtenerPorId(Long documentoSistemaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (documentoSistemaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/documentossistemas");
        webTarget = webTarget.path(documentoSistemaPk.toString());
        return restClient.invokeGet(webTarget, SgDocumentoSistema.class);
    }

    public void eliminar(Long documentoSistemaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (documentoSistemaPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/documentossistemas");
        webTarget = webTarget.path(documentoSistemaPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long documentoSistemaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (documentoSistemaPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/documentossistemas/historial");
        webTarget = webTarget.path(documentoSistemaPk.toString());
        RevHistorico[] documentoSistema = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(documentoSistema);
    }

    public SgDocumentoSistema obtenerEnRevision(Long documentoSistemaPk, Long documentoSistemaRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (documentoSistemaPk == null || documentoSistemaRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SISTEMAS_INTEGRADOS, "v1/documentossistemas/revision");
        webTarget = webTarget.path(documentoSistemaPk.toString());
        webTarget = webTarget.path(documentoSistemaRev.toString());
        return restClient.invokeGet(webTarget, SgDocumentoSistema.class);
    }

}
