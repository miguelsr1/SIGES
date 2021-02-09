/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCentroEducativo;
import sv.gob.mined.siges.web.dto.SgCentroEducativoOficial;
import sv.gob.mined.siges.web.dto.SgCentroEducativoPrivado;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgCirculoAlfabetizacion;
import sv.gob.mined.siges.web.dto.SgCirculoInfancia;
import sv.gob.mined.siges.web.dto.SgSedeEducame;
import sv.gob.mined.siges.web.dto.SgDireccion;
import sv.gob.mined.siges.web.dto.SgRelSedeRiesgoAfectacion;
import sv.gob.mined.siges.web.dto.SgTipoCalendario;
import sv.gob.mined.siges.web.dto.catalogo.SgClasificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgGradoAfectacion;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.catalogo.SgServicioInfraestructura;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.web.dto.catalogo.SgTiposRiesgo;
import sv.gob.mined.siges.web.enumerados.TipoSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CentroEducativoOficialRestClient;
import sv.gob.mined.siges.web.restclient.CentroEducativoPrivadoRestClient;
import sv.gob.mined.siges.web.restclient.CirculoAlfabetizacionRestClient;
import sv.gob.mined.siges.web.restclient.CirculoInfanciaRestClient;
import sv.gob.mined.siges.web.restclient.RelSedeRiesgoAfectacionRestClient;
import sv.gob.mined.siges.web.restclient.SedeEducameRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TipoCalendarioRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgAccionPrevenirEmbarazo;
import sv.gob.mined.siges.web.enumerados.EnumAmbito;
import sv.gob.mined.siges.web.dto.SgAsistenciaSede;
import sv.gob.mined.siges.web.dto.SgCasoViolacion;
import sv.gob.mined.siges.web.dto.SgFactorRiesgoSede;
import sv.gob.mined.siges.web.dto.catalogo.SgGradoRiesgo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoAccion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoApoyo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoProveedor;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgDetalleMatricula;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgServicioEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgImplementadora;
import sv.gob.mined.siges.web.dto.SgOrganismoCESede;
import sv.gob.mined.siges.web.dto.SgProyectoInstitucionalSede;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoCierreSede;
import sv.gob.mined.siges.web.dto.catalogo.SgOrganismoCoordinacionEscolar;
import sv.gob.mined.siges.web.dto.catalogo.SgVelocidadInternet;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudTraslado;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAccionPrevenirEmbarazo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsistenciaSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCasoViolacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDetalleMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFactorRiesgoSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOrganismoCESede;
import sv.gob.mined.siges.web.lazymodels.LazyAsistenciaSedeDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyCasoViolacionDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyFactorRiesgoSedeDataModel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyectoInstitucionalSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudTraslado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTiposApoyo;
import sv.gob.mined.siges.web.lazymodels.LazyProyectoInstitucionalDataModel;
import sv.gob.mined.siges.web.lazymodels.LazySolicitudTrasladoDataModel;
import sv.gob.mined.siges.web.restclient.AccionPrevenirEmbarazoRestClient;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.AsistenciaSedeRestClient;
import sv.gob.mined.siges.web.restclient.CasoViolacionRestClient;
import sv.gob.mined.siges.web.restclient.DetalleMatriculaRestClient;
import sv.gob.mined.siges.web.restclient.FactorRiesgoSedeRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.OrganismoCESedeRestClient;
import sv.gob.mined.siges.web.restclient.ProyectoInstitucionalRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudTrasladoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;
import sv.gob.mined.siges.web.validacion.SedesViewValidator;

