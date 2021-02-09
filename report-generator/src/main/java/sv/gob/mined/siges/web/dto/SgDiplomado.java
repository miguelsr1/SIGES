/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dipPk", scope = SgDiplomado.class)
public class SgDiplomado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dipPk;

    private String dipCodigo;

    private String dipNombre;

    private Boolean dipHabilitado;

    private String dipDescripcion;

    private LocalDateTime dipUltModFecha;

    private String dipUltModUsuario;

    private Integer dipVersion;

    private List<SgModuloDiplomado> dipModulosDiplomado;

    public SgDiplomado() {
        this.dipHabilitado = Boolean.TRUE;
    }

    public Long getDipPk() {
        return dipPk;
    }

    public void setDipPk(Long dipPk) {
        this.dipPk = dipPk;
    }

    public String getDipCodigo() {
        return dipCodigo;
    }

    public void setDipCodigo(String dipCodigo) {
        this.dipCodigo = dipCodigo;
    }

    public String getDipNombre() {
        return dipNombre;
    }

    public void setDipNombre(String dipNombre) {
        this.dipNombre = dipNombre;
    }

    public String getDipDescripcion() {
        return dipDescripcion;
    }

    public void setDipDescripcion(String dipDescripcion) {
        this.dipDescripcion = dipDescripcion;
    }

    public LocalDateTime getDipUltModFecha() {
        return dipUltModFecha;
    }

    public void setDipUltModFecha(LocalDateTime dipUltModFecha) {
        this.dipUltModFecha = dipUltModFecha;
    }

    public String getDipUltModUsuario() {
        return dipUltModUsuario;
    }

    public void setDipUltModUsuario(String dipUltModUsuario) {
        this.dipUltModUsuario = dipUltModUsuario;
    }

    public Integer getDipVersion() {
        return dipVersion;
    }

    public void setDipVersion(Integer dipVersion) {
        this.dipVersion = dipVersion;
    }

    public Boolean getDipHabilitado() {
        return dipHabilitado;
    }

    public void setDipHabilitado(Boolean dipHabilitado) {
        this.dipHabilitado = dipHabilitado;
    }

    public List<SgModuloDiplomado> getDipModulosDiplomado() {
        return dipModulosDiplomado;
    }

    public void setDipModulosDiplomado(List<SgModuloDiplomado> dipModulosDiplomado) {
        this.dipModulosDiplomado = dipModulosDiplomado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dipPk != null ? dipPk.hashCode() : 0);
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
        final SgDiplomado other = (SgDiplomado) obj;
        if (!Objects.equals(this.dipPk, other.dipPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgDiplomado[ dipPk=" + dipPk + " ]";
    }

}
