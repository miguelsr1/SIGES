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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mfdPk", scope = SgModuloFormacionDocente.class)
public class SgModuloFormacionDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long mfdPk;
    
    private String mfdCodigo;
    
    private Boolean mfdHabilitado;
    
    private String mfdNombre;
    
    private String mfdNombreBusqueda;
    
    private LocalDateTime mfdUltModFecha;
    
    private String mfdUltModUsuario;
    
    private Integer mfdVersion;

    public SgModuloFormacionDocente() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mfdNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mfdNombre);
    }

    public SgModuloFormacionDocente(Long mfdPk) {
        this.mfdPk = mfdPk;
    }

    public Long getMfdPk() {
        return mfdPk;
    }

    public void setMfdPk(Long mfdPk) {
        this.mfdPk = mfdPk;
    }

    public String getMfdCodigo() {
        return mfdCodigo;
    }

    public void setMfdCodigo(String mfdCodigo) {
        this.mfdCodigo = mfdCodigo;
    }

    public Boolean getMfdHabilitado() {
        return mfdHabilitado;
    }

    public void setMfdHabilitado(Boolean mfdHabilitado) {
        this.mfdHabilitado = mfdHabilitado;
    }

    public String getMfdNombre() {
        return mfdNombre;
    }

    public void setMfdNombre(String mfdNombre) {
        this.mfdNombre = mfdNombre;
    }

    public String getMfdNombreBusqueda() {
        return mfdNombreBusqueda;
    }

    public void setMfdNombreBusqueda(String mfdNombreBusqueda) {
        this.mfdNombreBusqueda = mfdNombreBusqueda;
    }

    public LocalDateTime getMfdUltModFecha() {
        return mfdUltModFecha;
    }

    public void setMfdUltModFecha(LocalDateTime mfdUltModFecha) {
        this.mfdUltModFecha = mfdUltModFecha;
    }

    public String getMfdUltModUsuario() {
        return mfdUltModUsuario;
    }

    public void setMfdUltModUsuario(String mfdUltModUsuario) {
        this.mfdUltModUsuario = mfdUltModUsuario;
    }

    public Integer getMfdVersion() {
        return mfdVersion;
    }

    public void setMfdVersion(Integer mfdVersion) {
        this.mfdVersion = mfdVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mfdPk != null ? mfdPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgModuloFormacionDocente)) {
            return false;
        }
        SgModuloFormacionDocente other = (SgModuloFormacionDocente) object;
        if ((this.mfdPk == null && other.mfdPk != null) || (this.mfdPk != null && !this.mfdPk.equals(other.mfdPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgModuloFormacionDocente[ mfdPk=" + mfdPk + " ]";
    }
    
}
