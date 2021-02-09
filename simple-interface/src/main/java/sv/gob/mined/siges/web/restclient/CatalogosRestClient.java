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
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgCanton;
import sv.gob.mined.siges.web.dto.SgConfiguracion;
import sv.gob.mined.siges.web.dto.SgDepartamento;
import sv.gob.mined.siges.web.dto.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.SgMotivoLicencia;
import sv.gob.mined.siges.web.dto.SgMotivoPermuta;
import sv.gob.mined.siges.web.dto.SgMunicipio;
import sv.gob.mined.siges.web.dto.SgRecursoEducativo;
import sv.gob.mined.siges.web.dto.SgTipoCalendario;
import sv.gob.mined.siges.web.dto.SgZona;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCanton;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;


/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 8000L)
public class CatalogosRestClient implements Serializable {
    
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


    public CatalogosRestClient() {
    }

    @CacheResult(cacheName = Constantes.DEPARTAMENTO_CACHE)
    public List<SgDepartamento> buscarDepartamento(@CacheKey FiltroCodiguera filtro, String userToken) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos/buscar");
        SgDepartamento[] codiguera = RestClient.invokePost(webTarget, filtro, SgDepartamento[].class, userToken);
        List<SgDepartamento> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.MUNICIPIO_CACHE)
    public List<SgMunicipio> buscarMunicipio(@CacheKey FiltroMunicipio filtro, String userToken) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios/buscar");
        SgMunicipio[] codiguera = RestClient.invokePost(webTarget, filtro, SgMunicipio[].class, userToken);
        List<SgMunicipio> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.CANTON_CACHE)
    public List<SgCanton> buscarCanton(@CacheKey FiltroCanton filtro, String userToken) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones/buscar");
        SgCanton[] codiguera = RestClient.invokePost(webTarget, filtro, SgCanton[].class, userToken);
        List<SgCanton> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.ZONA_CACHE)
    public List<SgZona> buscarZona(@CacheKey FiltroCodiguera filtro, String userToken) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas/buscar");
        SgZona[] codiguera = RestClient.invokePost(webTarget, filtro, SgZona[].class, userToken);
        List<SgZona> lista = Arrays.asList(codiguera);
        return lista;
    }

   @CacheResult(cacheName = Constantes.JORNADA_LABORAL_CACHE)
    public List<SgJornadaLaboral> buscarJornadasLaborales(@CacheKey FiltroCodiguera filtro, String userToken) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/jornadaslaborales/buscar");
        SgJornadaLaboral[] codiguera = RestClient.invokePost(webTarget, filtro, SgJornadaLaboral[].class, userToken);
        List<SgJornadaLaboral> list = Arrays.asList(codiguera);
        return list;
    }
    
    @CacheResult(cacheName = Constantes.RECURSO_EDUCATIVO_CACHE)
    public List<SgRecursoEducativo> buscarRecursosEducativos(@CacheKey FiltroCodiguera filtro, String userToken) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/recursoseducativos/buscar");
        SgRecursoEducativo[] recursoEducativos = RestClient.invokePost(webTarget, filtro, SgRecursoEducativo[].class, userToken);
        return Arrays.asList(recursoEducativos);
    }
    
    @CacheResult(cacheName = Constantes.MOTIVO_LICENCIA_CACHE)
    public List<SgMotivoLicencia> buscarMotivosLicencia(@CacheKey FiltroCodiguera filtro, String userToken) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoslicencia/buscar");
        SgMotivoLicencia[] motivoLicencia = RestClient.invokePost(webTarget, filtro, SgMotivoLicencia[].class, userToken);
        return Arrays.asList(motivoLicencia);
    }
    
    @CacheResult(cacheName = Constantes.MOTIVO_PERMUTA_CACHE)
    public List<SgMotivoPermuta> buscarMotivosPermuta(@CacheKey FiltroCodiguera filtro, String userToken) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivospermuta/buscar");
        SgMotivoPermuta[] motivoPermuta = RestClient.invokePost(webTarget, filtro, SgMotivoPermuta[].class, userToken);
        return Arrays.asList(motivoPermuta);
    }
    
    
    @CacheResult(cacheName = Constantes.CONFIGURACION_CACHE)
    public List<SgConfiguracion> buscarConfiguraciones(@CacheKey FiltroCodiguera filtro, String userToken) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuraciones/buscar");
        SgConfiguracion[] configuraciones = RestClient.invokePost(webTarget, filtro, SgConfiguracion[].class, userToken);
        return Arrays.asList(configuraciones);
    }
    
    @CacheResult(cacheName = Constantes.TIPO_CALENDARIO_CACHE)
    public List<SgTipoCalendario> buscar(@CacheKey FiltroCodiguera filtro, String userToken) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcalendarioescolar/buscar");
        SgTipoCalendario[] tiposCalendario = RestClient.invokePost(webTarget, filtro, SgTipoCalendario[].class, userToken);
        return Arrays.asList(tiposCalendario);
    }
    
   

}
