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
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.LineaEstrategicaDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import java.io.Serializable;
import java.util.Date;
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
@Named(value = "lineaEstrategicaCE")
public class LineaEstrategicaCrearEditar implements Serializable {
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    
    @Inject
    LineaEstrategicaDelegate lineaEstrategicaD;
    
    private boolean update = false;
    private LineaEstrategica objeto;
    
    
    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = (LineaEstrategica) emd.getEntityById(LineaEstrategica.class.getName(), Integer.valueOf(id));
        } else {
           objeto  = new LineaEstrategica();
           objeto.setFechaCreacion(new Date());
           objeto.setEstado(EstadoComun.VIGENTE);
        }
    }
    
     
    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            lineaEstrategicaD.crearEditarLineaEstrategica(objeto);
            return "consultaLineaEstrategica.xhtml?faces-redirect=true"; 
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
     * Dirige el sitio a la consulta de líneas estratégicas
     * @return 
     */
    public String cerrar() {
        return "consultaLineaEstrategica.xhtml?faces-redirect=true";  
    }
    
    /**
     * Devuelve la lista de estados comunes (vigente, no vigente)
     * @return 
     */
    public EstadoComun[] getEstadoLineas() {
        return EstadoComun.values();
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

    public LineaEstrategica getObjeto() {
        return objeto;
    }

    public void setObjeto(LineaEstrategica objeto) {
        this.objeto = objeto;
    }
    
    

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

   
    
    
    // </editor-fold>
    
    
    
    
    
    
    
    
    
}
