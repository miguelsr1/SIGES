/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */

package sv.gob.mined.siges.filtros;
import java.io.Serializable;

public class FiltroMatriculasEnSede implements Serializable {
    
    private Long sedPk;
    private Integer anio;
            

    public FiltroMatriculasEnSede() {
    }

    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    

  
   
}

