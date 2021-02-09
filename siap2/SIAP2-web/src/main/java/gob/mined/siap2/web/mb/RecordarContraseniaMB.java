/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.utils.SofisRequestUtils;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Sofis Solutions
 */
@Named(value = "recordarContraseniaMB")
@ViewScoped
public class RecordarContraseniaMB implements Serializable {

    /**
     * Creates a new instance of CargoMB
     */
    public RecordarContraseniaMB() {
    }
    private String usuario = "";
    
    @Inject
    private UsuarioDelegate uDelegate;
    @Inject
    private TextMB textMB;

    public String guardar() {
        SsUsuario ssUsuario = uDelegate.obtenerSsUsuarioPorCodigo(usuario);
        if (ssUsuario == null){
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(textMB.obtenerTexto("labels.NoExisteUsuario")));
        }else{
            String rootUrl = SofisRequestUtils.getBaseUrl(((HttpServletRequest )FacesContext.getCurrentInstance().getExternalContext().getRequest()));
              uDelegate.recordarContrasenia(usuario, rootUrl);
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(textMB.obtenerTexto("labels.SeEnviaraLinkAlUsuario"))); 
        }
        return null;
    }
    
       
    /**
     * Retorna al menú anterior
     * 
     * @return 
     */
    public String cancelar() {
        return "IR_A_INICIO";
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    


}
