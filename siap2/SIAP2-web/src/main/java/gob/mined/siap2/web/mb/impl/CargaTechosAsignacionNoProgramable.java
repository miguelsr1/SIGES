/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.utils.AnioFiscalUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.TechoAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ConfiguracionDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.AsignacionNoProgramableDelegate;
import gob.mined.siap2.web.delegates.impl.PlanificacionEstrategicaDelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página de carga de techos de asignaciones no programables
 * 
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "cargaTechosAsignacionNP")
public class CargaTechosAsignacionNoProgramable implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    ProgramaDelegate programaDelegate;
    @Inject
    ConfiguracionDelegate configuracionDelegate;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    AsignacionNoProgramableDelegate asignacionNoProgramableDelegate;
    @Inject
    PlanificacionEstrategicaDelegate planificacionDelegate;

    private List<UnidadTecnica> direciones;
    private List<AsignacionNoProgramable> asignacionesEnPlanificacion;

    List<Integer> aniosPlanificacion;

    private boolean incluirSubprograma = true;
    private String idPlanificacion = null;
    private PlanificacionEstrategica planificacion;
    

    @PostConstruct
    public void init() {
        direciones = versionDelegate.getUnidadesTecnicasDireccion();
        idPlanificacion = null;
        loadAsignacionesNP();
    }

    /**
     * Este método carga las asignaciones no programables correspondientes a la planificación seleccionada
     */
   public void loadAsignacionesNP() {
        if (!TextUtils.isEmpty(idPlanificacion)) {
            planificacion = versionDelegate.getPlanificacionEstrategica(Integer.valueOf(idPlanificacion));  
            asignacionesEnPlanificacion=asignacionNoProgramableDelegate.getAsignacionesEnPlanificacion(planificacion.getId());
        }else{
            asignacionesEnPlanificacion= new LinkedList();
            planificacion= null;
        }
        completarAnios();
   }
           
   /**
    * completa los años que no tienen techos para la planificación
    */
    public void completarAnios() {
        aniosPlanificacion = new LinkedList();
        if (planificacion!= null) {            
            Integer desde = DatesUtils.getYearOfDate(planificacion.getDesde());
            Integer hasta = DatesUtils.getYearOfDate(planificacion.getHasta());
            for (int anio = desde; anio <= hasta; anio++) {
                aniosPlanificacion.add(anio);
                AnioFiscal anioFiscal = null;
                List<AnioFiscal> aniosFiscales = emd.findByOneProperty(AnioFiscal.class.getName(), "anio", anio);
                if (!aniosFiscales.isEmpty()) {
                    anioFiscal = aniosFiscales.get(0);
                }else{
                    anioFiscal = AnioFiscalUtils.crearAnioFiscal(anio);
                }
                //se añade el techo para la direccion si no existe
                for (AsignacionNoProgramable a : asignacionesEnPlanificacion) {                    
                    agrreglarTechoParaDireccion(a, anioFiscal, anio - desde);
                }
            }
        }
    }

    /**
     * Para la asignación pasada por parámetro se asegura que el techo del año fiscal este en la posición que corresponde.
     * Si no existe lo crea
     * 
     * @param a
     * @param anioFiscal
     * @param pos 
     */
    private void agrreglarTechoParaDireccion(AsignacionNoProgramable a, AnioFiscal anioFiscal, int pos) {
        int iter = 0;
        boolean encontro = false;
        List<TechoAsignacionNoProgramable> montos = a.getTechosPlanificacion();
        
        while (iter < montos.size()) {
            if (montos.get(iter).getAnioFiscal().getAnio().equals(anioFiscal.getAnio()) ) {
                encontro = true;
                break;
            }
            iter++;
        }
        //si lo encontro en otra posicion lo cambia de lugar
        if (encontro && iter != pos) {
            TechoAsignacionNoProgramable techo = montos.get(iter);
            montos.remove(iter);
            montos.add(pos, techo);
        }

        if (!encontro) {
            TechoAsignacionNoProgramable techo = new TechoAsignacionNoProgramable();
            techo.setAsignacionNoProgramable(a);
            techo.setAnioFiscal(anioFiscal);
            techo.setMonto(BigDecimal.ZERO);
            montos.add(pos, techo);
        }
    }
    
    

    /**
     * Guarda los techos
     * 
     * @return 
     */
    public String guardar() {
        try {
            //se remueven los anios fiscales vacios
            for (AsignacionNoProgramable a : asignacionesEnPlanificacion) {
                Iterator<TechoAsignacionNoProgramable> iter = a.getTechosPlanificacion().iterator();
                while (iter.hasNext()){
                    TechoAsignacionNoProgramable t = iter.next();
                    if (t.getAnioFiscal().getId() == null){
                        iter.remove();
                    }
                }
            }
            asignacionNoProgramableDelegate.guardarTechosAsignaiconesNoProgramables(asignacionesEnPlanificacion);
            return "consultaAsignacionesNoProgramables.xhtml?faces-redirect=true";
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        completarAnios();
        return null;
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public ProgramaDelegate getProgramaDelegate() {
        return programaDelegate;
    }

    public void setProgramaDelegate(ProgramaDelegate programaDelegate) {
        this.programaDelegate = programaDelegate;
    }

    public ConfiguracionDelegate getConfiguracionDelegate() {
        return configuracionDelegate;
    }

    public void setConfiguracionDelegate(ConfiguracionDelegate configuracionDelegate) {
        this.configuracionDelegate = configuracionDelegate;
    }

    public PlanificacionEstrategica getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(PlanificacionEstrategica planificacion) {
        this.planificacion = planificacion;
    }
    

    public List<UnidadTecnica> getDireciones() {
        return direciones;
    }

    public void setDireciones(List<UnidadTecnica> direciones) {
        this.direciones = direciones;
    }

    public List<AsignacionNoProgramable> getAsignacionesEnPlanificacion() {
        return asignacionesEnPlanificacion;
    }

    public void setAsignacionesEnPlanificacion(List<AsignacionNoProgramable> asignacionesEnPlanificacion) {
        this.asignacionesEnPlanificacion = asignacionesEnPlanificacion;
    }

   


    public List<Integer> getAniosPlanificacion() {
        return aniosPlanificacion;
    }

    public void setAniosPlanificacion(List<Integer> aniosPlanificacion) {
        this.aniosPlanificacion = aniosPlanificacion;
    }

    public boolean isIncluirSubprograma() {
        return incluirSubprograma;
    }

    public void setIncluirSubprograma(boolean incluirSubprograma) {
        this.incluirSubprograma = incluirSubprograma;
    }

   

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public String getIdPlanificacion() {
        return idPlanificacion;
    }

    public void setIdPlanificacion(String idPlanificacion) {
        this.idPlanificacion = idPlanificacion;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    // </editor-fold>
}
