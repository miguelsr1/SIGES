/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.finanzas;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "elqPk", scope = SgEvaluacionLiquidacion.class)
public class SgEvaluacionLiquidacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long elqPk;

    private SgLiquidacion elqLiquidacionFk;
    
    private String elqReembolsoCheque;
    
    private BigDecimal elqReembolsoMonto;
    
    private BigDecimal elqReintegroMonto;
    
    private String elqComentario;
    
    private LocalDateTime elqUltModFecha;

    private String elqUltModUsuario;

    private Integer elqVersion;
    
    
    public SgEvaluacionLiquidacion() {
        
    }

    public Long getElqPk() {
        return elqPk;
    }

    public void setElqPk(Long elqPk) {
        this.elqPk = elqPk;
    }

    public SgLiquidacion getElqLiquidacionFk() {
        return elqLiquidacionFk;
    }

    public void setElqLiquidacionFk(SgLiquidacion elqLiquidacionFk) {
        this.elqLiquidacionFk = elqLiquidacionFk;
    }

    public String getElqReembolsoCheque() {
        return elqReembolsoCheque;
    }

    public void setElqReembolsoCheque(String elqReembolsoCheque) {
        this.elqReembolsoCheque = elqReembolsoCheque;
    }

    public BigDecimal getElqReembolsoMonto() {
        return elqReembolsoMonto;
    }

    public void setElqReembolsoMonto(BigDecimal elqReembolsoMonto) {
        this.elqReembolsoMonto = elqReembolsoMonto;
    }

    public BigDecimal getElqReintegroMonto() {
        return elqReintegroMonto;
    }

    public void setElqReintegroMonto(BigDecimal elqReintegroMonto) {
        this.elqReintegroMonto = elqReintegroMonto;
    }

    public String getElqComentario() {
        return elqComentario;
    }

    public void setElqComentario(String elqComentario) {
        this.elqComentario = elqComentario;
    }
    
    

    public LocalDateTime getElqUltModFecha() {
        return elqUltModFecha;
    }

    public void setElqUltModFecha(LocalDateTime elqUltModFecha) {
        this.elqUltModFecha = elqUltModFecha;
    }

    public String getElqUltModUsuario() {
        return elqUltModUsuario;
    }

    public void setElqUltModUsuario(String elqUltModUsuario) {
        this.elqUltModUsuario = elqUltModUsuario;
    }

    public Integer getElqVersion() {
        return elqVersion;
    }

    public void setElqVersion(Integer elqVersion) {
        this.elqVersion = elqVersion;
    }

    
   


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (elqPk != null ? elqPk.hashCode() : 0);
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
        final SgEvaluacionLiquidacion other = (SgEvaluacionLiquidacion) obj;
        if (!Objects.equals(this.elqPk, other.elqPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEvaluacionLiquidacion[ elqPk=" + elqPk + " ]";
    }
    
}
