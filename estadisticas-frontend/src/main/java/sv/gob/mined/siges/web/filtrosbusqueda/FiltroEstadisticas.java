/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumDesagregacion;

public class FiltroEstadisticas implements Serializable {
        
    private Long indicadorPk;
    private Long nombrePk;
    private Integer anio;
    private EnumDesagregacion desagregacion;
    
    private Long nombrePkComparacion;
    private Integer anioComparacion;
   

    public FiltroEstadisticas() {
    }

    public Long getNombrePk() {
        return nombrePk;
    }

    public void setNombrePk(Long nombrePk) {
        this.nombrePk = nombrePk;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getIndicadorPk() {
        return indicadorPk;
    }

    public void setIndicadorPk(Long indicadorPk) {
        this.indicadorPk = indicadorPk;
    }

    public EnumDesagregacion getDesagregacion() {
        return desagregacion;
    }

    public void setDesagregacion(EnumDesagregacion desagregacion) {
        this.desagregacion = desagregacion;
    }

    public Long getNombrePkComparacion() {
        return nombrePkComparacion;
    }

    public void setNombrePkComparacion(Long nombrePkComparacion) {
        this.nombrePkComparacion = nombrePkComparacion;
    }

    public Integer getAnioComparacion() {
        return anioComparacion;
    }

    public void setAnioComparacion(Integer anioComparacion) {
        this.anioComparacion = anioComparacion;
    }
    
    
    
}
