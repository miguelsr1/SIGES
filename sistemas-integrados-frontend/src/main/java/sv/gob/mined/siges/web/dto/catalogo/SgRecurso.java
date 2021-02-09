/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rcsPk", scope = SgRecurso.class)
public class SgRecurso implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long rcsPk;

    private String rcsCodigo;

    private String rcsNombre;

    private Boolean rcsHabilitado;

    private LocalDateTime rcsUltModFecha;

    private String rcsUltModUsuario;

    private Integer rcsVersion;
    
    
    public SgRecurso() {
        this.rcsHabilitado = Boolean.TRUE;
    }

    public Long getRcsPk() {
        return rcsPk;
    }

    public void setRcsPk(Long rcsPk) {
        this.rcsPk = rcsPk;
    }

    public String getRcsCodigo() {
        return rcsCodigo;
    }

    public void setRcsCodigo(String rcsCodigo) {
        this.rcsCodigo = rcsCodigo;
    }

    public String getRcsNombre() {
        return rcsNombre;
    }

    public void setRcsNombre(String rcsNombre) {
        this.rcsNombre = rcsNombre;
    }

    public LocalDateTime getRcsUltModFecha() {
        return rcsUltModFecha;
    }

    public void setRcsUltModFecha(LocalDateTime rcsUltModFecha) {
        this.rcsUltModFecha = rcsUltModFecha;
    }

    public String getRcsUltModUsuario() {
        return rcsUltModUsuario;
    }

    public void setRcsUltModUsuario(String rcsUltModUsuario) {
        this.rcsUltModUsuario = rcsUltModUsuario;
    }

    public Integer getRcsVersion() {
        return rcsVersion;
    }

    public void setRcsVersion(Integer rcsVersion) {
        this.rcsVersion = rcsVersion;
    }

    
     public Boolean getRcsHabilitado() {
        return rcsHabilitado;
    }

    public void setRcsHabilitado(Boolean rcsHabilitado) {
        this.rcsHabilitado = rcsHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rcsPk != null ? rcsPk.hashCode() : 0);
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
        final SgRecurso other = (SgRecurso) obj;
        if (!Objects.equals(this.rcsPk, other.rcsPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRecursos[ rcsPk=" + rcsPk + " ]";
    }
    
}
