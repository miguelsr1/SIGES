
package sv.gob.mined.siges.dto;

import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class SpContexto implements Serializable {
    
    private String rol;
    
    private String ambito;
    
    private Long contexto;
    
    public SpContexto(){
        
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public Long getContexto() {
        return contexto;
    }

    public void setContexto(Long contexto) {
        this.contexto = contexto;
    }

 
    
    
}
