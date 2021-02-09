/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.jsoup.Jsoup;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgDetallePlanEscolar;
import sv.gob.mined.siges.web.dto.SgEncabezadoPlanCompras;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgPlanCompras;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SgInsumo;
import sv.gob.mined.siges.web.dto.siap2.SsMetodoAdq;
import sv.gob.mined.siges.web.enumerados.EnumPlanCompraEstado;
import sv.gob.mined.siges.web.enumerados.EnumPlanCompraUnidadMedida;
import sv.gob.mined.siges.web.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDetallePlanEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEncabezadoPlanCompra;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroInsumo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMetodoAdq;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlanCompras;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.lazymodels.LazyPlanComprasDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DetallePlanEscolarRestClient;
import sv.gob.mined.siges.web.restclient.InsumoRestClient;
import sv.gob.mined.siges.web.restclient.MetodoAdqRestClient;
import sv.gob.mined.siges.web.restclient.MovimientosRestClient;
import sv.gob.mined.siges.web.restclient.PlanComprasRestCliente;
import sv.gob.mined.siges.web.restclient.PresupuestoEscolarRestCliente;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean correspondiente al plan de compras.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped

public class PlanComprasBean implements Serializable {

    @Inject
    private PlanComprasRestCliente restClient;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private DetallePlanEscolarRestClient restDetalle;

    @Inject
    private MetodoAdqRestClient resMetodoAdq;

    @Inject
    private MovimientosRestClient movimientosRestClient;

    @Inject
    private PresupuestoEscolarRestCliente presupuestoEscolarClient;

    @Inject
    private InsumoRestClient insumoClient;

    @Inject
    @Param(name = "id")
    private Long presupuestoEscolarId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    private static final Logger LOGGER = Logger.getLogger(PlanComprasBean.class.getName());
    private SofisComboG<SgDetallePlanEscolar> actividadCombo = new SofisComboG<>();
    private SofisComboG<SgMovimientos> descripcionCombo = new SofisComboG<>();
    private SofisComboG<SsMetodoAdq> metodoCombo = new SofisComboG<>();
    private FiltroPresupuestoEscolar filtroPes = new FiltroPresupuestoEscolar();
    private FiltroPlanCompras filtro = new FiltroPlanCompras();
    private FiltroEncabezadoPlanCompra filtroPlan = new FiltroEncabezadoPlanCompra();
    private SgPlanCompras entidadEnEdicion = new SgPlanCompras();
    private SgEncabezadoPlanCompras planEnEdicion = new SgEncabezadoPlanCompras();
    private SgPresupuestoEscolar presupuesto = new SgPresupuestoEscolar();
    private SgMovimientos movimiento = new SgMovimientos();
    private SgInsumo insumoSeleccionado;
    private LazyPlanComprasDataModel reglasPlanComprasDataModel;
    private Integer paginado = 10;
    private Long totalResultados;
    private List<RevHistorico> historialPlanCompras = new ArrayList();
    private BigDecimal totalPlan = BigDecimal.ZERO;
    private SofisComboG<EnumPlanCompraUnidadMedida> comboUnidadMedida;
    private List<EnumPlanCompraUnidadMedida> unidades = new ArrayList();
    private SgPlanCompras compraAEliminar;
    private String leyendaTitulo = "Edición de Plan de Compras";
    private BigDecimal montoAprobado = BigDecimal.ZERO;
    private Boolean aplicaGuardar = Boolean.FALSE;
    private BigDecimal totalesPlan;
    private String estado = "";
    private SofisComboG<EnumPlanCompraEstado> comboEstado;
    private Boolean botonCerrar = Boolean.FALSE;
    private Boolean botonRevisar = Boolean.FALSE;
    private Boolean botonAprobar = Boolean.FALSE;
    private Boolean controlRevision = Boolean.FALSE;
    private Boolean botonObservar = Boolean.FALSE;
    private Boolean botonCorregir = Boolean.FALSE;
    private Boolean obseracionLectura = Boolean.FALSE;
    private Boolean acciones = Boolean.FALSE;
    private Boolean verDatosEstado = Boolean.FALSE;
    private Boolean soloLectura = Boolean.FALSE;

