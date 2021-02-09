/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesConfiguracion;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.entities.data.impl.ProgramacionFinancieraAccionCentral;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ConfiguracionDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.TextoDelegate;
import gob.mined.siap2.web.delegates.impl.AccionCentralDelegate;
import gob.mined.siap2.web.delegates.impl.PlanificacionEstrategicaDelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.presentacion.tipos.DataMontoAnio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "cargaTechosProgramacionFinancieraAC")
public class CargaTechosProgramacionFinancieraAccionCentral implements Serializable {

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
    @Inject
    TextoDelegate textoDelegate;

    private List<UnidadTecnica> direciones;
    private List<DataMontoAnio> montosUT;
    private List<Integer> aniosPlanificacion;

    @PostConstruct
    public void init() {
        direciones = versionDelegate.getUnidadesTecnicasDireccion();
        loadProgramas();
    }

    /**
     * Carga los programas para la planificación seleccionada
     */
    public void loadProgramas() {
        aniosPlanificacion = new LinkedList();
        montosUT = new LinkedList();

        Integer cantAnios = 5;
        Configuracion confVentana = configuracionDelegate.obtenerCnfPorCodigo(ConstantesConfiguracion.CANTIDAD_AMOS_PLANIFICACION);
        if (confVentana != null && !TextUtils.isEmpty(confVentana.getCnfValor())) {
            cantAnios = Integer.valueOf(confVentana.getCnfValor());
        }

        Integer anioDesde = DatesUtils.getYearOfDate(new Date());
        Configuracion confAnio = configuracionDelegate.obtenerCnfPorCodigo(ConstantesConfiguracion.ANIO_TECHO_CATEGORIA_PRESUPUESTAL);
        if (confAnio != null && !TextUtils.isEmpty(confAnio.getCnfValor())) {
            anioDesde = Integer.valueOf(confAnio.getCnfValor());
        }
        Integer anioHasta = anioDesde + cantAnios - 1;

        List<ProgramacionFinancieraAccionCentral> programacionActual = versionDelegate.getProgramacionFinancieraAccionCentral(anioDesde, anioHasta);
        for (ProgramacionFinancieraAccionCentral montoUT : programacionActual) {
            List<ProgramacionFinancieraAccionCentral> l = getMontosUT(montoUT.getUnidadTecnica());
            l.add(montoUT);
        }

        for (int anio = anioDesde; anio <= anioHasta; anio++) {
            aniosPlanificacion.add(anio);
            
            //se añade el techo para la direccion si no existe
            for (UnidadTecnica ut : direciones) {
                agrreglarTechoParaDireccion(ut, anio, anio - anioDesde);
            }
        }

    }

    /**
     * Agrega o cambia de lugar un techo de una UT
     * @param direccion
     * @param anio
     * @param pos 
     */
    private void agrreglarTechoParaDireccion(UnidadTecnica direccion, int anio, int pos) {
        int iter = 0;
        boolean encontro = false;
        List<ProgramacionFinancieraAccionCentral> montos = getMontosUT(direccion);

        while (iter < montos.size()) {
            if (montos.get(iter).getAnio().equals(anio)) {
                encontro = true;
                break;
            }
            iter++;
        }
        //si lo encontro en otra posicion lo cambia de lugar
        if (encontro && iter != pos) {
            ProgramacionFinancieraAccionCentral techo = montos.get(iter);
            montos.remove(iter);
            montos.add(pos, techo);
        }

        if (!encontro) {
            ProgramacionFinancieraAccionCentral techo = new ProgramacionFinancieraAccionCentral();
            techo.setUnidadTecnica(direccion);
            techo.setAnio(anio);
            techo.setMonto(BigDecimal.ZERO);
            montos.add(pos, techo);
        }
    }

    /**
     * Devuleve los montos de una UT
     * @param UT
     * @return 
     */
    public List<ProgramacionFinancieraAccionCentral> getMontosUT(UnidadTecnica UT) {
        for (DataMontoAnio monto : montosUT) {
            if (monto.getUt().equals(UT)) {
                return monto.getMontoAnios();
            }
        }
        DataMontoAnio monto = new DataMontoAnio();
        monto.setMontoAnios(new LinkedList());
        monto.setUt(UT);
        montosUT.add(monto);
        return monto.getMontoAnios();

    }

    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            //se guardan los elementos que tienen anio fiscal, los que no itenen se cargaron solo para mostrar 0
            List<ProgramacionFinancieraAccionCentral> l = new LinkedList();
            for (DataMontoAnio data : montosUT) {
                for (ProgramacionFinancieraAccionCentral iter : (List<ProgramacionFinancieraAccionCentral>)data.getMontoAnios()){
                    if (iter.getMonto()!= null && !iter.getMonto().equals(BigDecimal.ZERO)){
                        l.add(iter);
                    }
                }
            }
            versionDelegate.guardarProgramacionFinancieraAccionCentral(l);
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

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    // </editor-fold>
}
