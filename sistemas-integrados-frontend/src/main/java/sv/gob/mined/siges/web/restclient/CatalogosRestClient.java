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
import sv.gob.mined.siges.client.excepciones.HttpClientException;
import sv.gob.mined.siges.client.excepciones.HttpServerException;
import sv.gob.mined.siges.client.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.centros_educativos.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgAyuda;
import sv.gob.mined.siges.web.dto.catalogo.SgCanton;
import sv.gob.mined.siges.web.dto.catalogo.SgCaserio;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgGlosario;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgSiPromotor;
import sv.gob.mined.siges.web.dto.catalogo.SgSiTipoDocumento;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoAcuerdo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoCalle;
import sv.gob.mined.siges.web.dto.catalogo.SgZona;
import sv.gob.mined.siges.web.dto.centros_educativos.SgProgramaEducativo;
import sv.gob.mined.siges.web.filtros.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCanton;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCaserio;
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
@Timeout(value = 5000L)
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

    public List<SgTipoCalle> buscarTiposCalle(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcalle/buscar");
        SgTipoCalle[] carrera = restClient.invokePost(webTarget, filtro, SgTipoCalle[].class);
        return Arrays.asList(carrera);
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

    public List<SgConfiguracion> buscarConfiguracion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuraciones/buscar");
        SgConfiguracion[] tipoDocumento = restClient.invokePost(webTarget, filtro, SgConfiguracion[].class);
        return Arrays.asList(tipoDocumento);
    }

    @CacheResult(cacheName = Constantes.TIPO_DOCUMENTO_CACHE)
    public List<SgSiTipoDocumento> buscarTipoDocumento(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sitipodocumento/buscar");
        SgSiTipoDocumento[] tipoDocumento = restClient.invokePost(webTarget, filtro, SgSiTipoDocumento[].class);
        return Arrays.asList(tipoDocumento);
    }

    @CacheResult(cacheName = Constantes.TIPO_ACUERDO_CACHE)
    public List<SgTipoAcuerdo> buscarTipoAcuerdo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposacuerdos/buscar");
        SgTipoAcuerdo[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoAcuerdo[].class);
        List<SgTipoAcuerdo> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.PROMOTOR_CACHE)
    public List<SgSiPromotor> buscarPromotor(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/promotores/buscar");
        SgSiPromotor[] codiguera = restClient.invokePost(webTarget, filtro, SgSiPromotor[].class);
        List<SgSiPromotor> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.PROGRAMA_EDUCATIVO_CACHE)
    public List<SgProgramaEducativo> buscarProgramasEducativos(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programaseducativos/buscar");
        SgProgramaEducativo[] programasEducativos = restClient.invokePost(webTarget, filtro, SgProgramaEducativo[].class);
        return Arrays.asList(programasEducativos);
    }

}
