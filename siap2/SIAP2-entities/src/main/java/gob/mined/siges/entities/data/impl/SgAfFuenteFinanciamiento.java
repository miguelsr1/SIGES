/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_fuente_financiamiento", schema = Constantes.SCHEMA_CATALOGO)
@Cacheable(false)
public class SgAfFuenteFinanciamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ffi_pk")
    private Long ffiPk;

    @Size(max = 20)
    @Column(name = "ffi_codigo", length = 20)
    private String ffiCodigo;
    
    @Column(name = "ffi_version")
    @Version
    private Integer ffiVersion;
    
    public SgAfFuenteFinanciamiento() {
    }

    public Long getFfiPk() {
        return ffiPk;
    }

    public void setFfiPk(Long ffiPk) {
        this.ffiPk = ffiPk;
    }

    public String getFfiCodigo() {
        return ffiCodigo;
    }

    public void setFfiCodigo(String ffiCodigo) {
        this.ffiCodigo = ffiCodigo;
    }

    public Integer getFfiVersion() {
        return ffiVersion;
    }

    public void setFfiVersion(Integer ffiVersion) {
        this.ffiVersion = ffiVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ffiPk != null ? ffiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfFuenteFinanciamiento)) {
            return false;
        }
        SgAfFuenteFinanciamiento other = (SgAfFuenteFinanciamiento) object;
        if ((this.ffiPk == null && other.ffiPk != null) || (this.ffiPk != null && !this.ffiPk.equals(other.ffiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfFuenteFinanciamiento[ ffiPk=" + ffiPk + " ]";
    }
    
}