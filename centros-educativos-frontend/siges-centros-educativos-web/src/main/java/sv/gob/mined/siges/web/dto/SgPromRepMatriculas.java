/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.time.LocalDate;
import java.util.List;



/**
 *
 * @author Sofis Solutions
 */
public class SgPromRepMatriculas {
    
    private List<SgMatricula> matriculasPromovidas;
    private List<SgMatricula> matriculasRepetidoras;
    private SgSeccion nuevaSeccion;
    private LocalDate fechaIngreso;

    public List<SgMatricula> getMatriculasPromovidas() {
        return matriculasPromovidas;
    }

    public void setMatriculasPromovidas(List<SgMatricula> matriculasPromovidas) {
        this.matriculasPromovidas = matriculasPromovidas;
    }

    public List<SgMatricula> getMatriculasRepetidoras() {
        return matriculasRepetidoras;
    }

    public void setMatriculasRepetidoras(List<SgMatricula> matriculasRepetidoras) {
        this.matriculasRepetidoras = matriculasRepetidoras;
    }

    

    public SgSeccion getNuevaSeccion() {
        return nuevaSeccion;
    }

    public void setNuevaSeccion(SgSeccion nuevaSeccion) {
        this.nuevaSeccion = nuevaSeccion;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
   
    
    
}
