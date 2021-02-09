/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralPODelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "descertificarMontoFuenteInsumo")
public class DescertificarMontoFuenteInsumo implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    GeneralPODelegate generalPODelegate;
    @Inject
    ReporteDelegate reporteDelegate;

    private POMontoFuenteInsumo objeto;
    private POInsumos poInsumo;

    private String programa;
    private String subprograma;
    private String proyecto;
    private String accionCentral;
    private String asignacionNoProgramable;
    private String procesoDeCompra;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            
            objeto = (POMontoFuenteInsumo) generalPODelegate.loadPOMontoFuenteInsumo(Integer.valueOf(id));
            poInsumo = objeto.getInsumo();

            if (poInsumo.getPoa().getTipo() == TipoPOA.POA_PROYECTO) {
                POAProyecto poaP = (POAProyecto) poInsumo.getPoa();
                proyecto = poaP.getProyecto().getNombre();
                if (poaP.getProyecto().getProgramaPresupuestario() != null) {
                    subprograma = (poaP.getProyecto().getProgramaPresupuestario().getNombre());
                    programa = (poaP.getProyecto().getProgramaPresupuestario().getProgramaPresupuestario().getNombre());
                }
            }

            if (poInsumo.getPoa().getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                POAConActividades poActividades = (POAConActividades) poInsumo.getPoa();
                if (poActividades.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ACCION_CENTRAL) {
                    AccionCentral accionCentra = (AccionCentral) poActividades.getConMontoPorAnio();
                    accionCentral = (accionCentra.getNombre());
                }
                if (poActividades.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE) {
                    AsignacionNoProgramable asigNP = (AsignacionNoProgramable) poActividades.getConMontoPorAnio();
                    asignacionNoProgramable = (asigNP.getNombre());
                }
            }
            
            if (poInsumo.getProcesoInsumo() != null){
                procesoDeCompra = poInsumo.getProcesoInsumo().getProcesoAdquisicion().getNombre();
            }
            
        }
    }

    /**
     * Aprueba la descertificación 
     * @return 
     */
    public String aprobarDescertificacion() {
        try {
            objeto = generalPODelegate.aprobarDescertificacionMontoFuenteInsumo(objeto.getId());
            return "consultaNotificaciones.xhtml?faces-redirect=true";
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
     * Redirige el sitio a la consulta de notificaciones
     * @return 
     */
    public String cerrar() {
        return "consultaNotificaciones.xhtml?faces-redirect=true";
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public POInsumos getPoInsumo() {
        return poInsumo;
    }

    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public GeneralPODelegate getGeneralPODelegate() {
        return generalPODelegate;
    }

    public void setGeneralPODelegate(GeneralPODelegate generalPODelegate) {
        this.generalPODelegate = generalPODelegate;
    }

    public String getProcesoDeCompra() {
        return procesoDeCompra;
    }

    public void setProcesoDeCompra(String procesoDeCompra) {
        this.procesoDeCompra = procesoDeCompra;
    }

    public ReporteDelegate getReporteDelegate() {
        return reporteDelegate;
    }

    public void setReporteDelegate(ReporteDelegate reporteDelegate) {
        this.reporteDelegate = reporteDelegate;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getSubprograma() {
        return subprograma;
    }

    public void setSubprograma(String subprograma) {
        this.subprograma = subprograma;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getAccionCentral() {
        return accionCentral;
    }

    public void setAccionCentral(String accionCentral) {
        this.accionCentral = accionCentral;
    }

    public String getAsignacionNoProgramable() {
        return asignacionNoProgramable;
    }

    public void setAsignacionNoProgramable(String asignacionNoProgramable) {
        this.asignacionNoProgramable = asignacionNoProgramable;
    }
    
    public void setPoInsumo(POInsumos objeto) {
        this.poInsumo = objeto;
    }
    
    public POMontoFuenteInsumo getObjeto() {
        return objeto;
    }

    public void setObjeto(POMontoFuenteInsumo objeto) {
        this.objeto = objeto;
    }

    // </editor-fold>
}
