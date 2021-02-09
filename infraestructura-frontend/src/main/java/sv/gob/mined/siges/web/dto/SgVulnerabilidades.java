/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "vulPk", scope = SgVulnerabilidades.class)
public class SgVulnerabilidades implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long vulPk;
    private String vulCodigo;
    private String vulNombre;
    private String vulNombreBusqueda;
    private Boolean vulHabilitado;
    private LocalDateTime vulUltModFecha;
    private String vulUltModUsuario;
    private Integer vulVersion;

    public SgVulnerabilidades() {
        this.vulHabilitado = Boolean.TRUE;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.vulNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.vulNombre);
    }

    public Long getVulPk() {
        return vulPk;
    }

    public void setVulPk(Long vulPk) {
        this.vulPk = vulPk;
    }

    public String getVulCodigo() {
        return vulCodigo;
    }

    public void setVulCodigo(String vulCodigo) {
        this.vulCodigo = vulCodigo;
    }

    public String getVulNombre() {
        return vulNombre;
    }

    public void setVulNombre(String vulNombre) {
        this.vulNombre = vulNombre;
    }

    public String getVulNombreBusqueda() {
        return vulNombreBusqueda;
    }

    public void setVulNombreBusqueda(String vulNombreBusqueda) {
        this.vulNombreBusqueda = vulNombreBusqueda;
    }

    public Boolean getVulHabilitado() {
        return vulHabilitado;
    }

    public void setVulHabilitado(Boolean vulHabilitado) {
        this.vulHabilitado = vulHabilitado;
    }

    public LocalDateTime getVulUltModFecha() {
        return vulUltModFecha;
    }

    public void setVulUltModFecha(LocalDateTime vulUltModFecha) {
        this.vulUltModFecha = vulUltModFecha;
    }

    public String getVulUltModUsuario() {
        return vulUltModUsuario;
    }

    public void setVulUltModUsuario(String vulUltModUsuario) {
        this.vulUltModUsuario = vulUltModUsuario;
    }

    public Integer getVulVersion() {
        return vulVersion;
    }

    public void setVulVersion(Integer vulVersion) {
        this.vulVersion = vulVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.vulPk);
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
        final SgVulnerabilidades other = (SgVulnerabilidades) obj;
        if (!Objects.equals(this.vulPk, other.vulPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgVulnerabilidades[ vulPk=" + vulPk + " ]";
    }

}