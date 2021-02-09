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
import javax.ws.rs.client.Client;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.utils.ValidationUtils;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgIdentificacion;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.SgUnirPersona;
import sv.gob.mined.siges.web.dto.catalogo.SgTrastornoAprendizaje;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersona;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 15000L)
public class PersonaRestClient implements Serializable {

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

    public PersonaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPersona> buscar(FiltroPersona filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscar");
        SgPersona[] sede = restClient.invokePost(webTarget, filtro, SgPersona[].class);
        return new ArrayList<>(Arrays.asList(sede));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroPersona filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPersona> buscarLucene(FiltroPersona filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscarlucene");
        SgPersona[] sede = restClient.invokePost(webTarget, filtro, SgPersona[].class);
        return new ArrayList<>(Arrays.asList(sede));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotalLucene(FiltroPersona filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscarlucene/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgPersona obtenerPorId(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas");
        webTarget = webTarget.path(personaId.toString());
        return restClient.invokeGet(webTarget, SgPersona.class);
    }

    public SgPersona guardar(SgPersona persona) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (persona == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(persona);
        if (persona.getPerPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas");
            return restClient.invokePost(webTarget, persona, SgPersona.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas");
            webTarget = webTarget.path(persona.getPerPk().toString());
            return restClient.invokePut(webTarget, persona, SgPersona.class);
        }
    }

    public Boolean validar(SgPersona persona) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (persona == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/validar");
        return restClient.invokePost(webTarget, persona, Boolean.class);
    }

    public Boolean validarIdentificaciones(SgPersona persona) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (persona == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/validaridentificaciones");
        return restClient.invokePost(webTarget, persona, Boolean.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 20000L)
    public void eliminar(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas");
        webTarget = webTarget.path(personaId.toString());
        restClient.invokeDelete(webTarget);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<RevHistorico> obtenerHistorialPorId(Long personaId) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaId == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/historial");
        webTarget = webTarget.path(personaId.toString());
        RevHistorico[] sede = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(sede);
    }

    @Timeout(value = 20000L)
    public SgPersona obtenerEnRevision(Long personaPk, Long personaRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaPk == null || personaRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/revision");
        webTarget = webTarget.path(personaPk.toString());
        webTarget = webTarget.path(personaRev.toString());
        return restClient.invokeGet(webTarget, SgPersona.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPersona> obtenerPersonasPorIdentificacion(SgPersona p) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        FiltroPersona fp = new FiltroPersona();
        fp.setMaxResults(2L);
        //Cortar referencia circular
        if (p.getPerPk() == null && p.getPerIdentificaciones() != null) {
            for (SgIdentificacion i : p.getPerIdentificaciones()) {
                i.setIdePersona(null);
            }
        }

        fp.setNie(p.getPerNie());
        fp.setCun(p.getPerCun());
        fp.setDui(p.getPerDui());

        if (!StringUtils.isBlank(p.getPerDui())) {
            if (!ValidationUtils.validarDUI(p.getPerDui())) {
                BusinessException be = new BusinessException();
                be.addError("perDui", Mensajes.ERROR_DUI_INCORRECTO);
                throw be;
            }
        }

        fp.setNit(p.getPerNit());
        fp.setNip(p.getPerNip());
        fp.setNup(p.getPerNup());
        fp.setIsss(p.getPerIsss());
        fp.setInpep(p.getPerInpep());
        fp.setOtrasIden(p.getPerIdentificaciones());
        fp.setIncluirCampos(new String[]{"perVersion"});

        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscar");
        SgPersona[] personas = restClient.invokePost(webTarget, fp, SgPersona[].class);
        return new ArrayList<>(Arrays.asList(personas));
    }

    public SgPersona obtenerPersonaDesdeRNPNPorDUI(String dui) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/rnpn/buscardui");
        webTarget = webTarget.queryParam("dui", dui);
        return restClient.invokeGet(webTarget, SgPersona.class);
    }

    public SgPersona obtenerPersonaDesdeRNPNPorCUN(Long cun) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/rnpn/buscarcun");
        webTarget = webTarget.queryParam("cun", cun);
        return restClient.invokeGet(webTarget, SgPersona.class);
    }

    public SgPersona validarPersonaRNPNPorDUI(Long perPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/rnpn/validardui");
        webTarget = webTarget.path(perPk.toString());
        return restClient.invokePost(webTarget, null, null);
    }

    public SgPersona validarPersonaRNPNPorCUN(Long perPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/rnpn/validarcun");
        webTarget = webTarget.path(perPk.toString());
        return restClient.invokePost(webTarget, null, null);
    }

    public SgPersona validarPersonaRNPNPorPartidaNacimiento(Long perPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/rnpn/validarpartidanac");
        webTarget = webTarget.path(perPk.toString());
        return restClient.invokePost(webTarget, null, null);
    }

    public List<SgTrastornoAprendizaje> obtenerTrastornos(Long personaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (personaPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/buscarTrastornos");
        SgTrastornoAprendizaje[] sede = restClient.invokePost(webTarget, personaPk, SgTrastornoAprendizaje[].class);
        return new ArrayList<>(Arrays.asList(sede));
    }
    
    @Timeout(value = 50000L)
    public void unirPersona(SgUnirPersona dto) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (dto == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/personas/unir");
        restClient.invokePost(webTarget, dto, null);
    }

}
