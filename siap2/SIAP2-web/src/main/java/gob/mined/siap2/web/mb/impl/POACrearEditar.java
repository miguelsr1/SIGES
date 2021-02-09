/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.POA;
import gob.mined.siap2.entities.data.impl.POAMetaAnual;
import gob.mined.siap2.entities.data.impl.POAActividadMeta;
import gob.mined.siap2.entities.data.impl.POARiesgoPOA;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadosPOA;
import gob.mined.siap2.entities.enums.OrigenRiesgo;
import gob.mined.siap2.entities.enums.ValoracionRiesgo;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroIndicadorPrograma;
import gob.mined.siap2.filtros.FiltroUnidadTecnica;
import gob.mined.siap2.filtros.FiltroPrograma;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UnidadTecnicaDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralDelegate;
import gob.mined.siap2.web.delegates.impl.IndicadorDelegate;
import gob.mined.siap2.web.delegates.impl.POADelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.mb.UtilsMB;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaCE")
public class POACrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    UtilsMB utilsMB;
    
    @Inject
    EntityManagementDelegate emd;
    
    @Inject
    GeneralDelegate peled;

    @Inject
    VersionesDelegate versionDelegate;

    @Inject
    POADelegate poaDelegate;
    
    @Inject
    IndicadorDelegate indicadorDelegate;
    
    @Inject
    UnidadTecnicaDelegate unidadTecnicaDelegate;
    
    @Inject
    ProgramaDelegate progamaDelegate;
    
    @Inject
    private UsuarioInfo usuarioInfo;
    
    @Inject
    UsuarioDelegate usuarioDelegate;
    
    private POA poa;
    private POAMetaAnual meta;
    private POAActividadMeta actividad;
    protected String motivoRechazo;
    private Integer anioFiscalId;
  //  private SofisComboG<EstadosPOA> comboEstadosPOA;
    private UnidadTecnica unidadTecnicaSelected;
    private Programa programaInstitucionalSelected;
    private Indicador indicadorSelected;
    private Stack<POA> stack = new Stack();
    List<POAActividadMeta> listaActividades;
    private Boolean metaActual = false;
    @PostConstruct
    public void init() {

        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            poa = poaDelegate.obtenerPOAPorId(Integer.valueOf(id));
           // comboEstadosPOA.setSelectedT(poa.getEstado());
            anioFiscalId = poa.getAnio() != null ? poa.getAnio().getId() : null;
            if(poa.getMetasAnuales() == null) {
                poa.setMetasAnuales(new ArrayList());
            }
        } else {
            poa = initPlanOperativoAnual();
        }
    }
    
    /**
     * Crea e inicializa los valores de un programa institucional
     *
     * @return
     */
    private POA initPlanOperativoAnual() {
        POA p = new POA();
        p.setMetasAnuales(new ArrayList());
        p.setEstado(EstadosPOA.EN_ELABORACION);
        return p;
    }
    
    public void reloadMontosTRimestralesTarea() {
        Integer monto = 0;
        if(meta != null) {
            if(meta.getProgramaPrimerTrimestre() != null) {
                monto = monto + meta.getProgramaPrimerTrimestre();
            }
            if(meta.getProgramaSegundoTrimestre() != null) {
                monto = monto + meta.getProgramaSegundoTrimestre();
            }
            if(meta.getProgramaTercerTrimestre() != null) {
                monto = monto + meta.getProgramaTercerTrimestre();
            }
            if(meta.getProgramaCuartoTrimestre() != null) {
                monto = monto + meta.getProgramaCuartoTrimestre();
            }
        }
        
        meta.setTotal(monto);
    }
    /**
     * Guarda el poa en edición
     *
     * @return
     */
    public String guardar() {
        try {
            if(anioFiscalId != null && anioFiscalId > 0) {
                poa.setAnio(versionDelegate.getAniosFiscalesPorId(anioFiscalId));  
            }
            if(metaActual!= null && metaActual) {
                if(meta.getPoa() == null) {  
                    poa.getMetasAnuales().add(meta);
                    meta.setPoa(poa);
                } 
            } 
            
            poa = poaDelegate.guardar(poa, Boolean.FALSE, usuarioInfo.getUserCod(), Boolean.FALSE, Boolean.FALSE);
            if(metaActual!= null && metaActual) {
               metaActual = false; 
            } else {
                return "consultaPOA.xhtml?faces-redirect=true";
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
        return null;
    }
    
    /**
     * Este método se utiliza para rechazar el POA en edición
     * 
     * @throws IOException 
     */
    public void rechazarPOA() throws IOException {
        try {
            poaDelegate.rechazarPOA(poa.getId(), motivoRechazo);
            FacesContext.getCurrentInstance().getExternalContext().redirect("consultaPOA.xhtml");
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
    
    public void aprobarPOA() throws IOException {
        try {
            poaDelegate.aprobarPOA(poa.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("consultaPOA.xhtml");
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
     * Manda el poa a aprobación
     * @throws IOException 
     */    
    public void enviar() throws IOException {
        try {
            poaDelegate.enviarPOA(poa.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("consultaPOA.xhtml");
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
     * Agrega un programa institucional a un programa presupuestario
     */
    public void saveTareaAnual() {
        try {
            if(meta.getPoa() == null) {  
                poa.getMetasAnuales().add(meta);
                meta.setPoa(poa);
            }
            RequestContext.getCurrentInstance().execute("$('#anadirMetaPOA').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Agrega un programa institucional a un programa presupuestario
     */
    public void saveActividadMetaAnual() {
        try {
            if(actividad.getMeta() == null) {
                listaActividades.add(actividad);
                meta.setActividades(listaActividades);
                
                if(meta.getPoa() == null) {  
                    poa.getMetasAnuales().add(meta);
                    meta.setPoa(poa);
                }
                actividad.setMeta(meta);
            }
            
            RequestContext.getCurrentInstance().execute("$('#anadirActividadMeta').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    public void loadActividad(String index) {
        if (TextUtils.isEmpty(index)) {
            actividad = initActividad();
        } else {
            actividad = meta.getActividades().get(Integer.valueOf(index).intValue());
        }
        this.metaActual = true;
    }
    
     public void loadMeta(String index) {
        if (TextUtils.isEmpty(index)) {
            meta = initMetaAnual();
        } else {
            meta = poa.getMetasAnuales().get(Integer.valueOf(index).intValue());
        }
        if(meta != null) {
            listaActividades = meta.getActividades();
        }
        this.metaActual = true;
    }
    
    /**
     * Elimina de la lista una meta anual
     *
     * @param toRemove
     */
    public void eliminarMetaAnual(POAMetaAnual toRemove) {
        try {
            poa.getMetasAnuales().remove(toRemove);
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
     * Elimina de la lista una actividad de la meta anual
     * presupuestarios o supuestos asociados
     *
     * @param toRemove
     */
    public void eliminarActividadMetaAnual(POAActividadMeta toRemove) {
        try {
            listaActividades.remove(toRemove);
            meta.setActividades(listaActividades);
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
     * Crea e inicializa los valores de un programa institucional
     *
     * @return
     */
    private POAMetaAnual initMetaAnual() {
        POAMetaAnual meta = new POAMetaAnual();
        meta.setProgramaPrimerTrimestre(0);
        meta.setProgramaSegundoTrimestre(0);
        meta.setProgramaTercerTrimestre(0);
        meta.setProgramaCuartoTrimestre(0);
        meta.setActividades(new LinkedList());
        return meta;
    }
    
    private POAActividadMeta initActividad() {
        POAActividadMeta actividad = new POAActividadMeta();
        return actividad;
    }
    /**
     * Dirige el sitio a la consulta de programas presupuestarios
     *
     * @return
     */
    public String cerrar() {
        this.metaActual = false;
        return null;
    }
    
    /**
     * Dirige el sitio a la consulta de programas presupuestarios
     *
     * @return
     */
    public String salir() {
        this.metaActual = false;
        return "consultaPOA.xhtml?faces-redirect=true";
    }
    
    /**
     * Devuelve el nombre del botón que sirve para guardar o aceptar según sea
     * el caso
     *
     * @return
     */
    public String getLabelSaveButton() {
        if (stack.empty()) {
            return "labels.Guardar";
        } else {
            return "labels.Aceptar";
        }
    }

    /**
     * Devuelve el nombre del botón que sirve para cancelar o ir atrás según sea
     * el caso
     *
     * @return
     */
    public String getLabelCancelButton() {
        if (stack.empty()) {
            return "labels.Cancelar";
        } else {
            return "labels.Atras";
        }
    }
    
    public List<UnidadTecnica> completeUnidadesTecnicas(String query){
        try {
            //if(query != null && !query.isEmpty()) {
                FiltroUnidadTecnica filtro = new FiltroUnidadTecnica();
                filtro.setNombre(query);
                filtro.setCodUsuario(usuarioInfo.getUserCod());
                filtro.setAscending(new boolean[]{true});
                filtro.setUnidadOperativa(Boolean.TRUE);
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(11L);
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                filtro.setOperacion(ConstantesEstandares.Operaciones.GESTION_POA_UNIDADES_APOYO);
                return usuarioDelegate.getUTDeUsuarioConOperacionByNombre(filtro);
            //}
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    public List<UnidadTecnica> completeUnidadesTecnicasActividad(String query){
        try {
            return unidadTecnicaDelegate.getUTBynombre(query);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    public List<Programa> completeProgramasInstitucionales(String query){
        try {
           // if(query != null && !query.isEmpty()) {
                FiltroPrograma filtro = new FiltroPrograma();
                filtro.setNombre(query);
                filtro.setAscending(new boolean[]{true});
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(11L);
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                return progamaDelegate.obtenerProgramasPorFiltro(filtro);
           // }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    public List<Indicador> completeIndicador(String query){
        try {
            if(poa != null && poa.getProgramaInstitucional() != null && poa.getProgramaInstitucional().getId() > 0) {
                //if(query != null && !query.isEmpty()) {
                    FiltroIndicadorPrograma filtro = new FiltroIndicadorPrograma();
                    filtro.setProgramaId(poa.getProgramaInstitucional().getId());
                    filtro.setIndicadorNombre(query);
                    filtro.setAscending(new boolean[]{true});
                    filtro.setOrderBy(new String[]{"indicador.nombre"});
                    filtro.setMaxResults(11L);
                    filtro.setIncluirCampos(new String[]{"indicador.id","indicador.nombre","indicador.version"});
                    return indicadorDelegate.getIndicadorByNombreAndPrograma(filtro);
                //}
            }
            return null;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
        // <editor-fold defaultstate="collapsed" desc="MANEJO DE RIESGOS">
    protected POARiesgoPOA tmpRiesgo;

    /**
     * Este método se utiliza para inicializar un riesgo, en caso que no exista lo crea.
     */
    public void initRiesgo() {
        if (tmpRiesgo == null) {
            tmpRiesgo = new POARiesgoPOA();
        }
    }

    /**
     * Este método se utiliza para guardar un riesgo
     */
    public void saveRiesgo() {
        try {
            if (!poa.getRiesgos().contains(tmpRiesgo)) {
                poa.getRiesgos().add(tmpRiesgo);
            }
            guardar();
            RequestContext.getCurrentInstance().execute("$('#anadirRiesgo').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método se utiliza para eliminar un riesgo
     */
    public void eliminarRiesgo() {
        try {
            poa.getRiesgos().remove(tmpRiesgo);
            guardar();
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // </editor-fold>
    
    public OrigenRiesgo[] getOrigenRiesgos() {
        return OrigenRiesgo.values();
    }

    public ValoracionRiesgo[] getValoracionRiesgos() {
        return ValoracionRiesgo.values();
    }
    
    public POA getPoa() {
        return poa;
    }

    public void setPoa(POA poa) {
        this.poa = poa;
    }

    public Integer getAnioFiscalId() {
        return anioFiscalId;
    }

    public void setAnioFiscalId(Integer anioFiscalId) {
        this.anioFiscalId = anioFiscalId;
    }

    public UnidadTecnica getUnidadTecnicaSelected() {
        return unidadTecnicaSelected;
    }

    public void setUnidadTecnicaSelected(UnidadTecnica unidadTecnicaSelected) {
        this.unidadTecnicaSelected = unidadTecnicaSelected;
    }

    public Programa getProgramaInstitucionalSelected() {
        return programaInstitucionalSelected;
    }

    public void setProgramaInstitucionalSelected(Programa programaInstitucionalSelected) {
        this.programaInstitucionalSelected = programaInstitucionalSelected;
    }

    public Stack<POA> getStack() {
        return stack;
    }

    public void setStack(Stack<POA> stack) {
        this.stack = stack;
    }

    public Indicador getIndicadorSelected() {
        return indicadorSelected;
    }

    public void setIndicadorSelected(Indicador indicadorSelected) {
        this.indicadorSelected = indicadorSelected;
    }

    public POAMetaAnual getMeta() {
        return meta;
    }

    public void setMeta(POAMetaAnual meta) {
        this.meta = meta;
    }

    public Boolean getMetaActual() {
        return metaActual;
    }

    public void setMetaActual(Boolean metaActual) {
        this.metaActual = metaActual;
    }

    public POAActividadMeta getActividad() {
        return actividad;
    }

    public void setActividad(POAActividadMeta actividad) {
        this.actividad = actividad;
    }

    public List<POAActividadMeta> getListaActividades() {
        return listaActividades;
    }

    public void setListaActividades(List<POAActividadMeta> listaActividades) {
        this.listaActividades = listaActividades;
    }

    public POARiesgoPOA getTmpRiesgo() {
        return tmpRiesgo;
    }

    public void setTmpRiesgo(POARiesgoPOA tmpRiesgo) {
        this.tmpRiesgo = tmpRiesgo;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }
    
    
}