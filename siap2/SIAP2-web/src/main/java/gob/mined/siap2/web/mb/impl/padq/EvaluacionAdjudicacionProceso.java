/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.padq;

import gob.mined.siap2.business.utils.UtilsUACI;
import gob.mined.siap2.datatype.DataOfertaInsumo;
import gob.mined.siap2.entities.constantes.ConstantesConfiguracion;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItemOfertaInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItemProvOferta;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionLote;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionProveedor;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.enums.EstadoItem;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.datatypes.DataInsumoOferta;
import gob.mined.siap2.web.datatypes.DataLoteItem;
import gob.mined.siap2.web.delegates.ConfiguracionDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Esta clase implementa un backing bean de evaluación del proceso de
 * adjudicación.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "evaluacionAdjudicacionProceso")
public class EvaluacionAdjudicacionProceso implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    private JSFUtils jSFUtils;

    @Inject
    private ProcesoAdquisicionDelegate pAdqDelegate;

    @Inject
    private EntityManagementDelegate emd;

    @Inject
    TextMB textMB;
    @Inject
    private ConfiguracionDelegate confDelegate;
    @Inject
    private ReporteDelegate reporteDelegate;

    private ProcesoAdqMain mainBean;
    private Integer procesoAdquisicionId;
    private TreeNode rootArbol;
    private TreeNode[] selectedNode;
    private List<ProcesoAdquisicionItemProvOferta> ofertas = new ArrayList<>();
    private ProcesoAdquisicionItemProvOferta selectOferta;
    private SofisComboG<Proveedor> comboProveedores = new SofisComboG<Proveedor>();
    private ProcesoAdquisicionItem itemSeleccionado;
    private boolean adviertoPrecioProveedor = false;
    private List<ProcesoAdquisicionItem> items;
    private List<ProcesoAdquisicionLote> lotes;
    private Integer diasPlazoPausa;
    private boolean editarProveedor;
    private ProcesoAdquisicionItemProvOferta ofertaEditar;
    private Integer idItemSelect = null;
    private Integer cantNroReserva;
    private boolean editCantidad = false;
    private List<DataOfertaInsumo> ofertasInsumoProveedor = new ArrayList<>();
    private Date fechaPlazo;
    private BigDecimal precioUnitProveedor;
    private String nrosReservados;
    private List<DataInsumoOferta> insumosAEditar;

    //Este atributo se utiliza para obtener el item relacionado con un insumo al cual se le va a editar la cantidad
    private ProcesoAdquisicionItem itemInsumoAEditar;
    private BigDecimal precioTotalProveedor = BigDecimal.ZERO;
    private RelacionProAdqItemInsumo relacionItemInsumoAEditar;

    @PostConstruct
    public void init() {
        try {
            mainBean = (ProcesoAdqMain) JSFUtils.getBean("procesoAdqMain");
            procesoAdquisicionId = mainBean.getObjeto().getId();

            Configuracion conf = confDelegate.obtenerCnfPorCodigo(ConstantesConfiguracion.PLAZO_PAUZA_ITEM);
            if (conf == null) {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERROR_PLAZO_PAUZA_ITEM_NO_EXISTE);
                throw be;
            }
            diasPlazoPausa = Integer.valueOf(conf.getCnfValor());

            cargarArbol();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método determina si ya hay números reservados
     *
     * @return
     */
    public boolean getYaReservoNumeros() {
        if (mainBean.getObjeto().getReservaNroContratoFinal() != null && mainBean.getObjeto().getReservaNroContratoFinal() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Este método realiza la reserva de números.
     */
    public void initReservaNumeros() {
        if (getYaReservoNumeros()) {
            Integer nroInicial = mainBean.getObjeto().getReservaNroContratoInicial();
            Integer nroFinal = mainBean.getObjeto().getReservaNroContratoFinal();
            cantNroReserva = 0;
            for (Integer i = nroInicial; i <= nroFinal; i++) {
                if (i.equals(nroInicial)) {
                    nrosReservados = "" + i;
                } else {
                    nrosReservados += " ," + i;
                }
                cantNroReserva++;

            }
        }
    }

    /**
     * Este método realiza la carga del árbol de lotes, ítems, insumos
     */
    public void cargarArbol() {

        rootArbol = new DefaultTreeNode(new DataLoteItem(DataLoteItem.ROOT, null), null);
        items = mainBean.getObjeto().getItems(); 
        lotes = mainBean.getObjeto().getLotes();
        for (ProcesoAdquisicionLote lote : lotes) {
            DataLoteItem dtlLote = new DataLoteItem(DataLoteItem.LOTE, lote);
            dtlLote.setNombre(lote.getNombre());
            TreeNode nodoLote = new DefaultTreeNode(dtlLote, rootArbol);
            nodoLote.setSelectable(false);
            for (ProcesoAdquisicionItem item : lote.getItems()) {
                DataLoteItem dtlItem = new DataLoteItem(DataLoteItem.ITEM, item);
                TreeNode nodoItem = new DefaultTreeNode(dtlItem, nodoLote);
                if (idItemSelect != null && item.getId().equals(idItemSelect)) {
                    nodoLote.setExpanded(true);
                    nodoItem.setSelected(true);
                    nodoItem.setExpanded(true);
                    itemSeleccionado = item;
                }
                String estado = null;

                if (item.getEstado() != null) {
                    EstadoItem estadoItem = item.getEstado();
                    if (estadoItem.equals(EstadoItem.ADJUDICADO)) {
                        estado = "ADJUDICADO";
                    }
                    if (estadoItem.equals(EstadoItem.DESIERTO)) {
                        estado = "DESIERTO";
                    }
                    if (estadoItem.equals(EstadoItem.PAUSA)) {
                        estado = "PAUSA";
                        dtlItem.setEstaEnPausa(true);

                        Date fechaPausa = item.getFechaPausa();
                        Date fechaActual = new Date();
                        long diferenciaEn_ms = fechaActual.getTime() - fechaPausa.getTime();
                        long diferenciaDias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
                        Integer diasPausa = (int) diferenciaDias;
                        if (diasPlazoPausa <= diasPausa) {
                            dtlItem.setEditar(true);

                        }

                    }
                    if (estadoItem.equals(EstadoItem.SIN_EFECTO)) {
                        estado = "SIN_EFECTO";
                    }
                }
                if (estado != null) {
                    dtlItem.setNombre(item.getNombre() + " - " + estado);

                } else {
                    dtlItem.setNombre(item.getNombre());
                }
                nodoItem.setSelectable(true);
                Integer CantidadItem = 0;
                BigDecimal montoTotalEstimadoItem = BigDecimal.ZERO;
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición

                for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                    ProcesoAdquisicionInsumo insumo = rel.getInsumo();
                    DataLoteItem dtlInsumo = new DataLoteItem(DataLoteItem.INSUMO, insumo);
                    dtlInsumo.setuTecnica(insumo.getUnidadTecnica().getNombre());
                    dtlInsumo.setInsumo(insumo.getInsumo().getNombre());
                    dtlInsumo.setCodigoInsumo(insumo.getPoInsumo().getId());
                    //La cantidad de cada insumo se ontiene de la relación con su insumo
                    dtlInsumo.setRelacionInsumoItem(rel);
                    Integer cantidadInsumoParaItem = rel.getCantidad();
                    dtlInsumo.setCantidadItemInsumo(cantidadInsumoParaItem);
                    BigDecimal montoTotalEstimadoParaInsumoDelItem = insumo.getPoInsumo().getMontoUnit().multiply(new BigDecimal(cantidadInsumoParaItem));
                    dtlInsumo.setMontoTotalEstimado(montoTotalEstimadoParaInsumoDelItem);
                    dtlInsumo.setPrecioUnitEstimado(insumo.getPoInsumo().getMontoUnit());
                    dtlInsumo.setMontoTotalCertificado(insumo.getPoInsumo().getMontoTotalCertificado());
                    TreeNode nodoInsumo = new DefaultTreeNode(dtlInsumo, nodoItem);
                    nodoInsumo.setSelectable(false);
                    CantidadItem += cantidadInsumoParaItem;
                    /*Multiplico la cantidad usada del insumo por el monto estimado unitario*/
                    montoTotalEstimadoItem = montoTotalEstimadoItem.add(montoTotalEstimadoParaInsumoDelItem);
                }
                dtlItem.setCantidadItemInsumo(CantidadItem);
                dtlItem.setMontoTotalEstimado(montoTotalEstimadoItem);

            }
        }

        for (ProcesoAdquisicionItem item : items) {
            DataLoteItem dtl = new DataLoteItem(DataLoteItem.ITEM, item);

            String estado = null;
            if (item.getEstado() != null) {
                EstadoItem estadoItem = item.getEstado();
                if (estadoItem.equals(EstadoItem.ADJUDICADO)) {
                    estado = "ADJUDICADO";
                }
                if (estadoItem.equals(EstadoItem.DESIERTO)) {
                    estado = "DESIERTO";
                }
                if (estadoItem.equals(EstadoItem.PAUSA)) {
                    estado = "PAUSA";
                    dtl.setEstaEnPausa(true);
                }
                if (estadoItem.equals(EstadoItem.SIN_EFECTO)) {
                    estado = "SIN_EFECTO";
                }
            }

            if (estado != null) {
                dtl.setNombre(item.getNombre() + " - " + estado);
            } else {
                dtl.setNombre(item.getNombre());
            }

            TreeNode nodoItem = new DefaultTreeNode(dtl, rootArbol);
            if (idItemSelect != null && item.getId().equals(idItemSelect)) {
                nodoItem.setSelected(true);
                nodoItem.setExpanded(true);
                itemSeleccionado = item;
            }
            nodoItem.setSelectable(true);
            Integer CantidadItem = 0;
            BigDecimal montoTotalEstimadoItem = BigDecimal.ZERO;
            //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                ProcesoAdquisicionInsumo insumo = rel.getInsumo();
                DataLoteItem dtlInsumo = new DataLoteItem(DataLoteItem.INSUMO, insumo);

                dtlInsumo.setuTecnica(insumo.getUnidadTecnica().getNombre());
                dtlInsumo.setInsumo(insumo.getInsumo().getNombre());
                dtlInsumo.setCodigoInsumo(insumo.getPoInsumo().getId());
                dtlInsumo.setRelacionInsumoItem(insumo.getRelacionItemInsumoPorItem(item));
                //La cantidad de cada insumo se ontiene de la relación con su insumo
                Integer cantidadInsumoParaItem = rel.getCantidad();
                dtlInsumo.setCantidadItemInsumo(cantidadInsumoParaItem);
                BigDecimal montoTotalEstimadoParaInsumoDelItem = insumo.getPoInsumo().getMontoUnit().multiply(new BigDecimal(cantidadInsumoParaItem));
                dtlInsumo.setMontoTotalEstimado(montoTotalEstimadoParaInsumoDelItem);
                dtlInsumo.setPrecioUnitEstimado(insumo.getPoInsumo().getMontoUnit());
                dtlInsumo.setMontoTotalCertificado(insumo.getPoInsumo().getMontoTotalCertificado());
                TreeNode nodoInsumo = new DefaultTreeNode(dtlInsumo, nodoItem);
                nodoInsumo.setSelectable(false);
                CantidadItem += cantidadInsumoParaItem;
                montoTotalEstimadoItem = montoTotalEstimadoItem.add(montoTotalEstimadoParaInsumoDelItem);
            }
            dtl.setCantidadItemInsumo(CantidadItem);
            dtl.setMontoTotalEstimado(montoTotalEstimadoItem);
        }
    }

    /**
     * Este método permite obtener los proveedores del proceso.
     *
     * @return
     */
    private List<Proveedor> obtenerProveedoresDelProceso() {
        List<Proveedor> res = new LinkedList<>();
        if (mainBean.getObjeto().getProveedores() != null) {
            for (ProcesoAdquisicionProveedor pa : mainBean.getObjeto().getProveedores()) {
                res.add(pa.getProveedor());
            }
        }
        return res;
    }

    /**
     * Este método habilita la edición de una oferta.
     *
     * @param oferta
     * @param verOferta
     */
    public void verEditarOferta(ProcesoAdquisicionItemProvOferta oferta, boolean verOferta) {
        List<Proveedor> proveedores = pAdqDelegate.obtenerProveedoresDelProceso(procesoAdquisicionId);
        comboProveedores = new SofisComboG<>(proveedores, "nombreComercial");
        comboProveedores.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        setOfertaEditar(oferta);
        ofertasInsumoProveedor = new ArrayList<>();
        precioUnitProveedor = oferta.getPrecioUnitOferta();
        ProcesoAdquisicionItem item = oferta.getProcesoAdquisicionItem();
        Integer contador = 1;
        precioTotalProveedor = BigDecimal.ZERO;
        for (ProcesoAdquisicionItemOfertaInsumo ofertaInsumo : getOfertaEditar().getOfertasPorInsumo()) {
            DataOfertaInsumo dataOfertaInsumo = new DataOfertaInsumo();
            dataOfertaInsumo.setIndex(contador);
            dataOfertaInsumo.setProcesoInsumo(ofertaInsumo.getProcesoAdqInsumo());
            dataOfertaInsumo.setOfetaInsumo(ofertaInsumo);
            dataOfertaInsumo.setMontoOfertaInsumo(ofertaInsumo.getMontoOferta());
            dataOfertaInsumo.setCantidadOfertaInsumo(ofertaInsumo.getCantidadOferta());
            dataOfertaInsumo.setEditCantidad(false);
            BigDecimal montoTotalCertificado = dataOfertaInsumo.getProcesoInsumo().getPoInsumo().getMontoTotalCertificado();
            if (dataOfertaInsumo.getMontoOfertaInsumo().compareTo(montoTotalCertificado) > 0) {
                dataOfertaInsumo.setAlcanzaAPagarOferta(false);
            } else {
                dataOfertaInsumo.setAlcanzaAPagarOferta(true);
            }
            //La cantidad se obtiene de la relación del insumo con el item seleccionado
            Integer cantidadInsumoParaItem = dataOfertaInsumo.getProcesoInsumo().getRelacionItemInsumoPorItem(item).getCantidad();
            if (dataOfertaInsumo.getAlcanzaAPagarOferta() && dataOfertaInsumo.getCantidadOfertaInsumo().compareTo(cantidadInsumoParaItem) != 0) {
                dataOfertaInsumo.setAlcanzaAPagarOferta(false);
            }
            BigDecimal montoOfertado = ofertaInsumo.getMontoOferta();
            if (montoTotalCertificado.compareTo(montoOfertado) < 0 && item.getEstado() != null) {

                EstadoItem estadoItem = item.getEstado();
                if (estadoItem.equals(EstadoItem.PAUSA)) {
                    Date fechaPausa = item.getFechaPausa();
                    Date fechaActual = new Date();
                    long diferenciaEn_ms = fechaActual.getTime() - fechaPausa.getTime();
                    long diferenciaDias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
                    Integer diasPausa = (int) diferenciaDias;
                    if (diasPlazoPausa <= diasPausa) {
                        dataOfertaInsumo.setEditCantidad(true);
                        editCantidad = true;
                    }
                }
            }
            precioTotalProveedor = precioTotalProveedor.add(ofertaInsumo.getMontoOferta());
            ofertasInsumoProveedor.add(dataOfertaInsumo);
            contador++;
        }

        adviertoPrecioProveedor = false;

        comboProveedores.setSelectedT(getOfertaEditar().getProcesoAdquisicionProveedor().getProveedor());
        if (verOferta) {
            RequestContext.getCurrentInstance().execute("$('#verProveedor').modal('show');");
        } else {
            RequestContext.getCurrentInstance().execute("$('#anadirProveedor').modal('show');");
        }

    }

    /**
     * Este método permite editar un compromiso presupuestario.
     */
    public void editarCompromisoPresupuestario() {
        try {
            if (selectOferta == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_PROVEEDOR);
                throw b;
            }

            setOfertaEditar(selectOferta);
            ProcesoAdquisicionItem item = getOfertaEditar().getProcesoAdquisicionItem();

            List<ProcesoAdquisicionItemOfertaInsumo> ofertasInsumos = selectOferta.getOfertasPorInsumo();
            boolean hayUnMontoMayorCertificado = false;
            for (ProcesoAdquisicionItemOfertaInsumo ofertaInsumo : ofertasInsumos) {
                if (!esMontoMenorACertificadoRestante(item, ofertaInsumo.getProcesoAdqInsumo(), ofertaInsumo.getMontoOferta())) {
                    hayUnMontoMayorCertificado = true;
                    break;
                }
            }

            if (!hayUnMontoMayorCertificado) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_ALCANZA_PRESUPUESTO_ASIGNADO);
                throw b;
            }

            List<Proveedor> proveedores = pAdqDelegate.obtenerProveedoresDelProceso(procesoAdquisicionId);
            comboProveedores = new SofisComboG<>(proveedores, "nombreComercial");
            comboProveedores.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            ofertasInsumoProveedor = new ArrayList<>();
            precioUnitProveedor = getOfertaEditar().getPrecioUnitOferta();

            Integer cantidadTotalInsumosDelItem = 0;

            Integer contador = 1;
            for (ProcesoAdquisicionItemOfertaInsumo ofertaInsumo : getOfertaEditar().getOfertasPorInsumo()) {
                DataOfertaInsumo dataOfertaInsumo = new DataOfertaInsumo();
                dataOfertaInsumo.setIndex(contador);
                dataOfertaInsumo.setProcesoInsumo(ofertaInsumo.getProcesoAdqInsumo());
                dataOfertaInsumo.setOfetaInsumo(ofertaInsumo);
                dataOfertaInsumo.setMontoOfertaInsumo(ofertaInsumo.getMontoOferta());
                dataOfertaInsumo.setCantidadOfertaInsumo(ofertaInsumo.getCantidadOferta());
                dataOfertaInsumo.setEditCantidad(false);
                BigDecimal montoTotalCertificadoRestante = calcularMontoTotalCertificadoRestanteParaInsumo(dataOfertaInsumo.getProcesoInsumo());
                if (dataOfertaInsumo.getMontoOfertaInsumo().compareTo(montoTotalCertificadoRestante) > 0) {
                    dataOfertaInsumo.setAlcanzaAPagarOferta(false);
                } else {
                    dataOfertaInsumo.setAlcanzaAPagarOferta(true);
                }
                //La cantidad se obtiene de la relación del insumo con el item seleccionado
                Integer cantidadInsumoParaItem = dataOfertaInsumo.getProcesoInsumo().getRelacionItemInsumoPorItem(item).getCantidad();
                cantidadTotalInsumosDelItem += cantidadInsumoParaItem;
                if (dataOfertaInsumo.getAlcanzaAPagarOferta() && dataOfertaInsumo.getCantidadOfertaInsumo().compareTo(cantidadInsumoParaItem) != 0) {
                    dataOfertaInsumo.setAlcanzaAPagarOferta(false);
                }
                BigDecimal montoOfertado = ofertaInsumo.getMontoOferta();
                if (montoTotalCertificadoRestante.compareTo(montoOfertado) < 0) {
                    dataOfertaInsumo.setAlcanzaAPagarOferta(false);
                }

                ofertasInsumoProveedor.add(dataOfertaInsumo);
                contador++;
            }

            BigDecimal precioProveedorEditado = selectOferta.calcularPrecioTotal();
            precioTotalProveedor = precioUnitProveedor.multiply(new BigDecimal(cantidadTotalInsumosDelItem));

            adviertoPrecioProveedor = false;

            comboProveedores.setSelectedT(getOfertaEditar().getProcesoAdquisicionProveedor().getProveedor());

            RequestContext.getCurrentInstance().execute("$('#adjudicarSinCompromisoPresupuestario').modal({backdrop: 'static', keyboard: false});");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }

    }

    /**
     * Este método permite agregar un proveedor a un ítem.
     */
    public void agregarProveedorItem() {
        try {

            if (comboProveedores.getSelectedT() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_PROVEEDOR);
                throw b;
            }
            BigDecimal montoTotalOfertado = BigDecimal.ZERO;

            ProcesoAdquisicionItem item = null;
            if (getOfertaEditar() == null) {
                DataLoteItem dtl = (DataLoteItem) selectedNode[0].getData();
                item = (ProcesoAdquisicionItem) dtl.getObjeto();
            } else {
                item = getOfertaEditar().getProcesoAdquisicionItem();
            }
            boolean montoMayorACertificadoRestante = false;
            for (DataOfertaInsumo ofertaInsumo : ofertasInsumoProveedor) {

                if (ofertaInsumo.getMontoOfertaInsumo() == null || ofertaInsumo.getMontoOfertaInsumo().equals(BigDecimal.ZERO)) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_DEBE_AGREGAR_MONTO_OFERTA_PARA_TODOS_LOS_INSUMOS);
                    throw b;
                }

                if (ofertaInsumo.getCantidadOfertaInsumo() == null || ofertaInsumo.getCantidadOfertaInsumo().equals(0)) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_DEBE_AGREGAR_CANTIDAD_OFERTA_PARA_TODOS_LOS_INSUMOS);
                    throw b;
                }

                if (!adviertoPrecioProveedor && !montoMayorACertificadoRestante && !esMontoMenorACertificadoRestante(item, ofertaInsumo.getProcesoInsumo(), ofertaInsumo.getMontoOfertaInsumo())) {
                    montoMayorACertificadoRestante = true;
                }
            }

            if (montoMayorACertificadoRestante && !adviertoPrecioProveedor) {
                jSFUtils.mostrarAdvertenciaByPropertieCode(ConstantesErrores.ADV_PRECIO_PROVEEDOR_SUPERA_PRESUPUESTOS_ASIGNADOS);
                adviertoPrecioProveedor = true;
                return;
            }

            ProcesoAdquisicionItemProvOferta oferta = null;
            if (getOfertaEditar() == null) {
                oferta = new ProcesoAdquisicionItemProvOferta();

            } else {
                oferta = getOfertaEditar();
            }

            Proveedor proveedor = comboProveedores.getSelectedT();
            ProcesoAdquisicionProveedor proveedorProceso = pAdqDelegate.obtenerProcesoAdquisicionProveedorPorProveedorIdyProcesoId(proveedor.getId(), procesoAdquisicionId);
            for (ProcesoAdquisicionItemProvOferta ofer : ofertas) {
                if (getOfertaEditar() == null && ofer.getProcesoAdquisicionProveedor().getId().equals(proveedorProceso.getId())) {
                    BusinessException b2 = new BusinessException();
                    b2.addError(ConstantesErrores.ERR_PROVEEDOR_YA_INGRESADO);
                    throw b2;
                } else if (getOfertaEditar() != null && !ofertaEditar.equals(ofer) && ofer.getProcesoAdquisicionProveedor().getId().equals(proveedorProceso.getId())) {
                    BusinessException b2 = new BusinessException();
                    b2.addError(ConstantesErrores.ERR_PROVEEDOR_YA_INGRESADO);
                    throw b2;
                }
            }
            List<ProcesoAdquisicionItemOfertaInsumo> ofertasPorInsumo = new ArrayList<>();
            for (DataOfertaInsumo ofertaInsumo : ofertasInsumoProveedor) {
                ProcesoAdquisicionItemOfertaInsumo proAdqItemOfertaIns = null;
                if (getOfertaEditar() == null) {

                    proAdqItemOfertaIns = new ProcesoAdquisicionItemOfertaInsumo();
                } else {

                    proAdqItemOfertaIns = ofertaInsumo.getOfetaInsumo();
                }

                proAdqItemOfertaIns.setOfertaItemProveedor(oferta);
                proAdqItemOfertaIns.setProcesoAdqInsumo(ofertaInsumo.getProcesoInsumo());
                proAdqItemOfertaIns.setMontoOferta(ofertaInsumo.getMontoOfertaInsumo());
                proAdqItemOfertaIns.setCantidadOferta(ofertaInsumo.getCantidadOfertaInsumo());
                ofertasPorInsumo.add(proAdqItemOfertaIns);
            }
            oferta.setOfertasPorInsumo(ofertasPorInsumo);

            oferta.setProcesoAdquisicionItem(item);

            oferta.setProcesoAdquisicionProveedor(proveedorProceso);
            oferta.setPrecioUnitOferta(precioUnitProveedor);
            oferta = pAdqDelegate.agregarEditarOfertaItem(oferta, item.getId());
            oferta.calcularPrecioTotal();
            if (getOfertaEditar() == null) {
                ofertas.add(oferta);
            } else {
                Iterator<ProcesoAdquisicionItemProvOferta> it = ofertas.iterator();
                ProcesoAdquisicionItemProvOferta ofertaCambiar = null;
                while (ofertaCambiar == null && it.hasNext()) {
                    ProcesoAdquisicionItemProvOferta ofertaItem = it.next();
                    if (ofertaItem.getId().equals(oferta.getId())) {
                        ofertaCambiar = ofertaItem;
                    }
                }
                if (ofertaCambiar != null) {
                    int index = ofertas.indexOf(ofertaCambiar);
                    ofertas.remove(ofertaCambiar);
                    ofertas.add(index, oferta);
                }
                if (editCantidad) {
                    List<ProcesoAdquisicionItemOfertaInsumo> ofertasInsumo = oferta.getOfertasPorInsumo();
                    //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                    List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();
                    List<ProcesoAdquisicionInsumo> insumosItem = insumosDelItem;
                    Iterator<DataOfertaInsumo> itOfertaInsmo = ofertasInsumoProveedor.iterator();
                    DataOfertaInsumo dataOfertaInsumoEditCantidad = null;
                    while (dataOfertaInsumoEditCantidad == null && itOfertaInsmo.hasNext()) {
                        DataOfertaInsumo ofertaInsumo = itOfertaInsmo.next();
                        if (ofertaInsumo.getEditCantidad()) {
                            //La cantidad se obtiene de la relación del insumo con el item seleccionado
                            Integer cantidadInsumoParaItem = ofertaInsumo.getProcesoInsumo().getRelacionItemInsumoPorItem(item).getCantidad();
                            if (!cantidadInsumoParaItem.equals(ofertaInsumo.getCantidadOfertaInsumo())) {
                                //si la oferta tiene distinta cantidad al insumo, entonces se debe modificar la cantidad del insumo
                                dataOfertaInsumoEditCantidad = ofertaInsumo;
                            }
                        }

                    }

                    if (dataOfertaInsumoEditCantidad != null) {
                        BusinessException b2 = new BusinessException();
                        b2.addError(ConstantesErrores.ERR_CANTIDAD_DEBE_SER_IGUAL_AL_INSUMO);
                        throw b2;
                    } else {
                        deshacer();
                    }

                }

            }
            setOfertaEditar(null);
            RequestContext.getCurrentInstance().execute("$('#anadirProveedor').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }

    }

    /**
     * Este método es la acción a ejecutar cuando se selecciona un nodo
     *
     * @param event
     */
    public void onNodeSelect(NodeSelectEvent event) {

        try {
            DefaultTreeNode nodo = (DefaultTreeNode) event.getTreeNode();
            DataLoteItem dtl = (DataLoteItem) nodo.getData();

            if (!dtl.getTipo().equals(DataLoteItem.ITEM)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }
            nodo.setExpanded(true);
            ProcesoAdquisicionItem item = (ProcesoAdquisicionItem) dtl.getObjeto();
            itemSeleccionado = item;
            obtenerOfertasDelItem(item);

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }

    }

    /**
     * Este método permite calcular los montos de un insumo de un proveedor.
     */
    public void calcularMontosInsumo() {
        try {
            precioTotalProveedor = BigDecimal.ZERO;
            if (precioUnitProveedor.equals(BigDecimal.ZERO)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_DEBE_AGREGAR_PRECIO_UNITARIO);
                throw b;
            }
            for (DataOfertaInsumo dataOferta : ofertasInsumoProveedor) {
                BigDecimal total = precioUnitProveedor.multiply(new BigDecimal(dataOferta.getCantidadOfertaInsumo()));
                total = total.setScale(2, RoundingMode.HALF_UP);
                dataOferta.setMontoOfertaInsumo(total);
                BigDecimal montoTotalCertificado = dataOferta.getProcesoInsumo().getPoInsumo().getMontoTotalCertificado();
                if (dataOferta.getMontoOfertaInsumo().compareTo(montoTotalCertificado) > 0) {
                    dataOferta.setAlcanzaAPagarOferta(false);
                } else {
                    dataOferta.setAlcanzaAPagarOferta(true);
                }
                precioTotalProveedor = precioTotalProveedor.add(total);
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }

    }

    /**
     * Este método permite eliminar una oferta.
     *
     * @param oferta
     */
    public void eliminarOferta(ProcesoAdquisicionItemProvOferta oferta) {
        try {
            pAdqDelegate.eliminarOfertaItem(oferta);
            mainBean.reloadProceso();
            ofertas.remove(oferta);
            idItemSelect = oferta.getProcesoAdquisicionItem().getId();
            cargarArbol();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }

    }

    /**
     * Este método permite añadir un proveedor
     */
    public void aniadirProveedor() {
        precioTotalProveedor = BigDecimal.ZERO;
        List<Proveedor> proveedores = pAdqDelegate.obtenerProveedoresDelProceso(procesoAdquisicionId);
        comboProveedores = new SofisComboG<>(proveedores, "nombreComercial");
        comboProveedores.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        try {
            if (selectedNode == null || selectedNode.length == 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }
            DataLoteItem dtl = (DataLoteItem) selectedNode[0].getData();
            if (!dtl.getTipo().equals(DataLoteItem.ITEM)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }
            editCantidad = false;
            ProcesoAdquisicionItem item = (ProcesoAdquisicionItem) dtl.getObjeto();
            ofertasInsumoProveedor = new ArrayList<>();
            Integer contador = 1;
            //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
            List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();
            for (ProcesoAdquisicionInsumo insumo : insumosDelItem) {
                DataOfertaInsumo dataOfertaInsumo = new DataOfertaInsumo();
                dataOfertaInsumo.setIndex(contador);
                dataOfertaInsumo.setProcesoInsumo(insumo);
                dataOfertaInsumo.setMontoOfertaInsumo(BigDecimal.ZERO);
                dataOfertaInsumo.setEditCantidad(false);
                //La cantidad se obtiene de la relación del insumo con el item seleccionado
                Integer cantidadInsumoParaItem = insumo.getRelacionItemInsumoPorItem(item).getCantidad();
                dataOfertaInsumo.setCantidadOfertaInsumo(cantidadInsumoParaItem);
                dataOfertaInsumo.setAlcanzaAPagarOferta(true);
                ofertasInsumoProveedor.add(dataOfertaInsumo);
                contador++;

            }
            adviertoPrecioProveedor = false;
            comboProveedores.clearSelectedT();
            setOfertaEditar(null);
            precioUnitProveedor = null;
            RequestContext.getCurrentInstance().execute("$('#anadirProveedor').modal('show');");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }

    }

    /**
     * Este método permite editar una oferta
     *
     * @param oferta
     */
    public void editarProveedor(ProcesoAdquisicionItemProvOferta oferta) {
        adviertoPrecioProveedor = false;
        editarProveedor = true;
        comboProveedores.setSelectedT(oferta.getProcesoAdquisicionProveedor().getProveedor());
        setOfertaEditar(oferta);
        RequestContext.getCurrentInstance().execute("$('#anadirProveedor').modal('show');");
    }

    /**
     * Este método registra que un ítem pasa a estar en pausa.
     */
    public void pausarItem() {
        try {
            if (selectOferta == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_PROVEEDOR);
                throw b;
            }

            if (itemSeleccionado == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }

            ProcesoAdquisicionItem item = itemSeleccionado;

            BigDecimal precioProveedor = selectOferta.calcularPrecioTotal();
            List<ProcesoAdquisicionItemOfertaInsumo> ofertasInsumos = selectOferta.getOfertasPorInsumo();
            boolean hayUnMontoMayorCertificadoRestante = false;
            for (ProcesoAdquisicionItemOfertaInsumo ofertaInsumo : ofertasInsumos) {
                if (!esMontoMenorACertificadoRestante(item, ofertaInsumo.getProcesoAdqInsumo(), ofertaInsumo.getMontoOferta())) {
                    hayUnMontoMayorCertificadoRestante = true;
                    break;
                }
            }

            if (!hayUnMontoMayorCertificadoRestante) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_ALCANZA_PRESUPUESTO_ASIGNADO);
                throw b;
            }

            item.setFechaPausa(new Date());
            mainBean.setObjeto(pAdqDelegate.asignarItem(item, selectOferta, EstadoItem.PAUSA, mainBean.getObjeto().getId()));
            item = pAdqDelegate.pausarItem(item.getId());
            obtenerOfertasDelItem(item);

            idItemSelect = item.getId();
            cargarArbol();

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método permite cargar los insumos de un lote para editar.
     *
     * @param dtl
     */
    public void cargarInsumosEditar(DataLoteItem dtl) {
        relacionItemInsumoAEditar = dtl.getRelacionInsumoItem();
        RequestContext.getCurrentInstance().execute("$('#editarInsumo').modal('show');");
    }

    /**
     * Este método permite obtener las ofertas de un ítem.
     *
     * @param item
     */
    private void obtenerOfertasDelItem(ProcesoAdquisicionItem item) {
        ofertas = pAdqDelegate.obtenerOfertasDelItem(item.getId());
        Integer ofertaId = null;
        if (item.getProveedorOfertaAdjudicadaId() != null) {
            ofertaId = item.getProveedorOfertaAdjudicadaId().getId();
        }

        for (ProcesoAdquisicionItemProvOferta oferta : ofertas) {
            oferta.setEditable(false);
            if (oferta.getId().equals(ofertaId)) {
                oferta.setAsociado(true);

                if (item.getEstado().equals(EstadoItem.PAUSA)) {
                    Date fechaPausa = item.getFechaPausa();
                    Date fechaActual = new Date();
                    long diferenciaEn_ms = fechaActual.getTime() - fechaPausa.getTime();
                    long diferenciaDias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
                    Integer diasPausa = (int) diferenciaDias;
                    if (diasPlazoPausa <= diasPausa) {
                        oferta.setEditable(true);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(fechaPausa); // Configuramos la fecha que se recibe
                        calendar.add(Calendar.DAY_OF_YEAR, diasPlazoPausa);  // numero de días a añadir, o restar en caso de días<0
                        fechaPlazo = calendar.getTime();
                    }

                }
            } else {
                oferta.setAsociado(false);
            }
        }
    }

    /**
     * Este método realiza la reserva de números de contrato.
     */
    public void reservarNrosContrato() {
        try {

            if (cantNroReserva < 1) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_CANTIDAD_NROS_A_RESERVAR);
                throw b;
            }
            pAdqDelegate.reservarNumerosContrato(procesoAdquisicionId, cantNroReserva);
            mainBean.reloadProceso();

            if (mainBean.getObjeto().getReservaNroContratoFinal() != null && mainBean.getObjeto().getReservaNroContratoFinal() != 0) {
                Integer nroInicial = mainBean.getObjeto().getReservaNroContratoInicial();
                Integer nroFinal = mainBean.getObjeto().getReservaNroContratoFinal();

                for (Integer i = nroInicial; i <= nroFinal; i++) {
                    if (i.equals(nroInicial)) {
                        nrosReservados = "" + i;
                    } else {
                        nrosReservados += " ," + i;
                    }

                }
            } 

            String texto = textMB.obtenerTexto("labels.ReservaDeNumerosDeContratoExitosa");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método registra un ítem como desierto.
     */
    public void desiertoItem() {
        try {
            if (itemSeleccionado == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }
            ProcesoAdquisicionItem item = itemSeleccionado;
            mainBean.setObjeto(pAdqDelegate.asignarItem(item, null, EstadoItem.DESIERTO, mainBean.getObjeto().getId()));

            mainBean.reloadProceso();
            //se vuelve a cargar el item por el modificado
            item = UtilsUACI.getItem(mainBean.getObjeto(), item.getId());
            obtenerOfertasDelItem(item);
            idItemSelect = item.getId();
            cargarArbol();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método permite declarar un ítem sin efecto.
     */
    public void sinEfectoItem() {
        try {

            if (itemSeleccionado == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }

            ProcesoAdquisicionItem item = itemSeleccionado;

            mainBean.setObjeto(pAdqDelegate.asignarItem(item, null, EstadoItem.SIN_EFECTO, mainBean.getObjeto().getId()));

            mainBean.reloadProceso();
            //se vuelve a cargar el item por el modificado
            item = UtilsUACI.getItem(mainBean.getObjeto(), item.getId());
            obtenerOfertasDelItem(item);
            idItemSelect = item.getId();
            cargarArbol();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método permite registrar un ítem como adjudicado.
     */
    public void adjudicarItem() {
        try {
            if (selectOferta == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_PROVEEDOR);
                throw b;
            }

            if (itemSeleccionado == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }

            ProcesoAdquisicionItem item = itemSeleccionado;

            List<ProcesoAdquisicionItemOfertaInsumo> ofertasInsumos = selectOferta.getOfertasPorInsumo();
            boolean hayUnMontoMayorCertificadorestante = false;
            boolean cumpleCantidad = true;
            for (ProcesoAdquisicionItemOfertaInsumo ofertaInsumo : ofertasInsumos) {
                if (!esMontoMenorACertificadoRestante(item, ofertaInsumo.getProcesoAdqInsumo(), ofertaInsumo.getMontoOferta())) {
                    hayUnMontoMayorCertificadorestante = true;
                    break;
                }

                //La cantidad se obtiene de la relación entre el insumo y el item
                Integer cantidadInsumo = ofertaInsumo.getProcesoAdqInsumo().getRelacionItemInsumoPorItem(item).getCantidad();
                if (cantidadInsumo != ofertaInsumo.getCantidadOferta().intValue()) {
                    cumpleCantidad = false;
                    break;
                }
            }

            if (hayUnMontoMayorCertificadorestante) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_MONTOS_ASIGNADOS_PARA_INSUMOS_MENOR_A_PRECIO_PROVEEDOR);
                throw b;
            }

            mainBean.setObjeto(pAdqDelegate.asignarItem(item, selectOferta, EstadoItem.ADJUDICADO, mainBean.getObjeto().getId()));
            idItemSelect = item.getId();
            mainBean.reloadProceso();
            //se vuelve a cargar el item por el modificado
            item = UtilsUACI.getItem(mainBean.getObjeto(), item.getId());
            obtenerOfertasDelItem(item);
            cargarArbol();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }

    }

    /**
     * Este método permite adjudicar un ítem sin compromiso presupuestario.
     */
    public void adjudicarItemSinCompromisoPresupuestario() {
        try {
            ProcesoAdquisicionItem item = itemSeleccionado;
            List<ProcesoAdquisicionItemOfertaInsumo> ofertasInsumos = selectOferta.getOfertasPorInsumo();
            for (ProcesoAdquisicionItemOfertaInsumo ofertaInsumo : ofertasInsumos) {
                boolean encontro = false;
                Iterator<DataOfertaInsumo> itOfertasInsProv = ofertasInsumoProveedor.iterator();
                while (itOfertasInsProv.hasNext() && !encontro) {
                    DataOfertaInsumo ofertaInsProv = itOfertasInsProv.next();
                    if (ofertaInsumo.getId().equals(ofertaInsProv.getOfetaInsumo().getId())) {
                        encontro = true;
                        ofertaInsumo.setMontoOferta(ofertaInsProv.getMontoOfertaInsumo());
                    }
                }
            }
            selectOferta = pAdqDelegate.adjudicarItemSinCompromisoPresupuestario(selectOferta);
            mainBean.reloadProceso();
            idItemSelect = item.getId();
            //se vuelve a cargar el item por el modificado
            item = UtilsUACI.getItem(mainBean.getObjeto(), item.getId());
            obtenerOfertasDelItem(item);
            cargarArbol();
            RequestContext.getCurrentInstance().execute("$('#adjudicarSinCompromisoPresupuestario').modal('hide');");

        } catch (GeneralException ex) {
            mainBean.reloadProceso();
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            mainBean.reloadProceso();
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método permite cancelar la adjudicación de un ítem sin compromiso
     * presupuestario.
     */
    public void cancelarAdjudicacionItemSinCompromisoPresupuestario() {
        ProcesoAdquisicionItem item = itemSeleccionado;
        mainBean.reloadProceso();
        idItemSelect = item.getId();

        //se vuelve a cargar el item por el modificado
        item = UtilsUACI.getItem(mainBean.getObjeto(), item.getId());
        obtenerOfertasDelItem(item);
        cargarArbol();
        RequestContext.getCurrentInstance().execute("$('#adjudicarSinCompromisoPresupuestario').modal('hide');");
    }

    /**
     * Este método permite deshacer la selección de un ítem.
     */
    public void deshacer() {
        try {
            if (itemSeleccionado == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }

            ProcesoAdquisicionItem item = itemSeleccionado;

            item = pAdqDelegate.deshacerEstadoItem(item.getId());
            mainBean.reloadProceso();
            idItemSelect = itemSeleccionado.getId();
            obtenerOfertasDelItem(item);
            cargarArbol();
            mainBean.reloadProceso();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método es el evento en la edición de una celda.
     *
     * @param event
     */
    public void onCellEdit(CellEditEvent event) {
        int rowIndex = event.getRowIndex();

        DataOfertaInsumo dataOferta = ofertasInsumoProveedor.get(rowIndex);

        BigDecimal total = precioUnitProveedor.multiply(new BigDecimal(dataOferta.getCantidadOfertaInsumo()));
        total = total.setScale(2, RoundingMode.HALF_UP);

        dataOferta.setMontoOfertaInsumo(total);
        BigDecimal montoTotalCertificado = dataOferta.getProcesoInsumo().getPoInsumo().getMontoTotalCertificado();
        if (dataOferta.getMontoOfertaInsumo().compareTo(montoTotalCertificado) > 0) {
            dataOferta.setAlcanzaAPagarOferta(false);

        } else {
            dataOferta.setAlcanzaAPagarOferta(true);

        }

    }

    /**
     * Este método calcula el total de un insumo proveedor.
     *
     * @param rowIndex
     */
    public void calcularTotal(Integer rowIndex) {
        DataOfertaInsumo dataOferta = ofertasInsumoProveedor.get(rowIndex);

        BigDecimal total = precioUnitProveedor.multiply(new BigDecimal(dataOferta.getCantidadOfertaInsumo()));
        total = total.setScale(2, RoundingMode.HALF_UP);

        dataOferta.setMontoOfertaInsumo(total);
        BigDecimal montoTotalCertificado = dataOferta.getProcesoInsumo().getPoInsumo().getMontoTotalCertificado();
        if (dataOferta.getMontoOfertaInsumo().compareTo(montoTotalCertificado) > 0) {
            dataOferta.setAlcanzaAPagarOferta(false);

        } else {
            dataOferta.setAlcanzaAPagarOferta(true);

        }
    }

    /**
     * Este método verifica los montos de una oferta en particular.
     *
     * @param rowIndex
     */
    public void verificarMontos(Integer rowIndex) {
        DataOfertaInsumo dataOferta = ofertasInsumoProveedor.get(rowIndex);
        BigDecimal montoTotalCertificado = dataOferta.getProcesoInsumo().getPoInsumo().getMontoTotalCertificado();
        if (dataOferta.getMontoOfertaInsumo().compareTo(montoTotalCertificado) > 0) {
            dataOferta.setAlcanzaAPagarOferta(false);

        } else {
            dataOferta.setAlcanzaAPagarOferta(true);

        }
    }

    /**
     * Este método determina si una oferta está asociada a un proveedor.
     *
     * @param oferta
     * @return
     */
    public boolean isOfertaAsociada(ProcesoAdquisicionItemProvOferta oferta) {
        return oferta.getId().equals(itemSeleccionado.getId());
    }

    /**
     * Este método realiza el monto total de un ítem
     *
     * @param item
     * @return
     */
    private BigDecimal calculoMontoItem(ProcesoAdquisicionItem item) {
        BigDecimal valorTotalItem = BigDecimal.ZERO;
        //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
        List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();//pAdqDelegate.obtenerInsumosDelItem(item.getId());
        for (ProcesoAdquisicionInsumo insumo : insumosDelItem) {
            BigDecimal montoTotalCertificado = insumo.getPoInsumo().getMontoTotalCertificado();
            valorTotalItem = valorTotalItem.add(montoTotalCertificado);
        }
        return valorTotalItem;
    }

    /**
     * Este método determina si el monto certificado de un insumo es menor a lo
     * disponible certificado.
     *
     * @param item
     * @param insumoOfertado
     * @param montoOfertado
     * @return
     */
    private boolean esMontoMenorACertificadoRestante(ProcesoAdquisicionItem item, ProcesoAdquisicionInsumo insumoOfertado, BigDecimal montoOfertado) {
        BigDecimal valorTotalItem = BigDecimal.ZERO;
        //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
        List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();
        for (ProcesoAdquisicionInsumo insumo : insumosDelItem) {

            if (insumo.getId().equals(insumoOfertado.getId())) {
                BigDecimal montoTotalCertificadoRestante = calcularMontoTotalCertificadoRestanteParaInsumo(insumo);
                if (montoTotalCertificadoRestante.compareTo(montoOfertado) < 0) {
                    return false;
                } else {
                    return true;
                }
            }

        }
        return true;

    }

    /**
     * Este método calcula el monto por proveedor.
     *
     * @param rowIndex
     */
    public void calcularMontoProveedor(Integer rowIndex) {
        DataInsumoOferta datainsumo = insumosAEditar.get(rowIndex);

        BigDecimal total = precioUnitProveedor.multiply(new BigDecimal(datainsumo.getCantidadEditable()));
        total = total.setScale(2, RoundingMode.HALF_UP);

        datainsumo.setMontoCalculadoProveedor(total);
        ProcesoAdquisicionInsumo insumoProceso = datainsumo.getProcesoInsumo();
        BigDecimal montoTotalCertificado = insumoProceso.getPoInsumo().getMontoTotalCertificado();
        if (datainsumo.getMontoCalculadoProveedor().compareTo(montoTotalCertificado) > 0) {
            datainsumo.setAlcanzaAPagarOferta(false);
        } else {
            datainsumo.setAlcanzaAPagarOferta(true);
        }
    }

    /**
     * Este método guarda los cambios realizados a un insumo
     */
    public void guardarCambioInsumos() {
        try {
            pAdqDelegate.editarInsumosProceso(relacionItemInsumoAEditar);
            mainBean.reloadProceso();
            cargarArbol();
            obtenerOfertasDelItem(relacionItemInsumoAEditar.getItem());
            relacionItemInsumoAEditar = null;
            RequestContext.getCurrentInstance().execute("$('#editarInsumo').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método genera el reporte de reserva de fondos.
     */
    public void generarReportereservaDeFondo() {
        byte[] bytespdf = reporteDelegate.generarDocumentoReservaDeFondos(procesoAdquisicionId);
        String nombreArchivo = "ReservaDeFondos_"+mainBean.getObjeto().getSecuenciaAnio()+"_"+mainBean.getObjeto().getSecuenciaNumero()+".pdf";
        ArchivoUtils.downloadPdfFromBytes(bytespdf, nombreArchivo);
    }

    /**
     * Este método calcula el monto total certificado disponible para un insumo
     *
     * @param insumo
     * @return
     */
    public BigDecimal calcularMontoTotalCertificadoRestanteParaInsumo(ProcesoAdquisicionInsumo insumo) {
        BigDecimal montoTotalCertificado = insumo.getPoInsumo().getMontoTotalCertificado();
        BigDecimal montoAdjudicado = insumo.getMontoTotalAdjudicado();
        return montoTotalCertificado.subtract(montoAdjudicado);
    }

    /**
     * Este método determina si una oferta es adjudicable.
     *
     * @param oferta
     * @return
     */
    public Boolean noEsOfertaAdjudicable(ProcesoAdquisicionItemProvOferta oferta) {
        ProcesoAdquisicionItem item = oferta.getProcesoAdquisicionItem();
        Boolean hayUnaNoAdjudicable = false;
        //Si el item ya tiene oferta adjudicada, no lo tomo en cuenta.
        if (item.getEstado() == null || !item.getEstado().equals(EstadoItem.ADJUDICADO)) {
            BigDecimal montoTotalOfertado = oferta.getPrecioTotal();
            BigDecimal montoRestanteCertificadoTotal = BigDecimal.ZERO;
            Iterator<ProcesoAdquisicionInsumo> itInsumosDelItemDeLaOferta = item.getInsumosTemporalesDelItem().iterator();
            while (itInsumosDelItemDeLaOferta.hasNext() && !hayUnaNoAdjudicable) {
                ProcesoAdquisicionInsumo insumoDelItemDeLaOferta = itInsumosDelItemDeLaOferta.next();
                Integer cantidadInsumo = insumoDelItemDeLaOferta.getRelacionItemInsumoPorItem(item).getCantidad();
                BigDecimal montoOfertaInsumo = oferta.getPrecioUnitOferta().multiply(new BigDecimal(cantidadInsumo));
                BigDecimal montoRestanteCertificadoInsumo = calcularMontoTotalCertificadoRestanteParaInsumo(insumoDelItemDeLaOferta);
                if (montoOfertaInsumo.compareTo(montoRestanteCertificadoInsumo) > 0) {
                    hayUnaNoAdjudicable = true;
                } else {
                    montoRestanteCertificadoTotal = montoRestanteCertificadoTotal.add(montoOfertaInsumo);
                }
            }
            if (!hayUnaNoAdjudicable) {
                if (montoTotalOfertado.compareTo(montoRestanteCertificadoTotal) > 0) {
                    hayUnaNoAdjudicable = true;
                }
            }
        }
        return hayUnaNoAdjudicable;
    }
// <editor-fold defaultstate="collapsed" desc="getter-setter">

    public TreeNode getRootArbol() {
        return rootArbol;
    }

    public void setRootArbol(TreeNode rootArbol) {
        this.rootArbol = rootArbol;
    }

    public SofisComboG<Proveedor> getComboProveedores() {
        return comboProveedores;
    }

    public void setComboProveedores(SofisComboG<Proveedor> comboProveedores) {
        this.comboProveedores = comboProveedores;
    }

    public TreeNode[] getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode[] selectedNode) {
        this.selectedNode = selectedNode;
    }

    public List<ProcesoAdquisicionItemProvOferta> getOfertas() {
        return ofertas;
    }

    public void setOfertas(List<ProcesoAdquisicionItemProvOferta> ofertas) {
        this.ofertas = ofertas;
    }

    public ProcesoAdquisicionItemProvOferta getSelectOferta() {
        return selectOferta;
    }

    public void setSelectOferta(ProcesoAdquisicionItemProvOferta selectOferta) {
        this.selectOferta = selectOferta;
    }

    public ProcesoAdquisicionItem getItemSeleccionado() {
        return itemSeleccionado;
    }

    public void setItemSeleccionado(ProcesoAdquisicionItem itemSeleccionado) {
        this.itemSeleccionado = itemSeleccionado;
    }

    public Integer getCantNroReserva() {
        return cantNroReserva;
    }

    public void setCantNroReserva(Integer cantNroReserva) {
        this.cantNroReserva = cantNroReserva;
    }

    public List<DataOfertaInsumo> getOfertasInsumoProveedor() {
        return ofertasInsumoProveedor;
    }

    public void setOfertasInsumoProveedor(List<DataOfertaInsumo> ofertasInsumoProveedor) {
        this.ofertasInsumoProveedor = ofertasInsumoProveedor;
    }

    public boolean getEditCantidad() {
        return editCantidad;
    }

    public void setEditCantidad(boolean editCantidad) {
        this.editCantidad = editCantidad;
    }

    public boolean getAdviertoPrecioProveedor() {
        return adviertoPrecioProveedor;
    }

    public void setAdviertoPrecioProveedor(boolean adviertoPrecioProveedor) {
        this.adviertoPrecioProveedor = adviertoPrecioProveedor;
    }

    public Date getFechaPlazo() {
        return fechaPlazo;
    }

    public void setFechaPlazo(Date fechaPlazo) {
        this.fechaPlazo = fechaPlazo;
    }

    public BigDecimal getPrecioUnitProveedor() {
        return precioUnitProveedor;
    }

    public void setPrecioUnitProveedor(BigDecimal precioUnitProveedor) {
        this.precioUnitProveedor = precioUnitProveedor;
    }

    public String getNrosReservados() {
        return nrosReservados;
    }

    public void setNrosReservados(String nrosReservados) {
        this.nrosReservados = nrosReservados;
    }

    public List<DataInsumoOferta> getInsumosAEditar() {
        return insumosAEditar;
    }

    public void setInsumosAEditar(List<DataInsumoOferta> insumosAEditar) {
        this.insumosAEditar = insumosAEditar;
    }

    public BigDecimal getPrecioTotalProveedor() {
        return precioTotalProveedor;
    }

    public void setPrecioTotalProveedor(BigDecimal precioTotalProveedor) {
        this.precioTotalProveedor = precioTotalProveedor;
    }

    public RelacionProAdqItemInsumo getRelacionItemInsumoAEditar() {
        return relacionItemInsumoAEditar;
    }

    public void setRelacionItemInsumoAEditar(RelacionProAdqItemInsumo relacionItemInsumoAEditar) {
        this.relacionItemInsumoAEditar = relacionItemInsumoAEditar;
    }

    public ProcesoAdquisicionItemProvOferta getOfertaEditar() {
        return ofertaEditar;
    }

    public void setOfertaEditar(ProcesoAdquisicionItemProvOferta ofertaEditar) {
        this.ofertaEditar = ofertaEditar;
    }
// </editor-fold>
}
