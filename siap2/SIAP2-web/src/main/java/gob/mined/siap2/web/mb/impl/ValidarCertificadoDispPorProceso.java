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
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que se encarga de validar todos los certificados de disponibilidad presupuestaria agrupados por proyecto..
 * 
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "validarCertificadoDispPorProceso")
public class ValidarCertificadoDispPorProceso implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    protected DualListModel<LineaEstrategica> lineasEstrategicas;

    @Inject
    private PermisosMB permisosMB;
    @Inject
    private JSFUtils jSFUtils;

    @Inject
    private InsumoDelegate insumoDelegate;

    @Inject
    private EntityManagementDelegate emd;

    @Inject
    private ReporteDelegate reporteDelegate;

    private List<POMontoFuenteInsumo> montosFuenteInsumos;
    private UnidadTecnica ut;
    private ProcesoAdquisicion proceso;
    
    private List<POMontoFuenteInsumo> montosSeleccionadosParaAprobar;

    @PostConstruct
    public void init() {
//        String strIdProcesoAdq = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idProcesoAdq");
//        String strIdUT = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUnidadTecnica");
//
//        Integer idProcesoAdq = Integer.valueOf(strIdProcesoAdq);
//        Integer idUT = Integer.valueOf(strIdUT);
//
//        ut = (UnidadTecnica) emd.getEntityById(UnidadTecnica.class.getName(), idUT);
//        proceso = (ProcesoAdquisicion) emd.getEntityById(ProcesoAdquisicion.class.getName(), idProcesoAdq);
//
//        montosFuenteInsumos = insumoDelegate.getMontofuentesProceso(idUT, idProcesoAdq, true);

    }

    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
//        try {
//            Set<POInsumos> insumosUsados = new HashSet();
//            for(POMontoFuenteInsumo data : montosSeleccionadosParaAprobar){
//                data.setCertificadoDisponibilidadPresupuestariaAprobada(true);
//                insumosUsados.add(data.getInsumo());
//            }
//            insumoDelegate.validarMontoCertificado(new LinkedList(insumosUsados));
//            return "consultaCertificadoDisponibilidadPresupuestaria.xhtml?faces-redirect=true";
//        } catch (GeneralException ex) {
//            logger.log(Level.SEVERE, null, ex);
//            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
//            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
//            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
//        }
        return null;
    }

    /**
     * Este método retorna la página anterior.
     * @return 
     */
    public String cerrar() {
        return "consultaCertificadoDisponibilidadPresupuestaria.xhtml?faces-redirect=true";
    }
    
    /**
//     * Genera el certificado de disponibilidad del proceso con los montos
//     * seleccionados;
//     */
//    public void generarCertificadoDisponibilidadProceso() {
//        try {            
//            byte[] bytespdf = reporteDelegate.generarCertificadoDisponibilidadProceso(montosFuenteInsumos);
//            ArchivoUtils.downloadPdfFromBytes(bytespdf, "CertificadoDeDisponibilidadPresupuestaria.pdf");
//        } catch (GeneralException ex) {
//            logger.log(Level.SEVERE, null, ex);
//            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
//            RequestContext.getCurrentInstance().scrollTo("formCreatePlantilla");
//        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
//            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
//            RequestContext.getCurrentInstance().scrollTo("formCreatePlantilla");
//        }
//    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public PermisosMB getPermisosMB() {
        return permisosMB;
    }

    public void setPermisosMB(PermisosMB permisosMB) {
        this.permisosMB = permisosMB;
    }

    public InsumoDelegate getInsumoDelegate() {
        return insumoDelegate;
    }

    public void setInsumoDelegate(InsumoDelegate insumoDelegate) {
        this.insumoDelegate = insumoDelegate;
    }

    public ReporteDelegate getReporteDelegate() {
        return reporteDelegate;
    }

    public void setReporteDelegate(ReporteDelegate reporteDelegate) {
        this.reporteDelegate = reporteDelegate;
    }

    public List<POMontoFuenteInsumo> getMontosFuenteInsumos() {
        return montosFuenteInsumos;
    }

    public void setMontosFuenteInsumos(List<POMontoFuenteInsumo> montosFuenteInsumos) {
        this.montosFuenteInsumos = montosFuenteInsumos;
    }

    public UnidadTecnica getUt() {
        return ut;
    }

    public void setUt(UnidadTecnica ut) {
        this.ut = ut;
    }

    public ProcesoAdquisicion getProceso() {
        return proceso;
    }

    public void setProceso(ProcesoAdquisicion proceso) {
        this.proceso = proceso;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public DualListModel<LineaEstrategica> getLineasEstrategicas() {
        return lineasEstrategicas;
    }

    public void setLineasEstrategicas(DualListModel<LineaEstrategica> lineasEstrategicas) {
        this.lineasEstrategicas = lineasEstrategicas;
    }
    
    public List<POMontoFuenteInsumo> getMontosSeleccionadosParaAprobar() {
        return montosSeleccionadosParaAprobar;
    }

    public void setMontosSeleccionadosParaAprobar(List<POMontoFuenteInsumo> montosSeleccionadosParaAprobar) {
        this.montosSeleccionadosParaAprobar = montosSeleccionadosParaAprobar;
    }

    // </editor-fold>
}
