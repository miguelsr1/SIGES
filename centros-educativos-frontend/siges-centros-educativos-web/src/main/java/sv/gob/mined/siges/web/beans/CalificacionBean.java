/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCalificacionCE;
import sv.gob.mined.siges.web.dto.SgCalificacionEstudiante;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgPeriodoCalendario;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.catalogo.SgCalificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCalificacionCatalogo;
import sv.gob.mined.siges.web.lazymodels.LazyCalificacionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalendarioEscolarRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanGradoRestClient;
import sv.gob.mined.siges.web.restclient.PeriodoCalificacionRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgArchivoCalificaciones;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPorcentajeAsistenciasRequest;
import sv.gob.mined.siges.web.dto.SgPorcentajeAsistenciasResponse;
import sv.gob.mined.siges.web.dto.SgRelGradoPlanEstudio;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgFormula;
import sv.gob.mined.siges.web.dto.catalogo.SgPlantilla;
import sv.gob.mined.siges.web.enumerados.EnumEstadoImportado;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRangoFecha;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroFormula;
import sv.gob.mined.siges.web.restclient.ArchivoCalificacionesRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.ComponenteDocenteRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.RangoFechaRestClient;
import sv.gob.mined.siges.web.restclient.RelGradoPlanEstudioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CalificacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalificacionBean.class.getName());

    @Inject
    @Param(name = "seccionId")
    private Long seccionId;

    @Inject
    @Param(name = "id")
    private Long calificacionId;

    @Inject
    @Param(name = "rev")
    private Long calificacionRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    @Param(name = "importarArchivo")
    private Boolean importarArchivo;

    @Inject
    private CalificacionRestClient restClient;

    @Inject
    private CalificacionEstudianteRestClient calificacionEstudianteRestClient;

    @Inject
    private CalendarioEscolarRestClient calendarioEscolarRestClient;

    @Inject
    private PeriodoCalificacionRestClient restPeriodoCalificacion;

    @Inject
    private SeccionRestClient seccionRestClient;

    @Inject
    private ComponentePlanGradoRestClient componentePlanGradoRestClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private RelGradoPlanEstudioRestClient relGradoPlanEstudioClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    private ArchivoCalificacionesRestClient archivoCalificacionRestClient;

    @Inject
    private RangoFechaRestClient rangoFechaRestClient;

    @Inject
    private MatriculaRestClient restMatricula;

    @Inject
    private ComponenteDocenteRestClient componenteDocenteRestClient;

    @Inject
    private PlantillaRestClient plantillaRestClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgCalificacionCE entidadEnEdicion = new SgCalificacionCE();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCalificacionDataModel calificacionLazyModel;

    private SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudio;
    private SofisComboG<SgCalificacion>[] comboCalificacion;
    private SofisComboG<SgPeriodoCalificacion> comboPeriodoCalificacion;
    private SofisComboG<EnumCalendarioEscolar> comboTipoPeriodoCalendario;
    private SofisComboG<String> comboPeriodoCalendario;
    private SofisComboG<SgRangoFecha> comboRangoFecha;
    private List<SgEstudiante> listEstudiantes;
    private LocalDate fechaRegistro;
    private List<SgCalificacionEstudiante> calificacionesEstList;
    private SgEscalaCalificacion escalaCalificacion;
    private SgSeccion seccionEnEdicion;
    private Boolean escalaNumerica = Boolean.FALSE;
    private Boolean soloLectura = Boolean.FALSE;
    private List<EnumTiposPeriodosCalificaciones> periodoOrdinarioList;
    private SgComponentePlanGrado componentePlanGrado;
    private Integer cantidadPruebaComponentePlanGrado = 0;
    private List<SelectItem> comboPeriodos;
    private Integer comboPeriodoSeleccionado;
    private LocalDate fechaCalificaciones;
    private Boolean importandoArchivo = Boolean.FALSE;
    private Boolean puedeGuardarArchivo = Boolean.FALSE;
    private SgArchivoCalificaciones archivoCalificaciones;
    private SgConfiguracion configuracionImport;
    private SgConfiguracion configuracionImportMensaje;
    private Boolean nuevosEstudiantesSeccionSinCalificacion = Boolean.FALSE;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(xlsx)$/";
    private Boolean calificarSinNie = Boolean.TRUE;
    private Boolean calificarSinMatriculaValidada = Boolean.TRUE;
    private Boolean calificarConMatriculaProvisional = Boolean.TRUE;
    private Boolean requiereValidacionAcademica = Boolean.FALSE;
    private HashMap<Long, Boolean> estudiantesHabilitados = new HashMap<>();
    private HashMap<Long, Boolean> estudiantesMatriculaAbierta = new HashMap<>();
    private List<SgPorcentajeAsistenciasResponse> listDatosHabilitadoPExtraordinario = new ArrayList();
    private SgSeccion seccionEvaluadaHabilitarPExtra;
    private SgComponentePlanEstudio cpeEvaluadaHabilitarPExtra;
    private SgFormula formulaHabilitaPExtraordinario;
    private SgComponentePlanGrado parametroFormulaHabilitaPExtraordinario;
    private String[] formulasAuxiliares = new String[]{"maximo(a,b)=if(isNaN(a),if(isNaN(b),0,b),if(isNaN(b),a,max(a,b)))", "maxTres(a,b,c)=maximo(a,maximo(b,c))"};
    private String[] incluirCamposSeccion = new String[]{
        "secServicioEducativo.sduGrado.graPk",
        "secServicioEducativo.sduSede.sedTipoCalendario.tcePk",
        "secServicioEducativo.sduSede.sedPk",
        "secServicioEducativo.sduSede.sedTipo",
        "secPlanEstudio.pesPk",
        "secPlanEstudio.pesNombre",
        "secAnioLectivo.alePk",
        "secTipoPeriodo",
        "secNumeroPeriodo",
        "secEstado",
        "secNombre",
        "secCodigo",
        "secVersion"};
    private List<SgCalificacionEstudiante> otrasCalificaciones;
    private List<SgEstudiante> nuevosEstudiantesSinCalificacion;
    private List<SgComponentePlanGrado> componentesImportacion;
    private List<String> msjImportacionComponentesPosiblesList;
    private LocalDate fechaCalificacionImport;
    private List<SgComponentePlanEstudio> componentesAsignadoHorarioEscolar;
    private SgPlantilla plantillaImportacion;
    private Integer configuracionMetodoImportacion;//valor 0-importa un solo componente / valor 1-importa de a muchos componentes
    private Boolean puedeCalificarComponente;

    public CalificacionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (calificacionId != null && calificacionId > 0) {
                if (calificacionRev != null && calificacionRev > 0) {
                    //TODO: Hay que implementar obtenerEnRevision custom para inicializar calificaciones
                    //this.actualizarRevision(restClient.obtenerEnRevision(calificacionId, calificacionRev));
                    //this.soloLectura = Boolean.TRUE;
                } else {
                    SgCalificacionCE calce = obtenerPorIdCustom(calificacionId);
                    if (calce == null) {
                        return;
                    }
                    this.actualizar(calce);
                    soloLectura = editable != null ? !editable : soloLectura;
                    if (seccionEnEdicion != null) {
                        if (EnumSeccionEstado.CERRADA.equals(seccionEnEdicion.getSecEstado())) {
                            soloLectura = Boolean.TRUE;
                        }
                    }
                    if (calce.getCalRangoFecha() != null) {
                        if (!calce.getCalRangoFecha().getRfeHabilitado()) {
                            soloLectura = Boolean.TRUE;
                            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.WARNING_CALIFICACION_RANGO_FECHA_DESHABILITADO), "");
                        }
                    }
                }
            } else {
                this.agregar();
                if (seccionId != null && seccionId > 0) {
                    componentePlanEstudioCargaCombo(seccionRestClient.obtenerPorId(seccionId));
                    if (seccionEnEdicion != null) {
                        if (EnumSeccionEstado.CERRADA.equals(seccionEnEdicion.getSecEstado())) {
                            soloLectura = Boolean.TRUE;
                        }
                    }
                }
                importandoArchivo = importarArchivo != null ? importarArchivo : Boolean.FALSE;
                if (importandoArchivo) {
                    FiltroCodiguera fc = new FiltroCodiguera();
                    fc.setCodigo(Constantes.TAMANIO_ARCHIVO_IMPORT);
                    List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
                    if (!conf.isEmpty()) {
                        tamanioImportArchivo = conf.get(0).getConValor();
                    }
                    fc.setCodigo(Constantes.TIPO_ARCHIVO_IMPORT);
                    conf = catalogoRestClient.buscarConfiguracion(fc);
                    if (!conf.isEmpty()) {
                        tipoImportArchivo = conf.get(0).getConValor();
                    }
                }
            }

            validarAcceso();
            configuracionImportacion();
            obtenerPlantilla();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void obtenerPlantilla() {
        try {
            if (BooleanUtils.isTrue(importandoArchivo)) {
                if (configuracionMetodoImportacion.equals(1)) {
                    plantillaImportacion = plantillaRestClient.obtenerPorCodigo(Constantes.IMPORTACION_CALIFICACIONES_MAS_DE_UN_COMP);
                } else {
                    plantillaImportacion = plantillaRestClient.obtenerPorCodigo(Constantes.IMPORTACION_CALIFICACIONES_UNICO_COMP);
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String obtenerNombrePagina() {
        if (BooleanUtils.isTrue(importandoArchivo)) {
            return Etiquetas.getValue("importacionCalificaciones");
        } else {
            return Etiquetas.getValue("gestionCalificacion");
        }
    }

    public void validarAcceso() {
        //Control de seguridad
        if (entidadEnEdicion.getCalTipoPeriodoCalificacion() != null && entidadEnEdicion.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)) {
            this.soloLectura = Boolean.TRUE;
        }
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_CALIFICACIONES)) {
                LOGGER.log(Level.INFO, sessionBean.getUser().getName() + " - Redirigiendo a inicio. " + ConstantesOperaciones.BUSCAR_CALIFICACIONES);
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getCalPk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_CALIFICACIONES)) {
                    LOGGER.log(Level.INFO, sessionBean.getUser().getName() + " - Redirigiendo a inicio. " + ConstantesOperaciones.CREAR_CALIFICACIONES);
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_CALIFICACIONES)) {
                    LOGGER.log(Level.INFO, sessionBean.getUser().getName() + " - Redirigiendo a inicio. " + ConstantesOperaciones.ACTUALIZAR_CALIFICACIONES);
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public SgCalificacionCE obtenerPorIdCustom(Long calPk) throws Exception {
        FiltroCalificacion fc = new FiltroCalificacion();
        fc.setCalPk(calPk);
        List<EnumTiposPeriodosCalificaciones> tipos = new ArrayList<>();
        tipos.add(EnumTiposPeriodosCalificaciones.ORD);
        tipos.add(EnumTiposPeriodosCalificaciones.EXORD);
        fc.setCalTiposPeriodoCalificacion(tipos);

        List<SgCalificacionCE> cals = restClient.buscar(fc);
        if (cals.isEmpty()) {
            JSFUtils.redirectNotFound();
            return null;
        }
        SgCalificacionCE calce = cals.get(0);
        return calce;
    }

    public String obtenerTooltipDeshabilitacion(SgCalificacionEstudiante cae) {
        if (BooleanUtils.isFalse(puedeCalificarComponente)) {
            return null;
        }
        if (estudiantesMatriculaAbierta.containsKey(cae.getCaeEstudiante().getEstPk()) && !estudiantesMatriculaAbierta.get(cae.getCaeEstudiante().getEstPk())) {
            return Etiquetas.getValue("hmatrculaCerrada");
        }
        if (cae.getCaeEstudiante().getEstPersona().getPerNie() == null) {
            return Etiquetas.getValue("hdeshabilitadoFaltaNie");
        }
        if (BooleanUtils.isNotTrue(cae.getCaeEstudiante().getEstUltimaMatricula().getMatValidacionAcademica())) {
            return Etiquetas.getValue("hdeshabilitadoMatriculaNoValidada");
        }
        if (BooleanUtils.isTrue(cae.getCaeEstudiante().getEstUltimaMatricula().getMatProvisional())) {
            return Etiquetas.getValue("hdeshabilitadoMatriculaProvisional");
        }

        return null;
    }

    public Boolean deshabilitarIngresoCalificacion(SgCalificacionEstudiante cae) {
        if (BooleanUtils.isFalse(puedeCalificarComponente)) {
            return Boolean.TRUE;
        }
        if (!calificarSinNie && cae.getCaeEstudiante().getEstPersona().getPerNie() == null) {
            return Boolean.TRUE;
        }
        if (BooleanUtils.isTrue(requiereValidacionAcademica)) {
            if (!calificarSinMatriculaValidada && BooleanUtils.isNotTrue(cae.getCaeEstudiante().getEstUltimaMatricula().getMatValidacionAcademica())) {
                return Boolean.TRUE;
            }
        }
        if (!calificarConMatriculaProvisional && BooleanUtils.isTrue(cae.getCaeEstudiante().getEstUltimaMatricula().getMatProvisional())) {
            return Boolean.TRUE;
        }

        if (estudiantesMatriculaAbierta.containsKey(cae.getCaeEstudiante().getEstPk())) {
            return !estudiantesMatriculaAbierta.get(cae.getCaeEstudiante().getEstPk());
        }

        return Boolean.FALSE;
    }

    private void verificarCalificacionSinMatriculaValidada(Long pkGrado, Long pkPlanEstudio) {
        try {
            FiltroRelGradoPlanEstudio filtro = new FiltroRelGradoPlanEstudio();
            filtro.setRgpGrado(pkGrado);
            filtro.setRgpPlanEstudio(pkPlanEstudio);
            filtro.setIncluirCampos(new String[]{"rgpPermiteCalificarSinMatValidada", "rgpPermiteCalificarConMatProvisional", "rgpRequiereValidacionAcademica"});
            List<SgRelGradoPlanEstudio> list = relGradoPlanEstudioClient.buscarConCache(filtro);
            if (!list.isEmpty()) {
                SgRelGradoPlanEstudio relGradoPlanEstudio = list.get(0);
                this.calificarSinMatriculaValidada = relGradoPlanEstudio.getRgpPermiteCalificarSinMatValidada();
                this.calificarConMatriculaProvisional = relGradoPlanEstudio.getRgpPermiteCalificarConMatProvisional();
                this.requiereValidacionAcademica = relGradoPlanEstudio.getRgpRequiereValidacionAcademica();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }

    }

    public void actualizarRevision(SgCalificacionCE cal) {
        entidadEnEdicion = cal;
    }

    public void actualizar(SgCalificacionCE cal) {
        try {
            nuevosEstudiantesSeccionSinCalificacion = Boolean.FALSE;
            nuevosEstudiantesSinCalificacion = new ArrayList<>();
            limpiarCombos();
            if (cal == null) {
                this.agregar();
            } else {
                EnumTiposPeriodosCalificaciones tipo = cal.getCalTipoPeriodoCalificacion();
                entidadEnEdicion = cal;
                seccionEnEdicion = entidadEnEdicion.getCalSeccion();

                if (EnumTiposPeriodosCalificaciones.ORD.equals(entidadEnEdicion.getCalTipoPeriodoCalificacion())) {
                    this.calificarSinNie = entidadEnEdicion.getCalRangoFecha().getRfePeriodoCalificacion().getPcaPermiteCalificarSinNie();
                    verificarCalificacionSinMatriculaValidada(
                            entidadEnEdicion.getCalSeccion().getSecServicioEducativo().getSduGrado().getGraPk(),
                            entidadEnEdicion.getCalSeccion().getSecPlanEstudio().getPesPk());
                }
                if (this.calificacionId == null) {
                    //Actualizamos desde base y cargamos combos

                    componentePlanEstudioCargaCombo(entidadEnEdicion.getCalSeccion());
                    entidadEnEdicion.setCalTipoPeriodoCalificacion(tipo);
                    comboComponentePlanEstudio.setSelectedT(entidadEnEdicion.getCalComponentePlanEstudio());
                    cargarPeriodoCalificacion();
                    switch (entidadEnEdicion.getCalTipoPeriodoCalificacion()) {
                        case ORD:
                            comboPeriodoCalificacion.setSelectedT(entidadEnEdicion.getCalRangoFecha().getRfePeriodoCalificacion());
                            cargarRangoFecha();
                            comboRangoFecha.setSelectedT(entidadEnEdicion.getCalRangoFecha());
                            break;
                        case EXORD:
                            comboTipoPeriodoCalendario.setSelectedT(entidadEnEdicion.getCalTipoCalendarioEscolar());
                            cargarRangoFechaCalendarioEscolar();
                            comboPeriodoSeleccionado = entidadEnEdicion.getCalNumero();
                            break;
                    }
                } else {
                    //Solo lectura, no se cargan combos
                    cargarEscalaCalificacion();
                }

                //Verificar lista total de estudiantes de la sección
                listEstudiantes = buscarEstudiantes();
                //Buscar calificaciones de estudiantes de la seccion
                List<SgCalificacionEstudiante> calEstudiantes = this.buscarCalificacionesEstudiantes();

                entidadEnEdicion.setCalCalificacionesEstudiante(
                        calEstudiantes.stream()
                                .filter(c -> c.getCaeCalificacion().getCalPk().equals(entidadEnEdicion.getCalPk()))
                                .collect(Collectors.toList())
                );

                //calificaciones de los estudiantes realizadas en otras secciones
                otrasCalificaciones = calEstudiantes.stream()
                        .filter(c -> !c.getCaeCalificacion().getCalPk().equals(entidadEnEdicion.getCalPk()))
                        .collect(Collectors.toList());

                for (SgEstudiante e : otrasCalificaciones.stream().map(c -> c.getCaeEstudiante()).collect(Collectors.toList())) {
                    listEstudiantes.remove(e);
                }

                //Nuevos estudiantes que todavia no tienen calificacion
                List<SgEstudiante> estudiantesCalificados = this.entidadEnEdicion.getCalCalificacionesEstudiante().stream().map(c -> c.getCaeEstudiante()).collect(Collectors.toList());
                for (int i = 0; i < listEstudiantes.size(); i++) {
                    SgEstudiante est = listEstudiantes.get(i);
                    if (!estudiantesCalificados.contains(est)) {
                        SgCalificacionEstudiante calEst = new SgCalificacionEstudiante();
                        calEst.setCaeEstudiante(est);
                        this.entidadEnEdicion.getCalCalificacionesEstudiante().add(calEst);
                        this.nuevosEstudiantesSinCalificacion.add(est);
                        nuevosEstudiantesSeccionSinCalificacion = Boolean.TRUE;
                    }
                }

                if (nuevosEstudiantesSeccionSinCalificacion) {
                    Collections.sort(this.entidadEnEdicion.getCalCalificacionesEstudiante(), (o1, o2) -> o1.getCaeEstudiante().getEstPersona().getPerNombreCompletoOrder().compareTo(o2.getCaeEstudiante().getEstPersona().getPerNombreCompletoOrder()));
                }

                if (escalaCalificacion.getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA)) {
                    escalaNumerica = Boolean.TRUE;
                } else {
                    escalaNumerica = Boolean.FALSE;
                    comboCalificacion = new SofisComboG[entidadEnEdicion.getCalCalificacionesEstudiante().size()];

                    FiltroCalificacionCatalogo fcal = new FiltroCalificacionCatalogo();
                    fcal.setCalEscalaCalificacionPk(escalaCalificacion.getEcaPk());
                    fcal.setOrderBy(new String[]{"calValor"});
                    List<SgCalificacion> calificaciones = catalogoRestClient.buscarCalificacion(fcal);

                    for (int i = 0; i < entidadEnEdicion.getCalCalificacionesEstudiante().size(); i++) {
                        SgCalificacionEstudiante as = entidadEnEdicion.getCalCalificacionesEstudiante().get(i);
                        comboCalificacion[i] = new SofisComboG(calificaciones, "calValor");
                        comboCalificacion[i].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                        comboCalificacion[i].setSelectedT(as.getCaeCalificacionConceptualEstudiante());
                    }
                }
                if (entidadEnEdicion.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.EXORD)) {
                    if (entidadEnEdicion.getCalComponentePlanEstudio() != null) {
                        datosHabilitacionPeriodoExtraordinario(entidadEnEdicion.getCalComponentePlanEstudio());
                    } else {
                        if (comboComponentePlanEstudio.getSelectedT() != null) {
                            datosHabilitacionPeriodoExtraordinario(comboComponentePlanEstudio.getSelectedT());
                        }
                    }
                }

                buscarComponentesAsignadosHorarioEscolar();
                puedeCalificarComponente = Boolean.TRUE;
                if (entidadEnEdicion.getCalComponentePlanEstudio() != null) {
                    if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.PERMITE_CALIFICAR_SIN_HORARIO_ESCOLAR_ASIGNADO)) {
                        puedeCalificarComponente = Boolean.FALSE;
                        if (componentesAsignadoHorarioEscolar != null && !componentesAsignadoHorarioEscolar.isEmpty()) {
                            List<SgComponentePlanEstudio> list = componentesAsignadoHorarioEscolar.stream().filter(c -> c.getCpePk().equals(entidadEnEdicion.getCalComponentePlanEstudio().getCpePk())).collect(Collectors.toList());
                            if (!list.isEmpty()) {
                                puedeCalificarComponente = Boolean.TRUE;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargaTabla() {
        try {
            puedeCalificarComponente = Boolean.TRUE;
            fechaCalificacionImport = null;
            msjImportacionComponentesPosiblesList = null;
            puedeGuardarArchivo = Boolean.FALSE;
            nuevosEstudiantesSeccionSinCalificacion = Boolean.FALSE;
            fechaCalificaciones = null;
            if ((!importandoArchivo) && ((EnumTiposPeriodosCalificaciones.ORD.equals(entidadEnEdicion.getCalTipoPeriodoCalificacion()) && comboRangoFecha.getSelectedT() != null)
                    || (EnumTiposPeriodosCalificaciones.EXORD.equals(entidadEnEdicion.getCalTipoPeriodoCalificacion()) && comboPeriodoSeleccionado > 0 && escalaCalificacion != null))) {

                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.PERMITE_CALIFICAR_SIN_HORARIO_ESCOLAR_ASIGNADO)) {
                    puedeCalificarComponente = Boolean.FALSE;
                    if (componentesAsignadoHorarioEscolar != null && !componentesAsignadoHorarioEscolar.isEmpty()) {
                        List<SgComponentePlanEstudio> list = componentesAsignadoHorarioEscolar.stream().filter(c -> c.getCpePk().equals(comboComponentePlanEstudio.getSelectedT().getCpePk())).collect(Collectors.toList());
                        if (!list.isEmpty()) {
                            puedeCalificarComponente = Boolean.TRUE;
                        }
                    }
                }

                FiltroCalificacion fc = new FiltroCalificacion();
                fc.setSecPk(seccionEnEdicion.getSecPk());
                fc.setCalComponentePlanEstudio(this.comboComponentePlanEstudio.getSelectedT().getCpePk());
                switch (entidadEnEdicion.getCalTipoPeriodoCalificacion()) {
                    case ORD:
                        fc.setCalRangoFecha(comboRangoFecha.getSelectedT().getRfePk());
                        fc.setCalTipoPeriodoCalificacion(entidadEnEdicion.getCalTipoPeriodoCalificacion());

                        break;
                    case EXORD:
                        fc.setCalTipoCalendarioEscolar(comboTipoPeriodoCalendario.getSelectedT());
                        fc.setCalTipoPeriodoCalificacion(entidadEnEdicion.getCalTipoPeriodoCalificacion());
                        fc.setCalNumero(comboPeriodoSeleccionado);
                        break;
                    default:
                        break;
                }
                fc.setIncluirCampos(new String[]{"calVersion"});
                List<SgCalificacionCE> list = restClient.buscar(fc);
                if (!list.isEmpty()) {
                    //Actualizar
                    this.actualizar(this.obtenerPorIdCustom(list.get(0).getCalPk()));
                } else {
                    //Agregar
                    this.entidadEnEdicion.setCalPk(null);
                    this.entidadEnEdicion.setCalVersion(0);
                    this.entidadEnEdicion.setCalCalificacionesEstudiante(new ArrayList<>());
                    if (EnumTiposPeriodosCalificaciones.ORD.equals(entidadEnEdicion.getCalTipoPeriodoCalificacion())) {
                        this.calificarSinNie = this.comboPeriodoCalificacion.getSelectedT().getPcaPermiteCalificarSinNie();
                        verificarCalificacionSinMatriculaValidada(
                                seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk(),
                                seccionEnEdicion.getSecPlanEstudio().getPesPk());
                    }

                    //long startTimeCPG = System.currentTimeMillis();
                    listEstudiantes = buscarEstudiantes();
                    //Buscar calificaciones de estudiantes para mismo período y componente en otra sección
                    otrasCalificaciones = buscarCalificacionesEstudiantes();
                    for (SgEstudiante e : otrasCalificaciones.stream().map(c -> c.getCaeEstudiante()).collect(Collectors.toList())) {
                        listEstudiantes.remove(e);
                    }

                    //long endTimeCPG = System.currentTimeMillis();
                    //LOGGER.log(Level.WARNING, "USER: " + sessionBean.getUser().getName() + " NUEVO: " + (endTimeCPG - startTimeCPG) + " milliseconds");
                    for (int i = 0; i < listEstudiantes.size(); i++) {
                        SgEstudiante est = listEstudiantes.get(i);
                        SgCalificacionEstudiante calEst = new SgCalificacionEstudiante();
                        calEst.setCaeEstudiante(est);
                        this.entidadEnEdicion.getCalCalificacionesEstudiante().add(calEst);
                    }

                    if (escalaCalificacion.getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA)) {
                        escalaNumerica = Boolean.TRUE;
                    } else {
                        escalaNumerica = Boolean.FALSE;
                        FiltroCalificacionCatalogo fcal = new FiltroCalificacionCatalogo();
                        fcal.setCalEscalaCalificacionPk(escalaCalificacion.getEcaPk());
                        fcal.setOrderBy(new String[]{"calValor"});
                        List<SgCalificacion> calificaciones = catalogoRestClient.buscarCalificacion(fcal);

                        comboCalificacion = new SofisComboG[listEstudiantes.size()];
                        for (int i = 0; i < listEstudiantes.size(); i++) {
                            comboCalificacion[i] = new SofisComboG(calificaciones, "calValor");
                            comboCalificacion[i].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                        }
                    }
                    if (entidadEnEdicion.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.EXORD)) {
                        if (entidadEnEdicion.getCalComponentePlanEstudio() != null) {
                            datosHabilitacionPeriodoExtraordinario(entidadEnEdicion.getCalComponentePlanEstudio());
                        } else {
                            if (comboComponentePlanEstudio.getSelectedT() != null) {
                                datosHabilitacionPeriodoExtraordinario(comboComponentePlanEstudio.getSelectedT());
                            }
                        }
                    }

                }

            } else {
                msjImportacionComponentesPosiblesList = new ArrayList();
                if (importandoArchivo) {
                    List<SgComponentePlanGrado> componentesDePeriodo = componentesImportacion.stream().filter(c -> c.getCpgPeriodosCalificacion().equals(comboPeriodoCalificacion.getSelectedT().getPcaNumero())).collect(Collectors.toList());
                    if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.PERMITE_CALIFICAR_SIN_HORARIO_ESCOLAR_ASIGNADO)) {
                        if (componentesAsignadoHorarioEscolar != null && !componentesAsignadoHorarioEscolar.isEmpty()) {
                            componentesDePeriodo = componentesDePeriodo.stream().filter(c -> componentesAsignadoHorarioEscolar.contains(c.getCpgComponentePlanEstudio())).collect(Collectors.toList());
                        } else {
                            componentesDePeriodo = new ArrayList();
                        }
                    }

                    if (!componentesDePeriodo.isEmpty()) {
                        msjImportacionComponentesPosiblesList = new ArrayList();
                        for (SgComponentePlanGrado cpg : componentesDePeriodo) {
                            msjImportacionComponentesPosiblesList.add(cpg.getCpgComponentePlanEstudio().getCpeNombre());
                        }
                    }

                }
            }
            PrimeFaces.current().executeScript("PF('blockerExord').hide()");
            PrimeFaces.current().executeScript("PF('blockerOrd').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void calificacionSelected(SgCalificacionEstudiante cal, Integer rowIndex) {
        cal.setCaeCalificacionConceptualEstudiante(this.comboCalificacion[rowIndex].getSelectedT());
    }

    public List<SgEstudiante> buscarEstudiantes() throws Exception {
        estudiantesMatriculaAbierta = new HashMap<>();
        FiltroMatricula filtroMat = new FiltroMatricula();
        filtroMat.setSecPk(seccionEnEdicion.getSecPk());
        filtroMat.setMatRetirada(Boolean.FALSE);
        filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPk",
            "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
            "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
            "matValidacionAcademica", "matProvisional", "matEstado",
            "matEstudiante.estVersion"});
        filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido",
            "matEstudiante.estPersona.perPrimerNombre"});
        filtroMat.setAscending(new boolean[]{true, true, true});
        List<SgMatricula> matriculas = restMatricula.buscar(filtroMat);
        List<SgEstudiante> estudiantes = matriculas.stream()
                .map(c -> {
                    c.getMatEstudiante().setEstUltimaMatricula(c);
                    if (!estudiantesMatriculaAbierta.containsKey(c.getMatEstudiante().getEstPk())) {
                        estudiantesMatriculaAbierta.put(c.getMatEstudiante().getEstPk(), c.getMatEstado().equals(EnumMatriculaEstado.ABIERTO) ? Boolean.TRUE : Boolean.FALSE);
                    }
                    return c.getMatEstudiante();
                })
                .collect(Collectors.toList());
        return estudiantes;
    }

    public List<SgCalificacionEstudiante> buscarCalificacionesEstudiantes() throws Exception {

        if (!this.listEstudiantes.isEmpty()) {
            FiltroCalificacionEstudiante filtro = new FiltroCalificacionEstudiante();
            filtro.setCaeEstudiantesPk(this.listEstudiantes.stream().map(e -> e.getEstPk()).collect(Collectors.toList()));
            filtro.setCaeComponentePlanEstudio(this.calificacionId == null ? this.comboComponentePlanEstudio.getSelectedT().getCpePk() : this.entidadEnEdicion.getCalComponentePlanEstudio().getCpePk());
            filtro.setCaeGradoFk(seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk());

            //Filtra dependiendo si sección es anual o semestral
            filtro.setCaeTipoPeriodo(seccionEnEdicion.getSecTipoPeriodo());
            filtro.setCaeNumeroPeriodo(seccionEnEdicion.getSecNumeroPeriodo());
            switch (entidadEnEdicion.getCalTipoPeriodoCalificacion()) {
                case ORD:
                    filtro.setCaeRangoFechaPk(this.calificacionId == null ? comboRangoFecha.getSelectedT().getRfePk() : this.entidadEnEdicion.getCalRangoFecha().getRfePk());
                    filtro.setCaeTipoPeriodoCalificacion(entidadEnEdicion.getCalTipoPeriodoCalificacion());

                    break;
                case EXORD:
                    filtro.setCaeTipoCalendarioEscolar(this.calificacionId == null ? comboTipoPeriodoCalendario.getSelectedT() : this.entidadEnEdicion.getCalTipoCalendarioEscolar());
                    filtro.setCaeTipoPeriodoCalificacion(entidadEnEdicion.getCalTipoPeriodoCalificacion());
                    filtro.setCaeNumero(comboPeriodoSeleccionado);
                    break;
                default:
                    break;
            }
            filtro.setIncluirCampos(new String[]{
                "caeCalificacion.calPk",
                "caeCalificacion.calVersion",
                "caeEstudiante.estPk",
                "caeCalificacion.calSeccion.secServicioEducativo.sduSede.sedCodigo",
                "caeCalificacion.calSeccion.secServicioEducativo.sduSede.sedNombre",
                "caeCalificacion.calSeccion.secServicioEducativo.sduSede.sedTipo",
                "caeFechaRealizado",
                "caeEstudiante.estUltimaMatricula.matValidacionAcademica",
                "caeEstudiante.estUltimaMatricula.matProvisional",
                "caeEstudiante.estPersona.perNie",
                "caeEstudiante.estPersona.perPrimerNombre", "caeEstudiante.estPersona.perSegundoNombre", "caeEstudiante.estPersona.perNombreBusqueda",
                "caeEstudiante.estPersona.perPrimerApellido", "caeEstudiante.estPersona.perSegundoApellido", "caeEstudiante.estPersona.perFechaNacimiento",
                "caeEstudiante.estVersion",
                "caeCalificacionNumericaEstudiante",
                "caeCalificacionConceptualEstudiante.calPk",
                "caeCalificacionConceptualEstudiante.calValor",
                "caeCalificacionConceptualEstudiante.calVersion",
                "caeObservacion",
                "caeProcesoDeCreacion",
                "caeVersion"});
            filtro.setOrderBy(new String[]{"caeEstudiante.estPersona.perPrimerApellidoBusqueda", "caeEstudiante.estPersona.perSegundoApellidoBusqueda", "caeEstudiante.estPersona.perPrimerNombreBusqueda", "caeEstudiante.estPersona.perSegundoNombreBusqueda"});
            filtro.setAscending(new boolean[]{true, true, true, true});
            return this.calificacionEstudianteRestClient.buscar(filtro);
        }
        return new ArrayList<>();
    }

    public void componentePlanEstudioSelected() {
        puedeCalificarComponente = Boolean.TRUE;
        comboPeriodoCalificacion = new SofisComboG();
        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboRangoFecha = new SofisComboG();
        comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboPeriodoSeleccionado = 0;
        this.entidadEnEdicion.setCalTipoPeriodoCalificacion(null);
        fechaCalificaciones = null;

        if (importandoArchivo && configuracionMetodoImportacion.equals(0)) {
            entidadEnEdicion.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.ORD);
            cargarPeriodoCalificacion();
        }
    }

    public void cargarCombos() {
        comboComponentePlanEstudio = new SofisComboG();
        comboComponentePlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboPeriodoCalificacion = new SofisComboG();
        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboRangoFecha = new SofisComboG();
        comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboPeriodoCalendario = new SofisComboG();
        comboPeriodoCalendario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        List<EnumTiposPeriodosCalificaciones> tipoPeriodoCalList = new ArrayList();
        tipoPeriodoCalList.add(EnumTiposPeriodosCalificaciones.ORD);
        tipoPeriodoCalList.add(EnumTiposPeriodosCalificaciones.EXORD);
        periodoOrdinarioList = new ArrayList(tipoPeriodoCalList);
    }

    private void limpiarCombos() {
        comboComponentePlanEstudio = new SofisComboG();
        comboComponentePlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboPeriodoCalificacion = new SofisComboG();
        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboRangoFecha = new SofisComboG();
        comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboPeriodoSeleccionado = 0;
        this.entidadEnEdicion.setCalTipoPeriodoCalificacion(null);
        fechaCalificaciones = null;
    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
    }

    public void buscarComponentesAsignadosHorarioEscolar() {
        try {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.PERMITE_CALIFICAR_SIN_HORARIO_ESCOLAR_ASIGNADO) && seccionEnEdicion != null) {
                componentesAsignadoHorarioEscolar = componenteDocenteRestClient.obtenerComponentesAsociadosDocenteEnSeccion(seccionEnEdicion.getSecPk());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);

        }
    }

    public void componentePlanEstudioCargaCombo(SgSeccion sec) {
        try {
            puedeCalificarComponente = Boolean.TRUE;
            componentesImportacion = new ArrayList();
            componentesAsignadoHorarioEscolar = new ArrayList();
            estudiantesMatriculaAbierta = new HashMap<>();
            puedeGuardarArchivo = Boolean.FALSE;
            limpiarCombos();
            seccionEnEdicion = sec;
            if (seccionEnEdicion == null) {
                return;
            }
            if (seccionEnEdicion.getSecPlanEstudio() == null) {
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SECCION_SIN_PLAN_ESTUDIO), "");
                return;
            }

            buscarComponentesAsignadosHorarioEscolar();

            FiltroComponentePlanGrado fpg = new FiltroComponentePlanGrado();
            fpg.setCpgGradoPk(seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk());
            fpg.setCpgPlanEstudioPk(seccionEnEdicion.getSecPlanEstudio().getPesPk());
            fpg.setCpgAgregandoSeccionExclusiva(seccionEnEdicion.getSecPk());
            fpg.setCpgCalificacionIngresadaCE(Boolean.TRUE);
            fpg.setIncluirCampos(new String[]{
                "cpgPeriodosCalificacion",
                "cpgPlanEstudio.pesRelacionModalidad.reaModalidadEducativa.modPk",
                "cpgPlanEstudio.pesRelacionModalidad.reaModalidadAtencion.matPk",
                "cpgPlanEstudio.pesRelacionModalidad.reaSubModalidadAtencion.smoPk",
                "cpgComponentePlanEstudio.cpePk",
                "cpgComponentePlanEstudio.cpeCodigo",
                "cpgComponentePlanEstudio.cpeNombre",
                "cpgComponentePlanEstudio.cpeTipo",
                "cpgComponentePlanEstudio.cpeVersion",
                "cpgComponentePlanEstudio.cpeCodigoExterno",
                "cpgComponentePlanEstudio.cpeNombrePublicable"});

            List<SgComponentePlanGrado> listCpg = componentePlanGradoRestClient.buscarConCache(fpg);

            List<SgComponentePlanEstudio> liscpe = listCpg.stream().map(c -> c.getCpgComponentePlanEstudio()).collect(Collectors.toList());
            comboComponentePlanEstudio = new SofisComboG(liscpe, "cpeNombre");
            comboComponentePlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboComponentePlanEstudio.ordenar();

            //Cuando se entra a la pantalla para importar archivo y el método para importar es de muchas componentes
            //configuracionMetodoImportacion=1
            //No hay eleccion de componente, entonces hay q listar todos los períodos posibles
            if (BooleanUtils.isTrue(importandoArchivo) && configuracionMetodoImportacion.equals(1) && !listCpg.isEmpty()) {
                entidadEnEdicion.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.ORD);
                SgComponentePlanGrado componenteAux = listCpg.get(0);
                componentesImportacion = listCpg;
                List<Integer> periodosCantidad = listCpg.stream().map(c -> c.getCpgPeriodosCalificacion()).distinct().collect(Collectors.toList());
                comboTipoPeriodoCalendario = new SofisComboG();
                comboTipoPeriodoCalendario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboPeriodos = new ArrayList();
                comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
                comboPeriodoCalificacion = new SofisComboG();
                comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboRangoFecha = new SofisComboG();
                comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                fechaCalificaciones = null;
                comboPeriodoSeleccionado = 0;

                FiltroPeriodoCalificacion fpc = new FiltroPeriodoCalificacion();
                fpc.setIncluirCampos(new String[]{
                    "pcaNumero",
                    "pcaPermiteCalificarSinNie",
                    "pcaEsPrueba",
                    "pcaNombre",
                    "pcaTipoPeriodo",
                    "pcaNumeroPeriodo",
                    "pcaVersion"});
                fpc.setPcaEsPrueba(Boolean.FALSE);
                fpc.setPcaModalidadEducativa(componenteAux.getCpgPlanEstudio().getPesRelacionModalidad().getReaModalidadEducativa().getModPk());
                fpc.setPcaModalidadAtencion(componenteAux.getCpgPlanEstudio().getPesRelacionModalidad().getReaModalidadAtencion().getMatPk());
                fpc.setPcaNumeros(periodosCantidad);
                fpc.setPcaSubModalidadAtencion(componenteAux.getCpgPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion() != null ? componenteAux.getCpgPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                fpc.setPcaAnioLectivo(seccionEnEdicion.getSecAnioLectivo().getAlePk());
                fpc.setPcaTipoCalendario(seccionEnEdicion.getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());

                //Filtra dependiendo si periodo es anual o semestral
                fpc.setPcaTipoPeriodo(seccionEnEdicion.getSecTipoPeriodo());
                fpc.setPcaNumeroPeriodo(seccionEnEdicion.getSecNumeroPeriodo());
                List<SgPeriodoCalificacion> listPeriodoCalif = restPeriodoCalificacion.buscar(fpc);
                comboPeriodoCalificacion = new SofisComboG(listPeriodoCalif, "nombreTipoPeriodo");
                comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (listPeriodoCalif.size() == 1) {
                    comboPeriodoCalificacion.setSelectedT(listPeriodoCalif.get(0));
                    cargarRangoFecha();
                }

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);

        }
    }

    public void cargarEscalaCalificacion() throws Exception {
        if (this.calificacionId == null) { //Nueva calficación
            entidadEnEdicion.setCalComponentePlanEstudio(comboComponentePlanEstudio.getSelectedT());
        }

        if (entidadEnEdicion.getCalComponentePlanEstudio() != null && entidadEnEdicion.getCalTipoPeriodoCalificacion() != null) {
            FiltroComponentePlanGrado fcp = new FiltroComponentePlanGrado();
            fcp.setCpgGradoPk(seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk());
            fcp.setCpgPlanEstudioPk(seccionEnEdicion.getSecPlanEstudio().getPesPk());
            fcp.setCpgComponentePlanEstudioPk(entidadEnEdicion.getCalComponentePlanEstudio().getCpePk());
            fcp.setCpgAgregandoSeccionExclusiva(seccionEnEdicion.getSecPk());

            fcp.setIncluirCampos(new String[]{
                "cpgPeriodosCalificacion",
                "cpgEscalaCalificacion.ecaPk",
                "cpgEscalaCalificacion.ecaCodigo",
                "cpgEscalaCalificacion.ecaHabilitado",
                "cpgEscalaCalificacion.ecaNombre",
                "cpgEscalaCalificacion.ecaTipoEscala",
                "cpgEscalaCalificacion.ecaMinimo",
                "cpgEscalaCalificacion.ecaMaximo",
                "cpgEscalaCalificacion.ecaMinimoAprobacion",
                "cpgEscalaCalificacion.ecaPrecision",
                "cpgEscalaCalificacion.ecaValorMinimo.calValor",
                "cpgEscalaCalificacion.ecaValorMinimo.calOrden",
                "cpgEscalaCalificacion.ecaVersion",
                "cpgCantidadPrimeraPrueba",
                "cpgCantidadPrimeraSuficiencia",
                "cpgCantidadSegundaPrueba",
                "cpgCantidadSegundaSuficiencia",
                "cpgPrecision",
                "cpgPlanEstudio.pesRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk",
                "cpgPlanEstudio.pesRelacionModalidad.reaModalidadEducativa.modPk",
                "cpgPlanEstudio.pesRelacionModalidad.reaModalidadAtencion.matPk",
                "cpgPlanEstudio.pesRelacionModalidad.reaSubModalidadAtencion.smoPk",
                "cpgFormulaHabilitacionPP.fomTextoLargo",
                "cpgFormulaHabilitacionPP.fomPk",
                "cpgFormulaHabilitacionPP.fomTienSubformula",
                "cpgFormulaHabilitacionPP.fomTipoFormula",
                "cpgFormulaHabilitacionPS.fomTextoLargo",
                "cpgFormulaHabilitacionPS.fomPk",
                "cpgFormulaHabilitacionPS.fomTipoFormula",
                "cpgFormulaHabilitacionPS.fomTienSubformula",
                "cpgFormulaHabilitacionSP.fomTextoLargo",
                "cpgFormulaHabilitacionSP.fomPk",
                "cpgFormulaHabilitacionSP.fomTipoFormula",
                "cpgFormulaHabilitacionSP.fomTienSubformula",
                "cpgFormulaHabilitacionSS.fomTextoLargo",
                "cpgFormulaHabilitacionSS.fomPk",
                "cpgFormulaHabilitacionSS.fomTipoFormula",
                "cpgFormulaHabilitacionSS.fomTienSubformula",
                "cpgParametroFormulaPruebaPP.cpgComponentePlanEstudio.cpePk",
                "cpgParametroFormulaPruebaPP.cpgComponentePlanEstudio.cpeTipo",
                "cpgParametroFormulaPruebaPS.cpgComponentePlanEstudio.cpePk",
                "cpgParametroFormulaPruebaPS.cpgComponentePlanEstudio.cpeTipo",
                "cpgParametroFormulaPruebaSP.cpgComponentePlanEstudio.cpePk",
                "cpgParametroFormulaPruebaSP.cpgComponentePlanEstudio.cpeTipo",
                "cpgParametroFormulaPruebaSS.cpgComponentePlanEstudio.cpePk",
                "cpgParametroFormulaPruebaSS.cpgComponentePlanEstudio.cpeTipo"});
            List<SgComponentePlanGrado> cpg = componentePlanGradoRestClient.buscarConCache(fcp);
            if (!cpg.isEmpty()) {
                componentePlanGrado = cpg.get(0);
                escalaCalificacion = componentePlanGrado.getCpgEscalaCalificacion();

            }
        }

    }

    public void cargarPeriodoCalificacion() {
        try {
            puedeCalificarComponente = Boolean.TRUE;
            estudiantesMatriculaAbierta = new HashMap<>();
            puedeGuardarArchivo = Boolean.FALSE;
            cargarEscalaCalificacion();
            if (entidadEnEdicion.getCalComponentePlanEstudio() != null && entidadEnEdicion.getCalTipoPeriodoCalificacion() != null) {
                comboTipoPeriodoCalendario = new SofisComboG();
                comboTipoPeriodoCalendario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboPeriodos = new ArrayList();
                comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
                comboPeriodoCalificacion = new SofisComboG();
                comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboRangoFecha = new SofisComboG();
                comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                fechaCalificaciones = null;
                comboPeriodoSeleccionado = 0;

                switch (entidadEnEdicion.getCalTipoPeriodoCalificacion()) {
                    case ORD:
                        FiltroPeriodoCalificacion fpc = new FiltroPeriodoCalificacion();
                        fpc.setIncluirCampos(new String[]{
                            "pcaNumero",
                            "pcaPermiteCalificarSinNie",
                            "pcaEsPrueba",
                            "pcaNombre",
                            "pcaTipoPeriodo",
                            "pcaNumeroPeriodo",
                            "pcaVersion"});
                        fpc.setPcaEsPrueba(Boolean.FALSE);
                        fpc.setPcaModalidadEducativa(componentePlanGrado.getCpgPlanEstudio().getPesRelacionModalidad().getReaModalidadEducativa().getModPk());
                        fpc.setPcaModalidadAtencion(componentePlanGrado.getCpgPlanEstudio().getPesRelacionModalidad().getReaModalidadAtencion().getMatPk());
                        fpc.setPcaNumero(componentePlanGrado.getCpgPeriodosCalificacion());
                        fpc.setPcaSubModalidadAtencion(componentePlanGrado.getCpgPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion() != null ? componentePlanGrado.getCpgPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                        fpc.setPcaAnioLectivo(seccionEnEdicion.getSecAnioLectivo().getAlePk());
                        fpc.setPcaTipoCalendario(seccionEnEdicion.getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());

                        //Filtra dependiendo si periodo es anual o semestral
                        fpc.setPcaTipoPeriodo(seccionEnEdicion.getSecTipoPeriodo());
                        fpc.setPcaNumeroPeriodo(seccionEnEdicion.getSecNumeroPeriodo());

                        List<SgPeriodoCalificacion> listPeriodoCalif = restPeriodoCalificacion.buscar(fpc);
                        comboPeriodoCalificacion = new SofisComboG(listPeriodoCalif, "nombreTipoPeriodo");
                        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                        if (listPeriodoCalif.size() == 1) {
                            comboPeriodoCalificacion.setSelectedT(listPeriodoCalif.get(0));
                            cargarRangoFecha();
                        }
                        break;

                    case EXORD:
                        FiltroPeriodoCalendario fperCal = new FiltroPeriodoCalendario();
                        fperCal.setCesNivelFk(componentePlanGrado.getCpgPlanEstudio().getPesRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk());
                        fperCal.setCesModalidadAtencionFk(componentePlanGrado.getCpgPlanEstudio().getPesRelacionModalidad().getReaModalidadAtencion().getMatPk());
                        fperCal.setCesSubModalidadAtencionFk(componentePlanGrado.getCpgPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion() != null ? componentePlanGrado.getCpgPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                        fperCal.setFechaCalificacion(LocalDate.now());
                        fperCal.setCesHabilitado(Boolean.TRUE);
                        fperCal.setIncluirCampos(new String[]{
                            "cesTipo",
                            "cesVersion"
                        });

                        List<SgPeriodoCalendario> listPeriodoCalendario = calendarioEscolarRestClient.buscar(fperCal);
                        List<EnumCalendarioEscolar> listEnumCalendario = new ArrayList();
                        for (SgPeriodoCalendario periodoCalendario : listPeriodoCalendario) {
                            if (!listEnumCalendario.contains(periodoCalendario.getCesTipo())) {
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.PREC) && componentePlanGrado.getCpgCantidadPrimeraPrueba() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.PREC);
                                }
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.PRECPS) && componentePlanGrado.getCpgCantidadPrimeraSuficiencia() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.PRECPS);
                                }
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.SREC) && componentePlanGrado.getCpgCantidadSegundaPrueba() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.SREC);
                                }
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.SRECPS) && componentePlanGrado.getCpgCantidadSegundaSuficiencia() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.SRECPS);
                                }
                            }
                        }

                        comboTipoPeriodoCalendario = new SofisComboG(listEnumCalendario, "text");
                        comboTipoPeriodoCalendario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                        comboPeriodos = new ArrayList();
                        comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
                        if (listEnumCalendario.size() == 1) {
                            comboTipoPeriodoCalendario.setSelectedT(listEnumCalendario.get(0));
                            cargarRangoFechaCalendarioEscolar();
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarRangoFecha() {
        try {
            puedeCalificarComponente = Boolean.TRUE;
            estudiantesMatriculaAbierta = new HashMap<>();
            puedeGuardarArchivo = Boolean.FALSE;
            comboRangoFecha = new SofisComboG();
            comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            fechaCalificaciones = null;
            if (comboPeriodoCalificacion.getSelectedT() != null) {
                FiltroRangoFecha fre = new FiltroRangoFecha();
                fre.setPeriodoCalificacionPk(comboPeriodoCalificacion.getSelectedT().getPcaPk());
                fre.setHabilitado(!sessionBean.getOperaciones().contains(ConstantesOperaciones.CALIFICACIONES_SUPERUSUARIO) ? Boolean.TRUE : null);
                fre.setIncluirCampos(new String[]{
                    "rfeCodigo",
                    "rfeFechaDesde",
                    "rfeFechaDesde",
                    "rfeFechaHasta",
                    "rfeHabilitado",
                    "rfePeriodoCalificacion.pcaPk",
                    "rfePeriodoCalificacion.pcaNombre",
                    "rfePeriodoCalificacion.pcaNumero",
                    "rfePeriodoCalificacion.pcaPermiteCalificarSinNie",
                    "rfePeriodoCalificacion.pcaVersion",
                    "rfePeriodoCalificacion.pcaEsPrueba",
                    "rfeVersion"});
                fre.setOrderBy(new String[]{"rfeFechaDesde"});
                fre.setAscending(new boolean[]{true});
                List<SgRangoFecha> listrfe = rangoFechaRestClient.buscarConCache(fre);
                comboRangoFecha = new SofisComboG(listrfe, "rfeCodigoRango");
                comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarRangoFechaCalendarioEscolar() {
        try {
            puedeCalificarComponente = Boolean.TRUE;
            estudiantesMatriculaAbierta = new HashMap<>();
            comboPeriodoSeleccionado = 0;
            comboPeriodos = new ArrayList();
            comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            fechaCalificaciones = null;
            Integer cantidadPrueba = 0;
            if (comboTipoPeriodoCalendario.getSelectedT() != null) {
                switch (comboTipoPeriodoCalendario.getSelectedT()) {
                    case PREC:
                        cantidadPrueba = componentePlanGrado.getCpgCantidadPrimeraPrueba();
                        break;
                    case PRECPS:
                        cantidadPrueba = componentePlanGrado.getCpgCantidadPrimeraSuficiencia();
                        break;
                    case SREC:
                        cantidadPrueba = componentePlanGrado.getCpgCantidadSegundaPrueba();
                        break;
                    case SRECPS:
                        cantidadPrueba = componentePlanGrado.getCpgCantidadSegundaSuficiencia();
                        break;
                    default:
                        cantidadPrueba = 0;
                        break;
                }
                comboPeriodos = new ArrayList();
                comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
                for (int i = 1; i <= cantidadPrueba; i++) {
                    comboPeriodos.add(new SelectItem(i, Etiquetas.getValue("calificacion") + " " + i));
                }
                /*   if(cantidadPrueba==1){
                    comboPeriodoSeleccionado=1;
                    cargaTabla();
                }*/
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void dateSelect() {
        if (calificacionId != null || (comboRangoFecha != null && comboRangoFecha.getSelectedT() != null) || comboPeriodoSeleccionado > 0) {
            if (entidadEnEdicion != null) {
                for (SgCalificacionEstudiante calEst : entidadEnEdicion.getCalCalificacionesEstudiante()) {
                    calEst.setCaeFechaRealizado(fechaCalificaciones);
                }
            }
        }
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgCalificacionCE();
    }

    public void guardar() {
        try {
            if (entidadEnEdicion.getCalPk() == null) {
                switch (entidadEnEdicion.getCalTipoPeriodoCalificacion()) {
                    case ORD:
                        entidadEnEdicion.setCalRangoFecha(comboRangoFecha.getSelectedT());
                        entidadEnEdicion.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.ORD);
                        entidadEnEdicion.setCalTipoCalendarioEscolar(null);
                        entidadEnEdicion.setCalNumero(null);
                        break;
                    case EXORD:
                        entidadEnEdicion.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.EXORD);
                        entidadEnEdicion.setCalTipoCalendarioEscolar(comboTipoPeriodoCalendario.getSelectedT());
                        entidadEnEdicion.setCalNumero(comboPeriodoSeleccionado);
                        entidadEnEdicion.setCalRangoFecha(null);
                        break;
                    default:
                        break;
                }
                entidadEnEdicion.setCalComponentePlanEstudio(comboComponentePlanEstudio.getSelectedT());
                entidadEnEdicion.setCalSeccion(seccionEnEdicion);
            }

            entidadEnEdicion.setCalFecha(LocalDate.now());
            List<SgCalificacionEstudiante> calBeforeSave = entidadEnEdicion.getCalCalificacionesEstudiante();
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

            if (entidadEnEdicion.getCalPk() != null) {
                if (calificacionId != null) {
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    JSFUtils.redirectToPage(ConstantesPaginas.CALIFICACIONES);
                } else {
                    //this.agregar();
                    this.actualizar(this.obtenerPorIdCustom(entidadEnEdicion.getCalPk()));
                }
            } else {
                //No se guardo o se elimino porque todas las calificaciones tenian valor vacio
                //Restablecemos entidad
                nuevosEstudiantesSeccionSinCalificacion = Boolean.FALSE;
                for (SgCalificacionEstudiante c : calBeforeSave) {
                    c.setCaePk(null);
                    c.setCaeVersion(null);
                }
                entidadEnEdicion.setCalCalificacionesEstudiante(calBeforeSave);
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCalPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void configuracionImportacion() {
        try {
            if (importandoArchivo.equals(Boolean.TRUE)) {
                FiltroCodiguera fc = new FiltroCodiguera();
                fc.setCodigo(Constantes.CODIGO_IMPORTAR_CALIFICACIONES_ASINC);
                List<SgConfiguracion> configList = catalogoRestClient.buscarConfiguracion(fc);
                configuracionImport = configList.isEmpty() ? null : configList.get(0);

                fc.setCodigo(Constantes.IMPORTACION_CALIFICACION_MSJ);
                configList = catalogoRestClient.buscarConfiguracion(fc);
                configuracionImportMensaje = configList.isEmpty() ? null : configList.get(0);

                fc.setCodigo(Constantes.IMPORTACION_CALIFICACION_VARIOS_COMPONENTES);
                configList = catalogoRestClient.buscarConfiguracion(fc);

                //IMPORTACIÓN
                //Hay dos métodos para importar archivo, pero funciona uno solo a la vez en el sistema, esto depende de la configuracion
                //Uno es para importar un único componente configuracionMetodoImportacion=0
                //El otro es para importar de a muchos componentes configuracionMetodoImportacion=1
                configuracionMetodoImportacion = 0;
                if (!configList.isEmpty() && configList.get(0).getConValor().equals("1")) {
                    configuracionMetodoImportacion = 1;
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void calificarConArchivo(FileUploadEvent event) {
        try {
            puedeGuardarArchivo = Boolean.FALSE;

            SgArchivo arc = new SgArchivo();
            handleArchivoBean.subirArchivoTmp(event.getFile(), arc);
            archivoCalificaciones = new SgArchivoCalificaciones();
            archivoCalificaciones.setAccSeccionPk(seccionEnEdicion.getSecPk());
            archivoCalificaciones.setAccRangoFecha(comboRangoFecha.getSelectedT());
            archivoCalificaciones.setAccComponentePlanEstudio(comboComponentePlanEstudio.getSelectedT());
            archivoCalificaciones.setAccEscalaCalificacion(escalaCalificacion);
            archivoCalificaciones.setAccArchivo(arc);
            archivoCalificaciones.setAccFechaCalificado(fechaCalificacionImport);

            switch (configuracionMetodoImportacion) {
                case 0:
                    archivoCalificaciones.setAccEstado(EnumEstadoImportado.PROCESAMIENTO_DIRECTO);

                    if (configuracionImport != null && configuracionImport.getConValor().equals("1")) {
                        archivoCalificaciones.setAccEstado(EnumEstadoImportado.VALIDACION);
                        restClient.importar(archivoCalificaciones);
                    } else {
                        entidadEnEdicion = restClient.importar(archivoCalificaciones);
                    }
                    break;

                case 1:
                    archivoCalificaciones.setAccEstado(EnumEstadoImportado.VALIDACION);
                    restClient.importar(archivoCalificaciones);

                    break;

            }

            puedeGuardarArchivo = Boolean.TRUE;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.VALIDACION_ARCHIVO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajesError(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionFechaArchivo() {

    }

    public void guardarArchivo() {
        try {

            if (configuracionImport != null) {

                archivoCalificaciones.setAccFechaCalificado(fechaCalificacionImport);
                if (configuracionImport.getConValor().equals("1")) {
                    archivoCalificaciones.setAccEstado(EnumEstadoImportado.NO_PROCESADO);
                    archivoCalificaciones.setAccMetodoImportacion(configuracionMetodoImportacion.toString());
                    archivoCalificaciones = archivoCalificacionRestClient.guardar(archivoCalificaciones);
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ARCHIVO_EXITOSAMENTE_COLA), "");
                    puedeGuardarArchivo = Boolean.FALSE;
                    return;
                } else {
                    switch (configuracionMetodoImportacion) {
                        case 0:
                            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                            break;
                        case 1:
                            archivoCalificaciones.setAccEstado(EnumEstadoImportado.PROCESAMIENTO_DIRECTO);
                            archivoCalificaciones.setAccMetodoImportacion(configuracionMetodoImportacion.toString());
                            //  archivoCalificaciones = archivoCalificacionRestClient.guardar(archivoCalificaciones);
                            //  JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ARCHIVO_EXITOSAMENTE_COLA), "");
                            restClient.importar(archivoCalificaciones);
                            break;

                    }
                }
            }

            //entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            puedeGuardarArchivo = Boolean.FALSE;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void obtenerFormula() {
        try {
            formulaHabilitaPExtraordinario = new SgFormula();
            parametroFormulaHabilitaPExtraordinario = null;
            EnumCalendarioEscolar tipoCalendario;
            if (entidadEnEdicion.getCalPk() != null) {
                tipoCalendario = entidadEnEdicion.getCalTipoCalendarioEscolar();
            } else {
                tipoCalendario = comboTipoPeriodoCalendario.getSelectedT();
            }
            switch (tipoCalendario) {
                case PREC:
                    formulaHabilitaPExtraordinario = componentePlanGrado.getCpgFormulaHabilitacionPP();
                    parametroFormulaHabilitaPExtraordinario = componentePlanGrado.getCpgParametroFormulaPruebaPP();
                    break;
                case PRECPS:
                    formulaHabilitaPExtraordinario = componentePlanGrado.getCpgFormulaHabilitacionPS();
                    parametroFormulaHabilitaPExtraordinario = componentePlanGrado.getCpgParametroFormulaPruebaPS();
                    break;
                case SREC:
                    formulaHabilitaPExtraordinario = componentePlanGrado.getCpgFormulaHabilitacionSP();
                    parametroFormulaHabilitaPExtraordinario = componentePlanGrado.getCpgParametroFormulaPruebaSP();
                    break;
                case SRECPS:
                    formulaHabilitaPExtraordinario = componentePlanGrado.getCpgFormulaHabilitacionPP();
                    parametroFormulaHabilitaPExtraordinario = componentePlanGrado.getCpgParametroFormulaPruebaSS();
                    break;
                default:
            }

            if (formulaHabilitaPExtraordinario == null) {
                FiltroRelGradoPlanEstudio frgp = new FiltroRelGradoPlanEstudio();
                frgp.setRgpGrado(seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk());
                frgp.setRgpPlanEstudio(seccionEnEdicion.getSecPlanEstudio().getPesPk());
                frgp.setIncluirCampos(new String[]{
                    "rgpFormulaHabilitacionPP.fomTextoLargo",
                    "rgpFormulaHabilitacionPP.fomPk",
                    "rgpFormulaHabilitacionPP.fomTienSubformula",
                    "rgpFormulaHabilitacionPS.fomTextoLargo",
                    "rgpFormulaHabilitacionPS.fomPk",
                    "rgpFormulaHabilitacionPS.fomTienSubformula",
                    "rgpFormulaHabilitacionSP.fomTextoLargo",
                    "rgpFormulaHabilitacionSP.fomPk",
                    "rgpFormulaHabilitacionSP.fomTienSubformula",
                    "rgpFormulaHabilitacionSS.fomTextoLargo",
                    "rgpFormulaHabilitacionSS.fomTienSubformula",
                    "rgpFormulaHabilitacionSS.fomPk"});
                List<SgRelGradoPlanEstudio> listRelGra = relGradoPlanEstudioClient.buscarConCache(frgp);
                if (!listRelGra.isEmpty()) {
                    switch (tipoCalendario) {
                        case PREC:
                            formulaHabilitaPExtraordinario = listRelGra.get(0).getRgpFormulaHabilitacionPP();
                            break;
                        case PRECPS:
                            formulaHabilitaPExtraordinario = listRelGra.get(0).getRgpFormulaHabilitacionPS();
                            break;
                        case SREC:
                            formulaHabilitaPExtraordinario = listRelGra.get(0).getRgpFormulaHabilitacionSP();
                            break;
                        case SRECPS:
                            formulaHabilitaPExtraordinario = listRelGra.get(0).getRgpFormulaHabilitacionSS();
                            break;
                        default:
                    }
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void datosHabilitacionPeriodoExtraordinario(SgComponentePlanEstudio componentePlan) {
        try {
            obtenerFormula();
            if (formulaHabilitaPExtraordinario != null) {
                if (componentePlan != null) {
                    if (cpeEvaluadaHabilitarPExtra == null || !componentePlan.getCpePk().equals(cpeEvaluadaHabilitarPExtra.getCpePk())) {
                        SgPorcentajeAsistenciasRequest datosCalificacion = new SgPorcentajeAsistenciasRequest();
                        datosCalificacion.setPinEstudiantes(listEstudiantes);
                        datosCalificacion.setComponentePlanEstudio(componentePlan.getCpePk());
                        datosCalificacion.setComponentePlanEstudioPrueba(parametroFormulaHabilitaPExtraordinario != null ? parametroFormulaHabilitaPExtraordinario.getCpgComponentePlanEstudio().getCpePk() : null);
                        datosCalificacion.setPinSeccion(seccionEnEdicion.getSecPk());
                        datosCalificacion.setComponentePlanGrado(componentePlanGrado);
                        listDatosHabilitadoPExtraordinario = restClient.datosHabilitacionPeriodoExtraordinario(datosCalificacion);
                        seccionEvaluadaHabilitarPExtra = seccionEnEdicion;
                        cpeEvaluadaHabilitarPExtra = componentePlan;
                    }
                }
                habilitacionEstudiantePeriodoExtraordinario();
            } else {
                listDatosHabilitadoPExtraordinario = new ArrayList();
                habilitacionEstudiantePeriodoExtraordinario();
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private Function cargarSubformula(SgFormula formula) {
        Function f = new Function(formula.getFomTextoLargo());
        if (formula.getFomTienSubformula()) {
            for (SgFormula fr : formula.getFomSubFormula()) {
                f.addDefinitions(cargarSubformula(fr));
            }
        }
        return f;
    }

    public void habilitacionEstudiantePeriodoExtraordinario() {
        try {
            estudiantesHabilitados = new HashMap<>();

            if (formulaHabilitaPExtraordinario != null) {
                if (formulaHabilitaPExtraordinario.getFomPk() != null && !StringUtils.isBlank(formulaHabilitaPExtraordinario.getFomTextoLargo())) {
                    Function funcion = new Function(formulaHabilitaPExtraordinario.getFomTextoLargo());

                    if (BooleanUtils.isTrue(formulaHabilitaPExtraordinario.getFomTienSubformula())) {
                        //SE UTILIZA OBTENERPORID A MODO DE OBTENER LA LISTA DE SUBFORMULAS
                        FiltroFormula ff = new FiltroFormula();
                        ff.setIncluirSubformulas(Boolean.TRUE);
                        ff.setFormulaPk(formulaHabilitaPExtraordinario.getFomPk());
                        List<SgFormula> form = catalogoRestClient.buscarFormulas(ff);
                        formulaHabilitaPExtraordinario = form.get(0);
                        funcion = cargarSubformula(formulaHabilitaPExtraordinario);
                    }

                    Function formulaAx0 = new Function(formulasAuxiliares[0]);
                    Function formulaAx1 = new Function(formulasAuxiliares[1]);
                    formulaAx1.addFunctions(formulaAx0);
                    funcion.addDefinitions(formulaAx0);
                    funcion.addDefinitions(formulaAx1);

                    String parteDefinicion;

                    String[] formulaSeparada = formulaHabilitaPExtraordinario.getFomTextoLargo().split("=", 2);
                    parteDefinicion = formulaSeparada[0];
                    parteDefinicion = parteDefinicion.trim();
                    Boolean errorEnFormula = Boolean.FALSE;

                    for (SgPorcentajeAsistenciasResponse datoHabilitacion : listDatosHabilitadoPExtraordinario) {

                        if (BooleanUtils.isFalse(datoHabilitacion.getPinEstudianteConTodosLosPeriodosCalificados())) {
                            estudiantesHabilitados.put(datoHabilitacion.getPinEstudiante().getEstPk(), Boolean.FALSE);
                            continue;
                        }

                        Expression expresion = new Expression(parteDefinicion, funcion);
                        Argument arg_asis = new Argument("asistencias=" + datoHabilitacion.getPinCantidadAsistencias());
                        expresion.addArguments(arg_asis);
                        Argument arg_notaAsis = new Argument("ni=" + datoHabilitacion.getPinNotaInstitucional());
                        expresion.addArguments(arg_notaAsis);
                        Argument arg_cantidad = new Argument("cantidadAsignReprobadas=" + datoHabilitacion.getPinCantidadNoAprobado());
                        expresion.addArguments(arg_cantidad);
                        Argument arg_nota_eval = new Argument("NOTAEVAL=" + datoHabilitacion.getPinNotaInstitucionalPrueba());
                        expresion.addArguments(arg_nota_eval);
                        Argument arg_mayor_nota = new Argument("mayorNota=" + datoHabilitacion.getPinMayorNota());
                        expresion.addArguments(arg_mayor_nota);

                        Argument arg_ppe = new Argument("ppe", Double.NaN);
                        expresion.addArguments(arg_ppe);
                        if (datoHabilitacion.getPinPpe() != null) {
                            expresion.setArgumentValue("ppe", datoHabilitacion.getPinPpe());
                        }
                        Argument arg_ppeps = new Argument("ppeps", Double.NaN);
                        expresion.addArguments(arg_ppeps);
                        if (datoHabilitacion.getPinPpeps() != null) {
                            expresion.setArgumentValue("ppeps", datoHabilitacion.getPinPpeps());
                        }
                        Argument arg_spe = new Argument("spe", Double.NaN);
                        expresion.addArguments(arg_spe);
                        if (datoHabilitacion.getPinSpe() != null) {
                            expresion.setArgumentValue("spe", datoHabilitacion.getPinSpe());
                        }
                        Argument arg_sppe = new Argument("sppe", Double.NaN);
                        expresion.addArguments(arg_sppe);
                        if (datoHabilitacion.getPinSppe() != null) {
                            expresion.setArgumentValue("sppe", datoHabilitacion.getPinSppe());
                        }

                        if (componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimoAprobacion() != null) {
                            Argument arg_speps = new Argument("ma=" + componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimoAprobacion().toString());
                            expresion.addArguments(arg_speps);
                        }

                        Double res = expresion.calculate();

                        if (Double.compare(res, 0) == 0 || Double.isNaN(res)) {
                            if (!estudiantesHabilitados.containsKey(datoHabilitacion.getPinEstudiante().getEstPk())) {
                                estudiantesHabilitados.put(datoHabilitacion.getPinEstudiante().getEstPk(), Boolean.FALSE);
                            }
                            if (Double.isNaN(res) && !errorEnFormula) {
                                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP_4, Mensajes.obtenerMensaje(Mensajes.ERROR_EN_FORMULA), "");
                                errorEnFormula = Boolean.TRUE;
                            }
                        } else {
                            if (!estudiantesHabilitados.containsKey(datoHabilitacion.getPinEstudiante().getEstPk())) {
                                estudiantesHabilitados.put(datoHabilitacion.getPinEstudiante().getEstPk(), Boolean.TRUE);
                            }
                        }
                    }
                }
            } else {
                for (SgEstudiante est : listEstudiantes) {
                    estudiantesHabilitados.put(est.getEstPk(), Boolean.FALSE);
                }
            }

            if (EnumTiposPeriodosCalificaciones.EXORD.equals(entidadEnEdicion.getCalTipoPeriodoCalificacion())) {
                Iterator<SgCalificacionEstudiante> it = this.entidadEnEdicion.getCalCalificacionesEstudiante().iterator();
                while (it.hasNext()) {
                    SgCalificacionEstudiante cal = it.next();
                    if (!verificarEstudianteHabilitadoPExtraordinario(cal.getCaeEstudiante().getEstPk())) {
                        it.remove();
                        if (nuevosEstudiantesSinCalificacion != null) {
                            nuevosEstudiantesSinCalificacion.remove(cal.getCaeEstudiante());
                        }
                    }
                }
                if (nuevosEstudiantesSinCalificacion == null || nuevosEstudiantesSinCalificacion.isEmpty()) {
                    nuevosEstudiantesSeccionSinCalificacion = Boolean.FALSE;
                }
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public Boolean verificarEstudianteHabilitadoPExtraordinario(Long estudiante) {
        Boolean retorno = Boolean.TRUE;
        if (EnumTiposPeriodosCalificaciones.EXORD.equals(entidadEnEdicion.getCalTipoPeriodoCalificacion())) {
            if (estudiantesHabilitados.containsKey(estudiante)) {
                retorno = estudiantesHabilitados.get(estudiante);

            }
        }

        return retorno;
    }

    public String fechaComoString(SgCalificacionEstudiante est) {
        if (est != null) {
            if (est.getCaeFechaRealizado() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
                return formatter.format(est.getCaeFechaRealizado());
            }
        }
        return null;
    }
    
    public String obtenerValorCalificacion(SgCalificacionEstudiante est) {
        if (est != null) {      
                return est.getCaeCalificacionValor();
          
        }
        return null;
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgCalificacionCE getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCalificacionCE entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazyCalificacionDataModel getCalificacionLazyModel() {
        return calificacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCalificacionDataModel calificacionLazyModel) {
        this.calificacionLazyModel = calificacionLazyModel;
    }

    public List<SgEstudiante> getListEstudiantes() {
        return listEstudiantes;
    }

    public void setListEstudiantes(List<SgEstudiante> listEstudiantes) {
        this.listEstudiantes = listEstudiantes;
    }

    public SofisComboG<SgComponentePlanEstudio> getComboComponentePlanEstudio() {
        return comboComponentePlanEstudio;
    }

    public void setComboComponentePlanEstudio(SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudio) {
        this.comboComponentePlanEstudio = comboComponentePlanEstudio;
    }

    public SofisComboG<SgCalificacion>[] getComboCalificacion() {
        return comboCalificacion;
    }

    public void setComboCalificacion(SofisComboG<SgCalificacion>[] comboCalificacion) {
        this.comboCalificacion = comboCalificacion;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<SgCalificacionEstudiante> getCalificacionesEstList() {
        return calificacionesEstList;
    }

    public void setCalificacionesEstList(List<SgCalificacionEstudiante> calificacionesEstList) {
        this.calificacionesEstList = calificacionesEstList;
    }

    public SofisComboG<SgPeriodoCalificacion> getComboPeriodoCalificacion() {
        return comboPeriodoCalificacion;
    }

    public void setComboPeriodoCalificacion(SofisComboG<SgPeriodoCalificacion> comboPeriodoCalificacion) {
        this.comboPeriodoCalificacion = comboPeriodoCalificacion;
    }

    public SofisComboG<SgRangoFecha> getComboRangoFecha() {
        return comboRangoFecha;
    }

    public void setComboRangoFecha(SofisComboG<SgRangoFecha> comboRangoFecha) {
        this.comboRangoFecha = comboRangoFecha;
    }

    public SgEscalaCalificacion getEscalaCalificacion() {
        return escalaCalificacion;
    }

    public void setEscalaCalificacion(SgEscalaCalificacion escalaCalificacion) {
        this.escalaCalificacion = escalaCalificacion;
    }

    public SgSeccion getSeccionEnEdicion() {
        return seccionEnEdicion;
    }

    public void setSeccionEnEdicion(SgSeccion seccionEnEdicion) {
        this.seccionEnEdicion = seccionEnEdicion;
    }

    public Long getSeccionId() {
        return seccionId;
    }

    public void setSeccionId(Long seccionId) {
        this.seccionId = seccionId;
    }

    public Boolean getEscalaNumerica() {
        return escalaNumerica;
    }

    public void setEscalaNumerica(Boolean escalaNumerica) {
        this.escalaNumerica = escalaNumerica;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<String> getComboPeriodoCalendario() {
        return comboPeriodoCalendario;
    }

    public void setComboPeriodoCalendario(SofisComboG<String> comboPeriodoCalendario) {
        this.comboPeriodoCalendario = comboPeriodoCalendario;
    }

    public SofisComboG<EnumCalendarioEscolar> getComboTipoPeriodoCalendario() {
        return comboTipoPeriodoCalendario;
    }

    public void setComboTipoPeriodoCalendario(SofisComboG<EnumCalendarioEscolar> comboTipoPeriodoCalendario) {
        this.comboTipoPeriodoCalendario = comboTipoPeriodoCalendario;
    }

    public SgComponentePlanGrado getComponentePlanGrado() {
        return componentePlanGrado;
    }

    public void setComponentePlanGrado(SgComponentePlanGrado componentePlanGrado) {
        this.componentePlanGrado = componentePlanGrado;
    }

    public Integer getCantidadPruebaComponentePlanGrado() {
        return cantidadPruebaComponentePlanGrado;
    }

    public void setCantidadPruebaComponentePlanGrado(Integer cantidadPruebaComponentePlanGrado) {
        this.cantidadPruebaComponentePlanGrado = cantidadPruebaComponentePlanGrado;
    }

    public List<SelectItem> getComboPeriodos() {
        return comboPeriodos;
    }

    public void setComboPeriodos(List<SelectItem> comboPeriodos) {
        this.comboPeriodos = comboPeriodos;
    }

    public Integer getComboPeriodoSeleccionado() {
        return comboPeriodoSeleccionado;
    }

    public void setComboPeriodoSeleccionado(Integer comboPeriodoSeleccionado) {
        this.comboPeriodoSeleccionado = comboPeriodoSeleccionado;
    }

    public List<EnumTiposPeriodosCalificaciones> getPeriodoOrdinarioList() {
        return periodoOrdinarioList;
    }

    public void setPeriodoOrdinarioList(List<EnumTiposPeriodosCalificaciones> periodoOrdinarioList) {
        this.periodoOrdinarioList = periodoOrdinarioList;
    }

    public Long getCalificacionId() {
        return calificacionId;
    }

    public LocalDate getFechaCalificaciones() {
        return fechaCalificaciones;
    }

    public void setFechaCalificaciones(LocalDate fechaCalificaciones) {
        this.fechaCalificaciones = fechaCalificaciones;
    }

    public Boolean getImportandoArchivo() {
        return importandoArchivo;
    }

    public void setImportandoArchivo(Boolean importandoArchivo) {
        this.importandoArchivo = importandoArchivo;
    }

    public Boolean getPuedeGuardarArchivo() {
        return puedeGuardarArchivo;
    }

    public void setPuedeGuardarArchivo(Boolean puedeGuardarArchivo) {
        this.puedeGuardarArchivo = puedeGuardarArchivo;
    }

    public SgArchivoCalificaciones getArchivoCalificaciones() {
        return archivoCalificaciones;
    }

    public void setArchivoCalificaciones(SgArchivoCalificaciones archivoCalificaciones) {
        this.archivoCalificaciones = archivoCalificaciones;
    }

    public SgConfiguracion getConfiguracionImport() {
        return configuracionImport;
    }

    public void setConfiguracionImport(SgConfiguracion configuracionImport) {
        this.configuracionImport = configuracionImport;
    }

    public Boolean getNuevosEstudiantesSeccionSinCalificacion() {
        return nuevosEstudiantesSeccionSinCalificacion;
    }

    public void setNuevosEstudiantesSeccionSinCalificacion(Boolean nuevosEstudiantesSeccionSinCalificacion) {
        this.nuevosEstudiantesSeccionSinCalificacion = nuevosEstudiantesSeccionSinCalificacion;
    }

    public List<SgCalificacionEstudiante> getOtrasCalificaciones() {
        return otrasCalificaciones;
    }

    public void setOtrasCalificaciones(List<SgCalificacionEstudiante> otrasCalificaciones) {
        this.otrasCalificaciones = otrasCalificaciones;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }

    public String getTipoImportArchivo() {
        return tipoImportArchivo;
    }

    public void setTipoImportArchivo(String tipoImportArchivo) {
        this.tipoImportArchivo = tipoImportArchivo;
    }

    public Boolean getCalificarSinNie() {
        return calificarSinNie;
    }

    public Boolean getCalificarSinMatriculaValidada() {
        return calificarSinMatriculaValidada;
    }

    public Boolean getCalificarConMatriculaProvisional() {
        return calificarConMatriculaProvisional;
    }

    public List<SgPorcentajeAsistenciasResponse> getListDatosHabilitadoPExtraordinario() {
        return listDatosHabilitadoPExtraordinario;
    }

    public void setListDatosHabilitadoPExtraordinario(List<SgPorcentajeAsistenciasResponse> listDatosHabilitadoPExtraordinario) {
        this.listDatosHabilitadoPExtraordinario = listDatosHabilitadoPExtraordinario;
    }

    public SgComponentePlanGrado getParametroFormulaHabilitaPExtraordinario() {
        return parametroFormulaHabilitaPExtraordinario;
    }

    public void setParametroFormulaHabilitaPExtraordinario(SgComponentePlanGrado parametroFormulaHabilitaPExtraordinario) {
        this.parametroFormulaHabilitaPExtraordinario = parametroFormulaHabilitaPExtraordinario;
    }

    public String[] getIncluirCamposSeccion() {
        return incluirCamposSeccion;
    }

    public HashMap<Long, Boolean> getEstudiantesMatriculaAbierta() {
        return estudiantesMatriculaAbierta;
    }

    public void setEstudiantesMatriculaAbierta(HashMap<Long, Boolean> estudiantesMatriculaAbierta) {
        this.estudiantesMatriculaAbierta = estudiantesMatriculaAbierta;
    }

    public List<SgComponentePlanGrado> getComponentesImportacion() {
        return componentesImportacion;
    }

    public void setComponentesImportacion(List<SgComponentePlanGrado> componentesImportacion) {
        this.componentesImportacion = componentesImportacion;
    }

    public List<String> getMsjImportacionComponentesPosiblesList() {
        return msjImportacionComponentesPosiblesList;
    }

    public void setMsjImportacionComponentesPosiblesList(List<String> msjImportacionComponentesPosiblesList) {
        this.msjImportacionComponentesPosiblesList = msjImportacionComponentesPosiblesList;
    }

    public LocalDate getFechaCalificacionImport() {
        return fechaCalificacionImport;
    }

    public void setFechaCalificacionImport(LocalDate fechaCalificacionImport) {
        this.fechaCalificacionImport = fechaCalificacionImport;
    }

    public SgConfiguracion getConfiguracionImportMensaje() {
        return configuracionImportMensaje;
    }

    public void setConfiguracionImportMensaje(SgConfiguracion configuracionImportMensaje) {
        this.configuracionImportMensaje = configuracionImportMensaje;
    }

    public Integer getConfiguracionMetodoImportacion() {
        return configuracionMetodoImportacion;
    }

    public void setConfiguracionMetodoImportacion(Integer configuracionMetodoImportacion) {
        this.configuracionMetodoImportacion = configuracionMetodoImportacion;
    }

    public SgPlantilla getPlantillaImportacion() {
        return plantillaImportacion;
    }

    public void setPlantillaImportacion(SgPlantilla plantillaImportacion) {
        this.plantillaImportacion = plantillaImportacion;
    }

    public Boolean getPuedeCalificarComponente() {
        return puedeCalificarComponente;
    }

    public void setPuedeCalificarComponente(Boolean puedeCalificarComponente) {
        this.puedeCalificarComponente = puedeCalificarComponente;
    }

}
