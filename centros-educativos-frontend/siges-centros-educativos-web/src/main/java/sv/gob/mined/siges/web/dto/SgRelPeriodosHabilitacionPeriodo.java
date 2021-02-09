/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rphPk", scope = SgRelPeriodosHabilitacionPeriodo.class)
public class SgRelPeriodosHabilitacionPeriodo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long rphPk;

    private EnumTiposPeriodosCalificaciones rphTipoPeriodoCalificacion;

    private EnumCalendarioEscolar rphTipoCalendarioEscolar;

    private SgRangoFecha rphRangoFechaFk;

    private SgHabilitacionPeriodoCalificacion rphHabilitacionPeriodoCalificacionFk;
    
    private SgComponentePlanGrado rphComponentePlanGradoFk;

    private Integer rphNumeroExtraordinario;    

    private String rphCalificacionNumerica;
   
    private SgCalificacion rphCalificacionConceptual;
    
    private String rphCalificacionActualValor;

    private String rphInfoProcesamiento;

    private LocalDateTime rphUltModFecha;

    private String rphUltModUsuario;

    private Integer rphVersion;
    
    
    public SgRelPeriodosHabilitacionPeriodo() {
  
    }



    @JsonIgnore
    public String getCalificacionValor() {
        if (rphCalificacionConceptual != null) {
            return rphCalificacionConceptual.getCalValor();
        }
        if (!StringUtils.isBlank(rphCalificacionNumerica)) {
            return this.rphCalificacionNumerica;
        }
        return "";
    }
    
    @JsonIgnore
    public String getNombrePeriodo(){
        if(rphRangoFechaFk!=null){
            return rphRangoFechaFk.getRfeCodigoRango();
        }else{
            if(rphTipoCalendarioEscolar!=null){
                return rphTipoCalendarioEscolar.getText();
            }
        }
        return null;
    }

    public Long getRphPk() {
        return rphPk;
    }

    public void setRphPk(Long rphPk) {
        this.rphPk = rphPk;
    }


    public LocalDateTime getRphUltModFecha() {
        return rphUltModFecha;
    }

    public void setRphUltModFecha(LocalDateTime rphUltModFecha) {
        this.rphUltModFecha = rphUltModFecha;
    }

    public String getRphUltModUsuario() {
        return rphUltModUsuario;
    }

    public void setRphUltModUsuario(String rphUltModUsuario) {
        this.rphUltModUsuario = rphUltModUsuario;
    }

    public Integer getRphVersion() {
        return rphVersion;
    }

    public void setRphVersion(Integer rphVersion) {
        this.rphVersion = rphVersion;
    }

    public EnumTiposPeriodosCalificaciones getRphTipoPeriodoCalificacion() {
        return rphTipoPeriodoCalificacion;
    }

    public void setRphTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones rphTipoPeriodoCalificacion) {
        this.rphTipoPeriodoCalificacion = rphTipoPeriodoCalificacion;
    }

    public EnumCalendarioEscolar getRphTipoCalendarioEscolar() {
        return rphTipoCalendarioEscolar;
    }

    public void setRphTipoCalendarioEscolar(EnumCalendarioEscolar rphTipoCalendarioEscolar) {
        this.rphTipoCalendarioEscolar = rphTipoCalendarioEscolar;
    }

    public SgRangoFecha getRphRangoFechaFk() {
        return rphRangoFechaFk;
    }

    public void setRphRangoFechaFk(SgRangoFecha rphRangoFechaFk) {
        this.rphRangoFechaFk = rphRangoFechaFk;
    }

    public SgHabilitacionPeriodoCalificacion getRphHabilitacionPeriodoCalificacionFk() {
        return rphHabilitacionPeriodoCalificacionFk;
    }

    public void setRphHabilitacionPeriodoCalificacionFk(SgHabilitacionPeriodoCalificacion rphHabilitacionPeriodoCalificacionFk) {
        this.rphHabilitacionPeriodoCalificacionFk = rphHabilitacionPeriodoCalificacionFk;
    }

    public SgComponentePlanGrado getRphComponentePlanGradoFk() {
        return rphComponentePlanGradoFk;
    }

    public void setRphComponentePlanGradoFk(SgComponentePlanGrado rphComponentePlanGradoFk) {
        this.rphComponentePlanGradoFk = rphComponentePlanGradoFk;
    }

    public Integer getRphNumeroExtraordinario() {
        return rphNumeroExtraordinario;
    }

    public void setRphNumeroExtraordinario(Integer rphNumeroExtraordinario) {
        this.rphNumeroExtraordinario = rphNumeroExtraordinario;
    }

    public String getRphCalificacionNumerica() {
        return rphCalificacionNumerica;
    }

    public void setRphCalificacionNumerica(String rphCalificacionNumerica) {
        this.rphCalificacionNumerica = rphCalificacionNumerica;
    }

    public SgCalificacion getRphCalificacionConceptual() {
        return rphCalificacionConceptual;
    }

    public void setRphCalificacionConceptual(SgCalificacion rphCalificacionConceptual) {
        this.rphCalificacionConceptual = rphCalificacionConceptual;
    }

    public String getRphInfoProcesamiento() {
        return rphInfoProcesamiento;
    }

    public void setRphInfoProcesamiento(String rphInfoProcesamiento) {
        this.rphInfoProcesamiento = rphInfoProcesamiento;
    }

    public String getRphCalificacionActualValor() {
        return rphCalificacionActualValor;
    }

    public void setRphCalificacionActualValor(String rphCalificacionActualValor) {
        this.rphCalificacionActualValor = rphCalificacionActualValor;
    }

    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.rphTipoPeriodoCalificacion);
        hash = 37 * hash + Objects.hashCode(this.rphTipoCalendarioEscolar);
        hash = 37 * hash + Objects.hashCode(this.rphRangoFechaFk);
        hash = 37 * hash + Objects.hashCode(this.rphComponentePlanGradoFk);
        hash = 37 * hash + Objects.hashCode(this.rphNumeroExtraordinario);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgRelPeriodosHabilitacionPeriodo other = (SgRelPeriodosHabilitacionPeriodo) obj;
        if (this.rphTipoPeriodoCalificacion != other.rphTipoPeriodoCalificacion) {
            return false;
        }
        if (this.rphTipoCalendarioEscolar != other.rphTipoCalendarioEscolar) {
            return false;
        }
        if (!Objects.equals(this.rphRangoFechaFk, other.rphRangoFechaFk)) {
            return false;
        }
        if (!Objects.equals(this.rphComponentePlanGradoFk, other.rphComponentePlanGradoFk)) {
            return false;
        }
        if (!Objects.equals(this.rphNumeroExtraordinario, other.rphNumeroExtraordinario)) {
            return false;
        }
        return true;
    }

    


    


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelPeriodosHabilitacionPeriodo[ rphPk=" + rphPk + " ]";
    }
    
}
