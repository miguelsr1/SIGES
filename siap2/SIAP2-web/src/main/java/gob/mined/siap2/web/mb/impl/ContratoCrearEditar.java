/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.utils.FlujoCajaMensualUtils;
import gob.mined.siap2.business.utils.TipoSeguimientoUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.ActaContrato;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.entities.data.impl.ModificativaContrato;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.PagoInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.entities.data.impl.RelActaItem;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.enums.EstadoActa;
import gob.mined.siap2.entities.enums.EstadoContrato;
import gob.mined.siap2.entities.enums.TipoActaContrato;
import gob.mined.siap2.entities.enums.TipoPagoActa;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.datatypes.DataLoteItem;
import gob.mined.siap2.web.datatypes.DataPagoItem;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.delegates.impl.ContratoDelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.mb.UtilsMB;
import gob.mined.siap2.web.mb.impl.padq.ContratoOrdenCompra;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.web.utils.InsumoContratoFlujoCajaMensual;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "contratoCE")
public class ContratoCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    ContratoDelegate contratoDelegate;
    @Inject
    TextMB textMB;
    @Inject
    UsuarioDelegate usuarioDelegate;
    @Inject
    UsuarioInfo userInfo;
    @Inject
    ProcesoAdquisicionDelegate proAdqDelegate;
    @Inject
    private ReporteDelegate reporteDelegate;
    @Inject
    ArchivoDelegate archivoDelegate;

    private ContratoOC objeto;
    private ActaContrato tempActa;
    private String impuestosAplica;
    private UploadedFile uploadedFile;
    private SofisComboG<SsUsuario> comboUsuarios;
    private SofisComboG<SsUsuario> comboUsuariosFirmantes;
    private Boolean modificandoOrdenInicio = false;
    private TreeNode rootNodePagos;
    private Boolean esActaRecepcion = true;
    private List<RelacionProAdqItemInsumo> relacionesItemsInsumos;
    private Set<FlujoCajaAnio> programacionPagosEnEdicion;
    private String idPOFlujoCajaMenusal;
    private Date fechaFinParaExtender;
    private boolean habilitarAnadirInsumo;
    private List<POInsumos> poInusmosDisponiblesParaModificativa;
    private List<POInsumos> poInsumosSeleccionadosParaAgregarAModificativa;
    private List<POInsumos> poInsumosAEliminarDeModificativa;
    private ModificativaContrato tempModificativa;
    private Boolean deshabilitarTabCompromisoYReservaEnModificativa;
    private DataLoteItem itemADistribuir;
    private String filtroFCMCodrigoInsumo;
    private String filtroFCMAnio;
    private String filtroFCMTipo;
    private RelacionProAdqItemInsumo relacionItemInsumoEnEdicion;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            objeto = contratoDelegate.cargarContrato(Integer.valueOf(id));
            if (objeto.getEstado() == null) {
                objeto.setEstado(EstadoContrato.EN_EJECUCION);
            }
        }

