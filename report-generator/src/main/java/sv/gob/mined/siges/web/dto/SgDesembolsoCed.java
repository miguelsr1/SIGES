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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dcePk", scope = SgDesembolsoCed.class)
public class SgDesembolsoCed implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long dcePk;

    private SgDetalleDesembolso dceDetDesembolsoFk;
    
    private SgReqFondoCed dceReqFondoCedFk;

    private BigDecimal dceMonto;

    private LocalDateTime dceUltModFecha;

    private String dceUltModUsuario;

    private Integer dceVersion;
    
    
    public SgDesembolsoCed() {
        
    }

    public Long getDcePk() {
        return dcePk;
    }

    public void setDcePk(Long dcePk) {
        this.dcePk = dcePk;
    }

   
    public LocalDateTime getDceUltModFecha() {
        return dceUltModFecha;
    }

    public void setDceUltModFecha(LocalDateTime dceUltModFecha) {
        this.dceUltModFecha = dceUltModFecha;
    }

    public String getDceUltModUsuario() {
        return dceUltModUsuario;
    }

    public void setDceUltModUsuario(String dceUltModUsuario) {
        this.dceUltModUsuario = dceUltModUsuario;
    }

    public Integer getDceVersion() {
        return dceVersion;
    }

    public void setDceVersion(Integer dceVersion) {
        this.dceVersion = dceVersion;
    }

    public SgDetalleDesembolso getDceDetDesembolsoFk() {
        return dceDetDesembolsoFk;
    }

    public void setDceDetDesembolsoFk(SgDetalleDesembolso dceDetDesembolsoFk) {
        this.dceDetDesembolsoFk = dceDetDesembolsoFk;
    }

    public SgReqFondoCed getDceReqFondoCedFk() {
        return dceReqFondoCedFk;
    }

    public void setDceReqFondoCedFk(SgReqFondoCed dceReqFondoCedFk) {
        this.dceReqFondoCedFk = dceReqFondoCedFk;
    }


    public BigDecimal getDceMonto() {
        return dceMonto;
    }

    public void setDceMonto(BigDecimal dceMonto) {
        this.dceMonto = dceMonto;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dcePk != null ? dcePk.hashCode() : 0);
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
        final SgDesembolsoCed other = (SgDesembolsoCed) obj;
        if (!Objects.equals(this.dcePk, other.dcePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgDesembolsoCed[ dcePk=" + dcePk + " ]";
    }
    
}
