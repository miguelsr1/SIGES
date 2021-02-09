/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.business.ejbs.DatosUsuario;
import java.io.Serializable;
import java.security.Principal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sofis Solutions
 */
@Named(value = "inicioMB")
@SessionScoped
public class InicioMB implements Serializable {

    @Inject
    private DatosUsuario datUsuario;

    /**
     * Creates a new instance of CargoMB
     */
    public InicioMB() {
    }
    private String nombreUsuario = "";

    public String datosPrincipal() {
        Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        if (principal != null) {
            datUsuario.setCodigoUsuario(principal.getName());

            datUsuario.setOrigen("DNI_WEB");
            nombreUsuario = datUsuario.getCodigoUsuario();

            ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("datUsuario", datUsuario);

        } else {
            return "IR_A_INICIO";
        }
        return null;
    }

    public String getNombreUsuario() {
        datosPrincipal();
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String cancelarAcciones() {
        return "IR_A_INICIO";
    }

    public String logout() {

        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        return "IR_A_INICIO";
    }

    public String redirigirInicio(String codigo) {
        return codigo;
    }

}
