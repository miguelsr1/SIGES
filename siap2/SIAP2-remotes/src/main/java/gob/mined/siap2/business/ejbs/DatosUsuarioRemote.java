/*
 * 
 * 
 */
package gob.mined.siap2.business.ejbs;

import javax.ejb.Remote;

/**
 *
 * @author Sofis Solutions
 */
@Remote
public interface DatosUsuarioRemote {

     public String getCodigoUsuario() ;

    public void setCodigoUsuario(String codigoUsuario);

    public String getOrigen();

    public void setOrigen(String origen);
    
}
