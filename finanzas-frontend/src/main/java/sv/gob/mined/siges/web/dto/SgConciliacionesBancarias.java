/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "conPk", scope = SgConciliacionesBancarias.class)
public class SgConciliacionesBancarias implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long conPk;
    private SgCuentasBancarias conCuentaFk;
    private LocalDate conFechaDesde;
    private LocalDate conFechaHasta;
    private BigDecimal conMonto;
    private LocalDateTime conUltModFecha;
    private String conUltModUsuario;
    private Integer conVersion;

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public SgConciliacionesBancarias() {
    }

    public Long getConPk() {
        return conPk;
    }

    public void setConPk(Long conPk) {
        this.conPk = conPk;
    }

    public SgCuentasBancarias getConCuentaFk() {
        return conCuentaFk;
    }

    public void setConCuentaFk(SgCuentasBancarias conCuentaFk) {
        this.conCuentaFk = conCuentaFk;
    }

    public LocalDate getConFechaDesde() {
        return conFechaDesde;
    }

    public void setConFechaDesde(LocalDate conFechaDesde) {
        this.conFechaDesde = conFechaDesde;
    }

    public LocalDate getConFechaHasta() {
        return conFechaHasta;
    }

    public void setConFechaHasta(LocalDate conFechaHasta) {
        this.conFechaHasta = conFechaHasta;
    }

    public BigDecimal getConMonto() {
        return conMonto;
    }

    public void setConMonto(BigDecimal conMonto) {
        this.conMonto = conMonto;
    }

    public LocalDateTime getConUltModFecha() {
        return conUltModFecha;
    }

    public void setConUltModFecha(LocalDateTime conUltModFecha) {
        this.conUltModFecha = conUltModFecha;
    }

    public String getConUltModUsuario() {
        return conUltModUsuario;
    }

    public void setConUltModUsuario(String conUltModUsuario) {
        this.conUltModUsuario = conUltModUsuario;
    }

    public Integer getConVersion() {
        return conVersion;
    }

    public void setConVersion(Integer conVersion) {
        this.conVersion = conVersion;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.conPk);
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
        final SgConciliacionesBancarias other = (SgConciliacionesBancarias) obj;
        if (!Objects.equals(this.conPk, other.conPk)) {
            return false;
        }
        return true;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">

    @Override
    public String toString() {
        return "com.sofis.entidades.SgConciliacionesBancarias[ conPk=" + conPk + " ]";
    }

}  // </editor-fold>
