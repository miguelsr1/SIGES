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
import sv.gob.mined.siges.web.dto.SgConfiguracion;
import sv.gob.mined.siges.web.dto.SgTipoServicioSanitario;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgAyuda;
import sv.gob.mined.siges.web.dto.catalogo.SgCanton;
import sv.gob.mined.siges.web.dto.catalogo.SgCaserio;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgEspacioComun;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoInmueble;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoProcesoLegalizacion;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgGlosario;
import sv.gob.mined.siges.web.dto.catalogo.SgInfAccesibilidad;
import sv.gob.mined.siges.web.dto.catalogo.SgInfItemObraExterior;
import sv.gob.mined.siges.web.dto.catalogo.SgInfMinisterioOtorgante;
import sv.gob.mined.siges.web.dto.catalogo.SgInfNaturalezaContrato;
import sv.gob.mined.siges.web.dto.catalogo.SgInfObraExterior;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoAbastecimiento;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoDocumento;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoImagen;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoManejoDesechos;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipologiaConstruccion;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTratamientoAgua;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgPropietariosTerreno;
import sv.gob.mined.siges.web.dto.catalogo.SgServicioBasico;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoCalle;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoConstruccion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoMejora;
import sv.gob.mined.siges.web.dto.catalogo.SgZona;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtros.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCanton;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCaserio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEspacioComplementario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstadoProcesoLegalizacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioBasico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTipoMejora;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTipoServicioSanitario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;

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

    @CacheResult(cacheName = Constantes.GLOSARIOS)
    public List<SgGlosario> buscarGlosarios(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/glosarios/buscar");
        SgGlosario[] glosarios = restClient.invokePost(webTarget, filtro, SgGlosario[].class);
        return Arrays.asList(glosarios);
    }

    @CacheResult(cacheName = Constantes.GLOSARIOS)
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

    @CacheResult(cacheName = Constantes.TIPOS_CALLE)
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

    @CacheResult(cacheName = Constantes.ESTADO_INMUEBLE_CACHE)
    public List<SgEstadoInmueble> buscarEstadoInmueble(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosinmuebles/buscar");
        SgEstadoInmueble[] estadosInmuebles = restClient.invokePost(webTarget, filtro, SgEstadoInmueble[].class);
        return Arrays.asList(estadosInmuebles);
    }

    @CacheResult(cacheName = Constantes.SERVICIO_BASICO_CACHE)
    public List<SgServicioBasico> buscarServicioBasico(FiltroServicioBasico filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/serviciosbasicos/buscar");
        SgServicioBasico[] serviciosBasicos = restClient.invokePost(webTarget, filtro, SgServicioBasico[].class);
        return Arrays.asList(serviciosBasicos);
    }

    @CacheResult(cacheName = Constantes.ESPACIO_COMUN_CACHE)
    public List<SgEspacioComun> buscarEspacioComun(FiltroEspacioComplementario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/espaciocomun/buscar");
        SgEspacioComun[] espacioComun = restClient.invokePost(webTarget, filtro, SgEspacioComun[].class);
        return Arrays.asList(espacioComun);
    }

    @CacheResult(cacheName = Constantes.PROPIETARIOS_TERRENO_CACHE)
    public List<SgPropietariosTerreno> buscarPropietariosTerreno(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/propietarioterreno/buscar");
        SgPropietariosTerreno[] propietarioTerreno = restClient.invokePost(webTarget, filtro, SgPropietariosTerreno[].class);
        return Arrays.asList(propietarioTerreno);
    }

    @CacheResult(cacheName = Constantes.FUENTE_FINANCIAMIENTO_CACHE)
    public List<SgFuenteFinanciamiento> buscarFuenteFinanciamiento(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento/buscar");
        SgFuenteFinanciamiento[] fuenteFinanciamiento = restClient.invokePost(webTarget, filtro, SgFuenteFinanciamiento[].class);
        return Arrays.asList(fuenteFinanciamiento);
    }

    @CacheResult(cacheName = Constantes.TIPO_CONSTRUCCION_CACHE)
    public List<SgTipoConstruccion> buscarTipoConstruccion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoconstruccion/buscar");
        SgTipoConstruccion[] tipoConstruccion = restClient.invokePost(webTarget, filtro, SgTipoConstruccion[].class);
        return Arrays.asList(tipoConstruccion);
    }

    @CacheResult(cacheName = Constantes.ESTADO_PROCESO_LEGALIZACION_CACHE)
    public List<SgEstadoProcesoLegalizacion> buscarEstadoProcesoLegalizacion(FiltroEstadoProcesoLegalizacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoprocesolegalizacion/buscar");
        SgEstadoProcesoLegalizacion[] estadoProcesoLegalizacion = restClient.invokePost(webTarget, filtro, SgEstadoProcesoLegalizacion[].class);
        return Arrays.asList(estadoProcesoLegalizacion);
    }

    @CacheResult(cacheName = Constantes.MINISTERIO_OTORGANTE_CACHE)
    public List<SgInfMinisterioOtorgante> buscarMinisterioOtorgante(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ministeriootorgante/buscar");
        SgInfMinisterioOtorgante[] ministerioOtorgante = restClient.invokePost(webTarget, filtro, SgInfMinisterioOtorgante[].class);
        return Arrays.asList(ministerioOtorgante);
    }

    @CacheResult(cacheName = Constantes.NATURALEZA_CONTRATO_CACHE)
    public List<SgInfNaturalezaContrato> buscarNaturalezaContrato(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/naturalezacontrato/buscar");
        SgInfNaturalezaContrato[] naturalezaContrato = restClient.invokePost(webTarget, filtro, SgInfNaturalezaContrato[].class);
        return Arrays.asList(naturalezaContrato);
    }

    @CacheResult(cacheName = Constantes.TIPO_SERVICIO_SANITARIO_CACHE)
    public List<SgTipoServicioSanitario> buscarTipoServicioSanitario(FiltroTipoServicioSanitario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposserviciosanitario/buscar");
        SgTipoServicioSanitario[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoServicioSanitario[].class);
        List<SgTipoServicioSanitario> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.TIPOLOGIA_CONSTRUCCION)
    public List<SgInfTipologiaConstruccion> buscarTiplogiaConstruccion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipologiaconstruccion/buscar");
        SgInfTipologiaConstruccion[] tipologiaConstruccion = restClient.invokePost(webTarget, filtro, SgInfTipologiaConstruccion[].class);
        return Arrays.asList(tipologiaConstruccion);
    }

    @CacheResult(cacheName = Constantes.TIPO_IMAGEN)
    public List<SgInfTipoImagen> buscarTipoImagen(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoimagen/buscar");
        SgInfTipoImagen[] tipoImagen = restClient.invokePost(webTarget, filtro, SgInfTipoImagen[].class);
        return Arrays.asList(tipoImagen);
    }

    @CacheResult(cacheName = Constantes.TIPO_DOCUMENTO)
    public List<SgInfTipoDocumento> buscarTipoDocumento(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipodocumento/buscar");
        SgInfTipoDocumento[] tipoDocumento = restClient.invokePost(webTarget, filtro, SgInfTipoDocumento[].class);
        return Arrays.asList(tipoDocumento);
    }

    @CacheResult(cacheName = Constantes.TIPO_CONFIGURACION)
    public List<SgConfiguracion> buscarConfiguracion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuraciones/buscar");
        SgConfiguracion[] tipoDocumento = restClient.invokePost(webTarget, filtro, SgConfiguracion[].class);
        return Arrays.asList(tipoDocumento);
    }

    public SgInfTipologiaConstruccion obtenerPorIdTipologiaConstruccion(Long infTipologiaConstruccionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipologiaConstruccionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipologiaconstruccion");
        webTarget = webTarget.path(infTipologiaConstruccionPk.toString());
        return restClient.invokeGet(webTarget, SgInfTipologiaConstruccion.class);
    }
    
    @CacheResult(cacheName = Constantes.TIPO_MEJORA)
    public List<SgTipoMejora> buscarTipoMejora(FiltroTipoMejora filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomejora/buscar");
        SgTipoMejora[] tipoMejora = restClient.invokePost(webTarget, filtro, SgTipoMejora[].class);
        return Arrays.asList(tipoMejora);
    }
    
    @CacheResult(cacheName = Constantes.ACCESIBILIDAD)
    public List<SgInfAccesibilidad> buscarAccesibilidad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/accesibilidad/buscar");
        SgInfAccesibilidad[] accesibilidad = restClient.invokePost(webTarget, filtro, SgInfAccesibilidad[].class);
        return Arrays.asList(accesibilidad);
    }
    
    @CacheResult(cacheName = Constantes.TIPO_ABASTECIMIENTO)
    public List<SgInfTipoAbastecimiento> buscarTipoAbastecimiento(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoabastecimiento/buscar");
        SgInfTipoAbastecimiento[] tipoAbastecimiento = restClient.invokePost(webTarget, filtro, SgInfTipoAbastecimiento[].class);
        return Arrays.asList(tipoAbastecimiento);
    }
    
    @CacheResult(cacheName = Constantes.MANEJO_DESECHOS)
    public List<SgInfTipoManejoDesechos> buscarManejoDeschos(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipomanejodesechos/buscar");
        SgInfTipoManejoDesechos[] tipoManejoDesechos = restClient.invokePost(webTarget, filtro, SgInfTipoManejoDesechos[].class);
        return Arrays.asList(tipoManejoDesechos);
    }
    
    @CacheResult(cacheName = Constantes.TRATAMIENTO_AGUA)
    public List<SgInfTratamientoAgua> buscarTratamientoAgua(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tratamientoagua/buscar");
        SgInfTratamientoAgua[] tratamientoAgua = restClient.invokePost(webTarget, filtro, SgInfTratamientoAgua[].class);
        return Arrays.asList(tratamientoAgua);
    }    

    public SgInfTipoAbastecimiento obtenerPorIdTipoAbastecimiento(Long infTipoAbastecimientoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (infTipoAbastecimientoPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoabastecimiento");
        webTarget = webTarget.path(infTipoAbastecimientoPk.toString());
        return restClient.invokeGet(webTarget, SgInfTipoAbastecimiento.class);
    }
    
    @CacheResult(cacheName = Constantes.ITEM_OBRA_EXTERIOR)
      public List<SgInfItemObraExterior> buscarItemObraExterior(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/itemobraexterior/buscar");
        SgInfItemObraExterior[] itemObraExterior = restClient.invokePost(webTarget, filtro, SgInfItemObraExterior[].class);
        return Arrays.asList(itemObraExterior);
    }
      
      @CacheResult(cacheName = Constantes.OBRA_EXTERIOR)
      public List<SgInfObraExterior> buscarObraExterior(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/obraexterior/buscar");
        SgInfObraExterior[] obraExterior = restClient.invokePost(webTarget, filtro, SgInfObraExterior[].class);
        return Arrays.asList(obraExterior);
    }
      
      @CacheResult(cacheName = Constantes.UNIDADES_AMINISTRATIVAS)
     public List<SgAfUnidadesAdministrativas> buscarUnidadesAdministrativas(FiltroUnidadesAdministrativas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAD/buscar");
        SgAfUnidadesAdministrativas[] unidades = restClient.invokePost(webTarget, filtro, SgAfUnidadesAdministrativas[].class);
        return Arrays.asList(unidades);
    }
     
     public SgAfUnidadesAdministrativas obtenerPorIdUnidadesAdministrativas(Long unidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (unidadPk == null ) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAD");
        webTarget = webTarget.path(unidadPk.toString());
        return restClient.invokeGet(webTarget, SgAfUnidadesAdministrativas.class);
    }

}
