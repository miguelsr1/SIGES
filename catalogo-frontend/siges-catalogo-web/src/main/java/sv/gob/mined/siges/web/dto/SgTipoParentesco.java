/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tpaPk", scope = SgTipoParentesco.class)
public class SgTipoParentesco implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tpaPk;

    private String tpaCodigo;

    private String tpaNombre;

    private Boolean tpaHabilitado;

    private LocalDateTime tpaUltModFecha;

    private String tpaUltModUsuario;

    private Integer tpaVersion;
    
    
    public SgTipoParentesco() {
        this.tpaHabilitado = Boolean.TRUE;
    }

    public Long getTpaPk() {
        return tpaPk;
    }

    public void setTpaPk(Long tpaPk) {
        this.tpaPk = tpaPk;
    }

    public String getTpaCodigo() {
        return tpaCodigo;
    }

    public void setTpaCodigo(String tpaCodigo) {
        this.tpaCodigo = tpaCodigo;
    }

    public String getTpaNombre() {
        return tpaNombre;
    }

    public void setTpaNombre(String tpaNombre) {
        this.tpaNombre = tpaNombre;
    }

    public LocalDateTime getTpaUltModFecha() {
        return tpaUltModFecha;
    }

    public void setTpaUltModFecha(LocalDateTime tpaUltModFecha) {
        this.tpaUltModFecha = tpaUltModFecha;
    }

    public String getTpaUltModUsuario() {
        return tpaUltModUsuario;
    }

    public void setTpaUltModUsuario(String tpaUltModUsuario) {
        this.tpaUltModUsuario = tpaUltModUsuario;
    }

    public Integer getTpaVersion() {
        return tpaVersion;
    }

    public void setTpaVersion(Integer tpaVersion) {
        this.tpaVersion = tpaVersion;
    }

    
     public Boolean getTpaHabilitado() {
        return tpaHabilitado;
    }

    public void setTpaHabilitado(Boolean tpaHabilitado) {
        this.tpaHabilitado = tpaHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tpaPk != null ? tpaPk.hashCode() : 0);
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
        final SgTipoParentesco other = (SgTipoParentesco) obj;
        if (!Objects.equals(this.tpaPk, other.tpaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoParentesco[ tpaPk=" + tpaPk + " ]";
    }
    
}
