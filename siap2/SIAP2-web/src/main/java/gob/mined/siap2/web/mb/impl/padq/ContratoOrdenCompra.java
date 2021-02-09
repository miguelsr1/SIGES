/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.padq;

import gob.mined.siap2.business.utils.FlujoCajaMensualUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItemProvOferta;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.enums.EstadoContrato;
import gob.mined.siap2.entities.enums.TipoGeneracionContrato;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.datatypes.DataProveedorOferta;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.impl.SelectOneUTBean;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 * Esta clase implementa el backing bean de los contratos u órdenes de compra
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "contratoOrdenCompra")
public class ContratoOrdenCompra extends SelectOneUTBean {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    private ProcesoAdquisicionDelegate padqDelegate;
    @Inject
    private UsuarioDelegate usuarioDelegate;
    @Inject
    private EntityManagementDelegate emd;
    @Inject
    private JSFUtils jSFUtils;
    @Inject
    TextMB textMB;
    @Inject
    private ReporteDelegate reporteDelegate;
    @Inject
    ArchivoDelegate archivoDelegate;

    private ContratoOC contratoEnEdicion;
    private ProcesoAdqMain mainBean;
    private Set<FlujoCajaAnio> programacionPagosEnEdicion;

    List<DataProveedorOferta> ofertasProveedor;
    private DualListModel<Impuesto> impuestos = new DualListModel(new LinkedList(), new LinkedList());
    private String tipoGeneracion;
    private Integer anioFiscal;
    private Integer mesInicial;
    private Integer cantidadMeses;

    public static final String GENERACION_MANUAL = "MANUAL";

    @PostConstruct
    public void init() {
        mainBean = (ProcesoAdqMain) JSFUtils.getBean("procesoAdqMain");
        ProcesoAdquisicion proceso = mainBean.getObjeto();

        if (proceso.getTipoContratos() != null) {
            padqDelegate.generarActualizarContratos(proceso.getId(), proceso.getTipoContratos());
        }
        mainBean.reloadProceso();
    }

    /**
     * Este método permite cambiar la agrupación del contrato.
     *
     * @param tipo
     */
    public void cambiarAgrupacion(TipoGeneracionContrato tipo) {
        padqDelegate.generarActualizarContratos(mainBean.getObjeto().getId(), tipo);
        mainBean.reloadProceso();
    }

    /**
     * Este método permite obtener el proceso de adquisición.
     *
     * @return
     */
    public ProcesoAdquisicion getProceso() {
        return mainBean.getObjeto();
    }

    /**
     * Genera y devuelve una lista de números de contratos comenzando en 1 de forma incremental
     * @return 
     */
    private List<Integer> getNrosContrato() {
        ProcesoAdquisicion proceso = mainBean.getObjeto();
        List<Integer> nrosContrato = new LinkedList<>();
        Integer nroInicial = proceso.getReservaNroContratoInicial();
        Integer nroFinal = proceso.getReservaNroContratoFinal();
        if ((nroInicial != null && !nroInicial.equals(0)) && (nroFinal != null && !nroFinal.equals(0))) {
            for (Integer i = nroInicial; i <= nroFinal; i++) {
                nrosContrato.add(i);
            }
        }
        for (ContratoOC contrato : proceso.getContratos()) {
            if (contrato.getNroContrato() != null && !contrato.equals(contratoEnEdicion)) {
                nrosContrato.remove(contrato.getNroContrato());
            }
        }
        return nrosContrato;
    }

    /**
     * Este método genera un Map con los número de contrato
     *
     * @return
     */
    public Map getMapNrosContrato() {
        Map nrosContratoMap = new LinkedHashMap();
        for (Integer i : getNrosContrato()) {
            nrosContratoMap.put(String.valueOf(i), i);
        }
        return nrosContratoMap;
    }

    /**
     * Este método carga los datos del contrato en la opción ver y actualiza la
     * página.
     *
     * @param toSet
     */
    public void cargarContratoVerItems(ContratoOC toSet) {
        this.contratoEnEdicion = toSet;
        RequestContext.getCurrentInstance().execute("$('#verItems').modal('show');");
    }

    /**
     * Este método resetea las fechas de inicio y fin del contrato.
     */
    public void limpiarFechas() {
        contratoEnEdicion.setFechaFin(null);
        contratoEnEdicion.setFechaInicio(null);
    }

