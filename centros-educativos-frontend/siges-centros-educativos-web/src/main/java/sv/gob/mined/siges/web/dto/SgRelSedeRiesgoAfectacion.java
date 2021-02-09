/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgGradoAfectacion;
import sv.gob.mined.siges.web.dto.catalogo.SgTiposRiesgo;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rarPk", scope = SgRelSedeRiesgoAfectacion.class)
public class SgRelSedeRiesgoAfectacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rarPk;

    private SgTiposRiesgo rarTipoRiesgo;

    private SgGradoAfectacion rarGradoAfectacion;

    private LocalDateTime rarUltModFecha;

    private String rarUltModUsuario;

    private Integer rarVersion;

    public SgRelSedeRiesgoAfectacion() {

    }

    public Long getRarPk() {
        return rarPk;
    }

    public void setRarPk(Long rarPk) {
        this.rarPk = rarPk;
    }

    public SgTiposRiesgo getRarTipoRiesgo() {
        return rarTipoRiesgo;
    }

    public void setRarTipoRiesgo(SgTiposRiesgo rarTipoRiesgo) {
        this.rarTipoRiesgo = rarTipoRiesgo;
    }

    public SgGradoAfectacion getRarGradoAfectacion() {
        return rarGradoAfectacion;
    }

    public void setRarGradoAfectacion(SgGradoAfectacion rarGradoAfectacion) {
        this.rarGradoAfectacion = rarGradoAfectacion;
    }


    public LocalDateTime getRarUltModFecha() {
        return rarUltModFecha;
    }

    public void setRarUltModFecha(LocalDateTime rarUltModFecha) {
        this.rarUltModFecha = rarUltModFecha;
    }

    public String getRarUltModUsuario() {
        return rarUltModUsuario;
    }

    public void setRarUltModUsuario(String rarUltModUsuario) {
        this.rarUltModUsuario = rarUltModUsuario;
    }

    public Integer getRarVersion() {
        return rarVersion;
    }

    public void setRarVersion(Integer rarVersion) {
        this.rarVersion = rarVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rarPk != null ? rarPk.hashCode() : 0);
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
        final SgRelSedeRiesgoAfectacion other = (SgRelSedeRiesgoAfectacion) obj;
        if (!Objects.equals(this.rarPk, other.rarPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelSedeRiesgoAfectacion[ rarPk=" + rarPk + " ]";
    }

}
