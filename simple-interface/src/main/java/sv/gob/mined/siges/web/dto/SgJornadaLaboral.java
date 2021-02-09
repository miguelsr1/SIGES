/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "jlaPk", scope = SgJornadaLaboral.class)
public class SgJornadaLaboral implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long jlaPk;

    private String jlaCodigo;

    private String jlaNombre;

    private Integer jlaVersion;
    
    
    public SgJornadaLaboral() {
    }

    public Long getJlaPk() {
        return jlaPk;
    }

    public void setJlaPk(Long jlaPk) {
        this.jlaPk = jlaPk;
    }

    public String getJlaCodigo() {
        return jlaCodigo;
    }

    public void setJlaCodigo(String jlaCodigo) {
        this.jlaCodigo = jlaCodigo;
    }

    public String getJlaNombre() {
        return jlaNombre;
    }

    public void setJlaNombre(String jlaNombre) {
        this.jlaNombre = jlaNombre;
    }

    public Integer getJlaVersion() {
        return jlaVersion;
    }

    public void setJlaVersion(Integer jlaVersion) {
        this.jlaVersion = jlaVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jlaPk != null ? jlaPk.hashCode() : 0);
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
        final SgJornadaLaboral other = (SgJornadaLaboral) obj;
        if (!Objects.equals(this.jlaPk, other.jlaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral{" + "jlaPk=" + jlaPk + '}';
    }
    
}
