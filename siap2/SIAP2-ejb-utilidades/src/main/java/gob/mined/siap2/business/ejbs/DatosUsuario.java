/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;

/**
 * Este session bean permite obtener datos del usuario.
 *
 * @author Sofis Solutions
 */
@Stateful(name = "DatosUsuario")
@LocalBean
public class DatosUsuario implements DatosUsuarioRemote {

    @Resource
    private javax.ejb.SessionContext ctx;
    private String codigoUsuario = null;
    private String origen = null;

    public DatosUsuario() {

    }

    /**
     * Obtiene el código del usuario según el principal.
     *
     * @return
     */
    @Override
    public String getCodigoUsuario() {
        codigoUsuario = ctx.getCallerPrincipal().getName();
        return codigoUsuario;
    }

    /**
     * Setea el código de usuario
     *
     * @param codigoUsuario
     */
    @Override
    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    /**
     * Dato utilizado para la auditoría
     *
     * @return
     */
    @Override
    public String getOrigen() {
        return origen;
    }

    /**
     * Dato utilizado para la auditoría
     *
     * @param origen
     */
    @Override
    public void setOrigen(String origen) {

        this.origen = origen;
    }
}
