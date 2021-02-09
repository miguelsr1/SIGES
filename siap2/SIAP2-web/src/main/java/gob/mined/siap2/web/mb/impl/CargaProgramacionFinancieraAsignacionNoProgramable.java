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
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.ProgramacionFinancieraAsignacionNoProgramable;
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
 * Esta clase corresponde al backing bean de la carga de programación financiera
 * de una asignación no programable.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "cargaProgFinAsignacionNP")
public class CargaProgramacionFinancieraAsignacionNoProgramable implements Serializable {

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

    private List<DataMontoAnio> montosAsig;
    private List<Integer> aniosPlanificacion;

    @PostConstruct
    public void init() {

        loadProgramacion();
    }

    /**
     * Este método realiza la carga de una programación financiera.
     */
    public void loadProgramacion() {
        aniosPlanificacion = new LinkedList();
        montosAsig = new LinkedList();

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

        List<ProgramacionFinancieraAsignacionNoProgramable> programacionActual = versionDelegate.getProgramacionFinancieraAsigNP(anioDesde, anioHasta);
        for (ProgramacionFinancieraAsignacionNoProgramable montoUT : programacionActual) {
            List<ProgramacionFinancieraAsignacionNoProgramable> l = getMontosAsig(montoUT.getAsignacionNoProgramable());
            l.add(montoUT);
        }

        List<AsignacionNoProgramable> asignaciones = emd.getEntities(AsignacionNoProgramable.class.getName(), "nombre");
        for (int anio = anioDesde; anio <= anioHasta; anio++) {
            aniosPlanificacion.add(anio);
            //se añade el techo para la direccion si no existe
            for (AsignacionNoProgramable a : asignaciones) {
                agrreglarTechoParaDireccion(a, anio, anio - anioDesde);
            }
        }

    }

    /**
     * Este método realiza el cálculo por dirección (ut).
     *
     * @param asig
     * @param anio
     * @param pos
     */
    private void agrreglarTechoParaDireccion(AsignacionNoProgramable asig, int anio, int pos) {
        int iter = 0;
        boolean encontro = false;
        List<ProgramacionFinancieraAsignacionNoProgramable> montos = getMontosAsig(asig);

        while (iter < montos.size()) {
            if (montos.get(iter).getAnio().equals(anio)) {
                encontro = true;
                break;
            }
            iter++;
        }
        //si lo encontro en otra posicion lo cambia de lugar
        if (encontro && iter != pos) {
            ProgramacionFinancieraAsignacionNoProgramable techo = montos.get(iter);
            montos.remove(iter);
            montos.add(pos, techo);
        }

        if (!encontro) {
            ProgramacionFinancieraAsignacionNoProgramable techo = new ProgramacionFinancieraAsignacionNoProgramable();
            techo.setAsignacionNoProgramable(asig);
            techo.setAnio(anio);
            techo.setMonto(BigDecimal.ZERO);
            montos.add(pos, techo);
        }
    }

    /**
     * Este método obtienen los montos de las asignaciones no programables.
     *
     * @param a
     * @return
     */
    public List<ProgramacionFinancieraAsignacionNoProgramable> getMontosAsig(AsignacionNoProgramable a) {
        for (DataMontoAnio monto : montosAsig) {
            if (monto.getAsignacionNoProgramable().equals(a)) {
                return monto.getMontoAnios();
            }
        }
        DataMontoAnio monto = new DataMontoAnio();
        monto.setMontoAnios(new LinkedList());
        monto.setAsignacionNoProgramable(a);
        montosAsig.add(monto);
        return monto.getMontoAnios();

    }

    /**
     * Este método guarda una programación financiera de una asignación no programable.
     * @return 
     */
    public String guardar() {
        try {
            List<ProgramacionFinancieraAsignacionNoProgramable> l = new LinkedList();
            for (DataMontoAnio data : montosAsig) {
                for (ProgramacionFinancieraAsignacionNoProgramable iter : (List<ProgramacionFinancieraAsignacionNoProgramable>) data.getMontoAnios()) {

                    if (iter.getMonto() == null) {
                        iter.setMonto(BigDecimal.ZERO);
                    }
                    l.add(iter);
                }
            }
            versionDelegate.guardarProgramacionFinancieraAsigNP(l);

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

    public List<DataMontoAnio> getMontosAsig() {
        return montosAsig;
    }

    public void setMontosAsig(List<DataMontoAnio> montosAsig) {
        this.montosAsig = montosAsig;
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
