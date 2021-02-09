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
import sv.gob.mined.siges.web.dto.catalogo.SgAyuda;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.web.dto.catalogo.SgGlosario;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgNacionalidad;
import sv.gob.mined.siges.web.dto.catalogo.SgPais;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;

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

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
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

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
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

    @CacheResult(cacheName = Constantes.AYUDA_CACHE)
    public List<SgAyuda> buscarAyudas(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas/buscar");
        SgAyuda[] ayudas = restClient.invokePost(webTarget, filtro, SgAyuda[].class);
        return Arrays.asList(ayudas);
    }

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

    @CacheResult(cacheName = Constantes.NACIONALIDAD_CACHE)
    public List<SgNacionalidad> buscarNacionalidad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nacionalidades/buscar");
        SgNacionalidad[] nacionalidades = restClient.invokePost(webTarget, filtro, SgNacionalidad[].class);
        return Arrays.asList(nacionalidades);
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

}
