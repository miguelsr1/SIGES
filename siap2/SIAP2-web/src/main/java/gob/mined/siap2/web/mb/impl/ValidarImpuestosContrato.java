/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ContratoDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralPODelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
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
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * de validar los impuestos de un contrato
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "validarImpuestosContrato")
public class ValidarImpuestosContrato implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    GeneralPODelegate generalPODelegate;
    @Inject
    ReporteDelegate reporteDelegate;
    @Inject
    ContratoDelegate contratoDelegate;
    @Inject
    VersionesDelegate versionDelegate;

    private ContratoOC objeto;

    private String programa;
    private String subprograma;
    private String proyecto;
    private String accionCentral;
    private String asignacionNoProgramable;
    private String procesoDeCompra;

    private DualListModel<Impuesto> impuestos = new DualListModel(new LinkedList(), new LinkedList());

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            objeto = contratoDelegate.cargarContrato(Integer.valueOf(id));

            //se cargan los impuestos
            List<Impuesto> disponibles = versionDelegate.getClasesGeneralCodiguera(Impuesto.class);
            List<Impuesto> enUso = new LinkedList<>();
            if(objeto != null){
                for (Impuesto i : objeto.getImpuestos()) {
                    enUso.add(i);
                    if (disponibles.contains(i)) {
                        disponibles.remove(i);
                    }
                }
            }
            impuestos = new DualListModel(disponibles, enUso);
        }
    }

    /**
     * Este método se encarga de realizar la validación de los impuestos.
     * 
     * @return 
     */
    public String validarImpuestos() {
        try {
            List <Impuesto>  listImp = new LinkedList(impuestos.getTarget());
            
            objeto.setImpuestos(listImp);
            objeto = contratoDelegate.validarImpuestosContrato(objeto);
            
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
     * Este método se utiliza para volver a la pagina anterior
     * 
     * @return 
     */
    public String cerrar() {
        return "consultaNotificaciones.xhtml?faces-redirect=true";
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public ContratoDelegate getContratoDelegate() {
        return contratoDelegate;
    }

    public void setContratoDelegate(ContratoDelegate contratoDelegate) {
        this.contratoDelegate = contratoDelegate;
    }

    public ContratoOC getObjeto() {
        return objeto;
    }

    public void setObjeto(ContratoOC objeto) {
        this.objeto = objeto;
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

    public VersionesDelegate getVersionDelegate() {
        return versionDelegate;
    }

    public void setVersionDelegate(VersionesDelegate versionDelegate) {
        this.versionDelegate = versionDelegate;
    }

    public DualListModel<Impuesto> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(DualListModel<Impuesto> impuestos) {
        this.impuestos = impuestos;
    }

    public void setAsignacionNoProgramable(String asignacionNoProgramable) {
        this.asignacionNoProgramable = asignacionNoProgramable;
    }

    // </editor-fold>
}
