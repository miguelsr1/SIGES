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
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.CertificadoPresupuestarioDelegate;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
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
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "enviarCertificadoDispPorProceso")
public class EnviarCertificadoDispPorProceso implements Serializable {

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

    @Inject
    private UsuarioInfo userInfo;
    
    @Inject
    private CertificadoPresupuestarioDelegate certificadoPresupDelegate;

    private List<POMontoFuenteInsumo> montosFuenteInsumos;
    private UnidadTecnica ut;
    private ProcesoAdquisicion proceso;

    private List<POMontoFuenteInsumo> montosSeleccionadosParaReporte;
    
    

    @PostConstruct
    public void init() {        
        String strIdProcesoAdq = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idProcesoAdq");
        String strIdUT = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUT");

        Integer idProcesoAdq = Integer.valueOf(strIdProcesoAdq);
        Integer idUT = Integer.valueOf(strIdUT);

        ut = (UnidadTecnica) emd.getEntityById(UnidadTecnica.class.getName(), idUT);
        proceso = (ProcesoAdquisicion) emd.getEntityById(ProcesoAdquisicion.class.getName(), idProcesoAdq);

        if (permisosMB.tieneOperacionEnUT(idUT, ConstantesEstandares.Operaciones.ENVIAR_CERTIFICADO_PRES_POR_PROCESO) ){
            montosFuenteInsumos = insumoDelegate.getMontofuentesProceso(idUT, idProcesoAdq);
        }
        
        montosSeleccionadosParaReporte = new LinkedList<>();
    }

    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            if (montosSeleccionadosParaReporte.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PARA_ENVIAR_A_VALIDAR_AL_MENOS_DEBE_SELECCIONAR_UN_MONTO);
                throw b;
            }
            certificadoPresupDelegate.enviarAvalidarFuentes(ut.getId(), montosSeleccionadosParaReporte);
            return "consultaCertificadoDisponibilidadPresupuestaria.xhtml?faces-redirect=true";
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
     * Genera el certificado de disponibilidad del proceso con los montos
     * seleccionados;
     */
    public void generarCertificadoDisponibilidadProceso() {
        try {
            if (montosSeleccionadosParaReporte.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PARA_ENVIAR_A_VALIDAR_AL_MENOS_DEBE_SELECCIONAR_UN_MONTO);
                throw b;
            }
            
////            byte[] bytespdf = reporteDelegate.generarCertificadoDisponibilidadProceso(montosSeleccionadosParaReporte);
//            ArchivoUtils.downloadPdfFromBytes(bytespdf, "CertificadoDeDisponibilidadPresupuestaria.pdf");
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

    
    

    /**
     * Copia el valor del monto estimado al monto a certificar
     * 
     * @param monto 
     */
    public void copiarMonto(POMontoFuenteInsumo monto){
        monto.setCertificado(monto.getMonto());
    }
    
    
    
    
    /**
     * Dirige el sitio a la consulta de certificados de disponibilidad presupuestaria
     * @return 
     */
    public String cerrar() {
        return "consultaCertificadoDisponibilidadPresupuestaria.xhtml?faces-redirect=true";
    }
    
    /**
     * Verifica si un monto fuente está asociado a un certificado de disponibilidad presupuestaria con estado vacío o rechazado
     * @param montoFienteId
     * @return 
     */
    public Boolean estaMontoFuenteDisponibleParaEnviar(Integer montoFienteId){
        return certificadoPresupDelegate.estaMontoFuenteDisponibleParaEnviar(montoFienteId);
    }

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

    public List<POMontoFuenteInsumo> getMontosSeleccionadosParaReporte() {
        return montosSeleccionadosParaReporte;
    }

    public void setMontosSeleccionadosParaReporte(List<POMontoFuenteInsumo> montosSeleccionadosParaReporte) {
        this.montosSeleccionadosParaReporte = montosSeleccionadosParaReporte;
    }

    // </editor-fold>
}
