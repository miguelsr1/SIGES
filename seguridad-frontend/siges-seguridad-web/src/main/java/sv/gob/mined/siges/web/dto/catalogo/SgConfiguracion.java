/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "conPk", scope = SgConfiguracion.class)
public class SgConfiguracion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long conPk;

    private String conCodigo;

    private String conNombre;

    private Boolean conHabilitado;

    private LocalDateTime conUltModFecha;

    private String conUltModUsuario;

    private Integer conVersion;
    
    
    public SgConfiguracion() {
        this.conHabilitado = Boolean.TRUE;
    }

    public Long getConPk() {
        return conPk;
    }

    public void setConPk(Long conPk) {
        this.conPk = conPk;
    }

    public String getConCodigo() {
        return conCodigo;
    }

    public void setConCodigo(String conCodigo) {
        this.conCodigo = conCodigo;
    }

    public String getConNombre() {
        return conNombre;
    }

    public void setConNombre(String conNombre) {
        this.conNombre = conNombre;
    }

    public LocalDateTime getConUltModFecha() {
        return conUltModFecha;
    }

    public void setConUltModFecha(LocalDateTime conUltModFecha) {
        this.conUltModFecha = conUltModFecha;
    }

    public String getConUltModUsuario() {
        return conUltModUsuario;
    }

    public void setConUltModUsuario(String conUltModUsuario) {
        this.conUltModUsuario = conUltModUsuario;
    }

    public Integer getConVersion() {
        return conVersion;
    }

    public void setConVersion(Integer conVersion) {
        this.conVersion = conVersion;
    }

    
     public Boolean getConHabilitado() {
        return conHabilitado;
    }

    public void setConHabilitado(Boolean conHabilitado) {
        this.conHabilitado = conHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (conPk != null ? conPk.hashCode() : 0);
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
        final SgConfiguracion other = (SgConfiguracion) obj;
        if (!Objects.equals(this.conPk, other.conPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgConfiguracion[ conPk=" + conPk + " ]";
    }
    
}
