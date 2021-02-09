/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.utils.SofisStringUtils;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idiPk", scope = SgIdioma.class)
public class SgIdioma implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long idiPk;
    
    private String idiCodigo;
    
    private Boolean idiHabilitado;
    
    private String idiNombre;
    
    private String idiNombreBusqueda;
    
    private LocalDateTime idiUltModFecha;
    
    private String idiUltModUsuario;
    
    private Integer idiVersion;

    public SgIdioma() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.idiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.idiNombre);
    }

    public SgIdioma(Long idiPk) {
        this.idiPk = idiPk;
    }

    public Long getIdiPk() {
        return idiPk;
    }

    public void setIdiPk(Long idiPk) {
        this.idiPk = idiPk;
    }

    public String getIdiCodigo() {
        return idiCodigo;
    }

    public void setIdiCodigo(String idiCodigo) {
        this.idiCodigo = idiCodigo;
    }

    public Boolean getIdiHabilitado() {
        return idiHabilitado;
    }

    public void setIdiHabilitado(Boolean idiHabilitado) {
        this.idiHabilitado = idiHabilitado;
    }

    public String getIdiNombre() {
        return idiNombre;
    }

    public void setIdiNombre(String idiNombre) {
        this.idiNombre = idiNombre;
    }

    public String getIdiNombreBusqueda() {
        return idiNombreBusqueda;
    }

    public void setIdiNombreBusqueda(String idiNombreBusqueda) {
        this.idiNombreBusqueda = idiNombreBusqueda;
    }

    public LocalDateTime getIdiUltModFecha() {
        return idiUltModFecha;
    }

    public void setIdiUltModFecha(LocalDateTime idiUltModFecha) {
        this.idiUltModFecha = idiUltModFecha;
    }

    public String getIdiUltModUsuario() {
        return idiUltModUsuario;
    }

    public void setIdiUltModUsuario(String idiUltModUsuario) {
        this.idiUltModUsuario = idiUltModUsuario;
    }

    public Integer getIdiVersion() {
        return idiVersion;
    }

    public void setIdiVersion(Integer idiVersion) {
        this.idiVersion = idiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idiPk != null ? idiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgIdioma)) {
            return false;
        }
        SgIdioma other = (SgIdioma) object;
        if ((this.idiPk == null && other.idiPk != null) || (this.idiPk != null && !this.idiPk.equals(other.idiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgIdioma[ idiPk=" + idiPk + " ]";
    }
    
}
