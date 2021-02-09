/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;

/**
 *
 * @author usuario
 */

public class SpMensajeRespuesta implements Serializable {
        
        private String estado;
        
        private String mensajeError;
        
        public SpMensajeRespuesta(){
            
        }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    
    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    @Override
    public String toString() {
        return "mensajeRespuesta{" + "estado=" + estado + ", mensajeError=" + mensajeError + '}';
    }
        
        
}
