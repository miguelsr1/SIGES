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
import java.time.LocalDateTime;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.EnumDesembolsoEstado;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dsbPk", scope = SgDesembolso.class)
public class SgDesembolso implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dsbPk;

    private EnumDesembolsoEstado dsbEstado;

    private Double dsbPorcentaje;

    private BigDecimal dsbMonto;

    private LocalDateTime dsbUltModFecha;

    private String dsbltModUsuario;

    private Integer dsbVersion;

    private Boolean seleccionado;

    private List<SgDetalleDesembolso> dsbDetalleDesembolso;

    public SgDesembolso() {
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public Long getDsbPk() {
        return dsbPk;
    }

    public void setDsbPk(Long dsbPk) {
        this.dsbPk = dsbPk;
    }

    public LocalDateTime getDsbUltModFecha() {
        return dsbUltModFecha;
    }

    public void setDsbUltModFecha(LocalDateTime dsbUltModFecha) {
        this.dsbUltModFecha = dsbUltModFecha;
    }

    public String getDsbltModUsuario() {
        return dsbltModUsuario;
    }

    public void setDsbltModUsuario(String dsbltModUsuario) {
        this.dsbltModUsuario = dsbltModUsuario;
    }

    public Integer getDsbVersion() {
        return dsbVersion;
    }

    public void setDsbVersion(Integer dsbVersion) {
        this.dsbVersion = dsbVersion;
    }

    public Double getDsbPorcentaje() {
        return dsbPorcentaje;
    }

    public void setDsbPorcentaje(Double dsbPorcentaje) {
        this.dsbPorcentaje = dsbPorcentaje;
    }

    public BigDecimal getDsbMonto() {
        return dsbMonto;
    }

    public void setDsbMonto(BigDecimal dsbMonto) {
        this.dsbMonto = dsbMonto;
    }

    public EnumDesembolsoEstado getDsbEstado() {
        return dsbEstado;
    }

    public void setDsbEstado(EnumDesembolsoEstado dsbEstado) {
        this.dsbEstado = dsbEstado;
    }

    public Boolean getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public List<SgDetalleDesembolso> getDsbDetalleDesembolso() {
        return dsbDetalleDesembolso;
    }

    public void setDsbDetalleDesembolso(List<SgDetalleDesembolso> dsbDetalleDesembolso) {
        this.dsbDetalleDesembolso = dsbDetalleDesembolso;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dsbPk != null ? dsbPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDesembolso)) {
            return false;
        }
        SgDesembolso other = (SgDesembolso) object;
        if ((this.dsbPk == null && other.dsbPk != null) || (this.dsbPk != null && !this.dsbPk.equals(other.dsbPk))) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto[ dsbPk=" + dsbPk + " ]";
    }

    // </editor-fold>
}
