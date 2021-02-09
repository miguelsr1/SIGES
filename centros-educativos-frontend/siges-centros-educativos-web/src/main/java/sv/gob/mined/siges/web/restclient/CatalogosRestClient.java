/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import java.io.Serializable;
import java.time.LocalDate;
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
import sv.gob.mined.siges.web.dto.SgNacionalidad;
import sv.gob.mined.siges.web.dto.SgTipoCalendario;
import sv.gob.mined.siges.web.dto.catalogo.SgTrastornoAprendizaje;
import sv.gob.mined.siges.web.dto.catalogo.SgAccionSolicitudTraslado;
import sv.gob.mined.siges.web.dto.catalogo.SgAfp;
import sv.gob.mined.siges.web.dto.catalogo.SgAlcanceCapacitacion;
import sv.gob.mined.siges.web.dto.catalogo.SgAreaAsignaturaModulo;
import sv.gob.mined.siges.web.dto.catalogo.SgAreaIndicador;
import sv.gob.mined.siges.web.dto.catalogo.SgAsociacion;
import sv.gob.mined.siges.web.dto.catalogo.SgAyuda;
import sv.gob.mined.siges.web.dto.catalogo.SgCalidadIngresoAplicantes;
import sv.gob.mined.siges.web.dto.catalogo.SgCalificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgCanton;
import sv.gob.mined.siges.web.dto.catalogo.SgCargaArchivo;
import sv.gob.mined.siges.web.dto.catalogo.SgCargo;
import sv.gob.mined.siges.web.dto.catalogo.SgCargoOAE;
import sv.gob.mined.siges.web.dto.catalogo.SgCargoTitular;
import sv.gob.mined.siges.web.dto.catalogo.SgCarrera;
import sv.gob.mined.siges.web.dto.catalogo.SgCaserio;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaCurso;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaEmpleado;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaFormacionDocente;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaViolencia;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgClasificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgCompaniaTelecomunicacion;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracionFirmaElectronica;
import sv.gob.mined.siges.web.dto.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgDependenciaEconomica;
import sv.gob.mined.siges.web.dto.catalogo.SgDiscapacidad;
import sv.gob.mined.siges.web.dto.catalogo.SgElementoHogar;
import sv.gob.mined.siges.web.dto.catalogo.SgEnfermedadNoTransmisible;
import sv.gob.mined.siges.web.dto.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgEscolaridad;
import sv.gob.mined.siges.web.dto.catalogo.SgEspacioComun;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoDatoContratacion;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoInmueble;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoProcesoLegalizacion;
import sv.gob.mined.siges.web.dto.catalogo.SgEtnia;
import sv.gob.mined.siges.web.dto.catalogo.SgFormula;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteAbastecimientoAgua;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgGradoAfectacion;
import sv.gob.mined.siges.web.dto.catalogo.SgIdioma;
import sv.gob.mined.siges.web.dto.catalogo.SgInstitucionPagaSalario;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.catalogo.SgLeySalario;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadEstudio;
import sv.gob.mined.siges.web.dto.catalogo.SgMedioTransporte;
import sv.gob.mined.siges.web.dto.catalogo.SgModoPago;
import sv.gob.mined.siges.web.dto.catalogo.SgModuloFormacionDocente;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoInasistencia;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoInasistenciaPersonal;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoRetiro;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoTraslado;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgNivelEmpleado;
import sv.gob.mined.siges.web.dto.catalogo.SgNivelFormacionDocente;
import sv.gob.mined.siges.web.dto.catalogo.SgNivelIdioma;
import sv.gob.mined.siges.web.dto.catalogo.SgOcupacion;
import sv.gob.mined.siges.web.dto.catalogo.SgPais;
import sv.gob.mined.siges.web.dto.catalogo.SgProfesion;
import sv.gob.mined.siges.web.dto.catalogo.SgRecurso;
import sv.gob.mined.siges.web.dto.catalogo.SgSectorEconomico;
import sv.gob.mined.siges.web.dto.catalogo.SgServicioInfraestructura;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.dto.catalogo.SgSistemaGestionContenido;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoCalle;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoCapacitacion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoContrato;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoEstudio;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoIdentificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoInstitucionPaga;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoManifestacion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoNombramiento;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoParentesco;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoRepresentante;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoSangre;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoServicioSanitario;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoTelefono;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoTrabajo;
import sv.gob.mined.siges.web.dto.catalogo.SgTiposRiesgo;
import sv.gob.mined.siges.web.dto.catalogo.SgZona;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEscalaCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCanton;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCaserio;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidadAtencion;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroAccion;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCalificacionCatalogo;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMotivoRetiro;
import sv.gob.mined.siges.web.dto.catalogo.SgGradoRiesgo;
import sv.gob.mined.siges.web.dto.catalogo.SgIdentificacionOAE;
import sv.gob.mined.siges.web.dto.catalogo.SgImplementadora;
import sv.gob.mined.siges.web.dto.catalogo.SgInfMinisterioOtorgante;
import sv.gob.mined.siges.web.dto.catalogo.SgInfNaturalezaContrato;
import sv.gob.mined.siges.web.dto.catalogo.SgMaterialPiso;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoFallecimiento;
import sv.gob.mined.siges.web.dto.catalogo.SgMaximoNivelEducativoAlcanzado;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoAnulacionTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoCierreSede;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoReimpresion;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivosSeleccionPLaza;
import sv.gob.mined.siges.web.dto.catalogo.SgOrganismoCoordinacionEscolar;
import sv.gob.mined.siges.web.dto.catalogo.SgPerfilesUsuariosIngresadosCe;
import sv.gob.mined.siges.web.dto.catalogo.SgPreguntaCierreAnio;
import sv.gob.mined.siges.web.dto.catalogo.SgPropietariosTerreno;
import sv.gob.mined.siges.web.dto.catalogo.SgProyectoInstitucional;
import sv.gob.mined.siges.web.dto.catalogo.SgReferenciasApoyo;
import sv.gob.mined.siges.web.dto.catalogo.SgServicioBasico;
import sv.gob.mined.siges.web.dto.catalogo.SgTerapia;
import sv.gob.mined.siges.web.dto.catalogo.SgTiemposComidaDia;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoAccion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoAcuerdo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoAlfabetizador;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoApoyo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoDocumentoDocente;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoModulo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoProveedor;
import sv.gob.mined.siges.web.dto.catalogo.SgTituloAnterior;
import sv.gob.mined.siges.web.dto.catalogo.SgVelocidadInternet;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEspacioComplementario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyectoInstitucional;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioBasico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTiposApoyo;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroAsociacion;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCargoOAE;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCargoTitular;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroDefinicionTitulo;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroEspecialidad;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroEstadoProcesoLegalizacion;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroFormula;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroTipoRepresentante;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroTipoServicioSanitario;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroTiposManifestacionViolencia;

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

    @CacheResult(cacheName = Constantes.SECTOR_ECONOMICO_CACHE)
    public List<SgSectorEconomico> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sectoreseconomico/buscar");
        SgSectorEconomico[] sectorEconomico = restClient.invokePost(webTarget, filtro, SgSectorEconomico[].class);
        List<SgSectorEconomico> se = Arrays.asList(sectorEconomico);
        return se;
    }

    @CacheResult(cacheName = Constantes.MODALIDAD_ATENCION_CACHE)
    public List<SgModalidadAtencion> buscarModalidadAtencion(FiltroModalidadAtencion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion/buscar");
        SgModalidadAtencion[] modalidadesAtencion = restClient.invokePost(webTarget, filtro, SgModalidadAtencion[].class);
        return Arrays.asList(modalidadesAtencion);
    }

    @CacheResult(cacheName = Constantes.ESCALA_CALIFICACION_CACHE)
    public List<SgEscalaCalificacion> buscarEscalaCalificacion(FiltroEscalaCalificacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escalacalificacion/buscar");
        SgEscalaCalificacion[] modalidadesAtencion = restClient.invokePost(webTarget, filtro, SgEscalaCalificacion[].class);
        return Arrays.asList(modalidadesAtencion);
    }

    public SgModalidadAtencion obtenerPorIdModalidadAtencion(Long modalidadAtencionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadAtencionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion");
        webTarget = webTarget.path(modalidadAtencionPk.toString());
        return restClient.invokeGet(webTarget, SgModalidadAtencion.class);
    }

    @CacheResult(cacheName = Constantes.SUBMODALIDAD_ATENCION_CACHE)
    public List<SgSubModalidadAtencion> buscarSubModalidad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades/buscar");
        SgSubModalidadAtencion[] subModalidad = restClient.invokePost(webTarget, filtro, SgSubModalidadAtencion[].class);
        return Arrays.asList(subModalidad);
    }

    public SgSubModalidadAtencion obtenerPorIdSubModalidad(Long subModalidadesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (subModalidadesPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades");
        webTarget = webTarget.path(subModalidadesPk.toString());
        return restClient.invokeGet(webTarget, SgSubModalidadAtencion.class);
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

    @CacheResult(cacheName = Constantes.CLASIFICACION_CACHE)
    public List<SgClasificacion> buscarClasificacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificaciones/buscar");
        SgClasificacion[] codiguera = restClient.invokePost(webTarget, filtro, SgClasificacion[].class);
        List<SgClasificacion> se = Arrays.asList(codiguera);
        return se;
    }

    @CacheResult(cacheName = Constantes.MOTIVO_INASISTENCIA_CACHE)
    public List<SgMotivoInasistencia> buscarMotivoInasistencia(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistencia/buscar");
        SgMotivoInasistencia[] codiguera = restClient.invokePost(webTarget, filtro, SgMotivoInasistencia[].class);
        List<SgMotivoInasistencia> se = Arrays.asList(codiguera);
        return se;
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

    @CacheResult(cacheName = Constantes.ETNIA_CACHE)
    public List<SgEtnia> buscarEtnia(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etnias/buscar");
        SgEtnia[] codiguera = restClient.invokePost(webTarget, filtro, SgEtnia[].class);
        List<SgEtnia> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.MEDIO_TRANSPORTE_CACHE)
    public List<SgMedioTransporte> buscarMediosTransporte(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/mediostransporte/buscar");
        SgMedioTransporte[] codiguera = restClient.invokePost(webTarget, filtro, SgMedioTransporte[].class);
        List<SgMedioTransporte> lista = Arrays.asList(codiguera);
        return lista;
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

    @CacheResult(cacheName = Constantes.TIPO_TELEFONO_CACHE)
    public List<SgTipoTelefono> buscarTipoTelefono(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostelefono/buscar");
        SgTipoTelefono[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoTelefono[].class);
        List<SgTipoTelefono> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.TIPO_IDENTIFICACION_CACHE)
    public List<SgTipoIdentificacion> buscarTipoIdentificacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposidentificacion/buscar");
        SgTipoIdentificacion[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoIdentificacion[].class);
        List<SgTipoIdentificacion> lista = Arrays.asList(codiguera);
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

    @CacheResult(cacheName = Constantes.CATEGORIA_VIOLENCIA_CACHE)
    public List<SgCategoriaViolencia> buscarCategoriaViolencia(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaviolencia/buscar");
        SgCategoriaViolencia[] codiguera = restClient.invokePost(webTarget, filtro, SgCategoriaViolencia[].class);
        List<SgCategoriaViolencia> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.TIPO_MANIFESTACION_CACHE)
    public List<SgTipoManifestacion> buscarTipoManifestacion(FiltroTiposManifestacionViolencia filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmanifestacion/buscar");
        SgTipoManifestacion[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoManifestacion[].class);
        List<SgTipoManifestacion> list = Arrays.asList(codiguera);
        return list;
    }

    public SgTipoManifestacion obtenerPorIdTipoManifestacion(Long tipoManifPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoManifPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmanifestacion");
        webTarget = webTarget.path(tipoManifPk.toString());
        return restClient.invokeGet(webTarget, SgTipoManifestacion.class);
    }

    @CacheResult(cacheName = Constantes.JORNADA_LABORAL_CACHE)
    public List<SgJornadaLaboral> buscarJornadasLaborales(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/jornadaslaborales/buscar");
        SgJornadaLaboral[] codiguera = restClient.invokePost(webTarget, filtro, SgJornadaLaboral[].class);
        List<SgJornadaLaboral> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.AREA_INDICADOR_CACHE)
    public List<SgAreaIndicador> buscarAreaIndicador(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasindicadores/buscar");
        SgAreaIndicador[] areasIndicadores = restClient.invokePost(webTarget, filtro, SgAreaIndicador[].class);
        return Arrays.asList(areasIndicadores);
    }

    @CacheResult(cacheName = Constantes.AREA_ASIGNATURA_MODULO_CACHE)
    public List<SgAreaAsignaturaModulo> buscarAreaAsignaturaModulo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/areasasignaturamodulo/buscar");
        SgAreaAsignaturaModulo[] areasAsignaturaModulo = restClient.invokePost(webTarget, filtro, SgAreaAsignaturaModulo[].class);
        return Arrays.asList(areasAsignaturaModulo);
    }

    @CacheResult(cacheName = Constantes.CALIFICACION_CACHE)
    public List<SgCalificacion> buscarCalificacion(FiltroCalificacionCatalogo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calificaciones/buscar");
        SgCalificacion[] clasificaciones = restClient.invokePost(webTarget, filtro, SgCalificacion[].class);
        return Arrays.asList(clasificaciones);
    }

    @CacheResult(cacheName = Constantes.NIVEL_EMPLEADO_CACHE)
    public List<SgNivelEmpleado> buscarNivelEmpleado(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelempleado/buscar");
        SgNivelEmpleado[] codiguera = restClient.invokePost(webTarget, filtro, SgNivelEmpleado[].class);
        List<SgNivelEmpleado> list = Arrays.asList(codiguera);
        return list;

    }

    @CacheResult(cacheName = Constantes.CATEGORIA_EMPLEADO_CACHE)
    public List<SgCategoriaEmpleado> buscarCategoriaEmpleado(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriaempleado/buscar");
        SgCategoriaEmpleado[] codiguera = restClient.invokePost(webTarget, filtro, SgCategoriaEmpleado[].class);
        List<SgCategoriaEmpleado> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.CARGO_CACHE)
    public List<SgCargo> buscarCargo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargo/buscar");
        SgCargo[] codiguera = restClient.invokePost(webTarget, filtro, SgCargo[].class);
        List<SgCargo> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.ACCION_CACHE)
    public List<SgAccionSolicitudTraslado> buscarAcciones(FiltroAccion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/acciones/buscar");
        SgAccionSolicitudTraslado[] codiguera = restClient.invokePost(webTarget, filtro, SgAccionSolicitudTraslado[].class);
        List<SgAccionSolicitudTraslado> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.TIPO_CONTRATO_CACHE)
    public List<SgTipoContrato> buscarTipoContrato(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipocontrato/buscar");
        SgTipoContrato[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoContrato[].class);
        List<SgTipoContrato> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.LEY_SALARIO_CACHE)
    public List<SgLeySalario> buscarLeySalario(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/leysalario/buscar");
        SgLeySalario[] codiguera = restClient.invokePost(webTarget, filtro, SgLeySalario[].class);
        List<SgLeySalario> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.FUENTE_FINANCIAMIENTO_CACHE)
    public List<SgFuenteFinanciamiento> buscarFuenteFinanciamiento(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentefinanciamiento/buscar");
        SgFuenteFinanciamiento[] codiguera = restClient.invokePost(webTarget, filtro, SgFuenteFinanciamiento[].class);
        List<SgFuenteFinanciamiento> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.INSTITUCION_PAGA_SALARIO_CACHE)
    public List<SgInstitucionPagaSalario> buscarInstitucionPagaSalario(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/institucionpagosalario/buscar");
        SgInstitucionPagaSalario[] codiguera = restClient.invokePost(webTarget, filtro, SgInstitucionPagaSalario[].class);
        List<SgInstitucionPagaSalario> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.TIPO_INSTITUCION_PAGA_CACHE)
    public List<SgTipoInstitucionPaga> buscarTipoInstitucionPaga(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoinstitucionpago/buscar");
        SgTipoInstitucionPaga[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoInstitucionPaga[].class);
        return new ArrayList<>(Arrays.asList(codiguera));
    }

    @CacheResult(cacheName = Constantes.TIPO_NOMBRAMIENTO_CACHE)
    public List<SgTipoNombramiento> buscarTipoNombramiento(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiponombramiento/buscar");
        SgTipoNombramiento[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoNombramiento[].class);
        List<SgTipoNombramiento> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.MODO_PAGO_CACHE)
    public List<SgModoPago> buscarModoPago(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modopago/buscar");
        SgModoPago[] codiguera = restClient.invokePost(webTarget, filtro, SgModoPago[].class);
        List<SgModoPago> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.SISTEMA_GESTION_CONTENIDO_CACHE)
    public List<SgSistemaGestionContenido> buscarSGC(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sistemasgestioncontenido/buscar");
        SgSistemaGestionContenido[] codiguera = restClient.invokePost(webTarget, filtro, SgSistemaGestionContenido[].class);
        List<SgSistemaGestionContenido> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.IDIOMA_CACHE)
    public List<SgIdioma> buscarIdioma(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/idiomas/buscar");
        SgIdioma[] codiguera = restClient.invokePost(webTarget, filtro, SgIdioma[].class);
        List<SgIdioma> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.NIVEL_IDIOMA_CACHE)
    public List<SgNivelIdioma> buscarNivelIdioma(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesidiomas/buscar");
        SgNivelIdioma[] codiguera = restClient.invokePost(webTarget, filtro, SgNivelIdioma[].class);
        List<SgNivelIdioma> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.ESCOLARIDAD_CACHE)
    public List<SgEscolaridad> buscarEscolaridad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escolaridades/buscar");
        SgEscolaridad[] codiguera = restClient.invokePost(webTarget, filtro, SgEscolaridad[].class);
        List<SgEscolaridad> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.TIPO_ESTUDIO_CACHE)
    public List<SgTipoEstudio> buscarTipoEstudio(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposestudio/buscar");
        SgTipoEstudio[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoEstudio[].class);
        List<SgTipoEstudio> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.CARRERA_CACHE)
    public List<SgCarrera> buscarCarrera(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/carreras/buscar");
        SgCarrera[] codiguera = restClient.invokePost(webTarget, filtro, SgCarrera[].class);
        List<SgCarrera> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.ESPECIALIDAD_CACHE)
    public List<SgEspecialidad> buscarEspecialidad(FiltroEspecialidad filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/especialidades/buscar");
        SgEspecialidad[] codiguera = restClient.invokePost(webTarget, filtro, SgEspecialidad[].class);
        List<SgEspecialidad> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.MODALIDAD_ESTUDIO_CACHE)
    public List<SgModalidadEstudio> buscarModalidadEstudio(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesestudio/buscar");
        SgModalidadEstudio[] codiguera = restClient.invokePost(webTarget, filtro, SgModalidadEstudio[].class);
        List<SgModalidadEstudio> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.TIPO_CAPACITACION_CACHE)
    public List<SgTipoCapacitacion> buscarTipoCapacitacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcapacitacion/buscar");
        SgTipoCapacitacion[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoCapacitacion[].class);
        List<SgTipoCapacitacion> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.ALCANCE_CAPACITACION_CACHE)
    public List<SgAlcanceCapacitacion> buscarAlcanceCapacitacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/alcancescapacitacion/buscar");
        SgAlcanceCapacitacion[] codiguera = restClient.invokePost(webTarget, filtro, SgAlcanceCapacitacion[].class);
        List<SgAlcanceCapacitacion> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.PROFESION_CACHE)
    public List<SgProfesion> buscarProfesion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/profesiones/buscar");
        SgProfesion[] codiguera = restClient.invokePost(webTarget, filtro, SgProfesion[].class);
        List<SgProfesion> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.TIPO_TRABAJO_CACHE)
    public List<SgTipoTrabajo> buscarTipoTrabajo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostrabajo/buscar");
        SgTipoTrabajo[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoTrabajo[].class);
        List<SgTipoTrabajo> list = Arrays.asList(codiguera);
        return list;
    }

    public SgTipoTrabajo obtenerPorIdTipoTrabajo(Long tipoTrabajoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoTrabajoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostrabajo");
        webTarget = webTarget.path(tipoTrabajoPk.toString());
        return restClient.invokeGet(webTarget, SgTipoTrabajo.class);
    }

    @CacheResult(cacheName = Constantes.DEPENDENCIA_ECONOMICA_CACHE)
    public List<SgDependenciaEconomica> buscarDependenciaEconomica(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/dependenciaseconomica/buscar");
        SgDependenciaEconomica[] codiguera = restClient.invokePost(webTarget, filtro, SgDependenciaEconomica[].class);
        List<SgDependenciaEconomica> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.TIPO_SANGRE_CACHE)
    public List<SgTipoSangre> buscarTipoSangre(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipossangre/buscar");
        SgTipoSangre[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoSangre[].class);
        List<SgTipoSangre> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.DISCAPACIDAD_CACHE)
    public List<SgDiscapacidad> buscarDiscapacidad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/discapacidades/buscar");
        SgDiscapacidad[] codiguera = restClient.invokePost(webTarget, filtro, SgDiscapacidad[].class);
        List<SgDiscapacidad> list = Arrays.asList(codiguera);
        return list;
    }

    public SgDiscapacidad obtenerPorIdDiscapacidad(Long discapacidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (discapacidadPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/discapacidades");
        webTarget = webTarget.path(discapacidadPk.toString());
        return restClient.invokeGet(webTarget, SgDiscapacidad.class);
    }

    @CacheResult(cacheName = Constantes.TRASTORNO_APRENDIZAJE_CACHE)
    public List<SgTrastornoAprendizaje> buscarTrastornoAprendizaje(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/trastornosaprendizaje/buscar");
        SgTrastornoAprendizaje[] codiguera = restClient.invokePost(webTarget, filtro, SgTrastornoAprendizaje[].class);
        List<SgTrastornoAprendizaje> list = Arrays.asList(codiguera);
        return list;
    }

    public SgTrastornoAprendizaje obtenerPorIdTrastornoAprendizaje(Long trasPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (trasPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/trastornosaprendizaje");
        webTarget = webTarget.path(trasPk.toString());
        return restClient.invokeGet(webTarget, SgTrastornoAprendizaje.class);
    }

    @CacheResult(cacheName = Constantes.OCUPACION_CACHE)
    public List<SgOcupacion> buscarOcupacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ocupaciones/buscar");
        SgOcupacion[] codiguera = restClient.invokePost(webTarget, filtro, SgOcupacion[].class);
        List<SgOcupacion> list = Arrays.asList(codiguera);
        return list;
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

    @CacheResult(cacheName = Constantes.CONFIGURACION_CACHE_CODIGO)
    public SgConfiguracion buscarConfiguracionPorCodigo(String codigo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigo == null) {
            return null;
        }  
        FiltroCodiguera filtro = new FiltroCodiguera();
        filtro.setCodigoExacto(codigo);
        List<SgConfiguracion> configs = buscarConfiguracion(filtro);
        if (!configs.isEmpty()) {
            return configs.get(0);
        }
        return null;
    }
    
    @CacheResult(cacheName = Constantes.CONFIGURACION_FIRMA_ELECTRONICA_CACHE_CODIGO)
    public SgConfiguracionFirmaElectronica buscarConfiguracionFirmaElectronicaPorCodigo(String codigo) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException, Exception {
        if (codigo == null) {
            return null;
        }
        FiltroCodiguera filtro = new FiltroCodiguera();
        filtro.setCodigoExacto(codigo);    
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuracionesfirmaelectronica/buscar");
        SgConfiguracionFirmaElectronica[] codiguera = restClient.invokePost(webTarget, filtro, SgConfiguracionFirmaElectronica[].class);
        List<SgConfiguracionFirmaElectronica> list = Arrays.asList(codiguera);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    

    public LocalDate obtenerFechaIngresoMatriculasPorDefecto(Integer anio, SgTipoCalendario tip) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        FiltroCodiguera fcn = new FiltroCodiguera();
        if (Constantes.CALENDARIO_INTERNACIONAL.equals(tip.getTceCodigo())) {
            fcn.setCodigoExacto(Constantes.CONFIG_DIA_MES_INGRESO_MATRICULA_INTERNACIONAL);
        } else {
            fcn.setCodigoExacto(Constantes.CONFIG_DIA_MES_INGRESO_MATRICULA_NACIONAL);
        }
        List<SgConfiguracion> cnfs = this.buscarConfiguracion(fcn);
        if (cnfs.isEmpty()) {
            BusinessException be = new BusinessException();
            be.addError("NO_SE_ENCONTRO_CONFIG_DIA_MES_INGRESO_MATRICULA");
            throw be;
        }
        SgConfiguracion cnf = cnfs.get(0);
        String[] diaMes = cnf.getConValor().split("/");
        return LocalDate.of(anio, Integer.parseInt(diaMes[1]), Integer.parseInt(diaMes[0]));
    }

    @CacheResult(cacheName = Constantes.MOTIVO_RETIRO_CACHE)
    public List<SgMotivoRetiro> buscarMotivoRetiro(FiltroMotivoRetiro filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosretiro/buscar");
        SgMotivoRetiro[] motivosRetiro = restClient.invokePost(webTarget, filtro, SgMotivoRetiro[].class);
        return Arrays.asList(motivosRetiro);
    }

    @CacheResult(cacheName = Constantes.TIPO_PARENTESCO_CACHE)
    public List<SgTipoParentesco> buscarTipoParentesco(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposparentesco/buscar");
        SgTipoParentesco[] tiposParentesco = restClient.invokePost(webTarget, filtro, SgTipoParentesco[].class);
        return Arrays.asList(tiposParentesco);
    }

    @CacheResult(cacheName = Constantes.NIVEL_FORMACION_DOCENTE_CACHE)
    public List<SgNivelFormacionDocente> buscarNivelFormacionDocente(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nivelesformaciondocente/buscar");
        SgNivelFormacionDocente[] codiguera = restClient.invokePost(webTarget, filtro, SgNivelFormacionDocente[].class);
        List<SgNivelFormacionDocente> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.CATEGORIA_FORMACION_DOCENTE_CACHE)
    public List<SgCategoriaFormacionDocente> buscarCategoriaFormacionDocente(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriasformaciondocente/buscar");
        SgCategoriaFormacionDocente[] codiguera = restClient.invokePost(webTarget, filtro, SgCategoriaFormacionDocente[].class);
        List<SgCategoriaFormacionDocente> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.MODULO_FORMACION_DOCENTE_CACHE)
    public List<SgModuloFormacionDocente> buscarModuloFormacionDocente(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modulosformaciondocente/buscar");
        SgModuloFormacionDocente[] codiguera = restClient.invokePost(webTarget, filtro, SgModuloFormacionDocente[].class);
        List<SgModuloFormacionDocente> list = Arrays.asList(codiguera);
        return list;
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

    @CacheResult(cacheName = Constantes.TIPO_CALLE_CACHE)
    public List<SgTipoCalle> buscarTiposCalle(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposcalle/buscar");
        SgTipoCalle[] carrera = restClient.invokePost(webTarget, filtro, SgTipoCalle[].class);
        return Arrays.asList(carrera);
    }

    @CacheResult(cacheName = Constantes.SERVICIO_INFRAESTRUCTURA_CACHE)
    public List<SgServicioInfraestructura> buscarServiciosInfraestructura(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/servicioinfraestructura/buscar");
        SgServicioInfraestructura[] servicioInfraestructura = restClient.invokePost(webTarget, filtro, SgServicioInfraestructura[].class);
        return Arrays.asList(servicioInfraestructura);
    }

    public SgEspecialidad obtenerPorIdEspecialidad(Long especialidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (especialidadPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/especialidades");
        webTarget = webTarget.path(especialidadPk.toString());
        return restClient.invokeGet(webTarget, SgEspecialidad.class);
    }

    @CacheResult(cacheName = Constantes.TIPO_ORGANISMO_ADMINISTRATIVO_CACHE)
    public List<SgTipoOrganismoAdministrativo> buscarTipoOrganismoAdministrativo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposorganismoadministrativo/buscar");
        SgTipoOrganismoAdministrativo[] tiposOrganismoAdministrativo = restClient.invokePost(webTarget, filtro, SgTipoOrganismoAdministrativo[].class);
        return Arrays.asList(tiposOrganismoAdministrativo);
    }

    @CacheResult(cacheName = Constantes.AFP_CACHE)
    public List<SgAfp> buscarAfp(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/afp/buscar");
        SgAfp[] afps = restClient.invokePost(webTarget, filtro, SgAfp[].class);
        return Arrays.asList(afps);
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

    @CacheResult(cacheName = Constantes.MOTIVO_TRASLADO_CACHE)
    public List<SgMotivoTraslado> buscarMotivoTraslado(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivostraslado/buscar");
        SgMotivoTraslado[] motivos = restClient.invokePost(webTarget, filtro, SgMotivoTraslado[].class);
        return Arrays.asList(motivos);
    }

    @CacheResult(cacheName = Constantes.TIPO_REPRESENTANTE_CACHE)
    public List<SgTipoRepresentante> buscarTipoRepresentante(FiltroTipoRepresentante filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporepresentante/buscar");
        SgTipoRepresentante[] tipoRepresentante = restClient.invokePost(webTarget, filtro, SgTipoRepresentante[].class);
        return Arrays.asList(tipoRepresentante);
    }

    @CacheResult(cacheName = Constantes.CARGO_TITULAR_CACHE)
    public List<SgCargoTitular> buscarCargoTitular(FiltroCargoTitular filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargotitular/buscar");
        SgCargoTitular[] cargoTitular = restClient.invokePost(webTarget, filtro, SgCargoTitular[].class);
        return Arrays.asList(cargoTitular);
    }

    @CacheResult(cacheName = Constantes.DEFINICION_TITULO_CACHE)
    public List<SgDefinicionTitulo> buscarDefinicionTitulo(FiltroDefinicionTitulo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/definiciontitulo/buscar");
        SgDefinicionTitulo[] definicionTitulo = restClient.invokePost(webTarget, filtro, SgDefinicionTitulo[].class);
        return Arrays.asList(definicionTitulo);
    }

    public SgDefinicionTitulo obtenerPorIdDefinicionTitulo(Long definicionTituloPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (definicionTituloPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/definiciontitulo");
        webTarget = webTarget.path(definicionTituloPk.toString());
        return restClient.invokeGet(webTarget, SgDefinicionTitulo.class);
    }

    @CacheResult(cacheName = Constantes.TIPO_RIESGO_CACHE)
    public List<SgTiposRiesgo> buscarTipoRiesgo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiporiesgo/buscar");
        SgTiposRiesgo[] tipoRiesgo = restClient.invokePost(webTarget, filtro, SgTiposRiesgo[].class);
        return new ArrayList<>(Arrays.asList(tipoRiesgo));
    }

    @CacheResult(cacheName = Constantes.GRADO_AFECTACION_CACHE)
    public List<SgGradoAfectacion> buscarGradoAfectacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/gradoafectacion/buscar");
        SgGradoAfectacion[] gradoAfectacion = restClient.invokePost(webTarget, filtro, SgGradoAfectacion[].class);
        return new ArrayList<>(Arrays.asList(gradoAfectacion));
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

    @CacheResult(cacheName = Constantes.FORMULAS_CACHE)
    public List<SgFormula> buscarFormulas(FiltroFormula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formulas/buscar");
        SgFormula[] formulas = restClient.invokePost(webTarget, filtro, SgFormula[].class);
        return Arrays.asList(formulas);
    }

    @CacheResult(cacheName = Constantes.MOTIVO_INASISTENCIA_PERSONAL_CACHE)
    public List<SgMotivoInasistenciaPersonal> buscarMotivoInasistenciaPersonal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosinasistenciapersonal/buscar");
        SgMotivoInasistenciaPersonal[] codiguera = restClient.invokePost(webTarget, filtro, SgMotivoInasistenciaPersonal[].class);
        List<SgMotivoInasistenciaPersonal> se = Arrays.asList(codiguera);
        return se;
    }

    @CacheResult(cacheName = Constantes.CATEGORIAS_CURSO_CACHE)
    public List<SgCategoriaCurso> buscarCategoriaCurso(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriascursos/buscar");
        SgCategoriaCurso[] codiguera = restClient.invokePost(webTarget, filtro, SgCategoriaCurso[].class);
        List<SgCategoriaCurso> se = Arrays.asList(codiguera);
        return se;
    }

    @CacheResult(cacheName = Constantes.TIPOS_MODULO_CACHE)
    public List<SgTipoModulo> buscarTipoModulo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmodulo/buscar");
        SgTipoModulo[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoModulo[].class);
        List<SgTipoModulo> se = Arrays.asList(codiguera);
        return se;
    }

    @CacheResult(cacheName = Constantes.TIPO_ACCION_CACHE)
    public List<SgTipoAccion> buscarTipoAccion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposaccion/buscar");
        SgTipoAccion[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoAccion[].class);
        List<SgTipoAccion> se = Arrays.asList(codiguera);
        return se;
    }

    @CacheResult(cacheName = Constantes.TIPO_APOYO_CACHE)
    public List<SgTipoApoyo> buscarTipoApoyo(FiltroTiposApoyo filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposapoyo/buscar");
        SgTipoApoyo[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoApoyo[].class);
        List<SgTipoApoyo> se = Arrays.asList(codiguera);
        return se;
    }

    @CacheResult(cacheName = Constantes.TIPO_PROVEEDOR_CACHE)
    public List<SgTipoProveedor> buscarTipoProveedor(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposproveedor/buscar");
        SgTipoProveedor[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoProveedor[].class);
        List<SgTipoProveedor> se = Arrays.asList(codiguera);
        return se;
    }

    @CacheResult(cacheName = Constantes.GRADO_RIESGO_CACHE)
    public List<SgGradoRiesgo> buscarGradoRiesgo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/gradosriesgo/buscar");
        SgGradoRiesgo[] codiguera = restClient.invokePost(webTarget, filtro, SgGradoRiesgo[].class);
        List<SgGradoRiesgo> se = Arrays.asList(codiguera);
        return se;
    }

    @CacheResult(cacheName = Constantes.PROYECTO_INSTITUCIONAL_CACHE)
    public List<SgProyectoInstitucional> buscarProyecto(FiltroProyectoInstitucional filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosinstitucionales/buscar");
        SgProyectoInstitucional[] proyectos = restClient.invokePost(webTarget, filtro, SgProyectoInstitucional[].class);
        return Arrays.asList(proyectos);
    }

    public SgProyectoInstitucional obtenerPorIdProyectoInstitucional(Long proyectoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (proyectoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/proyectosinstitucionales");
        webTarget = webTarget.path(proyectoPk.toString());
        return restClient.invokeGet(webTarget, SgProyectoInstitucional.class);
    }

    @CacheResult(cacheName = Constantes.VELOCIDAD_INTERNET_CACHE)
    public List<SgVelocidadInternet> buscarVelocidadInternet(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/velocidadesinternet/buscar");
        SgVelocidadInternet[] codiguera = restClient.invokePost(webTarget, filtro, SgVelocidadInternet[].class);
        List<SgVelocidadInternet> se = Arrays.asList(codiguera);
        return se;
    }

    @CacheResult(cacheName = Constantes.ORGANISMO_COORDINACION_ESCOLAR_CACHE)
    public List<SgOrganismoCoordinacionEscolar> buscarOrganismoCoordinacionEscolar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/organismoscoordinacionescolar/buscar");
        SgOrganismoCoordinacionEscolar[] codiguera = restClient.invokePost(webTarget, filtro, SgOrganismoCoordinacionEscolar[].class);
        List<SgOrganismoCoordinacionEscolar> se = Arrays.asList(codiguera);
        return se;
    }

    @CacheResult(cacheName = Constantes.RECURSOS_CACHE)
    public List<SgRecurso> buscarRecurso(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/recursos/buscar");
        SgRecurso[] codiguera = restClient.invokePost(webTarget, filtro, SgRecurso[].class);
        List<SgRecurso> list = Arrays.asList(codiguera);
        return list;
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

    @CacheResult(cacheName = Constantes.CARGO_OAE_CACHE)
    public List<SgCargoOAE> buscarCargoOAE(FiltroCargoOAE filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargooae/buscar");
        SgCargoOAE[] cargoOAE = restClient.invokePost(webTarget, filtro, SgCargoOAE[].class);
        return Arrays.asList(cargoOAE);
    }

    @CacheResult(cacheName = Constantes.TIPO_DOCUMENTO_DOCENTE)
    public List<SgTipoDocumentoDocente> buscarTipoDocumentoDocente(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposdocumentosdocentes/buscar");
        SgTipoDocumentoDocente[] cargoOAE = restClient.invokePost(webTarget, filtro, SgTipoDocumentoDocente[].class);
        return Arrays.asList(cargoOAE);
    }

    @CacheResult(cacheName = Constantes.ESTADO_DATO_CONTRATACION_CACHE)
    public List<SgEstadoDatoContratacion> buscarEstadoDatoContratacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosdatocontratacion/buscar");
        SgEstadoDatoContratacion[] estados = restClient.invokePost(webTarget, filtro, SgEstadoDatoContratacion[].class);
        return Arrays.asList(estados);
    }

    @CacheResult(cacheName = Constantes.CARGA_ARCHIVOS)
    public List<SgCargaArchivo> buscarCargaArchivos(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargasarchivos/buscar");
        SgCargaArchivo[] cargas = restClient.invokePost(webTarget, filtro, SgCargaArchivo[].class);
        return Arrays.asList(cargas);
    }

    public List<SgFormula> buscarFormula(FiltroFormula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formulas/buscar");
        SgFormula[] formulas = restClient.invokePost(webTarget, filtro, SgFormula[].class);
        return Arrays.asList(formulas);
    }

    @CacheResult(cacheName = Constantes.CARGA_MAXIMO_NIVEL_EDUCATIVO_ALCANZADO)
    public List<SgMaximoNivelEducativoAlcanzado> buscarMaximoNivelEducativoAlcanzado(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/maximoniveleducativoalcanzado/buscar");
        SgMaximoNivelEducativoAlcanzado[] cargas = restClient.invokePost(webTarget, filtro, SgMaximoNivelEducativoAlcanzado[].class);
        return Arrays.asList(cargas);
    }

    @CacheResult(cacheName = Constantes.MOTIVO_FALLECIMIENTO_CACHE)
    public List<SgMotivoFallecimiento> buscarMotivoFallecimiento(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivofallecimiento/buscar");
        SgMotivoFallecimiento[] motivos = restClient.invokePost(webTarget, filtro, SgMotivoFallecimiento[].class);
        return Arrays.asList(motivos);
    }

    public List<SgEstadoInmueble> buscarEstadoInmueble(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadosinmuebles/buscar");
        SgEstadoInmueble[] estadosInmuebles = restClient.invokePost(webTarget, filtro, SgEstadoInmueble[].class);
        return Arrays.asList(estadosInmuebles);
    }

    public List<SgServicioBasico> buscarServicioBasico(FiltroServicioBasico filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/serviciosbasicos/buscar");
        SgServicioBasico[] serviciosBasicos = restClient.invokePost(webTarget, filtro, SgServicioBasico[].class);
        return Arrays.asList(serviciosBasicos);
    }

    public List<SgEspacioComun> buscarEspacioComun(FiltroEspacioComplementario filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/espaciocomun/buscar");
        SgEspacioComun[] espacioComun = restClient.invokePost(webTarget, filtro, SgEspacioComun[].class);
        return Arrays.asList(espacioComun);
    }

    public List<SgPropietariosTerreno> buscarPropietariosTerreno(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/propietarioterreno/buscar");
        SgPropietariosTerreno[] propietarioTerreno = restClient.invokePost(webTarget, filtro, SgPropietariosTerreno[].class);
        return Arrays.asList(propietarioTerreno);
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

    @CacheResult(cacheName = Constantes.IMPLEMENTADORA_CACHE)
    public List<SgImplementadora> buscarImplementadora(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/implementadoras/buscar");
        SgImplementadora[] codiguera = restClient.invokePost(webTarget, filtro, SgImplementadora[].class);
        List<SgImplementadora> lista = Arrays.asList(codiguera);
        return lista;
    }

    @CacheResult(cacheName = Constantes.MOTIVO_ANULACION_TITULO)
    public List<SgMotivoAnulacionTitulo> buscarMotivoAnulacionTitulo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoanulaciontitulo/buscar");
        SgMotivoAnulacionTitulo[] motivoAnulacionTitulo = restClient.invokePost(webTarget, filtro, SgMotivoAnulacionTitulo[].class);
        return Arrays.asList(motivoAnulacionTitulo);
    }

    @CacheResult(cacheName = Constantes.TIPO_ALFABETIZADOR)
    public List<SgTipoAlfabetizador> buscarTipoAlfabetizador(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipoalfabetizador/buscar");
        SgTipoAlfabetizador[] tipoAlfabetizador = restClient.invokePost(webTarget, filtro, SgTipoAlfabetizador[].class);
        return Arrays.asList(tipoAlfabetizador);
    }

    @CacheResult(cacheName = Constantes.MOTIVO_REIMPRESION)
    public List<SgMotivoReimpresion> buscarMotivoReimpresion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoreimpresion/buscar");
        SgMotivoReimpresion[] motivoReimpresion = restClient.invokePost(webTarget, filtro, SgMotivoReimpresion[].class);
        return Arrays.asList(motivoReimpresion);
    }

    @CacheResult(cacheName = Constantes.ASOCIACIONES_CACHE)
    public List<SgAsociacion> buscarAsociaciones(FiltroAsociacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/asociaciones/buscar");
        SgAsociacion[] asociaciones = restClient.invokePost(webTarget, filtro, SgAsociacion[].class);
        return Arrays.asList(asociaciones);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, java.util.concurrent.TimeoutException.class}, delay = 250)
    public SgFormula obtenerPorIdFormula(Long formulaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (formulaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/formulas");
        webTarget = webTarget.path(formulaPk.toString());
        return restClient.invokeGet(webTarget, SgFormula.class);
    }

    @CacheResult(cacheName = Constantes.PREGUNTAS_CIERRE_ANIO_CACHE)
    public List<SgPreguntaCierreAnio> buscarPreguntasCierreAnio(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/preguntacierreanio/buscar");
        SgPreguntaCierreAnio[] preguntaCierreAnio = restClient.invokePost(webTarget, filtro, SgPreguntaCierreAnio[].class);
        return Arrays.asList(preguntaCierreAnio);
    }

    @CacheResult(cacheName = Constantes.PREGUNTAS_CIERRE_ANIO_CACHE)
    public List<SgTituloAnterior> buscarTituloAnterior(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tituloanterior/buscar");
        SgTituloAnterior[] tituloAnterior = restClient.invokePost(webTarget, filtro, SgTituloAnterior[].class);
        return Arrays.asList(tituloAnterior);
    }
    
    public SgTipoOrganismoAdministrativo obtenerPorIdTipoOrganismo(Long tipoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (tipoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposorganismoadministrativo");
        webTarget = webTarget.path(tipoPk.toString());
        return restClient.invokeGet(webTarget, SgTipoOrganismoAdministrativo.class);
    }
    
    @CacheResult(cacheName = Constantes.PERFIL_CACHE)
    public List<SgPerfilesUsuariosIngresadosCe> buscarPerfiles(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/perfilesdeusuariosingresadosce/buscar");
        SgPerfilesUsuariosIngresadosCe[] perfilesdeusuariosingresadosce = restClient.invokePost(webTarget, filtro, SgPerfilesUsuariosIngresadosCe[].class);
        return Arrays.asList(perfilesdeusuariosingresadosce);
    }
    
    @CacheResult(cacheName = Constantes.REFERENCIAS_CACHE)
    public List<SgReferenciasApoyo> buscarReferenciasApoyo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/referenciasdeapoyo/buscar");
        SgReferenciasApoyo[] codiguera = restClient.invokePost(webTarget, filtro, SgReferenciasApoyo[].class);
        List<SgReferenciasApoyo> list = Arrays.asList(codiguera);
        return list;
    }

    @CacheResult(cacheName = Constantes.TERAPIAS_CACHE)
    public List<SgTerapia> buscarTerapias(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/terapias/buscar");
        SgTerapia[] codiguera = restClient.invokePost(webTarget, filtro, SgTerapia[].class);
        List<SgTerapia> list = Arrays.asList(codiguera);
        return list;
    }
    
    public SgReferenciasApoyo obtenerPorIdReferenciasApoyo(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/referenciasdeapoyo");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokeGet(webTarget, SgReferenciasApoyo.class);
    }
    
    public SgTerapia obtenerPorIdTerapia(Long pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/terapias");
        webTarget = webTarget.path(pk.toString());
        return restClient.invokeGet(webTarget, SgTerapia.class);
    }
    
    @CacheResult(cacheName = Constantes.MOTIVOS_SELECCION_CACHE)
    public List<SgMotivosSeleccionPLaza> buscarMotivosSeleccion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosseleccionplaza/buscar");
        SgMotivosSeleccionPLaza[] codiguera = restClient.invokePost(webTarget, filtro, SgMotivosSeleccionPLaza[].class);
        List<SgMotivosSeleccionPLaza> list = Arrays.asList(codiguera);
        return list;
    }
    
    @CacheResult(cacheName = Constantes.CALIDAD_INGRESO_APLICANTE_CACHE)
    public List<SgCalidadIngresoAplicantes> buscarIngresoAplicantePlaza(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/calidadesingresoaplicantesplaza/buscar");
        SgCalidadIngresoAplicantes[] codiguera = restClient.invokePost(webTarget, filtro, SgCalidadIngresoAplicantes[].class);
        List<SgCalidadIngresoAplicantes> list = Arrays.asList(codiguera);
        return list;
    }
    
    @CacheResult(cacheName = Constantes.IDENTIFICACION_OAE_CACHE)
    public List<SgIdentificacionOAE> buscarIdentificacionOAE(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/identificacionoae/buscar");
        SgIdentificacionOAE[] identificacionOAE = restClient.invokePost(webTarget, filtro, SgIdentificacionOAE[].class);
        return Arrays.asList(identificacionOAE);
    }
    
    @CacheResult(cacheName = Constantes.ENFERMEDAD_NO_TRANSMITIBLE_CACHE)
    public List<SgEnfermedadNoTransmisible> buscarEnfermedadNoTransmisible(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/enfermedadesnotransmisibles/buscar");
        SgEnfermedadNoTransmisible[] enfermedadesNoTransmisibles = restClient.invokePost(webTarget, filtro, SgEnfermedadNoTransmisible[].class);
        return Arrays.asList(enfermedadesNoTransmisibles);
    }
    
    public SgEnfermedadNoTransmisible obtenerPorIdEnfermedadNoTransmitible(Long enfermedadNoTransmisiblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (enfermedadNoTransmisiblePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/enfermedadesnotransmisibles");
        webTarget = webTarget.path(enfermedadNoTransmisiblePk.toString());
        return restClient.invokeGet(webTarget, SgEnfermedadNoTransmisible.class);
    }
    
    @CacheResult(cacheName = Constantes.TIEMPO_COMIDA_DIA_CACHE)
    public List<SgTiemposComidaDia> buscarTiempoComidaDia(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiempocomidadia/buscar");
        SgTiemposComidaDia[] tiempoComidaDia = restClient.invokePost(webTarget, filtro, SgTiemposComidaDia[].class);
        return Arrays.asList(tiempoComidaDia);
    }
    
    public SgTiemposComidaDia obtenerPorIdTiempoComida(Long enfermedadNoTransmisiblePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (enfermedadNoTransmisiblePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiempocomidadia");
        webTarget = webTarget.path(enfermedadNoTransmisiblePk.toString());
        return restClient.invokeGet(webTarget, SgTiemposComidaDia.class);
    }
    
    @CacheResult(cacheName = Constantes.COMPANIAS_TELECOMUNICACION_CACHE)
    public List<SgCompaniaTelecomunicacion> buscarCompaniasTelecomunicacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/companiastelecomunicacion/buscar");
        SgCompaniaTelecomunicacion[] companias = restClient.invokePost(webTarget, filtro, SgCompaniaTelecomunicacion[].class);
        return Arrays.asList(companias);
    }
    
    @CacheResult(cacheName = Constantes.ELEMENTOS_HOGAR_CACHE)
    public List<SgElementoHogar> buscarElementosHogar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/elementoshogar/buscar");
        SgElementoHogar[] elementos = restClient.invokePost(webTarget, filtro, SgElementoHogar[].class);
        return Arrays.asList(elementos);
    }
    
    public SgElementoHogar obtenerPorIdElementoHogar(Long elementoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (elementoPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/elementoshogar");
        webTarget = webTarget.path(elementoPk.toString());
        return restClient.invokeGet(webTarget, SgElementoHogar.class);
    }
    
    @CacheResult(cacheName = Constantes.FUENTES_ABASTECIMIENTO_AGUA)
    public List<SgFuenteAbastecimientoAgua> buscarFuentesAbastecimientoAgua(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/fuentesabastecimientoagua/buscar");
        SgFuenteAbastecimientoAgua[] fuentes = restClient.invokePost(webTarget, filtro, SgFuenteAbastecimientoAgua[].class);
        return Arrays.asList(fuentes);
    }
    
    @CacheResult(cacheName = Constantes.MATERIALES_PISO_CACHE)
    public List<SgMaterialPiso> buscarMaterialesPiso(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/materialespiso/buscar");
        SgMaterialPiso[] tiempoComidaDia = restClient.invokePost(webTarget, filtro, SgMaterialPiso[].class);
        return Arrays.asList(tiempoComidaDia);
    }
    
    @CacheResult(cacheName = Constantes.MOTIVO_CIERRE_SEDE_CACHE)
    public List<SgMotivoCierreSede> buscarMotivoCierreSede(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null ) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client,ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivoscierresede/buscar");
        SgMotivoCierreSede[] motivosCierreSede = restClient.invokePost(webTarget, filtro, SgMotivoCierreSede[].class);
        return Arrays.asList(motivosCierreSede);
    }
}
