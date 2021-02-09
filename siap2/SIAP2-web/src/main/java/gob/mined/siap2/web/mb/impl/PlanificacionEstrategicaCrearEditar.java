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
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.persistence.dao.reference.EntityReference;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.PlanificacionEstrategicaDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
@Named(value = "planificacionEstrategicaCE")
public class PlanificacionEstrategicaCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    PlanificacionEstrategicaDelegate planificacion;

    private boolean update = false;
    private PlanificacionEstrategica objeto;
    private List<LineaEstrategica> lineasEstrategicas = new LinkedList<>();    
    private String idLineaToAdd;


    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = (PlanificacionEstrategica) emd.getEntityById(PlanificacionEstrategica.class.getName(), Integer.valueOf(id));
            lineasEstrategicas.addAll(planificacion.getLineas(objeto.getId()));

        } else {
            objeto = new PlanificacionEstrategica();
            objeto.setEstado(EstadoComun.VIGENTE);
        }
    }

    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            planificacion.crearActualizarPlanificacion(objeto,new HashSet<LineaEstrategica>(lineasEstrategicas));
            return "consultaPlanificacionEstrategica.xhtml?faces-redirect=true";
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
     * Dirige el sitio a la página de consulta de planificación estratégica
     * @return 
     */
    public String cerrar() {
        return "consultaPlanificacionEstrategica.xhtml?faces-redirect=true";
    }

    /**
     * Devuelve la lista de posibles estados de planificación
     * @return 
     */
    public EstadoComun[] getEstadoPlanificacion() {
        return EstadoComun.values();
    }
    
   
    /**
     * Elimina una línea estratégica luego de validar si es posible
     * @param le 
     */
    public void eliminarLinea(LineaEstrategica le) {
        try {
            if (objeto.getId()!= null){
                planificacion.chequearEliminableLinea(objeto.getId(), le.getId());                
            }
            lineasEstrategicas.remove(le);
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
     * Agrega una línea estratégica
     */
    public void addLinea() {
        lineasEstrategicas.add((LineaEstrategica) emd.getEntityById(LineaEstrategica.class.getName(), Integer.valueOf(idLineaToAdd)));
        RequestContext.getCurrentInstance().execute("$('#addLineaEstrategica').modal('hide');");
    }

    /**
     * Devuelve la lista de líneas estratégicas disponibles
     * @return 
     */
    public Map<String, String> getLineasDisponibles() {
        Map<String, String> lineas = new LinkedHashMap();         

        String[] propiedades = {"id", "nombre"};
        String className = LineaEstrategica.class.getName();                  
        MatchCriteriaTO vigente = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", EstadoComun.VIGENTE);
      
        String[] orderBy = {"nombre"};
        boolean[] asc = {true};               
        List<EntityReference<Integer>> ll = emd.getEntitiesReferenceByCriteria(className, vigente, null, null, propiedades, orderBy, asc);

        for (EntityReference l : ll) {
            if (!containsLinea(l)) {
                lineas.put((String) l.getPropertyMap().get("nombre"), String.valueOf(l.getPropertyMap().get("id"))); // value, label, the value to choose and label to appear fo the user
            }
        }
        return lineas;
    }

    /**
     * Verifica sí una línea estratégica está dentro de una lista de estas
     * @param toCheck
     * @return 
     */  
    private boolean containsLinea(EntityReference toCheck) {
        for (LineaEstrategica l : lineasEstrategicas) {
            if (l.getId().equals(toCheck.getPropertyMap().get("id"))) {
                return true;
            }
        }
        return false;
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public static Logger getLogger() {
        return logger;
    }

    public List<LineaEstrategica> getLineasEstrategicas() {
        return lineasEstrategicas;
    }

    public void setLineasEstrategicas(List<LineaEstrategica> lineasEstrategicas) {
        this.lineasEstrategicas = lineasEstrategicas;
    }


    public void setIdLineaToAdd(String idLineaToAdd) {
        this.idLineaToAdd = idLineaToAdd;
    }

    public String getIdLineaToAdd() {
        return idLineaToAdd;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public PlanificacionEstrategica getObjeto() {
        return objeto;
    }

    public void setObjeto(PlanificacionEstrategica objeto) {
        this.objeto = objeto;
    }

    // </editor-fold>
    
  

}
