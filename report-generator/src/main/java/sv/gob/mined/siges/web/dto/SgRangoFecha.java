/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rfePk", scope = SgRangoFecha.class)
public class SgRangoFecha implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rfePk;

    private String rfeCodigo;

    private LocalDate rfeFechaDesde;

    private LocalDate rfeFechaHasta;

    private Boolean rfeHabilitado;

    private LocalDateTime rfeUltModFecha;

    private String rfeUltModUsuario;

    private Integer rfeVersion;

    private SgPeriodoCalificacion rfePeriodoCalificacion;

    public SgRangoFecha() {
    }

    public Long getRfePk() {
        return rfePk;
    }

    public void setRfePk(Long rfePk) {
        this.rfePk = rfePk;
    }

    public String getRfeCodigo() {
        return rfeCodigo;
    }

    public void setRfeCodigo(String rfeCodigo) {
        this.rfeCodigo = rfeCodigo;
    }

    public LocalDate getRfeFechaDesde() {
        return rfeFechaDesde;
    }

    public void setRfeFechaDesde(LocalDate rfeFechaDesde) {
        this.rfeFechaDesde = rfeFechaDesde;
    }

    public LocalDate getRfeFechaHasta() {
        return rfeFechaHasta;
    }

    public void setRfeFechaHasta(LocalDate rfeFechaHasta) {
        this.rfeFechaHasta = rfeFechaHasta;
    }

    public Boolean getRfeHabilitado() {
        return rfeHabilitado;
    }

    public void setRfeHabilitado(Boolean rfeHabilitado) {
        this.rfeHabilitado = rfeHabilitado;
    }

    public LocalDateTime getRfeUltModFecha() {
        return rfeUltModFecha;
    }

    public void setRfeUltModFecha(LocalDateTime rfeUltModFecha) {
        this.rfeUltModFecha = rfeUltModFecha;
    }

    public String getRfeUltModUsuario() {
        return rfeUltModUsuario;
    }

    public void setRfeUltModUsuario(String rfeUltModUsuario) {
        this.rfeUltModUsuario = rfeUltModUsuario;
    }

    public Integer getRfeVersion() {
        return rfeVersion;
    }

    public void setRfeVersion(Integer rfeVersion) {
        this.rfeVersion = rfeVersion;
    }

    public SgPeriodoCalificacion getRfePeriodoCalificacion() {
        return rfePeriodoCalificacion;
    }

    public void setRfePeriodoCalificacion(SgPeriodoCalificacion rfePeriodoCalificacion) {
        this.rfePeriodoCalificacion = rfePeriodoCalificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rfePk != null ? rfePk.hashCode() : 0);
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
        final SgRangoFecha other = (SgRangoFecha) obj;
        if (!Objects.equals(this.rfePk, other.rfePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgRangoFecha[ rfePk=" + rfePk + " ]";
    }

}
