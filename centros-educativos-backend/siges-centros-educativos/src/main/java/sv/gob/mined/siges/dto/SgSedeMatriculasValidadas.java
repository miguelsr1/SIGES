package sv.gob.mined.siges.dto;

import java.io.Serializable;

/**
 *
 * @author fabricio
 */
public class SgSedeMatriculasValidadas implements Serializable {
    
    private Long sedePk;
    private Long cantidadMatriculasValidadas;
    private Long cantidadMatriculasNoValidadas;
    
   
    public SgSedeMatriculasValidadas(Long sedePk) {
        this.sedePk = sedePk;
        this.cantidadMatriculasNoValidadas = 0L;
        this.cantidadMatriculasValidadas = 0L;
    }
    
    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Long getCantidadMatriculasValidadas() {
        return cantidadMatriculasValidadas;
    }

    public void setCantidadMatriculasValidadas(Long cantidadMatriculasValidadas) {
        this.cantidadMatriculasValidadas = cantidadMatriculasValidadas;
    }

    public Long getCantidadMatriculasNoValidadas() {
        return cantidadMatriculasNoValidadas;
    }

    public void setCantidadMatriculasNoValidadas(Long cantidadMatriculasNoValidadas) {
        this.cantidadMatriculasNoValidadas = cantidadMatriculasNoValidadas;
    }

    @Override
    public String toString() {
        return "SgSedeMatriculasValidadas{" + "sedePk=" + sedePk + ", cantidadMatriculasValidadas=" + cantidadMatriculasValidadas + ", cantidadMatriculasNoValidadas=" + cantidadMatriculasNoValidadas + '}';
    }
    
    
    
}

