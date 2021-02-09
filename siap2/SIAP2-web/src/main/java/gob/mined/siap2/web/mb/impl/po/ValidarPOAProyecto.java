/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.enums.TipoProyecto;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.TextMB;
import static gob.mined.siap2.web.mb.impl.po.POConActividadesEInsumosAbstract.logger;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que valida un POA de proyecto.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "validarPOAProyecto")
public class ValidarPOAProyecto extends POAProyectoCE implements Serializable {

    @Inject
    PermisosMB permisosMB;
    @Inject
    private TextMB textMB;

    boolean cumplePermisos;
    
    @PostConstruct
    @Override
    public void init() {
        super.init();
        
        cumplePermisos = false;
        if (poa!= null && poa.getTipo() == TipoPOA.POA_PROYECTO){
            POAProyecto poaProyecto = poa;            
            if (poaProyecto.getProyecto().getTipoProyecto() == TipoProyecto.ADMINISTRATIVO){
                cumplePermisos = permisosMB.tieneOperacion(ConstantesEstandares.Operaciones.VALIDAR_POA_PROYECTO_ADMINISTRATIVO);
            }else{
                cumplePermisos =permisosMB.tieneOperacion(ConstantesEstandares.Operaciones.VALIDAR_POA_PROYECTO_INV_NO_INV);
            }
        }
        
        if (!cumplePermisos){
            poa = null;
            String texto = textMB.obtenerTexto("labels.NoTienePermisosParaValidarUnEsteTipoDePOA");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, texto));
        }
        
    }

    /**
     * Este método se utiliza para validar el POA en edición
     * @throws IOException 
     */
    public void generarLineaBase() throws IOException {
        try {
            pOAProyectoDelegate.validarPOA(poa.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("consultaNotificaciones.xhtml");
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
     * Este método se utiliza para rechazar el POA en edición
     * 
     * @throws IOException 
     */
    public void rechazarPOA() throws IOException {
        try {
            pOAProyectoDelegate.rechazarPOA(poa.getId(), motivoRechazo);
            FacesContext.getCurrentInstance().getExternalContext().redirect("consultaNotificaciones.xhtml");
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
   
}
