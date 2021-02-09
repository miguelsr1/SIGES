/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.ejbs.impl.POAProyectoBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroPOA;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
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
@Named(value = "generacionReportePEP")
public class generacionReportePEP implements Serializable {

    @Inject
    EntityManagementDelegate emd;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    ReporteDelegate reporteDelegate;

    @Inject
    private POAProyectoBean poaProyectoBean;
    
    
    private Proyecto proyectoSelected;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private SofisComboG<AnioFiscal> comboAnioFiscal;
    private Integer proyectoId;
    @PostConstruct
    public void init() {
        initAnioFisscales();
    }

    /**
     * Carga un combo con años fiscales
     */
    public void initAnioFisscales() {
        List<AnioFiscal> l = emd.getEntities(AnioFiscal.class.getName(), "anio");
        int anio = Calendar.getInstance().get(Calendar.YEAR);
        AnioFiscal selecionado = null;
        Iterator<AnioFiscal> iter = l.iterator();
        while (iter.hasNext() && selecionado == null) {
            AnioFiscal temp = iter.next();
            if (temp.getAnio().intValue() == anio) {
                selecionado = temp;
            }
        }
        comboAnioFiscal = new SofisComboG<>(l, "anio");
        comboAnioFiscal.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        comboAnioFiscal.setSelectedT(selecionado);
    }

    /**
     * Genera y descarga un reporte PEP
     *
     * @param borrador
     */
    public void generarReportePEP(boolean borrador) {
        try {
            proyectoId = null;
            if (comboAnioFiscal.getSelectedT() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_ANIO_FISCAL);
                throw b;
            }
            if(proyectoSelected != null) {
                proyectoId = proyectoSelected.getId();
            }
            byte[] bytes = null;
            if(proyectoId != null) {
                bytes = reporteDelegate.generarReportePEPGlobal(comboAnioFiscal.getSelectedT().getId(), proyectoId);
            } else {
                bytes = reporteDelegate.generarReportePEPGlobal(comboAnioFiscal.getSelectedT().getId());
            }
            
            ArchivoUtils.downloadPdfFromBytes(bytes, "reportePEP-" + comboAnioFiscal.getSelectedT().getAnio() + ".pdf");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    public List<Proyecto> completeProyecto(String query){
        try {
            FiltroPOA filtro = new FiltroPOA();
            filtro.setNombre(query);
            filtro.setMaxResults(11L);
            filtro.setIncluirCampos(new String[]{"nombre","version"});
            return poaProyectoBean.obtenerPOAProyectoPorFiltro(filtro);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    //geters
    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public SofisComboG<AnioFiscal> getComboAnioFiscal() {
        return comboAnioFiscal;
    }

    public void setComboAnioFiscal(SofisComboG<AnioFiscal> comboAnioFiscal) {
        this.comboAnioFiscal = comboAnioFiscal;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public static Logger getLogger() {
        return logger;
    }
    public Proyecto getProyectoSelected() {
        return proyectoSelected;
    }

    public void setProyectoSelected(Proyecto proyectoSelected) {
        this.proyectoSelected = proyectoSelected;
    }
    public Integer getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
    }
    // </editor-fold>

   

    
   
}
