/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "aniPk", scope = SgAnioFiscal.class)
public class SgAnioFiscal implements Serializable {

    private Long aniPk;

    private Integer aniAnio;

    private LocalDateTime aniDesde;

    private Boolean aniEjecucion;

    private Boolean aniPlanificacion;

    private LocalDateTime aniHasta;

    private String aniNombre;

    private LocalDateTime aniUltModFecha;

    private String aniUltModUsuario;

    private Integer aniVersion;

    private Boolean aniCerrado;

    private Boolean aniFormulacionCe;

    private Boolean aniAjusteCe;

    public SgAnioFiscal() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getAniPk() {
        return aniPk;
    }

    public void setAniPk(Long aniPk) {
        this.aniPk = aniPk;
    }

    public Integer getAniAnio() {
        return aniAnio;
    }

    public void setAniAnio(Integer aniAnio) {
        this.aniAnio = aniAnio;
    }

    public LocalDateTime getAniDesde() {
        return aniDesde;
    }

    public void setAniDesde(LocalDateTime aniDesde) {
        this.aniDesde = aniDesde;
    }

    public Boolean getAniEjecucion() {
        return aniEjecucion;
    }

    public void setAniEjecucion(Boolean aniEjecucion) {
        this.aniEjecucion = aniEjecucion;
    }

    public Boolean getAniPlanificacion() {
        return aniPlanificacion;
    }

    public void setAniPlanificacion(Boolean aniPlanificacion) {
        this.aniPlanificacion = aniPlanificacion;
    }

    public LocalDateTime getAniHasta() {
        return aniHasta;
    }

    public void setAniHasta(LocalDateTime aniHasta) {
        this.aniHasta = aniHasta;
    }

    public String getAniNombre() {
        return aniNombre;
    }

    public void setAniNombre(String aniNombre) {
        this.aniNombre = aniNombre;
    }

    public LocalDateTime getAniUltModFecha() {
        return aniUltModFecha;
    }

    public void setAniUltModFecha(LocalDateTime aniUltModFecha) {
        this.aniUltModFecha = aniUltModFecha;
    }

    public String getAniUltModUsuario() {
        return aniUltModUsuario;
    }

    public void setAniUltModUsuario(String aniUltModUsuario) {
        this.aniUltModUsuario = aniUltModUsuario;
    }

    public Integer getAniVersion() {
        return aniVersion;
    }

    public void setAniVersion(Integer aniVersion) {
        this.aniVersion = aniVersion;
    }

    public Boolean getAniCerrado() {
        return aniCerrado;
    }

    public void setAniCerrado(Boolean aniCerrado) {
        this.aniCerrado = aniCerrado;
    }

    public Boolean getAniFormulacionCe() {
        return aniFormulacionCe;
    }

    public void setAniFormulacionCe(Boolean aniFormulacionCe) {
        this.aniFormulacionCe = aniFormulacionCe;
    }

    public Boolean getAniAjusteCe() {
        return aniAjusteCe;
    }

    public void setAniAjusteCe(Boolean aniAjusteCe) {
        this.aniAjusteCe = aniAjusteCe;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aniPk != null ? aniPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAnioFiscal)) {
            return false;
        }
        SgAnioFiscal other = (SgAnioFiscal) object;
        if ((this.aniPk == null && other.aniPk != null) || (this.aniPk != null && !this.aniPk.equals(other.aniPk))) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAnioFiscal[ aniPk=" + aniPk + " ]";
    }
    // </editor-fold>

}
