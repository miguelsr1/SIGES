/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.entities.data.impl;

import java.io.Serializable;

/**
 *
 * @author bruno
 */
public class TotalesMatriculas implements Serializable{
    private Integer matriculasFormuladas;
    private Integer matriculasAprobadas;

    public Integer getMatriculasFormuladas() {
        return matriculasFormuladas;
    }

    public void setMatriculasFormuladas(Integer matriculasFormuladas) {
        this.matriculasFormuladas = matriculasFormuladas;
    }

    public Integer getMatriculasAprobadas() {
        return matriculasAprobadas;
    }

    public void setMatriculasAprobadas(Integer matriculasAprobadas) {
        this.matriculasAprobadas = matriculasAprobadas;
    }
    
    
}
