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
@Table(name = "sg_af_estados_bienes", schema = Constantes.SCHEMA_CATALOGO)
@Cacheable(false)
public class SgAfEstadosBienes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ebi_pk")
    private Long ebiPk;

    @Size(max = 4)
    @Column(name = "ebi_codigo", length = 4)
    private String ebiCodigo;

    @Column(name = "ebi_version")
    @Version
    private Integer ebiVersion;

    public SgAfEstadosBienes() {
    }

    public Long getEbiPk() {
        return ebiPk;
    }

    public void setEbiPk(Long ebiPk) {
        this.ebiPk = ebiPk;
    }

    public String getEbiCodigo() {
        return ebiCodigo;
    }

    public void setEbiCodigo(String ebiCodigo) {
        this.ebiCodigo = ebiCodigo;
    }

    public Integer getEbiVersion() {
        return ebiVersion;
    }

    public void setEbiVersion(Integer ebiVersion) {
        this.ebiVersion = ebiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.ebiPk);
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
        final SgAfEstadosBienes other = (SgAfEstadosBienes) obj;
        if (!Objects.equals(this.ebiPk, other.ebiPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstadosBienes[ ebiPk=" + ebiPk + " ]";
    }

}

