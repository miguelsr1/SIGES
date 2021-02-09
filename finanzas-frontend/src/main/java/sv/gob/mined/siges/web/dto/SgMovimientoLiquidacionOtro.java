/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mloPk", scope = SgMovimientoLiquidacionOtro.class)
public class SgMovimientoLiquidacionOtro implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mloPk;

    private SgMovimientoCuentaBancaria mloMovimientoFk;

    private SgLiquidacionOtroIngreso mloLiquidacionOtroFk;
    
    private Boolean mloEvaluado;
    
    private String mloComentario;
    
    private EnumMovimientosTipo mloTipoMovimiento;

    private LocalDateTime mloUltModFecha;

    private String mloUltModUsuario;

    private Integer mloVersion;
    
    
    public SgMovimientoLiquidacionOtro() {
    }

    public Long getMloPk() {
        return mloPk;
    }

    public void setMloPk(Long mloPk) {
        this.mloPk = mloPk;
    }

    public SgMovimientoCuentaBancaria getMloMovimientoFk() {
        return mloMovimientoFk;
    }

    public void setMloMovimientoFk(SgMovimientoCuentaBancaria mloMovimientoFk) {
        this.mloMovimientoFk = mloMovimientoFk;
    }

    public SgLiquidacionOtroIngreso getMloLiquidacionOtroFk() {
        return mloLiquidacionOtroFk;
    }

    public void setMloLiquidacionOtroFk(SgLiquidacionOtroIngreso mloLiquidacionOtroFk) {
        this.mloLiquidacionOtroFk = mloLiquidacionOtroFk;
    }

    public Boolean getMloEvaluado() {
        return mloEvaluado;
    }

    public void setMloEvaluado(Boolean mloEvaluado) {
        this.mloEvaluado = mloEvaluado;
    }

    public String getMloComentario() {
        return mloComentario;
    }

    public void setMloComentario(String mloComentario) {
        this.mloComentario = mloComentario;
    }

    public EnumMovimientosTipo getMloTipoMovimiento() {
        return mloTipoMovimiento;
    }

    public void setMloTipoMovimiento(EnumMovimientosTipo mloTipoMovimiento) {
        this.mloTipoMovimiento = mloTipoMovimiento;
    }

    

    public LocalDateTime getMloUltModFecha() {
        return mloUltModFecha;
    }

    public void setMloUltModFecha(LocalDateTime mloUltModFecha) {
        this.mloUltModFecha = mloUltModFecha;
    }

    public String getMloUltModUsuario() {
        return mloUltModUsuario;
    }

    public void setMloUltModUsuario(String mloUltModUsuario) {
        this.mloUltModUsuario = mloUltModUsuario;
    }

    public Integer getMloVersion() {
        return mloVersion;
    }

    public void setMloVersion(Integer mloVersion) {
        this.mloVersion = mloVersion;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mloPk != null ? mloPk.hashCode() : 0);
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
        final SgMovimientoLiquidacionOtro other = (SgMovimientoLiquidacionOtro) obj;
        if (!Objects.equals(this.mloPk, other.mloPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMovimientoLiquidacionOtro[ mloPk=" + mloPk + " ]";
    }
    
}
