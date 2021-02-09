/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "toaPk", scope = SgTipoOrganismoAdministrativo.class)
public class SgTipoOrganismoAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long toaPk;

    private String toaCodigo;

    private String toaNombre;

    private Boolean toaHabilitado;

    private LocalDateTime toaUltModFecha;

    private String toaUltModUsuario;

    private Integer toaVersion;

    public SgTipoOrganismoAdministrativo() {
        this.toaHabilitado = Boolean.TRUE;
    }

    public Long getToaPk() {
        return toaPk;
    }

    public void setToaPk(Long toaPk) {
        this.toaPk = toaPk;
    }

    public String getToaCodigo() {
        return toaCodigo;
    }

    public void setToaCodigo(String toaCodigo) {
        this.toaCodigo = toaCodigo;
    }

    public String getToaNombre() {
        return toaNombre;
    }

    public void setToaNombre(String toaNombre) {
        this.toaNombre = toaNombre;
    }

    public LocalDateTime getToaUltModFecha() {
        return toaUltModFecha;
    }

    public void setToaUltModFecha(LocalDateTime toaUltModFecha) {
        this.toaUltModFecha = toaUltModFecha;
    }

    public String getToaUltModUsuario() {
        return toaUltModUsuario;
    }

    public void setToaUltModUsuario(String toaUltModUsuario) {
        this.toaUltModUsuario = toaUltModUsuario;
    }

    public Integer getToaVersion() {
        return toaVersion;
    }

    public void setToaVersion(Integer toaVersion) {
        this.toaVersion = toaVersion;
    }

    public Boolean getToaHabilitado() {
        return toaHabilitado;
    }

    public void setToaHabilitado(Boolean toaHabilitado) {
        this.toaHabilitado = toaHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (toaPk != null ? toaPk.hashCode() : 0);
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
        final SgTipoOrganismoAdministrativo other = (SgTipoOrganismoAdministrativo) obj;
        if (!Objects.equals(this.toaPk, other.toaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoOrganismoAdministrativo[ toaPk=" + toaPk + " ]";
    }

}
