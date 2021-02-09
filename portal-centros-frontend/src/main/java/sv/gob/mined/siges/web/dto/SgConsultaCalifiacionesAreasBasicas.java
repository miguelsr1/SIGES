package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.TipoComponentePlanEstudio;

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

    public String getEnumText() {
        TipoComponentePlanEstudio tipo = TipoComponentePlanEstudio.valueOf(this.tipoComponente);
        switch (tipo) {
            case ASI:
            case AESS:
            case ATE:
            case IND:
            case MOD:
            case PRU:
                return tipo.getText();
            default:
                throw new AssertionError(tipo.name());
        }
    }

}
