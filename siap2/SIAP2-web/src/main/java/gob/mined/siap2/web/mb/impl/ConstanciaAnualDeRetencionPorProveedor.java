/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ImpuestoDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UtilsMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "constanciaAnualDeRetencionPorProveedor")
public class ConstanciaAnualDeRetencionPorProveedor implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    ReporteDelegate reporteDelegate;
    @Inject
    ImpuestoDelegate impuestoDelegate;
    @Inject
    TextMB textMB;
    @Inject
    UtilsMB utilsMB;

    private boolean update = false;
    private Proveedor objeto;
    private Integer anio;
    private Integer idUsuarioFirmante;

    private LazyDataModel lazyModel;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = (Proveedor) emd.getEntityById(Proveedor.class.getName(), Integer.valueOf(id));
        }
    }

   /**
    * Genera y descarga el reporte de constancia de retención de impuestos para determinado año
    */
    public void generarConstanciaRetencionDeImpuestos() {
        try {
            byte[] reporte = reporteDelegate.generarConstanciaRetencionDeImpuestos(anio, objeto.getId(), idUsuarioFirmante);
            ArchivoUtils.downloadPdfFromBytes(reporte, "ConstanciaRetencionDeImpuestos_"+anio+".pdf");

            String texto = textMB.obtenerTexto("labels.ReporteGeneradoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Redirige el sitio a la página de emisión de comprobante de retención de impuesto
     * @return 
     */
    public String cerrar() {
        return "emisionComprobanteRetencionImpuesto.xhtml?faces-redirect=true";
    }

    /**
     * Devuelve todos los usuarios que poseen en su rol la operación que les permite firmar la constancia anual de retención
     * @return 
     */
    public Map<String, String> getUsuariosFirmantes(){
        return utilsMB.getUsuariosConOperacion(ConstantesEstandares.Operaciones.FIRMAR_COSNTANCIA_ANUAL_DE_RETENCION_POR_PROVEEDOR);
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

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public Integer getIdUsuarioFirmante() {
        return idUsuarioFirmante;
    }

    public void setIdUsuarioFirmante(Integer idUsuarioFirmante) {
        this.idUsuarioFirmante = idUsuarioFirmante;
    }


    

    public Proveedor getObjeto() {
        return objeto;
    }

    public void setObjeto(Proveedor objeto) {
        this.objeto = objeto;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }


    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