//        //si no es el administrador del contrato deja el contrato vacio
//        if (!userInfo.getUserCod().equals(objeto.getAdministrador().getUsuCod())) {
//            objeto = null;
//        }
        initUsuarios();
        initUsuariosFirmantes();
        cargarImpuestosAplica();
        reloadNodePagos();
        relacionesItemsInsumos = cargarRelacionesItemInsumos();
        modificarOrdenInicio();
        cargarPagosAnioMes();
        habilitarAnadirInsumo = false;
    }

    /**
     * Recarga desde la base de datos un Contrato / Orden de compre
     */
    public void recargarObjeto() {
        objeto = contratoDelegate.cargarContrato(objeto.getId());

        comboUsuarios.setSelectedT(objeto.getAdministrador());
        comboUsuariosFirmantes.setSelectedT(objeto.getFirmanteOrdenInicio());
    }

    /**
     * Valida e inicia un contrato
     *
     * @return
     */
    public String iniciarContrato() {
        try {
            BusinessException b = new BusinessException();
            if (objeto.getFechasDesdeOrdenInicio()) {
                //La fecha de fin debe ser mayor o igual a la fecha de inicio
                if (objeto.getFechaFin().compareTo(objeto.getFechaInicio()) < 0) {
                    b.addError(ConstantesErrores.ERR_FECHA_FIN_ORDEN_INICIO_MENOR_FECHA_INICIO);
                }
                //La fecha de inicio debe ser mayor o igual a la fecha de emisión del contrato/orden de compra
                if (objeto.getFechaInicio().compareTo(objeto.getFechaEmision()) < 0) {
                    b.addError(ConstantesErrores.ERR_FECHA_INICIO_ORDEN_INICIO_MENOR_FECHA_EMISION);
                }
            }
            if (objeto.getArchivoOrdenInicio() == null) {
                b.addError(ConstantesErrores.ERR_DEBE_CARGAR_ORDEN_INICIO_PARA_INICIAR);
            }
            if (b.getErrores().size() > 0) {
                throw b;
            }
            objeto.setEstado(EstadoContrato.EN_EJECUCION);
            contratoDelegate.notificarProcesoAdquisicionAsignadoAOrdenDeInicio(objeto);

            return guardar();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("mainPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("mainPanel");
        }
        return null;
    }

    /**
     * Genera y descarga el reporte de orden de inicio de un Contrato / OC
     */
    public void generarOrdenInicio() {
        try {
            BusinessException b = new BusinessException();
            if (comboUsuarios.getSelectedT() == null) {
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_ADMIN_CONTRATO_OC);
            }
            if (comboUsuariosFirmantes.getSelectedT() == null) {
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_FIRMANTE_CONTRATO_OC);
            }
            if (objeto.getFechasDesdeOrdenInicio()) {
                //La fecha de fin debe ser mayor o igual a la fecha de inicio
                if (objeto.getFechaFin().compareTo(objeto.getFechaInicio()) < 0) {
                    b.addError(ConstantesErrores.ERR_FECHA_FIN_ORDEN_INICIO_MENOR_FECHA_INICIO);
                }
                //La fecha de inicio debe ser mayor o igual a la fecha de emisión del contrato/orden de compra
                if (objeto.getFechaInicio().compareTo(objeto.getFechaEmision()) < 0) {
                    b.addError(ConstantesErrores.ERR_FECHA_INICIO_ORDEN_INICIO_MENOR_FECHA_EMISION);
                }
            }
            if (!b.getErrores().isEmpty()) {
                throw b;
            }
            if (objeto.getFechasDesdeOrdenInicio()) {
                Integer plazo = Math.round(DatesUtils.getDateDiff(objeto.getFechaInicio(), objeto.getFechaFin(), TimeUnit.DAYS));
                objeto.setPlazoEntrega(plazo);
            }
            objeto.setFirmanteOrdenInicio(comboUsuariosFirmantes.getSelectedT());
            byte[] bytespdf = reporteDelegate.generarOrdenInicio(objeto);
            String nombreArchivo = "OrdenInicio_" + objeto.getNroContrato() + ".pdf";
            ArchivoUtils.downloadPdfFromBytes(bytespdf, nombreArchivo);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("mainPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("mainPanel");
        }

    }

    /**
     * Genera y descarga el reporte de ficha de un Contrato / OC
     */
    public void generarFichaContrato() {
        byte[] bytespdf = reporteDelegate.generarFichaContrato(objeto.getId());
        FacesContext fc = FacesContext.getCurrentInstance();

        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
        ec.setResponseContentType("application/pdf");
        ec.setResponseContentLength(bytespdf.length);
        String nombreArchivo = "FichaContrato_" + objeto.getNroContrato() + ".pdf";
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"");
        OutputStream output;
        try {
            output = ec.getResponseOutputStream();
            output.write(bytespdf);
        } catch (IOException ex) {
            Logger.getLogger(ContratoOrdenCompra.class.getName()).log(Level.SEVERE, null, ex);
        }
        fc.responseComplete();

    }

    /**
     * Guarda el objeto en edición
     *
     * @return
     */
    public String guardar() {
        try {
            BusinessException b = new BusinessException();
            if (comboUsuarios.getSelectedT() == null) {
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_ADMIN_CONTRATO_OC);
            }
            if (comboUsuariosFirmantes.getSelectedT() == null) {
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_FIRMANTE_CONTRATO_OC);
            }
            if (!b.getErrores().isEmpty()) {
                throw b;
            }
            objeto.setAdministrador(comboUsuarios.getSelectedT());
            objeto.setFirmanteOrdenInicio(comboUsuariosFirmantes.getSelectedT());
            objeto = contratoDelegate.guardarContraro(objeto);
            modificarOrdenInicio();
            if (objeto.getAdministrador().getUsuCod().equals(userInfo.getUserCod())) {
                String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
                RequestContext.getCurrentInstance().scrollTo("mainPanel");
            } else {
                return "consultaContratoOC.xhtml?faces-redirect=true";
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("mainPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("mainPanel");
        }
        return null;
    }

    /**
     * Deja un contrato en estado cerrado
     *
     * @return
     */
    public String cerrarContrato() {
        try {
            contratoDelegate.cerrarContrato(objeto.getId());
            return "consultaContratoOC.xhtml?faces-redirect=true";

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }

    /**
     * Obtiene el proveedor asociado a un Contrato / OC
     *
     * @return
     */
    public Proveedor getProveedor() {
        if (objeto.getItems().isEmpty()) {
            return null;
        }
        return objeto.getItems().get(0).getProveedorOfertaAdjudicadaId().getProcesoAdquisicionProveedor().getProveedor();
    }

    /**
     * Carga los datos de un Acta de Contrato / OC
     */
    public void reloadPago() {
        idPOFlujoCajaMenusal = null;
        if (tempActa == null) {
            tempActa = new ActaContrato();
            tempActa.setQuedanEmitido(false);
            tempActa.setLugarRecepcion("MINED");
            tempActa.setPagosInsumo(new LinkedList());
            tempActa.setRelActaItem(new LinkedList<RelActaItem>());
            for (ProcesoAdquisicionItem item : objeto.getItems()) {
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                RelActaItem relActaItem = new RelActaItem();
                relActaItem.setActa(tempActa);
                relActaItem.setItem(item);
                tempActa.getRelActaItem().add(relActaItem);

                for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                    PagoInsumo pi = new PagoInsumo();
                    pi.setContrato(tempActa);
                    pi.setRelacionItemInsumo(rel);
                    tempActa.getPagosInsumo().add(pi);
                }
            }
        } else {
            tempActa = contratoDelegate.cargarPago(tempActa.getId());
            if (tempActa.getMesPago() != null) {
                idPOFlujoCajaMenusal = String.valueOf(tempActa.getMesPago().getId());
            }
        }
        reloadNodePagos();
        calcularPorcentajeMontoFijo();
    }

    /**
     * Rearma el tree table de actas de Contrato / OC
     */
    public void reloadNodePagos() {
        rootNodePagos = crearRootNodePagos();
    }

    /**
     * Crea el tree table de actas de Contrato / OC
     *
     * @return
     */
    public TreeNode crearRootNodePagos() {
        TreeNode rootNodePagos = new DefaultTreeNode(new DataLoteItem(DataLoteItem.ROOT, null, false), null);
        rootNodePagos.setExpanded(true);
        if (tempActa != null) {

            BigDecimal pagoActa = BigDecimal.ZERO;
            BigDecimal importeTotalTodosLosItems = BigDecimal.ZERO;
            Integer cantidadTotalTodosLosItems = 0;
            if ((tempActa.getConDetalle() == null || !tempActa.getConDetalle()) && tempActa.getMesPago() != null) {
                pagoActa = tempActa.getMesPago().getMonto();

                for (ProcesoAdquisicionItem item : objeto.getItems()) {
                    for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                        importeTotalTodosLosItems = importeTotalTodosLosItems.add(rel.getMontoTotalAdjudicado());
                        cantidadTotalTodosLosItems += rel.getCantidadAdjudicada();
                    }
                }
            }

            //Con el siguiente valor se hacen los prorateos en el último item
            BigDecimal totalRepartidoEnItems = BigDecimal.ZERO;
            List<DataPagoItem> listaDataPagosItems = new LinkedList<DataPagoItem>();
            List<PagoInsumo> listaUltimosPagos = null;

            for (ProcesoAdquisicionItem item : objeto.getItems()) {
                DataPagoItem pataPagoItem = new DataPagoItem();
                pataPagoItem.setCantidad(0);
                pataPagoItem.setMontoTotal(BigDecimal.ZERO);
                pataPagoItem.setCantRecibida(0);
                pataPagoItem.setImporteUnit(BigDecimal.ZERO);
                pataPagoItem.setImporte(BigDecimal.ZERO);
                pataPagoItem.setItem(item);

                Iterator<RelActaItem> itRelacionesActasItems = tempActa.getRelActaItem().iterator();
                Boolean encontro = false;
                while (itRelacionesActasItems.hasNext() && !encontro) {
                    RelActaItem relActaItem = itRelacionesActasItems.next();
                    if (relActaItem.getItem().getId().equals(item.getId())) {
                        pataPagoItem.setRelActaItem(relActaItem);
                        relActaItem.setDescripcion(item.getTdr().getContenido());
                        encontro = true;
                    }
                }

                DataLoteItem dt = new DataLoteItem(DataLoteItem.ITEM, pataPagoItem);
                TreeNode itemNode = new DefaultTreeNode(dt, rootNodePagos);
                itemNode.setExpanded(true);

                List<RelacionProAdqItemInsumo> relacionesDelItem = item.getRelItemInsumos();
                for (PagoInsumo pago : tempActa.getPagosInsumo()) {
                    if (relacionesDelItem.contains(pago.getRelacionItemInsumo())) {
                        pataPagoItem.setMontoUnitAdjudicado(pago.getRelacionItemInsumo().getMontoUnitAdjudicado());
                        pataPagoItem.setCantidad(pataPagoItem.getCantidad() + pago.getRelacionItemInsumo().getCantidadAdjudicada());
                        pataPagoItem.setMontoTotal(pataPagoItem.getMontoTotal().add(pago.getRelacionItemInsumo().getMontoTotalAdjudicado()));
                        DataLoteItem dtpago = new DataLoteItem(DataLoteItem.INSUMO, pago);
                        TreeNode itemPago = new DefaultTreeNode(dtpago, itemNode);

                        if (pago.getCantRecibida() != null) {
                            pataPagoItem.setCantRecibida(pataPagoItem.getCantRecibida() + pago.getCantRecibida());
                        }
                        if (pago.getImporte() != null) {
                            pataPagoItem.setImporte(pataPagoItem.getImporte().add(pago.getImporte()));
                        }

                    }
                }
                if (pataPagoItem.getCantRecibida() != null && pataPagoItem.getCantRecibida() > 0) {
                    pataPagoItem.setImporteUnit(pataPagoItem.getImporte().divide(new BigDecimal(pataPagoItem.getCantRecibida()), 2, RoundingMode.HALF_UP));
                }

                /**
                 * ****Esto es para hacer la distribución "perfecta" en caso
                 * que el acta sea sin detalle (se realiza el 100% del
                 * pago)*****
                 */
                if ((tempActa.getConDetalle() == null || !tempActa.getConDetalle()) && tempActa.getMesPago() != null) {

                    BigDecimal relacionMontoActaConMontoTotalItems = pagoActa.divide(importeTotalTodosLosItems, 2, RoundingMode.HALF_UP);

                    //PARA LOS ITEMS
                    BigDecimal totalItemDistribuido = pataPagoItem.getMontoTotal().multiply(relacionMontoActaConMontoTotalItems);

                    pataPagoItem.setImporteUnit(pataPagoItem.getMontoUnitAdjudicado());
                    Integer cantidadItem = totalItemDistribuido.divide(pataPagoItem.getMontoUnitAdjudicado()).setScale(0, RoundingMode.HALF_DOWN).intValue();
                    if (cantidadItem < 0) {
                        cantidadItem = 0;
                    }
                    if (totalItemDistribuido.compareTo(pataPagoItem.getMontoUnitAdjudicado()) >= 0) {
                        pataPagoItem.setCantRecibida(cantidadItem);
                    } else {
                        pataPagoItem.setCantRecibida(0);
                    }
                    /*Obtuve la proporcion del monto del item, con eso la cantidad y ahora recalculo el monto en base a la cantidad y el monto unit*/
                    totalItemDistribuido = pataPagoItem.getMontoUnitAdjudicado().multiply(new BigDecimal(cantidadItem));

                    pataPagoItem.setImporte(totalItemDistribuido);
                    //Para el prorateo
                    totalRepartidoEnItems = totalRepartidoEnItems.add(totalItemDistribuido);

                    //Para el prorateo
                    listaDataPagosItems.add(pataPagoItem);

                    //PARA LOS INSUMOS
                    List<PagoInsumo> listaPagos = new LinkedList<PagoInsumo>();
                    BigDecimal totalRepartidoEnInsumos = BigDecimal.ZERO;
                    listaUltimosPagos = new LinkedList<PagoInsumo>();
                    for (PagoInsumo pago : tempActa.getPagosInsumo()) {
                        if (relacionesDelItem.contains(pago.getRelacionItemInsumo())) {
                            BigDecimal totalInsumoDistribuido = pago.getRelacionItemInsumo().getMontoTotalAdjudicado().multiply(relacionMontoActaConMontoTotalItems);

                            Integer cantidadInsumo = totalInsumoDistribuido.divide(pago.getRelacionItemInsumo().getMontoUnitAdjudicado()).setScale(0, RoundingMode.HALF_DOWN).intValue();
                            if (cantidadInsumo < 0) {
                                cantidadInsumo = 0;
                            }
                            if (totalItemDistribuido.compareTo(pataPagoItem.getMontoUnitAdjudicado()) >= 0) {
                                pago.setCantRecibida(cantidadInsumo);
                            } else {
                                pago.setCantRecibida(0);
                            }

                            totalInsumoDistribuido = pago.getRelacionItemInsumo().getMontoUnitAdjudicado().multiply(new BigDecimal(cantidadInsumo));
                            pago.setImporte(totalInsumoDistribuido);
                            //Para el prorateo
                            totalRepartidoEnInsumos = totalRepartidoEnInsumos.add(totalInsumoDistribuido);
                            listaPagos.add(pago);
                            listaUltimosPagos.add(pago);
                        }
                    }

                    //PRORATEO EN ULTIMO INSUMO
                    BigDecimal diferenciaImportes = pataPagoItem.getImporte().subtract(totalRepartidoEnInsumos);
                    Integer cantidadInsumo = 0;
                    for (int i = 0; i < listaPagos.size(); i++) {
                        PagoInsumo pagoUltimo = listaPagos.get(i);
                        if (i + 1 == listaPagos.size()) {
                            pagoUltimo.setImporte(pagoUltimo.getImporte().add(diferenciaImportes));
                            Integer difCantidades = pataPagoItem.getCantRecibida() - cantidadInsumo;
                            if (difCantidades >= 0) {
                                pagoUltimo.setCantRecibida(difCantidades);
                            } else {
                                pagoUltimo.setCantRecibida(0);
                            }
                        } else {
                            cantidadInsumo += pagoUltimo.getCantRecibida();
                        }
                    }

                }
            }

            if ((tempActa.getConDetalle() == null || !tempActa.getConDetalle()) && tempActa.getMesPago() != null) {
                //PRORATEO EN ULTIMO ITEM
                if (totalRepartidoEnItems.compareTo(pagoActa) != 0) {
                    BigDecimal diferenciaImportes = pagoActa.subtract(totalRepartidoEnItems);
                    BigDecimal importeTotalUltimoItem = BigDecimal.ZERO;
                    for (int i = 0; i < listaDataPagosItems.size(); i++) {
                        if (i + 1 == listaDataPagosItems.size()) {
                            DataPagoItem dpiUltimo = listaDataPagosItems.get(i);
                            dpiUltimo.setImporte(dpiUltimo.getImporte().add(diferenciaImportes));
                            if (dpiUltimo.getImporte().compareTo(BigDecimal.ZERO) < 0) {
                                dpiUltimo.setImporte(BigDecimal.ZERO);
                            }
                            importeTotalUltimoItem = dpiUltimo.getImporte();
                        }
                    }
                    /*En el ultimo pago vuelvo a ajustar el importe por el ajuste que se hizo en el ultimo item*/
                    BigDecimal totalUltimospagos = BigDecimal.ZERO;
                    for (int i = 0; i < listaUltimosPagos.size(); i++) {
                        PagoInsumo pago = listaUltimosPagos.get(i);
                        totalUltimospagos = totalUltimospagos.add(pago.getImporte());
                        if (i + 1 == listaUltimosPagos.size()) {
                            BigDecimal diferenciaSumaPagosEItem = importeTotalUltimoItem.subtract(totalUltimospagos);
                            pago.setImporte(pago.getImporte().add(diferenciaSumaPagosEItem));
                            if (pago.getImporte().compareTo(BigDecimal.ZERO) < 0) {
                                pago.setImporte(BigDecimal.ZERO);
                            }
                        }
                    }
                }
            }
        }
        return rootNodePagos;
    }

    /**
     * Distribuye y prorratea el monto y las cantidades de los insumos que
     * pertenecen a un ítem en un acta
     */
    public void distribuirItem() {
        try {
            DataPagoItem dataPagoItem = (DataPagoItem) itemADistribuir.getObjeto();
            Integer cantidadRecibidaItem = dataPagoItem.getCantRecibida();
            BigDecimal montoUnitarioRecibidoItem = dataPagoItem.getImporteUnit();
            BigDecimal montoTotalRecibidoItem = montoUnitarioRecibidoItem.multiply(new BigDecimal(cantidadRecibidaItem));

            if (cantidadRecibidaItem.compareTo(dataPagoItem.getCantidad()) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_CANTIDAD_RECIBIDA_ITEM_MAYOR_CANTIDAD_ADJUDICADA);
                throw b;
            }
            if (montoUnitarioRecibidoItem.compareTo(dataPagoItem.getMontoUnitAdjudicado()) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_MONTO_UNIT_RECIBIDO_ITEM_MAYOR_MONTO_UNIT_ADJUDICADO);
                throw b;
            }
            if (montoTotalRecibidoItem.compareTo(dataPagoItem.getMontoTotal()) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_MONTO_RECIBIDO_ITEM_MAYOR_MONTO_ADJUDICADO);
                throw b;
            }

            dataPagoItem.setImporte(montoTotalRecibidoItem);

            Integer cantidadSumadaDeInsumosEnDistribuccion = 0;
            BigDecimal montoSumadoDeInsumoEnDistribuccion = BigDecimal.ZERO;
            PagoInsumo ultimoPago = null;

            List<RelacionProAdqItemInsumo> relacionesDelItem = dataPagoItem.getItem().getRelItemInsumos();
            for (PagoInsumo pago : tempActa.getPagosInsumo()) {

                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición     
                if (relacionesDelItem.contains(pago.getRelacionItemInsumo())) {
                    float cantidadRecibidaInsumo = (pago.getRelacionItemInsumo().getCantidadAdjudicada() * cantidadRecibidaItem) / dataPagoItem.getCantidad().floatValue();
                    pago.setCantRecibida(Math.round(cantidadRecibidaInsumo));

                    BigDecimal importeRecibidoInsumo = pago.getRelacionItemInsumo().getMontoTotalAdjudicado().multiply(montoTotalRecibidoItem);
                    importeRecibidoInsumo = importeRecibidoInsumo.divide(dataPagoItem.getMontoTotal(), 2, RoundingMode.HALF_UP);
                    pago.setImporte(importeRecibidoInsumo);

                    //se suma la cantidades recibidas
                    cantidadSumadaDeInsumosEnDistribuccion = cantidadSumadaDeInsumosEnDistribuccion + pago.getCantRecibida();
                    montoSumadoDeInsumoEnDistribuccion = montoSumadoDeInsumoEnDistribuccion.add(pago.getImporte());
                    ultimoPago = pago;
                }
            }

            //se reorganizan los montos en el ultimo pago si alguno no da
            if (ultimoPago != null) {
                if (cantidadSumadaDeInsumosEnDistribuccion.compareTo(cantidadRecibidaItem) != 0) {
                    Integer diferencia = cantidadRecibidaItem - cantidadSumadaDeInsumosEnDistribuccion;
                    ultimoPago.setCantRecibida(ultimoPago.getCantRecibida() + diferencia);
                }
                if (montoSumadoDeInsumoEnDistribuccion.compareTo(montoTotalRecibidoItem) != 0) {
                    BigDecimal diferencia = montoTotalRecibidoItem.subtract(montoSumadoDeInsumoEnDistribuccion);
                    ultimoPago.setImporte(ultimoPago.getImporte().add(diferencia));
                }
            }

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Setea los montos y las cantidades de los pagos asociadas a un acta en
     * cero
     *
     * @param dataPagoItem
     */
    private void setearValoresActaEnCero(DataPagoItem dataPagoItem) {
        dataPagoItem.setCantRecibida(0);
        dataPagoItem.setImporteUnit(BigDecimal.ZERO);
        dataPagoItem.setImporte(BigDecimal.ZERO);
        List<RelacionProAdqItemInsumo> relacionesDelItem = dataPagoItem.getItem().getRelItemInsumos();
        for (PagoInsumo pago : tempActa.getPagosInsumo()) {
            if (relacionesDelItem.contains(pago.getRelacionItemInsumo())) {
                pago.setCantRecibida(0);
                pago.setImporte(BigDecimal.ZERO);
            }
        }
    }

    /**
     * Valida que la cantidad de un insumo ingresada en un acta no supere la
     * cantidad adjudicada del mismo
     *
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     */
    public void validarCantidadInsumo(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value != null && !TextUtils.isEmpty(value.toString())) {
            Integer cantidadIngresada = Integer.valueOf(value.toString());
            Integer idRelacion = (Integer) component.getAttributes().get("idRelacion");

            //busca el insumo con el que se esta tratando
            RelacionProAdqItemInsumo relacionItemInsumo = null;
            Iterator<PagoInsumo> iterator = tempActa.getPagosInsumo().iterator();
            while (iterator.hasNext() && relacionItemInsumo == null) {
                PagoInsumo pagoInsumo = iterator.next();
                if (pagoInsumo.getRelacionItemInsumo().getId().equals(idRelacion)) {
                    relacionItemInsumo = pagoInsumo.getRelacionItemInsumo();
                }
            }

            //suma como quedarian las cantidades
            int cantidadTotal = cantidadIngresada;

            for (ActaContrato pago : objeto.getPagos()) {
                if (!pago.equals(tempActa)) {
                    for (PagoInsumo pagoInsumo : pago.getPagosInsumo()) {
                        if (pagoInsumo.getRelacionItemInsumo().getId().equals(idRelacion)) {
                            cantidadTotal = cantidadTotal + pagoInsumo.getCantRecibida();
                        }
                    }
                }
            }
            /*Cambios de adjudicacion*/
            if (cantidadTotal > relacionItemInsumo.getCantidadAdjudicada().intValue()) {
                FacesMessage msg = new FacesMessage("Error al validar la cantidad del insumo", "Error: la suma de las cantidades de los pagos por el insumo ( " + cantidadTotal + "), sobrepasa la cantidad contratada (" + relacionItemInsumo.getCantidadAdjudicada() + ")");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }

    /**
     * Valida que el monto de un insumo ingresado en un acta no supere la
     * cantidad adjudicada del mismo
     *
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     */
    public void validarMontoTotalInsumo(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value != null && !TextUtils.isEmpty(value.toString())) {
            BigDecimal montoIngresado = new BigDecimal(value.toString());
            Integer idRelacion = (Integer) component.getAttributes().get("idRelacion");

            //busca el insumo con el que se esta tratando
            RelacionProAdqItemInsumo relacionItemInsumo = null;
            Iterator<PagoInsumo> iterator = tempActa.getPagosInsumo().iterator();
            while (iterator.hasNext() && relacionItemInsumo == null) {
                PagoInsumo pagoInsumo = iterator.next();
                if (pagoInsumo.getRelacionItemInsumo().getId().equals(idRelacion)) {
                    relacionItemInsumo = pagoInsumo.getRelacionItemInsumo();
                }
            }

            //suma como quedarian las cantidades
            BigDecimal montoTotal = montoIngresado;

            for (ActaContrato pago : objeto.getPagos()) {
                if (!pago.equals(tempActa)) {
                    for (PagoInsumo pagoInsumo : pago.getPagosInsumo()) {
                        if (pagoInsumo.getRelacionItemInsumo().getId().equals(idRelacion)) {
                            montoTotal = montoTotal.add(pagoInsumo.getImporte());
                        }
                    }
                }
            }
            /*Cambios de adjudicacion*/
            if (montoTotal.compareTo(relacionItemInsumo.getMontoTotalAdjudicado()) > 0) {
                FacesMessage msg = new FacesMessage("Error al validar el monto total del insumo", "Error: la suma de los montos de los pagos por el insumo ( " + montoTotal + "), sobrepasa el monto total contratado (" + relacionItemInsumo.getMontoTotalAdjudicado() + ")");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }

    /**
     * Guarda un acta y la asocia a un Contrato / OC
     */
    public void guardarPago() {
        try {
            contratoDelegate.guardarPago(tempActa, objeto.getId());
            recargarObjeto();
            RequestContext.getCurrentInstance().execute("$('#anadirPago').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina un acta y la desasocia de un Contrato / OC
     *
     * @param pago
     */
    public void eliminarPago(ActaContrato pago) {
        try {
            contratoDelegate.eliminarPago(pago.getId());
            recargarObjeto();
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Devuelve todos los insumos que están en los ítems de un contrato
     *
     * @return
     */
    public LinkedList<ProcesoAdquisicionInsumo> getInsumos() {
        LinkedList<ProcesoAdquisicionInsumo> l = new LinkedList();
        for (ProcesoAdquisicionItem item : objeto.getItems()) {
            //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                ProcesoAdquisicionInsumo padqinsumo = rel.getInsumo();
                l.add(padqinsumo);
            }
        }
        return l;
    }

    /**
     * Devuelve las relaciones entre ítem e insumo que tienen los ítems que
     * están en un contrato
     *
     * @return
     */
    public LinkedList<RelacionProAdqItemInsumo> cargarRelacionesItemInsumos() {
        LinkedList<RelacionProAdqItemInsumo> l = new LinkedList();
        for (ProcesoAdquisicionItem item : objeto.getItems()) {
            l.addAll(item.getRelItemInsumos());
        }
        return l;
    }

    /**
     * Devuelve la suma de todas las cantidades recibidas asociadas a los pagos
     * que están en las actas de recepción aprobadas
     *
     * @param relacionItemInsumo
     * @return
     */
    public Integer getCantidadRecibidaInsumo(RelacionProAdqItemInsumo relacionItemInsumo) {
        Integer recibido = 0;
        if (relacionItemInsumo != null) {
            for (ActaContrato acta : objeto.getPagos()) {
                for (PagoInsumo pago : acta.getPagosInsumo()) {
                    if (acta.getTipo().equals(TipoActaContrato.RECEPCION) && acta.getEstado().equals(EstadoActa.APROBADA)) {
                        if (pago.getRelacionItemInsumo().getId().equals(relacionItemInsumo.getId())) {
                            recibido = recibido + pago.getCantRecibida();
                        }
                    }
                }
            }
        }
        return recibido;
    }

    /**
     * Devuelve la suma de todos los montos pagados asociados a los pagos que
     * están en las actas de recepción aprobadas
     *
     * @param relacionItemInsumo
     * @return
     */
    public BigDecimal getMontoPagado(RelacionProAdqItemInsumo relacionItemInsumo) {
        BigDecimal pagado = BigDecimal.ZERO;
        if (relacionItemInsumo != null) {
            for (ActaContrato acta : objeto.getPagos()) {
                for (PagoInsumo pago : acta.getPagosInsumo()) {
                    if (acta.getTipo().equals(TipoActaContrato.RECEPCION) && acta.getEstado().equals(EstadoActa.APROBADA)) {
                        if (pago.getRelacionItemInsumo().getId().equals(relacionItemInsumo.getId())) {
                            pagado = pagado.add(pago.getImporte());
                        }
                    }
                }
            }
        }
        return pagado;
    }

    /**
     * Devuelve el porcentaje correspondiente al monto recibido en actas de
     * recepción
     *
     * @param estimado
     * @param actual
     * @return
     */
    public BigDecimal getPorcentaje(BigDecimal estimado, BigDecimal actual) {
        BigDecimal porc = BigDecimal.ZERO;
        BigDecimal porcentaje = BigDecimal.ZERO;
        if (!estimado.equals(BigDecimal.ZERO)) {
            porcentaje = actual.multiply(new BigDecimal(100));
            porc = porcentaje.divide(estimado, 2, RoundingMode.HALF_UP);
        } else {
            porc = BigDecimal.ZERO;
        }
        return porc;
    }

    /**
     * Devuelve el porcentaje correspondiente al monto recibido en actas de
     * recepción
     *
     * @param estimadoI
     * @param actualI
     * @return
     */
    public BigDecimal getPorcentaje(Integer estimadoI, Integer actualI) {
        BigDecimal porc = BigDecimal.ZERO;
        BigDecimal actual = new BigDecimal(actualI);
        BigDecimal estimado = new BigDecimal(estimadoI);
        BigDecimal porcentaje = actual.multiply(new BigDecimal(100));
        if (!estimado.equals(BigDecimal.ZERO)) {
            porc = porcentaje.divide(estimado, 2, RoundingMode.HALF_UP);
        }
        return porc;
    }

    //private static final String COD_TEXT_PLANIFICADO = "FCM_Planificado";
    private static final String COD_TEXT_REAL_PAGOS = "FCM_3_Deducido_Pagos";
    private static final String COD_TEXT_PAC = "FCM_1_PAC_Planificado";
    private static final String COD_TEXT_PRO_ADQ = "FCM_2_Ppro_Adq_Planificado";

    /**
     * Limpia los filtros de los flujos de caja mensual de un Contrato / OC
     */
    public void limpiarFiltroFCM() {
        filtroFCMCodrigoInsumo = null;
        filtroFCMAnio = null;
        filtroFCMTipo = null;
    }

    /**
     * Inicializa el flujo de caja mensual de un Contrato / OC
     *
     * @return
     */
    private List<POFlujoCajaMenusal> initFlujoCajaTotal() {
        List<POFlujoCajaMenusal> total = new LinkedList();
        for (int i = 1; i <= 12; i++) {
            POFlujoCajaMenusal f = new POFlujoCajaMenusal();
            f.setMes(i);
            f.setMonto(BigDecimal.ZERO);
            total.add(f);
        }
        return total;
    }

    /**
     * Devuelve una lista de meses
     *
     * @return
     */
    public List<String> getMeses() {
        return TipoSeguimientoUtils.getListName(TipoSeguimiento.MENSUAL);
    }

    /**
     * Devuelve el monto correspondiente a un mes especifico del flujo de caja
     * mensual
     *
     * @param item
     * @param index
     * @return
     */
    public BigDecimal getValorFCM(InsumoContratoFlujoCajaMensual item, int index) {
        if (item.getFlujoCajaMenusalInsumo().size() <= index) {
            return null;
        }
        return item.getFlujoCajaMenusalInsumo().get(index).getMonto();
    }

    /**
     * Carga el combo de usuarios que pueden crear o editar un Contrato / OC
     */
    public void initUsuarios() {
        List<SsUsuario> l = usuarioDelegate.getUsuariosConOperacion(ConstantesEstandares.OPERACION_CREAR_EDITAR_CONTRATO_OC);
        comboUsuarios = new SofisComboG<>(l, "usuCod");
        comboUsuarios.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        comboUsuarios.setSelectedT(objeto.getAdministrador());
    }

    /**
     * Carga el combo de usuarios que pueden firmar un Contrato / OC
     */
    public void initUsuariosFirmantes() {
        List<SsUsuario> l = usuarioDelegate.getUsuariosConOperacion(ConstantesEstandares.OPERACION_FIRMAR_CONTRATO_OC);
        comboUsuariosFirmantes = new SofisComboG<>(l, "usuCod");
        comboUsuariosFirmantes.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        comboUsuariosFirmantes.setSelectedT(objeto.getFirmanteOrdenInicio());
    }

    /**
     * Genera y descarga el reporte de anexo de documento de compra
     */
    public void generarAnexoDocumentoCompra() {
        byte[] bytespdf = reporteDelegate.generarDocumentoCompra(objeto.getId());
        FacesContext fc = FacesContext.getCurrentInstance();

        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
        ec.setResponseContentType("application/pdf");
        ec.setResponseContentLength(bytespdf.length);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "AnexoDocumentoCompra.pdf" + "\"");
        OutputStream output;
        try {
            output = ec.getResponseOutputStream();
            output.write(bytespdf);

        } catch (IOException ex) {
            Logger.getLogger(ContratoOrdenCompra.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        fc.responseComplete();

    }

    /**
     * Genera y descarga el reporte de documento de orden de compra
     */
    public void generarReporteDocumentoOrdenCompra() {
        byte[] bytespdf = reporteDelegate.generarDocumentoOrdenCompra(objeto.getId());
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
        ec.setResponseContentType("application/pdf");
        ec.setResponseContentLength(bytespdf.length);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "OrdenDeCompra.pdf" + "\"");
        OutputStream output;
        try {
            output = ec.getResponseOutputStream();
            output.write(bytespdf);

        } catch (IOException ex) {
            Logger.getLogger(ContratoOrdenCompra.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        fc.responseComplete();
    }

    /**
     * Genera y descarga el reporte de acta de recepción
     *
     * @param actaId
     */
    public void generarReporteActaDeRecepcion(Integer actaId) {
        try {
            byte[] bytespdf = reporteDelegate.generarActaDeRecepcion(actaId);
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("application/pdf");
            ec.setResponseContentLength(bytespdf.length);

            ActaContrato acta = contratoDelegate.cargarPago(actaId);
            String nroActa = acta.getContratoOC().getId() + "_" + acta.getNroActa();
            String fileName = "actaRecepcion_" + nroActa + ".pdf";

            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            OutputStream output;
            try {
                output = ec.getResponseOutputStream();
                output.write(bytespdf);

            } catch (IOException ex) {
                Logger.getLogger(ContratoOrdenCompra.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            fc.responseComplete();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Genera y descarga el reporte de acta de anticipo
     *
     * @param actaId
     */
    public void generarReporteActaDeAnticipo(Integer actaId) {
        try {
            byte[] bytespdf = reporteDelegate.generarActaDeAnticipo(actaId);
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("application/pdf");
            ec.setResponseContentLength(bytespdf.length);

            ActaContrato acta = contratoDelegate.cargarPago(actaId);
            String nroActa = acta.getContratoOC().getId() + "_" + acta.getNroActa();
            String fileName = "actaAnticipo_" + nroActa + ".pdf";

            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            OutputStream output;
            try {
                output = ec.getResponseOutputStream();
                output.write(bytespdf);

            } catch (IOException ex) {
                Logger.getLogger(ContratoOrdenCompra.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            fc.responseComplete();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Genera y descarga el reporte de acta de devolución
     *
     * @param actaId
     */
    public void generarReporteActaDeDevolucion(Integer actaId) {
        try {
            byte[] bytespdf = reporteDelegate.generarActaDeDevolucion(actaId);
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("application/pdf");
            ec.setResponseContentLength(bytespdf.length);

            ActaContrato acta = contratoDelegate.cargarPago(actaId);
            String nroActa = acta.getContratoOC().getId() + "_" + acta.getNroActa();
            String fileName = "actaDevolucion_" + nroActa + ".pdf";

            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            OutputStream output;
            try {
                output = ec.getResponseOutputStream();
                output.write(bytespdf);

            } catch (IOException ex) {
                Logger.getLogger(ContratoOrdenCompra.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            fc.responseComplete();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Verifica que el contrato / OC se puede cerrar
     *
     * @return
     */
    public boolean sePuedeCerrar() {
        return contratoDelegate.sePuedeCerrarContrato(objeto);
    }

    /**
     * Carga en un texto los Impuestos asociados a un Contrato / OC
     */
    private void cargarImpuestosAplica() {
        impuestosAplica = "";
        if (objeto.getImpuestos().size() == 0) {
            impuestosAplica = "No aplican impuestos";
        } else if (objeto.getImpuestos().size() == 1) {
            impuestosAplica = objeto.getImpuestos().get(0).getNombre();
        } else {
            for (int i = 0; i < objeto.getImpuestos().size(); i++) {
                Impuesto imp = objeto.getImpuestos().get(i);
                if (i != objeto.getImpuestos().size() - 1) {
                    impuestosAplica += imp.getNombre() + ", ";
                } else {
                    impuestosAplica += imp.getNombre();
                }

            }
        }
    }

    /**
     * Permite cargar determinado archivo
     *
     * @param event
     */
    public void fileUploadListener(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            if (uploadedFile != null) {
                objeto.setTempUploadedFile(ArchivoUtils.getDataFile(uploadedFile));
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Asocia un archivo de orden de inicio a un Contrato / OC
     */
    public void guardarOrdenInicioFirmada() {
        try {
            if (objeto.getTempUploadedFile() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_ARCHIVO_ORDEN_INICIO_VACIO);
                throw b;
            }

            objeto = contratoDelegate.saveArchivoOrdenInicio(objeto);
            guardar();
            objeto.setTempUploadedFile(null);
            uploadedFile = null;
            RequestContext.getCurrentInstance().execute("$('#anadirOrdenInicio').modal('hide');");
            RequestContext.getCurrentInstance().execute("$('#verOrdenInicio').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Permite descargar determinado archivo
     *
     * @param archivo
     */
    public void downloadFile(Archivo archivo) {
        try {
            ArchivoUtils.downloadFile(archivo, archivoDelegate.getFile(archivo));
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Si las fechas ingresadas en el contrato son correctas, abre el pop-up
     * para cargar la orden de inicio firmada
     */
    public void cargarOrdenInicio() {
        try {
            recargarObjeto();
            BusinessException b = new BusinessException();
            /*No lo dejo cargar la orden de inicio si las fechas están mal, porque una vez que suba el archivo, los campos quedan readonly*/
            if (objeto.getFechasDesdeOrdenInicio()) {
                //La fecha de fin debe ser mayor o igual a la fecha de inicio
                if (objeto.getFechaFin().compareTo(objeto.getFechaInicio()) < 0) {
                    b.addError(ConstantesErrores.ERR_FECHA_FIN_ORDEN_INICIO_MENOR_FECHA_INICIO);
                }
                //La fecha de inicio debe ser mayor o igual a la fecha de emisión del contrato/orden de compra
                if (objeto.getFechaInicio().compareTo(objeto.getFechaEmision()) < 0) {
                    b.addError(ConstantesErrores.ERR_FECHA_INICIO_ORDEN_INICIO_MENOR_FECHA_EMISION);
                }
            }
            if (b.getErrores().size() > 0) {
                throw b;
            }
            RequestContext.getCurrentInstance().execute("$('#anadirOrdenInicio').modal('show');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("mainPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("mainPanel");
        }
    }

    /**
     * Abre el pop-up para descargar la orden de inicio
     */
    public void verOrdenInicio() {
        RequestContext.getCurrentInstance().execute("$('#verOrdenInicio').modal('show');");
    }

    /**
     * Habilita o deshabilita la carga de orden de inicio
     */
    public void modificarOrdenInicio() {
        if (comboUsuarios != null && comboUsuarios.getSelectedT() != null
                && (objeto.getFechaEmision() != null)
                && (objeto.getFechaInicio() != null)
                && (objeto.getFechaFin() != null)
                && comboUsuariosFirmantes.getSelectedT() != null) {
            if (!objeto.getFechaEmision().toString().isEmpty()
                    && !objeto.getFechaInicio().toString().isEmpty()
                    && !objeto.getFechaFin().toString().isEmpty()) {
                modificandoOrdenInicio = true;
            }
        } else {
            modificandoOrdenInicio = false;
        }

    }

    /**
     * Devuelve un Map que contiene la descripción de los pagos que se asocian a
     * las actas y el id del Flujo de caja mensual
     *
     * @return
     */
    public HashMap<String, String> cargarPagosAnioMes() {
        HashMap<String, String> mapFlujoCajaAniosMeses = new HashMap<String, String>();
        for (FlujoCajaAnio fca : objeto.getProgramacionPagosContrato()) {
            for (int i = 0; i < fca.getFlujoCajaMenusal().size(); i++) {
                POFlujoCajaMenusal fcm = fca.getFlujoCajaMenusal().get(i);
                if (fcm.getMonto().compareTo(BigDecimal.ZERO) > 0) {
                    String mes = UtilsMB.getTituloSeguimiento(TipoSeguimiento.MENSUAL, i);
                    String anio = fca.getAnio().toString();
                    String monto = NumberUtils.nomberToString(fcm.getMonto());
                    String codigoMoneda = textMB.obtenerTexto("labels.MonedaSistema");
                    String descripcionPago = mes + " - " + anio + " - " + codigoMoneda + " " + monto;
                    mapFlujoCajaAniosMeses.put(descripcionPago, String.valueOf(fcm.getId()));
                    //Asocio el pago seleccionado en el acta con el monto del mismo
                }
            }
        }
        return TextUtils.ordenarMapDeStringByValue(mapFlujoCajaAniosMeses);
    }

    /**
     * Hace que se muestre la tabla de pagos de ítems
     *
     * @return
     */
    public Boolean mostrarOcultarTablaPagoItems() {
        return true;
    }

    /**
     * Devuelve la suma de las cantidades ingresadas en los pagos asociados a un
     * acta de contrato / OC
     *
     * @param acta
     * @return
     */
    public Integer calcularCantidadTotalRecibidaActa(ActaContrato acta) {
        Integer cantidadRecibidaTotal = 0;
        for (PagoInsumo pago : acta.getPagosInsumo()) {
            if (pago.getCantRecibida() != null) {
                cantidadRecibidaTotal += pago.getCantRecibida();
            }
        }
        return cantidadRecibidaTotal;
    }

    /**
     * Devuelve la suma de los montos ingresadas en los pagos asociados a un
     * acta de contrato / OC
     *
     * @param acta
     * @return
     */
    public BigDecimal calcularImporteTotalRecibidoActa(ActaContrato acta) {
        BigDecimal importeRecibidoTotal = BigDecimal.ZERO;
        for (PagoInsumo pago : acta.getPagosInsumo()) {
            if (pago.getImporte() != null) {
                importeRecibidoTotal = importeRecibidoTotal.add(pago.getImporte());
            }
        }
        return importeRecibidoTotal;
    }

    /**
     * Actualiza la suma de las cantidades y los montos ingresados en los pagos
     * asociados a un acta de contrato / OC
     */
    public void actualizarCantidadEImporteRecibidosItem() {
        for (TreeNode nodo : rootNodePagos.getChildren()) {
            DataPagoItem dataPagoItem = null;
            DataLoteItem dataLoteItem = (DataLoteItem) nodo.getData();
            //Recorro los nodos y paro cunado encuentro un item
            if (dataLoteItem.getTipo().equals(DataLoteItem.ITEM)) {
                dataPagoItem = (DataPagoItem) dataLoteItem.getObjeto();

                Integer cantidadRecibidaTotalItem = 0;
                BigDecimal importeRecibidoTotalItem = BigDecimal.ZERO;
                //Si el item del pago es igual al item que encontr en el nodo, sumo sus cantidades
                for (PagoInsumo pago : tempActa.getPagosInsumo()) {
                    if (pago.getRelacionItemInsumo().getItem().getId().equals(dataPagoItem.getItem().getId())) {
                        if (pago.getCantRecibida() != null) {
                            cantidadRecibidaTotalItem += pago.getCantRecibida();
                        }
                        if (pago.getImporte() != null) {
                            importeRecibidoTotalItem = importeRecibidoTotalItem.add(pago.getImporte());
                        }
                    }
                }
                dataPagoItem.setCantRecibida(cantidadRecibidaTotalItem);
                dataPagoItem.setImporte(importeRecibidoTotalItem);
                if (cantidadRecibidaTotalItem > 0) {
                    dataPagoItem.setImporteUnit(importeRecibidoTotalItem.divide(new BigDecimal(cantidadRecibidaTotalItem), 2, RoundingMode.HALF_UP));
                } else {
                    dataPagoItem.setImporteUnit(BigDecimal.ZERO);
                }
            }

        }
    }

    /**
     * Distribuye montos y cantidades en los insumos de los ítems del acta en
     * edición de acuerdo al pago seleccionado
     */
    public void cargarDatosActaConSinDetalle() {
        BigDecimal pagoActa = BigDecimal.ZERO;
        if (!TextUtils.isEmpty(idPOFlujoCajaMenusal)) {
            for (FlujoCajaAnio fca : objeto.getProgramacionPagosContrato()) {
                for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                    if (fcm.getId().equals(Integer.valueOf(idPOFlujoCajaMenusal))) {
                        pagoActa = fcm.getMonto();
                        tempActa.setAnioPago(fca);
                        tempActa.setMesPago(fcm);
                    }
                }
            }
        }
        BigDecimal importeTotalTodosLosItems = BigDecimal.ZERO;

        Integer cantidadTotalTodosLosItems = 0;
        for (ProcesoAdquisicionItem item : objeto.getItems()) {
            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                importeTotalTodosLosItems = importeTotalTodosLosItems.add(rel.getMontoTotalAdjudicado());
                cantidadTotalTodosLosItems += rel.getCantidadAdjudicada();
            }
        }

        //Con el siguiente valor se hacen los prorateos en el último item
        BigDecimal totalRepartidoEnItems = BigDecimal.ZERO;
        List<DataPagoItem> listaDataPagosItems = new LinkedList<DataPagoItem>();
        List<PagoInsumo> listaUltimosPagos = null;

        if ((tempActa.getConDetalle() == null || !tempActa.getConDetalle()) && tempActa.getMesPago() != null) {

            for (TreeNode nodo : rootNodePagos.getChildren()) {
                DataPagoItem pataPagoItem = null;
                DataLoteItem dataLoteItem = (DataLoteItem) nodo.getData();
                List<RelacionProAdqItemInsumo> relacionesDelItem = new LinkedList<RelacionProAdqItemInsumo>();

                if (dataLoteItem.getTipo().equals(DataLoteItem.ITEM)) {
                    pataPagoItem = (DataPagoItem) dataLoteItem.getObjeto();
                    relacionesDelItem = pataPagoItem.getItem().getRelItemInsumos();

                    BigDecimal relacionMontoActaConMontoTotalItems = pagoActa.divide(importeTotalTodosLosItems, 2, RoundingMode.HALF_UP);

                    //PARA LOS ITEMS
                    BigDecimal totalItemDistribuido = pataPagoItem.getMontoTotal().multiply(relacionMontoActaConMontoTotalItems);

                    pataPagoItem.setImporteUnit(pataPagoItem.getMontoUnitAdjudicado());
                    Integer cantidadItem = totalItemDistribuido.divide(pataPagoItem.getMontoUnitAdjudicado()).setScale(0, RoundingMode.HALF_DOWN).intValue();
                    if (cantidadItem < 0) {
                        cantidadItem = 0;
                    }
                    if (totalItemDistribuido.compareTo(pataPagoItem.getMontoUnitAdjudicado()) >= 0) {
                        pataPagoItem.setCantRecibida(cantidadItem);
                    } else {
                        pataPagoItem.setCantRecibida(0);
                    }

                    totalItemDistribuido = pataPagoItem.getMontoUnitAdjudicado().multiply(new BigDecimal(cantidadItem));
                    //Para el prorateo
                    totalRepartidoEnItems = totalRepartidoEnItems.add(totalItemDistribuido);

                    pataPagoItem.setImporte(totalItemDistribuido);

                    //Para el prorateo
                    listaDataPagosItems.add(pataPagoItem);

                    //PARA LOS INSUMOS
                    List<PagoInsumo> listaPagos = new LinkedList<PagoInsumo>();
                    BigDecimal totalRepartidoEnInsumos = BigDecimal.ZERO;
                    listaUltimosPagos = new LinkedList<PagoInsumo>();
                    for (PagoInsumo pago : tempActa.getPagosInsumo()) {
                        if (relacionesDelItem.contains(pago.getRelacionItemInsumo())) {
                            BigDecimal totalInsumoDistribuido = pago.getRelacionItemInsumo().getMontoTotalAdjudicado().multiply(relacionMontoActaConMontoTotalItems);

                            Integer cantidadInsumo = totalInsumoDistribuido.divide(pago.getRelacionItemInsumo().getMontoUnitAdjudicado()).setScale(0, RoundingMode.HALF_DOWN).intValue();
                            if (cantidadInsumo < 0) {
                                cantidadInsumo = 0;
                            }
                            if (totalItemDistribuido.compareTo(pataPagoItem.getMontoUnitAdjudicado()) >= 0) {
                                pago.setCantRecibida(cantidadInsumo);
                            } else {
                                pago.setCantRecibida(0);
                            }

                            totalInsumoDistribuido = pago.getRelacionItemInsumo().getMontoUnitAdjudicado().multiply(new BigDecimal(cantidadInsumo));
                            pago.setImporte(totalInsumoDistribuido);
                            //Para el prorateo
                            totalRepartidoEnInsumos = totalRepartidoEnInsumos.add(totalInsumoDistribuido);
                            listaPagos.add(pago);
                            listaUltimosPagos.add(pago);
                        }
                    }

                    //PRORATEO EN ULTIMO INSUMO
                    BigDecimal diferenciaImportes = pataPagoItem.getImporte().subtract(totalRepartidoEnInsumos);
                    Integer cantidadInsumo = 0;
                    for (int i = 0; i < listaPagos.size(); i++) {
                        PagoInsumo pagoUltimo = listaPagos.get(i);
                        if (i + 1 == listaPagos.size()) {
                            pagoUltimo.setImporte(pagoUltimo.getImporte().add(diferenciaImportes));
                            Integer difCantidades = pataPagoItem.getCantRecibida() - cantidadInsumo;
                            if (difCantidades >= 0) {
                                pagoUltimo.setCantRecibida(difCantidades);
                            } else {
                                pagoUltimo.setCantRecibida(0);
                            }
                        } else {
                            cantidadInsumo += pagoUltimo.getCantRecibida();
                        }
                    }

                }
            }
            //PRORATEO EN ULTIMO ITEM
            if (totalRepartidoEnItems.compareTo(pagoActa) != 0) {
                BigDecimal diferenciaImportes = pagoActa.subtract(totalRepartidoEnItems);
                BigDecimal importeTotalUltimoItem = BigDecimal.ZERO;
                for (int i = 0; i < listaDataPagosItems.size(); i++) {
                    if (i + 1 == listaDataPagosItems.size()) {
                        DataPagoItem dpiUltimo = listaDataPagosItems.get(i);
                        dpiUltimo.setImporte(dpiUltimo.getImporte().add(diferenciaImportes));
                        if (dpiUltimo.getImporte().compareTo(BigDecimal.ZERO) < 0) {
                            dpiUltimo.setImporte(BigDecimal.ZERO);
                        }
                        importeTotalUltimoItem = dpiUltimo.getImporte();
                    }
                }
                /*En el ultimo pago vuelvo a ajustar el importe por el ajuste que se hizo en el ultimo item*/
                BigDecimal totalUltimospagos = BigDecimal.ZERO;
                for (int i = 0; i < listaUltimosPagos.size(); i++) {
                    PagoInsumo pago = listaUltimosPagos.get(i);
                    totalUltimospagos = totalUltimospagos.add(pago.getImporte());
                    if (i + 1 == listaUltimosPagos.size()) {
                        BigDecimal diferenciaSumaPagosEItem = importeTotalUltimoItem.subtract(totalUltimospagos);
                        pago.setImporte(pago.getImporte().add(diferenciaSumaPagosEItem));
                        if (pago.getImporte().compareTo(BigDecimal.ZERO) < 0) {
                            pago.setImporte(BigDecimal.ZERO);
                        }
                    }
                }
            }
        }
    }

    /**
     * Al acta pasada por parámetro se le asigna el estado: Aprobada y se genera
     * un número de solicitud de pago
     *
     * @param acta
     */
    public void aprobarActa(ActaContrato acta) {
        try {
            contratoDelegate.aprobarActa(acta.getId());
            recargarObjeto();
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBackConParametros').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Revierte un acta, quedando en estado: Revertida
     *
     * @param acta
     */
    public void revertirActa(ActaContrato acta) {
        try {
            contratoDelegate.revertirActa(acta.getId());
            recargarObjeto();
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBackConParametros').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Para un acta de devolución o de anticipo, se calcula el porcentaje en
     * base al monto fijo ingresado o viceversa
     */
    public void calcularPorcentajeMontoFijo() {
        if (tempActa.getTipoPago() != null) {
            if (tempActa.getTipoPago().equals(TipoPagoActa.PORCENTAJE)) {
                if (tempActa.getPorcentaje() != null) {
                    BigDecimal montoFijo = NumberUtils.porcentaje(tempActa.getPorcentaje(), objeto.getMontoAdjudicado(), RoundingMode.DOWN);
                    tempActa.setMontoRecibido(montoFijo);
                } else {
                    tempActa.setPorcentaje(BigDecimal.ZERO);
                    tempActa.setMontoRecibido(BigDecimal.ZERO);
                }
            } else if (tempActa.getTipoPago().equals(TipoPagoActa.MONTO_FIJO)) {
                if (tempActa.getMontoRecibido() != null) {
                    BigDecimal porcentaje = tempActa.getMontoRecibido().multiply(new BigDecimal(100));
                    porcentaje = porcentaje.divide(objeto.getMontoAdjudicado(), 2, RoundingMode.DOWN);
                    tempActa.setPorcentaje(porcentaje);
                } else {
                    tempActa.setPorcentaje(BigDecimal.ZERO);
                    tempActa.setMontoRecibido(BigDecimal.ZERO);
                }
            }
        } else {
            tempActa.setPorcentaje(BigDecimal.ZERO);
            tempActa.setMontoRecibido(BigDecimal.ZERO);
        }
    }

    /**
     * Evalúa si el monto ejecutado y el adjudicado en un Contrato / OC son
     * iguales
     *
     * @return
     */
    public boolean montoAdjIgualMontoEjec() {
        return objeto.getMontoAdjudicado().equals(objeto.getMontoEjecutado());
    }

    /**
     * Evalúa si el monto de anticipo en un acta es igual al monto de anticipo
     * del contrato / OC
     *
     * @return
     */
    public boolean montoActaAnticipoIgualPorcentajeAnticipoContrato() {
        boolean sePuedeCerrar = true;
        if (objeto.getPorcentajeAnticipo() > 0) {
            Iterator<ActaContrato> itActas = objeto.getPagos().iterator();
            boolean hayActaAnticipoAprobada = false;
            while (itActas.hasNext() && !hayActaAnticipoAprobada) {
                ActaContrato acta = itActas.next();
                if (acta.getQuedanEmitido() != null) {
                    if (acta.getTipo().equals(TipoActaContrato.ANTICIPO) && acta.getEstado().equals(EstadoActa.APROBADA) && acta.getQuedanEmitido()) {
                        if (acta.getPorcentaje().compareTo(new BigDecimal(objeto.getPorcentajeAnticipo())) == 0) {
                            hayActaAnticipoAprobada = true;
                        }
                    }
                }
            }
            if (!hayActaAnticipoAprobada) {
                sePuedeCerrar = false;
            }
        }
        return sePuedeCerrar;
    }

    /**
     * Devuelve el porcentaje de anticipo de un acta de anticipo aprobada
     *
     * @return
     */
    public BigDecimal getPorcentajeAnticipoActaAprobada() {
        Iterator<ActaContrato> itActas = objeto.getPagos().iterator();
        boolean hayActaAnticipoAprobada = false;
        BigDecimal porcentaje = BigDecimal.ZERO;
        while (itActas.hasNext() && !hayActaAnticipoAprobada) {
            ActaContrato acta = itActas.next();
            if (acta.getTipo().equals(TipoActaContrato.ANTICIPO) && acta.getEstado().equals(EstadoActa.APROBADA)) {
                hayActaAnticipoAprobada = true;
                porcentaje = acta.getPorcentaje();
            }
        }
        return porcentaje;
    }

    /**
     * Evalúa si el monto de devolución en un acta es igual al monto de
     * devolución del contrato / OC
     *
     * @return
     */
    public boolean montoActaDevolucionIgualPorcentajeDevolucionContrato() {
        boolean sePuedeCerrar = true;
        if (objeto.getPorcentajeDevolucion() > 0) {
            Iterator<ActaContrato> itActas = objeto.getPagos().iterator();
            boolean hayActaDevolucionAprobada = false;
            while (itActas.hasNext() && !hayActaDevolucionAprobada) {
                ActaContrato acta = itActas.next();
                if (acta.getQuedanEmitido() != null) {
                    if (acta.getTipo().equals(TipoActaContrato.DEVOLUCION) && acta.getEstado().equals(EstadoActa.APROBADA) && acta.getQuedanEmitido()) {
                        if (acta.getPorcentaje().compareTo(new BigDecimal(objeto.getPorcentajeDevolucion())) == 0) {
                            hayActaDevolucionAprobada = true;
                        }
                    }
                }
            }
            if (!hayActaDevolucionAprobada) {
                sePuedeCerrar = false;
            }
        }
        return sePuedeCerrar;
    }

    /**
     * Devuelve el porcentaje de devolución de un acta de devolución aprobada
     *
     * @return
     */
    public BigDecimal getPorcentajeDevolucionActaAprobada() {
        Iterator<ActaContrato> itActas = objeto.getPagos().iterator();
        boolean hayActaDevolucionAprobada = false;
        BigDecimal porcentaje = BigDecimal.ZERO;
        while (itActas.hasNext() && !hayActaDevolucionAprobada) {
            ActaContrato acta = itActas.next();
            if (acta.getTipo().equals(TipoActaContrato.DEVOLUCION) && acta.getEstado().equals(EstadoActa.APROBADA)) {
                hayActaDevolucionAprobada = true;
                porcentaje = acta.getPorcentaje();
            }
        }
        return porcentaje;
    }

    /**
     * Verifica si un acta de anticipo aprobada tiene Quedan emitido
     *
     * @return
     */
    public Boolean montoActaAnticipoTieneQuedanEmitido() {
        boolean tieneQuedanEmitido = true;
        if (objeto.getPorcentajeAnticipo() > 0) {
            Iterator<ActaContrato> itActas = objeto.getPagos().iterator();
            boolean hayActaAnticipoAprobadaConQuedan = false;
            while (itActas.hasNext() && !hayActaAnticipoAprobadaConQuedan) {
                ActaContrato acta = itActas.next();
                if (acta.getTipo().equals(TipoActaContrato.ANTICIPO) && acta.getEstado().equals(EstadoActa.APROBADA)) {
                    if (acta.getQuedanEmitido() != null && acta.getQuedanEmitido()) {
                        hayActaAnticipoAprobadaConQuedan = true;
                    }
                }
            }
            if (!hayActaAnticipoAprobadaConQuedan) {
                tieneQuedanEmitido = false;
            }
        }
        return tieneQuedanEmitido;
    }

    /**
     * Verifica si un acta de anticipo aprobada tiene Quedan emitido
     *
     * @return
     */
    public Boolean montoActaDevolucionTieneQuedanEmitido() {
        boolean tieneQuedanEmitido = true;
        if (objeto.getPorcentajeAnticipo() > 0) {
            Iterator<ActaContrato> itActas = objeto.getPagos().iterator();
            boolean hayActaDevolucionAprobadaConQuedan = false;
            while (itActas.hasNext() && !hayActaDevolucionAprobadaConQuedan) {
                ActaContrato acta = itActas.next();
                if (acta.getTipo().equals(TipoActaContrato.DEVOLUCION) && acta.getEstado().equals(EstadoActa.APROBADA)) {
                    if (acta.getQuedanEmitido() != null && acta.getQuedanEmitido()) {
                        hayActaDevolucionAprobadaConQuedan = true;
                    }
                }
            }
            if (!hayActaDevolucionAprobadaConQuedan) {
                tieneQuedanEmitido = false;
            }
        }
        return tieneQuedanEmitido;
    }

    /**
     * Abra el po-up que permite editar la rescisión del contrato
     */
    public void abrirRescisionContrato() {
        RequestContext.getCurrentInstance().execute("$('#confirmarRescisionContratoOC').modal('show');");
    }

    /**
     * Guarda los datos de la rescisión de un contrato / OC
     */
    public void guardarRescicionContratoOC() {
        try {
            for (RelacionProAdqItemInsumo rel : relacionesItemsInsumos) {
                for (ProcesoAdquisicionItem item : objeto.getItems()) {
                    for (RelacionProAdqItemInsumo relItem : item.getRelItemInsumos()) {
                        if (rel.getId().equals(relItem.getId())) {
                            relItem.setMontoRescindido(rel.getMontoRescindido());
                        }
                    }
                }
            }

            contratoDelegate.rescindirContratoOC(objeto);

            objeto.setTempUploadedFile(null);
            uploadedFile = null;

            recargarObjeto();
            RequestContext.getCurrentInstance().execute("$('#confirmarRescisionContratoOC').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Se carga el pop-up para cargar / editar la reprogramación de pagos del
     * contrato / OC
     */
    public void reprogramacionPagos() {
        try {
            programacionPagosEnEdicion = objeto.getProgramacionPagosContrato();
            RequestContext.getCurrentInstance().execute("$('#cargarReprogramacionPagos').modal('show');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Añade un flujo de 12 meses a la programación de pagos del contrato / OC
     */
    public void aniadirProgramacionDePagos() {
        FlujoCajaAnio fcm = new FlujoCajaAnio();
        fcm.setFlujoCajaMenusal(FlujoCajaMensualUtils.generarFCM12Meses());
        fcm.setId(0);
        programacionPagosEnEdicion.add(fcm);
    }

    /**
     * Guarda los cambios realizados en la programación de pagos de un COntrato
     * / OC
     */
    public void guardarProgramaiconPagos() {
        try {
            objeto.setProgramacionPagosContrato(programacionPagosEnEdicion);
            contratoDelegate.guardarProgramacionDePagosDeContrato(objeto);

            recargarObjeto();
            RequestContext.getCurrentInstance().execute("$('#cargarReprogramacionPagos').modal('hide');");
        } catch (GeneralException ex) {
            recargarObjeto();
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            recargarObjeto();
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Carga un texto con la descripción del pago seleccionado en un acta de
     * recepción
     *
     * @param acta
     * @return
     */
    public String cargarTextoPago(ActaContrato acta) {
        String descripcionPago = "";

        if (acta.getTipo().equals(TipoActaContrato.RECEPCION) && acta.getMesPago() != null) {
            String mes = UtilsMB.getTituloSeguimiento(TipoSeguimiento.MENSUAL, (acta.getMesPago().getMes() - 1));
            String anio = acta.getAnioPago().getAnio().toString();
            String monto = NumberUtils.nomberToString(acta.getMesPago().getMonto());
            descripcionPago = mes + " - " + anio + " - US$ " + monto;
        }

        return descripcionPago;
    }

    /**
     * Habilito la reprogramación solo de los pagos que aún no tengan acta
     * emitida
     *
     * @param idPOFCM
     * @return
     */
    public Boolean deshabilitarReprogramacionPagoMes(Integer idPOFCM) {
        Boolean deshabilitar = false;
        Iterator<ActaContrato> itActas = objeto.getPagos().iterator();
        while (itActas.hasNext() && !deshabilitar) {
            ActaContrato acta = itActas.next();
            if (acta.getMesPago() != null && acta.getTipo().equals(TipoActaContrato.RECEPCION) && acta.getMesPago().getId().equals(idPOFCM)) {
                deshabilitar = true;
            }
        }
        return deshabilitar;
    }

    /**
     * Si el año seleccionado tiene pagos que ya están asociados a actas, dicho
     * año no podrá modificarse
     *
     * @param idFCA
     * @return
     */
    public Boolean deshabilitarReprogramacionPagoAnio(Integer idFCA) {
        Boolean deshabilitar = false;
        if (idFCA != 0) {
            for (FlujoCajaAnio fca : objeto.getProgramacionPagosContrato()) {
                if (fca.getId().equals(idFCA)) {
                    Iterator<POFlujoCajaMenusal> itFCM = fca.getFlujoCajaMenusal().iterator();
                    while (itFCM.hasNext() && !deshabilitar) {
                        POFlujoCajaMenusal fcm = itFCM.next();
                        Iterator<ActaContrato> itActas = objeto.getPagos().iterator();
                        while (itActas.hasNext() && !deshabilitar) {
                            ActaContrato acta = itActas.next();
                            if (acta.getMesPago() != null && acta.getTipo().equals(TipoActaContrato.RECEPCION) && acta.getMesPago().getId().equals(fcm.getId())) {
                                deshabilitar = true;
                            }
                        }
                    }
                }
            }
        }
        return deshabilitar;
    }

    /**
     * Devuelve el monto total rescindido en un contrato / OC
     *
     * @return
     */
    public BigDecimal calcularMontoTotalRescindido() {
        BigDecimal montoTotalRescindido = BigDecimal.ZERO;
        for (ProcesoAdquisicionItem item : objeto.getItems()) {
            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                BigDecimal montoRescindidoRel = rel.getMontoRescindido() != null ? rel.getMontoRescindido() : BigDecimal.ZERO;
                montoTotalRescindido = montoTotalRescindido.add(montoRescindidoRel);
            }
        }
        return montoTotalRescindido;
    }

    /**
     * Habilita la extensión de plazo de un Contrato/OC
     */
    public void extenderPlazoContrato() {
        try {
            fechaFinParaExtender = objeto.getFechaFin();
            RequestContext.getCurrentInstance().execute("$('#cargarExtensionContrato').modal('show');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Guarda la extensión de plazo de un Contrato/OC
     */
    public void guardarExtensionPlazoContrato() {
        try {
            contratoDelegate.guardarExtensionPlazoContrato(objeto.getId(), fechaFinParaExtender);
            recargarObjeto();
            RequestContext.getCurrentInstance().execute("$('#cargarExtensionContrato').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Deshabilita la extensión de plazo de un Contrato/OC
     */
    public void deshabilitarExtenderPlazoContrato() {
        try {
            contratoDelegate.deshabilitarExtenderPlazoContrato(objeto.getId());
            recargarObjeto();
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBackConParametros').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }

    }

    /**
     * Prepara el pop-up (datos que va a contener) para crear o editar una
     * modificativa
     *
     * @param crearModificativa
     */
    public void cargarAniadirEditarModificativa(boolean crearModificativa) {
        try {
            poInsumosAEliminarDeModificativa = new LinkedList<>();
            if (crearModificativa) {
                tempModificativa = null;
            }
            habilitarAnadirInsumo = false;
            if (tempModificativa == null || tempModificativa.getId() == null) {
                tempModificativa = new ModificativaContrato();
                tempModificativa.setPoInsumos(new LinkedList<POInsumos>());
                tempModificativa.setProgramacionPagos(new HashSet<FlujoCajaAnio>());
                deshabilitarTabCompromisoYReservaEnModificativa = true;
            } else {
                tempModificativa = contratoDelegate.cargarModificativaContratoOC(tempModificativa.getId());
                deshabilitarTabCompromisoYReservaEnModificativa = (tempModificativa.getPoInsumos().isEmpty() || tempModificativa.getProgramacionPagos().isEmpty());
            }
            RequestContext.getCurrentInstance().execute("$('#anadirModificativa').modal('show');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Carga los insumos disponibles para agregar a una modificativa
     */
    public void cargarAniadirInsumoAModificativas() {
        try {
            habilitarAnadirInsumo = true;
            poInsumosSeleccionadosParaAgregarAModificativa = new LinkedList<>();
            poInusmosDisponiblesParaModificativa = contratoDelegate.getPoInsumosDisponiblesParaModificativasContratoOC(objeto.getId(), tempModificativa);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Asocia insumos a una modificativa
     */
    public void agregarPoInsumosSelecionadosAModificativa() {
        try {
            if (poInsumosSeleccionadosParaAgregarAModificativa.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NO_SELECCIONO_INSUMO_PARA_AGREGAR_A_MODIFICATIVA);
                throw b;
            }
            if (tempModificativa.getPoInsumos() == null) {
                tempModificativa.setPoInsumos(new LinkedList<POInsumos>());
            }
            List<POInsumos> listaInsumosQuitarDeListaAEliminar = new LinkedList<>();
            for (POInsumos poInsumo : poInsumosSeleccionadosParaAgregarAModificativa) {
                if (!tempModificativa.getPoInsumos().contains(poInsumo)) {
                    tempModificativa.getPoInsumos().add(poInsumo);
                }
                for (POInsumos poInsumoEliminar : poInsumosAEliminarDeModificativa) {
                    if (poInsumo.getId().equals(poInsumoEliminar.getId())) {
                        listaInsumosQuitarDeListaAEliminar.add(poInsumoEliminar);
                    }
                }
                if (!listaInsumosQuitarDeListaAEliminar.isEmpty()) {
                    poInsumosAEliminarDeModificativa.removeAll(listaInsumosQuitarDeListaAEliminar);
                }
            }
            habilitarAnadirInsumo = false;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }

    }

    /**
     * Guarda los cambios realizados a una modificativa
     */
    public void guardarModificativa() {
        try {
            contratoDelegate.guardarModificativa(tempModificativa, objeto.getId(), poInsumosAEliminarDeModificativa);
            tempModificativa = null;
            recargarObjeto();
            RequestContext.getCurrentInstance().execute("$('#anadirModificativa').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Desasocia insumos de una modificativa
     *
     * @param poInsumo
     */
    public void eliminarPoInsumoDeModificativa(POInsumos poInsumo) {
        try {
            tempModificativa.getPoInsumos().remove(poInsumo);
            poInsumosAEliminarDeModificativa.add(poInsumo);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Cancela la asociación de insumos a una modificativa
     */
    public void cancelArgregarPoInsumosSelecionados() {
        habilitarAnadirInsumo = false;
    }

    /**
     * Suma el monto total de todos los insumos asociados a una modificativa
     *
     * @return
     */
    public BigDecimal calcularMontoTotalInsumosModificativa() {
        BigDecimal montoTotal = BigDecimal.ZERO;
        if (tempModificativa != null) {
            if (tempModificativa.getPoInsumos() != null) {
                for (POInsumos poInsumo : tempModificativa.getPoInsumos()) {
                    if (poInsumo.getMontoTotal() != null) {
                        montoTotal = montoTotal.add(poInsumo.getMontoTotal());
                    }
                }
            }
        }
        return montoTotal;
    }

    /**
     * Suma el monto certificado de todos los insumos asociados a una
     * modificativa
     *
     * @return
     */
    public BigDecimal calcularMontoCertInsumosModificativa() {
        BigDecimal montoTotalCert = BigDecimal.ZERO;
        if (tempModificativa != null) {
            if (tempModificativa.getPoInsumos() != null) {
                for (POInsumos poInsumo : tempModificativa.getPoInsumos()) {
                    if (poInsumo.getMontoTotalCertificado() != null) {
                        montoTotalCert = montoTotalCert.add(poInsumo.getMontoTotalCertificado());
                    }
                }
            }
        }
        return montoTotalCert;
    }

    /**
     * Agrega un nuevo año a la programación de pagos de una modificativa
     */
    public void aniadirProgramacionDePagosAModificativa() {
        FlujoCajaAnio fcm = new FlujoCajaAnio();
        fcm.setFlujoCajaMenusal(FlujoCajaMensualUtils.generarFCM12Meses());
        tempModificativa.getProgramacionPagos().add(fcm);
    }

    /**
     * Deshabilita los meses de pagos que ya están asociados a las actas
     *
     * @return
     */
    public Boolean deshabilitarProgramacionPagoMesModificativa(Integer numAnio, Integer numMes) {
        Integer mes = numMes + 1;
        Boolean deshabilitar = false;
        if (numAnio != null && numMes != null) {
            List<Integer> listaMesesADeshabilitar = new ArrayList<>();
            Iterator<ActaContrato> itActas = objeto.getPagos().iterator();
            while (itActas.hasNext() && !deshabilitar) {
                ActaContrato acta = itActas.next();
                if (acta.getTipo().equals(TipoActaContrato.RECEPCION) && acta.getAnioPago().getAnio().equals(numAnio)) {//Si es el mismo año de un acta
                    if (acta.getMesPago() != null && acta.getTipo().equals(TipoActaContrato.RECEPCION)) {
                        if (acta.getMesPago().getMes().equals(mes)) {//Si es el mismo año de un acta
                            deshabilitar = true;
                        }
                    }
                }
            }
        }
        return deshabilitar;
    }

    /**
     * Este método realiza la solicitud de un compromiso presupuestario para la
     * modificativa
     */
    public void solicitarCompromisoPresupuestarioParaModificativa() {
        try {
            tempModificativa = contratoDelegate.solicitarCompromisoPresupuestarioParaModificativa(tempModificativa.getId());
            recargarObjeto();
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
     * Aprueba una modificativa
     */
    public void aprobarModificativa() {
        try {
            contratoDelegate.aprobarModificativa(tempModificativa, poInsumosAEliminarDeModificativa);
            tempModificativa = null;
            recargarObjeto();
            relacionesItemsInsumos = cargarRelacionesItemInsumos();
            RequestContext.getCurrentInstance().execute("$('#anadirModificativa').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Calcula el monto total de una modificativa en base al monto certificado
     * de los insumos que tiene asociado
     *
     * @return
     */
    public BigDecimal calcularMontoTotalModificativa(Integer idModificativa) {
        return contratoDelegate.calcularMontoTotalModificativa(idModificativa);
    }

    /**
     * Devuelve el nombre y el MIT del proveedor del contrato
     *
     * @return
     */
    public String obtenerNombreYNitProveedorContrato() {
        String textoProveedor = "";
        if (objeto.getProcesoAdquisicionProveedor() != null) {
            if (objeto.getProcesoAdquisicionProveedor().getProveedor().getNombreComercial() != null) {
                textoProveedor += objeto.getProcesoAdquisicionProveedor().getProveedor().getNombreComercial();
            }
            if (objeto.getProcesoAdquisicionProveedor().getProveedor().getNitOferente() != null) {
                textoProveedor += objeto.getProcesoAdquisicionProveedor().getProveedor().getNitOferente();
            }
        }
        return textoProveedor;
    }

    /**
     * Carga el modal para editar la duración del insumo en el contrato
     *
     * @param idRelacionItemInsumo
     * @return
     */
    public void cargarDuracionInsumoEnContrato(Integer idRelacionItemInsumo) {
        relacionItemInsumoEnEdicion = (RelacionProAdqItemInsumo) emd.getEntityById(RelacionProAdqItemInsumo.class.getName(), idRelacionItemInsumo);
    }

    /**
     * Guarda la duración del insumo en el contrato
     */
    public void guardarDuracionInsumoEnContrato() {
        try {
            relacionItemInsumoEnEdicion = contratoDelegate.guardarDuracionInsumoEnContrato(relacionItemInsumoEnEdicion);
            RequestContext.getCurrentInstance().execute("$('#editarDuracionInsumoEnContrato').modal('hide');");      
            objeto = contratoDelegate.cargarContrato(objeto.getId());
            relacionesItemsInsumos = cargarRelacionesItemInsumos();
        
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

// <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public UsuarioInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UsuarioInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ContratoOC getObjeto() {
        return objeto;
    }

    public void setObjeto(ContratoOC objeto) {
        this.objeto = objeto;
    }

    public ActaContrato getTempActa() {
        return tempActa;
    }

    public void setTempActa(ActaContrato tempPago) {
        this.tempActa = tempPago;
    }

    public ContratoDelegate getContratoDelegate() {
        return contratoDelegate;
    }

    public void setContratoDelegate(ContratoDelegate contratoDelegate) {
        this.contratoDelegate = contratoDelegate;
    }

    public ReporteDelegate getReporteDelegate() {
        return reporteDelegate;
    }

    public void setReporteDelegate(ReporteDelegate reporteDelegate) {
        this.reporteDelegate = reporteDelegate;
    }

    public String getFiltroFCMCodrigoInsumo() {
        return filtroFCMCodrigoInsumo;
    }

    public void setFiltroFCMCodrigoInsumo(String filtroFCMCodrigoInsumo) {
        this.filtroFCMCodrigoInsumo = filtroFCMCodrigoInsumo;
    }

    public UsuarioDelegate getUsuarioDelegate() {
        return usuarioDelegate;
    }

    public void setUsuarioDelegate(UsuarioDelegate usuarioDelegate) {
        this.usuarioDelegate = usuarioDelegate;
    }

    public SofisComboG<SsUsuario> getComboUsuarios() {
        return comboUsuarios;
    }

    public void setComboUsuarios(SofisComboG<SsUsuario> comboUsuarios) {
        this.comboUsuarios = comboUsuarios;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public DataLoteItem getItemADistribuir() {
        return itemADistribuir;
    }

    public void setItemADistribuir(DataLoteItem itemADistribuir) {
        this.itemADistribuir = itemADistribuir;
    }

    public String getFiltroFCMAnio() {
        return filtroFCMAnio;
    }

    public void setFiltroFCMAnio(String filtroFCMAnio) {
        this.filtroFCMAnio = filtroFCMAnio;
    }

    public String getCOD_TEXT_REAL_PAGOS() {
        return COD_TEXT_REAL_PAGOS;
    }

    public String getCOD_TEXT_PAC() {
        return COD_TEXT_PAC;
    }

    public String getCOD_TEXT_PRO_ADQ() {
        return COD_TEXT_PRO_ADQ;
    }

    public String getFiltroFCMTipo() {
        return filtroFCMTipo;
    }

    public void setFiltroFCMTipo(String filtroFCMTipo) {
        this.filtroFCMTipo = filtroFCMTipo;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public String getImpuestosAplica() {
        return impuestosAplica;
    }

    public void setImpuestosAplica(String impuestosAplica) {
        this.impuestosAplica = impuestosAplica;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Boolean getModificandoOrdenInicio() {
        return modificandoOrdenInicio;
    }

    public void setModificandoOrdenInicio(Boolean modificandoOrdenInicio) {
        this.modificandoOrdenInicio = modificandoOrdenInicio;
    }

    public SofisComboG<SsUsuario> getComboUsuariosFirmantes() {
        return comboUsuariosFirmantes;
    }

    public TreeNode getRootNodePagos() {
        return rootNodePagos;
    }

    public void setRootNodePagos(TreeNode rootNodePagos) {
        this.rootNodePagos = rootNodePagos;
    }

    public void setComboUsuariosFirmantes(SofisComboG<SsUsuario> comboUsuariosFirmantes) {
        this.comboUsuariosFirmantes = comboUsuariosFirmantes;
    }

    public Boolean getEsActaRecepcion() {
        return esActaRecepcion;
    }

    public void setEsActaRecepcion(Boolean esActaRecepcion) {
        this.esActaRecepcion = esActaRecepcion;
    }

    public List<RelacionProAdqItemInsumo> getRelacionesItemsInsumos() {
        return relacionesItemsInsumos;
    }

    public void setRelacionesItemsInsumos(List<RelacionProAdqItemInsumo> relacionesItemsInsumos) {
        this.relacionesItemsInsumos = relacionesItemsInsumos;
    }

    public Set<FlujoCajaAnio> getProgramacionPagosEnEdicion() {
        return programacionPagosEnEdicion;
    }

    public void setProgramacionPagosEnEdicion(Set<FlujoCajaAnio> programacionPagosEnEdicion) {
        this.programacionPagosEnEdicion = programacionPagosEnEdicion;
    }

    public String getIdPOFlujoCajaMenusal() {
        return idPOFlujoCajaMenusal;
    }

    public void setIdPOFlujoCajaMenusal(String idPOFlujoCajaMenusal) {
        this.idPOFlujoCajaMenusal = idPOFlujoCajaMenusal;
    }

    public Date getFechaFinParaExtender() {
        return fechaFinParaExtender;
    }

    public void setFechaFinParaExtender(Date fechaFinParaExtender) {
        this.fechaFinParaExtender = fechaFinParaExtender;
    }

    public boolean getHabilitarAnadirInsumo() {
        return habilitarAnadirInsumo;
    }

    public void setHabilitarAnadirInsumo(boolean habilitarAnadirInsumo) {
        this.habilitarAnadirInsumo = habilitarAnadirInsumo;
    }

    public List<POInsumos> getPoInusmosDisponiblesParaModificativa() {
        return poInusmosDisponiblesParaModificativa;
    }

    public void setArchivoDelegate(ArchivoDelegate archivoDelegate) {
        this.archivoDelegate = archivoDelegate;
    }

    public List<POInsumos> getPoInsumosSeleccionadosParaAgregarAModificativa() {
        return poInsumosSeleccionadosParaAgregarAModificativa;
    }

    public void setPoInsumosSeleccionadosParaAgregarAModificativa(List<POInsumos> poInsumosSeleccionadosParaAgregarAModificativa) {
        this.poInsumosSeleccionadosParaAgregarAModificativa = poInsumosSeleccionadosParaAgregarAModificativa;
    }

    public ModificativaContrato getTempModificativa() {
        return tempModificativa;
    }

    public void setTempModificativa(ModificativaContrato tempModificativa) {
        this.tempModificativa = tempModificativa;
    }

    public List<POInsumos> getPoInsumosAEliminarDeModificativa() {
        return poInsumosAEliminarDeModificativa;
    }

    public void setPoInsumosAEliminarDeModificativa(List<POInsumos> poInsumosAEliminarDeModificativa) {
        this.poInsumosAEliminarDeModificativa = poInsumosAEliminarDeModificativa;
    }

    public Boolean getDeshabilitarTabCompromisoYReservaEnModificativa() {
        return deshabilitarTabCompromisoYReservaEnModificativa;
    }

    public void setDeshabilitarTabCompromisoYReservaEnModificativ(Boolean deshabilitarTabCompromisoYReservaEnModificativa) {
        this.deshabilitarTabCompromisoYReservaEnModificativa = deshabilitarTabCompromisoYReservaEnModificativa;
    }

    public RelacionProAdqItemInsumo getRelacionItemInsumoEnEdicion() {
        return relacionItemInsumoEnEdicion;
    }

    public void setRelacionItemInsumoEnEdicion(RelacionProAdqItemInsumo relacionItemInsumoEnEdicion) {
        this.relacionItemInsumoEnEdicion = relacionItemInsumoEnEdicion;
    }

    // </editor-fold>
}
