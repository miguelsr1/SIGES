/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;

public class FiltroMatriculasEnSeccionSAT implements Serializable {

    private String codigoCentro;
    private LocalDate fecha;
    private Boolean soloRepetidores;
    private Boolean soloSobreedad;

    public FiltroMatriculasEnSeccionSAT() {
    }

    public FiltroMatriculasEnSeccionSAT(String codigoCentro, LocalDate fecha) {
        this.codigoCentro = codigoCentro;
        this.fecha = fecha;
    }
      

    public String getCodigoCentro() {
        return codigoCentro;
    }

    public void setCodigoCentro(String codigoCentro) {
        this.codigoCentro = codigoCentro;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getSoloRepetidores() {
        return soloRepetidores;
    }

    public void setSoloRepetidores(Boolean soloRepetidores) {
        this.soloRepetidores = soloRepetidores;
    }

    public Boolean getSoloSobreedad() {
        return soloSobreedad;
    }

    public void setSoloSobreedad(Boolean soloSobreedad) {
        this.soloSobreedad = soloSobreedad;
    }
    
    

}
