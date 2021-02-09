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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rolPk", scope = SgRol.class)
public class SgRol implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
   private Long rolPk;

    private String rolCodigo;

    private String rolNombre;

    private Boolean rolHabilitado;

    private LocalDateTime rolUltModFecha;

    private String rolUltModUsuario;

    private Integer rolVersion;
    
    private Boolean rolDelegable;
    
    
    public SgRol() {
        this.rolHabilitado = Boolean.TRUE;
        rolDelegable=Boolean.FALSE;
    }

    public Long getRolPk() {
        return rolPk;
    }

    public void setRolPk(Long rolPk) {
        this.rolPk = rolPk;
    }

    public String getRolCodigo() {
        return rolCodigo;
    }

    public void setRolCodigo(String rolCodigo) {
        this.rolCodigo = rolCodigo;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public LocalDateTime getRolUltModFecha() {
        return rolUltModFecha;
    }

    public void setRolUltModFecha(LocalDateTime rolUltModFecha) {
        this.rolUltModFecha = rolUltModFecha;
    }

    public String getRolUltModUsuario() {
        return rolUltModUsuario;
    }

    public void setRolUltModUsuario(String rolUltModUsuario) {
        this.rolUltModUsuario = rolUltModUsuario;
    }

    public Integer getRolVersion() {
        return rolVersion;
    }

    public void setRolVersion(Integer rolVersion) {
        this.rolVersion = rolVersion;
    }

    public Boolean getRolHabilitado() {
        return rolHabilitado;
    }

    public void setRolHabilitado(Boolean rolHabilitado) {
        this.rolHabilitado = rolHabilitado;
    }

    public Boolean getRolDelegable() {
        return rolDelegable;
    }

    public void setRolDelegable(Boolean rolDelegable) {
        this.rolDelegable = rolDelegable;
    }
    
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolPk != null ? rolPk.hashCode() : 0);
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
        final SgRol other = (SgRol) obj;
        if (!Objects.equals(this.rolPk, other.rolPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRol[ rolPk=" + rolPk + " ]";
    }
    
}
