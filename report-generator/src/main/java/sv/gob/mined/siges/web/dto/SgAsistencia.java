/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "asiPk", scope = SgAsistencia.class)
public class SgAsistencia implements Serializable {

    private Long asiPk;

    private Boolean asiInasistencia;

    private String asiObservacion;

    private LocalDateTime asiUltModFecha;

    private String asiUltModUsuario;

    private Integer asiVersion;

    private SgEstudiante asiEstudiante;

    private SgMotivoInasistencia asiMotivoInasistencia;

    private SgControlAsistenciaCabezal asiControl;

    public SgAsistencia() {
        this.asiInasistencia = Boolean.FALSE;
    }

    public Long getAsiPk() {
        return asiPk;
    }

    public void setAsiPk(Long asiPk) {
        this.asiPk = asiPk;
    }

    public Boolean getAsiInasistencia() {
        return asiInasistencia;
    }

    public void setAsiInasistencia(Boolean asiInasistencia) {
        this.asiInasistencia = asiInasistencia;
        if (!this.asiInasistencia) {
            this.asiMotivoInasistencia = null;
            this.asiObservacion = null;
        }
    }

    public String getAsiObservacion() {
        return asiObservacion;
    }

    public void setAsiObservacion(String asiObservacion) {
        this.asiObservacion = asiObservacion;
    }

    public LocalDateTime getAsiUltModFecha() {
        return asiUltModFecha;
    }

    public void setAsiUltModFecha(LocalDateTime asiUltModFecha) {
        this.asiUltModFecha = asiUltModFecha;
    }

    public String getAsiUltModUsuario() {
        return asiUltModUsuario;
    }

    public void setAsiUltModUsuario(String asiUltModUsuario) {
        this.asiUltModUsuario = asiUltModUsuario;
    }

    public Integer getAsiVersion() {
        return asiVersion;
    }

    public void setAsiVersion(Integer asiVersion) {
        this.asiVersion = asiVersion;
    }

    public SgEstudiante getAsiEstudiante() {
        return asiEstudiante;
    }

    public void setAsiEstudiante(SgEstudiante asiEstudiante) {
        this.asiEstudiante = asiEstudiante;
    }

    public SgMotivoInasistencia getAsiMotivoInasistencia() {
        return asiMotivoInasistencia;
    }

    public void setAsiMotivoInasistencia(SgMotivoInasistencia asiMotivoInasistencia) {
        this.asiMotivoInasistencia = asiMotivoInasistencia;
    }

    public SgControlAsistenciaCabezal getAsiControl() {
        return asiControl;
    }

    public void setAsiControl(SgControlAsistenciaCabezal asiControl) {
        this.asiControl = asiControl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asiPk != null ? asiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAsistencia)) {
            return false;
        }
        SgAsistencia other = (SgAsistencia) object;
        if ((this.asiPk == null && other.asiPk != null) || (this.asiPk != null && !this.asiPk.equals(other.asiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAsistencia[ asiPk=" + asiPk + " ]";
    }

}
