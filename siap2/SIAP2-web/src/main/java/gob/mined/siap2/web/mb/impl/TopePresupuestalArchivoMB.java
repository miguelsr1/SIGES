package gob.mined.siap2.web.mb.impl;

import com.mined.siap2.to.TopePresupuestalArchivoTo;
import gob.mined.siap2.business.GestionPresupuestoEscolarBean;
import gob.mined.siap2.business.RelacionGesPresEsAnioFiscalBean;
import gob.mined.siap2.business.ejbs.impl.CategoriaPresupuestoEscolarBean;
import gob.mined.siap2.business.ejbs.impl.GeneracionTechosDesdeArchivoBean;
import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.enums.TipoMonto;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import gob.mined.siap2.entities.enums.TipoSubcomponente;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.filtros.FiltroComponentePresupuestoEscolar;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ViewScoped
@Named(value = "topePresupuestalArchivoMB")
public class TopePresupuestalArchivoMB implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private SofisComboG<AnioFiscal> comboAnioFiscal = new SofisComboG<>();
    private SofisComboG<CategoriaPresupuestoEscolar> comboCategoria = new SofisComboG<>();
    private SofisComboG<ComponentePresupuestoEscolar> comboComponentes = new SofisComboG<>();
    private SofisComboG<TipoPresupuestoAnio> comboTipoPresupuestoAnio = new SofisComboG<>();
    private SofisComboG<TipoMonto> comboTipoMonto = new SofisComboG<>();
    private SofisComboG<RelacionGesPresEsAnioFiscal> comboPresupuesto = new SofisComboG<>();

    @Inject
    private GeneracionTechosDesdeArchivoBean generacionTechosDesdeArchivoBean;
    
    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private EntityManagementDelegate emd;

    @Inject
    private GestionPresupuestoEscolarBean escolarBean;

    @Inject
    private RelacionGesPresEsAnioFiscalBean relPresupuestoDao;
    @Inject
    private GeneralBean generalBean;
    @Inject
    private CategoriaPresupuestoEscolarBean categoriaBean;

    private UploadedFile uploadedFile;
    private TopePresupuestalArchivoTo crearDTO;
    
    private Boolean editable = Boolean.TRUE;
    private RelacionGesPresEsAnioFiscal relPresupuesto;
    
    @Inject
    private UsuarioInfo usuarioInfo;
    private SsUsuario usuarioLogueado;
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            crearDTO = new TopePresupuestalArchivoTo();
            usuarioLogueado = usuarioInfo.getUsuario();
            
            String compoId = buscarIdentificador("compoId");
            String opcion = buscarIdentificador("op");
            if (!TextUtils.isEmpty(compoId)) {
                relPresupuesto = (RelacionGesPresEsAnioFiscal) emd.getEntityById(RelacionGesPresEsAnioFiscal.class.getCanonicalName(), Integer.valueOf(compoId));
                if(relPresupuesto != null) {
                    this.editable = Boolean.FALSE;
                    comboAnioFiscal.setSelectedT(relPresupuesto.getAnioFiscal());
                    comboCategoria.setSelectedT(relPresupuesto.getComponentePresupuestoEscolar().getCategoriaPresupuestoEscolar());
                    cargarComponentesPorCategoria();
                    comboComponentes.setSelectedT(relPresupuesto.getComponentePresupuestoEscolar());
                    comboTipoPresupuestoAnio.setSelectedT(relPresupuesto.getTipo());
                    cargarPresupuestosPorSubcomponente();
                    comboPresupuesto.setSelectedT(relPresupuesto);
                    
                    
                    if (!TextUtils.isEmpty(opcion)) {
                        if("1".equals(opcion)) {
                            comboTipoMonto.setSelectedT(TipoMonto.FORMULADO);
                        } else if("2".equals(opcion)) {
                            comboTipoMonto.setSelectedT(TipoMonto.APROBADO);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    public void cargarCombos() {
        try {
            comboAnioFiscal = new SofisComboG<>(generalBean.getAniosFiscalesPlanificacion(), "nombre");
            comboAnioFiscal.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);

            comboCategoria = new SofisComboG<>(categoriaBean.getAllCategoriaPresupuestoEscolarHabilitados(), "nombre");
            comboCategoria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
            comboTipoPresupuestoAnio = new SofisComboG(Arrays.asList(TipoPresupuestoAnio.values()), "label");
            comboTipoPresupuestoAnio.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        
            comboComponentes = new SofisComboG<>();
            comboComponentes.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);

            List<TipoMonto> lista = new ArrayList(Arrays.asList(TipoMonto.values()));
            comboTipoMonto = new SofisComboG<>(lista, "label");
            comboTipoMonto.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
            comboPresupuesto = new SofisComboG<>();
            comboPresupuesto.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }

    }

    /**
     * Este método limpia los objetos de contexto para la creacion y filtro
     */
    public void cleanObject() {
        comboAnioFiscal.clearSelectedT();
        comboCategoria.clearSelectedT();
        comboComponentes.clearSelectedT();
        comboTipoPresupuestoAnio.clearSelectedT();
    }

    /**
     * Este método busca la llave de los parametros enviados desde la vista y
     * obtiene su valor
     *
     * @param parametro
     * @return
     */
    public String buscarIdentificador(String parametro) {
        try {
            String valor = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parametro);
            if (valor == null) {
                valor = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get(parametro);
            }
            return valor;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    public void cargarComponentesPorCategoria() {
        try {
            comboComponentes.clearSelectedT();
            comboComponentes = new SofisComboG<>();
            if (comboCategoria.getSelectedT() != null) {

                FiltroComponentePresupuestoEscolar filtro = new FiltroComponentePresupuestoEscolar();
                filtro.setCategoriaComponenteId(comboCategoria.getSelectedT().getId());
                filtro.setHabilitado(Boolean.TRUE);
                filtro.setIncluirCampos(new String[]{"nombre", "version"});
                filtro.setTipoSubcomponente(TipoSubcomponente.CARGA_DESDE_ARCHIVO);
                comboComponentes = new SofisComboG<>(escolarBean.getComponentesPresupuestoEscolarByFiltro(filtro), "nombre");
            }
            comboComponentes.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    public void cargarPresupuestosPorSubcomponente() {
        try {
            comboPresupuesto.clearSelectedT();
            comboPresupuesto = new SofisComboG<>();
            if (this.comboComponentes.getSelectedT() != null && this.comboTipoPresupuestoAnio.getSelectedT() != null 
                    && this.comboAnioFiscal.getSelectedT() != null) {
                
                List<RelacionGesPresEsAnioFiscal> listado = relPresupuestoDao.getRelacionesAnioFiscalPorSubcomponente(comboComponentes.getSelectedT().getId(), 
                                            comboTipoPresupuestoAnio.getSelectedT(),
                                            comboAnioFiscal.getSelectedT().getId());
                comboPresupuesto = new SofisComboG<>(listado, "descripcion");
            } 
            comboPresupuesto.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    public void fileUploadListener(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            if (uploadedFile != null) {
                crearDTO.setFile(ArchivoUtils.getDataFile(uploadedFile));
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Metodo utilizado para guardar y/o actualizar un registro de
     * TopePresupuestal
     *
     * @return
     */
    public String guardarActualizar() {
        try {
            List<SsUsuario> usuarios = new ArrayList(); 
            crearDTO.setAnioFiscal(this.comboAnioFiscal.getSelectedT());
            crearDTO.setGesPresEs(this.comboComponentes.getSelectedT());
            crearDTO.setTipoMonto(this.comboTipoMonto.getSelectedT());
            crearDTO.setRelGesPres(this.comboPresupuesto.getSelectedT());
            crearDTO.setTipo(this.comboTipoPresupuestoAnio.getSelectedT());
            
            
            
            if(usuarioLogueado != null && usuarioLogueado.getUsuId() != null) {
                usuarios.add(usuarioLogueado);
            }
            
         
            crearDTO.setUsuarios(usuarios);
            generacionTechosDesdeArchivoBean.crearDesdeArchivo(crearDTO);

            RelacionGesPresEsAnioFiscal relacionGesPresEsAnioFiscal = this.comboPresupuesto.getSelectedT();
            relacionGesPresEsAnioFiscal.setProcesoEnCurso(Boolean.TRUE);
            
            relacionGesPresEsAnioFiscal = relPresupuestoDao.crearActualizar(relacionGesPresEsAnioFiscal);
            
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('show');");
            
        } catch (BusinessException be) {
            jSFUtils.mostrarErrorByPropertieCode(be.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }

    public String confirmar() {
        if((this.editable == null || !this.editable) && this.relPresupuesto != null) { 
            return "crearEditarGestionPresupuestoEscolar.xhtml?faces-redirect=true&id="+this.relPresupuesto.getComponentePresupuestoEscolar().getId() + "&tab=1";
        }
        return "consultaTopePresupuestal.xhtml?faces-redirect=true";
    }
    
    /**
     * Dirige al sitio de Tope Presupuestal
     *
     * @return
     */
    public String cerrar() {
        return "consultaTopePresupuestal.xhtml?faces-redirect=true";
    }

    /**
     * Metodo que verifica si el estado actual del registro es Aprobacion
     *
     * @param tp
     * @return
     */
    public boolean verificarEstadoAprobado(TopePresupuestal tp) {
        return tp != null && tp.getEstado() != null && tp.getEstado().equals(EstadoTopePresupuestal.APROBACION);
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscal() {
        return comboAnioFiscal;
    }

    public void setComboAnioFiscal(SofisComboG<AnioFiscal> comboAnioFiscal) {
        this.comboAnioFiscal = comboAnioFiscal;
    }

    public SofisComboG<CategoriaPresupuestoEscolar> getComboCategoria() {
        return comboCategoria;
    }

    public void setComboCategoria(SofisComboG<CategoriaPresupuestoEscolar> comboCategoria) {
        this.comboCategoria = comboCategoria;
    }

    public SofisComboG<ComponentePresupuestoEscolar> getComboComponentes() {
        return comboComponentes;
    }

    public void setComboComponentes(SofisComboG<ComponentePresupuestoEscolar> comboComponentes) {
        this.comboComponentes = comboComponentes;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public TopePresupuestalArchivoTo getCrearDTO() {
        return crearDTO;
    }

    public void setCrearDTO(TopePresupuestalArchivoTo crearDTO) {
        this.crearDTO = crearDTO;
    }

    public SofisComboG<TipoMonto> getComboTipoMonto() {
        return comboTipoMonto;
    }

    public void setComboTipoMonto(SofisComboG<TipoMonto> comboTipoMonto) {
        this.comboTipoMonto = comboTipoMonto;
    }

    public SofisComboG<RelacionGesPresEsAnioFiscal> getComboPresupuesto() {
        return comboPresupuesto;
    }

    public void setComboPresupuesto(SofisComboG<RelacionGesPresEsAnioFiscal> comboPresupuesto) {
        this.comboPresupuesto = comboPresupuesto;
    }

    public SofisComboG<TipoPresupuestoAnio> getComboTipoPresupuestoAnio() {
        return comboTipoPresupuestoAnio;
    }

    public void setComboTipoPresupuestoAnio(SofisComboG<TipoPresupuestoAnio> comboTipoPresupuestoAnio) {
        this.comboTipoPresupuestoAnio = comboTipoPresupuestoAnio;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public RelacionGesPresEsAnioFiscal getRelPresupuesto() {
        return relPresupuesto;
    }

    public void setRelPresupuesto(RelacionGesPresEsAnioFiscal relPresupuesto) {
        this.relPresupuesto = relPresupuesto;
    }

}
