/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.padq;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividad;
import gob.mined.siap2.entities.data.impl.POActividadAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.data.impl.TDR;
import gob.mined.siap2.entities.enums.PasosProcesoAdquisicion;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralPODelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.ws.to.DataFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * Esta clase implementa el backing bean de los insumos del proceso.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "insumosDelProceso")
public class InsumosDelProceso implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    JSFUtils jSFUtils;
    @Inject
    ProcesoAdquisicionDelegate delegate;
    @Inject
    GeneralPODelegate generalPOdelegate;
    @Inject
    ArchivoDelegate archivoDelegate;
    @Inject
    private TextMB textMB;
    private ProcesoAdqMain mainBean;
    private ProcesoAdqAgregarInsumos procesoAdqAgregarInsumos;

    private String rubro;
    private String codigo;
    private String nombre;

    private Integer procesoId;

    private List<ProcesoAdquisicionInsumo> insumosDataTable;
    private UploadedFile uploadedFile;

    private Map<Integer, Boolean> mapPoInsumosRecepcionTDR;

    private List<RelacionProAdqItemInsumo> relacionesDelInsumo = new ArrayList<>();

    private ProcesoAdquisicionInsumo insumoSeleccionado;
    private String detalleTipo = "";

    private static final String POA_PROYECTO = "POA_PROYECTO";
    private static final String POA_ACCION_CENTRA = "POA_ACCION_CENTRA";
    private static final String POA_ASIGNACION_NP = "POA_ASIGNACION_NP";

    private String detalleNompreObjeto;
    private String detalleLinkObjeto;
    private String detalleLinkPOA;
    protected TDR tempTDR;
    private List<DataFile> archivosAGuardar = new LinkedList();

    @PostConstruct
    public void init() {
        mainBean = (ProcesoAdqMain) JSFUtils.getBean("procesoAdqMain");
        procesoId = mainBean.getObjeto().getId();
        procesoAdqAgregarInsumos = (ProcesoAdqAgregarInsumos) JSFUtils.getBean("procesoAdqAgregarInsumos");

        cargarMapPoInsumosRecepcionTDR();
    }

    
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        rubro = null;
        codigo = null;
        nombre = null;
    }

    /**
     * Este me´todo permite obtener los insumos de un proceso de adquisición.
     *
     * @return
     */
    public List<ProcesoAdquisicionInsumo> getInsumos() {
        List<ProcesoAdquisicionInsumo> l = new LinkedList<>();
        try {

            for (ProcesoAdquisicionInsumo i : mainBean.getObjeto().getInsumos()) {

                boolean cumple = true;
                if (!TextUtils.isEmpty(rubro)) {
                    if (!i.getInsumo().getObjetoEspecificoGasto().getClasificador().toString().contains(rubro)) {
                        cumple = false;
                    }
                }
                if (!TextUtils.isEmpty(nombre)) {
                    if (!i.getInsumo().getNombre().toString().contains(nombre)) {
                        cumple = false;
                    }
                }
                if (!TextUtils.isEmpty(codigo)) {
                    if (!i.getPoInsumo().getId().toString().contains(codigo)) {
                        cumple = false;
                    }
                }

                if (cumple) {
                    l.add(i);
                }

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
        return l;
    }

    /**
     * Este método permite eliminar un insumo del proceso de adquisición.
     *
     * @param i
     */
    public void eliminarInsumo(ProcesoAdquisicionInsumo i) {
        try {
            delegate.eliminarInsumoProceso(mainBean.getObjeto().getId(), i.getId());
            //Si no salta error de que el insumo a eliminar está asociado a algún item, puede saltar ahora con los items sin guardar
            GestionLoteItems gestionLoteItemsBean = (GestionLoteItems) JSFUtils.getBean("gestionLoteItems");
            for (ProcesoAdquisicionItem item : gestionLoteItemsBean.getItemsProceso()) {
                if (item.getInsumosTemporalesDelItem().contains(i)) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_ITEM_TIENE_OFERTAS_ASOCIADAS);
                    throw b;
                }
            }
            mainBean.reloadProceso();
            procesoAdqAgregarInsumos.createRootNode();
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
     * Este método devuelve el ítem de un insumo
     *
     * @param idInsumo
     * @return
     */
    public ProcesoAdquisicionItem getItemDelInsumo(Integer idInsumo) {
        return delegate.obtenerItemDelInsumo(idInsumo);
    }

    /**
     * Este método permite obtener los TDR de un insumo
     *
     * @param insumo
     */
    public void initTDR(POInsumos insumo) {
        tempTDR = null;
        if (insumo.getId() != null) {
            tempTDR = insumo.getTdr();
        }

        if (tempTDR != null) {

            RequestContext.getCurrentInstance().execute("$('#verTDRInsumoModal').modal('show');");
        }
    }

    /**
     * Este método permite determinar si está habilitado el botón de
     * notificación.
     *
     * @return
     */
    public boolean getHabilitarBotonNotificacion() {
        boolean habilitar = mainBean.getObjeto().getEstado() == PasosProcesoAdquisicion.RECEPCION_TDR_ET_CERT_DISP;

        if (habilitar && getInsumosSinTDR().size() == 0) {
            habilitar = false;
        }
        return habilitar;
    }

    /**
     * Este método devuelve el paso en el que se encuentra el proceso de
     * adquisición.
     *
     * @return
     */
    public PasosProcesoAdquisicion getEstadoProceso() {
        return mainBean.getObjeto().getEstado();
    }

    /**
     * Este método obtiene los insumos del proceso que no tienen TDR cargados.
     *
     * @return
     */
    private List<ProcesoAdquisicionInsumo> getInsumosSinTDR() {
        ArrayList<ProcesoAdquisicionInsumo> insumosSinTDR = new ArrayList<>();
        for (ProcesoAdquisicionInsumo insumo : getInsumos()) {
            if (insumo.getPoInsumo().getTdr() == null) {
                insumosSinTDR.add(insumo);
            }
        }
        return insumosSinTDR;
    }

    /**
     * Este método permite notificar a las UT respecto a la solicitud de TDR.
     */
    public void notificarUTSobreTDR() {
        try {
            delegate.notificarUsuariosUTSobreTDR(mainBean.getObjeto().getId());
            String texto = textMB.obtenerTexto("labels.NotificacionEnviadaInsumoSinTDR");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * En un map cargo para cada PoInsumo de cada insumo del proceso si recibió
     * o no físicamente su TDR/ET Esto luego se utiliza para guardar los cambios
     * de recepción de TDR/ET
     */
    public void cargarMapPoInsumosRecepcionTDR() {
        mapPoInsumosRecepcionTDR = new HashMap<Integer, Boolean>();
        for (ProcesoAdquisicionInsumo insumo : getInsumosDataTable(false)) {
            Boolean recibioTDR = insumo.getPoInsumo().getRecepcionFisicaTDR();
            //Si está en null es porque no recibió TDR/ET fisicamente
            if (recibioTDR == null) {
                recibioTDR = false;
            }
            mapPoInsumosRecepcionTDR.put(insumo.getPoInsumo().getId(), recibioTDR);
        }
    }

    public void deshacer() {

        insumosDataTable = delegate.obtenerInsumosDelProceso(mainBean.getObjeto().getId());
    }

    /**
     * Se guarda la recepción física de TDR/ET
     */
    public void guardar() {
        try {
            delegate.guardarInsumos(insumosDataTable, mapPoInsumosRecepcionTDR);
            String texto = textMB.obtenerTexto("labels.InsumosDelProcesoGuardados");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));

            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
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
     * Como este método se encuentra en un onChange de un checkbox, se niega el
     * booleano que tenía guardado anteriormente y se guarda en el map que tiene
     * la lista de PoInsumos y su correspondiente recepción física de TDR
     *
     * @param idPoInsumo
     */
    public void cambiarRecepcionFisicaTDR(Integer idPoInsumo) {
        Boolean estadoAnterior = mapPoInsumosRecepcionTDR.get(idPoInsumo);

        mapPoInsumosRecepcionTDR.put(idPoInsumo, !estadoAnterior);

    }

    /**
     * Este método carga los detalles del insumo
     *
     * @param insumo
     */
    public void cargarDetallesInsumo(ProcesoAdquisicionInsumo insumo) {
        relacionesDelInsumo = insumo.getRelItemInsumos();
        insumoSeleccionado = insumo;

        if (insumoSeleccionado.getPoInsumo().getPoa().getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
            POAConActividades poaActividad = (POAConActividades) insumoSeleccionado.getPoInsumo().getPoa();
            detalleNompreObjeto = poaActividad.getConMontoPorAnio().getNombre();
            if (poaActividad.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ACCION_CENTRAL) {
                detalleTipo = POA_ACCION_CENTRA;
            } else {
                detalleTipo = POA_ASIGNACION_NP;
            }
        } else {
            POAProyecto pOAProyecto = (POAProyecto) insumoSeleccionado.getPoInsumo().getPoa();
            detalleTipo = POA_PROYECTO;
            detalleNompreObjeto = pOAProyecto.getProyecto().getNombre();
        }

        RequestContext.getCurrentInstance().execute("$('#verDetallesInsumoModal').modal('show');");
    }

    /**
     * Este método devuelve el nombre de la actividad del POA.
     *
     * @return
     */
    public String getNombreActividadPOA() {
        if (insumoSeleccionado == null) {
            return null;
        }

        POActividadBase act = insumoSeleccionado.getPoInsumo().getActividad();
        if (act instanceof POActividad) {
            return ((POActividad) act).getNombre();
        } else if (act instanceof POActividadAsignacionNoProgramable) {
            return ((POActividadAsignacionNoProgramable) act).getActividadNP().getNombre();
        } else if (act instanceof POActividadProyecto) {
            return ((POActividadProyecto) act).getActividadCodiguera().getNombre();
        }

        return null;
    }

    /**
     * Devuelve la línea de POA del insumo seleccionado
     * @return 
     */
    public POLinea getPOSelecionadoLinea() {
        if (insumoSeleccionado == null) {
            return null;
        }
        if (POA_PROYECTO.equals(detalleTipo)) {
            return generalPOdelegate.obtenerPOLineaPorIdPoInsumo(insumoSeleccionado.getPoInsumo().getId());
        }
        return null;
    }

    /**
     * Este método permite ver en un modal un detalle del insumo.
     * @param insumo 
     */
    public void cargarDetallesProyectoAccionCentralDelInsumo(ProcesoAdquisicionInsumo insumo) {
        RequestContext.getCurrentInstance().execute("$('#VerDetallesProyectoAccionCentralModal').modal('show');");
    }

    /**
     * Este método corresponde al evento de carga de un archivo.
     * @param event 
     */
    public void fileUploadListener(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            if (uploadedFile != null) {
                DataFile aguardar = ArchivoUtils.getDataFile(uploadedFile);
                archivosAGuardar.add(aguardar);
            }
            uploadedFile = null;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /***
     * Instancia la lista que guardará los certificados de disponibilidad presupuestaria
     */
    public void initCertificadoDispPres() {
        archivosAGuardar = new LinkedList();
    }

    /**
     * Este método permite guardar el certificado de disponibilidad presupuestaria de un insumo.
     */
    public void guardarCertificadosDispPres() {
        try {
            delegate.actualizarCertificadosDeDisponibilidadPresupuestaria(insumoSeleccionado.getPoInsumo().getId(), insumoSeleccionado.getPoInsumo().getArchivosCertificacionPresupuestaria(), archivosAGuardar);
            mainBean.reloadProceso();
            RequestContext.getCurrentInstance().execute("$('#anadirCertificadoDispPres').modal('hide');");
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
     * Este método permite eliminar un archivo.
     * @param a 
     */
    public void eliminarArchivo(Archivo a) {
        insumoSeleccionado.getPoInsumo().getArchivosCertificacionPresupuestaria().remove(a);
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public ProcesoAdqMain getMainBean() {
        return mainBean;
    }

    public void setMainBean(ProcesoAdqMain mainBean) {
        this.mainBean = mainBean;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TDR getTempTDR() {
        return tempTDR;
    }

    public void setTempTDR(TDR tempTDR) {
        this.tempTDR = tempTDR;
    }

    public List<DataFile> getArchivosAGuardar() {
        return archivosAGuardar;
    }

    public void setArchivosAGuardar(List<DataFile> archivosAGuardar) {
        this.archivosAGuardar = archivosAGuardar;
    }

    /**
     * Se agrega este atributo para que funcione el orden de las distintas
     * columnas de la tabla. Ya que si el value de la dateTable se carga
     * directamente de la base de datos por medio del getInsumos el ordenamiento
     * no funciona.
     *
     * 22/2/2017 - Eduardo - Incidente: 7696
     */
    public List<ProcesoAdquisicionInsumo> getInsumosDataTable(boolean forzarActualizacion) {
        if (insumosDataTable == null || forzarActualizacion) {
            insumosDataTable = getInsumos();
        }
        return insumosDataTable;
    }

    public void setInsumosDataTable(List<ProcesoAdquisicionInsumo> insumosDataTable) {
        this.insumosDataTable = insumosDataTable;
    }

    public List<RelacionProAdqItemInsumo> getRelacionesDelInsumo() {
        return relacionesDelInsumo;
    }

    public void setRelacionesDelInsumo(List<RelacionProAdqItemInsumo> relacionesDelInsumo) {
        this.relacionesDelInsumo = relacionesDelInsumo;
    }

    public ProcesoAdquisicionInsumo getInsumoSeleccionado() {
        return insumoSeleccionado;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void setInsumoSeleccionado(ProcesoAdquisicionInsumo insumoSeleccionado) {
        this.insumoSeleccionado = insumoSeleccionado;
    }

    public String getDetalleTipo() {
        return detalleTipo;
    }

    public void setDetalleTipo(String detalleTipo) {
        this.detalleTipo = detalleTipo;
    }

    public String getDetalleNompreObjeto() {
        return detalleNompreObjeto;
    }

    public void setDetalleNompreObjeto(String detalleNompreObjeto) {
        this.detalleNompreObjeto = detalleNompreObjeto;
    }

    public String getDetalleLinkObjeto() {
        return detalleLinkObjeto;
    }

    public void setDetalleLinkObjeto(String detalleLinkObjeto) {
        this.detalleLinkObjeto = detalleLinkObjeto;
    }

    public String getDetalleLinkPOA() {
        return detalleLinkPOA;
    }

    public void setDetalleLinkPOA(String detalleLinkPOA) {
        this.detalleLinkPOA = detalleLinkPOA;
    }
 // </editor-fold>
}
