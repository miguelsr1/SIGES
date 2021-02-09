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
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.siap2.SsTransferencia;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tgdPk", scope = SgTransferenciaGDep.class)
public class SgTransferenciaGDep implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tgdPk;

    private SsTransferencia tgdTransferenciaFk;
    
    private SgDepartamento tgdDepartamentoFk;

    private BigDecimal tgdMonto;

    private LocalDateTime tgdUltModFecha;

    private String tgdUltModUsuario;

    private Integer tgdVersion;
    
    private SgRequerimientoFondo tgdSolicitudTrans;
    
    public SgTransferenciaGDep() {

    }

    public Long getTgdPk() {
        return tgdPk;
    }

    public void setTgdPk(Long tgdPk) {
        this.tgdPk = tgdPk;
    }

    public SsTransferencia getTgdTransferenciaFk() {
        return tgdTransferenciaFk;
    }

    public void setTgdTransferenciaFk(SsTransferencia tgdTransferenciaFk) {
        this.tgdTransferenciaFk = tgdTransferenciaFk;
    }

    public SgDepartamento getTgdDepartamentoFk() {
        return tgdDepartamentoFk;
    }

    public void setTgdDepartamentoFk(SgDepartamento tgdDepartamentoFk) {
        this.tgdDepartamentoFk = tgdDepartamentoFk;
    }

    public BigDecimal getTgdMonto() {
        return tgdMonto;
    }

    public void setTgdMonto(BigDecimal tgdMonto) {
        this.tgdMonto = tgdMonto;
    }

    public LocalDateTime getTgdUltModFecha() {
        return tgdUltModFecha;
    }

    public void setTgdUltModFecha(LocalDateTime tgdUltModFecha) {
        this.tgdUltModFecha = tgdUltModFecha;
    }

    public String getTgdUltModUsuario() {
        return tgdUltModUsuario;
    }

    public void setTgdUltModUsuario(String tgdUltModUsuario) {
        this.tgdUltModUsuario = tgdUltModUsuario;
    }

    public Integer getTgdVersion() {
        return tgdVersion;
    }

    public void setTgdVersion(Integer tgdVersion) {
        this.tgdVersion = tgdVersion;
    }

    public SgRequerimientoFondo getTgdSolicitudTrans() {
        return tgdSolicitudTrans;
    }

    public void setTgdSolicitudTrans(SgRequerimientoFondo tgdSolicitudTrans) {
        this.tgdSolicitudTrans = tgdSolicitudTrans;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tgdPk != null ? tgdPk.hashCode() : 0);
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
        final SgTransferenciaGDep other = (SgTransferenciaGDep) obj;
        if (!Objects.equals(this.tgdPk, other.tgdPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTransferenciaGDep[ tgdPk=" + tgdPk + " ]";
    }
    
}
