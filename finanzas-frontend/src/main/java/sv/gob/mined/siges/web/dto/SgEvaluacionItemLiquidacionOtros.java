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

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eloPk", scope = SgEvaluacionItemLiquidacionOtros.class)
public class SgEvaluacionItemLiquidacionOtros implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long eloPk;

     private SgLiquidacionOtroIngreso eloLoiFk;
    
    private SgItemLiquidacion eloItemFk;

    private Boolean eloMarcado;
    
    private LocalDateTime eloUltModFecha;

    private String eloUltModUsuario;

    private Integer eloVersion;
    
    
    public SgEvaluacionItemLiquidacionOtros() {
    }

    public Long getEloPk() {
        return eloPk;
    }

    public void setEloPk(Long eloPk) {
        this.eloPk = eloPk;
    }

    public SgLiquidacionOtroIngreso getEloLoiFk() {
        return eloLoiFk;
    }

    public void setEloLoiFk(SgLiquidacionOtroIngreso eloLoiFk) {
        this.eloLoiFk = eloLoiFk;
    }

    public SgItemLiquidacion getEloItemFk() {
        return eloItemFk;
    }

    public void setEloItemFk(SgItemLiquidacion eloItemFk) {
        this.eloItemFk = eloItemFk;
    }

    public Boolean getEloMarcado() {
        return eloMarcado;
    }

    public void setEloMarcado(Boolean eloMarcado) {
        this.eloMarcado = eloMarcado;
    }

    
    public LocalDateTime getEloUltModFecha() {
        return eloUltModFecha;
    }

    public void setEloUltModFecha(LocalDateTime eloUltModFecha) {
        this.eloUltModFecha = eloUltModFecha;
    }

    public String getEloUltModUsuario() {
        return eloUltModUsuario;
    }

    public void setEloUltModUsuario(String eloUltModUsuario) {
        this.eloUltModUsuario = eloUltModUsuario;
    }

    public Integer getEloVersion() {
        return eloVersion;
    }

    public void setEloVersion(Integer eloVersion) {
        this.eloVersion = eloVersion;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eloPk != null ? eloPk.hashCode() : 0);
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
        final SgEvaluacionItemLiquidacionOtros other = (SgEvaluacionItemLiquidacionOtros) obj;
        if (!Objects.equals(this.eloPk, other.eloPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEvaluacionItemLiquidacionOtros[ eloPk=" + eloPk + " ]";
    }
    
}
