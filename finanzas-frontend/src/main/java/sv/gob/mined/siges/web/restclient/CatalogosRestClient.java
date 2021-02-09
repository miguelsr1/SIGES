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
import javax.cache.annotation.CacheResult;
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
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgGlosario;
import sv.gob.mined.siges.web.dto.SgItemLiquidacion;
import sv.gob.mined.siges.web.dto.catalogo.SgAyuda;
import sv.gob.mined.siges.web.dto.catalogo.SgCanton;
import sv.gob.mined.siges.web.dto.catalogo.SgCaserio;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgPais;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.dto.catalogo.SgZona;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCanton;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCaserio;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoCalle;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
@Timeout(value = 10000L)
public class CatalogosRestClient implements Serializable {

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

    public CatalogosRestClient() {
    }

    @CacheResult(cacheName = Constantes.SEXO_CACHE)
    public List<SgSexo> buscarSexo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sexos/buscar");
        SgSexo[] codiguera = restClient.invokePost(webTarget, filtro, SgSexo[].class);
        List<SgSexo> se = Arrays.asList(codiguera);
        return se;
    }


    @CacheResult(cacheName = Constantes.ESTADO_CIVIL_CACHE)
    public List<SgEstadoCivil> buscarEstadoCivil(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoscivil/buscar");
        SgEstadoCivil[] codiguera = restClient.invokePost(webTarget, filtro, SgEstadoCivil[].class);
        List<SgEstadoCivil> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.PAIS_CACHE)
    public List<SgPais> buscarPais(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/paises/buscar");
        SgPais[] codiguera = restClient.invokePost(webTarget, filtro, SgPais[].class);
        List<SgPais> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.DEPARTAMENTO_CACHE)
    public List<SgDepartamento> buscarDepartamento(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos/buscar");
        SgDepartamento[] codiguera = restClient.invokePost(webTarget, filtro, SgDepartamento[].class);
        List<SgDepartamento> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.MUNICIPIO_CACHE)
    public List<SgMunicipio> buscarMunicipio(FiltroMunicipio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios/buscar");
        SgMunicipio[] codiguera = restClient.invokePost(webTarget, filtro, SgMunicipio[].class);
        List<SgMunicipio> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.CANTON_CACHE)
    public List<SgCanton> buscarCanton(FiltroCanton filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones/buscar");
        SgCanton[] codiguera = restClient.invokePost(webTarget, filtro, SgCanton[].class);
        List<SgCanton> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.CASERIO_CACHE)
    public List<SgCaserio> buscarCaserio(FiltroCaserio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/caserios/buscar");
        SgCaserio[] codiguera = restClient.invokePost(webTarget, filtro, SgCaserio[].class);
        List<SgCaserio> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.TIPO_CALLE_CACHE)
    public List<SgTipoCalle> buscarTiposCalle(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcalle/buscar");
        SgTipoCalle[] carrera = restClient.invokePost(webTarget, filtro, SgTipoCalle[].class);
        return Arrays.asList(carrera);
    }

    @CacheResult(cacheName = Constantes.ZONA_CACHE)
    public List<SgZona> buscarZona(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas/buscar");
        SgZona[] codiguera = restClient.invokePost(webTarget, filtro, SgZona[].class);
        List<SgZona> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.CONFIGURACION_CACHE)
    public List<SgConfiguracion> buscarConfiguracion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuraciones/buscar");
        SgConfiguracion[] codiguera = restClient.invokePost(webTarget, filtro, SgConfiguracion[].class);
        List<SgConfiguracion> list = Arrays.asList(codiguera);
        return list;
    }

    public SgConfiguracion buscarConfiguracionPorCodigo(String codigo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigo == null) {
            return null;
        }
        FiltroCodiguera filtro = new FiltroCodiguera();
        filtro.setCodigoExacto(codigo);
        List<SgConfiguracion> configs = this.buscarConfiguracion(filtro);
        if (!configs.isEmpty()) {
            return configs.get(0);
        }
        return null;
    }

    @CacheResult(cacheName = Constantes.AYUDA_CACHE)
    public List<SgAyuda> buscarAyudas(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas/buscar");
        SgAyuda[] ayudas = restClient.invokePost(webTarget, filtro, SgAyuda[].class);
        return Arrays.asList(ayudas);
    }

    @CacheResult(cacheName = Constantes.GLOSARIOS_CACHE)
    public List<SgGlosario> buscarGlosarios(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/glosarios/buscar");
        SgGlosario[] glosarios = restClient.invokePost(webTarget, filtro, SgGlosario[].class);
        return Arrays.asList(glosarios);
    }

    public Long buscarGlosariosTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/glosarios/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }


    public SgSexo obtenerPorIdSexo(Long sexoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (sexoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sexos");
        webTarget = webTarget.path(sexoPk.toString());
        return restClient.invokeGet(webTarget, SgSexo.class);
    }

    public SgDepartamento obtenerPorIdDepartamento(Long Pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (Pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos");
        webTarget = webTarget.path(Pk.toString());
        return restClient.invokeGet(webTarget, SgDepartamento.class);
    }

    public SgMunicipio obtenerPorIdMunicipio(Long municipioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (municipioPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios");
        webTarget = webTarget.path(municipioPk.toString());
        return restClient.invokeGet(webTarget, SgMunicipio.class);
    }
    
    
    public List<SgItemLiquidacion> buscarItemsLiquidacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemsliquidacion/buscar");
        SgItemLiquidacion[] codiguera = restClient.invokePost(webTarget, filtro, SgItemLiquidacion[].class);
        List<SgItemLiquidacion> lista = Arrays.asList(codiguera);
        return lista;
    }
    
}
