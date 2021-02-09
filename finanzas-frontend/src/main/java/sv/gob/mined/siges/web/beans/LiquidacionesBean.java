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
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.Liquidacion;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgFactura;
import sv.gob.mined.siges.web.dto.SgLiquidacion;
import sv.gob.mined.siges.web.dto.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoLiquidacion;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.web.enumerados.EnumTipoDocumentoPago;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFactura;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyLiquidacionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.FacturaRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.LiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoCuentaBancariaRestClient;
import sv.gob.mined.siges.web.restclient.MovimientosRestClient;
import sv.gob.mined.siges.web.restclient.PresupuestoEscolarRestCliente;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de liquidaciones.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class LiquidacionesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LiquidacionesBean.class.getName());

    @Inject
    private LiquidacionRestClient restClient;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteClient;

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
    private GesPresEsRestClient subComponenteClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private CatalogosRestClient restCatalogo;
    
    @Inject
    private SessionBean sessionBean;
    
    

    private Integer paginado = 10;
    private Long totalResultados;
    private LazyLiquidacionDataModel liquidacionLazyModel;
    private FiltroLiquidacion filtro = new FiltroLiquidacion();
    private FiltroPresupuestoEscolar filtroPresupuesto = new FiltroPresupuestoEscolar();
    private FiltroMovimientos filtroMov = new FiltroMovimientos();

    private SgLiquidacion entidadEnEdicion = new SgLiquidacion();
    private SgSede sedeSeleccionadaFiltro;
    private SgSede sedeSeleccionada;

    private List<SgLiquidacion> historialLiquidacion = new ArrayList();
    private List<SgMovimientos> listMov = new ArrayList();
    private List<Liquidacion> liquidacion = new ArrayList();
    private List<SgMovimientoCuentaBancaria> ingresosMovs = new ArrayList();
    private List<SgMovimientoCuentaBancaria> egresosMovs = new ArrayList();

    private SofisComboG<EnumEstadoLiquidacion> comboEstado = new SofisComboG<>();
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();
    private SofisComboG<SgAnioLectivo> comboAnioLectivo = new SofisComboG<>();
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponenteN = new SofisComboG<>();
    private SofisComboG<SgAnioLectivo> comboAnioLectivoN = new SofisComboG<>();
    private SofisComboG<SgDepartamento> comboDepartamento = new SofisComboG<>();

    private Liquidacion ingresos = new Liquidacion(Etiquetas.getValue("labelIngresosLiq"), Etiquetas.getValue("totalMontoDepositado"));
    private Liquidacion egresos = new Liquidacion(Etiquetas.getValue("labelEgresosLiq"), Etiquetas.getValue("totalMontosInvertidos"));

    /**
     * Constructor de la clase.
     */
    public LiquidacionesBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Liquidaciones.
     */
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca los registros de Liquidaciones según los filtros utilizados.
     */
    public void buscar() {
        try {

            if (comboAnioLectivo.getSelectedT() != null) {
                filtro.setAnioFiscal(paginado);
            }

            if (comboEstado.getSelectedT() != null) {
                filtro.setLiqEstado(comboEstado.getSelectedT());
            }

            if (comboComponente.getSelectedT() != null) {
                filtro.setLiqComponentePk(comboComponente.getSelectedT().getCpeId());
            }

            if (sedeSeleccionadaFiltro != null) {
                filtro.setLiqSedePk(sedeSeleccionadaFiltro.getSedPk());
            }
            
            if(comboDepartamento.getSelectedT()!=null){
                filtro.setDepartamentoPk(comboDepartamento.getSelectedT().getDepPk());
            }

            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{"liqPk", "liqEstado", "liqVersion",
                "liqComponentePk.cpeId", "liqComponentePk.cpeCodigo", "liqComponentePk.cpeNombre", "liqComponentePk.cpeVersion",
                "liqSubComponenteFk.gesId", "liqSubComponenteFk.gesCod", "liqSubComponenteFk.gesNombre", "liqSubComponenteFk.gesVersion",
                "liqSedePk.sedPk", "liqSedePk.sedCodigo",
                "liqSedePk.sedNombre", "liqSedePk.sedTipo", "liqSedePk.sedVersion",
                "liqAnioPk.aleAnio", "liqAnioPk.alePk", "liqAnioPk.aleVersion", "liqUltModFecha"
            });
            totalResultados = restClient.buscarTotal(filtro);
            liquidacionLazyModel = new LazyLiquidacionDataModel(restClient, filtro, totalResultados, paginado);
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
            FiltroCodiguera filtroComponente = new FiltroCodiguera();
            filtroComponente.setOrderBy(new String[]{"cpeNombre"});
            filtroComponente.setIncluirCampos(new String[]{"cpeVersion", "cpeNombre", "cpeCodigo"});
            comboComponente = new SofisComboG<>(componenteClient.buscar(filtroComponente), "cpeNombre");
            comboComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboEstado = new SofisComboG<>(Arrays.asList(EnumEstadoLiquidacion.values()), "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboAnioLectivo = new SofisComboG<>(cargarAnios(), "aleAnio");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depPk","depNombre", "depVersion"});
            List<SgDepartamento> departamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(departamentos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de agregar.
     */
    public void cargarCombosAgregar() {
        try {
            comboAnioLectivoN = new SofisComboG<>(cargarAnios(), "aleAnio");
            comboAnioLectivoN.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de componentes.
     */
    public void cargarComponentes() {
        try {
            if (comboAnioLectivoN.getSelectedT() != null && sedeSeleccionada != null) {
                filtroPresupuesto = new FiltroPresupuestoEscolar();
                filtroPresupuesto.setIncluirCampos(new String[]{"pesPk", "pesVersion"});
                filtroPresupuesto.setAnioFiscal(comboAnioLectivoN.getSelectedT().getAleAnio());
                filtroPresupuesto.setIdSede(sedeSeleccionada.getSedPk());
                List<SgPresupuestoEscolar> list = presupuestoRestClient.buscar(filtroPresupuesto);

                if (!list.isEmpty() && list.get(0) != null) {
                    filtroMov = new FiltroMovimientos();
                    filtroMov.setIncluirCampos(new String[]{"movPk", "movVersion",
                        "movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeId",
                        "movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeCodigo",
                        "movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeNombre",
                        "movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeVersion"
                    });
                    filtroMov.setMovPresupuestoFk(list.get(0).getPesPk());
                    filtroMov.setMovTipo(EnumMovimientosTipo.I);
                    filtroMov.setMovOrigen(EnumMovimientosOrigen.T);
                    List<SgMovimientos> movs = movimientosRestClient.buscar(filtroMov);
                    if (!movs.isEmpty()) {
                        List<SsCategoriaPresupuestoEscolar> componentes = movs.stream().filter(m -> m.getMovTechoPresupuestal() != null).
                                map(m -> m.getMovTechoPresupuestal().getSubComponente().getGesCategoriaComponente()).collect(Collectors.toList());
                        comboComponenteN = new SofisComboG<>(componentes, "cpeNombre");
                    }
                }
            }

            comboComponenteN.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el combo de subcomponente
     */
    public void cargarSubComponente() {
        try {
            if (comboComponente.getSelectedT() != null) {
                FiltroGesPresEs filtroSubComponente = new FiltroGesPresEs();
                filtroSubComponente.setOrderBy(new String[]{"gesNombre"});
                filtroSubComponente.setIncluirCampos(new String[]{"gesVersion", "gesNombre", "gesCod"});
                filtroSubComponente.setCpeId(comboComponente.getSelectedT().getCpeId());
                comboSubComponente = new SofisComboG<>(subComponenteClient.buscarSubcomponente(filtroSubComponente), "gesNombre");
                comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
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
    public List<SgSede> completeSedeFiltro(String query) {
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
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {

    }

    /**
     * Limpia los combos usados para filtrar y agregar.
     */
    private void limpiarCombosAgregar() {
        sedeSeleccionada = null;
        comboAnioLectivoN = new SofisComboG<>();
        comboComponenteN = new SofisComboG<>();
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroLiquidacion();
        comboAnioLectivo.setSelected(-1);
        comboComponente.setSelected(-1);
        comboSubComponente.setSelected(-1);
        comboEstado.setSelected(-1);
        comboDepartamento.setSelected(-1);
        sedeSeleccionadaFiltro = null;
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Liquidaciones.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgLiquidacion();
        liquidacion = new ArrayList();
        limpiarCombosAgregar();
        cargarCombosAgregar();
    }

    /**
     * Obtiene los ingresos y egresos.
     */
    public void obtenerIngresosEgresos() {
        try {
            if (comboComponenteN.getSelectedT() != null && sedeSeleccionada != null) {

                ingresosMovs = new ArrayList();
                FiltroMovimientoCuentaBancaria filtroMovCB = new FiltroMovimientoCuentaBancaria();
                filtroMovCB.setIncluirCampos(new String[]{
                    "mcbPk", "mcbDetalle", "mcbFecha", "mcbMonto", "mcbVersion"
                });
                filtroMovCB.setComponenteIngresoPk(comboComponenteN.getSelectedT().getCpeId());
                filtroMovCB.setSedePk(sedeSeleccionada.getSedPk());
                filtroMovCB.setMcbTipoMovimiento(TipoMovimiento.HABER);
                ingresosMovs = movCBRestClient.buscar(filtroMovCB);
                if (!ingresosMovs.isEmpty()) {
                    ingresos.setMovimientos(ingresosMovs);
                    ingresos.setTotal(ingresosMovs.stream().map(im -> im.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add));
                }

                egresosMovs = new ArrayList();
                FiltroFactura filtroFac = new FiltroFactura();
                filtroFac.setComponente(comboComponenteN.getSelectedT().getCpeId());
                filtroFac.setSedePk(sedeSeleccionada.getSedPk());
                filtroFac.setFacTipoDocumento(EnumTipoDocumentoPago.FACTURA);
                filtroFac.setIncluirCampos(new String[]{"facPk", "pago.pgsMovimientoCBFk",
                    "pago.pgsMovimientoCBFk.mcbPk", "pago.pgsMovimientoCBFk.mcbDetalle", "pago.pgsMovimientoCBFk.mcbFecha",
                    "pago.pgsMovimientoCBFk.mcbMonto", "pago.pgsMovimientoCBFk.mcbVersion",
                    "facVersion"});

                List<SgFactura> listFacturas = facturaRestClient.buscar(filtroFac);
                if (!listFacturas.isEmpty()) {
                    egresosMovs = listFacturas.stream().map(f -> f.getPago().getPgsMovimientoCBFk()).collect(Collectors.toList());
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
     * Devuelve los totales de los movimientos
     */
    public BigDecimal obtenerSaldo() {
        BigDecimal saldo = BigDecimal.ZERO;
        try {
            if (!liquidacion.isEmpty() && liquidacion.size() > 1) {
                if (liquidacion.get(0).getTotal() != null && liquidacion.get(1).getTotal() != null) {
                    saldo = liquidacion.get(0).getTotal().subtract(liquidacion.get(1).getTotal());
                } else if (liquidacion.get(0).getTotal() != null && liquidacion.get(1).getTotal() == null) {
                    saldo = liquidacion.get(0).getTotal();
                } else if (liquidacion.get(0).getTotal() == null && liquidacion.get(1).getTotal() != null) {
                    saldo = BigDecimal.ZERO.subtract(liquidacion.get(1).getTotal());
                }
            }
            return saldo;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return saldo;
    }

    /**
     * Crea o actualiza un registro de Liquidaciones.
     *
     * @param SgLiquidacion : var.
     */
    public void actualizar(SgLiquidacion var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombosAgregar();
        liquidacion = new ArrayList();
        entidadEnEdicion = (SgLiquidacion) SerializationUtils.clone(var);
        sedeSeleccionada = entidadEnEdicion.getLiqSedePk();
        comboAnioLectivoN = new SofisComboG<>(cargarAnios(), "aleAnio");
        comboAnioLectivoN.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboAnioLectivoN.setSelectedT(entidadEnEdicion.getLiqAnioPk());

        cargarComponentes();
        comboComponenteN.setSelectedT(entidadEnEdicion.getLiqComponentePk());

        obtenerIngresosEgresos();
        //obtenerEgresos();

    }

    /**
     * Crea o actualiza un registro de Liquidaciones.
     */
    public void guardar() {
        try {
            if (comboAnioLectivoN.getSelectedT() != null && sedeSeleccionada != null && comboComponenteN.getSelectedT() != null && !ingresosMovs.isEmpty() && !egresosMovs.isEmpty()) {
                entidadEnEdicion.setLiqAnioPk(comboAnioLectivoN.getSelectedT());
                entidadEnEdicion.setLiqSedePk(sedeSeleccionada);
                entidadEnEdicion.setLiqComponentePk(comboComponenteN.getSelectedT());
                entidadEnEdicion.setLiqEstado(EnumEstadoLiquidacion.CONFIRMADA);
                entidadEnEdicion = restClient.guardarConMovimientos(entidadEnEdicion);

                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                buscar();
            } else {
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_LIQUIDACION_GUARDAR), "");
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Elimina un registro de Liquidaciones.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getLiqPk());
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
            historialLiquidacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroLiquidacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroLiquidacion filtro) {
        this.filtro = filtro;
    }

    public SgLiquidacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgLiquidacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgLiquidacion> getHistorialLiquidacion() {
        return historialLiquidacion;
    }

    public void setHistorialLiquidacion(List<SgLiquidacion> historialLiquidacion) {
        this.historialLiquidacion = historialLiquidacion;
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

    public LazyLiquidacionDataModel getLiquidacionLazyModel() {
        return liquidacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyLiquidacionDataModel liquidacionLazyModel) {
        this.liquidacionLazyModel = liquidacionLazyModel;
    }

    public SofisComboG<SsCategoriaPresupuestoEscolar> getComboComponente() {
        return comboComponente;
    }

    public void setComboComponente(SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente) {
        this.comboComponente = comboComponente;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public SofisComboG<SsCategoriaPresupuestoEscolar> getComboComponenteN() {
        return comboComponenteN;
    }

    public void setComboComponenteN(SofisComboG<SsCategoriaPresupuestoEscolar> comboComponenteN) {
        this.comboComponenteN = comboComponenteN;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<EnumEstadoLiquidacion> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoLiquidacion> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivoN() {
        return comboAnioLectivoN;
    }

    public void setComboAnioLectivoN(SofisComboG<SgAnioLectivo> comboAnioLectivoN) {
        this.comboAnioLectivoN = comboAnioLectivoN;
    }

    public List<Liquidacion> getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(List<Liquidacion> liquidacion) {
        this.liquidacion = liquidacion;
    }

    public SofisComboG<SsGesPresEs> getComboSubComponente() {
        return comboSubComponente;
    }

    public void setComboSubComponente(SofisComboG<SsGesPresEs> comboSubComponente) {
        this.comboSubComponente = comboSubComponente;
    }
    
    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    // </editor-fold>

    
}
