/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;

/**
 *
 * @author usuario
 */

public class SgCalificacionCompPeriodo implements Serializable {     
    
    private static final long serialVersionUID = 1L;
    
    private Long componentePlanEstudioPk;
    
    private Long rangoFechaPk;
    
    private EnumTiposPeriodosCalificaciones tipoPeriodoCalificacion;
    
    private EnumCalendarioEscolar tipoCalendarioEscolar;
    
    private Integer rphNumeroExtraordinario;
    

    public SgCalificacionCompPeriodo() {
    }

    public Long getComponentePlanEstudioPk() {
        return componentePlanEstudioPk;
    }

    public void setComponentePlanEstudioPk(Long componentePlanEstudioPk) {
        this.componentePlanEstudioPk = componentePlanEstudioPk;
    }

    public Long getRangoFechaPk() {
        return rangoFechaPk;
    }

    public void setRangoFechaPk(Long rangoFechaPk) {
        this.rangoFechaPk = rangoFechaPk;
    }

    public EnumTiposPeriodosCalificaciones getTipoPeriodoCalificacion() {
        return tipoPeriodoCalificacion;
    }

    public void setTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones tipoPeriodoCalificacion) {
        this.tipoPeriodoCalificacion = tipoPeriodoCalificacion;
    }

    public EnumCalendarioEscolar getTipoCalendarioEscolar() {
        return tipoCalendarioEscolar;
    }

    public void setTipoCalendarioEscolar(EnumCalendarioEscolar tipoCalendarioEscolar) {
        this.tipoCalendarioEscolar = tipoCalendarioEscolar;
    }

    public Integer getRphNumeroExtraordinario() {
        return rphNumeroExtraordinario;
    }

    public void setRphNumeroExtraordinario(Integer rphNumeroExtraordinario) {
        this.rphNumeroExtraordinario = rphNumeroExtraordinario;
    }

 

  
   
}
