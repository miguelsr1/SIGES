/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.persistence.Transient;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rfePk", scope = SgRangoFecha.class)
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
    
    @Transient
    private Boolean rfeHabilitadoPorSolicitud;

    public SgRangoFecha() {
    }
    
    @JsonIgnore
    public String getRfeCodigoRango(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return rfeCodigo+" - "+rfeFechaDesde.format(formatter)+" - "+rfeFechaHasta.format(formatter);
    }
    
    @JsonIgnore
    public String getRfeCodigoRangoPerido(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return rfePeriodoCalificacion.getPcaNombre()+" - "+ rfeCodigo+" - "+rfeFechaDesde.format(formatter)+" - "+rfeFechaHasta.format(formatter);
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

    public Boolean getRfeHabilitadoPorSolicitud() {
        return rfeHabilitadoPorSolicitud;
    }

    public void setRfeHabilitadoPorSolicitud(Boolean rfeHabilitadoPorSolicitud) {
        this.rfeHabilitadoPorSolicitud = rfeHabilitadoPorSolicitud;
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
