/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mcsPk", scope = SgMotivoCierreSede.class)
public class SgMotivoCierreSede implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mcsPk;

    private String mcsCodigo;

    private String mcsNombre;

    private Boolean mcsHabilitado;

    private LocalDateTime mcsUltModFecha;

    private String mcsUltModUsuario;

    private Integer mcsVersion;
    
    
    public SgMotivoCierreSede() {
        this.mcsHabilitado = Boolean.TRUE;
    }

    public Long getMcsPk() {
        return mcsPk;
    }

    public void setMcsPk(Long mcsPk) {
        this.mcsPk = mcsPk;
    }

    public String getMcsCodigo() {
        return mcsCodigo;
    }

    public void setMcsCodigo(String mcsCodigo) {
        this.mcsCodigo = mcsCodigo;
    }

    public String getMcsNombre() {
        return mcsNombre;
    }

    public void setMcsNombre(String mcsNombre) {
        this.mcsNombre = mcsNombre;
    }

    public LocalDateTime getMcsUltModFecha() {
        return mcsUltModFecha;
    }

    public void setMcsUltModFecha(LocalDateTime mcsUltModFecha) {
        this.mcsUltModFecha = mcsUltModFecha;
    }

    public String getMcsUltModUsuario() {
        return mcsUltModUsuario;
    }

    public void setMcsUltModUsuario(String mcsUltModUsuario) {
        this.mcsUltModUsuario = mcsUltModUsuario;
    }

    public Integer getMcsVersion() {
        return mcsVersion;
    }

    public void setMcsVersion(Integer mcsVersion) {
        this.mcsVersion = mcsVersion;
    }

    
     public Boolean getMcsHabilitado() {
        return mcsHabilitado;
    }

    public void setMcsHabilitado(Boolean mcsHabilitado) {
        this.mcsHabilitado = mcsHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mcsPk != null ? mcsPk.hashCode() : 0);
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
        final SgMotivoCierreSede other = (SgMotivoCierreSede) obj;
        if (!Objects.equals(this.mcsPk, other.mcsPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMotivoCierreSede[ mcsPk=" + mcsPk + " ]";
    }
    
}
