/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tcePk", scope = SgTipoCalendario.class)
public class SgTipoCalendario implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tcePk;

    private String tceCodigo;

    private String tceNombre;

    private Integer tceVersion;
    
    
    public SgTipoCalendario() {
    }

    public Long getTcePk() {
        return tcePk;
    }

    public void setTcePk(Long tcePk) {
        this.tcePk = tcePk;
    }

    public String getTceCodigo() {
        return tceCodigo;
    }

    public void setTceCodigo(String tceCodigo) {
        this.tceCodigo = tceCodigo;
    }

    public String getTceNombre() {
        return tceNombre;
    }

    public void setTceNombre(String tceNombre) {
        this.tceNombre = tceNombre;
    }

    public Integer getTceVersion() {
        return tceVersion;
    }

    public void setTceVersion(Integer tceVersion) {
        this.tceVersion = tceVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tcePk != null ? tcePk.hashCode() : 0);
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
        final SgTipoCalendario other = (SgTipoCalendario) obj;
        if (!Objects.equals(this.tcePk, other.tcePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoCalendario[ tcePk=" + tcePk + " ]";
    }
    
}
