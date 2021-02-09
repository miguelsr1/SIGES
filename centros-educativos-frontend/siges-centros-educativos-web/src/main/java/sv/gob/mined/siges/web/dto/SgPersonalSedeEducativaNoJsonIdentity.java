/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */

public class SgPersonalSedeEducativaNoJsonIdentity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long psePk;
    
    private SgPersonaLite psePersona;
    
    private Integer pseVersion;
    
    
    public SgPersonalSedeEducativaNoJsonIdentity(){
    }

    public SgPersonalSedeEducativaNoJsonIdentity(Long psePk, Integer pseVersion) {
        this.psePk = psePk;
        this.pseVersion = pseVersion;
    }
    

    @JsonIgnore
    public String getPseNombreCompleto() {
        return this.psePersona.getPerNombreCompleto();
    }
    
    @JsonIgnore
    public String getPseDuiNombreCompleto() {
        return this.psePersona.getPerDuiNombreCompleto();
    }
    
    public Long getPsePk() {
        return psePk;
    }

    public void setPsePk(Long psePk) {
        this.psePk = psePk;
    }

    public Integer getPseVersion() {
        return pseVersion;
    }

    public void setPseVersion(Integer pseVersion) {
        this.pseVersion = pseVersion;
    }

    public SgPersonaLite getPsePersona() {
        return psePersona;
    }

    public void setPsePersona(SgPersonaLite psePersona) {
        this.psePersona = psePersona;
    }
    
    //Debe ser igual al hashCode de SgPersonalSedeEducativa
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (psePk != null ? psePk.hashCode() : 0);
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
        final SgPersonalSedeEducativaNoJsonIdentity other = (SgPersonalSedeEducativaNoJsonIdentity) obj;
        if (!Objects.equals(this.psePk, other.psePk)) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
