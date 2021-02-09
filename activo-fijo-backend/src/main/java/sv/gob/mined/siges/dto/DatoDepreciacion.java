/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacion;

/**
 *
 * @author Sofis Solutions
 */
public class DatoDepreciacion implements Serializable {
    private Integer anio;
    private Integer mes;
    SgAfDepreciacion depreciacion;

    public DatoDepreciacion() {
    }

    public DatoDepreciacion(Integer anio, Integer mes, SgAfDepreciacion depreciacion) {
        this.anio = anio;
        this.mes = mes;
        this.depreciacion = depreciacion;
    }
    
    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public SgAfDepreciacion getDepreciacion() {
        return depreciacion;
    }

    public void setDepreciacion(SgAfDepreciacion depreciacion) {
        this.depreciacion = depreciacion;
    } 
}
