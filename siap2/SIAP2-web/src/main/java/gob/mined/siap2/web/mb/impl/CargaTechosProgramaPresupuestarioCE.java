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
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ConfiguracionDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaDelegate;
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
 * Este backing bean implementa los eventos y lógica de presentación de la
 * página de carga de techos a nivel de programa presupuestario
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "cargaTechosProgramaPresupuestarioCE")
public class CargaTechosProgramaPresupuestarioCE implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    ProgramaDelegate programaDelegate;
    @Inject
    ConfiguracionDelegate configuracionDelegate;

    List<ProgramaPresupuestario> programas;
    List<ProgramaPresupuestario> programasAMostrar;
    List<Integer> aniosPlanificacion;
    PlanificacionEstrategica planificacion;

    private boolean incluirSubprograma = true;
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
                    agrreglarTechoConPlanificacion(programa, anioFiscal, anio - desde);
                    if (incluirSubprograma) {
                        for (ProgramaPresupuestario ssubprograma : programa.getProgramasPresupuestarios()) {
                            agrreglarTechoConPlanificacion(ssubprograma, anioFiscal, anio - desde);
                        }
                    }
                }
            }

            //se carga el arbol para mostrar
            for (ProgramaPresupuestario programa : programas) {
                programasAMostrar.add(programa);
                if (incluirSubprograma) {
                    for (ProgramaPresupuestario subprograma : programa.getProgramasPresupuestarios()) {
                        programasAMostrar.add(subprograma);
                    }
                }
            }
        }
    }

    /**
     * Para el programa pasado por parámetro se asegura que el techo del año
     * fiscal este en la posición que corresponde. Si no existe lo crea
     *
     * @param p
     * @param anioFiscal
     * @param pos
     */
    public static void agrreglarTechoConPlanificacion(ProgramaPresupuestario p, AnioFiscal anioFiscal, int pos) {
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
     * Elimina los techos vacíos que no se cargaron
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
    }

    /**
     * Guarda los techos
     *
     * @return
     */
    public String guardar() {
        try {
            for (ProgramaPresupuestario programa : programas) {
                removeAniosVacios(programa);
            }
            programaDelegate.guardarTechos(programas);
            return "consultaProgramaPresupuestario.xhtml?faces-redirect=true";
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
     * Este método se usa para retornar una clase css y según la clase armar el
     * dibujo de la estructura de árbol en la vista
     *
     * @param programa
     * @return
     */
    public String getClasesParaFilaTabla(ProgramaPresupuestario programa) {
        if (programa.getProgramaPresupuestario() == null) {
            return "fila-programa";
        }
        //ya se que es subprograma
        int index = programasAMostrar.indexOf(programa);
        if (index + 1 == programasAMostrar.size()) {
            return "fila-subprograma ultimo-subprograma ";
        }
        //el siguiente es un programa entonces es el ultimo
        ProgramaPresupuestario siguiente = programasAMostrar.get(index + 1);
        if (siguiente.getProgramaPresupuestario() == null) {
            return "fila-subprograma ultimo-subprograma ";
        }
        return "fila-subprograma";
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
