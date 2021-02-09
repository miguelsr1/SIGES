/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import javax.persistence.ManyToOne;

/**
 *
 * @author usuario
 */
public class SpRolesUsuario implements Serializable {
        
    private String token;
    
    @ManyToOne
    private SpUsuario usuario;
    
    public SpRolesUsuario(){
        
    }
    

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SpUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(SpUsuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "rolesUsuario{" + "token=" + token + ", usuario=" + usuario + '}';
    }
    
    
}
