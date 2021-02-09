/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "reaPk", scope = SgRelModEdModAten.class)
public class SgRelModEdModAten implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long reaPk;

    private SgModalidad reaModalidadEducativa;

    private SgModalidadAtencion reaModalidadAtencion;

    private SgSubModalidadAtencion reaSubModalidadAtencion;

    private Boolean reaHabilitado;

    private LocalDateTime reaUltModFecha;

    private String reaUltModUsuario;

    private Integer reaVersion;

    private List<SgGrado> reaGrado;

    public SgRelModEdModAten() {
        this.reaHabilitado = Boolean.TRUE;
    }

    public SgRelModEdModAten(Long reaPk) {
        this.reaPk = reaPk;
    }

    public Long getReaPk() {
        return reaPk;
    }

    public void setReaPk(Long reaPk) {
        this.reaPk = reaPk;
    }

    public SgModalidad getReaModalidadEducativa() {
        return reaModalidadEducativa;
    }

    public void setReaModalidadEducativa(SgModalidad reaModalidadEducativa) {
        this.reaModalidadEducativa = reaModalidadEducativa;
    }

    public SgModalidadAtencion getReaModalidadAtencion() {
        return reaModalidadAtencion;
    }

    public void setReaModalidadAtencion(SgModalidadAtencion reaModalidadAtencion) {
        this.reaModalidadAtencion = reaModalidadAtencion;
    }

    public SgSubModalidadAtencion getReaSubModalidadAtencion() {
        return reaSubModalidadAtencion;
    }

    public void setReaSubModalidadAtencion(SgSubModalidadAtencion reaSubModalidadAtencion) {
        this.reaSubModalidadAtencion = reaSubModalidadAtencion;
    }

    public Boolean getReaHabilitado() {
        return reaHabilitado;
    }

    public void setReaHabilitado(Boolean reaHabilitado) {
        this.reaHabilitado = reaHabilitado;
    }

    public LocalDateTime getReaUltModFecha() {
        return reaUltModFecha;
    }

    public void setReaUltModFecha(LocalDateTime reaUltModFecha) {
        this.reaUltModFecha = reaUltModFecha;
    }

    public String getReaUltModUsuario() {
        return reaUltModUsuario;
    }

    public void setReaUltModUsuario(String reaUltModUsuario) {
        this.reaUltModUsuario = reaUltModUsuario;
    }

    public Integer getReaVersion() {
        return reaVersion;
    }

    public void setReaVersion(Integer reaVersion) {
        this.reaVersion = reaVersion;
    }

    public List<SgGrado> getReaGrado() {
        return reaGrado;
    }

    public void setReaGrado(List<SgGrado> reaGrado) {
        this.reaGrado = reaGrado;
    }

    public String getReaModalidadAtencionNombre() {
        return this.reaModalidadAtencion != null ? this.reaModalidadAtencion.getMatNombre() : "";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reaPk != null ? reaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRelModEdModAten)) {
            return false;
        }
        SgRelModEdModAten other = (SgRelModEdModAten) object;
        if ((this.reaPk == null && other.reaPk != null) || (this.reaPk != null && !this.reaPk.equals(other.reaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelModEdModAten[ reaPk=" + reaPk + " ]";
    }

}
