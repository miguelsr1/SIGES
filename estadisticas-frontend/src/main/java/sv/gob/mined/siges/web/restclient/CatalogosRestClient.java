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
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgEstCategoriaIndicador;
import sv.gob.mined.siges.web.dto.catalogo.SgEstDatasets;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.web.dto.catalogo.SgGlosario;
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
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 3000L)
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
    

    @CacheResult(cacheName = Constantes.AYUDA_CACHE)
    public List<SgAyuda> buscarAyudas(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas/buscar");
        SgAyuda[] ayudas = restClient.invokePost(webTarget, filtro, SgAyuda[].class);
        return Arrays.asList(ayudas);
    }
    
    @CacheResult(cacheName = Constantes.CATEGORIA_INDICADOR_CACHE)
    public List<SgEstCategoriaIndicador> buscarCategoriaIndicador(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "/v1/categoriasindicador/buscar");
        SgEstCategoriaIndicador[] catInd = restClient.invokePost(webTarget, filtro, SgEstCategoriaIndicador[].class);
        return Arrays.asList(catInd);
    }
    
    @CacheResult(cacheName = Constantes.NOMBRE_EXTRACCION_CACHE)
    public List<SgEstNombreExtraccion> buscarNombresExtraccion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nombresextracciones/buscar");
        SgEstNombreExtraccion[] nombres = restClient.invokePost(webTarget, filtro, SgEstNombreExtraccion[].class);
        return Arrays.asList(nombres);
    }
    
    @CacheResult(cacheName = Constantes.DATASET_CACHE)
    public List<SgEstDatasets> buscarDatasets(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/datasets/buscar");
        SgEstDatasets[] datasets = restClient.invokePost(webTarget, filtro, SgEstDatasets[].class);
        return Arrays.asList(datasets);
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
    
    
    
}
