/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.Supuesto;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralDelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.UtilsMB;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "programaInstitucionalCE")
public class ProgramaInstitucionalCrearEditar implements Serializable {

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
    ProgramaDelegate progDelegate;
    @Inject
    VersionesDelegate versionDelegate;

    private boolean update = false;
    private ProgramaInstitucional objeto;
    private Supuesto supuesto;
    private Stack<ProgramaInstitucional> stack = new Stack();
    protected DualListModel<LineaEstrategica> lineasEstrategicas;    
    private String idPlanificacion;
    private String idLinea;
    private String idToEliminar;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        idPlanificacion=null;
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = (ProgramaInstitucional) versionDelegate.getProgramaInstitucional(Integer.valueOf(id));
            if (objeto.getPlanificacionEstrategica()!=null){
                idPlanificacion = String.valueOf(objeto.getPlanificacionEstrategica().getId());
            }
            loadLineasDisponibles(true);
            //en caso de ser un subprograma carga la línea
            if (objeto.getProgramaInstitucional()!=null){
                LineaEstrategica l = getLineaEstrategicaDeSubprograma(objeto);
                if (l!=null){
                    idLinea=String.valueOf(l.getId());
                }
            }

        } else {
            objeto = initPrograma();
            loadLineasDisponibles(false);
        }
    }

    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            if (objeto.getProgramaInstitucional() == null){
                //setea las lineas estrategicas en caso de no ser subprograma (si no las mantiene)
                objeto.setLineasEstrategicas(new HashSet( lineasEstrategicas.getTarget()));
            }else{
                //en caso de ser un subprograma setea la línea que eligio
                objeto.setLineasEstrategicas(new LinkedHashSet<LineaEstrategica>());
                if (!TextUtils.isEmpty(idLinea)){
                    LineaEstrategica linea =(LineaEstrategica) emd.getEntityById(LineaEstrategica.class.getName(), Integer.valueOf(idLinea));
                    objeto.getLineasEstrategicas().add(linea);
                }
            }
            if (stack.isEmpty()) {                
                progDelegate.crearActualizarProgramaInstitucional(objeto, Integer.valueOf(idPlanificacion));                
                return "consultaProgramaInstitucional.xhtml?faces-redirect=true";
            } else {
                ProgramaInstitucional padre = stack.pop();
                if (objeto.getProgramaInstitucional() == null) {
                    objeto.setProgramaInstitucional(padre);
                    padre.getProgramasInstitucionales().add(objeto);
                }
                objeto = padre;
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
     * Dirige el sitio a la consulta de programas institucionales
     * @return 
     */
    public String cerrar() {
        if (stack.isEmpty()) {
            return "consultaProgramaInstitucional.xhtml?faces-redirect=true";
        } else {
            objeto = stack.pop();
        }
        return null;
    }
    
    /**
     * Devuelve un Map con las líneas estratégicas y sus respectivos id
     * @return 
     */
    public Map<String, String> getLineasParaSubprograma(){
        Map<String, String> map = new LinkedHashMap<>();
        List<LineaEstrategica> l = lineasEstrategicas.getTarget();
        for (LineaEstrategica iter: l){
            map.put(iter.getNombre(), String.valueOf(iter.getId()));
        }
        return map;
    }

    /**
     * Carga un subprograma
     * @param index 
     */
    public void loadSubprograma(String index) {
        ProgramaInstitucional p;
        if (TextUtils.isEmpty(index) ) {
            p = initPrograma();
        } else {
            p = objeto.getProgramasInstitucionales().get(Integer.valueOf(index).intValue());
        }
        idLinea = null;        
        //si es un subprograma y tiene línea se carga
        if (p.getProgramaInstitucional()!=null && !p.getLineasEstrategicas().isEmpty()){
            idLinea= String.valueOf(getLineaEstrategicaDeSubprograma(p).getId());
        }
        stack.push(objeto);
        objeto = p;
    }
    
    /**
     * Devuelve la línea estratégica asociada a un subprograma
     * @param subprograma
     * @return 
     */
    public LineaEstrategica getLineaEstrategicaDeSubprograma(ProgramaInstitucional subprograma){
        if (subprograma.getLineasEstrategicas().isEmpty()){
            return null;
        }
        Iterator iter = subprograma.getLineasEstrategicas().iterator();
        return (LineaEstrategica)iter.next();
    }

    /**
     * Crea o carga un supuesto desde la lista de supuestos del programa institucional en edición
     * @param index 
     */
    public void loadSupuesto(String index) {
        if (TextUtils.isEmpty(index)) {
            supuesto = new Supuesto();
        } else {
            supuesto = objeto.getSupuestos().get(Integer.valueOf(index).intValue());
        }
    }

    /**
     * Agrega un supuesto a la lista de supuestos del programa institucional en edición
     */
    public void saveSupuesto() {
        try {
            if (supuesto.getPrograma() == null) {
                objeto.getSupuestos().add(supuesto);
                supuesto.setPrograma(objeto);
            }
            RequestContext.getCurrentInstance().execute("$('#anadirSupuesto').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Carga una lista de líneas estratégicas disponibles
     * @param conservarOriginales 
     */
    public void loadLineasDisponibles(boolean conservarOriginales){
        List<LineaEstrategica> disponibles = new LinkedList<>();
        if (!TextUtils.isEmpty(idPlanificacion)){
            disponibles =versionDelegate.getLineasEstrategicasVigetnes(Integer.valueOf(idPlanificacion));
        }
        List<LineaEstrategica> enUso = new LinkedList<>();
        if (conservarOriginales){
            ProgramaInstitucional programa = objeto;
            if (objeto.getProgramaInstitucional()!= null){
                programa= objeto.getProgramaInstitucional();
            }
            for (LineaEstrategica l : programa.getLineasEstrategicas()){
                enUso.add(l);
                if (disponibles.contains(l)){
                    disponibles.remove(l);
                }
            }            
        }
        lineasEstrategicas = new DualListModel(disponibles, enUso);        
    }

    /**
     * Elimina un supuesto
     * @param toRemove 
     */
    public void eliminarSupuesto(Supuesto toRemove) {
        try {
            objeto.getSupuestos().remove(toRemove);
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
     * Elimina un subprograma institucional si no tiene programas institucionales o supuestos asociados
     * @param toRemove 
     */
    public void eliminarSubprograma(ProgramaInstitucional toRemove) {
        try {
            if (toRemove.getProgramasInstitucionales().size() > 0 || toRemove.getSupuestos().size() > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA);
                throw b;
            }
            objeto.getProgramasInstitucionales().remove(toRemove);
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
     * Verifica si es un sub programa o no
     * @return 
     */
    public boolean isSubprograma() {
        if (objeto.getProgramaInstitucional() != null) {
            return true;
        }
        if (!stack.isEmpty()) {
            return true;
        }

        return false;

    }
      

    /**
     * Devuelve la lista de líneas estratégicas vigentes
     * @return 
     */
    public Map<String, String> getLineas() {
        if (!TextUtils.isEmpty(idPlanificacion)){        
            return utilsMB.getLineasEstrategicasVigetnes(idPlanificacion); 
        }else{
            return new HashMap();
        }
    }
    
    /**
     * Devuelve el nombre de una planificación estratégica
     * @return 
     */
    public String getNombrePlanificacion() {
        if (!TextUtils.isEmpty(idPlanificacion)) {
            PlanificacionEstrategica p = (PlanificacionEstrategica) emd.getEntityById(PlanificacionEstrategica.class.getName(), Integer.valueOf(idPlanificacion));
            return p.getNombre();
        } else {
            return null;
        }
    }

    /**
     * Crea e inicializa los valores de un programa institucional
     * @return 
     */
    private ProgramaInstitucional initPrograma() {
        ProgramaInstitucional p = new ProgramaInstitucional();
        p.setProgramasInstitucionales(new LinkedList());
        p.setSupuestos(new LinkedList());
        p.setEstado(EstadoComun.VIGENTE);
        return p;
    }

    /**
     * Devuelve el nombre del botón que sirve para guardar o aceptar según sea el caso
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
     * Devuelve el nombre del botón que sirve para cancelar o ir atrás según sea el caso
     * @return 
     */
    public String getLabelCancelButton() {
        if (stack.empty()) {
            return "labels.Cancelar";
        } else {
            return "labels.Atras";
        }
    }

    /**
     * Devuelve una lista de nombres
     * @return 
     */
    public List<String> getNombrePadres() {
        List l = new LinkedList();
        
        ProgramaInstitucional inicio;
        if (stack.isEmpty() ){
            inicio = objeto;
        }else{
            inicio = stack.get(0);
        }
        inicio = inicio.getProgramaInstitucional();
        while (inicio != null) {
            l.add(0, getNombrePrograma(inicio.getNombre()));
            inicio = inicio.getProgramaInstitucional();
        }
        
        Iterator iter = stack.iterator();
        while (iter.hasNext()) {
            ProgramaInstitucional p = (ProgramaInstitucional) iter.next();
            l.add(getNombrePrograma(p.getNombre()));
        }

        if (TextUtils.isEmpty(objeto.getNombre())) {
            l.add(l.size(),"Actual");
        } else {
            l.add(l.size(),objeto.getNombre());
        }
        
        
        return l;
    }

    /*
    Devuelve el nombre de un programa institucional
    */
    private String getNombrePrograma(String nombre) {
        if (TextUtils.isEmpty(nombre)) {
            return ("Programa Sin Nombre");
        }
        return nombre;
    }
    
    
    
    /**
     * Devuelve una clase CSS según la cantidad de programas institucionales
     * @return 
     */
    public String getSubClassLevel() {
        return getCssLvl(stack.size());
    }

    /**
     * Devuelve una clase CSS
     * @return 
     */
    public String getCssLvl(Integer lvl){
        if (lvl<5){
            return "nivel-interno-" + lvl;
        }else{
            return "nivel-interno-5"; 
        }
        
    }
    
    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }
    
    public Stack<ProgramaInstitucional> getStack() {
        return stack;
    }

    public void setStack(Stack<ProgramaInstitucional> stack) {
        this.stack = stack;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public String getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(String idLinea) {
        this.idLinea = idLinea;
    }
    

    public Supuesto getSupuesto() {
        return supuesto;
    }

    public String getIdToEliminar() {
        return idToEliminar;
    }

    public void setIdToEliminar(String idToEliminar) {
        this.idToEliminar = idToEliminar;
    }

    public void setSupuesto(Supuesto supuesto) {
        this.supuesto = supuesto;
    }

    public String getIdPlanificacion() {
        return idPlanificacion;
    }

    public void setIdPlanificacion(String idPlanificacion) {
        this.idPlanificacion = idPlanificacion;
    }

    public DualListModel<LineaEstrategica> getLineasEstrategicas() {
        return lineasEstrategicas;
    }

    public void setLineasEstrategicas(DualListModel<LineaEstrategica> lineasEstrategicas) {
        this.lineasEstrategicas = lineasEstrategicas;
    }


    
    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public GeneralDelegate getPeled() {
        return peled;
    }

    public void setPeled(GeneralDelegate peled) {
        this.peled = peled;
    }

    public ProgramaInstitucional getObjeto() {
        return objeto;
    }

    public void setObjeto(ProgramaInstitucional objeto) {
        this.objeto = objeto;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
