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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "trePk", scope = SgTipoRepresentante.class)
public class SgTipoRepresentante implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long trePk;

    private String treCodigo;

    private String treNombre;

    private Boolean treHabilitado;

    private LocalDateTime treUltModFecha;

    private String treUltModUsuario;

    private Integer treVersion;
    
    
    public SgTipoRepresentante() {
        this.treHabilitado = Boolean.TRUE;
    }

    public Long getTrePk() {
        return trePk;
    }

    public void setTrePk(Long trePk) {
        this.trePk = trePk;
    }

    public String getTreCodigo() {
        return treCodigo;
    }

    public void setTreCodigo(String treCodigo) {
        this.treCodigo = treCodigo;
    }

    public String getTreNombre() {
        return treNombre;
    }

    public void setTreNombre(String treNombre) {
        this.treNombre = treNombre;
    }

    public LocalDateTime getTreUltModFecha() {
        return treUltModFecha;
    }

    public void setTreUltModFecha(LocalDateTime treUltModFecha) {
        this.treUltModFecha = treUltModFecha;
    }

    public String getTreUltModUsuario() {
        return treUltModUsuario;
    }

    public void setTreUltModUsuario(String treUltModUsuario) {
        this.treUltModUsuario = treUltModUsuario;
    }

    public Integer getTreVersion() {
        return treVersion;
    }

    public void setTreVersion(Integer treVersion) {
        this.treVersion = treVersion;
    }

    
     public Boolean getTreHabilitado() {
        return treHabilitado;
    }

    public void setTreHabilitado(Boolean treHabilitado) {
        this.treHabilitado = treHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (trePk != null ? trePk.hashCode() : 0);
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
        final SgTipoRepresentante other = (SgTipoRepresentante) obj;
        if (!Objects.equals(this.trePk, other.trePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoRepresentante[ trePk=" + trePk + " ]";
    }
    
}
