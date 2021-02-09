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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "canPk", scope = SgCanton.class)
public class SgCanton implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long canPk;

    private String canCodigo;

    private String canNombre;

    private Integer canVersion;

    public SgCanton() {
    }

    public Long getCanPk() {
        return canPk;
    }

    public void setCanPk(Long canPk) {
        this.canPk = canPk;
    }

    public String getCanCodigo() {
        return canCodigo;
    }

    public void setCanCodigo(String canCodigo) {
        this.canCodigo = canCodigo;
    }

    public String getCanNombre() {
        return canNombre;
    }

    public void setCanNombre(String canNombre) {
        this.canNombre = canNombre;
    }

    public Integer getCanVersion() {
        return canVersion;
    }

    public void setCanVersion(Integer canVersion) {
        this.canVersion = canVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (canPk != null ? canPk.hashCode() : 0);
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
        final SgCanton other = (SgCanton) obj;
        if (!Objects.equals(this.canPk, other.canPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgCanton[ canPk=" + canPk + " ]";
    }

}
