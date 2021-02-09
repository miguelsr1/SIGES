/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "proveedorVer")
public class ProveedorVer implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    
    private Proveedor objeto;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            
            objeto = (Proveedor) emd.getEntityById(Proveedor.class.getCanonicalName(), Integer.parseInt(id));

        } 
    }

    /**
     * Dirige el sitio a la página de consulta de proveedores
     * @return 
     */
    public String cerrar() {
        return "consultaProveedores.xhtml?faces-redirect=true";
    }

      
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    

    public static Logger getLogger() {
        return logger;
    }

    

  

    public Proveedor getObjeto() {
        return objeto;
    }

    public void setObjeto(Proveedor objeto) {
        this.objeto = objeto;
    }
    
      // </editor-fold>
}
