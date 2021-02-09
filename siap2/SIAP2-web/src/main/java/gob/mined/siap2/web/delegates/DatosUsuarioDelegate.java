/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates;

import gob.mined.siap2.business.ejbs.DatosUsuarioRemote;
import gob.mined.siap2.web.utils.EJBUtils;
import java.io.Serializable;
import javax.inject.Named;

/**
 * Esta clase permite obtener datos de usuarios desde la capa lógica.
 * @author Sofis Solutions
 */
@Named
public class DatosUsuarioDelegate implements Serializable {

    private final DatosUsuarioRemote dur;

    /**
     * Constructor por defecto. OBtiene el EJB.
     */
    public DatosUsuarioDelegate() {
        dur = EJBUtils.getDatosUsuario();
    }

    public String getCodigoUsuario() {
        return dur.getCodigoUsuario();
    }

    public void setCodigoUsuario(String codigoUsuario) {
        dur.setCodigoUsuario(codigoUsuario);
    }

    public String getOrigen() {
        return dur.getOrigen();
    }

    public void setOrigen(String origen) {
        dur.setOrigen(origen);
    }
}