    /**
     * Este método actualiza los datos de un contrato en la página.
     *
     * @param contrato
     */
    public void cargarContrato(ContratoOC contrato) {
        unidadTecnicaSelecionada = contrato.getUnidadTecnica();
        contratoEnEdicion = contrato;
        if (contratoEnEdicion.getFechaInicio() != null && contratoEnEdicion.getFechaFin() != null) {
            if (contratoEnEdicion.getFechaInicio().compareTo(contratoEnEdicion.getFechaFin()) > 0) {
                limpiarFechas();
            }
        }
        //se cargan los impuestos
        List<Impuesto> disponibles = versionDelegate.getClasesGeneralCodiguera(Impuesto.class);
        List<Impuesto> enUso = new LinkedList<>();
        for (Impuesto i : contratoEnEdicion.getImpuestos()) {
            enUso.add(i);
            if (disponibles.contains(i)) {
                disponibles.remove(i);
            }
        }
        impuestos = new DualListModel(disponibles, enUso);
    }

    /**
     * Este método actualiza la visualización de los proveedores de un contrato.
     *
     * @param contrato
     */
    public void cargarContratoVerProveedores(ContratoOC contrato) {
        ofertasProveedor = new ArrayList<>();

        for (ProcesoAdquisicionItem item : contrato.getItems()) {
            for (ProcesoAdquisicionItemProvOferta oferta : item.getOfertasProvedor()) {
                DataProveedorOferta dataProveedorOferta = new DataProveedorOferta();
                dataProveedorOferta.setMontoOferta(oferta.calcularPrecioTotal());
                if (item.getProveedorOfertaAdjudicadaId().equals(oferta)) {
                    dataProveedorOferta.setOfertaGanadora(true);
                } else {
                    dataProveedorOferta.setOfertaGanadora(false);
                }
                dataProveedorOferta.setProveedorId(oferta.getProcesoAdquisicionProveedor().getProveedor().getId());
                dataProveedorOferta.setProveedorNombre(oferta.getProcesoAdquisicionProveedor().getProveedor().getNombreComercial());
                ofertasProveedor.add(dataProveedorOferta);
            }
        }
        RequestContext.getCurrentInstance().execute("$('#verProveedores').modal('show');");
    }

