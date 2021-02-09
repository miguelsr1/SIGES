/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centroseducativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dirPk", scope = SgDireccion.class)
public class SgDireccion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dirPk;

    private String dirDireccion;

    private SgDepartamento dirDepartamento;

    private SgMunicipio dirMunicipio;

    private String dirCaserioTexto;

    private Double dirLatitud;

    private Double dirLongitud;

    private LocalDateTime dirUltModFecha;

    private String dirUltModUsuario;

    private Integer dirVersion;

    public SgDireccion() {
    }

    @JsonIgnore
    public String getDirCompleta() {
        StringBuilder s = new StringBuilder();
        if (!StringUtils.isBlank(this.dirDireccion)) {
            s.append(this.dirDireccion).append(", ");
        }
        if (!StringUtils.isBlank(this.dirCaserioTexto)) {
            s.append(this.dirCaserioTexto).append(", ");
        }
        if (this.dirMunicipio != null) {
            s.append(this.dirMunicipio.getMunNombre()).append(", ");
        }
        if (this.dirDepartamento != null) {
            s.append(this.dirDepartamento.getDepNombre()).append(".");
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    public Long getDirPk() {
        return dirPk;
    }

    public void setDirPk(Long dirPk) {
        this.dirPk = dirPk;
    }

    public LocalDateTime getDirUltModFecha() {
        return dirUltModFecha;
    }

    public void setDirUltModFecha(LocalDateTime dirUltModFecha) {
        this.dirUltModFecha = dirUltModFecha;
    }

    public String getDirUltModUsuario() {
        return dirUltModUsuario;
    }

    public void setDirUltModUsuario(String dirUltModUsuario) {
        this.dirUltModUsuario = dirUltModUsuario;
    }

    public Integer getDirVersion() {
        return dirVersion;
    }

    public void setDirVersion(Integer dirVersion) {
        this.dirVersion = dirVersion;
    }

    public String getDirDireccion() {
        return dirDireccion;
    }

    public void setDirDireccion(String dirDireccion) {
        this.dirDireccion = dirDireccion;
    }

    public SgDepartamento getDirDepartamento() {
        return dirDepartamento;
    }

    public void setDirDepartamento(SgDepartamento dirDepartamento) {
        this.dirDepartamento = dirDepartamento;
    }

    public SgMunicipio getDirMunicipio() {
        return dirMunicipio;
    }

    public void setDirMunicipio(SgMunicipio dirMunicipio) {
        this.dirMunicipio = dirMunicipio;
    }

    public String getDirCaserioTexto() {
        return dirCaserioTexto;
    }

    public void setDirCaserioTexto(String dirCaserioTexto) {
        this.dirCaserioTexto = dirCaserioTexto;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.dirPk);
    }

    public Double getDirLatitud() {
        return dirLatitud;
    }

    public void setDirLatitud(Double dirLatitud) {
        this.dirLatitud = dirLatitud;
    }

    public Double getDirLongitud() {
        return dirLongitud;
    }

    public void setDirLongitud(Double dirLongitud) {
        this.dirLongitud = dirLongitud;
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
        final SgDireccion other = (SgDireccion) obj;
        return this.dirPk != null && this.dirPk.equals(other.dirPk);
    }

    @Override
    public String toString() {
        return "SgDireccion{" + "dirPk=" + dirPk + ", dirDireccion=" + dirDireccion + ", dirDepartamento=" + dirDepartamento + ", dirMunicipio=" + dirMunicipio + ", dirUltModFecha=" + dirUltModFecha + ", dirUltModUsuario=" + dirUltModUsuario + ", dirVersion=" + dirVersion + '}';
    }

}
