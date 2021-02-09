/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */

package sv.gob.mined.siges.filtros;
import java.io.Serializable;
import java.time.LocalDate;

public class FiltroMatriculasEnSeccion implements Serializable {
    
    private Long implementadoraPk;
    private String implementadoraCodigo;
    private LocalDate matriculasFecha;
            

    public FiltroMatriculasEnSeccion() {
    }

    public Long getImplementadoraPk() {
        return implementadoraPk;
    }

    public void setImplementadoraPk(Long implementadoraPk) {
        this.implementadoraPk = implementadoraPk;
    }

    public String getImplementadoraCodigo() {
        return implementadoraCodigo;
    }

    public void setImplementadoraCodigo(String implementadoraCodigo) {
        this.implementadoraCodigo = implementadoraCodigo;
    }

    public LocalDate getMatriculasFecha() {
        return matriculasFecha;
    }

    public void setMatriculasFecha(LocalDate matriculasFecha) {
        this.matriculasFecha = matriculasFecha;
    }

    

    

  
   
}

