/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.padq;

import gob.mined.siap2.business.utils.UtilsUACI;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionLote;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.data.impl.TDR;
import gob.mined.siap2.entities.enums.EstadoItem;
import gob.mined.siap2.entities.enums.EstadoProcesoAdq;
import gob.mined.siap2.entities.enums.PasosProcesoAdquisicion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.datatypes.DataExportLoteItemInsumos;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.delegates.impl.GanttDelegate;
import gob.mined.siap2.web.delegates.impl.PACDelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
 * Esta clase implementa el backing bean del proceso de adquisición.
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "procesoAdqMain")
public class ProcesoAdqMain implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    ProcesoAdquisicionDelegate delegate;

    @Inject
    TextMB textMB;
    @Inject
    PACDelegate pACDelegate;
    @Inject
    UsuarioDelegate usuarioDelegate;
    @Inject
    ArchivoDelegate archivoDelegate;
    @Inject
    UsuarioInfo usrInfo;

    private boolean update = false;
    private ProcesoAdquisicion objeto;

    private SofisComboG<AnioFiscal> comboAnioFiscal;
    private SofisComboG<SsUsuario> comboUsuarios;
    private Integer openTab = 0;

    private String selectedMetodoId;
    @Inject
    GanttDelegate ganttDelegate;
    private String ganttJson;
    private Date fechaInicioGantt;
    private Integer idProceso;
    private Integer diasProcesoEnPausa;

    private boolean modificandoTDR = false;
    private UploadedFile uploadedFile;
    private String colorSegunFechaPlanificada = "VERDE";
    private List<DataExportLoteItemInsumos> relacionesLotesItemsInsumos;
    private boolean usuarioActualCorrespondeConTecnicoUACISelect;
    private BigDecimal montoAdjudicado;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id != null) {
            idProceso = Integer.valueOf(id);
        }
        if (!TextUtils.isEmpty(id)) {
            Integer idProceso = Integer.valueOf(id);
            update = true;
            objeto = delegate.cargarProceso(idProceso);
            colorSegunFechaPlanificada = delegate.getColorSegunFechaPlanificada(objeto);
            cargarRelacionesLotesItemsInsumos();
        } else {
            objeto = new ProcesoAdquisicion();
            objeto.setResponsable(null);
            objeto.setInsumos(new LinkedList());
            objeto.setItems(new LinkedList());
            objeto.setLotes(new LinkedList());
            objeto.setContratos(new LinkedList());
            objeto.setProveedores(new LinkedList());
        }

        initAnioFisscales();
        initUsuarios();
        verificarUsuarioActualCorrespondeConTecnicoUACISelect();
        reloadDataGantt();
        if (objeto.getEstadoProceso() == EstadoProcesoAdq.PAUSA) {
            calcularDiasDePausaDelProceso();
        }

    }

    /**
     * Recarga el proceso de adquisición en edición, obteniendo todos los datos de la base
     */
    public void reloadProceso() {
        if (objeto.getId() != null) {
            objeto = delegate.cargarProceso(objeto.getId());
        }

        //los metodos de adquisicion siempre se vuelven a cargar porque al variar el motnto son distintos
 
        comboAnioFiscal.setSelectedT(objeto.getAnio());
         comboUsuarios.setSelectedT(objeto.getResponsable());

        InsumosDelProceso insumoDelProcesoBean = (InsumosDelProceso) JSFUtils.getBean("insumosDelProceso");
        insumoDelProcesoBean.getInsumosDataTable(true);
        insumoDelProcesoBean.cargarMapPoInsumosRecepcionTDR();

        GestionLoteItems gestionLoteItemBean = (GestionLoteItems) JSFUtils.getBean("gestionLoteItems");
        gestionLoteItemBean.init();

        EvaluacionAdjudicacionProceso evaluacionAdjudicacionProceso = (EvaluacionAdjudicacionProceso) JSFUtils.getBean("evaluacionAdjudicacionProceso");
        evaluacionAdjudicacionProceso.init();

        if (objeto.getEstadoProceso() == EstadoProcesoAdq.PAUSA) {
            calcularDiasDePausaDelProceso();
        }
        this.cargarRelacionesLotesItemsInsumos();
        this.calcularPresupuestoAdjudicado();
    }

    /**
     * Guarda los cambios aplicados al proceso de adquisición
     */
    public void guardar() {
        try {
            BusinessException b = new BusinessException();
            if (comboAnioFiscal.getSelected() == null || comboAnioFiscal.getSelected().equals(0)) {
                b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_AÑO_FISCAL);
            }
            if (TextUtils.isEmpty(objeto.getNombre())) {
                b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_NOMBRE_PROCESO_ADQUISICION);
            }
            if (objeto != null && objeto.getId() != null) {
                if (this.habilitarCampoTecnicoUACI() && (comboUsuarios.getSelected() == null || comboUsuarios.getSelected().equals(0))) {
                    b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_TECNICO_UACI);
                }
                if (selectedMetodoId == null || selectedMetodoId.isEmpty()) {
                    b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_METODO_ADQ);
                }
                if (fechaInicioGantt == null) {
                    b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_FECHA_INICIO_GANTT);
                }
            }
            if (!b.getErrores().isEmpty()) {
                throw b;
            }
            objeto.setAnio(comboAnioFiscal.getSelectedT());
            objeto.setResponsable(comboUsuarios.getSelectedT());
            //Si se está creando el proceso, el selectMetodoId lo paso en null, si no lo convierto a entero
            Integer idMetodoSeleccionado = (selectedMetodoId == null) ? null : Integer.valueOf(selectedMetodoId);

            objeto = delegate.guardarProceso(objeto, idMetodoSeleccionado, fechaInicioGantt, ganttJson);
            reloadProceso();
            reloadDataGantt();
            reloadProceso();
            colorSegunFechaPlanificada = delegate.getColorSegunFechaPlanificada(objeto);
            RequestContext.getCurrentInstance().execute("repintarGantt();");

            String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
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
     * Cambia de estado (para atrás o adelante) el proceso de adquisición en edición
     * @param posNuevoEstado 
     */
    private void cambiarEstado(int posNuevoEstado) {
        try {
            delegate.cambiarEstadoProceso(objeto.getId(), PasosProcesoAdquisicion.getEstadoByPos(posNuevoEstado), ganttJson);
            reloadProceso();

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
     * Carga el combo de usuarios (técnicos UACI)
     */
    public void initUsuarios() {
        List<SsUsuario> l = usuarioDelegate.getUsuariosConOperacion(ConstantesEstandares.Operaciones.OPERACION_CREAR_EDITAR_PROCESO_ADQUISICION);
        comboUsuarios = new SofisComboG<>(l, "nombreCompleto");
         
        if (objeto.getResponsable() != null) {
            comboUsuarios.setSelectedT(objeto.getResponsable());
        } else {
            comboUsuarios.setSelected(null);
        }
    }

    /**
     * Con los datos actualizados del proceso, setea el Json utilizado para dibujar el Cronograma 
     */
    public void regenerarGantt() {
        try {
            if (!TextUtils.isEmpty(selectedMetodoId)) {
                ganttJson = ganttDelegate.regenerarGantt(Integer.valueOf(selectedMetodoId), fechaInicioGantt);
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
     * Devuelve una lista de todos los métodos de adquisición habilitados para seleccionar en el 
     * proceso de adquisición en edición
     * @return 
     */
    public Map<String, String> getMetodosAdquisicion() {
        Integer anioSeleccionado = null;
        if (comboAnioFiscal.getSelectedT() != null) {
            anioSeleccionado = comboAnioFiscal.getSelectedT().getAnio();
        }
        List<MetodoAdquisicion> l = ganttDelegate.getMetodosCumplenRango(objeto.getMontoTotal(), anioSeleccionado);
        Map<String, String> map = new LinkedHashMap();
        for (MetodoAdquisicion ma : l) {
            if(ma.getEsUACI() != null && ma.getEsUACI()){
                map.put(ma.getNombre(), ma.getId().toString());
            }
        }

        //vuelve a setear el metodo selecionado cada vez que se repinta
        if (objeto.getMetodoAdquisicion() == null) {
            selectedMetodoId = null;
        } else {
            selectedMetodoId = objeto.getMetodoAdquisicion().getId().toString();
        }

        return map;
    }

    /**
     *  Con los datos actualizados del proceso, setea el Json utilizado para dibujar el Cronograma 
     */
    public void reloadDataGantt() {
        ganttJson = "{\"tasks\":[]}";
        selectedMetodoId = null;
        if (objeto.getGantt() != null) {
            ganttJson = ganttDelegate.getGantt(objeto.getGantt().getId());
        }
        if (objeto.getMetodoAdquisicion() != null) {
            selectedMetodoId = objeto.getMetodoAdquisicion().getId().toString();
        }
         
        fechaInicioGantt = objeto.getMenorFechaGantt();
    }

    /**
     * Este método verifica que: 1. Todos los insumos del proceso solo podrán
     * estar participando del proceso actual. 2. Todos los insumos del proceso
     * deben tener TDR o ET, Certificado de Disponibilidad cargados y recepción
     * de TDR/ET cargado. 3. El proceso tiene un método de adquisición asignado
     * y el presupuesto total estimado del proceso es compatible con el rango
     * permitido para el método. 4. El proceso tiene un cronograma planificado
     * establecido y están marcadas las dos fechas clave: recepción del TDR/ET y
     * fecha estimada de inicio de la contratación
     *
     * @param idProceso En caso que algún punto no sea satisfactorio se mostrará
     * el mensaje adecuado. Si todos los puntos son satisfactorios, se muestra
     * un mensaje indicandolo
     */
    public void verificarConsistencia() {
        try {
            delegate.verificarConsistenciaProceso(objeto.getId());
            String texto = textMB.obtenerTexto("labels.ProcesoConsistente");
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
     * Cuando se presiona el botón de pausar el proceso, se actualiza el objeto
     * en la base y se le actualizan al objeto ProcesoAdquisicion en memoria
     * solamente los datos referidos a la pausa. También se calcula la
     * diferencia en días entre la fecha actual y la última vez que se puso en
     * pausa el proceso
     */
    public void pausarProceso() {
        try {
            objeto = delegate.pausarProceso(objeto.getId(), EstadoProcesoAdq.PAUSA);
            calcularDiasDePausaDelProceso();
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
     * Reanuda un proceso de adquisición que está en pausa, asignándole el estado: "Normal"
     */
    public void reanudarProceso() {
        try {
            objeto = delegate.pausarProceso(objeto.getId(), EstadoProcesoAdq.NORMAL);
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
     * Devuelve la cantidad de días que el proceso de adquisición en edición estuvo en pausa
     */
    private void calcularDiasDePausaDelProceso() {
        diasProcesoEnPausa = Math.round(DatesUtils.getDateDiff(objeto.getFechaUltimapausa(), new Date(), TimeUnit.DAYS));
    }

    /**
     * Verifica si el proceso de adquisición en edición está en pausa
     * @return 
     */
    public Boolean procesoEstaEnPausa() {
        return objeto.getEstadoProceso() == EstadoProcesoAdq.PAUSA;
    }

    /**
     * Verifica si el proceso de adquisición en edición está declarado desierto o sin efecto
     * @return 
     */
    public Boolean estaDesiertoOSinEfecto() {
        return (objeto.getEstadoProceso() == EstadoProcesoAdq.DESIERTO || objeto.getEstadoProceso() == EstadoProcesoAdq.SIN_EFECTO);
    }

    /**
     * Verifica si un proceso de adquisición está declarado desierto o sin efecto
     * @return 
     */
    public Boolean estaDesiertoOSinEfecto(Integer idProcesoAdq) {
        ProcesoAdquisicion proceso = delegate.cargarProceso(idProcesoAdq);
        return (proceso.getEstadoProceso() == EstadoProcesoAdq.DESIERTO || proceso.getEstadoProceso() == EstadoProcesoAdq.SIN_EFECTO);
    }

    /**
     * Declara al proceso de adquisición en edición como "Desierto"
     * @return 
     */
    public String declararDesierto() {
        String retorno = null;
        try {
            objeto = delegate.declararProcesoDesiertoOSinEfecto(objeto.getId(), EstadoProcesoAdq.DESIERTO);
            retorno = "consultaProcesoAdquisicion.xhtml?faces-redirect=true";
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } finally {
            return retorno;
        }
    }

    /**
     * Declara al proceso de adquisición en edición como "Sin efecto"
     * @return 
     */
    public String declararSinEfecto() {
        String retorno = null;
        try {
            objeto = delegate.declararProcesoDesiertoOSinEfecto(objeto.getId(), EstadoProcesoAdq.SIN_EFECTO);
            retorno = "consultaProcesoAdquisicion.xhtml?faces-redirect=true";
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } finally {
            return retorno;
        }
    }

    /**
     * Suma el monto adjudicado de los insumos asignados al proceso de adquisición en edición
     */
    private void calcularPresupuestoAdjudicado() {
        montoAdjudicado = BigDecimal.ZERO;
        for (ProcesoAdquisicionInsumo insumo : objeto.getInsumos()) {
            montoAdjudicado = montoAdjudicado.add(insumo.getMontoTotalAdjudicado());
        }
    }

    /**
     * Devuelve el nombre del permiso necesario para cambiar de estado en un proceso de adquisición
     * @return 
     */
    public String getPermisoParaCambioDeEstados() {
        return UtilsUACI.getPermisoParaCambiarEstado(objeto.getEstado());
        
    }
    /**
     * Verifica si existe al menos un ítem adjudicado en el proceso de adquisición en edición
     * @return 
     */
    public boolean hayItemsAdjudicados() {
        boolean hayAdjudicados = false;
        Iterator<ProcesoAdquisicionItem> itItems = objeto.getItems().iterator();
        while (itItems.hasNext() && !hayAdjudicados) {
            ProcesoAdquisicionItem item = itItems.next();
            if (item.getEstado() != null && item.getEstado().equals(EstadoItem.ADJUDICADO)) {
                hayAdjudicados = true;
            }
        }
        if (!hayAdjudicados) {
            Iterator<ProcesoAdquisicionLote> itLotes = objeto.getLotes().iterator();
            while (itLotes.hasNext() && !hayAdjudicados) {
                ProcesoAdquisicionLote lote = itLotes.next();
                Iterator<ProcesoAdquisicionItem> itItemsLote = lote.getItems().iterator();
                while (itItemsLote.hasNext() && !hayAdjudicados) {
                    ProcesoAdquisicionItem itemlote = itItemsLote.next();
                    if (itemlote.getEstado() != null && itemlote.getEstado().equals(EstadoItem.ADJUDICADO)) {
                        hayAdjudicados = true;
                    }
                }
            }
        }
        return hayAdjudicados;
    }
    
    // <editor-fold defaultstate="collapsed" desc="auxiliares">
    public void siguienteEstadoProceso() {
        cambiarEstado(objeto.getEstado().getPosicion() + 1);
        colorSegunFechaPlanificada = delegate.getColorSegunFechaPlanificada(objeto);
    }

    public void anteriorEstadoProceso() {
        cambiarEstado(objeto.getEstado().getPosicion() - 1);
        colorSegunFechaPlanificada = delegate.getColorSegunFechaPlanificada(objeto);
    }

    public boolean esUltimoEstado() {
        return PasosProcesoAdquisicion.values().length == (objeto.getEstado().getPosicion() + 1);
    }

    public boolean esPrimerEstado() {
        return 0 == (objeto.getEstado().getPosicion());
    }

    public void initAnioFisscales() {
        List<AnioFiscal> l = emd.getEntities(AnioFiscal.class.getName(), "anio");
        comboAnioFiscal = new SofisComboG<>(l, "anio");
        if (objeto.getId() != null) {
            comboAnioFiscal.setSelectedT(objeto.getAnio());
        } else {
            comboAnioFiscal.setSelected(0);
        }
    }

    public String getSecueniaObjeto() {
        if (objeto == null || objeto.getId() == null) {
            return " - ";
        }
        return String.valueOf(objeto.getSecuenciaAnio()) + " - " + String.valueOf(objeto.getSecuenciaNumero());
    }

    public void cargarProcesoTDR() {
        idProceso = objeto.getId();

        modificandoTDR = false;
        initTDR();

    }

    public void modificarTDR() {
        modificandoTDR = true;
    }

    public void fileUploadListener(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            if (uploadedFile != null) {
                tempTDR.setTempUploadedFile(ArchivoUtils.getDataFile(uploadedFile));
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    protected TDR tempTDR;

    public void initTDR() {
        tempTDR = objeto.getTdr();
        if (idProceso != null) {
            tempTDR = delegate.getTDRProceso(idProceso);
        }

        if (tempTDR == null) {
            tempTDR = new TDR();
            RequestContext.getCurrentInstance().execute("$('#anadirTDRProceso').modal('show');");
        } else {
            RequestContext.getCurrentInstance().execute("$('#verTDRProcesoModal').modal('show');");
        }
    }

    public void guardarTDR() {
        try {
            if (TextUtils.isEmpty(tempTDR.getContenido()) && tempTDR.getTempUploadedFile() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_TDR_VACIO_AL_MENOS_ESCRIBA_TEXTO_O_SELECCIONE_ARCHIVO);
                throw b;
            }

            objeto = delegate.saveTDRProceso(idProceso, tempTDR);

            RequestContext.getCurrentInstance().execute("$('#anadirTDRProceso').modal('hide');");
            RequestContext.getCurrentInstance().execute("$('#verTDRProcesoModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /*El combo para seleccionar el técnico UACI debe aparecer a partir del paso: Revisión Jefe UACI*/
    public Boolean habilitarCampoTecnicoUACI() {
        return objeto.getEstado() != null && objeto.getEstado().getPosicion() >= PasosProcesoAdquisicion.REVISION_JEFE_UACI.getPosicion();
    }

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

    public void validarCoincidenciaAnioFiscalYAnioInicioCrono() {
        try {
            if (fechaInicioGantt != null && comboAnioFiscal.getSelectedT() != null) {
                Integer anioFiscal = null;
                Integer anioInicioCrono = null;

                anioFiscal = comboAnioFiscal.getSelectedT().getAnio();

                Calendar calFechaInicioGantt = Calendar.getInstance();
                calFechaInicioGantt.setTime(fechaInicioGantt);
                anioInicioCrono = calFechaInicioGantt.get(Calendar.YEAR);

                if (!anioFiscal.equals(anioInicioCrono)) {
                    fechaInicioGantt = null;
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_ANIO_FISCAL_Y_AÑO_INICIO_CRONO_DISTINTOS);
                    throw b;
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
     * cargarRelacionesLotesItemsInsumos. Crea una lista que contiene la
     * información de todos los lotes, items e insumos del proceso
     */
    private void cargarRelacionesLotesItemsInsumos() {
        relacionesLotesItemsInsumos = new LinkedList<DataExportLoteItemInsumos>();

        if (objeto.getLotes() != null) {
            for (ProcesoAdquisicionLote lote : objeto.getLotes()) {
                for (ProcesoAdquisicionItem item : lote.getItems()) {
                    for (RelacionProAdqItemInsumo relacion : item.getRelItemInsumos()) {
                        String insumo = relacion.getInsumo().getInsumo().getNombre() + " (" + relacion.getInsumo().getPoInsumo().getId() + ")";
                        DataExportLoteItemInsumos dataExport = new DataExportLoteItemInsumos(lote.getNombre(), item.getNombre(), insumo, relacion.getCantidad().toString());
                        relacionesLotesItemsInsumos.add(dataExport);
                    }
                }
            }
        }

        if (objeto.getItems() != null) {
            for (ProcesoAdquisicionItem item : objeto.getItems()) {
                for (RelacionProAdqItemInsumo relacion : item.getRelItemInsumos()) {
                    String insumo = relacion.getInsumo().getInsumo().getNombre() + " (" + relacion.getInsumo().getPoInsumo().getId() + ")";
                    DataExportLoteItemInsumos dataExport = new DataExportLoteItemInsumos(item.getNombre(), insumo, relacion.getCantidad().toString());
                    relacionesLotesItemsInsumos.add(dataExport);
                }
            }
        }

    }

    /**
     * verificarUsuarioActualCorrespondeConTecnicoUACISelect. Verifica que el
     * usuario en sesion sea el mismo que el seleccionado para trabajar el
     * proceso a partir del paso "Revisión Técnico UACI"
     */
    private void verificarUsuarioActualCorrespondeConTecnicoUACISelect() {
        setUsuarioActualCorrespondeConTecnicoUACISelect(true);
        if (comboUsuarios.getSelectedT() != null) {
            if (objeto.getEstado().getPosicion() > PasosProcesoAdquisicion.REVISION_JEFE_UACI.getPosicion() && !comboUsuarios.getSelectedT().getUsuCod().equals(usrInfo.getUserCod())) {
                setUsuarioActualCorrespondeConTecnicoUACISelect(false);
            }
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public ProcesoAdquisicion getObjeto() {
        return objeto;
    }

    public void setObjeto(ProcesoAdquisicion objeto) {
        this.objeto = objeto;
    }

    public Integer getOpenTab() {
        return openTab;
    }

    public void setOpenTab(Integer openTab) {
        this.openTab = openTab;
    }

    public boolean isUpdate() {
        return update;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscal() {
        return comboAnioFiscal;
    }

    public void setComboAnioFiscal(SofisComboG<AnioFiscal> comboAnioFiscal) {
        this.comboAnioFiscal = comboAnioFiscal;
    }

    public ProcesoAdquisicionDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(ProcesoAdquisicionDelegate delegate) {
        this.delegate = delegate;
    }

    public SofisComboG<SsUsuario> getComboUsuarios() {
        return comboUsuarios;
    }

    public void setComboUsuarios(SofisComboG<SsUsuario> comboUsuarios) {
        this.comboUsuarios = comboUsuarios;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getSelectedMetodoId() {
        return selectedMetodoId;
    }

    public void setSelectedMetodoId(String selectedMetodoId) {
        this.selectedMetodoId = selectedMetodoId;
    }

    public Date getFechaInicioGantt() {
        return fechaInicioGantt;
    }

    public void setFechaInicioGantt(Date fechaInicioGantt) {
        this.fechaInicioGantt = fechaInicioGantt;
    }

    public void setGanttJson(String ganttJson) {
        this.ganttJson = ganttJson;
    }

    public String getGanttJson() {
        return ganttJson;
    }

    public void setIdProceso(Integer idProceso) {
        this.idProceso = idProceso;
    }

    public Integer getIdProceso() {
        return idProceso;
    }

    public Integer getDiasProcesoEnPausa() {
        return diasProcesoEnPausa;
    }

    public void setDiasProcesoEnPausa(Integer diasProcesoEnPausa) {
        this.diasProcesoEnPausa = diasProcesoEnPausa;
    }

    public TDR getTempTDR() {
        return tempTDR;
    }

    public void setTempTDR(TDR tempTDR) {
        this.tempTDR = tempTDR;
    }

    public boolean isModificandoTDR() {
        return modificandoTDR;
    }

    public void setModificandoTDR(boolean modificandoTDR) {
        this.modificandoTDR = modificandoTDR;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getColorSegunFechaPlanificada() {
        return colorSegunFechaPlanificada;
    }

    public void setColorSegunFechaPlanificada(String colorSegunFechaPlanificada) {
        this.colorSegunFechaPlanificada = colorSegunFechaPlanificada;
    }

    public List<DataExportLoteItemInsumos> getRelacionesLotesItemsInsumos() {
        return relacionesLotesItemsInsumos;
    }

    public void setRelacionesLotesItemsInsumos(List<DataExportLoteItemInsumos> relacionesLotesItemsInsumos) {
        this.relacionesLotesItemsInsumos = relacionesLotesItemsInsumos;
    }

    public boolean isUsuarioActualCorrespondeConTecnicoUACISelect() {
        return usuarioActualCorrespondeConTecnicoUACISelect;
    }

    public void setUsuarioActualCorrespondeConTecnicoUACISelect(boolean usuarioActualCorrespondeConTecnicoUACISelect) {
        this.usuarioActualCorrespondeConTecnicoUACISelect = usuarioActualCorrespondeConTecnicoUACISelect;
    }

    public BigDecimal getMontoAdjudicado() {
        return montoAdjudicado;
    }

    public void setMontoAdjudicado(BigDecimal montoAdjudicado) {
        this.montoAdjudicado = montoAdjudicado;
    }
    // </editor-fold>    

}
