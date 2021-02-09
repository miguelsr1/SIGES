package sv.gob.mined.siges.persistencia.entidades;

import java.io.Serializable;

/**
 *
 * @author Sofis
 */
public class SgConsultaCalifiacionesAreasBasicas implements Serializable {

    private String tipoComponente;
    private String nombreComponente;
    private Double promedioCalificacion;

    public SgConsultaCalifiacionesAreasBasicas() {
    }

    
    public String getTipoComponente() {
        return tipoComponente;
    }

    public void setTipoComponente(String tipoComponente) {
        this.tipoComponente = tipoComponente;
    }

    public String getNombreComponente() {
        return nombreComponente;
    }

    public void setNombreComponente(String nombreComponente) {
        this.nombreComponente = nombreComponente;
    }

    public Double getPromedioCalificacion() {
        return promedioCalificacion;
    }

    public void setPromedioCalificacion(Double promedioCalificacion) {
        this.promedioCalificacion = promedioCalificacion;
    }

}
