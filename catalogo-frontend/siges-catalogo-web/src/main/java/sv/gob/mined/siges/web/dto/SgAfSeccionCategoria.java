/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.enumerados.EnumSeccionesCargoBienes;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "scaPk", scope = SgAfSeccionCategoria.class)
public class SgAfSeccionCategoria implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long scaPk;
    private EnumSeccionesCargoBienes scaSeccion;
    private LocalDateTime scaUltModFecha;
    private String scaUltModUsuario;
    private Integer scaVersion;
    private SgAfCategoriaBienes scaCategoriaFk;

    public SgAfSeccionCategoria() {
    }

    public SgAfSeccionCategoria(Long scaPk) {
        this.scaPk = scaPk;
    }

    public Long getScaPk() {
        return scaPk;
    }

    public void setScaPk(Long scaPk) {
        this.scaPk = scaPk;
    }

    public EnumSeccionesCargoBienes getScaSeccion() {
        return scaSeccion;
    }

    public void setScaSeccion(EnumSeccionesCargoBienes scaSeccion) {
        this.scaSeccion = scaSeccion;
    }

    public LocalDateTime getScaUltModFecha() {
        return scaUltModFecha;
    }

    public void setScaUltModFecha(LocalDateTime scaUltModFecha) {
        this.scaUltModFecha = scaUltModFecha;
    }

    public String getScaUltModUsuario() {
        return scaUltModUsuario;
    }

    public void setScaUltModUsuario(String scaUltModUsuario) {
        this.scaUltModUsuario = scaUltModUsuario;
    }

    public Integer getScaVersion() {
        return scaVersion;
    }

    public void setScaVersion(Integer scaVersion) {
        this.scaVersion = scaVersion;
    }

    public SgAfCategoriaBienes getScaCategoriaFk() {
        return scaCategoriaFk;
    }

    public void setScaCategoriaFk(SgAfCategoriaBienes scaCategoriaFk) {
        this.scaCategoriaFk = scaCategoriaFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scaPk != null ? scaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfSeccionCategoria)) {
            return false;
        }
        SgAfSeccionCategoria other = (SgAfSeccionCategoria) object;
        if ((this.scaPk == null && other.scaPk != null) || (this.scaPk != null && !this.scaPk.equals(other.scaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfSeccionesCategoria[ scaPk=" + scaPk + " ]";
    }
    
}
