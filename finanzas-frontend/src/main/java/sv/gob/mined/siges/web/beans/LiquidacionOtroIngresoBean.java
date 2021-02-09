/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesAyuda;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.Liquidacion;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgFactura;
import sv.gob.mined.siges.web.dto.SgLiquidacionOtroIngreso;
import sv.gob.mined.siges.web.dto.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.dto.SgMovimientoLiquidacionOtro;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoLiquidacion;
import sv.gob.mined.siges.web.enumerados.EnumEvaluacionLiquidacion;
import sv.gob.mined.siges.web.enumerados.EnumFacturaEstado;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFactura;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacionOtroIngreso;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoLiquidacionOtro;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyLiquidacionOtroIngresoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.FacturaRestClient;
import sv.gob.mined.siges.web.restclient.LiquidacionOtroIngresoRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoCuentaBancariaRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoLiquidacionOtroRestClient;
import sv.gob.mined.siges.web.restclient.MovimientosRestClient;
import sv.gob.mined.siges.web.restclient.PresupuestoEscolarRestCliente;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión liquidaciones.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class LiquidacionOtroIngresoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LiquidacionOtroIngresoBean.class.getName());

    @Inject
    private LiquidacionOtroIngresoRestClient restClient;

    @Inject
    private MovimientoLiquidacionOtroRestClient movLiquidacionOtroRest;

    @Inject
    private AnioLectivoRestClient anioLectivoRestClient;

    @Inject
    private PresupuestoEscolarRestCliente presupuestoRestClient;

    @Inject
    private MovimientosRestClient movimientosRestClient;

    @Inject
    private FacturaRestClient facturaRestClient;

    @Inject
    private MovimientoCuentaBancariaRestClient movCBRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    @Param(name = "loiPk")
    private Long loiPk;
    private Boolean soloLectura = Boolean.FALSE;

    private Integer paginado = 10;
    private Long totalResultados;

    private Boolean liquidacionGuardada = Boolean.FALSE;
    private SgMovimientoLiquidacionOtro movLiqEnEdicion = new SgMovimientoLiquidacionOtro();
    private SgMovimientoCuentaBancaria movCuentaBcEnEdicion = new SgMovimientoCuentaBancaria();
    private SgSede sedeSeleccionada;

    private FiltroLiquidacionOtroIngreso filtro = new FiltroLiquidacionOtroIngreso();
    private SgLiquidacionOtroIngreso entidadEnEdicion = new SgLiquidacionOtroIngreso();
    private LazyLiquidacionOtroIngresoDataModel liquidacionOtroIngresoLazyModel;

    private List<SgLiquidacionOtroIngreso> historialLiquidacionOtroIngreso = new ArrayList();
    private List<Liquidacion> liquidacion = new ArrayList();
    private List<SgMovimientoCuentaBancaria> ingresosMovs = new ArrayList();
    private List<SgMovimientoCuentaBancaria> egresosMovs = new ArrayList();
    private List<SgMovimientoLiquidacionOtro> listMovs = new ArrayList();

    private SofisComboG<EnumEstadoLiquidacion> comboEstado = new SofisComboG<>();
    private SofisComboG<SgAnioLectivo> comboAnioLectivo = new SofisComboG<>();
    private SofisComboG<EnumEvaluacionLiquidacion> estadosLiq = new SofisComboG<>();

    private Liquidacion ingresos = new Liquidacion(Etiquetas.getValue("labelIngresosLiq"), Etiquetas.getValue("totalMontoDepositado"));
    private Liquidacion egresos = new Liquidacion(Etiquetas.getValue("labelEgresosLiq"), Etiquetas.getValue("totalMontosInvertidos"));

    /**
     * Constructor de la clase.
     */
    public LiquidacionOtroIngresoBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Liquidaciones.
     */
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (loiPk != null && loiPk > 0) {
                obtenerLiquidacion(loiPk);
                if (entidadEnEdicion.getLoiPk() != null) {
                    actualizar();
                }
            } else {
                agregar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtener entidadEnEdicion
     */
    private void obtenerLiquidacion(Long loiPk) {
        try {
            FiltroLiquidacionOtroIngreso filtroLiq = new FiltroLiquidacionOtroIngreso();
            filtroLiq.setIncluirCampos(new String[]{
                "loiPk", "loiSedePk.sedPk", "loiSedePk.sedCodigo", "loiSedePk.sedNombre", "loiSedePk.sedTipo", "loiSedePk.sedVersion",
                "loiAnioPk.alePk", "loiAnioPk.aleAnio", "loiAnioPk.aleVersion", "loiEstado", "loiVersion"});
            filtroLiq.setLoiPk(loiPk);
            List<SgLiquidacionOtroIngreso> list = restClient.buscar(filtroLiq);
            if (list != null && !list.isEmpty() && list.get(0) != null) {
                liquidacionGuardada = Boolean.TRUE;
                entidadEnEdicion = list.get(0);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Obtiene el título de la página correspondiente al bean
     *
     * @return
     */
    public String getTituloPagina() {
        if (this.loiPk == null) {
            return Etiquetas.getValue("nuevoLiquidacionOtroIngreso");
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verLiquidacionOtroIngreso");
        } else {
            return Etiquetas.getValue("edicionLiquidacionOtroIngreso");
        }
    }

    /**
     * Carga el combo de Sede para creación y edición de registros.
     *
     * @param query String: Palabra de filtro digitada en el combo
     * @return List<SgSede>
     */
    public List<SgSede> completeSede(String query) {
        try {
            return obtenerSedes(query);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    /**
     * Carga el combo de Sede para creación y edición de registros.
     *
     * @param query String: Palabra de filtro digitada en el combo
     * @return List<SgSede>
     */
    public List<SgSede> obtenerSedes(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
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
     * Carga el combo de años electivos
     *
     * @return List<SgAnioElectivo>
     */
    public List<SgAnioLectivo> cargarAnios() {
        try {
            FiltroAnioLectivo filtroAnioLectivo = new FiltroAnioLectivo();
            filtro.setIncluirCampos(new String[]{"alePk", "aleAnio", "aleVersion"});
            filtroAnioLectivo.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            filtroAnioLectivo.setOrderBy(new String[]{"aleAnio"});
            filtroAnioLectivo.setAscending(new boolean[]{false});
            List<SgAnioLectivo> listAniosLectivos = anioLectivoRestClient.buscar(filtroAnioLectivo);
            return listAniosLectivos;
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
     * Busca los registros de Liquidaciones según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            liquidacionOtroIngresoLazyModel = new LazyLiquidacionOtroIngresoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de filtros.
     */
    public void cargarCombos() {
        try {

            comboAnioLectivo = new SofisComboG<>(cargarAnios(), "aleAnio");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (estadosLiq.isEmpty()) {
                estadosLiq = new SofisComboG(Arrays.asList(EnumEvaluacionLiquidacion.values()), "text");
                estadosLiq.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            } else {
                estadosLiq.setSelected(-1);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {

    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroLiquidacionOtroIngreso();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Liquidaciones.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgLiquidacionOtroIngreso();
        entidadEnEdicion.setLoiEstado(EnumEstadoLiquidacion.CONFIRMADA);
        liquidacion = new ArrayList();
    }

    /**
     * Crea o actualiza un registro deLiquidaciones.
     *
     * @param SgFactura : var.
     */
    public void actualizar() {
        JSFUtils.limpiarMensajesError();
        liquidacion = new ArrayList();
        sedeSeleccionada = entidadEnEdicion.getLoiSedePk();

        comboAnioLectivo.setSelectedT(entidadEnEdicion.getLoiAnioPk());

        if (entidadEnEdicion.getLoiPk() != null) {
            obtenerMovimientoLiq();
        } else {
            obtenerIngresosEgresos();
        }
    }

    /**
     * Crea o actualiza un registro de Liquidaciones.
     */
    public void guardar() {
        try {

            if (comboAnioLectivo.getSelectedT() != null && sedeSeleccionada != null) {
                if (!ingresosMovs.isEmpty()) {
                    entidadEnEdicion.setLoiAnioPk(comboAnioLectivo.getSelectedT());
                    entidadEnEdicion.setLoiSedePk(sedeSeleccionada);
                    entidadEnEdicion = restClient.guardarConMovimientos(entidadEnEdicion);
                    obtenerLiquidacion(entidadEnEdicion.getLoiPk());
                    obtenerMovimientoLiq();
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_LIQUIDACION_INGRESOS_GUARDAR), "");
                }
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_LIQUIDACION_GUARDAR), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Obtiene los movimientos de la liquidacion no guardada
     */
    public void obtenerIngresosEgresos() {
        try {
            liquidacion = new ArrayList();
            if (comboAnioLectivo.getSelectedT() != null && sedeSeleccionada != null) {

                ingresosMovs = new ArrayList();
                FiltroMovimientoCuentaBancaria filtroMovCB = new FiltroMovimientoCuentaBancaria();
                filtroMovCB.setIncluirCampos(new String[]{
                    "mcbPk", "mcbDetalle", "mcbFecha", "mcbMonto", "mcbVersion","mcbTipoMovimiento"
                });
                filtroMovCB.setSedePresupuesto(sedeSeleccionada.getSedPk());
                filtroMovCB.setAnioFiscal(comboAnioLectivo.getSelectedT().getAleAnio());
                filtroMovCB.setMcbTipoMovimiento(TipoMovimiento.HABER);
                ingresosMovs = movCBRestClient.buscar(filtroMovCB);
                if (!ingresosMovs.isEmpty()) {
                    ingresos.setMovimientos(ingresosMovs);
                    ingresos.setTotal(ingresosMovs.stream().map(im -> im.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add));
                }

                egresosMovs = new ArrayList();
                FiltroFactura filtroFac = new FiltroFactura();
                filtroFac.setSedePk(sedeSeleccionada.getSedPk());
                filtroFac.setAnioFiscal(comboAnioLectivo.getSelectedT().getAleAnio());
                filtroFac.setMovimientoOrigen(EnumMovimientosOrigen.P);
                filtroFac.setFacEstado(EnumFacturaEstado.PAGADO);
                filtroFac.setIncluirCampos(new String[]{"facPk", "pago.pgsMovimientoCBFk",
                    "pago.pgsMovimientoCBFk.mcbPk", "pago.pgsMovimientoCBFk.mcbDetalle", "pago.pgsMovimientoCBFk.mcbFecha",
                    "pago.pgsMovimientoCBFk.mcbMonto", "pago.pgsMovimientoCBFk.mcbVersion","pago.pgsMovimientoCBFk.mcbTipoMovimiento",
                    "pago.pgsFactura.facNumero","pago.pgsFactura.facProveedorFk.proNombre","pago.pgsNumeroCheque",
                    "facVersion"});

                List<SgFactura> listFacturas = facturaRestClient.buscar(filtroFac);
                if (!listFacturas.isEmpty()) {
                    egresosMovs = listFacturas.stream().filter(f -> f.getPago() != null).filter(f -> f.getPago().getPgsMovimientoCBFk() != null).map(f -> f.getPago().getPgsMovimientoCBFk()).collect(Collectors.toList());
                }
                egresos.setTotal(egresosMovs.stream().map(im -> im.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add));
                egresos.setMovimientos(egresosMovs);

            }

            liquidacion.add(ingresos);
            liquidacion.add(egresos);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Obtiene los movimientos de la liquidacion guardada
     */
    public void obtenerMovimientoLiq() {
        try {
            liquidacion = new ArrayList();
            if (entidadEnEdicion.getLoiPk() != null) {

                ingresosMovs = new ArrayList();
                egresosMovs = new ArrayList();
                FiltroMovimientoLiquidacionOtro filtroMovLiq = new FiltroMovimientoLiquidacionOtro();
                filtroMovLiq.setMloLiquidacionOtroPk(entidadEnEdicion.getLoiPk());
                filtroMovLiq.setIncluirCampos(new String[]{"mloPk", "mloMovimientoFk.mcbPk",
                    "mloMovimientoFk.mcbFecha", "mloMovimientoFk.mcbDetalle",
                    "mloMovimientoFk.mcbMonto", "mloMovimientoFk.mcbVersion",
                    "mloMovimientoFk.movimientoLiquidacionOtro.mloPk", "mloMovimientoFk.movimientoLiquidacionOtro.mloVersion",
                    "mloMovimientoFk.movimientoLiquidacionOtro.mloEvaluado", "mloMovimientoFk.movimientoLiquidacionOtro.mloComentario",
                    "mloMovimientoFk.movimientoLiquidacionOtro.mloMovimientoFk.mcbPk", "mloMovimientoFk.movimientoLiquidacionOtro.mloMovimientoFk.mcbVersion",
                    "mloMovimientoFk.movimientoLiquidacionOtro.mloLiquidacionOtroFk.loiPk", "mloMovimientoFk.movimientoLiquidacionOtro.mloLiquidacionOtroFk.loiVersion",
                    "mloLiquidacionOtroFk.loiPk", "mloLiquidacionOtroFk.loiVersion",
                    "mloMovimientoFk.mcbTipoMovimiento",
                    "mloMovimientoFk.mcbPagoFk.pgsNumeroCheque","mloMovimientoFk.mcbPagoFk.pgsFactura.facNumero","mloMovimientoFk.mcbPagoFk.pgsFactura.facProveedorFk.proNombre",
                    "mloTipoMovimiento", "mloEvaluado", "mloComentario", "mloVersion"});

                listMovs = movLiquidacionOtroRest.buscar(filtroMovLiq);

                if (!listMovs.isEmpty()) {
                    ingresosMovs = listMovs.stream().filter(l -> l.getMloTipoMovimiento().equals(EnumMovimientosTipo.I))
                            .map(l -> l.getMloMovimientoFk()).sorted(Comparator.comparing(l -> l.getMcbPk())).collect(Collectors.toList());
                    if (!ingresosMovs.isEmpty()) {
                        ingresos.setMovimientos(ingresosMovs);
                        ingresos.setTotal(ingresosMovs.stream().map(im -> im.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add));
                    }

                    egresosMovs = listMovs.stream().filter(l -> l.getMloTipoMovimiento().equals(EnumMovimientosTipo.E)).map(l -> l.getMloMovimientoFk()).collect(Collectors.toList());

                    if (!egresosMovs.isEmpty()) {
                        egresos.setTotal(egresosMovs.stream().map(im -> im.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add));
                        egresos.setMovimientos(egresosMovs);
                    }
                }
            }

            liquidacion.add(ingresos);
            liquidacion.add(egresos);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Obtiene el saldo entre los ingresos y egresos
     */
    public BigDecimal obtenerSaldo() {
        BigDecimal saldo = BigDecimal.ZERO;
        try {
            if (!liquidacion.isEmpty() && liquidacion.size() > 1) {
                if (liquidacion.get(0) != null && liquidacion.get(1) != null) {
                    if (liquidacion.get(0).getTotal() != null && liquidacion.get(1).getTotal() != null) {
                        saldo = liquidacion.get(0).getTotal().subtract(liquidacion.get(1).getTotal());
                    } else if (liquidacion.get(0).getTotal() != null && liquidacion.get(1).getTotal() == null) {
                        saldo = liquidacion.get(0).getTotal();
                    } else if (liquidacion.get(0).getTotal() == null && liquidacion.get(1).getTotal() != null) {
                        saldo = BigDecimal.ZERO.subtract(liquidacion.get(1).getTotal());
                    }
                }
            }
            return saldo;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return saldo;
    }

    /**
     * Cambia el estado movimiento liquidación
     */
    public void cambiarEstadoMov(SgMovimientoCuentaBancaria var) {
        try {
            movCuentaBcEnEdicion = (SgMovimientoCuentaBancaria) SerializationUtils.clone(var);
            estadosLiq.setSelected(-1);
            Optional<SgMovimientoLiquidacionOtro> opMovLiq = listMovs.stream().filter(m -> m.getMloMovimientoFk().getMcbPk().equals(movCuentaBcEnEdicion.getMcbPk())).findFirst();
            if (opMovLiq.isPresent()) {
                movLiqEnEdicion = opMovLiq.get();
                if (movLiqEnEdicion.getMloEvaluado() != null) {
                    estadosLiq.setSelectedT(movLiqEnEdicion.getMloEvaluado() == Boolean.TRUE ? EnumEvaluacionLiquidacion.CORRECTO : EnumEvaluacionLiquidacion.NO_CORRECTO);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, "No se pudo evaluar el movimiento", "");
        }
    }

    /**
     * Cambia el estado de la liquidación
     */
    public void cambiarEstadoLiquidacion() {
        try {
            Long totalMovs = 0L;
            Long nocorrectos = 0L;
            Long correctos = 0L;

            totalMovs = listMovs.stream().count();
            nocorrectos = listMovs.stream().filter(m -> m.getMloEvaluado() != null).filter(m -> m.getMloEvaluado().equals(Boolean.FALSE)).count();
            correctos = listMovs.stream().filter(m -> m.getMloEvaluado() != null).filter(m -> m.getMloEvaluado().equals(Boolean.TRUE)).count();

            if (nocorrectos > 0L) {
                if (!entidadEnEdicion.getLoiEstado().equals(EnumEstadoLiquidacion.EVALUADA)) {
                    entidadEnEdicion.setLoiEstado(EnumEstadoLiquidacion.EVALUADA);
                    restClient.guardar(entidadEnEdicion);
                    obtenerLiquidacion(entidadEnEdicion.getLoiPk());
                }
            } else if (correctos < totalMovs) {
                if (!entidadEnEdicion.getLoiEstado().equals(EnumEstadoLiquidacion.EVALUADA)) {
                    entidadEnEdicion.setLoiEstado(EnumEstadoLiquidacion.EVALUADA);
                    restClient.guardar(entidadEnEdicion);
                    obtenerLiquidacion(entidadEnEdicion.getLoiPk());
                }
            } else if (totalMovs.equals(correctos)) {
                if (!entidadEnEdicion.getLoiEstado().equals(EnumEstadoLiquidacion.APROBADA)) {
                    entidadEnEdicion.setLoiEstado(EnumEstadoLiquidacion.APROBADA);
                    restClient.guardar(entidadEnEdicion);
                    obtenerLiquidacion(entidadEnEdicion.getLoiPk());
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Pre Evaluación de Movimiento
     */
    public void evaluarMovimientosLiquidacion() {
        try {
            if (liquidacion.get(0) != null) {
                for (SgMovimientoCuentaBancaria mov : liquidacion.get(0).getMovimientos()) {
                    if (mov.getCorrecto().equals(Boolean.TRUE)) {
                        if (mov.getMovimientoLiquidacionOtro() != null) {
                            movLiqEnEdicion = mov.getMovimientoLiquidacionOtro();
                            movLiqEnEdicion.setMloEvaluado(Boolean.TRUE);
                            movLiquidacionOtroRest.guardar(movLiqEnEdicion);
                        }
                    } else if (mov.getNoCorrecto().equals(Boolean.TRUE)) {

                        movCuentaBcEnEdicion = mov;
                        if (mov.getMovimientoLiquidacionOtro() != null) {
                            movLiqEnEdicion = mov.getMovimientoLiquidacionOtro();
                            movLiqEnEdicion.setMloEvaluado(Boolean.FALSE);
                            movLiquidacionOtroRest.guardar(movLiqEnEdicion);
                        }
                    }
                }

            }

            if (liquidacion.get(1) != null) {
                for (SgMovimientoCuentaBancaria mov : liquidacion.get(1).getMovimientos()) {
                    if (mov.getCorrecto().equals(Boolean.TRUE)) {
                        if (mov.getMovimientoLiquidacionOtro() != null) {
                            movLiqEnEdicion = mov.getMovimientoLiquidacionOtro();
                            movLiqEnEdicion.setMloEvaluado(Boolean.TRUE);
                            movLiquidacionOtroRest.guardar(movLiqEnEdicion);
                        }

                    } else if (mov.getNoCorrecto().equals(Boolean.TRUE)) {

                        movCuentaBcEnEdicion = mov;
                        if (mov.getMovimientoLiquidacionOtro() != null) {
                            movLiqEnEdicion = mov.getMovimientoLiquidacionOtro();
                            movLiqEnEdicion.setMloEvaluado(Boolean.FALSE);
                            movLiquidacionOtroRest.guardar(movLiqEnEdicion);
                        }
                    }
                }
            }

            obtenerMovimientoLiq();
            cambiarEstadoLiquidacion();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de movimientos de cuenta bancaria.
     */
    public void evaluarMovLiquidacion() {
        try {

            if (StringUtils.isBlank(movLiqEnEdicion.getMloComentario())) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "Campo motivo no puede ser vacío. ", "");
                return;
            } else {
                movLiqEnEdicion.setMloEvaluado(Boolean.FALSE);
            }

            movLiquidacionOtroRest.guardar(movLiqEnEdicion);

            obtenerMovimientoLiq();
            cambiarEstadoLiquidacion();
            PrimeFaces.current().executeScript("PF('itemDialog2').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Label evaluado movimiento
     */
    public String labelEvaluadoMov(SgMovimientoCuentaBancaria var) {
        String label = new String();
        try {

            SgMovimientoCuentaBancaria cuenta = (SgMovimientoCuentaBancaria) SerializationUtils.clone(var);
            if (cuenta.getMovimientoLiquidacionOtro().getMloEvaluado() != null) {
                if (cuenta.getMovimientoLiquidacionOtro().getMloEvaluado().equals(Boolean.TRUE)) {
                    label = "EVALUADO - CORRECTO";
                } else {
                    label = "EVALUADO - NO CORRECTO";
                }
            } else {
                label = "NO EVALUADO";
            }
            return label;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return label;
    }
    
       /**
     * Label evaluado movimiento
     */
    public String labelDetalleMov(SgMovimientoCuentaBancaria var) {
        String label = new String();
        try {

            SgMovimientoCuentaBancaria cuenta = (SgMovimientoCuentaBancaria) SerializationUtils.clone(var);
            if(cuenta.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE)){
                if(cuenta.getMcbPagoFk()!=null){
                    String cheque = cuenta.getMcbPagoFk().getPgsNumeroCheque()!=null ? cuenta.getMcbPagoFk().getPgsNumeroCheque().toString() : StringUtils.EMPTY;
                    String facNum = cuenta.getMcbPagoFk().getPgsFactura()!=null ? cuenta.getMcbPagoFk().getPgsFactura().getFacNumero() : StringUtils.EMPTY;
                    String proveedor = cuenta.getMcbPagoFk().getPgsFactura().getFacProveedorFk()!=null ? cuenta.getMcbPagoFk().getPgsFactura().getFacProveedorFk().getProNombre() : StringUtils.EMPTY;
                    
                    label = ConstantesAyuda.CHEQUE.concat(" # ").concat(cheque).concat(" ").concat(ConstantesAyuda.FACTURA).concat(" # ").concat(facNum).concat(" ").concat(ConstantesAyuda.PROVEEDOR).concat(": ").concat(proveedor);
                }
            }
            else{
                label = cuenta.getMcbDetalle();
            }
            return label;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return label;
    }
    
    /**
     * Style columna de evaluación movimiento liquidación
     */
    public String estiloColumnaDetalleMov(SgMovimientoCuentaBancaria var) {
        String label = new String();
        try {

            SgMovimientoCuentaBancaria mov = (SgMovimientoCuentaBancaria) SerializationUtils.clone(var);
            if(mov.getMovimientoLiquidacionOtro().getMloEvaluado()==null){
                label = "columnaRoja";
            }
            else{
                if(mov.getMovimientoLiquidacionOtro().getMloEvaluado().equals(Boolean.FALSE)){
                    label = "columnaAmarilla";
                }
                else if(mov.getMovimientoLiquidacionOtro().getMloEvaluado().equals(Boolean.TRUE)){
                    label = "columnaVerde";
                }
            }
            
            return label;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return label;
    }

    /**
     * Elimina un registro de Liquidaciones.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getLoiPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id Long: id del registro del cual se quiere obtener el historial
     */
    public void historial(Long id) {
        try {
            historialLiquidacionOtroIngreso = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroLiquidacionOtroIngreso getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroLiquidacionOtroIngreso filtro) {
        this.filtro = filtro;
    }

    public SgLiquidacionOtroIngreso getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgLiquidacionOtroIngreso entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgLiquidacionOtroIngreso> getHistorialLiquidacionOtroIngreso() {
        return historialLiquidacionOtroIngreso;
    }

    public void setHistorialLiquidacionOtroIngreso(List<SgLiquidacionOtroIngreso> historialLiquidacionOtroIngreso) {
        this.historialLiquidacionOtroIngreso = historialLiquidacionOtroIngreso;
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

    public LazyLiquidacionOtroIngresoDataModel getLiquidacionOtroIngresoLazyModel() {
        return liquidacionOtroIngresoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyLiquidacionOtroIngresoDataModel liquidacionOtroIngresoLazyModel) {
        this.liquidacionOtroIngresoLazyModel = liquidacionOtroIngresoLazyModel;
    }

    public SgMovimientoLiquidacionOtro getMovLiqEnEdicion() {
        return movLiqEnEdicion;
    }

    public void setMovLiqEnEdicion(SgMovimientoLiquidacionOtro movLiqEnEdicion) {
        this.movLiqEnEdicion = movLiqEnEdicion;
    }

    public SgMovimientoCuentaBancaria getMovCuentaBcEnEdicion() {
        return movCuentaBcEnEdicion;
    }

    public void setMovCuentaBcEnEdicion(SgMovimientoCuentaBancaria movCuentaBcEnEdicion) {
        this.movCuentaBcEnEdicion = movCuentaBcEnEdicion;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public List<Liquidacion> getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(List<Liquidacion> liquidacion) {
        this.liquidacion = liquidacion;
    }

    public SofisComboG<EnumEstadoLiquidacion> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoLiquidacion> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public SofisComboG<EnumEvaluacionLiquidacion> getEstadosLiq() {
        return estadosLiq;
    }

    public void setEstadosLiq(SofisComboG<EnumEvaluacionLiquidacion> estadosLiq) {
        this.estadosLiq = estadosLiq;
    }
    // </editor-fold>
}
