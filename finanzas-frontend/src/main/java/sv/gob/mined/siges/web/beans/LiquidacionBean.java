/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
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
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesAyuda;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.Liquidacion;
import sv.gob.mined.siges.web.dto.ResumenLiquidacion;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgEvaluacionItemLiquidacion;
import sv.gob.mined.siges.web.dto.SgEvaluacionLiquidacion;
import sv.gob.mined.siges.web.dto.SgFactura;
import sv.gob.mined.siges.web.dto.SgItemLiquidacion;
import sv.gob.mined.siges.web.dto.SgLiquidacion;
import sv.gob.mined.siges.web.dto.SgMovimientoCajaChica;
import sv.gob.mined.siges.web.dto.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.dto.SgMovimientoLiquidacion;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoLiquidacion;
import sv.gob.mined.siges.web.enumerados.EnumEvaluacionLiquidacion;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEvaluacionItemLiquidacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFactura;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoLiquidacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyLiquidacionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.EvaluacionItemLiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.EvaluacionLiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.FacturaRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.LiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoCajaChicaRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoCuentaBancariaRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoLiquidacionRestClient;
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
public class LiquidacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LiquidacionBean.class.getName());

    @Inject
    private LiquidacionRestClient restClient;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteClient;

    @Inject
    private EvaluacionItemLiquidacionRestClient evaluacionItemLiqRest;

    @Inject
    private GesPresEsRestClient subComponenteClient;

    @Inject
    private MovimientoLiquidacionRestClient movLiquidacionRest;

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
    private MovimientoCajaChicaRestClient movCajaChicaRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private EvaluacionLiquidacionRestClient evaluacionLiqRest;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    @Param(name = "liqPk")
    private Long liqPk;

    private Integer paginado = 10;
    private Long totalResultados;
    private String comentLiquidacion = new String();
    private Boolean soloLectura = Boolean.FALSE;
    private LazyLiquidacionDataModel liquidacionLazyModel;
    private Boolean liquidacionGuardada = Boolean.FALSE;
    private Boolean evaluadoMov = Boolean.FALSE;
    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroPresupuestoEscolar filtroPresupuesto = new FiltroPresupuestoEscolar();
    private FiltroMovimientos filtroMov = new FiltroMovimientos();
    private FiltroEvaluacionItemLiquidacion filtroEva = new FiltroEvaluacionItemLiquidacion();

    private SgLiquidacion entidadEnEdicion = new SgLiquidacion();
    private SgMovimientoLiquidacion movLiqEnEdicion = new SgMovimientoLiquidacion();
    private SgMovimientoCuentaBancaria movCuentaBcEnEdicion = new SgMovimientoCuentaBancaria();
    private SgEvaluacionLiquidacion evaluacionLiqEnEdicion = new SgEvaluacionLiquidacion();
    private SgSede sedeSeleccionadaFiltro;
    private SgSede sedeSeleccionada;
    private BigDecimal totalReintegro = BigDecimal.ZERO;
    private BigDecimal totalInvertido = BigDecimal.ZERO;
    private BigDecimal totalDepositado = BigDecimal.ZERO;
    private BigDecimal totalReembolsado = BigDecimal.ZERO;

    private List<SgLiquidacion> historialLiquidacion = new ArrayList();
    private List<SgMovimientos> listMov = new ArrayList();
    private List<Liquidacion> liquidacion = new ArrayList();
    private List<ResumenLiquidacion> listResumen = new ArrayList();
    private List<SgMovimientoCuentaBancaria> ingresosMovs = new ArrayList();
    private List<SgMovimientoLiquidacion> listMovs = new ArrayList();
    private List<SgMovimientoCuentaBancaria> listMovsCB = new ArrayList();
    private List<SgEvaluacionItemLiquidacion> listEvaItems = new ArrayList();
    private List<SgEvaluacionItemLiquidacion> itemsSeleccionados = new ArrayList();

    private SofisComboG<EnumEstadoLiquidacion> comboEstado = new SofisComboG<>();
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SgAnioLectivo> comboAnioLectivo = new SofisComboG<>();
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponenteN = new SofisComboG<>();
    private SofisComboG<SgAnioLectivo> comboAnioLectivoN = new SofisComboG<>();
    private SofisComboG<EnumEvaluacionLiquidacion> estadosLiq = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();


    public LiquidacionBean() {
    }

    @PostConstruct
    public void init() {
        try {

            cargarCombos();
            if (liqPk != null && liqPk > 0) {
                obtenerLiquidacion(liqPk);
                if (entidadEnEdicion.getLiqPk() != null) {
                    if(entidadEnEdicion.getLiqEstado().equals(EnumEstadoLiquidacion.APROBADA)){
                        soloLectura = Boolean.TRUE;
                    }
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
    private void obtenerLiquidacion(Long liqPk) {
        try {
            FiltroLiquidacion filtroLiq = new FiltroLiquidacion();
            filtroLiq.setIncluirCampos(new String[]{
                "liqPk", "liqComponentePk.cpeId", "liqComponentePk.cpeCodigo", "liqComponentePk.cpeNombre", "liqComponentePk.cpeVersion",
                "liqSubComponenteFk.gesId", "liqSubComponenteFk.gesCod", "liqSubComponenteFk.gesNombre", "liqSubComponenteFk.gesVersion",
                "liqSedePk.sedPk", "liqSedePk.sedCodigo", "liqSedePk.sedNombre", "liqSedePk.sedTipo", "liqSedePk.sedVersion",
                "evaluacionLiquidacion.elqPk", "evaluacionLiquidacion.elqReembolsoCheque", "evaluacionLiquidacion.elqReembolsoMonto", "evaluacionLiquidacion.elqReintegroMonto", "evaluacionLiquidacion.elqComentario",
                "evaluacionLiquidacion.elqLiquidacionFk.liqPk", "evaluacionLiquidacion.elqLiquidacionFk.liqVersion", "evaluacionLiquidacion.elqVersion",
                "liqAnioPk.alePk", "liqAnioPk.aleAnio", "liqAnioPk.aleVersion", "liqEstado", "liqVersion"});
            filtroLiq.setLiqPk(liqPk);
            List<SgLiquidacion> list = restClient.buscar(filtroLiq);
            if (list != null && !list.isEmpty() && list.get(0) != null) {
                liquidacionGuardada = Boolean.TRUE;
                entidadEnEdicion = list.get(0);

                if (entidadEnEdicion.getLiqPk() != null) {
                    if (entidadEnEdicion.getEvaluacionLiquidacion() != null) {
                        evaluacionLiqEnEdicion = entidadEnEdicion.getEvaluacionLiquidacion();
                    } else {
                        evaluacionLiqEnEdicion.setElqLiquidacionFk(entidadEnEdicion);
                    }
                }

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Obtener items evaluación liquidación
     */
    private void obtenerItems() {
        try {
            listEvaItems = new ArrayList();
            FiltroCodiguera filtroCod = new FiltroCodiguera();
            filtroCod.setOrderBy(new String[]{"iliCodigo"});
            filtroCod.setAscending(new boolean[]{true});
            List<SgItemLiquidacion> listItems = catalogosRestClient.buscarItemsLiquidacion(filtroCod);

            filtroEva = new FiltroEvaluacionItemLiquidacion();
            filtroEva.setEilLiqFk(entidadEnEdicion.getLiqPk());
            filtroEva.setIncluirCampos(new String[]{
                "eilPk", "eilLiqFk.liqPk", "eilLiqFk.liqVersion", "eilItemFk.iliPk",
                "eilItemFk.iliCodigo", "eilItemFk.iliNombre", "eilItemFk.iliVersion", "eilMarcado", "eilVersion"
            });

            List<SgEvaluacionItemLiquidacion> listEva = evaluacionItemLiqRest.buscar(filtroEva);
            if (!listItems.isEmpty()) {
                listItems.stream().forEach(i -> {
                    if (!listEva.isEmpty()) {
                        SgEvaluacionItemLiquidacion evaluacion = listEva.stream().filter(e -> e.getEilItemFk().equals(i)).findFirst().orElse(null);
                        if (evaluacion != null) {
                            listEvaItems.add(evaluacion);
                        } else {
                            SgEvaluacionItemLiquidacion eva = new SgEvaluacionItemLiquidacion();
                            eva.setEilLiqFk(entidadEnEdicion);
                            eva.setEilItemFk(i);
                            listEvaItems.add(eva);
                        }
                    } else {
                        SgEvaluacionItemLiquidacion eva = new SgEvaluacionItemLiquidacion();
                        eva.setEilLiqFk(entidadEnEdicion);
                        eva.setEilItemFk(i);
                        listEvaItems.add(eva);
                    }
                });
            }

            if (listEvaItems.isEmpty()) {

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de filtros.
     */
    public void cargarCombos() {
        try {
            comboAnioLectivoN = new SofisComboG<>(cargarAnios(), "aleAnio");
            comboAnioLectivoN.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

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
     * Obtiene el título de la página correspondiente al bean
     *
     * @return
     */
    public String getTituloPagina() {
        if (this.liqPk == null) {
            return Etiquetas.getValue("nuevaLiquidacion");
        }
        else if (this.soloLectura) {
            return Etiquetas.getValue("verLiquidacion");
        } else {
            return Etiquetas.getValue("edicionLiquidacion");
        }
    }

    /**
     * Carga el combo de componentes.
     */
    public void cargarComponentes() {
        try {
            comboComponenteN = new SofisComboG<>();
            comboSubComponente = new SofisComboG<>();
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
                                map(m -> m.getMovTechoPresupuestal().getSubComponente().getGesCategoriaComponente()).distinct().collect(Collectors.toList());

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
            if (comboComponenteN.getSelectedT() != null) {
                FiltroGesPresEs filtroSubComponente = new FiltroGesPresEs();
                filtroSubComponente.setOrderBy(new String[]{"gesNombre"});
                filtroSubComponente.setIncluirCampos(new String[]{"gesId", "gesVersion", "gesNombre", "gesCod"});
                filtroSubComponente.setCpeId(comboComponenteN.getSelectedT().getCpeId());
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
     * Limpia los combos usados para filtrar y crear.
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
        filtro = new FiltroCodiguera();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Liquidación.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgLiquidacion();
        entidadEnEdicion.setLiqEstado(EnumEstadoLiquidacion.CONFIRMADA);
        liquidacion = new ArrayList();
    }
    
    /**
     * Verificar si liquidación existe
     */
    public Boolean existeLiquidacion() {
        Boolean result = Boolean.FALSE;
        try {
            FiltroLiquidacion filtroLiq = new FiltroLiquidacion();
            filtroLiq.setIncluirCampos(new String[]{"liqPk","liqVersion"});
            filtroLiq.setAnioFiscal(comboAnioLectivoN.getSelected());
            filtroLiq.setLiqSedePk(sedeSeleccionada.getSedPk());
            filtroLiq.setLiqComponentePk(comboComponenteN.getSelectedT().getCpeId());
            filtroLiq.setLiqSubComponenteFk(comboSubComponente.getSelectedT().getGesId());
            List<SgLiquidacion> list = restClient.buscar(filtroLiq);
            
            if (list != null && !list.isEmpty() && list.get(0) != null) {
                result = Boolean.TRUE;
            }
            
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    /**
     * Obtiene los ingresos según criterios
    */
    public void obtenerIngresos() {
        try {
            
            ingresosMovs = new ArrayList();
            FiltroMovimientoCuentaBancaria filtroMovCB = new FiltroMovimientoCuentaBancaria();
            filtroMovCB.setIncluirCampos(new String[]{
                "mcbPk", "mcbDetalle", "mcbFecha", "mcbMonto", "mcbVersion","mcbTipoMovimiento"
            });
            filtroMovCB.setSubComponente(comboSubComponente.getSelectedT().getGesId());
            filtroMovCB.setSedePk(sedeSeleccionada.getSedPk());
            filtroMovCB.setMcbTipoMovimiento(TipoMovimiento.HABER);
            //filtroMovCB.setMcbFechaDesde(LocalDateTime.of(comboAnioLectivoN.getSelectedT().getAleAnio(), Month.JANUARY,1, 0, 0, 0));
            //filtroMovCB.setMcbFechaHasta(LocalDateTime.of(comboAnioLectivoN.getSelectedT().getAleAnio(), Month.DECEMBER,31, 0, 0, 0));
            ingresosMovs = movCBRestClient.buscar(filtroMovCB);
            if (!ingresosMovs.isEmpty()) {
                ingresosMovs.stream().forEach(m-> {
                    SgMovimientoLiquidacion mov = new SgMovimientoLiquidacion();
                    mov.setMlqMovimientoPk(m);
                    mov.setDetalle(m.getMcbDetalle());
                    mov.setMonto(m.getMcbMonto());
                    mov.setMlqTipoMovimiento(EnumMovimientosTipo.I);
                    listMovs.add(mov);
                });
            }
            
            totalDepositado = listMovs.stream().filter(m-> m.getMlqTipoMovimiento().equals(EnumMovimientosTipo.I)).map(m -> m.getMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * Obtiene los egresos según criterios
    */
    public void obtenerEgresos() {
        try {
            List<SgMovimientoCajaChica> egresosMovCC = new ArrayList();
            List<SgMovimientoCuentaBancaria> egresosMovs = new ArrayList();
            
            FiltroFactura filtroFac = new FiltroFactura();
            filtroFac.setSubcomponente(comboSubComponente.getSelectedT().getGesId());
            filtroFac.setSedePk(sedeSeleccionada.getSedPk());
            filtroFac.setIncluirCampos(new String[]{"facPk",
                "pago.pgsMovimientoCBFk.mcbPk","pago.pgsMovimientoCBFk.mcbDetalle","pago.pgsMovimientoCBFk.mcbFecha",
                "pago.pgsMovimientoCBFk.mcbMonto","pago.pgsMovimientoCBFk.mcbVersion","pago.pgsMovimientoCBFk.mcbTipoMovimiento",
                "pago.pgsMovimientoCBFk.mcbPagoFk.pgsFactura.facNumero",
                "pago.pgsMovimientoCBFk.mcbPagoFk.pgsFactura.facPk",
                "pago.pgsMovimientoCBFk.mcbPagoFk.pgsFactura.facVersion",
                "pago.pgsMovimientoCBFk.mcbPagoFk.pgsFactura.facProveedorFk.proNombre",
                "pago.pgsMovimientoCBFk.mcbPagoFk.pgsNumeroCheque",
                "facVersion"});

            List<SgFactura> listFacturasCB = facturaRestClient.buscar(filtroFac);
            
            
            FiltroFactura filtroFac2 = new FiltroFactura();
            filtroFac2.setSubcomponente(comboSubComponente.getSelectedT().getGesId());
            filtroFac2.setSedePk(sedeSeleccionada.getSedPk());
            filtroFac2.setIncluirCampos(new String[]{"facPk",
                "pago.pgsMovimientoCCFk.mccPk","pago.pgsMovimientoCCFk.mccDetalle","pago.pgsMovimientoCCFk.mccFecha",
                "pago.pgsMovimientoCCFk.mccMonto","pago.pgsMovimientoCCFk.mccVersion","pago.pgsMovimientoCCFk.mccTipoMovimiento",
                "pago.pgsMovimientoCCFk.mccPagoFk.pgsFactura.facNumero",
                "pago.pgsMovimientoCCFk.mccPagoFk.pgsFactura.facPk",
                "pago.pgsMovimientoCCFk.mccPagoFk.pgsFactura.facVersion",
                "pago.pgsMovimientoCCFk.mccPagoFk.pgsFactura.facProveedorFk.proNombre",
                "pago.pgsMovimientoCCFk.mccPagoFk.pgsNumeroCheque",
                "facVersion"});

            List<SgFactura> listFacturasCC = facturaRestClient.buscar(filtroFac2);
            
            if (!listFacturasCB.isEmpty()) {
                egresosMovs = listFacturasCB.stream().filter(f -> f.getPago() != null)
                                                     .filter(f -> f.getPago().getPgsMovimientoCBFk() != null)
                                                     //.filter(f -> f.getPago().getPgsMovimientoCBFk().getMcbFecha().getYear() == comboAnioLectivoN.getSelectedT().getAleAnio().intValue())
                                                     .map(f -> f.getPago().getPgsMovimientoCBFk()).collect(Collectors.toList());
            }
            
            if (!listFacturasCB.isEmpty()) {
                egresosMovCC = listFacturasCC.stream().filter(f -> f.getPago() != null)
                                                      .filter(f -> f.getPago().getPgsMovimientoCCFk() != null)
                                                      //.filter(f -> f.getPago().getPgsMovimientoCCFk().getMccFecha().getYear() == comboAnioLectivoN.getSelectedT().getAleAnio().intValue())
                                                      .map(f -> f.getPago().getPgsMovimientoCCFk()).collect(Collectors.toList());
            }

            if (!egresosMovs.isEmpty()) {
                egresosMovs.stream().forEach(m-> {
                    SgMovimientoLiquidacion mov = new SgMovimientoLiquidacion();
                    mov.setMlqMovimientoPk(m);
                    mov.setDetalle(m.getMcbDetalle());
                    mov.setMonto(m.getMcbMonto());
                    mov.setMlqTipoMovimiento(EnumMovimientosTipo.E);
                    listMovs.add(mov);
                });
            }
            
            if (!egresosMovCC.isEmpty()) {
                egresosMovCC.stream().forEach(m-> {
                    SgMovimientoLiquidacion mov = new SgMovimientoLiquidacion();
                    mov.setMlqMovimientoCcPk(m);
                    mov.setDetalle(m.getMccDetalle());
                    mov.setMonto(m.getMccMonto());
                    mov.setMlqTipoMovimiento(EnumMovimientosTipo.E);
                    listMovs.add(mov);
                });
                //totalInvertido = egresosMovs.stream().map(im -> im.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            
            
            
            totalInvertido = listMovs.stream().filter(m-> m.getMlqTipoMovimiento().equals(EnumMovimientosTipo.E)).map(m -> m.getMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * Obtiene los movimientos de la liquidacion no guardada
     */
    public void obtenerIngresosEgresos() {
        try {
            if(!existeLiquidacion()){
                listMovsCB = new ArrayList();
                if (comboSubComponente.getSelectedT() != null && sedeSeleccionada != null) {
                    
                    obtenerIngresos();
                    if(listMovs.stream().filter(m -> m.getMlqTipoMovimiento().equals(EnumMovimientosTipo.I)).count() == 0L ){
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_LIQUIDACION_INGRESOS_GUARDAR), "");
                    }

                    obtenerEgresos();
                }
            }
            else{
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_LIQUIDACION_EXISTE), "");
            }

            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Obtiene los movimientos de la liquidacion guardada
     */
    public void obtenerMovimientoLiq() {
        try {
            
            if (entidadEnEdicion.getLiqPk() != null) {
                List<SgMovimientoLiquidacion> listMovimientos = new ArrayList();
                listMovs = new ArrayList();
                if(!entidadEnEdicion.getLiqEstado().equals(EnumEstadoLiquidacion.APROBADA)){
                    obtenerIngresos();
                    obtenerEgresos();
                }
                
                FiltroMovimientoLiquidacion filtroMovLiq = new FiltroMovimientoLiquidacion();
                filtroMovLiq.setMlqLiquidacionPk(entidadEnEdicion.getLiqPk());
                filtroMovLiq.setAscending(new boolean[]{false});
                filtroMovLiq.setOrderBy(new String[]{"mlqTipoMovimiento"});
                filtroMovLiq.setIncluirCampos(new String[]{"mlqPk",
                "mlqMovimientoPk.mcbPk","mlqMovimientoPk.mcbFecha",
                "mlqMovimientoPk.mcbDetalle","mlqMovimientoPk.mcbMonto",
                "mlqMovimientoPk.mcbPagoFk.pgsNumeroCheque","mlqMovimientoPk.mcbPagoFk.pgsFactura.facPk",
                "mlqMovimientoPk.mcbPagoFk.pgsFactura.facVersion","mlqMovimientoPk.mcbPagoFk.pgsFactura.facNumero",
                "mlqMovimientoPk.mcbPagoFk.pgsFactura.facProveedorFk.proNombre",
                "mlqMovimientoPk.mcbTipoMovimiento","mlqMovimientoPk.mcbVersion",
                "mlqMovimientoCcPk.mccPk","mlqMovimientoCcPk.mccFecha",
                "mlqMovimientoCcPk.mccDetalle","mlqMovimientoCcPk.mccMonto",
                "mlqMovimientoCcPk.mccPagoFk.pgsNumeroCheque","mlqMovimientoCcPk.mccPagoFk.pgsFactura.facPk",
                "mlqMovimientoCcPk.mccPagoFk.pgsFactura.facVersion","mlqMovimientoCcPk.mccPagoFk.pgsFactura.facNumero",
                "mlqMovimientoCcPk.mccPagoFk.pgsFactura.facProveedorFk.proNombre",
                "mlqMovimientoCcPk.mccTipoMovimiento","mlqMovimientoCcPk.mccVersion",
                "mlqTipoMovimiento","mlqEvaluado","mlqComentario","mlqReintegro",
                "mlqLiquidacionPk.liqPk","mlqLiquidacionPk.liqVersion","mlqVersion"});

                List<SgMovimientoLiquidacion> list = movLiquidacionRest.buscar(filtroMovLiq);
                
                if(!list.isEmpty()){
                    listMovimientos = list.stream().collect(Collectors.toList());
                }

                listMovimientos.stream().forEach(l -> {
                    if (l.getMlqEvaluado() != null) {
                        if (l.getMlqEvaluado().equals(Boolean.TRUE)) {
                            l.setCorrecto(Boolean.TRUE);
                        } else if(l.getMlqEvaluado().equals(Boolean.FALSE)){
                            l.setNoCorrecto(Boolean.TRUE);
                        }
                    }
                    
                    if(l.getMlqMovimientoPk()!=null){
                        l.setDetalle(l.getMlqMovimientoPk().getMcbDetalle());
                        l.setMonto(l.getMlqMovimientoPk().getMcbMonto());
                    }
                    else if(l.getMlqMovimientoCcPk()!=null){
                        l.setDetalle(l.getMlqMovimientoCcPk().getMccDetalle());
                        l.setMonto(l.getMlqMovimientoCcPk().getMccMonto());
                    }
                    
                });
                
                if(!listMovs.isEmpty()){
                    for(SgMovimientoLiquidacion mov : listMovs){
                        if(mov.getMlqMovimientoPk()!=null){
                            Long any = listMovimientos.stream().filter(l-> l.getMlqMovimientoPk()!=null)
                                                               .filter(l->l.getMlqMovimientoPk().getMcbPk().equals(mov.getMlqMovimientoPk().getMcbPk())).count();
                            if(any.equals(0L)){
                                listMovimientos.add(mov);
                            }
                        }
                        else if(mov.getMlqMovimientoCcPk()!=null){
                            Long any1 = listMovimientos.stream().filter(l-> l.getMlqMovimientoCcPk()!=null)
                                                                .filter(l->l.getMlqMovimientoCcPk().getMccPk().equals(mov.getMlqMovimientoCcPk().getMccPk())).count();
                            if(any1.equals(0L)){
                                LOGGER.log(Level.SEVERE,"ENTRA ACA mov " + mov.getMlqMovimientoCcPk().getMccPk());
                                listMovimientos.add(mov);
                            }
                        }
                    }
                }
                
                listMovs = new ArrayList();
                listMovs = listMovimientos.stream().collect(Collectors.toList());
                
                if (!listMovs.isEmpty()) {
                    totalDepositado = BigDecimal.ZERO;
                    totalInvertido = BigDecimal.ZERO;
                    totalReintegro  = BigDecimal.ZERO;
                    
                    totalDepositado = listMovs.stream().filter(l->l.getMlqTipoMovimiento().equals(EnumMovimientosTipo.I)).filter(l -> l.getMonto()!=null).map(l -> l.getMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    totalReintegro = listMovs.stream().filter(l-> l.getMlqReintegro()!=null).filter(l -> l.getMlqTipoMovimiento().equals(EnumMovimientosTipo.E)).map(l-> l.getMlqReintegro()).reduce(BigDecimal.ZERO, BigDecimal::add);
                    totalInvertido = listMovs.stream().filter(l->l.getMlqTipoMovimiento().equals(EnumMovimientosTipo.E)).filter(l -> l.getMonto()!=null).map(l-> l.getMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
                        
                }
                
                cuadroResumen();
                
            }

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
            if (!listMovs.isEmpty()) {
                if (totalDepositado != null && totalDepositado!= null) {
                    saldo = totalDepositado.subtract(totalInvertido);
                } else if (totalDepositado != null && totalInvertido == null) {
                    saldo = totalDepositado;
                } else if (totalDepositado == null && totalInvertido != null) {
                    saldo = BigDecimal.ZERO.subtract(totalInvertido);
                }
            }
            return saldo;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return saldo;
    }

    /**
     * Llenado de cuadro resumen
     */
    private void cuadroResumen() {
        
        try {
            listResumen = new ArrayList();
            if(evaluacionLiqEnEdicion!=null){
                if(evaluacionLiqEnEdicion.getElqReembolsoMonto()!=null){
                    totalReembolsado = evaluacionLiqEnEdicion.getElqReembolsoMonto();
                }
            }
            
            ResumenLiquidacion resumen = new ResumenLiquidacion("MONTO DEPOSITADO",totalDepositado);
            ResumenLiquidacion resumen1 = new ResumenLiquidacion("MONTO INVERTIDO",totalInvertido);
            ResumenLiquidacion resumen2 = new ResumenLiquidacion("MONTO REEMBOLSADO",totalReembolsado);
            ResumenLiquidacion resumen3 = new ResumenLiquidacion("MONTO REINTEGRADO",totalReintegro);
            ResumenLiquidacion resumen4 = new ResumenLiquidacion("SALDO",obtenerSaldo());

            listResumen.add(resumen);
            listResumen.add(resumen1);
            listResumen.add(resumen2);
            listResumen.add(resumen3);
            listResumen.add(resumen4);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * Cambia el estado movimiento liquidación
     */
    public void cambiarEstadoMov(SgMovimientoLiquidacion var) {
        try {
            estadosLiq.setSelected(-1);
            SgMovimientoLiquidacion mov = (SgMovimientoLiquidacion) SerializationUtils.clone(var);
            if (var!=null) {
                movLiqEnEdicion = mov;
                if (movLiqEnEdicion.getMlqEvaluado() != null) {
                    estadosLiq.setSelectedT(movLiqEnEdicion.getMlqEvaluado() == Boolean.TRUE ? EnumEvaluacionLiquidacion.CORRECTO : EnumEvaluacionLiquidacion.NO_CORRECTO);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, "No se pudo evaluar el movimiento", "");
        }
    }

    /**
     * Cambia el estado movimiento liquidación
     */
    public void correctoMovimiento(SgMovimientoLiquidacion var) {
        try {
            SgMovimientoLiquidacion mov = (SgMovimientoLiquidacion) SerializationUtils.clone(var);
            listMovs.stream().filter(m -> m.getMlqPk().equals(mov.getMlqMovimientoPk())).forEach(m -> {
                    m.setCorrecto(Boolean.TRUE);
                    m.setNoCorrecto(Boolean.FALSE);
                });
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Cambia el estado movimiento liquidación
     */
    public void noCorrectoMovimiento(SgMovimientoLiquidacion var) {
        try {
            SgMovimientoLiquidacion mov = (SgMovimientoLiquidacion) SerializationUtils.clone(var);
            listMovs.stream().filter(m -> m.getMlqPk().equals(mov.getMlqPk())).forEach(m -> {
                m.setCorrecto(Boolean.FALSE);
                m.setNoCorrecto(Boolean.TRUE);
            });
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
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
            Long itemMarcados = 0L;
               
            totalMovs = listMovs.stream().count();
            nocorrectos = listMovs.stream().filter(m -> m.getMlqEvaluado() != null).filter(m -> m.getMlqEvaluado().equals(Boolean.FALSE)).count();
            correctos = listMovs.stream().filter(m -> m.getMlqEvaluado() != null).filter(m -> m.getMlqEvaluado().equals(Boolean.TRUE)).count();
            itemMarcados = listEvaItems.stream().filter(l->l.getEilMarcado().equals(Boolean.TRUE)).count();

            if (nocorrectos > 0L || itemMarcados >0) {
                if (!entidadEnEdicion.getLiqEstado().equals(EnumEstadoLiquidacion.EVALUADA)) {
                    entidadEnEdicion.setLiqEstado(EnumEstadoLiquidacion.EVALUADA);
                    restClient.guardar(entidadEnEdicion);
                    obtenerLiquidacion(entidadEnEdicion.getLiqPk());
                }
            } else if (correctos < totalMovs || itemMarcados>0L) {
                if (!entidadEnEdicion.getLiqEstado().equals(EnumEstadoLiquidacion.EVALUADA)) {
                    entidadEnEdicion.setLiqEstado(EnumEstadoLiquidacion.EVALUADA);
                    restClient.guardar(entidadEnEdicion);
                    obtenerLiquidacion(entidadEnEdicion.getLiqPk());
                }
            } else if (totalMovs.equals(correctos) && itemMarcados==0L) {
                if (!entidadEnEdicion.getLiqEstado().equals(EnumEstadoLiquidacion.APROBADA)) {
                    entidadEnEdicion.setLiqEstado(EnumEstadoLiquidacion.APROBADA);
                    restClient.guardar(entidadEnEdicion);
                    obtenerLiquidacion(entidadEnEdicion.getLiqPk());
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Crea o actualiza un registro de liquidación.
     *
     *
     */
    public void actualizar() {
        JSFUtils.limpiarMensajesError();

        listMovs = new ArrayList();
        sedeSeleccionada = entidadEnEdicion.getLiqSedePk();
        comboAnioLectivoN = new SofisComboG<>(cargarAnios(), "aleAnio");
        comboAnioLectivoN.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboAnioLectivoN.setSelectedT(entidadEnEdicion.getLiqAnioPk());

        cargarComponentes();
        comboComponenteN.setSelectedT(entidadEnEdicion.getLiqComponentePk());
        cargarSubComponente();
        comboSubComponente.setSelectedT(entidadEnEdicion.getLiqSubComponenteFk());
        if (entidadEnEdicion.getLiqPk() != null) {
            obtenerMovimientoLiq();
            obtenerItems();
        } else {
            obtenerIngresosEgresos();
        }
        if(entidadEnEdicion.getLiqEstado().equals(EnumEstadoLiquidacion.APROBADA)){
            soloLectura = Boolean.TRUE;
        }
    }

    /**
     * Pre Evaluación de Movimiento
     */
    public void evaluarMovimientosLiquidacion(){
        try {
            
            if (listMovs != null && !listMovs.isEmpty()) {
                for (SgMovimientoLiquidacion mov : listMovs) {
                    if (mov != null) {
                        movLiqEnEdicion = mov;
                        if (movLiqEnEdicion.getCorrecto().equals(Boolean.TRUE)) {
                            movLiqEnEdicion.setMlqEvaluado(Boolean.TRUE);
                        }
                        else if (movLiqEnEdicion.getNoCorrecto().equals(Boolean.TRUE)) {
                            movLiqEnEdicion.setMlqEvaluado(Boolean.FALSE);
                        }
                        
                        if(mov.getMlqPk()==null){
                            mov.setMlqLiquidacionPk(entidadEnEdicion);
                        }
                        
                        movLiquidacionRest.guardar(movLiqEnEdicion);
                    }
                }
            }

            obtenerMovimientoLiq();
            cambiarEstadoLiquidacion();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        }
        catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de movimientos de cuenta bancaria.
     */
    public void evaluarMovLiquidacion() {
        try {

            if (StringUtils.isBlank(movLiqEnEdicion.getMlqComentario())) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "Campo motivo no puede ser vacío. ", "");
                return;
            } else {
                movLiqEnEdicion.setMlqEvaluado(Boolean.FALSE);
            }

            movLiquidacionRest.guardar(movLiqEnEdicion);
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
    public String labelEvaluadoMov(SgMovimientoLiquidacion var) {
        String label = new String();
        try {

            SgMovimientoLiquidacion mov = (SgMovimientoLiquidacion) SerializationUtils.clone(var);
            if(mov!=null){
                if (mov.getMlqEvaluado() != null) {
                    if (mov.getMlqEvaluado().equals(Boolean.TRUE)) {
                        label = "EVALUADO - CORRECTO";
                    } else {
                        label = "EVALUADO - NO CORRECTO";
                    }
                } else {
                    label = "NO EVALUADO";
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
     * Label evaluado movimiento
     */
    public String labelDetalleMov(SgMovimientoLiquidacion var) {
        String label = new String();
        try {
            String proveedor = StringUtils.EMPTY;
            String cheque = StringUtils.EMPTY;
            String facNum = StringUtils.EMPTY;
            SgMovimientoLiquidacion mov = (SgMovimientoLiquidacion) SerializationUtils.clone(var);
            if(mov.getMlqMovimientoPk()!=null){
                SgMovimientoCuentaBancaria cuenta = mov.getMlqMovimientoPk();
                if(cuenta.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE)){
                    if(cuenta.getMcbPagoFk()!=null){
                        cheque = cuenta.getMcbPagoFk().getPgsNumeroCheque()!=null ? cuenta.getMcbPagoFk().getPgsNumeroCheque().toString() : StringUtils.EMPTY;

                        if(cuenta.getMcbPagoFk().getPgsFactura()!=null && cuenta.getMcbPagoFk().getPgsFactura().getFacNumero()!=null){
                            facNum = cuenta.getMcbPagoFk().getPgsFactura().getFacNumero();
                        }

                        if(cuenta.getMcbPagoFk().getPgsFactura()!=null){
                            proveedor = cuenta.getMcbPagoFk().getPgsFactura().getFacProveedorFk()!=null ? cuenta.getMcbPagoFk().getPgsFactura().getFacProveedorFk().getProNombre() : StringUtils.EMPTY;
                        }
                        label = ConstantesAyuda.CHEQUE.concat(" # ").concat(cheque).concat(" ").concat(ConstantesAyuda.FACTURA).concat(" # ").concat(facNum).concat(" ").concat(ConstantesAyuda.PROVEEDOR).concat(": ").concat(proveedor);
                    }
                }
                else{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    label = cuenta.getMcbFecha().format(formatter).concat(" - ").concat(cuenta.getMcbDetalle());
                }
            }
            else if(mov.getMlqMovimientoCcPk()!=null){
                SgMovimientoCajaChica movcc = mov.getMlqMovimientoCcPk();
                if(movcc.getMccTipoMovimiento().equals(TipoMovimiento.DEBE)){
                    if(movcc.getMccPagoFk()!=null){

                        if(movcc.getMccPagoFk().getPgsFactura()!=null && movcc.getMccPagoFk().getPgsFactura().getFacNumero()!=null){
                            facNum = movcc.getMccPagoFk().getPgsFactura().getFacNumero();
                        }

                        if(movcc.getMccPagoFk().getPgsFactura()!=null){
                            proveedor = movcc.getMccPagoFk().getPgsFactura().getFacProveedorFk()!=null ? movcc.getMccPagoFk().getPgsFactura().getFacProveedorFk().getProNombre() : StringUtils.EMPTY;
                        }
                        label = ConstantesAyuda.FACTURA.concat(" # ").concat(facNum).concat(" ").concat(ConstantesAyuda.PROVEEDOR).concat(": ").concat(proveedor);
                    }
                }
                else{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    label = movcc.getMccFecha().format(formatter).concat(" - ").concat(movcc.getMccDetalle());
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
     * Style columna de evaluación movimiento liquidación
     */
    public String estiloColumnaDetalleMov(SgMovimientoLiquidacion var) {
        String label = new String();
        try {
            SgMovimientoLiquidacion mov = (SgMovimientoLiquidacion) SerializationUtils.clone(var);
            if(mov!=null){
                if(mov.getMlqEvaluado()==null){
                    label = "columnaRoja";
                }
                else{
                    if(mov.getMlqEvaluado().equals(Boolean.FALSE)){
                        label = "columnaAmarilla";
                    }
                    else if(mov.getMlqEvaluado().equals(Boolean.TRUE)){
                        label = "columnaVerde";
                    }
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
     * Crea o actualiza un registro de movimientos de liquidación.
     */
    public void guardar() {
        try {
            if (comboAnioLectivoN.getSelectedT() != null && sedeSeleccionada != null && comboComponenteN.getSelectedT() != null
                    && comboSubComponente.getSelectedT() != null) {
                if (!ingresosMovs.isEmpty()) {
                    entidadEnEdicion.setLiqAnioPk(comboAnioLectivoN.getSelectedT());
                    entidadEnEdicion.setLiqSedePk(sedeSeleccionada);
                    entidadEnEdicion.setLiqComponentePk(comboComponenteN.getSelectedT());
                    entidadEnEdicion.setLiqSubComponenteFk(comboSubComponente.getSelectedT());
                    entidadEnEdicion.setMovimientosLiquidacion(listMovs);
                    entidadEnEdicion = restClient.guardarConMovimientos(entidadEnEdicion);
                    obtenerLiquidacion(entidadEnEdicion.getLiqPk());
                    if(entidadEnEdicion.getLiqEstado().equals(EnumEstadoLiquidacion.APROBADA)){
                        soloLectura = Boolean.TRUE;
                    }
                    obtenerMovimientoLiq();
                    obtenerItems();
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
     * Elimina un registro de movimientos de liquidación.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getLiqPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de movimientos de cuenta bancaria.
     */
    public void evaluarLiquidacion() {
        try {
            for (SgEvaluacionItemLiquidacion eva : listEvaItems) {
                evaluacionItemLiqRest.guardar(eva);
            }
            evaluacionLiqEnEdicion.setElqReintegroMonto(totalReintegro);
            evaluacionLiqEnEdicion = evaluacionLiqRest.guardar(evaluacionLiqEnEdicion);

            obtenerItems();
            cuadroResumen();
            cambiarEstadoLiquidacion();
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
     * Muestra los datos de auditoría.
     *
     * @param estId Long: Id del registro
     * @param estRev Long: Revisión del registro
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
    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
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

    public Boolean getLiquidacionGuardada() {
        return liquidacionGuardada;
    }

    public void setLiquidacionGuardada(Boolean liquidacionGuardada) {
        this.liquidacionGuardada = liquidacionGuardada;
    }

    public SofisComboG<EnumEvaluacionLiquidacion> getEstadosLiq() {
        return estadosLiq;
    }

    public void setEstadosLiq(SofisComboG<EnumEvaluacionLiquidacion> estadosLiq) {
        this.estadosLiq = estadosLiq;
    }

    public SgMovimientoLiquidacion getMovLiqEnEdicion() {
        return movLiqEnEdicion;
    }

    public void setMovLiqEnEdicion(SgMovimientoLiquidacion movLiqEnEdicion) {
        this.movLiqEnEdicion = movLiqEnEdicion;
    }

    public String getComentLiquidacion() {
        return comentLiquidacion;
    }

    public void setComentLiquidacion(String comentLiquidacion) {
        this.comentLiquidacion = comentLiquidacion;
    }

    public SgMovimientoCuentaBancaria getMovCuentaBcEnEdicion() {
        return movCuentaBcEnEdicion;
    }

    public void setMovCuentaBcEnEdicion(SgMovimientoCuentaBancaria movCuentaBcEnEdicion) {
        this.movCuentaBcEnEdicion = movCuentaBcEnEdicion;
    }

    public SofisComboG<SsGesPresEs> getComboSubComponente() {
        return comboSubComponente;
    }

    public void setComboSubComponente(SofisComboG<SsGesPresEs> comboSubComponente) {
        this.comboSubComponente = comboSubComponente;
    }

    public SgEvaluacionLiquidacion getEvaluacionLiqEnEdicion() {
        return evaluacionLiqEnEdicion;
    }

    public void setEvaluacionLiqEnEdicion(SgEvaluacionLiquidacion evaluacionLiqEnEdicion) {
        this.evaluacionLiqEnEdicion = evaluacionLiqEnEdicion;
    }

    public Boolean getEvaluadoMov() {
        return evaluadoMov;
    }

    public void setEvaluadoMov(Boolean evaluadoMov) {
        this.evaluadoMov = evaluadoMov;
    }

    public List<SgEvaluacionItemLiquidacion> getListEvaItems() {
        return listEvaItems;
    }

    public void setListEvaItems(List<SgEvaluacionItemLiquidacion> listEvaItems) {
        this.listEvaItems = listEvaItems;
    }

    public List<SgEvaluacionItemLiquidacion> getItemsSeleccionados() {
        return itemsSeleccionados;
    }

    public void setItemsSeleccionados(List<SgEvaluacionItemLiquidacion> itemsSeleccionados) {
        this.itemsSeleccionados = itemsSeleccionados;
    }
    
    public BigDecimal getTotalReintegro() {
        return totalReintegro;
    }

    public void setTotalReintegro(BigDecimal totalReintegro) {
        this.totalReintegro = totalReintegro;
    }
    
    public List<SgMovimientoCuentaBancaria> getListMovsCB() {
        return listMovsCB;
    }

    public void setListMovsCB(List<SgMovimientoCuentaBancaria> listMovsCB) {
        this.listMovsCB = listMovsCB;
    }

    public BigDecimal getTotalInvertido() {
        return totalInvertido;
    }

    public void setTotalInvertido(BigDecimal totalInvertido) {
        this.totalInvertido = totalInvertido;
    }

    public BigDecimal getTotalDepositado() {
        return totalDepositado;
    }

    public void setTotalDepositado(BigDecimal totalDepositado) {
        this.totalDepositado = totalDepositado;
    }
    
    public List<ResumenLiquidacion> getListResumen() {
        return listResumen;
    }

    public void setListResumen(List<ResumenLiquidacion> listResumen) {
        this.listResumen = listResumen;
    }
    
    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }
    
    public List<SgMovimientoLiquidacion> getListMovs() {
        return listMovs;
    }

    public void setListMovs(List<SgMovimientoLiquidacion> listMovs) {
        this.listMovs = listMovs;
    }
    
    // </editor-fold>

    
}
