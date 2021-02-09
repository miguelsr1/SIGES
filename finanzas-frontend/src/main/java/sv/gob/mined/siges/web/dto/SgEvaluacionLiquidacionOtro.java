/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eliPk", scope = SgEvaluacionLiquidacionOtro.class)
public class SgEvaluacionLiquidacionOtro implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long eliPk;

    private SgLiquidacionOtroIngreso eliLiquidacionOtrosFk;
    
    private String eliReembolsoCheque;

    private BigDecimal eliReembolsoMonto;
    
    private BigDecimal eliReintegroMonto;

    private String eliComentario;

    private LocalDateTime eliUltModFecha;

    private String eliUltModUsuario;

    private Integer eliVersion;
    
    
    public SgEvaluacionLiquidacionOtro() {
    }

    public Long getEliPk() {
        return eliPk;
    }

    public void setEliPk(Long eliPk) {
        this.eliPk = eliPk;
    }

    public SgLiquidacionOtroIngreso getEliLiquidacionOtrosFk() {
        return eliLiquidacionOtrosFk;
    }

    public void setEliLiquidacionOtrosFk(SgLiquidacionOtroIngreso eliLiquidacionOtrosFk) {
        this.eliLiquidacionOtrosFk = eliLiquidacionOtrosFk;
    }

    public String getEliReembolsoCheque() {
        return eliReembolsoCheque;
    }

    public void setEliReembolsoCheque(String eliReembolsoCheque) {
        this.eliReembolsoCheque = eliReembolsoCheque;
    }

    public BigDecimal getEliReembolsoMonto() {
        return eliReembolsoMonto;
    }

    public void setEliReembolsoMonto(BigDecimal eliReembolsoMonto) {
        this.eliReembolsoMonto = eliReembolsoMonto;
    }

    public BigDecimal getEliReintegroMonto() {
        return eliReintegroMonto;
    }

    public void setEliReintegroMonto(BigDecimal eliReintegroMonto) {
        this.eliReintegroMonto = eliReintegroMonto;
    }

    public String getEliComentario() {
        return eliComentario;
    }

    public void setEliComentario(String eliComentario) {
        this.eliComentario = eliComentario;
    }


    public LocalDateTime getEliUltModFecha() {
        return eliUltModFecha;
    }

    public void setEliUltModFecha(LocalDateTime eliUltModFecha) {
        this.eliUltModFecha = eliUltModFecha;
    }

    public String getEliUltModUsuario() {
        return eliUltModUsuario;
    }

    public void setEliUltModUsuario(String eliUltModUsuario) {
        this.eliUltModUsuario = eliUltModUsuario;
    }

    public Integer getEliVersion() {
        return eliVersion;
    }

    public void setEliVersion(Integer eliVersion) {
        this.eliVersion = eliVersion;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eliPk != null ? eliPk.hashCode() : 0);
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
        final SgEvaluacionLiquidacionOtro other = (SgEvaluacionLiquidacionOtro) obj;
        if (!Objects.equals(this.eliPk, other.eliPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEvaluacionLiquidacionOtro[ eliPk=" + eliPk + " ]";
    }
    
}
