/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_tipos_organismo_administrativo", schema = Constantes.SCHEMA_CATALOGO)
@Cache(expiry = 150000)
public class SgTipoOrganismoAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "toa_pk", nullable = false)
    private Long toaPk;

    @Size(max = 45)
    @Column(name = "toa_codigo", length = 45)
    private String toaCodigo;

    @Size(max = 255)
    @Column(name = "toa_nombre", length = 255)
    private String toaNombre;

    @Column(name = "toa_habilitado")
    private Boolean toaHabilitado;

    @Column(name = "toa_version")
    @Version
    private Integer toaVersion;


    public SgTipoOrganismoAdministrativo() {
    }

    public SgTipoOrganismoAdministrativo(Long toaPk, Integer toaVersion) {
        this.toaPk = toaPk;
        this.toaVersion = toaVersion;
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

    public Boolean getToaHabilitado() {
        return toaHabilitado;
    }

    public void setToaHabilitado(Boolean toaHabilitado) {
        this.toaHabilitado = toaHabilitado;
    }

    public Integer getToaVersion() {
        return toaVersion;
    }

    public void setToaVersion(Integer toaVersion) {
        this.toaVersion = toaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.toaPk);
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

}
