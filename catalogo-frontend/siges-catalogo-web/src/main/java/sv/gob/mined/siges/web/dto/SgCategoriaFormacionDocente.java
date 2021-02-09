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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cfdPk", scope = SgCategoriaFormacionDocente.class)
public class SgCategoriaFormacionDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long cfdPk;
    
    private String cfdCodigo;
    
    private Boolean cfdHabilitado;
    
    private String cfdNombre;
    
    private String cfdNombreBusqueda;
    
    private LocalDateTime cfdUltModFecha;
    
    private String cfdUltModUsuario;
    
    private Integer cfdVersion;

    public SgCategoriaFormacionDocente() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cfdNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cfdNombre);
    }

    public Long getCfdPk() {
        return cfdPk;
    }

    public void setCfdPk(Long cfdPk) {
        this.cfdPk = cfdPk;
    }

    public String getCfdCodigo() {
        return cfdCodigo;
    }

    public void setCfdCodigo(String cfdCodigo) {
        this.cfdCodigo = cfdCodigo;
    }

    public Boolean getCfdHabilitado() {
        return cfdHabilitado;
    }

    public void setCfdHabilitado(Boolean cfdHabilitado) {
        this.cfdHabilitado = cfdHabilitado;
    }

    public String getCfdNombre() {
        return cfdNombre;
    }

    public void setCfdNombre(String cfdNombre) {
        this.cfdNombre = cfdNombre;
    }

    public String getCfdNombreBusqueda() {
        return cfdNombreBusqueda;
    }

    public void setCfdNombreBusqueda(String cfdNombreBusqueda) {
        this.cfdNombreBusqueda = cfdNombreBusqueda;
    }

    public LocalDateTime getCfdUltModFecha() {
        return cfdUltModFecha;
    }

    public void setCfdUltModFecha(LocalDateTime cfdUltModFecha) {
        this.cfdUltModFecha = cfdUltModFecha;
    }

    public String getCfdUltModUsuario() {
        return cfdUltModUsuario;
    }

    public void setCfdUltModUsuario(String cfdUltModUsuario) {
        this.cfdUltModUsuario = cfdUltModUsuario;
    }

    public Integer getCfdVersion() {
        return cfdVersion;
    }

    public void setCfdVersion(Integer cfdVersion) {
        this.cfdVersion = cfdVersion;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cfdPk != null ? cfdPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCategoriaFormacionDocente)) {
            return false;
        }
        SgCategoriaFormacionDocente other = (SgCategoriaFormacionDocente) object;
        if ((this.cfdPk == null && other.cfdPk != null) || (this.cfdPk != null && !this.cfdPk.equals(other.cfdPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCategoriaFormacionDocente[ cfdPk=" + cfdPk + " ]";
    }
    
}
