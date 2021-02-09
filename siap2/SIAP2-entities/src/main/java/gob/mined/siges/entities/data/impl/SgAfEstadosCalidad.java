/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import java.io.Serializable;
import java.util.Objects;
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
@Table(name = "sg_af_estados_calidad", schema = Constantes.SCHEMA_CATALOGO)
@Cacheable(false)
public class SgAfEstadosCalidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "eca_pk")
    private Long ecaPk;

    @Size(max = 4)
    @Column(name = "eca_codigo", length = 4)
    private String ecaCodigo;

    @Column(name = "eca_version")
    @Version
    private Integer ecaVersion;

    public SgAfEstadosCalidad() {
    }

    public Long getEcaPk() {
        return ecaPk;
    }

    public void setEcaPk(Long ecaPk) {
        this.ecaPk = ecaPk;
    }

    public String getEcaCodigo() {
        return ecaCodigo;
    }

    public void setEcaCodigo(String ecaCodigo) {
        this.ecaCodigo = ecaCodigo;
    }
    
    public Integer getEcaVersion() {
        return ecaVersion;
    }

    public void setEcaVersion(Integer ecaVersion) {
        this.ecaVersion = ecaVersion;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstadosCalidad[ ecaPk=" + ecaPk + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.ecaPk);
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
        final SgAfEstadosCalidad other = (SgAfEstadosCalidad) obj;
        if (!Objects.equals(this.ecaPk, other.ecaPk)) {
            return false;
        }
        return true;
    }

}
