
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.OneToMany;

/**
 *
 * @author Sofis Solutions
 */
public class SpUsuario implements Serializable {
    
    private String usuario;
    
    private String nombres;
    
    private String email;
    
    @OneToMany
    private List<SpContexto> contexto;
    
    public SpUsuario(){
        
    }
    
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public List<SpContexto> getContexto() {
        return contexto;
    }

    public void setContexto(List<SpContexto> contexto) {
        this.contexto = contexto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