@Named
@ViewScoped
public class SedeBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SedeBean.class.getName());

    @Inject
    private SedeRestClient restClient;

    @Inject
    private CentroEducativoOficialRestClient cedOficialRestClient;

    @Inject
    private CentroEducativoPrivadoRestClient cedPrivadoRestClient;

    @Inject
    private CirculoAlfabetizacionRestClient circuloAlfabetizacionRestClient;

    @Inject
    private CirculoInfanciaRestClient circuloInfanciaRestClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private ApplicationBean appBean;

    @Inject
    private TipoCalendarioRestClient tipoCalendarioRestClient;

    @Inject
    private SedeEducameRestClient educameRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private RelSedeRiesgoAfectacionRestClient relSedeRiesgoAfectacion;

    @Inject
    private AnioLectivoRestClient restAnio;

    @Inject
    private SolicitudTrasladoRestClient restSolicitudesTraslado;

    @Inject
    private CasoViolacionRestClient casosViolacionRestClient;

    @Inject
    private AccionPrevenirEmbarazoRestClient accionPrevenirEmbarazoRestClient;

    @Inject
    private AsistenciaSedeRestClient asistenciaSedeRestClient;

    @Inject
    private FactorRiesgoSedeRestClient factorRiesgoRestClient;

    @Inject
    private ProyectoInstitucionalRestClient restProyectoInstitucional;

    @Inject
    private OrganismoCESedeRestClient restOrganismo;
    
    @Inject
    private DetalleMatriculaRestClient detalleMatriculaRestClient;
    
    @Inject
    private NivelRestClient NivelRestClient;

    @Inject
    @Param(name = "id")
    private Long sedeId;

    @Inject
    @Param(name = "rev")
    private Long sedeRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;
    
    @Inject
    @ConfigProperty(name = "constantes-entidades.sede-padre-alfabetizacion-pk")
    private Long sedePadreAlfabetizacionPk;

    @Inject
    private SedeRestClient restSede;

    private Boolean soloLectura = Boolean.FALSE;
    private String tabActiveId;
    private SgSede entidadEnEdicion;
    private SofisComboG<SgClasificacion> comboClasificacion;
    private SofisComboG<TipoSede> comboTiposSede;
    private SedesViewValidator sedesViewValidator;
    private List<SgJornadaLaboral> jornadasLaborales;
    private SofisComboG<SgTipoCalendario> tipoCalendarioCombo;
    private List<SgServicioInfraestructura> serviciosInfra;
    private SofisComboG<SgTipoOrganismoAdministrativo> tipoOrganismoAdministrativoCombo;
    private SgRelSedeRiesgoAfectacion factorRiesgoSocial;
    private SofisComboG<SgTiposRiesgo> tipoRiesgoCombo;
    private SofisComboG<SgGradoAfectacion> gradoAfectacionCombo;
    private SofisComboG<EnumEstadoSolicitudTraslado> comboEstadoTraslado;
    private SofisComboG<SgAnioLectivo> comboAnioLectivoSolicitudes;
    private SofisComboG<SgAnioLectivo> comboAnioLectivoRetirados;
    private SofisComboG<SgAnioLectivo> comboAnioLectivoMatriculados;
    private SofisComboG<SgAnioLectivo> comboAnioLectivoOtros;
    private SofisComboG<SgAnioLectivo> comboAnioLectivoInsert;
    private FiltroSolicitudTraslado filtroSolicitud = new FiltroSolicitudTraslado();
    private Integer paginado = 10;
    private Integer paginadoMedia = 1;
    private LazySolicitudTrasladoDataModel solicitudTrasladoLazyModel;
    private Long totalSolicitudes;
    private Long ninosSolicitudes;
    private Long ninasSolicitudes;
    private LazySolicitudTrasladoDataModel solicitudRetiradosLazyModel;
    private Long totalRetirados;
    private Long totalRetiradosSinRepetir;
    private Long ninosRetirados;
    private Long ninasRetirados;
    private LazySolicitudTrasladoDataModel solicitudMatriculadosLazyModel;
    private Long totalMatriculados;
    private Long totalMatriculadosSinRepetir;
    private Long ninosMatriculados;
    private Long ninasMatriculados;
    private List<EnumEstadoSolicitudTraslado> estados = new ArrayList();

    private SgCasoViolacion casoViolacion = new SgCasoViolacion();
    private SgAccionPrevenirEmbarazo accionPrevenirEmbarazo = new SgAccionPrevenirEmbarazo();
    private SgAsistenciaSede asistenciaSede = new SgAsistenciaSede();
    private SgFactorRiesgoSede factorRiesgoSede = new SgFactorRiesgoSede();
    private SofisComboG<SgTipoAccion> comboTipoAccion;
    private SofisComboG<SgTipoApoyo> comboTipoApoyo;
    private SofisComboG<SgTipoProveedor> comboTipoProveedor;
    private SofisComboG<SgTiposRiesgo> comboTipoRiesgo;
    private SofisComboG<SgGradoRiesgo> comboGradoRiesgo;
    private List<SgAccionPrevenirEmbarazo> listaAccionPrevenirEmbarazo = new ArrayList<>();
    private SgAnioLectivo anioSeleccionadoInsert;
    private LazyCasoViolacionDataModel casoViolacionLazyModel;
    private Long totalCasoViolacion;
    private LazyAsistenciaSedeDataModel asistenciaSedeLazyModel;
    private Long totalAsistenciaSede;

    private LazyFactorRiesgoSedeDataModel factorRiesgoAmbientalLazyModel;
    private Long totalFactorRiesgoAmbiental;
    private LazyFactorRiesgoSedeDataModel factorRiesgoSocialLazyModel;
    private Long totalFactorRiesgoSocial;

    private LazyProyectoInstitucionalDataModel proyectoInstitucionalLazyModel;
    private Long totalProyectos;
    private List<RevHistorico> historialProyectoInstitucional = new ArrayList();
    private SgProyectoInstitucionalSede proyectoEnEdicion;
    private SofisComboG<SgAnioLectivo> comboAnioLectivoProyectos;
    private SofisComboG<SgVelocidadInternet> comboVelocidadInternet;

    private SgOrganismoCESede organismoCE = new SgOrganismoCESede();
    private SofisComboG<SgOrganismoCoordinacionEscolar> comboOrganismo;
    private List<SgOrganismoCESede> listaOrganismos = new ArrayList<>();
    private Boolean tipoRiesgo;
    private List<SgTiposRiesgo> listaRiesgoOriginal = new ArrayList<>();
    private List<SgGradoRiesgo> listaGradoRiesgoOriginal = new ArrayList<>();
    private String tituloRiesgo;

    private List<SgDetalleMatricula> listDetalleMatricula=new ArrayList<>();
    private SgDetalleMatricula detalleMatriculaEnEdicion;
    private SgServicioEducativo servicioSeleccionado;
    private SofisComboG<SgAnioLectivo> comboAnioLectivoDetalleMatricula;
    private SofisComboG<SgNivel> comboNivelDetalleMatricula;

    private SofisComboG<SgImplementadora> comboImplementadora;

    private List<SgCasoViolacion> listCasoViolacion;    
    private List<SgAsistenciaSede> listAsistenciaSede;
    private List<SgFactorRiesgoSede> listFactorRiesgoSocial;
    private List<SgFactorRiesgoSede> listFactorRiesgoAmbiente;
    private SgOrganismoCESede organismoEnEdicion;
    private SgFactorRiesgoSede factorRiesgoEnEdicion;
    private SgFactorRiesgoSede factorRiesgoSocialEnEdicion;
    private SgAccionPrevenirEmbarazo prevenirEmbarazoEnEdicion;
    private SgAsistenciaSede asistenciaSedeEnEdicion;
    private SofisComboG<SgMotivoCierreSede> motivoCierreCombo;
    

    public SedeBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (sedeId != null && sedeId > 0) {
                if (sedeRev != null && sedeRev > 0) {
                    this.actualizar(restClient.obtenerEnRevision(sedeId, sedeRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    soloLectura = editable != null ? !editable : soloLectura;
                    this.actualizar(restClient.obtenerPorId(sedeId));
                }
            }
            validarAcceso();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_SEDES)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (sedeId == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_CENTRO_EDUCATIVO_OFICIAL)
                        && !sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_CENTRO_EDUCATIVO_PRIVADO)
                        && !sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_SEDE_CIRCULO_ALFABETIZACION)
                        && !sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_SEDE_CIRCULO_INFANCIA)
                        && !sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_EDUCAME)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                switch (entidadEnEdicion.getSedTipo()) {
                    case CED_OFI:
                        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_CENTRO_EDUCATIVO_OFICIAL)) {
                            JSFUtils.redirectToIndex();
                        }
                        if (!sessionBean.tieneOperacionEnContexto(ConstantesOperaciones.ACTUALIZAR_CENTRO_EDUCATIVO_OFICIAL, EnumAmbito.SEDE, sedeId)) {
                            soloLectura = Boolean.TRUE;
                        }
                        break;
                    case CED_PRI:
                        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_CENTRO_EDUCATIVO_PRIVADO)) {
                            JSFUtils.redirectToIndex();
                        }
                        if (!sessionBean.tieneOperacionEnContexto(ConstantesOperaciones.ACTUALIZAR_CENTRO_EDUCATIVO_PRIVADO, EnumAmbito.SEDE, sedeId)) {
                            soloLectura = Boolean.TRUE;
                        }
                        break;
                    case CIR_ALF:
                        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_SEDE_CIRCULO_ALFABETIZACION)) {
                            JSFUtils.redirectToIndex();
                        }
                        if (!sessionBean.tieneOperacionEnContexto(ConstantesOperaciones.ACTUALIZAR_SEDE_CIRCULO_ALFABETIZACION, EnumAmbito.SEDE, sedeId)) {
                            soloLectura = Boolean.TRUE;
                        }
                        break;
                    case CIR_INF:
                        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_SEDE_CIRCULO_INFANCIA)) {
                            JSFUtils.redirectToIndex();
                        }
                        if (!sessionBean.tieneOperacionEnContexto(ConstantesOperaciones.ACTUALIZAR_SEDE_CIRCULO_INFANCIA, EnumAmbito.SEDE, sedeId)) {
                            soloLectura = Boolean.TRUE;
                        }
                        break;
                    case UBI_EDUC:
                        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_EDUCAME)) {
                            JSFUtils.redirectToIndex();
                        }
                        if (!sessionBean.tieneOperacionEnContexto(ConstantesOperaciones.ACTUALIZAR_EDUCAME, EnumAmbito.SEDE, sedeId)) {
                            soloLectura = Boolean.TRUE;
                        }
                        break;
                    default:
                        JSFUtils.redirectToIndex();
                        break;
                }
            }
        }
    }

    public void updateAddressCoordinates() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String lat = params.get("latitude");
        String lng = params.get("longitude");
        SgDireccion dir = entidadEnEdicion.getSedDireccion();
        if (lat != null) {
            dir.setDirLatitud(Double.parseDouble(lat));
        }
        if (lng != null) {
            dir.setDirLongitud(Double.parseDouble(lng));
        }
    }

    public void solicitudesTraslado() {
        try {
            filtroSolicitud = new FiltroSolicitudTraslado();
            filtroSolicitud.setCentroEducativo(entidadEnEdicion.getSedPk());
            filtroSolicitud.setBuscarMismaSede(Boolean.TRUE);
            filtroSolicitud.setSotEstados(estados);
            filtroSolicitud.setOrderBy(new String[]{"sotEstudiante.estPersona.perNombreBusqueda"});
            filtroSolicitud.setAscending(new boolean[]{true});
            filtroSolicitud.setAnioLectivo(comboAnioLectivoSolicitudes.getSelectedT() != null ? comboAnioLectivoSolicitudes.getSelectedT().getAleAnio() : null);
            filtroSolicitud.setIncluirCampos(new String[]{"sotEstudiante.estPersona.perNie", "sotEstudiante.estPersona.perNombreBusqueda",
                "sotEstudiante.estPersona.perPrimerNombre", "sotEstudiante.estPersona.perSegundoNombre", "sotEstudiante.estPersona.perTercerNombre",
                "sotEstudiante.estPersona.perPrimerApellido", "sotEstudiante.estPersona.perSegundoApellido", "sotEstudiante.estPersona.perTercerApellido",
                "sotFechaSolicitud", "sotMotivoTraslado.motNombre",
                "sotSedeSolicitante.sedNombre", "sotSedeSolicitante.sedCodigo", "sotSedeSolicitante.sedTipo", "sotEstado"});

            //Obtener niñas
            filtroSolicitud.setSexo(Constantes.CODIGO_SEXO_FEMENINO);
            ninasSolicitudes = restSolicitudesTraslado.buscarTotal(filtroSolicitud);

            //Obtener niños
            filtroSolicitud.setSexo(Constantes.CODIGO_SEXO_MASCULINO);
            ninosSolicitudes = restSolicitudesTraslado.buscarTotal(filtroSolicitud);

            //Setear filtro por sexo
            filtroSolicitud.setSexo(null);
            totalSolicitudes = restSolicitudesTraslado.buscarTotal(filtroSolicitud);

            solicitudTrasladoLazyModel = new LazySolicitudTrasladoDataModel(restSolicitudesTraslado, filtroSolicitud, totalSolicitudes, paginado);

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void retiradosTraslado() {

        try {
            filtroSolicitud = new FiltroSolicitudTraslado();
            filtroSolicitud.setCentroEducativo(entidadEnEdicion.getSedPk());
            filtroSolicitud.setBuscarMismaSede(Boolean.FALSE);
            filtroSolicitud.setSotEstado(EnumEstadoSolicitudTraslado.CONFIRMADO);
            filtroSolicitud.setAnioLectivo(comboAnioLectivoRetirados.getSelectedT() != null ? comboAnioLectivoRetirados.getSelectedT().getAleAnio() : null);
            filtroSolicitud.setOrderBy(new String[]{"sotEstudiante.estPersona.perNombreBusqueda"});
            filtroSolicitud.setAscending(new boolean[]{true});
            filtroSolicitud.setIncluirCampos(new String[]{"sotEstudiante.estPersona.perNie",
                "sotEstudiante.estPersona.perNombreBusqueda",
                "sotEstudiante.estPersona.perPrimerNombre", "sotEstudiante.estPersona.perSegundoNombre", "sotEstudiante.estPersona.perTercerNombre",
                "sotEstudiante.estPersona.perPrimerApellido", "sotEstudiante.estPersona.perSegundoApellido", "sotEstudiante.estPersona.perTercerApellido",
                "sotFechaSolicitud", "sotMotivoTraslado.motNombre",
                "sotSedeSolicitante.sedNombre", "sotSedeSolicitante.sedCodigo", "sotSedeSolicitante.sedTipo", "sotEstado"});

            //Obtener niñas
            filtroSolicitud.setSexo(Constantes.CODIGO_SEXO_FEMENINO);
            ninasRetirados = restSolicitudesTraslado.buscarTotal(filtroSolicitud);

            //Obtener niños
            filtroSolicitud.setSexo(Constantes.CODIGO_SEXO_MASCULINO);
            ninosRetirados = restSolicitudesTraslado.buscarTotal(filtroSolicitud);

            //Setear filtro por sexo
            filtroSolicitud.setSexo(null);
            totalRetirados = restSolicitudesTraslado.buscarTotal(filtroSolicitud);
            totalRetiradosSinRepetir = ninasRetirados + ninosRetirados;

            solicitudRetiradosLazyModel = new LazySolicitudTrasladoDataModel(restSolicitudesTraslado, filtroSolicitud, totalRetirados, paginado);

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void matriculadosTraslado() {

        try {
            filtroSolicitud = new FiltroSolicitudTraslado();
            filtroSolicitud.setCentroEducativoDestino(entidadEnEdicion.getSedPk());
            filtroSolicitud.setBuscarMismaSede(Boolean.FALSE);
            // filtroSolicitud.setSotEstado(EnumEstadoSolicitudTraslado.CONFIRMADO);
            List<EnumEstadoSolicitudTraslado> estados = new ArrayList();
            estados.add(EnumEstadoSolicitudTraslado.CONFIRMADO);
            filtroSolicitud.setSotEstados(estados);
            filtroSolicitud.setAnioLectivo(comboAnioLectivoMatriculados.getSelectedT() != null ? comboAnioLectivoMatriculados.getSelectedT().getAleAnio() : null);
            filtroSolicitud.setOrderBy(new String[]{"sotEstudiante.estPersona.perNombreBusqueda"});
            filtroSolicitud.setAscending(new boolean[]{true});
            filtroSolicitud.setIncluirCampos(new String[]{"sotEstudiante.estPersona.perNie",
                "sotEstudiante.estPersona.perNombreBusqueda",
                "sotEstudiante.estPersona.perPrimerNombre", "sotEstudiante.estPersona.perSegundoNombre", "sotEstudiante.estPersona.perTercerNombre",
                "sotEstudiante.estPersona.perPrimerApellido", "sotEstudiante.estPersona.perSegundoApellido", "sotEstudiante.estPersona.perTercerApellido",
                "sotFechaSolicitud", "sotMotivoTraslado.motNombre",
                "sotSedeSolicitante.sedNombre", "sotSedeSolicitante.sedCodigo", "sotSedeSolicitante.sedTipo", "sotEstado"});

            //Obtener niñas
            filtroSolicitud.setSexo(Constantes.CODIGO_SEXO_FEMENINO);
            ninasMatriculados = restSolicitudesTraslado.buscarTotal(filtroSolicitud);

            //Obtener niños
            filtroSolicitud.setSexo(Constantes.CODIGO_SEXO_MASCULINO);
            ninosMatriculados = restSolicitudesTraslado.buscarTotal(filtroSolicitud);

            //Setear filtro por sexo
            filtroSolicitud.setSexo(null);
            totalMatriculados = restSolicitudesTraslado.buscarTotal(filtroSolicitud);
            totalMatriculadosSinRepetir = ninasMatriculados + ninosMatriculados;

            solicitudMatriculadosLazyModel = new LazySolicitudTrasladoDataModel(restSolicitudesTraslado, filtroSolicitud, totalMatriculados, paginado);

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiarFormulario() {
        comboTiposSede.setSelected(-1);
        comboClasificacion.setSelected(-1);
        tipoCalendarioCombo.setSelected(-1);
        tipoOrganismoAdministrativoCombo.setSelected(-1);
        comboImplementadora.setSelected(-1);
        entidadEnEdicion = null;
    }

    public void cargarCombos() {
        try {
            List<TipoSede> tipoSede = new ArrayList();

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_CENTRO_EDUCATIVO_PRIVADO)) {
                tipoSede.add(TipoSede.CED_PRI);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_CENTRO_EDUCATIVO_OFICIAL)) {
                tipoSede.add(TipoSede.CED_OFI);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_SEDE_CIRCULO_INFANCIA)) {
                tipoSede.add(TipoSede.CIR_INF);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_SEDE_CIRCULO_ALFABETIZACION)) {
                tipoSede.add(TipoSede.CIR_ALF);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_EDUCAME)) {
                tipoSede.add(TipoSede.UBI_EDUC);
            }

            comboTiposSede = new SofisComboG(tipoSede, "text");
            comboTiposSede.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"claNombre"});
            fc.setIncluirCampos(new String[]{"claNombre", "claVersion"});
            List<SgClasificacion> clasificaciones = restCatalogo.buscarClasificacion(fc);
            comboClasificacion = new SofisComboG(clasificaciones, "claNombre");
            comboClasificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"impOrden"});
            fc.setIncluirCampos(new String[]{"impNombre", "impVersion"});
            List<SgImplementadora> implementadoras = restCatalogo.buscarImplementadora(fc);
            comboImplementadora = new SofisComboG(implementadoras, "impNombre");
            comboImplementadora.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"jlaNombre"});
            fc.setIncluirCampos(new String[]{"jlaNombre", "jlaVersion"});
            jornadasLaborales = restCatalogo.buscarJornadasLaborales(fc);

            fc.setOrderBy(new String[]{"tceNombre"});
            fc.setIncluirCampos(new String[]{"tceNombre", "tceVersion"});
            List<SgTipoCalendario> listCalendario = tipoCalendarioRestClient.buscar(fc);
            tipoCalendarioCombo = new SofisComboG(listCalendario, "tceNombre");
            tipoCalendarioCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setIncluirCampos(new String[]{"sinNombre", "sinVersion"});
            fc.setOrderBy(new String[]{"sinNombre"});
            serviciosInfra = restCatalogo.buscarServiciosInfraestructura(fc);

            fc.setIncluirCampos(new String[]{"toaNombre", "toaVersion"});
            fc.setOrderBy(new String[]{"toaNombre"});
            List<SgTipoOrganismoAdministrativo> listaTipoOrg = restCatalogo.buscarTipoOrganismoAdministrativo(fc);
            tipoOrganismoAdministrativoCombo = new SofisComboG<>(listaTipoOrg, "toaNombre");
            tipoOrganismoAdministrativoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            List<SgTiposRiesgo> listaTipoRiesgo = restCatalogo.buscarTipoRiesgo(filtro);
            tipoRiesgoCombo = new SofisComboG<>(listaTipoRiesgo, "triNombre");
            tipoRiesgoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<SgGradoAfectacion> listaGradoAfectacion = restCatalogo.buscarGradoAfectacion(filtro);
            gradoAfectacionCombo = new SofisComboG<>(listaGradoAfectacion, "gafNombre");
            gradoAfectacionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumEstadoSolicitudTraslado> estadosSol = new ArrayList(Arrays.asList(EnumEstadoSolicitudTraslado.values()));
            comboEstadoTraslado = new SofisComboG(estadosSol, "text");
            comboEstadoTraslado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEstadoTraslado.ordenar();

            FiltroAnioLectivo fanio = new FiltroAnioLectivo();
            fanio.setAleSedeFk(entidadEnEdicion != null ? entidadEnEdicion.getSedPk() : null);
            fanio.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            fanio.setAscending(new boolean[]{false});
            fanio.setOrderBy(new String[]{"aleAnio"});
            List<SgAnioLectivo> anios = restAnio.buscar(fanio);

            comboAnioLectivoSolicitudes = new SofisComboG(new ArrayList(anios), "aleAnio");
            comboAnioLectivoSolicitudes.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboAnioLectivoRetirados = new SofisComboG(new ArrayList(anios), "aleAnio");
            comboAnioLectivoRetirados.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboAnioLectivoMatriculados = new SofisComboG(new ArrayList(anios), "aleAnio");
            comboAnioLectivoMatriculados.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboAnioLectivoDetalleMatricula= new SofisComboG(new ArrayList(anios), "aleAnio");
            comboAnioLectivoDetalleMatricula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboAnioLectivoOtros = new SofisComboG(new ArrayList(anios), "aleAnio");
            comboAnioLectivoOtros.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            for (SgAnioLectivo anio : anios) {
                if (anio.getAleEstado().equals(EnumAnioLectivoEstado.ABIERTO)) {
                    comboAnioLectivoOtros.setSelectedT(anio);
                    break;
                }
            }

            comboAnioLectivoProyectos = new SofisComboG(new ArrayList(anios), "aleAnio");
            comboAnioLectivoProyectos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            for (SgAnioLectivo a : anios) {
                if (a.getAleEstado().equals(EnumAnioLectivoEstado.ABIERTO)) {
                    comboAnioLectivoProyectos.setSelectedT(a);
                    break;
                }
            }

            comboAnioLectivoInsert = new SofisComboG(new ArrayList(anios), "aleAnio");
            comboAnioLectivoInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"tacNombre"});
            fc.setIncluirCampos(new String[]{"tacNombre", "tacNecesitaDescripcion", "tacVersion"});
            List<SgTipoAccion> lTiposAccion = restCatalogo.buscarTipoAccion(fc);
            comboTipoAccion = new SofisComboG(lTiposAccion, "tacNombre");
            comboTipoAccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroTiposApoyo fta=new FiltroTiposApoyo();
            fta.setHabilitado(Boolean.TRUE);
            fta.setAplicaSede(Boolean.TRUE);
            fta.setOrderBy(new String[]{"tapNombre"});
            fta.setIncluirCampos(new String[]{"tapNombre", "tapVersion"});
            List<SgTipoApoyo> lTipoApoyo = restCatalogo.buscarTipoApoyo(fta);
            comboTipoApoyo = new SofisComboG(lTipoApoyo, "tapNombre");
            comboTipoApoyo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"tprNombre"});
            fc.setIncluirCampos(new String[]{"tprNombre", "tprVersion"});
            List<SgTipoProveedor> lTipoProveedor = restCatalogo.buscarTipoProveedor(fc);
            comboTipoProveedor = new SofisComboG(lTipoProveedor, "tprNombre");
            comboTipoProveedor.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"triNombre"});
            fc.setIncluirCampos(new String[]{"triNombre", "triRiesgoAmbiental", "triRiesgoSocial", "triVersion"});
            listaRiesgoOriginal = restCatalogo.buscarTipoRiesgo(fc);

            fc.setOrderBy(new String[]{"greNombre"});
            fc.setIncluirCampos(new String[]{"greNombre", "greRiesgoAmbiental", "greRiesgoSocial", "greVersion"});
            listaGradoRiesgoOriginal = restCatalogo.buscarGradoRiesgo(fc);

            comboTipoRiesgo = new SofisComboG();
            comboTipoRiesgo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboGradoRiesgo = new SofisComboG();
            comboGradoRiesgo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"vinNombre"});
            fc.setIncluirCampos(new String[]{"vinNombre", "vinVersion"});
            List<SgVelocidadInternet> lVelocidad = restCatalogo.buscarVelocidadInternet(fc);
            comboVelocidadInternet = new SofisComboG(lVelocidad, "vinNombre");
            comboVelocidadInternet.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"oceNombre"});
            fc.setIncluirCampos(new String[]{"oceNombre", "oceVersion"});
            List<SgOrganismoCoordinacionEscolar> lOrganismo = restCatalogo.buscarOrganismoCoordinacionEscolar(fc);
            comboOrganismo = new SofisComboG(lOrganismo, "oceNombre");
            comboOrganismo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboNivelDetalleMatricula= new SofisComboG();
            comboNivelDetalleMatricula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            fc.setOrderBy(new String[]{"mcsNombre"});
            fc.setIncluirCampos(new String[]{"mcsNombre", "mcsVersion"});
            List<SgMotivoCierreSede> listCierre = restCatalogo.buscarMotivoCierreSede(fc);
            motivoCierreCombo= new SofisComboG(listCierre,"mcsNombre");
            motivoCierreCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() throws Exception {
        if (comboTiposSede.getSelectedT() != null) {
            switch (comboTiposSede.getSelectedT()) {
                case CED_OFI:
                    entidadEnEdicion = new SgCentroEducativoOficial();
                    entidadEnEdicion.setSedTipo(TipoSede.CED_OFI);
                    break;
                case CED_PRI:
                    entidadEnEdicion = new SgCentroEducativoPrivado();
                    entidadEnEdicion.setSedTipo(TipoSede.CED_PRI);
                    break;
                case CIR_ALF:
                    entidadEnEdicion = new SgCirculoAlfabetizacion();
                    entidadEnEdicion.setSedTipo(TipoSede.CIR_ALF);
                    
                    FiltroSedes filtro = new FiltroSedes();
                    filtro.setSedPk(this.sedePadreAlfabetizacionPk);
                    filtro.setIncluirCampos(new String[]{"sedTipo", "sedNombre", "sedCodigo", "sedVersion"});
                    List<SgSede> sedes = restClient.buscar(filtro);
                    if (!sedes.isEmpty()){
                        SgSede padre = sedes.get(0);
                        entidadEnEdicion.setSedSedeAdscritaDe(padre);
                    }
                    
                    break;
                case CIR_INF:
                    entidadEnEdicion = new SgCirculoInfancia();
                    entidadEnEdicion.setSedTipo(TipoSede.CIR_INF);
                    break;
                case UBI_EDUC:
                    entidadEnEdicion = new SgSedeEducame();
                    entidadEnEdicion.setSedTipo(TipoSede.UBI_EDUC);
                default:
                    break;
            }
        }
        this.entidadEnEdicion.actualizarServiciosInfra(serviciosInfra);

        sedesViewValidator = new SedesViewValidator(entidadEnEdicion);
        soloLectura = Boolean.FALSE;
    }

    public void actualizar(SgSede sede) {
        try {
            entidadEnEdicion = sede;
            if (entidadEnEdicion.getSedDireccion() == null) {
                entidadEnEdicion.setSedDireccion(new SgDireccion());
            }
            sedesViewValidator = new SedesViewValidator(entidadEnEdicion);
            tipoCalendarioCombo.setSelectedT(entidadEnEdicion.getSedTipoCalendario());
            comboClasificacion.setSelectedT(entidadEnEdicion.getSedClasificacion());
            comboVelocidadInternet.setSelectedT(entidadEnEdicion.getSedVelocidadInternet());
            comboImplementadora.setSelectedT(entidadEnEdicion.getSedImplementadora());
            
            if (BooleanUtils.isFalse(entidadEnEdicion.getSedActivo())){
                this.soloLectura = Boolean.TRUE;
            }

            if (entidadEnEdicion instanceof SgCentroEducativo) {
                SgCentroEducativo centro = (SgCentroEducativo) entidadEnEdicion;
                this.tipoOrganismoAdministrativoCombo.setSelectedT(centro.getCedTipoOrganismoAdministrativo());
            }
            this.entidadEnEdicion.actualizarServiciosInfra(serviciosInfra);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void activar(){
        try {
            restClient.activar(entidadEnEdicion.getSedPk());
            this.actualizar(restClient.obtenerPorId(entidadEnEdicion.getSedPk()));
            soloLectura = editable != null ? !editable : soloLectura;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setSedClasificacion(comboClasificacion.getSelectedT());
            entidadEnEdicion.setSedTipoCalendario(tipoCalendarioCombo.getSelectedT());
            entidadEnEdicion.setSedImplementadora(comboImplementadora!=null?comboImplementadora.getSelectedT():null);
            switch (entidadEnEdicion.getSedTipo()) {
                case CED_OFI:
                    entidadEnEdicion = cedOficialRestClient.guardar(entidadEnEdicion);
                    break;
                case CED_PRI:
                    entidadEnEdicion = cedPrivadoRestClient.guardar(entidadEnEdicion);
                    break;
                case CIR_ALF:
                    entidadEnEdicion = circuloAlfabetizacionRestClient.guardar(entidadEnEdicion);
                    break;
                case CIR_INF:
                    entidadEnEdicion = circuloInfanciaRestClient.guardar(entidadEnEdicion);
                    break;
                case UBI_EDUC:
                    entidadEnEdicion = educameRestClient.guardar(entidadEnEdicion);
                    break;
                default:
                    break;
            }
            sedesViewValidator = new SedesViewValidator(entidadEnEdicion);

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void prepararCierreSede(){
        motivoCierreCombo.setSelected(-1);
        entidadEnEdicion.setSedFechaCierre(null);
        entidadEnEdicion.setSedObservacionCierre(null);
        entidadEnEdicion.setSedMotivoCierre(null);
    }
    
    public void cerrarSede(){
        try{
            if(entidadEnEdicion!=null){
                entidadEnEdicion.setSedMotivoCierre(motivoCierreCombo.getSelectedT());
                restClient.cerrar(entidadEnEdicion);
                this.actualizar(restClient.obtenerPorId(sedeId));
                PrimeFaces.current().executeScript("PF('cerrarDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.CERRADO_SEDE_CORRECTO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgCerrarDialog", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgCerrarDialog", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            
        }
    }

    public String getTituloPagina() {
        if (this.sedeId == null) {
            return Etiquetas.getValue("nuevaSede");
        } else if (this.sedeRev != null) {
            return Etiquetas.getValue("historialSede") + " " + entidadEnEdicion.getSedNombre() + " (" + entidadEnEdicion.getSedUltModUsuario() + (entidadEnEdicion.getSedUltModFecha() != null ? (" " + this.appBean.getDateTimeFormater().format(entidadEnEdicion.getSedUltModFecha())) : "") + ")";
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verSede") + " " + entidadEnEdicion.getSedNombre();
        } else {
            return Etiquetas.getValue("edicionSede") + " " + entidadEnEdicion.getSedNombre();
        }
    }

    public void changeTab(TabChangeEvent event) {
        this.tabActiveId = event.getTab().getId();
        if (tabActiveId.equals("traslados")) {
            if(estados==null){
                estados=new ArrayList();
            }
            if (estados.isEmpty()) {
                estados.add(EnumEstadoSolicitudTraslado.PENDIENTE);
                estados.add(EnumEstadoSolicitudTraslado.PENDIENTE_RESOLUCION);
                estados.add(EnumEstadoSolicitudTraslado.AUTORIZADA);
                if (comboAnioLectivoSolicitudes.getSelectedT() == null) {
                    comboAnioLectivoSolicitudes.setSelected(1);
                }
                solicitudesTraslado();
            }
            if (comboAnioLectivoRetirados.getSelectedT() == null) {
                comboAnioLectivoRetirados.setSelected(1);
                retiradosTraslado();
            }
            if (comboAnioLectivoMatriculados.getSelectedT() == null) {
                comboAnioLectivoMatriculados.setSelected(1);
                matriculadosTraslado();
            }
        }
        if (tabActiveId.equals("otrosDatos")) {
            if (totalCasoViolacion == null) {
                buscarListas();
            }
            buscarFactorRiesgoAmbiental();
            buscarFactorRiesgoSocial();
        }
        if (tabActiveId.equals("beneficios")) {
            buscarProyectos();
        }
        PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
    }

    public void buscarListas() {
        try {
            buscarCasosViolacion();

            FiltroAccionPrevenirEmbarazo fAccionPrevenirEmbarazo = new FiltroAccionPrevenirEmbarazo();
            fAccionPrevenirEmbarazo.setApeAnio(comboAnioLectivoOtros.getSelectedT() != null ? comboAnioLectivoOtros.getSelectedT().getAlePk() : null);
            fAccionPrevenirEmbarazo.setApeSede(entidadEnEdicion.getSedPk());
            listaAccionPrevenirEmbarazo = accionPrevenirEmbarazoRestClient.buscar(fAccionPrevenirEmbarazo);

            FiltroOrganismoCESede forg = new FiltroOrganismoCESede();
            forg.setOcsSede(entidadEnEdicion.getSedPk());
            listaOrganismos = restOrganismo.buscar(forg);
            
            //TODO: cargar solo si centro privado y usuario tiene la op
            FiltroDetalleMatricula fdet= new FiltroDetalleMatricula();
            fdet.setSedeFk(entidadEnEdicion.getSedPk());
            listDetalleMatricula=new ArrayList(detalleMatriculaRestClient.buscar(fdet));
            
            cargarNivelDetalle();

            buscarAsistenciaSede();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarCasosViolacion() {
        try {
            FiltroCasoViolacion fCasoViolacion = new FiltroCasoViolacion();
            fCasoViolacion.setCviAnio(comboAnioLectivoOtros.getSelectedT() != null ? comboAnioLectivoOtros.getSelectedT().getAlePk() : null);
            fCasoViolacion.setCviSede(entidadEnEdicion.getSedPk());
            listCasoViolacion=casosViolacionRestClient.buscar(fCasoViolacion);

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarAsistenciaSede() {
        try {
            FiltroAsistenciaSede fAsistenciaSede = new FiltroAsistenciaSede();
            fAsistenciaSede.setAseAnio(comboAnioLectivoOtros.getSelectedT() != null ? comboAnioLectivoOtros.getSelectedT().getAlePk() : null);
            fAsistenciaSede.setAseSede(entidadEnEdicion.getSedPk());
            listAsistenciaSede=asistenciaSedeRestClient.buscar(fAsistenciaSede);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarFactorRiesgoAmbiental() {
        try {
            FiltroFactorRiesgoSede fFactorRiesgoSede = new FiltroFactorRiesgoSede();
            fFactorRiesgoSede.setFriSede(entidadEnEdicion.getSedPk());
            fFactorRiesgoSede.setFriRiesgoAmbiental(Boolean.TRUE);
            listFactorRiesgoAmbiente=factorRiesgoRestClient.buscar(fFactorRiesgoSede);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarFactorRiesgoSocial() {
        try {
            FiltroFactorRiesgoSede fFactorRiesgoSede = new FiltroFactorRiesgoSede();
            fFactorRiesgoSede.setFriSede(entidadEnEdicion.getSedPk());
            fFactorRiesgoSede.setFriRiesgoSocial(Boolean.TRUE);
            listFactorRiesgoSocial=factorRiesgoRestClient.buscar(fFactorRiesgoSede);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarProyectos() {
        try {
            FiltroProyectoInstitucionalSede filtroPro = new FiltroProyectoInstitucionalSede();
            filtroPro.setSede(entidadEnEdicion.getSedPk());
            filtroPro.setAnio(comboAnioLectivoProyectos.getSelected() != null ? comboAnioLectivoProyectos.getSelectedT().getAleAnio() : null);
            totalProyectos = restProyectoInstitucional.buscarTotal(filtroPro);
            proyectoInstitucionalLazyModel = new LazyProyectoInstitucionalDataModel(restProyectoInstitucional, filtroPro, totalProyectos, paginado);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setIncluirAdscritas(Boolean.FALSE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionarTipoOrganismoAdministrativo() {
        SgCentroEducativo cep = (SgCentroEducativo) entidadEnEdicion;
        cep.setCedTipoOrganismoAdministrativo(tipoOrganismoAdministrativoCombo.getSelectedT());
        entidadEnEdicion = cep;
    }

    public void seleccionarVelocidadInternet() {
        entidadEnEdicion.setSedVelocidadInternet(comboVelocidadInternet != null ? comboVelocidadInternet.getSelectedT() : null);
    }

    public void seleccionarAnioLectivoInsert() {
        this.anioSeleccionadoInsert = comboAnioLectivoInsert != null ? comboAnioLectivoInsert.getSelectedT() : null;
    }

    public void agregarCasoViolacion() {
        JSFUtils.limpiarMensajesError();
        casoViolacion = new SgCasoViolacion();
        comboAnioLectivoInsert.setSelected(-1);
        this.anioSeleccionadoInsert = null;
    }

    public void eliminarCasoViolacion(SgCasoViolacion elem) {
        try {
            casosViolacionRestClient.eliminar(elem.getCviPk());
            buscarCasosViolacion();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarCasoViolacionSede() {
        try {
            casoViolacion.setCviAnio(anioSeleccionadoInsert);
            casoViolacion.setCviSede(entidadEnEdicion);
            casoViolacion = casosViolacionRestClient.guardar(casoViolacion);

            buscarCasosViolacion();

            PrimeFaces.current().executeScript("PF('CasoViolacionDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgCasoViolacion", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgCasoViolacion", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarOrganismoCESede() {
        try {
            if (listaOrganismos == null) {
                listaOrganismos = new ArrayList<>();
            }

            organismoCE.setOcsSede(entidadEnEdicion);
            organismoCE.setOcsOrgCoordinacionEscolar(comboOrganismo != null ? comboOrganismo.getSelectedT() : null);
            if (BooleanUtils.isFalse(organismoCE.getOcsConsejoConsultivo())) {
                organismoCE.setOcsFuncionando(null);
            } else if (organismoCE.getOcsFuncionando() == null) {
                organismoCE.setOcsFuncionando(Boolean.FALSE);
            }

            organismoCE = restOrganismo.guardar(organismoCE);

            if (listaOrganismos.contains(organismoCE)) {
                listaOrganismos.set(listaOrganismos.indexOf(organismoCE), organismoCE);
            } else {
                listaOrganismos.add(organismoCE);
            }

            PrimeFaces.current().executeScript("PF('OrganismoCEDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgOrganismoCE", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgOrganismoCE", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarNivelDetalle(){
       try{
        FiltroNivel fn=new FiltroNivel();
        fn.setSedPk(entidadEnEdicion.getSedPk());
        List<SgNivel> niv=NivelRestClient.buscar(fn);

        comboNivelDetalleMatricula= new SofisComboG(niv, "nivNombre");
        comboNivelDetalleMatricula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
         } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
   
    
    public void agregarDetalleMatricula() {
        JSFUtils.limpiarMensajesError();
        servicioSeleccionado=null;
        detalleMatriculaEnEdicion = new SgDetalleMatricula();
        comboAnioLectivoDetalleMatricula.setSelected(-1);
        comboNivelDetalleMatricula.setSelected(-1);
    }
    
    public void editarDetalleMatricula(SgDetalleMatricula det) {
        detalleMatriculaEnEdicion=det;
        comboNivelDetalleMatricula.setSelectedT(det.getDemNivel());
        comboAnioLectivoDetalleMatricula.setSelectedT(det.getDemAnioLectivo());
    }
    
     public void guardarDetalleMatricula() {
            try {
                 detalleMatriculaEnEdicion.setDemAnioLectivo(comboAnioLectivoDetalleMatricula.getSelectedT());
                 detalleMatriculaEnEdicion.setDemSede(entidadEnEdicion);
                 detalleMatriculaEnEdicion.setDemNivel(comboNivelDetalleMatricula.getSelectedT());
                 detalleMatriculaEnEdicion=detalleMatriculaRestClient.guardar(detalleMatriculaEnEdicion);
                 listDetalleMatricula.add(detalleMatriculaEnEdicion);
                PrimeFaces.current().executeScript("PF('detalleMatriculaDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            } catch (BusinessException be) {
                JSFUtils.agregarMensajes("popupmsgDetalleMatricula", FacesMessage.SEVERITY_ERROR, be.getErrores());
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                JSFUtils.agregarError("popupmsgDetalleMatricula", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
     }
    
    public void eliminarDetalleMatricula(SgDetalleMatricula elem) {
        try {
            detalleMatriculaRestClient.eliminar(elem.getDemPk());
            this.listDetalleMatricula.remove(elem);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    

    public void agregarOrganismo() {
        JSFUtils.limpiarMensajesError();
        organismoCE = new SgOrganismoCESede();
        comboOrganismo.setSelected(-1);
    }

    
    public void actualizarOrganismo(SgOrganismoCESede elem){
        organismoEnEdicion=elem;
        JSFUtils.limpiarMensajesError();
    }
    
    public void eliminarOrganismo() {
        try {
            restOrganismo.eliminar(organismoEnEdicion.getOcsPk());
            this.listaOrganismos.remove(organismoEnEdicion);
            organismoEnEdicion=null;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarAccionPrevenirEmbarazo() {
        JSFUtils.limpiarMensajesError();
        accionPrevenirEmbarazo = new SgAccionPrevenirEmbarazo();
        comboAnioLectivoInsert.setSelected(-1);
        this.anioSeleccionadoInsert = null;
        comboTipoAccion.setSelected(-1);
    }
    
    public void actualizarAccionPrevenirEmbarazo(SgAccionPrevenirEmbarazo elem){
        prevenirEmbarazoEnEdicion=elem;
        JSFUtils.limpiarMensajesError();
    }

    public void eliminarAccionPrevenirEmbarazo() {
        try {
            accionPrevenirEmbarazoRestClient.eliminar(prevenirEmbarazoEnEdicion.getApePk());
            this.listaAccionPrevenirEmbarazo.remove(prevenirEmbarazoEnEdicion);
            prevenirEmbarazoEnEdicion=null;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarAccionPrevenirEmbarazoSede() {
        try {
            if (listaAccionPrevenirEmbarazo == null) {
                listaAccionPrevenirEmbarazo = new ArrayList<>();
            }

            accionPrevenirEmbarazo.setApeAnio(anioSeleccionadoInsert);
            accionPrevenirEmbarazo.setApeTipoAccion(comboTipoAccion.getSelectedT());
            accionPrevenirEmbarazo.setApeSede(entidadEnEdicion);
            accionPrevenirEmbarazo = accionPrevenirEmbarazoRestClient.guardar(accionPrevenirEmbarazo);

            if (comboAnioLectivoOtros.getSelectedT() != null && comboAnioLectivoOtros.getSelectedT().equals(anioSeleccionadoInsert)) {
                if (listaAccionPrevenirEmbarazo.contains(accionPrevenirEmbarazo)) {
                    listaAccionPrevenirEmbarazo.set(listaAccionPrevenirEmbarazo.indexOf(accionPrevenirEmbarazo), accionPrevenirEmbarazo);
                } else {
                    listaAccionPrevenirEmbarazo.add(accionPrevenirEmbarazo);
                }
            }

            PrimeFaces.current().executeScript("PF('AccionPrevenirEmbarazoDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgAccionPrevenirEmbarazo", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgAccionPrevenirEmbarazo", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarAsistencia() {
        JSFUtils.limpiarMensajesError();
        asistenciaSede = new SgAsistenciaSede();
        comboAnioLectivoInsert.setSelected(-1);
        this.anioSeleccionadoInsert = null;
        comboTipoApoyo.setSelected(-1);
        comboTipoProveedor.setSelected(-1);
    }
    
    public void actualizarAsistenciaSede(SgAsistenciaSede elem){
        asistenciaSedeEnEdicion=elem;
        JSFUtils.limpiarMensajesError();
    }

    public void eliminarAsistencia() {
        try {
            asistenciaSedeRestClient.eliminar(asistenciaSedeEnEdicion.getAsePk());
            buscarAsistenciaSede();
            asistenciaSedeEnEdicion=null;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarAsistenciaSede() {
        try {

            asistenciaSede.setAseAnio(anioSeleccionadoInsert);
            asistenciaSede.setAseTipoApoyo(comboTipoApoyo.getSelectedT());
            asistenciaSede.setAseTipoProveedor(comboTipoProveedor.getSelectedT());
            asistenciaSede.setAseSede(entidadEnEdicion);
            asistenciaSede = asistenciaSedeRestClient.guardar(asistenciaSede);

            buscarAsistenciaSede();

            PrimeFaces.current().executeScript("PF('AsistenciaDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgAsistencia", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgAsistencia", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarFactorRiesgoAmbiental() {
        //Ambiental
        tipoRiesgo = Boolean.TRUE;
        tituloRiesgo = Etiquetas.getValue("agregarFactorRiesgoAmbiental");

        comboTipoRiesgo = new SofisComboG(listaRiesgoOriginal.stream()
                .filter((r) -> BooleanUtils.isTrue(r.getTriRiesgoAmbiental()))
                .collect(Collectors.toList()), "triNombre");
        comboTipoRiesgo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboGradoRiesgo = new SofisComboG(listaGradoRiesgoOriginal.stream()
                .filter((g) -> BooleanUtils.isTrue(g.getGreRiesgoAmbiental()))
                .collect(Collectors.toList()), "greNombre");
        comboGradoRiesgo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        JSFUtils.limpiarMensajesError();
        factorRiesgoSede = new SgFactorRiesgoSede();
        comboAnioLectivoInsert.setSelected(-1);
        this.anioSeleccionadoInsert = null;
        comboTipoRiesgo.setSelected(-1);
        comboGradoRiesgo.setSelected(-1);
    }

    public void agregarFactorRiesgoSocial() {
        //Social
        tipoRiesgo = Boolean.FALSE;
        tituloRiesgo = Etiquetas.getValue("agregarFactorRiesgoSocial");

        comboTipoRiesgo = new SofisComboG(listaRiesgoOriginal.stream()
                .filter((r) -> BooleanUtils.isTrue(r.getTriRiesgoSocial()))
                .collect(Collectors.toList()), "triNombre");
        comboTipoRiesgo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboGradoRiesgo = new SofisComboG(listaGradoRiesgoOriginal.stream()
                .filter((g) -> BooleanUtils.isTrue(g.getGreRiesgoSocial()))
                .collect(Collectors.toList()), "greNombre");
        comboGradoRiesgo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        JSFUtils.limpiarMensajesError();
        factorRiesgoSede = new SgFactorRiesgoSede();
        comboAnioLectivoInsert.setSelected(-1);
        this.anioSeleccionadoInsert = null;
        comboTipoRiesgo.setSelected(-1);
        comboGradoRiesgo.setSelected(-1);
    }
    
    public void actualizarFactorRiesgoAmbiental(SgFactorRiesgoSede elem){
        factorRiesgoEnEdicion=elem;
        JSFUtils.limpiarMensajesError();
    }

    public void eliminarFactorRiesgoAmbiental() {
        try {
            factorRiesgoRestClient.eliminar(factorRiesgoEnEdicion.getFriPk());
            buscarFactorRiesgoAmbiental();
            factorRiesgoEnEdicion=null;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    
    public void actualizarRiesgoSocial(SgFactorRiesgoSede elem){
        factorRiesgoSocialEnEdicion=elem;
        JSFUtils.limpiarMensajesError();
    }
    
    public void eliminarFactorRiesgoSocial() {
        try {
            factorRiesgoRestClient.eliminar(factorRiesgoSocialEnEdicion.getFriPk());
            buscarFactorRiesgoSocial();
            factorRiesgoSocialEnEdicion=null;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarFactorRiesgoSede() {
        try {
            factorRiesgoSede.setFriTipoRiesgo(comboTipoRiesgo.getSelectedT());
            factorRiesgoSede.setFriGradoRiesgo(comboGradoRiesgo.getSelectedT());
            factorRiesgoSede.setFriSede(entidadEnEdicion);
            factorRiesgoSede = factorRiesgoRestClient.guardar(factorRiesgoSede);

            if (tipoRiesgo) {
                buscarFactorRiesgoAmbiental();
                PrimeFaces.current().ajax().update("form:tabs:pnlFactorRiesgoAmbiental");
            } else {
                buscarFactorRiesgoSocial();
                PrimeFaces.current().ajax().update("form:tabs:pnlFactorRiesgoSocial");
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('FactorRiesgoDialog').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgFactorRiesgo", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgFactorRiesgo", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<EnumEstadoSolicitudTraslado> completeEstadoSolicitud(String query) {
        try {
            List<EnumEstadoSolicitudTraslado> estadosSol = new ArrayList(Arrays.asList(EnumEstadoSolicitudTraslado.values()));
            return estadosSol.stream().filter(x -> x.getText().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void editarFactorRiesgoSocial(SgRelSedeRiesgoAfectacion elem) {
        factorRiesgoSocial = (SgRelSedeRiesgoAfectacion) SerializationUtils.clone(elem);
        tipoRiesgoCombo.setSelectedT(elem.getRarTipoRiesgo());
        gradoAfectacionCombo.setSelectedT(elem.getRarGradoAfectacion());
    }

    public void eliminarFactorRiesgoSocial(SgRelSedeRiesgoAfectacion elem) {
        this.entidadEnEdicion.getSedFactoresRiesgoSocial().remove(elem);
    }

    public String historialPIS(Long id) {
        try {
            historialProyectoInstitucional = restProyectoInstitucional.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void actualizarPIS(SgProyectoInstitucionalSede var) {
        proyectoEnEdicion = (SgProyectoInstitucionalSede) SerializationUtils.clone(var);
    }

    public void eliminarPIS() {
        try {
            restProyectoInstitucional.eliminar(proyectoEnEdicion.getProPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            proyectoEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public SgSede getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSede entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<TipoSede> getComboTiposSede() {
        return comboTiposSede;
    }

    public void setComboTiposSede(SofisComboG<TipoSede> comboTiposSede) {
        this.comboTiposSede = comboTiposSede;
    }

    public SofisComboG<SgClasificacion> getComboClasificacion() {
        return comboClasificacion;
    }

    public void setComboClasificacion(SofisComboG<SgClasificacion> comboClasificacion) {
        this.comboClasificacion = comboClasificacion;
    }

    public SedesViewValidator getSedesViewValidator() {
        return sedesViewValidator;
    }

    public void setSedesViewValidator(SedesViewValidator sedesViewValidator) {
        this.sedesViewValidator = sedesViewValidator;
    }

    public List<SgJornadaLaboral> getJornadasLaborales() {
        return jornadasLaborales;
    }

    public void setJornadasLaborales(List<SgJornadaLaboral> jornadasLaborales) {
        this.jornadasLaborales = jornadasLaborales;
    }

    public Long getSedeRev() {
        return sedeRev;
    }

    public SofisComboG<SgTipoCalendario> getTipoCalendarioCombo() {
        return tipoCalendarioCombo;
    }

    public void setTipoCalendarioCombo(SofisComboG<SgTipoCalendario> tipoCalendarioCombo) {
        this.tipoCalendarioCombo = tipoCalendarioCombo;
    }

    public SofisComboG<SgTipoOrganismoAdministrativo> getTipoOrganismoAdministrativoCombo() {
        return tipoOrganismoAdministrativoCombo;
    }

    public void setTipoOrganismoAdministrativoCombo(SofisComboG<SgTipoOrganismoAdministrativo> tipoOrganismoAdministrativoCombo) {
        this.tipoOrganismoAdministrativoCombo = tipoOrganismoAdministrativoCombo;
    }

    public String getTabActiveId() {
        return tabActiveId;
    }

    public void setTabActiveId(String tabActiveId) {
        this.tabActiveId = tabActiveId;
    }

    public SgRelSedeRiesgoAfectacion getFactorRiesgoSocial() {
        return factorRiesgoSocial;
    }

    public void setFactorRiesgoSocial(SgRelSedeRiesgoAfectacion factorRiesgoSocial) {
        this.factorRiesgoSocial = factorRiesgoSocial;
    }

    public SofisComboG<SgTiposRiesgo> getTipoRiesgoCombo() {
        return tipoRiesgoCombo;
    }

    public void setTipoRiesgoCombo(SofisComboG<SgTiposRiesgo> tipoRiesgoCombo) {
        this.tipoRiesgoCombo = tipoRiesgoCombo;
    }

    public SofisComboG<SgGradoAfectacion> getGradoAfectacionCombo() {
        return gradoAfectacionCombo;
    }

    public void setGradoAfectacionCombo(SofisComboG<SgGradoAfectacion> gradoAfectacionCombo) {
        this.gradoAfectacionCombo = gradoAfectacionCombo;
    }

    public SedeRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(SedeRestClient restClient) {
        this.restClient = restClient;
    }

    public CentroEducativoOficialRestClient getCedOficialRestClient() {
        return cedOficialRestClient;
    }

    public void setCedOficialRestClient(CentroEducativoOficialRestClient cedOficialRestClient) {
        this.cedOficialRestClient = cedOficialRestClient;
    }

    public CentroEducativoPrivadoRestClient getCedPrivadoRestClient() {
        return cedPrivadoRestClient;
    }

    public void setCedPrivadoRestClient(CentroEducativoPrivadoRestClient cedPrivadoRestClient) {
        this.cedPrivadoRestClient = cedPrivadoRestClient;
    }

    public CirculoAlfabetizacionRestClient getCirculoAlfabetizacionRestClient() {
        return circuloAlfabetizacionRestClient;
    }

    public void setCirculoAlfabetizacionRestClient(CirculoAlfabetizacionRestClient circuloAlfabetizacionRestClient) {
        this.circuloAlfabetizacionRestClient = circuloAlfabetizacionRestClient;
    }

    public CirculoInfanciaRestClient getCirculoInfanciaRestClient() {
        return circuloInfanciaRestClient;
    }

    public void setCirculoInfanciaRestClient(CirculoInfanciaRestClient circuloInfanciaRestClient) {
        this.circuloInfanciaRestClient = circuloInfanciaRestClient;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public ApplicationBean getAppBean() {
        return appBean;
    }

    public void setAppBean(ApplicationBean appBean) {
        this.appBean = appBean;
    }

    public TipoCalendarioRestClient getTipoCalendarioRestClient() {
        return tipoCalendarioRestClient;
    }

    public void setTipoCalendarioRestClient(TipoCalendarioRestClient tipoCalendarioRestClient) {
        this.tipoCalendarioRestClient = tipoCalendarioRestClient;
    }

    public SedeEducameRestClient getEducameRestClient() {
        return educameRestClient;
    }

    public void setEducameRestClient(SedeEducameRestClient educameRestClient) {
        this.educameRestClient = educameRestClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public RelSedeRiesgoAfectacionRestClient getRelSedeRiesgoAfectacion() {
        return relSedeRiesgoAfectacion;
    }

    public void setRelSedeRiesgoAfectacion(RelSedeRiesgoAfectacionRestClient relSedeRiesgoAfectacion) {
        this.relSedeRiesgoAfectacion = relSedeRiesgoAfectacion;
    }

    public Long getSedeId() {
        return sedeId;
    }

    public void setSedeId(Long sedeId) {
        this.sedeId = sedeId;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }

    public List<SgServicioInfraestructura> getServiciosInfra() {
        return serviciosInfra;
    }

    public void setServiciosInfra(List<SgServicioInfraestructura> serviciosInfra) {
        this.serviciosInfra = serviciosInfra;
    }

    public SofisComboG<EnumEstadoSolicitudTraslado> getComboEstadoTraslado() {
        return comboEstadoTraslado;
    }

    public void setComboEstadoTraslado(SofisComboG<EnumEstadoSolicitudTraslado> comboEstadoTraslado) {
        this.comboEstadoTraslado = comboEstadoTraslado;
    }

    public AnioLectivoRestClient getRestAnio() {
        return restAnio;
    }

    public void setRestAnio(AnioLectivoRestClient restAnio) {
        this.restAnio = restAnio;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivoSolicitudes() {
        return comboAnioLectivoSolicitudes;
    }

    public void setComboAnioLectivoSolicitudes(SofisComboG<SgAnioLectivo> comboAnioLectivoSolicitudes) {
        this.comboAnioLectivoSolicitudes = comboAnioLectivoSolicitudes;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivoRetirados() {
        return comboAnioLectivoRetirados;
    }

    public void setComboAnioLectivoRetirados(SofisComboG<SgAnioLectivo> comboAnioLectivoRetirados) {
        this.comboAnioLectivoRetirados = comboAnioLectivoRetirados;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivoMatriculados() {
        return comboAnioLectivoMatriculados;
    }

    public void setComboAnioLectivoMatriculados(SofisComboG<SgAnioLectivo> comboAnioLectivoMatriculados) {
        this.comboAnioLectivoMatriculados = comboAnioLectivoMatriculados;
    }

    public SolicitudTrasladoRestClient getRestSolicitudesTraslado() {
        return restSolicitudesTraslado;
    }

    public void setRestSolicitudesTraslado(SolicitudTrasladoRestClient restSolicitudesTraslado) {
        this.restSolicitudesTraslado = restSolicitudesTraslado;
    }

    public FiltroSolicitudTraslado getFiltroSolicitud() {
        return filtroSolicitud;
    }

    public void setFiltroSolicitud(FiltroSolicitudTraslado filtroSolicitud) {
        this.filtroSolicitud = filtroSolicitud;
    }

    public Long getTotalSolicitudes() {
        return totalSolicitudes;
    }

    public void setTotalSolicitudes(Long totalSolicitudes) {
        this.totalSolicitudes = totalSolicitudes;
    }

    public Long getNinosSolicitudes() {
        return ninosSolicitudes;
    }

    public void setNinosSolicitudes(Long ninosSolicitudes) {
        this.ninosSolicitudes = ninosSolicitudes;
    }

    public Long getNinasSolicitudes() {
        return ninasSolicitudes;
    }

    public void setNinasSolicitudes(Long ninasSolicitudes) {
        this.ninasSolicitudes = ninasSolicitudes;
    }

    public Long getTotalRetirados() {
        return totalRetirados;
    }

    public void setTotalRetirados(Long totalRetirados) {
        this.totalRetirados = totalRetirados;
    }

    public Long getNinosRetirados() {
        return ninosRetirados;
    }

    public void setNinosRetirados(Long ninosRetirados) {
        this.ninosRetirados = ninosRetirados;
    }

    public Long getNinasRetirados() {
        return ninasRetirados;
    }

    public void setNinasRetirados(Long ninasRetirados) {
        this.ninasRetirados = ninasRetirados;
    }

    public Long getTotalMatriculados() {
        return totalMatriculados;
    }

    public void setTotalMatriculados(Long totalMatriculados) {
        this.totalMatriculados = totalMatriculados;
    }

    public Long getNinosMatriculados() {
        return ninosMatriculados;
    }

    public void setNinosMatriculados(Long ninosMatriculados) {
        this.ninosMatriculados = ninosMatriculados;
    }

    public Long getNinasMatriculados() {
        return ninasMatriculados;
    }

    public void setNinasMatriculados(Long ninasMatriculados) {
        this.ninasMatriculados = ninasMatriculados;
    }

    public LazySolicitudTrasladoDataModel getSolicitudTrasladoLazyModel() {
        return solicitudTrasladoLazyModel;
    }

    public void setSolicitudTrasladoLazyModel(LazySolicitudTrasladoDataModel solicitudTrasladoLazyModel) {
        this.solicitudTrasladoLazyModel = solicitudTrasladoLazyModel;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public LazySolicitudTrasladoDataModel getSolicitudRetiradosLazyModel() {
        return solicitudRetiradosLazyModel;
    }

    public void setSolicitudRetiradosLazyModel(LazySolicitudTrasladoDataModel solicitudRetiradosLazyModel) {
        this.solicitudRetiradosLazyModel = solicitudRetiradosLazyModel;
    }

    public LazySolicitudTrasladoDataModel getSolicitudMatriculadosLazyModel() {
        return solicitudMatriculadosLazyModel;
    }

    public void setSolicitudMatriculadosLazyModel(LazySolicitudTrasladoDataModel solicitudMatriculadosLazyModel) {
        this.solicitudMatriculadosLazyModel = solicitudMatriculadosLazyModel;
    }

    public Long getTotalRetiradosSinRepetir() {
        return totalRetiradosSinRepetir;
    }

    public void setTotalRetiradosSinRepetir(Long totalRetiradosSinRepetir) {
        this.totalRetiradosSinRepetir = totalRetiradosSinRepetir;
    }

    public Long getTotalMatriculadosSinRepetir() {
        return totalMatriculadosSinRepetir;
    }

    public void setTotalMatriculadosSinRepetir(Long totalMatriculadosSinRepetir) {
        this.totalMatriculadosSinRepetir = totalMatriculadosSinRepetir;
    }

    public List<EnumEstadoSolicitudTraslado> getEstados() {
        return estados;
    }

    public void setEstados(List<EnumEstadoSolicitudTraslado> estados) {
        this.estados = estados;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivoOtros() {
        return comboAnioLectivoOtros;
    }

    public void setComboAnioLectivoOtros(SofisComboG<SgAnioLectivo> comboAnioLectivoOtros) {
        this.comboAnioLectivoOtros = comboAnioLectivoOtros;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivoInsert() {
        return comboAnioLectivoInsert;
    }

    public void setComboAnioLectivoInsert(SofisComboG<SgAnioLectivo> comboAnioLectivoInsert) {
        this.comboAnioLectivoInsert = comboAnioLectivoInsert;
    }

    public SgCasoViolacion getCasoViolacion() {
        return casoViolacion;
    }

    public void setCasoViolacion(SgCasoViolacion casoViolacion) {
        this.casoViolacion = casoViolacion;
    }

    public CasoViolacionRestClient getCasosViolacionRestClient() {
        return casosViolacionRestClient;
    }

    public void setCasosViolacionRestClient(CasoViolacionRestClient casosViolacionRestClient) {
        this.casosViolacionRestClient = casosViolacionRestClient;
    }

    public AccionPrevenirEmbarazoRestClient getAccionPrevenirEmbarazoRestClient() {
        return accionPrevenirEmbarazoRestClient;
    }

    public void setAccionPrevenirEmbarazoRestClient(AccionPrevenirEmbarazoRestClient accionPrevenirEmbarazoRestClient) {
        this.accionPrevenirEmbarazoRestClient = accionPrevenirEmbarazoRestClient;
    }

    public AsistenciaSedeRestClient getAsistenciaSedeRestClient() {
        return asistenciaSedeRestClient;
    }

    public void setAsistenciaSedeRestClient(AsistenciaSedeRestClient asistenciaSedeRestClient) {
        this.asistenciaSedeRestClient = asistenciaSedeRestClient;
    }

    public FactorRiesgoSedeRestClient getFactorRiesgoRestClient() {
        return factorRiesgoRestClient;
    }

    public void setFactorRiesgoRestClient(FactorRiesgoSedeRestClient factorRiesgoRestClient) {
        this.factorRiesgoRestClient = factorRiesgoRestClient;
    }

    public SgAccionPrevenirEmbarazo getAccionPrevenirEmbarazo() {
        return accionPrevenirEmbarazo;
    }

    public void setAccionPrevenirEmbarazo(SgAccionPrevenirEmbarazo accionPrevenirEmbarazo) {
        this.accionPrevenirEmbarazo = accionPrevenirEmbarazo;
    }

    public SgAsistenciaSede getAsistenciaSede() {
        return asistenciaSede;
    }

    public void setAsistenciaSede(SgAsistenciaSede asistenciaSede) {
        this.asistenciaSede = asistenciaSede;
    }

    public SgFactorRiesgoSede getFactorRiesgoSede() {
        return factorRiesgoSede;
    }

    public void setFactorRiesgoSede(SgFactorRiesgoSede factorRiesgoSede) {
        this.factorRiesgoSede = factorRiesgoSede;
    }

    public SofisComboG<SgTipoAccion> getComboTipoAccion() {
        return comboTipoAccion;
    }

    public void setComboTipoAccion(SofisComboG<SgTipoAccion> comboTipoAccion) {
        this.comboTipoAccion = comboTipoAccion;
    }

    public SofisComboG<SgTipoApoyo> getComboTipoApoyo() {
        return comboTipoApoyo;
    }

    public void setComboTipoApoyo(SofisComboG<SgTipoApoyo> comboTipoApoyo) {
        this.comboTipoApoyo = comboTipoApoyo;
    }

    public SofisComboG<SgTipoProveedor> getComboTipoProveedor() {
        return comboTipoProveedor;
    }

    public void setComboTipoProveedor(SofisComboG<SgTipoProveedor> comboTipoProveedor) {
        this.comboTipoProveedor = comboTipoProveedor;
    }

    public SofisComboG<SgTiposRiesgo> getComboTipoRiesgo() {
        return comboTipoRiesgo;
    }

    public void setComboTipoRiesgo(SofisComboG<SgTiposRiesgo> comboTipoRiesgo) {
        this.comboTipoRiesgo = comboTipoRiesgo;
    }

    public SofisComboG<SgGradoRiesgo> getComboGradoRiesgo() {
        return comboGradoRiesgo;
    }

    public void setComboGradoRiesgo(SofisComboG<SgGradoRiesgo> comboGradoRiesgo) {
        this.comboGradoRiesgo = comboGradoRiesgo;
    }

    public List<SgAccionPrevenirEmbarazo> getListaAccionPrevenirEmbarazo() {
        return listaAccionPrevenirEmbarazo;
    }

    public void setListaAccionPrevenirEmbarazo(List<SgAccionPrevenirEmbarazo> listaAccionPrevenirEmbarazo) {
        this.listaAccionPrevenirEmbarazo = listaAccionPrevenirEmbarazo;
    }

    public SgAnioLectivo getAnioSeleccionadoInsert() {
        return anioSeleccionadoInsert;
    }

    public void setAnioSeleccionadoInsert(SgAnioLectivo anioSeleccionadoInsert) {
        this.anioSeleccionadoInsert = anioSeleccionadoInsert;
    }

    public LazyCasoViolacionDataModel getCasoViolacionLazyModel() {
        return casoViolacionLazyModel;
    }

    public void setCasoViolacionLazyModel(LazyCasoViolacionDataModel casoViolacionLazyModel) {
        this.casoViolacionLazyModel = casoViolacionLazyModel;
    }

    public Long getTotalCasoViolacion() {
        return totalCasoViolacion;
    }

    public void setTotalCasoViolacion(Long totalCasoViolacion) {
        this.totalCasoViolacion = totalCasoViolacion;
    }

    public LazyAsistenciaSedeDataModel getAsistenciaSedeLazyModel() {
        return asistenciaSedeLazyModel;
    }

    public void setAsistenciaSedeLazyModel(LazyAsistenciaSedeDataModel asistenciaSedeLazyModel) {
        this.asistenciaSedeLazyModel = asistenciaSedeLazyModel;
    }

    public Long getTotalAsistenciaSede() {
        return totalAsistenciaSede;
    }

    public void setTotalAsistenciaSede(Long totalAsistenciaSede) {
        this.totalAsistenciaSede = totalAsistenciaSede;
    }

    public LazyFactorRiesgoSedeDataModel getFactorRiesgoAmbientalLazyModel() {
        return factorRiesgoAmbientalLazyModel;
    }

    public void setFactorRiesgoAmbientalLazyModel(LazyFactorRiesgoSedeDataModel factorRiesgoAmbientalLazyModel) {
        this.factorRiesgoAmbientalLazyModel = factorRiesgoAmbientalLazyModel;
    }

    public Long getTotalFactorRiesgoAmbiental() {
        return totalFactorRiesgoAmbiental;
    }

    public void setTotalFactorRiesgoAmbiental(Long totalFactorRiesgoAmbiental) {
        this.totalFactorRiesgoAmbiental = totalFactorRiesgoAmbiental;
    }

    public LazyFactorRiesgoSedeDataModel getFactorRiesgoSocialLazyModel() {
        return factorRiesgoSocialLazyModel;
    }

    public void setFactorRiesgoSocialLazyModel(LazyFactorRiesgoSedeDataModel factorRiesgoSocialLazyModel) {
        this.factorRiesgoSocialLazyModel = factorRiesgoSocialLazyModel;
    }

    public Long getTotalFactorRiesgoSocial() {
        return totalFactorRiesgoSocial;
    }

    public void setTotalFactorRiesgoSocial(Long totalFactorRiesgoSocial) {
        this.totalFactorRiesgoSocial = totalFactorRiesgoSocial;
    }

    public LazyProyectoInstitucionalDataModel getProyectoInstitucionalLazyModel() {
        return proyectoInstitucionalLazyModel;
    }

    public void setProyectoInstitucionalLazyModel(LazyProyectoInstitucionalDataModel proyectoInstitucionalLazyModel) {
        this.proyectoInstitucionalLazyModel = proyectoInstitucionalLazyModel;
    }

    public Long getTotalProyectos() {
        return totalProyectos;
    }

    public void setTotalProyectos(Long totalProyectos) {
        this.totalProyectos = totalProyectos;
    }

    public List<RevHistorico> getHistorialProyectoInstitucional() {
        return historialProyectoInstitucional;
    }

    public void setHistorialProyectoInstitucional(List<RevHistorico> historialProyectoInstitucional) {
        this.historialProyectoInstitucional = historialProyectoInstitucional;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivoProyectos() {
        return comboAnioLectivoProyectos;
    }

    public void setComboAnioLectivoProyectos(SofisComboG<SgAnioLectivo> comboAnioLectivoProyectos) {
        this.comboAnioLectivoProyectos = comboAnioLectivoProyectos;
    }

    public SofisComboG<SgVelocidadInternet> getComboVelocidadInternet() {
        return comboVelocidadInternet;
    }

    public void setComboVelocidadInternet(SofisComboG<SgVelocidadInternet> comboVelocidadInternet) {
        this.comboVelocidadInternet = comboVelocidadInternet;
    }

    public SgOrganismoCESede getOrganismoCE() {
        return organismoCE;
    }

    public void setOrganismoCE(SgOrganismoCESede organismoCE) {
        this.organismoCE = organismoCE;
    }

    public SofisComboG<SgOrganismoCoordinacionEscolar> getComboOrganismo() {
        return comboOrganismo;
    }

    public void setComboOrganismo(SofisComboG<SgOrganismoCoordinacionEscolar> comboOrganismo) {
        this.comboOrganismo = comboOrganismo;
    }

    public List<SgOrganismoCESede> getListaOrganismos() {
        return listaOrganismos;
    }

    public void setListaOrganismos(List<SgOrganismoCESede> listaOrganismos) {
        this.listaOrganismos = listaOrganismos;
    }

    public Boolean getTipoRiesgo() {
        return tipoRiesgo;
    }

    public void setTipoRiesgo(Boolean tipoRiesgo) {
        this.tipoRiesgo = tipoRiesgo;
    }

    public List<SgTiposRiesgo> getListaRiesgoOriginal() {
        return listaRiesgoOriginal;
    }

    public void setListaRiesgoOriginal(List<SgTiposRiesgo> listaRiesgoOriginal) {
        this.listaRiesgoOriginal = listaRiesgoOriginal;
    }

    public List<SgGradoRiesgo> getListaGradoRiesgoOriginal() {
        return listaGradoRiesgoOriginal;
    }

    public void setListaGradoRiesgoOriginal(List<SgGradoRiesgo> listaGradoRiesgoOriginal) {
        this.listaGradoRiesgoOriginal = listaGradoRiesgoOriginal;
    }

    public String getTituloRiesgo() {
        return tituloRiesgo;
    }

    public void setTituloRiesgo(String tituloRiesgo) {
        this.tituloRiesgo = tituloRiesgo;
    }

    public List<SgDetalleMatricula> getListDetalleMatricula() {
        return listDetalleMatricula;
    }

    public void setListDetalleMatricula(List<SgDetalleMatricula> listDetalleMatricula) {
        this.listDetalleMatricula = listDetalleMatricula;
    }

    public SgDetalleMatricula getDetalleMatriculaEnEdicion() {
        return detalleMatriculaEnEdicion;
    }

    public void setDetalleMatriculaEnEdicion(SgDetalleMatricula detalleMatriculaEnEdicion) {
        this.detalleMatriculaEnEdicion = detalleMatriculaEnEdicion;
    }

    public SgServicioEducativo getServicioSeleccionado() {
        return servicioSeleccionado;
    }

    public void setServicioSeleccionado(SgServicioEducativo servicioSeleccionado) {
        this.servicioSeleccionado = servicioSeleccionado;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivoDetalleMatricula() {
        return comboAnioLectivoDetalleMatricula;
    }

    public void setComboAnioLectivoDetalleMatricula(SofisComboG<SgAnioLectivo> comboAnioLectivoDetalleMatricula) {
        this.comboAnioLectivoDetalleMatricula = comboAnioLectivoDetalleMatricula;
    }

    public SofisComboG<SgNivel> getComboNivelDetalleMatricula() {
        return comboNivelDetalleMatricula;
    }

    public void setComboNivelDetalleMatricula(SofisComboG<SgNivel> comboNivelDetalleMatricula) {
        this.comboNivelDetalleMatricula = comboNivelDetalleMatricula;
    }

    
    public SofisComboG<SgImplementadora> getComboImplementadora() {
        return comboImplementadora;
    }

    public void setComboImplementadora(SofisComboG<SgImplementadora> comboImplementadora) {
        this.comboImplementadora = comboImplementadora;
    }


    public Integer getPaginadoMedia() {
        return paginadoMedia;
    }

    public void setPaginadoMedia(Integer paginadoMedia) {
        this.paginadoMedia = paginadoMedia;
    }

    public List<SgCasoViolacion> getListCasoViolacion() {
        return listCasoViolacion;
    }

    public void setListCasoViolacion(List<SgCasoViolacion> listCasoViolacion) {
        this.listCasoViolacion = listCasoViolacion;
    }

    public List<SgAsistenciaSede> getListAsistenciaSede() {
        return listAsistenciaSede;
    }

    public void setListAsistenciaSede(List<SgAsistenciaSede> listAsistenciaSede) {
        this.listAsistenciaSede = listAsistenciaSede;
    }

    public List<SgFactorRiesgoSede> getListFactorRiesgoSocial() {
        return listFactorRiesgoSocial;
    }

    public void setListFactorRiesgoSocial(List<SgFactorRiesgoSede> listFactorRiesgoSocial) {
        this.listFactorRiesgoSocial = listFactorRiesgoSocial;
    }

    public List<SgFactorRiesgoSede> getListFactorRiesgoAmbiente() {
        return listFactorRiesgoAmbiente;
    }

    public void setListFactorRiesgoAmbiente(List<SgFactorRiesgoSede> listFactorRiesgoAmbiente) {
        this.listFactorRiesgoAmbiente = listFactorRiesgoAmbiente;
    }

    public SgProyectoInstitucionalSede getProyectoEnEdicion() {
        return proyectoEnEdicion;
    }

    public void setProyectoEnEdicion(SgProyectoInstitucionalSede proyectoEnEdicion) {
        this.proyectoEnEdicion = proyectoEnEdicion;
    }

    public SgOrganismoCESede getOrganismoEnEdicion() {
        return organismoEnEdicion;
    }

    public void setOrganismoEnEdicion(SgOrganismoCESede organismoEnEdicion) {
        this.organismoEnEdicion = organismoEnEdicion;
    }

    public SgFactorRiesgoSede getFactorRiesgoEnEdicion() {
        return factorRiesgoEnEdicion;
    }

    public void setFactorRiesgoEnEdicion(SgFactorRiesgoSede factorRiesgoEnEdicion) {
        this.factorRiesgoEnEdicion = factorRiesgoEnEdicion;
    }

    public SgFactorRiesgoSede getFactorRiesgoSocialEnEdicion() {
        return factorRiesgoSocialEnEdicion;
    }

    public void setFactorRiesgoSocialEnEdicion(SgFactorRiesgoSede factorRiesgoSocialEnEdicion) {
        this.factorRiesgoSocialEnEdicion = factorRiesgoSocialEnEdicion;
    }

    public SgAccionPrevenirEmbarazo getPrevenirEmbarazoEnEdicion() {
        return prevenirEmbarazoEnEdicion;
    }

    public void setPrevenirEmbarazoEnEdicion(SgAccionPrevenirEmbarazo prevenirEmbarazoEnEdicion) {
        this.prevenirEmbarazoEnEdicion = prevenirEmbarazoEnEdicion;
    }

    public SgAsistenciaSede getAsistenciaSedeEnEdicion() {
        return asistenciaSedeEnEdicion;
    }

    public void setAsistenciaSedeEnEdicion(SgAsistenciaSede asistenciaSedeEnEdicion) {
        this.asistenciaSedeEnEdicion = asistenciaSedeEnEdicion;
    }

    public SofisComboG<SgMotivoCierreSede> getMotivoCierreCombo() {
        return motivoCierreCombo;
    }

    public void setMotivoCierreCombo(SofisComboG<SgMotivoCierreSede> motivoCierreCombo) {
        this.motivoCierreCombo = motivoCierreCombo;
    }

}
