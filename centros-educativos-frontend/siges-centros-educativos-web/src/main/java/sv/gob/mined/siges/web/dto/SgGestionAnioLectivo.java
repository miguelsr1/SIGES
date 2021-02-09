/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "galPk", scope = SgGestionAnioLectivo.class)
public class SgGestionAnioLectivo implements Serializable {

    private Long galPk;

    private String galCodigo;

    private Integer galAnio;

    private LocalDate galFechaDesde;

    private LocalDate galFechaHasta;

    private Boolean galAnioActual;

    private Boolean galHabilitado;

    private LocalDateTime galUltModFecha;

    private String galUltModUsuario;

    private Integer galVersion;

    public SgGestionAnioLectivo() {
    }

    public SgGestionAnioLectivo(Long galPk) {
        this.galPk = galPk;
    }

    public Long getGalPk() {
        return galPk;
    }

    public void setGalPk(Long galPk) {
        this.galPk = galPk;
    }

    public String getGalCodigo() {
        return galCodigo;
    }

    public void setGalCodigo(String galCodigo) {
        this.galCodigo = galCodigo;
    }

    public Integer getGalAnio() {
        return galAnio;
    }

    public void setGalAnio(Integer galAnio) {
        this.galAnio = galAnio;
    }

    public LocalDate getGalFechaDesde() {
        return galFechaDesde;
    }

    public void setGalFechaDesde(LocalDate galFechaDesde) {
        this.galFechaDesde = galFechaDesde;
    }

    public LocalDate getGalFechaHasta() {
        return galFechaHasta;
    }

    public void setGalFechaHasta(LocalDate galFechaHasta) {
        this.galFechaHasta = galFechaHasta;
    }

    public Boolean getGalAnioActual() {
        return galAnioActual;
    }

    public void setGalAnioActual(Boolean galAnioActual) {
        this.galAnioActual = galAnioActual;
    }

    public Boolean getGalHabilitado() {
        return galHabilitado;
    }

    public void setGalHabilitado(Boolean galHabilitado) {
        this.galHabilitado = galHabilitado;
    }

    public LocalDateTime getGalUltModFecha() {
        return galUltModFecha;
    }

    public void setGalUltModFecha(LocalDateTime galUltModFecha) {
        this.galUltModFecha = galUltModFecha;
    }

    public String getGalUltModUsuario() {
        return galUltModUsuario;
    }

    public void setGalUltModUsuario(String galUltModUsuario) {
        this.galUltModUsuario = galUltModUsuario;
    }

    public Integer getGalVersion() {
        return galVersion;
    }

    public void setGalVersion(Integer galVersion) {
        this.galVersion = galVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (galPk != null ? galPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgGestionAnioLectivo)) {
            return false;
        }
        SgGestionAnioLectivo other = (SgGestionAnioLectivo) object;
        if ((this.galPk == null && other.galPk != null) || (this.galPk != null && !this.galPk.equals(other.galPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgGestionAnioLectivo[ galPk=" + galPk + " ]";
    }

}
