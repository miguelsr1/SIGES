/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "munPk", scope = SgMunicipio.class)
public class SgMunicipio implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long munPk;

    private String munCodigo;

    private String munNombre;

    private Integer munVersion;

    public SgMunicipio() {
    }

    public Long getMunPk() {
        return munPk;
    }

    public void setMunPk(Long munPk) {
        this.munPk = munPk;
    }

    public String getMunCodigo() {
        return munCodigo;
    }

    public void setMunCodigo(String munCodigo) {
        this.munCodigo = munCodigo;
    }

    public String getMunNombre() {
        return munNombre;
    }

    public void setMunNombre(String munNombre) {
        this.munNombre = munNombre;
    }

    public Integer getMunVersion() {
        return munVersion;
    }

    public void setMunVersion(Integer munVersion) {
        this.munVersion = munVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (munPk != null ? munPk.hashCode() : 0);
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
        final SgMunicipio other = (SgMunicipio) obj;
        if (!Objects.equals(this.munPk, other.munPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgMunicipio[ munPk=" + munPk + " ]";
    }

}
