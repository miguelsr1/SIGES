/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;

/**
 *
 * @author usuario
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dinPk", scope = SgDatoIndicador.class)
public class SgDatoIndicador implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long dinPk;
    
    private SgEstIndicador dinIndicador;
    
    public SgDatoIndicador(){
        
    }

    public Long getDinPk() {
        return dinPk;
    }

    public void setDinPk(Long dinPk) {
        this.dinPk = dinPk;
    }   
    

    public SgEstIndicador getDinIndicador() {
        return dinIndicador;
    }

    public void setDinIndicador(SgEstIndicador dinIndicador) {
        this.dinIndicador = dinIndicador;
    }
    
    
    
}