    /**
     * Este método guardar el contrato sobre el que se está trabajando.
     */
    public void guardarContrato() {
        try {
            contratoEnEdicion.setUnidadTecnica(unidadTecnicaSelecionada);
            contratoEnEdicion.setImpuestos(new LinkedList(impuestos.getTarget()));

            padqDelegate.actualizarContrato(contratoEnEdicion);

            mainBean.reloadProceso();
            RequestContext.getCurrentInstance().execute("$('#cargarContrato').modal('hide');");
        } catch (GeneralException ex) {
            mainBean.reloadProceso();
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
     * Este método determina si se satisfacen las condiciones para generar el
     * contrato.
     *
     * @return
     */
    public boolean habilitarGenerarContrato() {
        ProcesoAdquisicion proceso = mainBean.getObjeto();
        if (proceso.getTipoContratos() == null) {
            return false;
        }
        if (proceso.getContratos().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Este método genera los contratos.
     */
    public void generarContratos() {
        try {
            padqDelegate.generarContratos(mainBean.getObjeto().getId());
            mainBean.reloadProceso();

            String texto = textMB.obtenerTexto("labels.GenerarContratosCorrectamente");
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
     * Este método realiza la solicitud de un compromiso presupuestario.
     */
    public void solicitarCompromisoPresupuestario() {
        try {
            padqDelegate.solicitarCompromisoPresupuestario(mainBean.getObjeto().getId());
            mainBean.reloadProceso();

            String texto = textMB.obtenerTexto("labels.CompromisoPresupueatarioSolicitadoCorrectamente");
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
     * Este método habilita la carga de programación de pagos del contrato.
     *
     * @param editar
     */
    public void initProgramaiconPagos(boolean editar) {
        try {
            if (contratoEnEdicion.getFechasDesdeOrdenInicio() == null && contratoEnEdicion.getFechasDesdeOrdenInicio().equals(Boolean.FALSE)) {
                if (contratoEnEdicion.getFechaInicio() == null || contratoEnEdicion.getFechaFin() == null) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_CARGUE_LAS_FECHAS_DEL_CONTRATO_ANTES_DE_CARGAR_PROG_DE_PAGOS);
                    throw b;
                }
            }

            programacionPagosEnEdicion = new LinkedHashSet<>(contratoEnEdicion.getProgramacionPagosProceso());
            tipoGeneracion = GENERACION_MANUAL;

            anioFiscal = null;
            mesInicial = null;
            cantidadMeses = null;

            if (editar) {
                RequestContext.getCurrentInstance().execute("$('#cargarProgramacionPagos').modal('show');");
            } else {
                RequestContext.getCurrentInstance().execute("$('#verProgramacionPagos').modal('show');");
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
     * Este método agrega la programación de pagos del contrato.
     */
    public void aniadirProgramacionDePagos() {
        FlujoCajaAnio fcm = new FlujoCajaAnio();
        fcm.setFlujoCajaMenusal(FlujoCajaMensualUtils.generarFCM12Meses());
        programacionPagosEnEdicion.add(fcm);
    }

    /**
     * Este método guarda la programación de pagos sobre la que se está
     * trabajando.
     */
    public void guardarProgramacionPagos() {
        try {
            
            contratoEnEdicion.setProgramacionPagosProceso(programacionPagosEnEdicion);
            padqDelegate.guardarProgramacionDePagosProceso(contratoEnEdicion);

            mainBean.reloadProceso();
            RequestContext.getCurrentInstance().execute("$('#cargarProgramacionPagos').modal('hide');");
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
     * Este método genera la programación de pagos automática
     */
    public void generarProgramacionPagos() {
        try {
            padqDelegate.generarDistribucionPagosAutomaticaProceso(contratoEnEdicion.getId(), anioFiscal, mesInicial, cantidadMeses);

            mainBean.reloadProceso();
            RequestContext.getCurrentInstance().execute("$('#cargarProgramacionPagos').modal('hide');");
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
     * Este método obtiene los insumos del contrato en edición.
     *
     * @return
     */
    public List<RelacionProAdqItemInsumo> getInsumosContratoEnEdicion() {
        List<RelacionProAdqItemInsumo> res = new LinkedList<>();
        if (contratoEnEdicion != null) {
            for (ProcesoAdquisicionItem item : contratoEnEdicion.getItems()) {
                for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                    res.add(rel);
                }
            }
        }
        return res;
    }

    /**
     * Este método genera el reporte de documento de compra de un contrato, en
     * formato PDF.
     *
     * @param contrato
     */
    public void generarReporteDocumentoCompra(ContratoOC contrato) {
        byte[] bytespdf = reporteDelegate.generarDocumentoCompra(contrato.getId());
        ArchivoUtils.downloadPdfFromBytes(bytespdf, "AnexoDocumentoCompra.pdf");
    }

    /**
     * Este método permite generar el reporte de orden de compra para un
     * contrato dado.
     *
     * @param contrato
     */
    public void generarReporteDocumentoOrdenCompra(ContratoOC contrato) {
        byte[] bytespdf = reporteDelegate.generarDocumentoOrdenCompra(contrato.getId());
        ArchivoUtils.downloadPdfFromBytes(bytespdf, "OrdenDeCompra.pdf");
    }
    
    /**
     * Verifica si los contratos ya fueron generados
     * @return 
     */
    public boolean contratosYaFueronGenerados(){
        for (ContratoOC contrato : mainBean.getObjeto().getContratos()){
            if (contrato.getEstado() != null && contrato.getEstado()!=EstadoContrato.EN_CREACION_DENTRO_PROCESO){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Este método permite realizar la descarga de un archivo.
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

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getProcesoAdquisicionId() {
        return mainBean.getObjeto().getId();
    }

    public ContratoOC getContratoEnEdicion() {
        return contratoEnEdicion;
    }

    public void setContratoEnEdicion(ContratoOC contratoEnEdicion) {
        this.contratoEnEdicion = contratoEnEdicion;
    }

    public List<DataProveedorOferta> getOfertasProveedor() {
        return ofertasProveedor;
    }

    public void setOfertasProveedor(List<DataProveedorOferta> ofertasProveedor) {
        this.ofertasProveedor = ofertasProveedor;
    }

    public DualListModel<Impuesto> getImpuestos() {
        return impuestos;
    }

    public Set<FlujoCajaAnio> getProgramacionPagosEnEdicion() {
        return programacionPagosEnEdicion;
    }

    public void setProgramacionPagosEnEdicion(Set<FlujoCajaAnio> programacionPagosEnEdicion) {
        this.programacionPagosEnEdicion = programacionPagosEnEdicion;
    }

    public String getTipoGeneracion() {
        return tipoGeneracion;
    }

    public void setTipoGeneracion(String tipoGeneracion) {
        this.tipoGeneracion = tipoGeneracion;
    }

    public Integer getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(Integer anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public Integer getCantidadMeses() {
        return cantidadMeses;
    }

    public void setCantidadMeses(Integer cantidadMeses) {
        this.cantidadMeses = cantidadMeses;
    }

    public Integer getMesInicial() {
        return mesInicial;
    }

    public void setMesInicial(Integer mesInicial) {
        this.mesInicial = mesInicial;
    }

    public void setImpuestos(DualListModel<Impuesto> impuestos) {
        this.impuestos = impuestos;
    }

    // </editor-fold>
}
