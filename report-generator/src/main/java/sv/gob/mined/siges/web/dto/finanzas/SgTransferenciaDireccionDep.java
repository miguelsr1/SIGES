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
import sv.gob.mined.siges.web.dto.siap2.SsTransferenciaComponente;
import sv.gob.mined.siges.web.enumerados.EnumTransferenciaEstado;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tddPk", scope = SgTransferenciaDireccionDep.class)
public class SgTransferenciaDireccionDep implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tddPk;

    private SsTransferenciaComponente tddTransferenciaFk;
    
    private SgDireccionDepartamental tddDireccionDepFk;

    private BigDecimal tddMontoAutorizado;
    
    private BigDecimal tddMontoSolicitado;
    
    private EnumTransferenciaEstado tddEstado;

    private LocalDateTime tddUltModFecha;

    private String tddUltModUsuario;

    private Integer tddVersion;
    
    
    public SgTransferenciaDireccionDep() {
    }

    public Long getTddPk() {
        return tddPk;
    }

    public SsTransferenciaComponente getTddTransferenciaFk() {
        return tddTransferenciaFk;
    }

    public void setTddTransferenciaFk(SsTransferenciaComponente tddTransferenciaFk) {
        this.tddTransferenciaFk = tddTransferenciaFk;
    } 

    public SgDireccionDepartamental getTddDireccionDepFk() {
        return tddDireccionDepFk;
    }

    public void setTddDireccionDepFk(SgDireccionDepartamental tddDireccionDepFk) {
        this.tddDireccionDepFk = tddDireccionDepFk;
    }

    public BigDecimal getTddMontoAutorizado() {
        return tddMontoAutorizado;
    }

    public void setTddMontoAutorizado(BigDecimal tddMontoAutorizado) {
        this.tddMontoAutorizado = tddMontoAutorizado;
    }

    public BigDecimal getTddMontoSolicitado() {
        return tddMontoSolicitado;
    }

    public void setTddMontoSolicitado(BigDecimal tddMontoSolicitado) {
        this.tddMontoSolicitado = tddMontoSolicitado;
    }

    public EnumTransferenciaEstado getTddEstado() {
        return tddEstado;
    }

    public void setTddEstado(EnumTransferenciaEstado tddEstado) {
        this.tddEstado = tddEstado;
    }

   
    public LocalDateTime getTddUltModFecha() {
        return tddUltModFecha;
    }

    public void setTddUltModFecha(LocalDateTime tddUltModFecha) {
        this.tddUltModFecha = tddUltModFecha;
    }

    public String getTddUltModUsuario() {
        return tddUltModUsuario;
    }

    public void setTddUltModUsuario(String tddUltModUsuario) {
        this.tddUltModUsuario = tddUltModUsuario;
    }

    public Integer getTddVersion() {
        return tddVersion;
    }

    public void setTddVersion(Integer tddVersion) {
        this.tddVersion = tddVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tddPk != null ? tddPk.hashCode() : 0);
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
        final SgTransferenciaDireccionDep other = (SgTransferenciaDireccionDep) obj;
        if (!Objects.equals(this.tddPk, other.tddPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTransferenciaDireccionDep[ tddPk=" + tddPk + " ]";
    }
    
    
    
}
