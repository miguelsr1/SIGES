/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.exceptions.GeneralException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que consolida un POG de proyecto.
 * 
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "consolidarPOGProyecto")
public class ConsolidarPOGProyecto extends POGProyectoCE implements Serializable {

    /**
     * Retorna la lineas de pog con que se trabajan en la pagina.
     * 
     * @return 
     */
    @Override
    public List<POLinea> getLineas() {
        return objeto.getPog().getLineas();
    }

    
    /**
     * Consolida el POG en edición
     */
    public void consolidarPOG() {
        try {
            proyectoDelegate.consolidarPOG(objeto.getId());
            initProyecto();
            RequestContext.getCurrentInstance().execute("$('#cerrarPOGModal').modal('hide');");

            String texto = textMB.obtenerTexto("labels.POGCerradoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");

        }catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        }catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

}
