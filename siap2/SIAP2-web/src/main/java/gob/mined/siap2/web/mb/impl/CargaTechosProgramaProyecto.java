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
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestarioTechoAnio;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTechoPresupuestarioAnio;
import gob.mined.siap2.entities.data.impl.ProyectoFuente;
import gob.mined.siap2.entities.data.impl.ProyectoTechoPresupuestarioAnio;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ConfiguracionDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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

/**
 * Este backing bean implementa los eventos y lógica de presentación de la
 * página de carga de techos a nivel de proyecto
 *
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "cargaTechosProgramaProyecto")
public class CargaTechosProgramaProyecto implements Serializable {

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
    TextMB textMB;

    List<ProgramaPresupuestario> programas;
    List<ProgramaPresupuestario> programasAMostrar;
    List<Integer> aniosPlanificacion;
    PlanificacionEstrategica planificacion;
    private String idPlanificacion = null;

    @PostConstruct
    public void init() {
        idPlanificacion = null;
        loadProgramas();
    }

    
    /**
     * Carga los programas para la planificación seleccionada
     */
    public void loadProgramas() {
        if (TextUtils.isEmpty(idPlanificacion)) {
            programas = new LinkedList();
            planificacion = null;
        } else {
            //se traen los programas que cumplan con la planificacion
            programas = programaDelegate.getProgramasPresupuestariosParaCargaDeTechos(Integer.valueOf(idPlanificacion));//se cargan los anios de la planificacion en una lista
            planificacion = (PlanificacionEstrategica) emd.getEntityById(PlanificacionEstrategica.class.getName(), Integer.valueOf(idPlanificacion));
        }
        completarAnios();
    }

    
    /**
     * completa los años que no tienen techos para la planificación
     */
    public void completarAnios() {
        aniosPlanificacion = new LinkedList();
        programasAMostrar = new LinkedList();
        if (planificacion != null) {
            Integer desde = DatesUtils.getYearOfDate(planificacion.getDesde());
            Integer hasta = DatesUtils.getYearOfDate(planificacion.getHasta());
            for (int anio = desde; anio <= hasta; anio++) {
                //busca el anio fiscal
                AnioFiscal anioFiscal = null;
                List<AnioFiscal> aniosFiscales = emd.findByOneProperty(AnioFiscal.class.getName(), "anio", anio);
                if (!aniosFiscales.isEmpty()) {
                    anioFiscal = aniosFiscales.get(0);
                } else {
                    anioFiscal = AnioFiscalUtils.crearAnioFiscal(anio);
                }

                aniosPlanificacion.add(anio);
                //se añade el techo si no existe en el programa
                for (ProgramaPresupuestario programa : programas) {
                    agrreglarTechoProgramasConPlanificacion(programa, anioFiscal, anio - desde);
                    for (ProgramaPresupuestario ssubprograma : programa.getProgramasPresupuestarios()) {
                        agrreglarTechoProgramasConPlanificacion(ssubprograma, anioFiscal, anio - desde);
                        for (Proyecto proyecto : ssubprograma.getProyectos()) {
                            agrreglarTechosProyecto(proyecto, anioFiscal, anio - desde);
                            for (ProyectoFuente aporte : proyecto.getFuentesProyecto()){
                                agrreglarTechosAporteProyecto(aporte, anioFiscal,  anio - desde);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Para el programa pasado por parámetro se asegura que el techo del año
     * fiscal este en la posición que corresponde. Si no existe lo crea
     *
     * 
     * @param p
     * @param anioFiscal
     * @param pos 
     */
    public static void agrreglarTechoProgramasConPlanificacion(ProgramaPresupuestario p, AnioFiscal anioFiscal, int pos) {
        int iter = 0;
        boolean encontro = false;

        while (iter < p.getTechosPresupuestarios().size()) {
            if (p.getTechosPresupuestarios().get(iter).getAnioFiscal().getAnio().equals(anioFiscal.getAnio())) {
                encontro = true;
                break;
            }
            iter++;
        }
        //si lo encontro en otra posicion lo cambia de lugar
        if (encontro && iter != pos) {
            ProgramaPresupuestarioTechoAnio techo = p.getTechosPresupuestarios().get(iter);
            p.getTechosPresupuestarios().remove(iter);
            p.getTechosPresupuestarios().add(pos, techo);
        }

        if (!encontro) {
            ProgramaPresupuestarioTechoAnio techo = new ProgramaPresupuestarioTechoAnio();
            techo.setProgramaPresupuestario(p);
            techo.setAnioFiscal(anioFiscal);
            techo.setTecho(BigDecimal.ZERO);
            p.getTechosPresupuestarios().add(pos, techo);
        }
    }

    /**
     * Para el proyecto pasado por parámetro se asegura que el techo del año
     * fiscal este en la posición que corresponde. Si no existe lo crea
     *
     * 
     * @param p
     * @param anioFiscal
     * @param pos 
     */
    public static void agrreglarTechosProyecto(Proyecto p, AnioFiscal anioFiscal, int pos) {
        int iter = 0;
        boolean encontro = false;

        while (iter < p.getTechos().size()) {
            if (p.getTechos().get(iter).getAnioFiscal().getAnio().equals(anioFiscal.getAnio())) {
                encontro = true;
                break;
            }
            iter++;
        }
        //si lo encontro en otra posicion lo cambia de lugar
        if (encontro && iter != pos) {
            ProyectoTechoPresupuestarioAnio techo = p.getTechos().get(iter);
            p.getTechos().remove(iter);
            p.getTechos().add(pos, techo);
        }

        if (!encontro) {
            ProyectoTechoPresupuestarioAnio techo = new ProyectoTechoPresupuestarioAnio();
            techo.setProyecto(p);
            techo.setAnioFiscal(anioFiscal);
            techo.setTecho(BigDecimal.ZERO);
            p.getTechos().add(pos, techo);
        }
    }
    
    
    /**
     * Para la fuente del proyecto pasado por parámetro se asegura que el techo del año
     * fiscal este en la posición que corresponde. Si no existe lo crea
     *
     * 
     * @param p
     * @param anioFiscal
     * @param pos 
     */
    public static void agrreglarTechosAporteProyecto(ProyectoFuente aporte, AnioFiscal anioFiscal, int pos) {
        int iter = 0;
        boolean encontro = false;

        while (iter < aporte.getTechos().size()) {
            if (aporte.getTechos().get(iter).getAnioFiscal().getAnio().equals(anioFiscal.getAnio())) {
                encontro = true;
                break;
            }
            iter++;
        }
        //si lo encontro en otra posicion lo cambia de lugar
        if (encontro && iter != pos) {
            ProyectoAporteTechoPresupuestarioAnio techo = aporte.getTechos().get(iter);
            aporte.getTechos().remove(iter);
            aporte.getTechos().add(pos, techo);
        }

        if (!encontro) {
            ProyectoAporteTechoPresupuestarioAnio techo = new ProyectoAporteTechoPresupuestarioAnio();
            techo.setAporte(aporte);
            techo.setAnioFiscal(anioFiscal);
            techo.setTecho(BigDecimal.ZERO);
            aporte.getTechos().add(pos, techo);
        }
    }

    /**
     * Remueve los valores vacíos a nivel de programa
     * 
     * @param programa 
     */
    private void removeAniosVacios(ProgramaPresupuestario programa) {
        Iterator<ProgramaPresupuestarioTechoAnio> iterTechoAnio = programa.getTechosPresupuestarios().iterator();
        while (iterTechoAnio.hasNext()) {
            ProgramaPresupuestarioTechoAnio techo = iterTechoAnio.next();
            if (techo.getAnioFiscal().getId() == null) {
                iterTechoAnio.remove();
            }
        }
        for (ProgramaPresupuestario hijo : programa.getProgramasPresupuestarios()) {
            removeAniosVacios(hijo);
        }
        for (Proyecto p : programa.getProyectos()) {
            removeAniosVacios(p);
        }
    }

    /**
     * Remueve los valores vacíos a nivel de proyecto
     * 
     * @param proyecto 
     */
    private void removeAniosVacios(Proyecto proyecto) {
        Iterator<ProyectoTechoPresupuestarioAnio> iterTechoAnio = proyecto.getTechos().iterator();
        while (iterTechoAnio.hasNext()) {
            ProyectoTechoPresupuestarioAnio techo = iterTechoAnio.next();
            if (techo.getAnioFiscal().getId() == null) {
                iterTechoAnio.remove();
            }
        }
        for (ProyectoFuente aporte: proyecto.getFuentesProyecto()){
            removeAniosVacios(aporte) ;
        }
    }
    
    
    /**
     * Remueve los años vacíos
     * 
     * @param aporte 
     */
    private void removeAniosVacios(ProyectoFuente aporte) {
        Iterator<ProyectoAporteTechoPresupuestarioAnio> iterTechoAnio = aporte.getTechos().iterator();
        while (iterTechoAnio.hasNext()) {
            ProyectoAporteTechoPresupuestarioAnio techo = iterTechoAnio.next();
            if (techo.getAnioFiscal().getId() == null) {
                iterTechoAnio.remove();
            }
        }
    }
    

    
     /**
      * Guarda todos los techos
      * 
      * @return 
      */
    public String guardar()  {
        try {
            for (ProgramaPresupuestario programa : programas) {
                removeAniosVacios(programa);
            }
            Set<String> noExactos = programaDelegate.guardarTechos(programas);
            
            
            if (noExactos.isEmpty()){
                String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            }else{
                String[] params ={TextUtils.join(", ", noExactos)};
                String texto = textMB.obtenerTextoConParams("labels.ADV_EN_LOS_SIGUIENTES_CASOS_1_EL_VALOR_ESTAVLECIDO_NO_COCNUERDA_CON_HIJO", params);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, texto, texto));
            }
            
            //se recarga la lista de elementos
            loadProgramas();
            
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");     
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

    /**
     * retorna el total de los techos par aun programa
     * 
     * @param programa
     * @return 
     */
    public BigDecimal getTotalPrograma(ProgramaPresupuestario programa) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < aniosPlanificacion.size(); i++) {
            total = total.add(programa.getTechosPresupuestarios().get(i).getTecho());
        }
        return total;
    }

    /**
     * Retorna el total de los techos para un proyecto
     * 
     * @param proyecto
     * @return 
     */
    public BigDecimal getTotalProyecto(Proyecto proyecto) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < aniosPlanificacion.size(); i++) {
            total = total.add(proyecto.getTechos().get(i).getTecho());
        }
        return total;
    }

    /**
     * Retorna el total de los techos para una una fuente
     * 
     * @param aporte
     * @return 
     */
     public BigDecimal getTotalAporte(ProyectoFuente aporte) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < aniosPlanificacion.size(); i++) {
            total = total.add(aporte.getTechos().get(i).getTecho());
        }
        return total;
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

    public List<ProgramaPresupuestario> getProgramas() {
        return programas;
    }

    public void setProgramas(List<ProgramaPresupuestario> programas) {
        this.programas = programas;
    }

    public PlanificacionEstrategica getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(PlanificacionEstrategica planificacion) {
        this.planificacion = planificacion;
    }

    public List<ProgramaPresupuestario> getProgramasAMostrar() {
        return programasAMostrar;
    }

    public void setProgramasAMostrar(List<ProgramaPresupuestario> programasAMostrar) {
        this.programasAMostrar = programasAMostrar;
    }

    public List<Integer> getAniosPlanificacion() {
        return aniosPlanificacion;
    }

    public void setAniosPlanificacion(List<Integer> aniosPlanificacion) {
        this.aniosPlanificacion = aniosPlanificacion;
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
