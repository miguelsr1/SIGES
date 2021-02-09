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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "depPk", scope = SgDepartamento.class)
public class SgDepartamento implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long depPk;

    private String depCodigo;

    private String depNombre;

    private Boolean depHabilitado;

    private LocalDateTime depUltModFecha;

    private String depUltModUsuario;

    private Integer depVersion;
    
    
    public SgDepartamento() {
        this.depHabilitado = Boolean.TRUE;
    }

    public Long getDepPk() {
        return depPk;
    }

    public void setDepPk(Long depPk) {
        this.depPk = depPk;
    }

    public String getDepCodigo() {
        return depCodigo;
    }

    public void setDepCodigo(String depCodigo) {
        this.depCodigo = depCodigo;
    }

    public String getDepNombre() {
        return depNombre;
    }

    public void setDepNombre(String depNombre) {
        this.depNombre = depNombre;
    }

    public LocalDateTime getDepUltModFecha() {
        return depUltModFecha;
    }

    public void setDepUltModFecha(LocalDateTime depUltModFecha) {
        this.depUltModFecha = depUltModFecha;
    }

    public String getDepUltModUsuario() {
        return depUltModUsuario;
    }

    public void setDepUltModUsuario(String depUltModUsuario) {
        this.depUltModUsuario = depUltModUsuario;
    }

    public Integer getDepVersion() {
        return depVersion;
    }

    public void setDepVersion(Integer depVersion) {
        this.depVersion = depVersion;
    }

    
     public Boolean getDepHabilitado() {
        return depHabilitado;
    }

    public void setDepHabilitado(Boolean depHabilitado) {
        this.depHabilitado = depHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (depPk != null ? depPk.hashCode() : 0);
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
        final SgDepartamento other = (SgDepartamento) obj;
        if (!Objects.equals(this.depPk, other.depPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgDepartamento[ depPk=" + depPk + " ]";
    }
    
}
