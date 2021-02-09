/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import sv.gob.mined.siges.utils.SofisDateUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgActividadCalendario;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.dto.SgTipoCalendario;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.enumerados.EnumActividadCalendario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalendarioUI;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalendarioUIAux;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidadAtencion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.web.lazymodels.LazyCalendarioDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ActividadCalendarioRestClient;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CalendarioRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.PeriodoCalificacionRestClient;
import sv.gob.mined.siges.web.restclient.TipoCalendarioRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgPeriodoCalendario;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.enumerados.EnumAmbito;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroActividadCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyActividadCalendarioDataModel;
import sv.gob.mined.siges.web.restclient.CalendarioEscolarRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CalendarioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalendarioBean.class.getName());

    @Inject
    private SedeRestClient sedeClient;

    @Inject
    private CalendarioRestClient restClient;

    @Inject
    private ActividadCalendarioRestClient actCalRestClient;

    @Inject
    private AnioLectivoRestClient anioLectRestClient;

    @Inject
    private TipoCalendarioRestClient tipoCalendarioRestClient;

    @Inject
    private PeriodoCalificacionRestClient restPeriodoClient;

    @Inject
    private ModalidadRestClient modClient;

    @Inject
    private CatalogosRestClient catalogoClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private NivelRestClient nivelRestClient;

    @Inject
    private CalendarioEscolarRestClient calendarioRestClient;

    @Inject
    @Param(name = "id")
    private Long calendarioId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;
    
    private ScheduleModel lazyEventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();

    private LazyActividadCalendarioDataModel actividadCalendarioLazyModel;
    private FiltroActividadCalendario filtroActividades = new FiltroActividadCalendario();

    private Boolean soloLectura = Boolean.FALSE;
    private SgCalendario entidadEnEdicion = new SgCalendario();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCalendarioDataModel calendarioLazyModel;
    private SofisComboG<SgAnioLectivo> anioLectivoCombo;
    private SofisComboG<SgTipoCalendario> tipoCalendarioCombo;

    private SgActividadCalendario actividadCalEnEdicion;
    private SofisComboG<EnumAmbito> comboAmbitoActividad;
    private SofisComboG<EnumActividadCalendario> comboTipoActividad;
    private SofisComboG<SgDepartamento> comboDepartamentoActividad;
    private SgSede sedeActividadSeleccionada;

    private SofisComboG<SgDepartamento> comboDepartamentoActividadBusqueda;
    private SgSede sedeActividadSeleccionadaBusqueda;
    private FiltroCalendarioUI filtroCalendarioUI = new FiltroCalendarioUI();
    private List<FiltroCalendarioUIAux> filtroModalidadesEducativas = new ArrayList();
    private List<FiltroCalendarioUIAux> filtroModalidadesAtencion = new ArrayList();
    private List<EnumCalendarioEscolar> tipoCalEscList;
    private List<SgNivel> listNivel;
    private List<SgPeriodoCalendario> listaCalendarioEsc;
    private List<SgPeriodoCalificacion> listaPeriodoCalificacion;

    public CalendarioBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (calendarioId != null && calendarioId > 0) {
                this.actualizar(restClient.obtenerPorId(calendarioId));
                soloLectura = editable != null ? !editable : soloLectura;
            } else {
                agregar();
            }
            inicializarFiltrosCalendarioUI();
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
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_CALENDARIOS)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getCalPk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_CALENDARIOS)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_CALENDARIOS)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public void actualizar(SgCalendario cal) {
        try {
            entidadEnEdicion = cal;

            if (entidadEnEdicion.getCalAnioLectivo() != null) {
                anioLectivoCombo.setSelectedT(entidadEnEdicion.getCalAnioLectivo());
            }
            if (entidadEnEdicion.getCalTipoCalendario() != null) {
                tipoCalendarioCombo.setSelectedT(entidadEnEdicion.getCalTipoCalendario());
            }

            FiltroPeriodoCalendario fpc = new FiltroPeriodoCalendario();
            fpc.setCalPk(entidadEnEdicion.getCalPk());
            listaCalendarioEsc = calendarioRestClient.buscar(fpc);

            cargarPeriodosCalificacion();

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarActividades() {
        try {
            //TODO: mejorar performance. No cargar todo siempre. Cargar según tab.
            filtroActividades.setSedePk(sedeActividadSeleccionadaBusqueda != null ? sedeActividadSeleccionadaBusqueda.getSedPk() : null);
            filtroActividades.setDepartamentoPk(comboDepartamentoActividadBusqueda.getSelectedT() != null ? comboDepartamentoActividadBusqueda.getSelectedT().getDepPk() : null);
            filtroActividades.setCalendarioPk(entidadEnEdicion.getCalPk());
            totalResultados = actCalRestClient.buscarTotal(filtroActividades);
            actividadCalendarioLazyModel = new LazyActividadCalendarioDataModel(actCalRestClient, filtroActividades, totalResultados, paginado);

            actualizarCalendarioUI();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargarPeriodosCalificacion() {
        try {
            FiltroPeriodoCalificacion filtroPeriodo = new FiltroPeriodoCalificacion();
            filtroPeriodo.setCalPk(calendarioId);
            filtroPeriodo.setInicializarRangoFecha(Boolean.TRUE);
            listaPeriodoCalificacion = restPeriodoClient.buscar(filtroPeriodo);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void inicializarFiltrosCalendarioUI() {
        try {
            List<SgModalidad> listaModalidad = modClient.buscar(new FiltroModalidad());
            filtroModalidadesEducativas = new ArrayList();
            for (SgModalidad m : listaModalidad) {
                FiltroCalendarioUIAux f = new FiltroCalendarioUIAux();
                f.setEtiqueta(m.getModCiclo().getCicNivel().getNivNombre() + " - " + m.getModCiclo().getCicNombre() + " - " + m.getModNombre());
                f.setId(m.getModPk());
                f.setSeleccionado(Boolean.TRUE);
                filtroModalidadesEducativas.add(f);
            }

            List<SgModalidadAtencion> listaModalidadAtencion = catalogoClient.buscarModalidadAtencion(new FiltroModalidadAtencion());
            filtroModalidadesAtencion = new ArrayList();
            for (SgModalidadAtencion m : listaModalidadAtencion) {
                FiltroCalendarioUIAux f = new FiltroCalendarioUIAux();
                f.setEtiqueta(m.getMatNombre());
                f.setId(m.getMatPk());
                f.setSeleccionado(Boolean.TRUE);
                filtroModalidadesAtencion.add(f);
            }
        } catch (Exception ex) {

        }
    }

    public void actualizarCalendarioUI() {

        lazyEventModel = new LazyScheduleModel() {

            @Override
            public void loadEvents(Date start, Date end) {

                try {
                    //ACTIVIDADES
                    if (BooleanUtils.isTrue(filtroCalendarioUI.getActividades())) {
                        FiltroActividadCalendario fil = new FiltroActividadCalendario();
                        fil.setAmbitos(filtroActividades.getAmbitos());
                        fil.setFechaDesde(SofisDateUtils.asLocalDate(start));
                        fil.setFechaHasta(SofisDateUtils.asLocalDate(end));
                        fil.setSedePk(sedeActividadSeleccionadaBusqueda != null ? sedeActividadSeleccionadaBusqueda.getSedPk() : null);
                        fil.setDepartamentoPk(comboDepartamentoActividadBusqueda.getSelectedT() != null ? comboDepartamentoActividadBusqueda.getSelectedT().getDepPk() : null);
                        fil.setCalendarioPk(entidadEnEdicion.getCalPk());
                        entidadEnEdicion.setCalActividadesCalendario(actCalRestClient.buscar(fil));

                        if (entidadEnEdicion.getCalActividadesCalendario() != null) {
                            entidadEnEdicion.getCalActividadesCalendario()
                                    .forEach((ac) -> {
                                        DefaultScheduleEvent evento = new DefaultScheduleEvent(ac.getAcaNombre(),
                                                SofisDateUtils.asDate(ac.getAcaFechaDesde()),
                                                SofisDateUtils.asDate(ac.getAcaFechaHasta()));
                                        evento.setStyleClass("celeste"); 
                                        lazyEventModel.addEvent(evento);
                                    });
                        }
                    }

                    //FIN ACTIVIDADES
                    
                    
                    //TODO: mejorar. Filtrar por fecha
                    if (BooleanUtils.isTrue(filtroCalendarioUI.getCalificaciones()) && entidadEnEdicion.getCalPeriodosCalificacion() != null) {
                        entidadEnEdicion.getCalPeriodosCalificacion()
                                .forEach((pc) -> {
                                    pc.getPcaRangoFecha().forEach((rf) -> {
                                        DefaultScheduleEvent evento = new DefaultScheduleEvent(pc.getPcaNombre(),
                                                SofisDateUtils.asDate(rf.getRfeFechaDesde()),
                                                SofisDateUtils.asDate(rf.getRfeFechaHasta()));
                                        evento.setStyleClass("azul");
                                        lazyEventModel.addEvent(evento);
                                    });
                                });
                    }

                    //TODO: mejorar. Filtrar por fecha
                    if (BooleanUtils.isTrue(filtroCalendarioUI.getPeriodosAcademicos())) {
                        if (listaPeriodoCalificacion != null) {
                            for (SgPeriodoCalificacion pc : listaPeriodoCalificacion) {
                                boolean incluir = false;
                                for (FiltroCalendarioUIAux ff : filtroModalidadesEducativas) {
                                    if (BooleanUtils.isTrue(ff.getSeleccionado())) {
                                        if (pc.getPcaModalidad() != null && pc.getPcaModalidad().getModPk().equals(ff.getId())) {
                                            incluir = true;
                                            break;
                                        }
                                    }
                                }
                                if (incluir) {
                                    for (FiltroCalendarioUIAux ff : filtroModalidadesAtencion) {
                                        if (BooleanUtils.isTrue(ff.getSeleccionado())) {
                                            if (pc.getPcaModalidadAtencion() != null && pc.getPcaModalidadAtencion().getMatPk().equals(ff.getId())) {
                                                incluir = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (incluir && pc != null && pc.getPcaRangoFecha() != null) {
                                    for (SgRangoFecha rf : pc.getPcaRangoFecha()) {
                                        DefaultScheduleEvent evento = new DefaultScheduleEvent(
                                                rf.getRfeCodigo(),
                                                SofisDateUtils.asDate(rf.getRfeFechaDesde()),
                                                SofisDateUtils.asDate(rf.getRfeFechaHasta()));
                                        evento.setStyleClass("verde");
                                        lazyEventModel.addEvent(evento);
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }

            }
        };

    }

    public List<EnumAmbito> completeAmbitosBusqueda(String query) {
        try {
            List<EnumAmbito> ambitos = new ArrayList<>();
            ambitos.add(EnumAmbito.MINED);
            ambitos.add(EnumAmbito.DEPARTAMENTAL);
            ambitos.add(EnumAmbito.SEDE);
            if (StringUtils.isBlank(query)) {
                return ambitos;
            } else {
                return ambitos.stream().filter(x -> x.getText().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }

    public void cargarCombos() {
        try {
            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setOrderBy(new String[]{"aleAnio"});
            fal.setAscending(new boolean[]{false});
            List<SgAnioLectivo> listAnio = anioLectRestClient.buscar(fal);
            anioLectivoCombo = new SofisComboG(listAnio, "aleAnio");
            anioLectivoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setIncluirCampos(new String[]{"tcePk","tceCodigo", "tceNombre", "tceVersion"});
            List<SgTipoCalendario> listCalendario = tipoCalendarioRestClient.buscar(fc);
            tipoCalendarioCombo = new SofisComboG(listCalendario, "tceNombre");
            tipoCalendarioCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumActividadCalendario> escalas = new ArrayList(Arrays.asList(EnumActividadCalendario.values()));
            comboTipoActividad = new SofisComboG(escalas, "text");
            comboTipoActividad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumAmbito> ambitos = new ArrayList<>();

            EnumAmbito ambito = sessionBean.obtenerMayorAmbitoOperacion(ConstantesOperaciones.VER_CALENDARIOS);

            if (ambito.getOrden() <= EnumAmbito.MINED.getOrden()) {
                ambitos.add(EnumAmbito.MINED);
            }
            if (ambito.getOrden() <= EnumAmbito.DEPARTAMENTAL.getOrden()) {
                ambitos.add(EnumAmbito.DEPARTAMENTAL);
            }
            if (ambito.getOrden() <= EnumAmbito.SEDE.getOrden()) {
                ambitos.add(EnumAmbito.SEDE);
            }
            comboAmbitoActividad = new SofisComboG(ambitos, "text");
            comboAmbitoActividad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (ambitos.size() == 1) {
                comboAmbitoActividad.setSelectedT(EnumAmbito.SEDE);
            }

            tipoCalEscList = new ArrayList();
            tipoCalEscList.add(EnumCalendarioEscolar.MAT);
            tipoCalEscList.add(EnumCalendarioEscolar.PREC);
            tipoCalEscList.add(EnumCalendarioEscolar.PRECPS);
            tipoCalEscList.add(EnumCalendarioEscolar.SREC);
            tipoCalEscList.add(EnumCalendarioEscolar.SRECPS);

            FiltroNivel fn = new FiltroNivel();
            fn.setNivHabilitado(Boolean.TRUE);
            fn.setIncluirCampos(new String[]{"nivPk", "nivNombre", "nivVersion"});
            listNivel = nivelRestClient.buscar(fn);

            fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = sessionBean.obtenerDepartamentosOperacion(ConstantesOperaciones.BUSCAR_ACTIVIDAD_CALENDARIO);
            comboDepartamentoActividadBusqueda = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentoActividadBusqueda.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboDepartamentoActividad = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentoActividad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean habilitarEdicionActividad(SgActividadCalendario act) {
        EnumAmbito ambito = sessionBean.obtenerMayorAmbitoOperacion(ConstantesOperaciones.ACTUALIZAR_ACTIVIDAD_CALENDARIO);
        if (ambito != null && act != null && ambito.getOrden() <= act.getAcaAmbito().getOrden()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void ambitoSelected() {
        try {
            if (EnumAmbito.SEDE.equals(this.comboAmbitoActividad.getSelectedT())) {
                cargarSedePorDefecto();
            }

            if (comboDepartamentoActividad != null) {
                comboDepartamentoActividad.setSelected(-1);
            }
            sedeActividadSeleccionada = null;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarSedePorDefecto() throws Exception {
        if (sessionBean.getSedeDefecto() != null) {
            sedeActividadSeleccionada = sessionBean.getSedeDefecto();
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();   
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    private void limpiarCombos() {
        anioLectivoCombo.setSelected(-1);
        tipoCalendarioCombo.setSelected(-1);
    }

    public void limpiar() {
        filtroActividades = new FiltroActividadCalendario();
        filtroActividades.setCalendarioPk(entidadEnEdicion.getCalPk());
        this.sedeActividadSeleccionadaBusqueda = null;
        this.comboDepartamentoActividadBusqueda.setSelected(-1);
        actividadCalendarioLazyModel = null;
        totalResultados = null;
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgCalendario();
    }

    public void agregarActividadCalendario() {
        JSFUtils.limpiarMensajesError();
        cargarCombos();
        actividadCalEnEdicion = new SgActividadCalendario();
    }

    public void actualizarActividadCalendario(SgActividadCalendario var) {
        try {
            JSFUtils.limpiarMensajesError();
            actividadCalEnEdicion = (SgActividadCalendario) SerializationUtils.clone(var);
            comboAmbitoActividad.setSelectedT(var.getAcaAmbito());
            ambitoSelected();
            if (comboDepartamentoActividad != null) {
                comboDepartamentoActividad.setSelectedT(var.getAcaDepartamento());
            }
            sedeActividadSeleccionada = var.getAcaSede();
            comboTipoActividad.setSelectedT(actividadCalEnEdicion.getAcaTipo());
            limpiarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if (anioLectivoCombo.getSelectedT() != null) {
                entidadEnEdicion.setCalAnioLectivo(anioLectivoCombo.getSelectedT());
            }
            if (tipoCalendarioCombo.getSelectedT() != null) {
                entidadEnEdicion.setCalTipoCalendario(tipoCalendarioCombo.getSelectedT());
            }
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            this.actualizar(restClient.obtenerPorId(entidadEnEdicion.getCalPk()));
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarActividadCalendario() {
        try {
            actividadCalEnEdicion.setAcaCalendario(entidadEnEdicion);
            actividadCalEnEdicion.setAcaTipo(comboTipoActividad.getSelectedT());
            actividadCalEnEdicion.setAcaAmbito(comboAmbitoActividad.getSelectedT());
            actividadCalEnEdicion.setAcaDepartamento(comboDepartamentoActividad != null ? comboDepartamentoActividad.getSelectedT() : null);
            actividadCalEnEdicion.setAcaSede(sedeActividadSeleccionada);
            actCalRestClient.guardar(actividadCalEnEdicion);

            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            this.actualizar(restClient.obtenerPorId(entidadEnEdicion.getCalPk()));
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCalPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            this.actualizar(restClient.obtenerPorId(entidadEnEdicion.getCalPk()));
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarActividadCalendario() {
        try {
            actCalRestClient.eliminar(actividadCalEnEdicion.getAcaPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            this.actualizar(restClient.obtenerPorId(entidadEnEdicion.getCalPk()));
            actividadCalEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgPeriodoCalendario> buscarTipo(EnumCalendarioEscolar tipo, SgNivel niv) {
        if (listaCalendarioEsc != null) {
            return listaCalendarioEsc.stream()
                    .filter(c -> c.getCesTipo().equals(tipo))
                    .filter(c -> c.getCesNivel().equals(niv))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public LocalDate getMinDate() {
        if (this.entidadEnEdicion.getCalAnioLectivo() != null) {
            return LocalDate.of(this.entidadEnEdicion.getCalAnioLectivo().getAleAnio(), 1, 1);
        }
        return null;
    }

    public LocalDate getMaxDate() {
        if (this.entidadEnEdicion.getCalAnioLectivo() != null) {
            return LocalDate.of(this.entidadEnEdicion.getCalAnioLectivo().getAleAnio(), 12, 31);
        }
        return null;
    }

    public FiltroActividadCalendario getFiltroActividades() {
        return filtroActividades;
    }

    public void setFiltroActividades(FiltroActividadCalendario filtroActividades) {
        this.filtroActividades = filtroActividades;
    }

    public SgCalendario getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCalendario entidadEnEdicion) {
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

    public LazyCalendarioDataModel getCalendarioLazyModel() {
        return calendarioLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCalendarioDataModel calendarioLazyModel) {
        this.calendarioLazyModel = calendarioLazyModel;
    }

    public SofisComboG<SgAnioLectivo> getAnioLectivoCombo() {
        return anioLectivoCombo;
    }

    public void setAnioLectivoCombo(SofisComboG<SgAnioLectivo> AnioLectivoCombo) {
        this.anioLectivoCombo = AnioLectivoCombo;
    }

    public SofisComboG<SgTipoCalendario> getTipoCalendarioCombo() {
        return tipoCalendarioCombo;
    }

    public void setTipoCalendarioCombo(SofisComboG<SgTipoCalendario> TipoCalendarioCombo) {
        this.tipoCalendarioCombo = TipoCalendarioCombo;
    }

    public SgActividadCalendario getActividadCalEnEdicion() {
        return actividadCalEnEdicion;
    }

    public void setActividadCalEnEdicion(SgActividadCalendario actividadCalEnEdicion) {
        this.actividadCalEnEdicion = actividadCalEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public FiltroCalendarioUI getFiltroCalendarioUI() {
        return filtroCalendarioUI;
    }

    public void setFiltroCalendarioUI(FiltroCalendarioUI filtroUI) {
        this.filtroCalendarioUI = filtroUI;
    }

    public List<FiltroCalendarioUIAux> getFiltroModalidadesEducativas() {
        return filtroModalidadesEducativas;
    }

    public void setFiltroModalidadesEducativas(List<FiltroCalendarioUIAux> filtroModalidadesEducativas) {
        this.filtroModalidadesEducativas = filtroModalidadesEducativas;
    }

    public List<FiltroCalendarioUIAux> getFiltroModalidadesAtencion() {
        return filtroModalidadesAtencion;
    }

    public void setFiltroModalidadesAtencion(List<FiltroCalendarioUIAux> filtroModalidadesAtencion) {
        this.filtroModalidadesAtencion = filtroModalidadesAtencion;
    }

    public List<EnumCalendarioEscolar> getTipoCalEscList() {
        return tipoCalEscList;
    }

    public void setTipoCalEscList(List<EnumCalendarioEscolar> tipoCalEscList) {
        this.tipoCalEscList = tipoCalEscList;
    }

    public List<SgNivel> getListNivel() {
        return listNivel;
    }

    public void setListNivel(List<SgNivel> listNivel) {
        this.listNivel = listNivel;
    }

    public LazyActividadCalendarioDataModel getActividadCalendarioLazyModel() {
        return actividadCalendarioLazyModel;
    }

    public void setActividadCalendarioLazyModel(LazyActividadCalendarioDataModel actividadCalendarioLazyModel) {
        this.actividadCalendarioLazyModel = actividadCalendarioLazyModel;
    }

    public SofisComboG<EnumAmbito> getComboAmbitoActividad() {
        return comboAmbitoActividad;
    }

    public void setComboAmbitoActividad(SofisComboG<EnumAmbito> comboAmbitoActividad) {
        this.comboAmbitoActividad = comboAmbitoActividad;
    }

    public SofisComboG<EnumActividadCalendario> getComboTipoActividad() {
        return comboTipoActividad;
    }

    public void setComboTipoActividad(SofisComboG<EnumActividadCalendario> comboTipoActividad) {
        this.comboTipoActividad = comboTipoActividad;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentoActividad() {
        return comboDepartamentoActividad;
    }

    public void setComboDepartamentoActividad(SofisComboG<SgDepartamento> comboDepartamentoActividad) {
        this.comboDepartamentoActividad = comboDepartamentoActividad;
    }

    public SgSede getSedeActividadSeleccionada() {
        return sedeActividadSeleccionada;
    }

    public void setSedeActividadSeleccionada(SgSede sedeActividadSeleccionada) {
        this.sedeActividadSeleccionada = sedeActividadSeleccionada;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentoActividadBusqueda() {
        return comboDepartamentoActividadBusqueda;
    }

    public void setComboDepartamentoActividadBusqueda(SofisComboG<SgDepartamento> comboDepartamentoActividadBusqueda) {
        this.comboDepartamentoActividadBusqueda = comboDepartamentoActividadBusqueda;
    }

    public SgSede getSedeActividadSeleccionadaBusqueda() {
        return sedeActividadSeleccionadaBusqueda;
    }

    public void setSedeActividadSeleccionadaBusqueda(SgSede sedeActividadSeleccionadaBusqueda) {
        this.sedeActividadSeleccionadaBusqueda = sedeActividadSeleccionadaBusqueda;
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

}
