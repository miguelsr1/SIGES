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
import gob.mined.siap2.entities.data.impl.TechoPlanificacionAccionCentral;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ConfiguracionDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.AccionCentralDelegate;
import gob.mined.siap2.web.delegates.impl.PlanificacionEstrategicaDelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.presentacion.tipos.DataMontoAnio;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * Este método corresponde al backing bean de la carga de techos de una acción central.
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "cargaTechosAccionCentralCE")
public class CargaTechosAccionCentral implements Serializable {

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
    AccionCentralDelegate accionCentralD;
    @Inject
    PlanificacionEstrategicaDelegate planificacionDelegate;

    private List<UnidadTecnica> direciones;
    private List<DataMontoAnio> montosUT;

    List<Integer> aniosPlanificacion;

    private boolean incluirSubprograma = true;
    private String idPlanificacion = null;
    private PlanificacionEstrategica planificacion;
    

    @PostConstruct
    public void init() {
        direciones = versionDelegate.getUnidadesTecnicasDireccion();
        idPlanificacion = null;
        loadProgramas();
    }

    /**
     * Este método realiza la carga de programas de una planificación.
     */
    public void loadProgramas() {
        aniosPlanificacion = new LinkedList();
        montosUT= new LinkedList();
        planificacion= null;

        //en caso de no tener planificacion no hace nada
        if (!TextUtils.isEmpty(idPlanificacion)) {
            planificacion = versionDelegate.getPlanificacionEstrategica(Integer.valueOf(idPlanificacion));            
            
            for (TechoPlanificacionAccionCentral montoUT : planificacion.getTechosAccionCentral()) {
                List<TechoPlanificacionAccionCentral> l = getMontosUT(montoUT.getUnidadTecnica());
                l.add(montoUT);
            }
            
            Integer desde = DatesUtils.getYearOfDate(planificacion.getDesde());
            Integer hasta = DatesUtils.getYearOfDate(planificacion.getHasta());
            for (int anio = desde; anio <= hasta; anio++) {
                aniosPlanificacion.add(anio);
                AnioFiscal anioFiscal=null;
                List<AnioFiscal> aniosFiscales= emd.findByOneProperty(AnioFiscal.class.getName(), "anio", anio);
                if (!aniosFiscales.isEmpty()){
                    anioFiscal = aniosFiscales.get(0);
                }else{
                    anioFiscal = AnioFiscalUtils.crearAnioFiscal(anio);
                }
                //se añade el techo para la direccion si no existe
                for (UnidadTecnica ut : direciones) {                    
                    agrreglarTechoParaDireccion(ut, anioFiscal, anio - desde);
                }
            }
        }
    }

    /**
     * Este método realiza el cálculo de techos por dirección.
     * @param direccion
     * @param anioFiscal
     * @param pos 
     */
    private void agrreglarTechoParaDireccion(UnidadTecnica direccion, AnioFiscal anioFiscal, int pos) {
        int iter = 0;
        boolean encontro = false;
        List<TechoPlanificacionAccionCentral> montos = getMontosUT(direccion);
        
        while (iter < montos.size()) {
            if (montos.get(iter).getAnioFiscal().getAnio().equals(anioFiscal.getAnio()) ) {
                encontro = true;
                break;
            }
            iter++;
        }
        //si lo encontro en otra posicion lo cambia de lugar
        if (encontro && iter != pos) {
            TechoPlanificacionAccionCentral techo = montos.get(iter);
            montos.remove(iter);
            montos.add(pos, techo);
        }

        if (!encontro) {
            TechoPlanificacionAccionCentral techo = new TechoPlanificacionAccionCentral();
            techo.setUnidadTecnica(direccion);
            techo.setAnioFiscal(anioFiscal);
            techo.setMonto(BigDecimal.ZERO);
            techo.setPlanificacion(planificacion);
            montos.add(pos, techo);
        }
    }
    
    /**
     * Este método realiza el cálculo de montos por ut.
     * @param UT
     * @return 
     */
    public List<TechoPlanificacionAccionCentral> getMontosUT(UnidadTecnica UT){
        for(DataMontoAnio monto :montosUT){
            if (monto.getUt().equals(UT)){
                return monto.getMontoAnios();
            }
        }
        DataMontoAnio monto= new DataMontoAnio();
        monto.setMontoAnios(new LinkedList());
        monto.setUt(UT);
        montosUT.add(monto);
        return monto.getMontoAnios();
        
    }
    
    
    /**
     * Este método guarda los techos de acción central.
     * @return 
     */
    public String guardar() {
        try {
            planificacion.getTechosAccionCentral().clear();
            for (DataMontoAnio data :montosUT){
                for (TechoPlanificacionAccionCentral techo: (List<TechoPlanificacionAccionCentral>)data.getMontoAnios()){
                    if (techo.getAnioFiscal().getId()!= null){
                        planificacion.getTechosAccionCentral().add(techo);
                    }
                }
            }
            planificacionDelegate.crearActualizarTechos(planificacion);
            return "consultaAccionCentral.xhtml?faces-redirect=true";
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

    public List<DataMontoAnio> getMontosUT() {
        return montosUT;
    }

    public void setMontosUT(List<DataMontoAnio> montosUT) {
        this.montosUT = montosUT;
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
