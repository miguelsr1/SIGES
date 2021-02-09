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
import sv.gob.mined.siges.web.dto.catalogo.SgAfCategoriaBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfClasificacionBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEmpleados;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosCalidad;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosDescargo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosInventario;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosTraslado;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFormaAdquisicion;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgAfOpcionesDescargo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfProyectos;
import sv.gob.mined.siges.web.dto.catalogo.SgAyuda;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgGlosario;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaBienes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEmpleados;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFuenteFinanciamientoAF;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyecto;

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

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
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
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
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
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    @CacheResult(cacheName = Constantes.FUENTE_FINANCIAMIENTO_AF_CACHE)
    public List<SgAfFuenteFinanciamiento> buscarFuenteFinanciamiento(FiltroFuenteFinanciamientoAF filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamientoAF/buscar");
        SgAfFuenteFinanciamiento[] codiguera = restClient.invokePost(webTarget, filtro, SgAfFuenteFinanciamiento[].class);
        List<SgAfFuenteFinanciamiento> list = Arrays.asList(codiguera);
        return list;
    }
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    @CacheResult(cacheName = Constantes.ESTADOS_TRASLADO_CACHE)
    public List<SgAfEstadosTraslado> buscarEstadosTraslado(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadostraslado/buscar");
        SgAfEstadosTraslado[] ciclos = restClient.invokePost(webTarget, filtro, SgAfEstadosTraslado[].class);
        return Arrays.asList(ciclos);
    }
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    @CacheResult(cacheName = Constantes.ESTADOS_INVENTARIO_CACHE)
    public List<SgAfEstadosInventario> buscarEstadosInventario(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosinventario/buscar");
        SgAfEstadosInventario[] ciclos = restClient.invokePost(webTarget, filtro, SgAfEstadosInventario[].class);
        return Arrays.asList(ciclos);
    }
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    @CacheResult(cacheName = Constantes.ESTADOS_DESCARGO_CACHE)
    public List<SgAfEstadosDescargo> buscarEstadosDescargo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosdescargo/buscar");
        SgAfEstadosDescargo[] ciclos = restClient.invokePost(webTarget, filtro, SgAfEstadosDescargo[].class);
        return Arrays.asList(ciclos);
    }
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    @CacheResult(cacheName = Constantes.OPCIONES_DESCARGO_CACHE)
    public List<SgAfOpcionesDescargo> buscarOpcionesDescargo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/opcionesdescargo/buscar");
        SgAfOpcionesDescargo[] opcionesDescargo = restClient.invokePost(webTarget, filtro, SgAfOpcionesDescargo[].class);
        return Arrays.asList(opcionesDescargo);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    @CacheResult(cacheName = Constantes.ESTADOS_CALIDAD_CACHE)
    public List<SgAfEstadosCalidad> buscarEstadosCalidad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoscalidad/buscar");
        SgAfEstadosCalidad[] ciclos = restClient.invokePost(webTarget, filtro, SgAfEstadosCalidad[].class);
        return Arrays.asList(ciclos);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    @CacheResult(cacheName = Constantes.ESTADOS_BIENES_CACHE)
    public List<SgAfEstadosBienes> buscarEstadosBienes(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosbienes/buscar");
        SgAfEstadosBienes[] ciclos = restClient.invokePost(webTarget, filtro, SgAfEstadosBienes[].class);
        return Arrays.asList(ciclos);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    @CacheResult(cacheName = Constantes.CATEGORIA_BIENES_CACHE)
    public List<SgAfCategoriaBienes> buscarCategoriaBienes(FiltroCategoriaBienes filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriabienes/buscar");
        SgAfCategoriaBienes[] categorias = restClient.invokePost(webTarget, filtro, SgAfCategoriaBienes[].class);
        return Arrays.asList(categorias);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    @CacheResult(cacheName = Constantes.CLASIFICACION_BIENES_CACHE)
    public List<SgAfClasificacionBienes> buscarClasificacionBienes(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificacionbienes/buscar");
        SgAfClasificacionBienes[] clasificaciones = restClient.invokePost(webTarget, filtro, SgAfClasificacionBienes[].class);
        return Arrays.asList(clasificaciones);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    @CacheResult(cacheName = Constantes.PROYECTOS_CACHE)
    public List<SgAfProyectos> buscarProyecto(FiltroProyecto filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectos/buscar");
        SgAfProyectos[] proyectos = restClient.invokePost(webTarget, filtro, SgAfProyectos[].class);
        return Arrays.asList(proyectos);
    }
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    @CacheResult(cacheName = Constantes.ESTADOS_FORMA_ADQUISICION_CACHE)
    public List<SgAfFormaAdquisicion> buscarFormaAdquisicion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formaadquisicion/buscar");
        SgAfFormaAdquisicion[] formaAdquisicion = restClient.invokePost(webTarget, filtro, SgAfFormaAdquisicion[].class);
        return Arrays.asList(formaAdquisicion);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    //@CacheResult(cacheName = Constantes.EMPLEADOS_CACHE)
    public List<SgAfEmpleados> buscarEmpleados(FiltroEmpleados filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        /**
        if (!StringUtils.isBlank(filtro.getDui())) {
            if (!ValidationUtils.validarDUI(filtro.getDui())) {
                BusinessException be = new BusinessException();
                be.addError("perDui", Mensajes.ERROR_DUI_INCORRECTO);
                throw be;
            }
        }**/
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_ACTIVO_FIJO, "v1/empleados/buscar");
        SgAfEmpleados[] empleados = restClient.invokePost(webTarget, filtro, SgAfEmpleados[].class);
        return Arrays.asList(empleados);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgDepartamento obtenerPorIdDepartamento(Long Pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (Pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos");
        webTarget = webTarget.path(Pk.toString());
        return restClient.invokeGet(webTarget, SgDepartamento.class);
    }
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgMunicipio obtenerPorIdMunicipio(Long municipioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (municipioPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios");
        webTarget = webTarget.path(municipioPk.toString());
        return restClient.invokeGet(webTarget, SgMunicipio.class);
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
    
    
    
}
