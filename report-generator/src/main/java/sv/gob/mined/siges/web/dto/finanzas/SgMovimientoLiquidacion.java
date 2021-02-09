/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.finanzas;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mlqPk", scope = SgMovimientoLiquidacion.class)
public class SgMovimientoLiquidacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mlqPk;

    private SgMovimientoCuentaBancaria mlqMovimientoPk;

    private SgLiquidacion mlqLiquidacionPk;

    private Boolean mlqEvaluado;

    private String mlqComentario;
    
    private EnumMovimientosTipo mlqTipoMovimiento;

    private LocalDateTime mlqUltModFecha;

    private String mlqUltModUsuario;

    private Integer mlqVersion;
    
    
    public SgMovimientoLiquidacion() {
    }

    public Long getMlqPk() {
        return mlqPk;
    }

    public void setMlqPk(Long mlqPk) {
        this.mlqPk = mlqPk;
    }

    public SgMovimientoCuentaBancaria getMlqMovimientoPk() {
        return mlqMovimientoPk;
    }

    public void setMlqMovimientoPk(SgMovimientoCuentaBancaria mlqMovimientoPk) {
        this.mlqMovimientoPk = mlqMovimientoPk;
    }

    public SgLiquidacion getMlqLiquidacionPk() {
        return mlqLiquidacionPk;
    }

    public void setMlqLiquidacionPk(SgLiquidacion mlqLiquidacionPk) {
        this.mlqLiquidacionPk = mlqLiquidacionPk;
    }

    public Boolean getMlqEvaluado() {
        return mlqEvaluado;
    }

    public void setMlqEvaluado(Boolean mlqEvaluado) {
        this.mlqEvaluado = mlqEvaluado;
    }

    public String getMlqComentario() {
        return mlqComentario;
    }

    public void setMlqComentario(String mlqComentario) {
        this.mlqComentario = mlqComentario;
    }

    public LocalDateTime getMlqUltModFecha() {
        return mlqUltModFecha;
    }
    
    public void setMlqTipoMovimiento(EnumMovimientosTipo mlqTipoMovimiento) {
        this.mlqTipoMovimiento = mlqTipoMovimiento;
    }

    public EnumMovimientosTipo getMlqTipoMovimiento() {
        return mlqTipoMovimiento;
    }

    public void setMlqUltModFecha(LocalDateTime mlqUltModFecha) {
        this.mlqUltModFecha = mlqUltModFecha;
    }

    public String getMlqUltModUsuario() {
        return mlqUltModUsuario;
    }

    public void setMlqUltModUsuario(String mlqUltModUsuario) {
        this.mlqUltModUsuario = mlqUltModUsuario;
    }

    public Integer getMlqVersion() {
        return mlqVersion;
    }

    public void setMlqVersion(Integer mlqVersion) {
        this.mlqVersion = mlqVersion;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mlqPk != null ? mlqPk.hashCode() : 0);
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
        final SgMovimientoLiquidacion other = (SgMovimientoLiquidacion) obj;
        if (!Objects.equals(this.mlqPk, other.mlqPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMovimientoLiquidacion[ mlqPk=" + mlqPk + " ]";
    }
    
}
