/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.ejbs.impl.ProyectoBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.tipos.FiltroRiesgo;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.impl.PlanificacionEstrategicaDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UtilsMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import gob.mined.siap2.ws.to.RiesgoTO;
import java.io.Serializable;
import java.util.ArrayList;
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
 * @author sofis
 */
@ViewScoped
@Named(value = "matrizRiesgos")
public class MatrizRiesgos implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    UtilsMB utilsMB;
    @Inject
    TextMB textMB;
    @Inject
    ProyectoBean proyBean;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    ReporteDelegate reporteDelegate;

    @Inject
    PlanificacionEstrategicaDelegate planificacion;

    private String planificacionEstrategicaId;
    private List<RiesgoTO> listaRiesgos = new ArrayList();
    private FiltroRiesgo filtro = new FiltroRiesgo();
    private String opcionVacia = "--";

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private SofisComboG<LineaEstrategica> comboLineasEstrategicas = new SofisComboG<>();
    private SofisComboG<ProgramaInstitucional> comboProgramaInstitucional = new SofisComboG<>();
    private SofisComboG<ProgramaPresupuestario> comboProgramaPresupuestario = new SofisComboG<>();
    private SofisComboG<AnioFiscal> comboAnioFiscalConsulta = new SofisComboG<>();

    @PostConstruct
    public void init() {
        opcionVacia = textMB.obtenerTexto("labels.filtroVacio");
        initFilter();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        planificacionEstrategicaId = null;
        filtro = new FiltroRiesgo();
        try {
            comboAnioFiscalConsulta = new SofisComboG<>(new ArrayList(versionDelegate.getAniosFiscalesPlanificacion()), "nombre");
            comboAnioFiscalConsulta.addEmptyItem(opcionVacia);
            comboProgramaInstitucional = new SofisComboG<>(new ArrayList(versionDelegate.getProgramasInstitucionalesVigentes()), "nombre");
            comboProgramaInstitucional.addEmptyItem(opcionVacia);
            comboProgramaPresupuestario = new SofisComboG<>(new ArrayList(versionDelegate.getProgramasPresupuestariosVigentes()), "nombre");
            comboProgramaPresupuestario.addEmptyItem(opcionVacia);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            filtro = new FiltroRiesgo();
            if (comboProgramaInstitucional.getSelectedT() != null) {
                filtro.setProgramaInstitucional(comboProgramaInstitucional.getSelectedT());
            }
            if (comboProgramaPresupuestario.getSelectedT() != null) {
                filtro.setProgramaPresupuestario(comboProgramaPresupuestario.getSelectedT());
            }
            if (comboAnioFiscalConsulta.getSelectedT()!=null){
                filtro.setAnioFiscal(comboAnioFiscalConsulta.getSelectedT());
            }

            listaRiesgos = new ArrayList(proyBean.obtenerRiesgosPorCriteria(filtro));
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        listaRiesgos.clear();
    }
    
    /**
     * Genera y descarga el reporte de matriz de riesgos
     */
    public void generarPDF() {
        try {
            byte[] bytespdf = reporteDelegate.generarReporteMatrizRiesgo(listaRiesgos);
            ArchivoUtils.downloadPdfFromBytes(bytespdf, "MatrizDeRiesgos.pdf");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("formCreatePlantilla");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("formCreatePlantilla");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public SofisComboG<LineaEstrategica> getComboLineasEstrategicas() {
        return comboLineasEstrategicas;
    }

    public void setComboLineasEstrategicas(SofisComboG<LineaEstrategica> comboLineasEstrategicas) {
        this.comboLineasEstrategicas = comboLineasEstrategicas;
    }

    public SofisComboG<ProgramaInstitucional> getComboProgramaInstitucional() {
        return comboProgramaInstitucional;
    }

    public void setComboProgramaInstitucional(SofisComboG<ProgramaInstitucional> comboProgramaInstitucional) {
        this.comboProgramaInstitucional = comboProgramaInstitucional;
    }

    public SofisComboG<ProgramaPresupuestario> getComboProgramaPresupuestario() {
        return comboProgramaPresupuestario;
    }

    public void setComboProgramaPresupuestario(SofisComboG<ProgramaPresupuestario> comboProgramaPresupuestario) {
        this.comboProgramaPresupuestario = comboProgramaPresupuestario;
    }

    public String getPlanificacionEstrategicaId() {
        return planificacionEstrategicaId;
    }

    public void setPlanificacionEstrategicaId(String planificacionEstrategicaId) {
        this.planificacionEstrategicaId = planificacionEstrategicaId;
    }

    public List<RiesgoTO> getListaRiesgos() {
        return listaRiesgos;
    }

    public void setListaRiesgos(List<RiesgoTO> listaRiesgos) {
        this.listaRiesgos = listaRiesgos;
    }

    public FiltroRiesgo getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroRiesgo filtro) {
        this.filtro = filtro;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscalConsulta() {
        return comboAnioFiscalConsulta;
    }

    public void setComboAnioFiscalConsulta(SofisComboG<AnioFiscal> comboAnioFiscalConsulta) {
        this.comboAnioFiscalConsulta = comboAnioFiscalConsulta;
    }

    // </editor-fold>
}