    public PlanComprasBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
    @PostConstruct
    public void init() {
        try {
            if (presupuestoEscolarId != null && presupuestoEscolarId > 0) {
                soloLectura = editable != null ? !editable : soloLectura;
                presupuesto = new SgPresupuestoEscolar();
                presupuesto = presupuestoEscolarClient.obtenerPorId(presupuestoEscolarId);
                buscar();
                if (presupuesto.getPesEstado().equals(EnumPresupuestoEscolarEstado.APROBADO)) {
                    validarPlan();
                    inicializarBotones();
                    datosEstado();
                } else {
                    JSFUtils.redirectToPage(ConstantesPaginas.PRESUPUESTOS_ESCOLARES);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga los combos de la busqueda
     */
    public void cargarCombos() {
        try {
            descripcionCombo = new SofisComboG();
            descripcionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            FiltroDetallePlanEscolar filtro = new FiltroDetallePlanEscolar();
            filtro.setSedePk(presupuesto.getPesSedeFk().getSedPk());
            List<SgDetallePlanEscolar> resultado = restDetalle.buscar(filtro);
            for (SgDetallePlanEscolar det : resultado) {
                det.setDpeActividad(Jsoup.parse(det.getDpeActividad()).text());
            }
            List<SgDetallePlanEscolar> nuevoResultado = resultado
                    .stream()
                    .filter(x -> x.getDpePlanEscolarAnual().getPeaAnioLectivo().getAleAnio().equals(presupuesto.getPesAnioFiscalFk().getAniAnio()))
                    .collect(Collectors.toList());

            actividadCombo = new SofisComboG(nuevoResultado, "dpeActividad");
            actividadCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            cargarComboDescripcion();

            FiltroMetodoAdq metodo = new FiltroMetodoAdq();
            metodo.setMetHabilitado(true);
            metodo.setMaxResults(11L);
            metodo.setOrderBy(new String[]{"metNombre"});
            metodo.setAscending(new boolean[]{true});
            metodo.setIncluirCampos(new String[]{"metId", "metNombre", "meVersion"});
            List<SsMetodoAdq> metodos = resMetodoAdq.buscar(metodo);
            metodoCombo = new SofisComboG(metodos, "metNombre");
            metodoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (entidadEnEdicion.getComMetodoFk() != null) {
                metodoCombo.setSelectedT(entidadEnEdicion.getComMetodoFk());
            }

        } catch (BusinessException ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Carga lo combo de descripciones de los movimientos por actividad
     */
    public void cargarComboDescripcion() {
        try {
            descripcionCombo = new SofisComboG();
            descripcionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            FiltroMovimientos mov = new FiltroMovimientos();
            mov.setMovPresupuestoFk(presupuesto.getPesPk());
            if (actividadCombo.getSelectedT() != null) {
                mov.setMovActividadPk(actividadCombo.getSelected().longValue());
                mov.setOrderBy(new String[]{"movFuenteRecursos"});
                mov.setAscending(new boolean[]{true});
                mov.setMovActividadAplicaPlanCompra(Boolean.TRUE);
                mov.setIncluirCampos(new String[]{
                    "movPk",
                    "movPresupuestoFk",
                    "movAreaInversionPk.adiPk",
                    "movAreaInversionPk.adiVersion",
                    "movSubAreaInversionPk.adiPk",
                    "movSubAreaInversionPk.adiVersion",
                    "movActividadPk",
                    "movFuenteRecursos",
                    "movVersion"});
                List<SgMovimientos> listDescripcion = movimientosRestClient.buscar(mov);
                descripcionCombo = new SofisComboG(listDescripcion, "movFuenteRecursos");
                descripcionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Setea la descripción Seleccionada.
     */
    public void setearDescripcion() {
        //Esto corresponde a cuando se selecciona la descripción
        entidadEnEdicion.setComMovimientosFk(descripcionCombo.getSelectedT());

    }

    /**
     * Método para el componente autocomplete de insumos
     *
     * @param query
     * @return
     */
    public List<SgInsumo> completeInusmo(String query) {
        try {
            FiltroInsumo ins = new FiltroInsumo();
            if (entidadEnEdicion.getComMovimientosFk() != null) {
                ins.setRelInsumoSubArea(entidadEnEdicion.getComMovimientosFk().getMovSubAreaInversionPk().getAdiPk());
                //ins.setRelInsumoArea(entidadEnEdicion.getComMovimientosFk().getMovAreaInversionPk().getAdiPk());
            }
            ins.setInsNombre(query);
            ins.setMaxResults(11L);
            ins.setOrderBy(new String[]{"insNombre"});
            ins.setAscending(new boolean[]{false});
            ins.setIncluirCampos(new String[]{
                "insPk",
                "insAreaRel.rinInsumoFk.insPk",
                "insPk",
                "insCodigo",
                "insNombre",
                "insVersion"});
            List<SgInsumo> listInsumos = insumoClient.buscar(ins);
            if (listInsumos.isEmpty()) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_INSUMOS_VACIOS), "");
            } else {
                return insumoClient.buscar(ins);
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {

    }

    /**
     * Crea plan de compras.
     */
    private void validarPlan() {
        try {
            FiltroEncabezadoPlanCompra plan = new FiltroEncabezadoPlanCompra();
            plan.setPlaPresupuestoFk(presupuesto.getPesPk());
            plan.setOrderBy(new String[]{"plaPk"});
            plan.setAscending(new boolean[]{true});
            plan.setIncluirCampos(new String[]{"plaPk",
                "plaEstado",
                "plaComentario",
                "plaPresupuestoFk",
                "plaUltModFecha",
                "plaUltModUsuario",
                "plaVersion"});
            List<SgEncabezadoPlanCompras> listPlan = restClient.buscarPlan(plan);
            if (listPlan.isEmpty()) {
                planEnEdicion.setPlaPresupuestoFk(presupuesto);
                planEnEdicion.setPlaEstado(EnumPlanCompraEstado.EN_PROCESO);
                planEnEdicion = restClient.guardarPlan(planEnEdicion);
            } else {
                planEnEdicion = listPlan.get(0);
                estado = planEnEdicion.getPlaEstado().getText();
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    /**
     * Maneja el render para los datos de estado.
     */
    public void datosEstado() {
        verDatosEstado = Boolean.FALSE;
        if (planEnEdicion.getPlaEstado().equals(EnumPlanCompraEstado.APROBADO) || planEnEdicion.getPlaEstado().equals(EnumPlanCompraEstado.OBSERVADO)) {
            verDatosEstado = Boolean.TRUE;
        }
    }

    /**
     * Maneja el control render de los botones a mostrar.
     */
    public void inicializarBotones() {
        botonCerrar = Boolean.FALSE;
        botonRevisar = Boolean.FALSE;
        botonAprobar = Boolean.FALSE;
        botonObservar = Boolean.FALSE;
        botonCorregir = Boolean.FALSE;
        obseracionLectura = Boolean.FALSE;
        controlRevision = Boolean.FALSE;
        acciones = Boolean.FALSE;
        if (planEnEdicion.getPlaEstado().equals(EnumPlanCompraEstado.EN_PROCESO) && editable == null) {
            botonCerrar = Boolean.TRUE;
            acciones = Boolean.TRUE;
        }
        if ((planEnEdicion.getPlaEstado().equals(EnumPlanCompraEstado.CERRADO) || planEnEdicion.getPlaEstado().equals(EnumPlanCompraEstado.CORREGIDO)) && editable == null) {
            botonRevisar = Boolean.TRUE;
        }

        if (planEnEdicion.getPlaEstado().equals(EnumPlanCompraEstado.EN_REVISION) && editable == null) {
            botonAprobar = Boolean.TRUE;
            botonObservar = Boolean.TRUE;
            controlRevision = Boolean.TRUE;
        }
        if (planEnEdicion.getPlaEstado().equals(EnumPlanCompraEstado.OBSERVADO) && editable == null) {
            botonCorregir = Boolean.TRUE;
            controlRevision = Boolean.TRUE;
            obseracionLectura = Boolean.TRUE;
            acciones = Boolean.TRUE;
        }

    }

    /**
     * Carga los datos de un plan de compras para poder ser editados.
     *
     * @param var
     */
    public void actualizar(SgPlanCompras var) {
        leyendaTitulo = "Edición de Plan de Compras";
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        insumoSeleccionado = null;
        entidadEnEdicion = (var);
        if (entidadEnEdicion != null) {
            cargarCombos();
            actividadCombo.setSelectedT(entidadEnEdicion.getComMovimientosFk().getMovActividadPk());
            cargarComboDescripcion();
            descripcionCombo.setSelectedT(entidadEnEdicion.getComMovimientosFk());
            insumoSeleccionado = entidadEnEdicion.getComInsumoFk();
        }
    }

    /**
     * Carga los combos de Estado
     */
    public void cargarComboEstado() {
        try {
            List<EnumPlanCompraEstado> listEstados = new ArrayList(Arrays.asList(EnumPresupuestoEscolarEstado.values()));
            comboEstado = new SofisComboG(listEstados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            if (presupuesto != null) {
                filtro.setComPresupuestoFk(presupuesto.getPesPk());
            }
            filtro.setIncluirCampos(new String[]{"comPk",
                "comCodigo",
                "comCantidad",
                "comUnidadMedida",
                "comPrecioUnitario",
                "comMontoTotal",
                "comFechaEstimadaCompra",
                "comMetodoFk.metId",
                "comMetodoFk.metNombre",
                "comMetodoFk.metHabilitado",
                "comMetodoFk.metUltMod",
                "comMetodoFk.metUltUsuario",
                "comMetodoFk.meVersion",
                "comInsumoFk.insPk",
                "comInsumoFk.insCodigo",
                "comInsumoFk.insNombre",
                "comInsumoFk.insVersion",
                "comMovimientosFk.movActividadPk.dpePk",
                "comMovimientosFk.movActividadPk.dpeActividad",
                "comMovimientosFk.movActividadPk.dpeVersion",
                "comMovimientosFk.movPk",
                "comMovimientosFk.movFuenteRecursos",
                "comMovimientosFk.movVersion",
                "comVersion"

            });
            filtro.setOrderBy(new String[]{"comPk"});
            filtro.setAscending(new boolean[]{false});
            totalResultados = restClient.buscarTotal(filtro);
            List<SgPlanCompras> resultado = restClient.buscar(filtro);
            reglasPlanComprasDataModel = new LazyPlanComprasDataModel(restClient, filtro, totalResultados, paginado);
            totalPlan = BigDecimal.ZERO;
            for (SgPlanCompras x : resultado) {
                if (x.getComMontoTotal() != null) {
                    totalPlan = totalPlan.add(x.getComMontoTotal());
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea un nuevo objeto de plan de compras
     */
    public void agregarPlan() {
        JSFUtils.limpiarMensajesError();
        leyendaTitulo = "Plan de Compras";
        entidadEnEdicion = new SgPlanCompras();
        insumoSeleccionado = null;
        cargarCombos();
        descripcionCombo = new SofisComboG<>();
        cargarComboDescripcion();
    }

    public void dateSelect(SelectEvent event) {
        try {
            entidadEnEdicion.setComPk(null);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de plan de compras.
     */
    public void guardar() {
        try {
            aplicaGuardar = Boolean.FALSE;
            preGuardar();
            if (aplicaGuardar) {
                entidadEnEdicion.setComMetodoFk(metodoCombo.getSelectedT());
                entidadEnEdicion.setComInsumoFk(insumoSeleccionado);
                entidadEnEdicion.setComPresupuestoFk(presupuesto);
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                buscar();
                PrimeFaces.current().executeScript("PF('itemDialogPlanCompras').hide()");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Realiza las validaciones de un registro de plan de compras.
     */
    public void preGuardar() {
        try {
            entidadEnEdicion.setComMetodoFk(metodoCombo.getSelectedT());
            entidadEnEdicion.setComInsumoFk(insumoSeleccionado);
            entidadEnEdicion.setComPresupuestoFk(presupuesto);
            if (actividadCombo.getSelectedT() != null
                    && descripcionCombo.getSelectedT() != null
                    && entidadEnEdicion.getComCantidad() != null
                    && entidadEnEdicion.getComPrecioUnitario() != null) {
                FiltroMovimientos mov = new FiltroMovimientos();
                mov.setMovPresupuestoFk(presupuesto.getPesPk());
                mov.setMovActividadPk(actividadCombo.getSelected().longValue());
                mov.setMovPk(entidadEnEdicion.getComMovimientosFk().getMovPk());
                mov.setOrderBy(new String[]{"movPk"});
                mov.setAscending(new boolean[]{true});
                mov.setIncluirCampos(new String[]{"movPk", "movPresupuestoFk", "movActividadPk", "movFuenteRecursos", "movMontoAprobado", "movVersion"});
                List<SgMovimientos> monto = movimientosRestClient.buscar(mov);
                SgMovimientos mov2 = monto.get(0);
                if (!monto.isEmpty()) {
                    FiltroPlanCompras com = new FiltroPlanCompras();
                    com.setComPresupuestoFk(presupuesto.getPesPk());
                    com.setComMovimientosFk(mov2);
                    com.setOrderBy(new String[]{"comPk"});
                    com.setAscending(new boolean[]{true});
                    com.setIncluirCampos(new String[]{"comPk", "comMovimientosFk.movPk", "comMovimientosFk.movVersion", "comMontoTotal", "comPresupuestoFk", "comVersion"});
                    List<SgPlanCompras> plan = restClient.buscar(com);
                    if (plan.isEmpty()) {
                        try {
                            if (mov2.getMovMontoAprobado().compareTo((entidadEnEdicion.getComCantidad()).multiply(entidadEnEdicion.getComPrecioUnitario())) == 1
                                    || mov2.getMovMontoAprobado().compareTo((entidadEnEdicion.getComCantidad()).multiply(entidadEnEdicion.getComPrecioUnitario())) == 0) {
                                aplicaGuardar = Boolean.TRUE;
                            } else {
                                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_MONTO_APROBADO_PLAN_COMPRA), "");
                            }
                        } catch (Exception ex) {
                            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                        }

                    } else {
                        totalesPlan = plan.stream()
                                .filter(x -> x.getComMovimientosFk() != null)
                                .filter(x -> x.getComMontoTotal() != null)
                                .map(SgPlanCompras::getComMontoTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal disponible = mov2.getMovMontoAprobado().subtract(totalesPlan);
                        if (disponible.compareTo((entidadEnEdicion.getComCantidad()).multiply(entidadEnEdicion.getComPrecioUnitario())) == 1
                                || disponible.compareTo((entidadEnEdicion.getComCantidad()).multiply(entidadEnEdicion.getComPrecioUnitario())) == 0) {
                            aplicaGuardar = Boolean.TRUE;
                        } else {
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_MONTO_APROBADO_PLAN_COMPRA), "");
                        }
                    }
                }

            } else {
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Se realiza cambio de plan de compras a estado a cerrado
     */
    public void confirmarCierre() {
        try {
            if (totalResultados == 0) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_PLAN_COMPRAS_VACIO), "");
            } else {
                planEnEdicion.setPlaEstado(EnumPlanCompraEstado.CERRADO);
                planEnEdicion = restClient.guardarPlan(planEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            }
            buscar();
            inicializarBotones();
            datosEstado();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Se realiza cambio de plan de compras a estado a en revision
     */
    public void confirmarRevision() {
        try {
            planEnEdicion.setPlaEstado(EnumPlanCompraEstado.EN_REVISION);
            planEnEdicion = restClient.guardarPlan(planEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
            inicializarBotones();
            cargarComboEstado();
            datosEstado();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Se realiza cambio de plan de compras a estado a observado
     */
    public void confirmarObservacion() {
        try {
            planEnEdicion.setPlaEstado(EnumPlanCompraEstado.OBSERVADO);
            planEnEdicion = restClient.guardarPlan(planEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
            inicializarBotones();
            datosEstado();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Se realiza cambio de plan de compras a estado a Corregido
     */
    public void confirmarCorregir() {
        try {
            planEnEdicion.setPlaEstado(EnumPlanCompraEstado.CORREGIDO);
            planEnEdicion = restClient.guardarPlan(planEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
            inicializarBotones();
            datosEstado();
        } catch (BusinessException be) {
            validarPlan();
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Se realiza cambio de plan de compras a estado a Corregido
     */
    public void confirmarAprobacion() {
        try {
            planEnEdicion.setPlaEstado(EnumPlanCompraEstado.APROBADO);
            planEnEdicion = restClient.guardarPlan(planEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
            inicializarBotones();
            datosEstado();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Elimina un registro de plan de compras.
     */
    public void eliminar() {
        try {
            restClient.eliminar(compraAEliminar.getComPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id
     */
    public void historial(Long id) {
        try {
            historialPlanCompras = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Inicializa el objeto de plan de compras a eliminar
     *
     * @param com
     */
    public void prepararEliminarPlan(SgPlanCompras com) {
        compraAEliminar = com;
    }

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public SofisComboG<SsMetodoAdq> getMetodoCombo() {
        return metodoCombo;
    }

    public void setMetodoCombo(SofisComboG<SsMetodoAdq> metodoCombo) {
        this.metodoCombo = metodoCombo;
    }

    public SgPresupuestoEscolar getPresupuesto() {
        return presupuesto;
    }

    public SofisComboG<EnumPlanCompraUnidadMedida> getComboUnidadMedida() {
        return comboUnidadMedida;
    }

    public void setComboUnidadMedida(SofisComboG<EnumPlanCompraUnidadMedida> comboUnidadMedida) {
        this.comboUnidadMedida = comboUnidadMedida;
    }

    public void setPresupuesto(SgPresupuestoEscolar presupuesto) {
        this.presupuesto = presupuesto;
    }

    public SofisComboG<SgDetallePlanEscolar> getActividadCombo() {
        return actividadCombo;
    }

    public void setActividadCombo(SofisComboG<SgDetallePlanEscolar> actividadCombo) {
        this.actividadCombo = actividadCombo;
    }

    public SofisComboG<SgMovimientos> getDescripcionCombo() {
        return descripcionCombo;
    }

    public void setDescripcionCombo(SofisComboG<SgMovimientos> descripcionCombo) {
        this.descripcionCombo = descripcionCombo;
    }

    public SgMovimientos getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(SgMovimientos movimiento) {
        this.movimiento = movimiento;
    }

    public SgInsumo getInsumoSeleccionado() {
        return insumoSeleccionado;
    }

    public void setInsumoSeleccionado(SgInsumo insumoSeleccionado) {
        this.insumoSeleccionado = insumoSeleccionado;
    }

    public SgPlanCompras getCompraAEliminar() {
        return compraAEliminar;
    }

    public void setCompraAEliminar(SgPlanCompras compraAEliminar) {
        this.compraAEliminar = compraAEliminar;
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

    public FiltroPresupuestoEscolar getFiltroPes() {
        return filtroPes;
    }

    public void setFiltroPes(FiltroPresupuestoEscolar filtroPes) {
        this.filtroPes = filtroPes;
    }

    public FiltroPlanCompras getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroPlanCompras filtro) {
        this.filtro = filtro;
    }

    public SgPlanCompras getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPlanCompras entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialPlanCompras() {
        return historialPlanCompras;
    }

    public void setHistorialPlanCompras(List<RevHistorico> historialPlanCompras) {
        this.historialPlanCompras = historialPlanCompras;
    }

    public LazyPlanComprasDataModel getReglasPlanComprasDataModel() {
        return reglasPlanComprasDataModel;
    }

    public void setReglasPlanComprasDataModel(LazyPlanComprasDataModel reglasPlanComprasDataModel) {
        this.reglasPlanComprasDataModel = reglasPlanComprasDataModel;
    }

    public String getLeyendaTitulo() {
        return leyendaTitulo;
    }

    public void setLeyendaTitulo(String leyendaTitulo) {
        this.leyendaTitulo = leyendaTitulo;
    }

    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }

    public Boolean getAplicaGuardar() {
        return aplicaGuardar;
    }

    public void setAplicaGuardar(Boolean aplicaGuardar) {
        this.aplicaGuardar = aplicaGuardar;
    }

    public BigDecimal getTotalesPlan() {
        return totalesPlan;
    }

    public void setTotalesPlan(BigDecimal totalesPlan) {
        this.totalesPlan = totalesPlan;
    }

    public BigDecimal getTotalPlan() {
        return totalPlan;
    }

    public void setTotalPlan(BigDecimal totalPlan) {
        this.totalPlan = totalPlan;
    }

    public SgEncabezadoPlanCompras getPlanEnEdicion() {
        return planEnEdicion;
    }

    public void setPlanEnEdicion(SgEncabezadoPlanCompras planEnEdicion) {
        this.planEnEdicion = planEnEdicion;
    }

    public FiltroEncabezadoPlanCompra getFiltroPlan() {
        return filtroPlan;
    }

    public void setFiltroPlan(FiltroEncabezadoPlanCompra filtroPlan) {
        this.filtroPlan = filtroPlan;
    }

    public Boolean getBotonCerrar() {
        return botonCerrar;
    }

    public Boolean getVerDatosEstado() {
        return verDatosEstado;
    }

    public void setVerDatosEstado(Boolean verDatosEstado) {
        this.verDatosEstado = verDatosEstado;
    }

    public void setBotonCerrar(Boolean botonCerrar) {
        this.botonCerrar = botonCerrar;
    }

    public Boolean getBotonAprobar() {
        return botonAprobar;
    }

    public void setBotonAprobar(Boolean botonAprobar) {
        this.botonAprobar = botonAprobar;
    }

    public Boolean getBotonRevisar() {
        return botonRevisar;
    }

    public void setBotonRevisar(Boolean botonRevisar) {
        this.botonRevisar = botonRevisar;
    }

    public SofisComboG<EnumPlanCompraEstado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumPlanCompraEstado> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public Boolean getControlRevision() {
        return controlRevision;
    }

    public void setControlRevision(Boolean controlRevision) {
        this.controlRevision = controlRevision;
    }

    public Boolean getBotonObservar() {
        return botonObservar;
    }

    public void setBotonObservar(Boolean botonObservar) {
        this.botonObservar = botonObservar;
    }

    public Boolean getBotonCorregir() {
        return botonCorregir;
    }

    public void setBotonCorregir(Boolean botonCorregir) {
        this.botonCorregir = botonCorregir;
    }

    public Boolean getObseracionLectura() {
        return obseracionLectura;
    }

    public void setObseracionLectura(Boolean obseracionLectura) {
        this.obseracionLectura = obseracionLectura;
    }

    public Boolean getAcciones() {
        return acciones;
    }

    public void setAcciones(Boolean acciones) {
        this.acciones = acciones;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    // </editor-fold>
}
