/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import org.omnifaces.cdi.Param;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgAsistencia;
import sv.gob.mined.siges.web.dto.SgControlAsistenciaCabezal;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoInasistencia;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroControlAsistenciaCabezal;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AsistenciaRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ControlAsistenciaCabezalRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgDiaLectivoHorarioEscolar;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.enumerados.EnumAmbito;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumCeldaDiaHora;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroActividadCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiaLectivoHorarioEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.restclient.ActividadCalendarioRestClient;
import sv.gob.mined.siges.web.restclient.DiaLectivoHorarioEscolarRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ControlAsistenciaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ControlAsistenciaBean.class.getName());

    @Inject
    private ControlAsistenciaCabezalRestClient restClient;

    @Inject
    private SeccionRestClient restSeccion;

    @Inject
    private MatriculaRestClient restMatricula;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private AsistenciaRestClient asistenciaClient;

    @Inject
    private ActividadCalendarioRestClient actividadCalendarioClient;

    @Inject
    private DiaLectivoHorarioEscolarRestClient diaLectivoHorarioEscolarClient;

    @Inject
    @Param(name = "seccionId")
    private Long seccionId;

    @Inject
    @Param(name = "id")
    private Long controlAsisId;

    @Inject
    @Param(name = "rev")
    private Long controlAsisRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    private SessionBean sessionBean;

    private Boolean soloLectura = Boolean.FALSE;
    private SgControlAsistenciaCabezal entidadEnEdicion;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;

    private SgProgramaEducativo programaEd;
    private SofisComboG<SgMotivoInasistencia>[] combosInasistencia;
    private List<SgMotivoInasistencia> motivosInasistencia;
    private Boolean controlRealizado = Boolean.FALSE;
    private Boolean nuevosEstudiantesSeccionSinControlAsistencia = Boolean.FALSE;
    private SofisComboG<SgMotivoInasistencia> combosInasistenciaAplicarTodos;
    private String observacionAplicaTodos;
    private Boolean faltoAplicaTodos = Boolean.FALSE;
    private Boolean diaNoLectivo = Boolean.FALSE;
    private Boolean habilitadoDiaLectivo = Boolean.FALSE;
    private SgSeccion seccionSeleccionada;
    private String[] incluirCamposSeccion = new String[]{
        "secServicioEducativo.sduGrado.graPk",
        "secServicioEducativo.sduSede.sedTipoCalendario.tcePk",
        "secServicioEducativo.sduSede.sedPk",
        "secServicioEducativo.sduSede.sedTipo",
        "secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk",
        "secPlanEstudio.pesPk",
        "secAnioLectivo.alePk",
        "secAnioLectivo.aleEstado",
        "secNombre",
        "secCodigo",
        "secEstado",
        "secVersion"};

    public ControlAsistenciaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (controlAsisId != null && controlAsisId > 0) {
                if (controlAsisRev != null && controlAsisRev > 0) {
                    this.actualizar(restClient.obtenerEnRevision(controlAsisId, controlAsisRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    FiltroControlAsistenciaCabezal filtro = new FiltroControlAsistenciaCabezal();
                    filtro.setCacPk(controlAsisId);
                    filtro.setIncluirAsistencias(Boolean.TRUE);
                    filtro.setIncluirAsistencias(Boolean.TRUE);
                    filtro.setIncluirCampos(new String[]{
                        "cacFecha",
                        "cacEstudiantesPresentes",
                        "cacEstudiantesEnLista",
                        "cacEstudiantesAusentesJustificados",
                        "cacEstudiantesAusentesSinJustificar",
                        "cacVersion",
                        "cacProcesoDeCreacion",
                        "cacSeccion.secPk",
                        "cacSeccion.secEstado",
                        "cacSeccion.secAnioLectivo.aleAnio",
                        "cacSeccion.secAnioLectivo.alePk",
                        "cacSeccion.secAnioLectivo.aleEstado",
                        "cacSeccion.secServicioEducativo.sduSede.sedNombre",
                        "cacSeccion.secServicioEducativo.sduSede.sedCodigo",
                        "cacSeccion.secServicioEducativo.sduSede.sedPk",
                        "cacSeccion.secServicioEducativo.sduSede.sedVersion",
                        "cacSeccion.secServicioEducativo.sduSede.sedTipo",
                        "cacSeccion.secServicioEducativo.sduSede.sedTipoCalendario.tcePk",
                        "cacSeccion.secServicioEducativo.sduSede.sedTipoCalendario.tceVersion",
                        "cacSeccion.secServicioEducativo.sduGrado.graPk",
                        "cacSeccion.secServicioEducativo.sduGrado.graNombre",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaPk",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivCodigo",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoCodigo",
                        "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk",
                        "cacSeccion.secServicioEducativo.sduOpcion.opcNombre",
                        "cacSeccion.secServicioEducativo.sduOpcion.opcPk",
                        "cacSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
                        "cacSeccion.secServicioEducativo.sduProgramaEducativo.pedPk",
                        "cacSeccion.secNombre",
                        "cacSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk",
                        "cacSeccion.secVersion"});
                    SgControlAsistenciaCabezal asist = restClient.buscar(filtro).get(0);
                    this.actualizar(asist);
                    soloLectura = editable != null ? !editable : soloLectura;
                    if (EnumSeccionEstado.CERRADA.equals(asist.getCacSeccion().getSecEstado())) {
                        soloLectura = Boolean.TRUE;
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.WARNING_SECCION_CERRADO), "");
                    }
                    if (asist.getCacSeccion().getSecAnioLectivo().getAleEstado().equals(EnumAnioLectivoEstado.CERRADO)) {
                        soloLectura = Boolean.TRUE;
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.WARNING_CONTROL_ASISTENCIA_ANIO_LECTIVO_CERRADO), "");
                    }
                }
            } else {
                this.agregar();
                if (seccionId != null && seccionId > 0) {
                    SgSeccion sec = restSeccion.obtenerPorId(seccionId);
                    cargarAsistencias(restSeccion.obtenerPorId(seccionId));
                    if (EnumSeccionEstado.CERRADA.equals(sec.getSecEstado())) {
                        soloLectura = Boolean.TRUE;
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.WARNING_SECCION_CERRADO), "");
                    }
                    if (sec.getSecAnioLectivo().getAleEstado().equals(EnumAnioLectivoEstado.CERRADO)) {
                        soloLectura = Boolean.TRUE;
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.WARNING_CONTROL_ASISTENCIA_ANIO_LECTIVO_CERRADO), "");
                    }
                }
            }
            validarAcceso();
            habilitadoFuncionalidadDiaLectivo();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_CABEZAL)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getCacPk() == null) {
                if (!(sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_CONTROL_ASISTENCIA_CABEZAL)
                        || sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_CONTROL_ASISTENCIA_CABEZAL_ARCHIVO))) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_CONTROL_ASISTENCIA_CABEZAL)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public void habilitadoFuncionalidadDiaLectivo() {
        try {
            habilitadoDiaLectivo = Boolean.FALSE;
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.HABILITAR_CONTROL_DIA_LECTIVO);
            List<SgConfiguracion> confList = restCatalogo.buscarConfiguracion(fc);
            if (!confList.isEmpty()) {
                SgConfiguracion conf = confList.get(0);
                if (conf.getConValor().equals("1")) {
                    habilitadoDiaLectivo = Boolean.TRUE;
                }
            }

        } catch (Exception exp) {

        }
    }

    public String getTituloPagina() {
        if (this.controlAsisId == null) {
            return Etiquetas.getValue("agregarControlAsistencia");
        } else if (this.controlAsisRev != null) {
            return Etiquetas.getValue("historialControlAsistencia");
            //return Etiquetas.getValue("historialControlAsistencia") + " " + entidadEnEdicion.getCacSeccion().getSecServicioEducativo().getSduGrado().getGraNombre()+"-"+seccionSelecionado.getSecNombre() entidadEnEdicion.getCacFecha() + " (" + entidadEnEdicion.getSedUltModUsuario() + (entidadEnEdicion.getSedUltModFecha() != null ? (" " + this.appBean.getDateTimeFormater().format(entidadEnEdicion.getSedUltModFecha())) : "") + ")";
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verControlAsistencia") + " " + entidadEnEdicion.getCacSeccion().getSecNombre();
        } else {
            return Etiquetas.getValue("edicionControlAsistencia") + " " + entidadEnEdicion.getCacSeccion().getSecNombre();
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"minNombre"});
            motivosInasistencia = restCatalogo.buscarMotivoInasistencia(fc);
            combosInasistenciaAplicarTodos = new SofisComboG(this.motivosInasistencia, "minNombre");
            combosInasistenciaAplicarTodos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void aplicarTodos() {
        Integer i = 0;
        if (faltoAplicaTodos && combosInasistenciaAplicarTodos.getSelectedT() == null) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Debe seleccionar Motivo de inasistencia", "");
            return;
        }

        for (SgAsistencia asis : entidadEnEdicion.getCacAsistencia()) {
            if (combosInasistenciaAplicarTodos.getSelectedT() != null) {
                combosInasistencia[i].setSelectedT(combosInasistenciaAplicarTodos.getSelectedT());
                asis.setAsiMotivoInasistencia(combosInasistenciaAplicarTodos.getSelectedT());
            } else {
                combosInasistencia[i].setSelected(-1);
                asis.setAsiMotivoInasistencia(null);
            }
            asis.setAsiObservacion(observacionAplicaTodos);
            asis.setAsiInasistencia(faltoAplicaTodos);
            i++;
        }
    }

    public void elegirFaltoAplicaTodos() {
        if (BooleanUtils.isFalse(faltoAplicaTodos)) {
            combosInasistenciaAplicarTodos.setSelected(-1);
            observacionAplicaTodos = null;
        }
    }

    public void agregar() {
        entidadEnEdicion = new SgControlAsistenciaCabezal();
        entidadEnEdicion.setCacEstudiantesAusentesJustificados(0);
        entidadEnEdicion.setCacEstudiantesAusentesSinJustificar(0);
    }
    
    public void inicializarAplicarTodos(){
        combosInasistenciaAplicarTodos.setSelected(-1);
        faltoAplicaTodos=Boolean.FALSE;
        observacionAplicaTodos=null;
    }

    public void dateSelect(SelectEvent event) {
        try {
            entidadEnEdicion.setCacPk(null);
            entidadEnEdicion.setCacVersion(null);
            this.cargarAsistencias(seccionSeleccionada);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarSeccion(SgSeccion seccion) {

        this.agregar();

        if (seccion != null) {
            cargarAsistencias(seccion);
        }
    }

    public void cargarAsistencias(SgSeccion seccion) {
        try {
            inicializarAplicarTodos();
            seccionSeleccionada = seccion;
            diaNoLectivo = Boolean.FALSE;
            this.entidadEnEdicion.setCacSeccion(seccion);
            this.nuevosEstudiantesSeccionSinControlAsistencia = Boolean.FALSE;
            if (seccionSeleccionada != null) {
                if (entidadEnEdicion.getCacFecha() != null) {
                    Boolean esDiaLectivo = fechaEsDiaLectivo(entidadEnEdicion.getCacFecha());
                    if (BooleanUtils.isFalse(esDiaLectivo)) {
                        diaNoLectivo = Boolean.TRUE;
                        entidadEnEdicion.setCacFecha(null);
                        this.entidadEnEdicion.setCacAsistencia(new ArrayList<>());
                        this.entidadEnEdicion.setCacEstudiantesAusentesJustificados(0);
                        this.entidadEnEdicion.setCacEstudiantesAusentesSinJustificar(0);
                        return;
                    }

                    FiltroControlAsistenciaCabezal filtro = new FiltroControlAsistenciaCabezal();
                    filtro.setSecPk(entidadEnEdicion.getCacSeccion().getSecPk());
                    filtro.setCacFecha(entidadEnEdicion.getCacFecha());
                    filtro.setIncluirAsistencias(Boolean.TRUE);
                    filtro.setIncluirCampos(new String[]{
                        "cacFecha",
                        "cacEstudiantesPresentes",
                        "cacEstudiantesEnLista",
                        "cacEstudiantesAusentesJustificados",
                        "cacEstudiantesAusentesSinJustificar",
                        "cacVersion",
                        "cacProcesoDeCreacion",
                        "cacSeccion.secPk",
                        "cacSeccion.secEstado",
                        "cacSeccion.secAnioLectivo.aleAnio",
                        "cacSeccion.secAnioLectivo.alePk",
                        "cacSeccion.secAnioLectivo.aleEstado",
                        "cacSeccion.secServicioEducativo.sduSede.sedPk",
                        "cacSeccion.secServicioEducativo.sduSede.sedTipo",
                        "cacSeccion.secServicioEducativo.sduSede.sedVersion",
                        "cacSeccion.secServicioEducativo.sduSede.sedTipoCalendario.tcePk",
                        "cacSeccion.secServicioEducativo.sduSede.sedTipoCalendario.tceVersion",
                        "cacSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk",
                        "cacSeccion.secVersion",});

                    List<SgControlAsistenciaCabezal> listControl = restClient.buscar(filtro);

                    if (!listControl.isEmpty()) {
                        this.actualizar(listControl.get(0));
                    } else {
                        this.controlRealizado = Boolean.FALSE;
                        this.entidadEnEdicion.setCacAsistencia(new ArrayList<>());
                        this.entidadEnEdicion.setCacEstudiantesAusentesJustificados(0);
                        this.entidadEnEdicion.setCacEstudiantesAusentesSinJustificar(0);

                        List<SgEstudiante> listaEstudiantes = buscarEstudiantes();
                        combosInasistencia = new SofisComboG[listaEstudiantes.size()];
                        for (int i = 0; i < listaEstudiantes.size(); i++) {
                            SgEstudiante est = listaEstudiantes.get(i);
                            combosInasistencia[i] = new SofisComboG(this.motivosInasistencia, "minNombre");
                            combosInasistencia[i].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                            SgAsistencia sgAsi = new SgAsistencia();
                            sgAsi.setAsiEstudiante(est);
                            this.entidadEnEdicion.getCacAsistencia().add(sgAsi);
                        }
                    }
                }
            } else {
                this.agregar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean fechaEsDiaLectivo(LocalDate fecha) {
        try {
            if (habilitadoDiaLectivo) {
                FiltroActividadCalendario fac = new FiltroActividadCalendario();
                fac.setNoUsarDataSecurity(Boolean.TRUE);
                List<EnumAmbito> listaAmbitos = new ArrayList();
                listaAmbitos.add(EnumAmbito.SEDE);
                listaAmbitos.add(EnumAmbito.DEPARTAMENTAL);
                listaAmbitos.add(EnumAmbito.MINED);
                fac.setAmbitos(listaAmbitos);
                fac.setAnioFk(entidadEnEdicion.getCacSeccion().getSecAnioLectivo().getAlePk());
                fac.setTipoCalendarioFk(entidadEnEdicion.getCacSeccion().getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());

                fac.setFechaDesde(fecha);
                fac.setFechaHasta(fecha);
                fac.setEsLectivo(Boolean.FALSE);
                fac.setTomarEnCuentaTodosAmbitos(Boolean.TRUE);

                fac.setSedePk(entidadEnEdicion.getCacSeccion().getSecServicioEducativo().getSduSede().getSedPk());
                fac.setDepartamentoPk(entidadEnEdicion.getCacSeccion().getSecServicioEducativo().getSduSede().getSedDireccion().getDirDepartamento().getDepPk());
                fac.setMaxResults(1L);
                Long j = actividadCalendarioClient.buscarTotal(fac);

                if (j.compareTo(0L) == 0) {
                    DayOfWeek diaDeSemana = fecha.getDayOfWeek();
                    EnumCeldaDiaHora celda = EnumCeldaDiaHora.dia(diaDeSemana.getValue());

                    FiltroDiaLectivoHorarioEscolar fhe = new FiltroDiaLectivoHorarioEscolar();
                    fhe.setSeccionFk(entidadEnEdicion.getCacSeccion().getSecPk());
                    fhe.setCeldaDiaHora(celda);
                    fhe.setIncluirCampos(new String[]{"dlhEsLectivo"});
                    List<SgDiaLectivoHorarioEscolar> diasLec = diaLectivoHorarioEscolarClient.buscar(fhe);
                    if (diasLec.isEmpty()) {
                        return Boolean.TRUE;
                    } else {
                        SgDiaLectivoHorarioEscolar diaLec = diasLec.get(0);
                        return diaLec.getDlhEsLectivo();
                    }
                }
                return Boolean.FALSE;
            }
            return Boolean.TRUE;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return Boolean.TRUE;
    }

    private List<SgEstudiante> buscarEstudiantes() throws Exception {
        FiltroMatricula filtroMat = new FiltroMatricula();
        filtroMat.setSecPk(entidadEnEdicion.getCacSeccion().getSecPk());
        filtroMat.setMatFechaEntreIngresoHasta(entidadEnEdicion.getCacFecha());
        filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPk",
            "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
            "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
            "matEstudiante.estVersion"});
        filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellidoBusqueda", "matEstudiante.estPersona.perSegundoApellidoBusqueda",
            "matEstudiante.estPersona.perPrimerNombreBusqueda"});
        filtroMat.setAscending(new boolean[]{true, true, true});
        List<SgMatricula> matriculas = restMatricula.buscar(filtroMat);
        List<SgEstudiante> estudiantes = matriculas.stream().map(c -> c.getMatEstudiante()).collect(Collectors.toList());
        return estudiantes;
    }

    public void inasistenciaSelected(SgAsistencia asist, Integer rowIndex) {
        asist.setAsiMotivoInasistencia(this.combosInasistencia[rowIndex].getSelectedT());
    }

    public void actualizar(SgControlAsistenciaCabezal cac) {
        try {
            this.nuevosEstudiantesSeccionSinControlAsistencia = Boolean.FALSE;
            if (cac == null) {
                this.agregar();
            } else {
                entidadEnEdicion = cac;
                controlRealizado = Boolean.TRUE;

                //Verificar si hay estudiantes nuevos y agregarlos. 
                List<SgEstudiante> listEstudiantes = buscarEstudiantes();
                List<SgEstudiante> estudiantesControlados = this.entidadEnEdicion.getCacAsistencia().stream().map(a -> a.getAsiEstudiante()).collect(Collectors.toList());
                for (int i = 0; i < listEstudiantes.size(); i++) {
                    SgEstudiante est = listEstudiantes.get(i);
                    if (!estudiantesControlados.contains(est)) {
                        SgAsistencia sgAsi = new SgAsistencia();
                        sgAsi.setAsiEstudiante(est);
                        this.entidadEnEdicion.getCacAsistencia().add(sgAsi);
                        nuevosEstudiantesSeccionSinControlAsistencia = Boolean.TRUE;
                    }
                }

                combosInasistencia = new SofisComboG[entidadEnEdicion.getCacAsistencia().size()];
                for (int i = 0; i < entidadEnEdicion.getCacAsistencia().size(); i++) {
                    SgAsistencia as = entidadEnEdicion.getCacAsistencia().get(i);
                    combosInasistencia[i] = new SofisComboG(this.motivosInasistencia, "minNombre");
                    combosInasistencia[i].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    combosInasistencia[i].setSelectedT(as.getAsiMotivoInasistencia());
                }

            }
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            SgSeccion secBeforeSave = entidadEnEdicion.getCacSeccion();  //En backend la sección es lazy por lo que luego de guardar vuelve null.
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            entidadEnEdicion.setCacSeccion(secBeforeSave);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            if (controlAsisId != null){
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                JSFUtils.redirectToPage(ConstantesPaginas.CONTROLES_ASISTENCIA);
            } else {
                this.agregar();
            }        
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgControlAsistenciaCabezal getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgControlAsistenciaCabezal entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SofisComboG<SgMotivoInasistencia>[] getCombosInasistencia() {
        return combosInasistencia;
    }

    public void setCombosInasistencia(SofisComboG<SgMotivoInasistencia>[] combosInasistencia) {
        this.combosInasistencia = combosInasistencia;
    }

    public Boolean getControlRealizado() {
        return controlRealizado;
    }

    public Long getControlAsisId() {
        return controlAsisId;
    }

    public SgProgramaEducativo getProgramaEd() {
        return programaEd;
    }

    public void setProgramaEd(SgProgramaEducativo programaEd) {
        this.programaEd = programaEd;
    }

    public Long getSeccionId() {
        return seccionId;
    }

    public void setSeccionId(Long seccionId) {
        this.seccionId = seccionId;
    }

    public Boolean getNuevosEstudiantesSeccionSinControlAsistencia() {
        return nuevosEstudiantesSeccionSinControlAsistencia;
    }

    public void setNuevosEstudiantesSeccionSinControlAsistencia(Boolean nuevosEstudiantesSeccionSinControlAsistencia) {
        this.nuevosEstudiantesSeccionSinControlAsistencia = nuevosEstudiantesSeccionSinControlAsistencia;
    }

    public SofisComboG<SgMotivoInasistencia> getCombosInasistenciaAplicarTodos() {
        return combosInasistenciaAplicarTodos;
    }

    public void setCombosInasistenciaAplicarTodos(SofisComboG<SgMotivoInasistencia> combosInasistenciaAplicarTodos) {
        this.combosInasistenciaAplicarTodos = combosInasistenciaAplicarTodos;
    }

    public String getObservacionAplicaTodos() {
        return observacionAplicaTodos;
    }

    public void setObservacionAplicaTodos(String observacionAplicaTodos) {
        this.observacionAplicaTodos = observacionAplicaTodos;
    }

    public Boolean getFaltoAplicaTodos() {
        return faltoAplicaTodos;
    }

    public void setFaltoAplicaTodos(Boolean faltoAplicaTodos) {
        this.faltoAplicaTodos = faltoAplicaTodos;
    }

    public Boolean getDiaNoLectivo() {
        return diaNoLectivo;
    }

    public void setDiaNoLectivo(Boolean diaNoLectivo) {
        this.diaNoLectivo = diaNoLectivo;
    }

    public SgSeccion getSeccionSeleccionada() {
        return seccionSeleccionada;
    }

    public void setSeccionSeleccionada(SgSeccion seccionSeleccionada) {
        this.seccionSeleccionada = seccionSeleccionada;
    }

    public String[] getIncluirCamposSeccion() {
        return incluirCamposSeccion;
    }

    public void setIncluirCamposSeccion(String[] incluirCamposSeccion) {
        this.incluirCamposSeccion = incluirCamposSeccion;
    }

}
