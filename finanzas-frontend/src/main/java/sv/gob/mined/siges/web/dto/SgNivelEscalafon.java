/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nesPk", scope = SgNivelEscalafon.class)
public class SgNivelEscalafon implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nesPk;

    private String nesCodigo;

    private String nesNombre;

    private String nesNombreBusqueda;

    private Boolean nesHabilitado;

    private LocalDateTime nesUltModFecha;

    private String nesUltModUsuario;

    private Integer nesVersion;

    public SgNivelEscalafon() {
    }

    public SgNivelEscalafon(Long nesPk) {
        this.nesPk = nesPk;
    }

    public Long getNesPk() {
        return nesPk;
    }

    public void setNesPk(Long nesPk) {
        this.nesPk = nesPk;
    }

    public String getNesCodigo() {
        return nesCodigo;
    }

    public void setNesCodigo(String nesCodigo) {
        this.nesCodigo = nesCodigo;
    }

    public String getNesNombre() {
        return nesNombre;
    }

    public void setNesNombre(String nesNombre) {
        this.nesNombre = nesNombre;
    }

    public String getNesNombreBusqueda() {
        return nesNombreBusqueda;
    }

    public void setNesNombreBusqueda(String nesNombreBusqueda) {
        this.nesNombreBusqueda = nesNombreBusqueda;
    }

    public Boolean getNesHabilitado() {
        return nesHabilitado;
    }

    public void setNesHabilitado(Boolean nesHabilitado) {
        this.nesHabilitado = nesHabilitado;
    }

    public LocalDateTime getNesUltModFecha() {
        return nesUltModFecha;
    }

    public void setNesUltModFecha(LocalDateTime nesUltModFecha) {
        this.nesUltModFecha = nesUltModFecha;
    }

    public String getNesUltModUsuario() {
        return nesUltModUsuario;
    }

    public void setNesUltModUsuario(String nesUltModUsuario) {
        this.nesUltModUsuario = nesUltModUsuario;
    }

    public Integer getNesVersion() {
        return nesVersion;
    }

    public void setNesVersion(Integer nesVersion) {
        this.nesVersion = nesVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nesPk != null ? nesPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgNivelEscalafon)) {
            return false;
        }
        SgNivelEscalafon other = (SgNivelEscalafon) object;
        if ((this.nesPk == null && other.nesPk != null) || (this.nesPk != null && !this.nesPk.equals(other.nesPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgNivelEscalafon{" + "nesPk=" + nesPk + '}';
    }

}
