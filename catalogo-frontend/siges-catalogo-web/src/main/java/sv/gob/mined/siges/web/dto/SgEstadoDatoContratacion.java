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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "edcPk", scope = SgEstadoDatoContratacion.class)
public class SgEstadoDatoContratacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long edcPk;

    private String edcCodigo;

    private String edcNombre;

    private Boolean edcHabilitado;

    private LocalDateTime edcUltModFecha;

    private String edcUltModUsuario;

    private Integer edcVersion;
    
    
    public SgEstadoDatoContratacion() {
        this.edcHabilitado = Boolean.TRUE;
    }

    public Long getEdcPk() {
        return edcPk;
    }

    public void setEdcPk(Long edcPk) {
        this.edcPk = edcPk;
    }

    public String getEdcCodigo() {
        return edcCodigo;
    }

    public void setEdcCodigo(String edcCodigo) {
        this.edcCodigo = edcCodigo;
    }

    public String getEdcNombre() {
        return edcNombre;
    }

    public void setEdcNombre(String edcNombre) {
        this.edcNombre = edcNombre;
    }

    public LocalDateTime getEdcUltModFecha() {
        return edcUltModFecha;
    }

    public void setEdcUltModFecha(LocalDateTime edcUltModFecha) {
        this.edcUltModFecha = edcUltModFecha;
    }

    public String getEdcUltModUsuario() {
        return edcUltModUsuario;
    }

    public void setEdcUltModUsuario(String edcUltModUsuario) {
        this.edcUltModUsuario = edcUltModUsuario;
    }

    public Integer getEdcVersion() {
        return edcVersion;
    }

    public void setEdcVersion(Integer edcVersion) {
        this.edcVersion = edcVersion;
    }

    
     public Boolean getEdcHabilitado() {
        return edcHabilitado;
    }

    public void setEdcHabilitado(Boolean edcHabilitado) {
        this.edcHabilitado = edcHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (edcPk != null ? edcPk.hashCode() : 0);
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
        final SgEstadoDatoContratacion other = (SgEstadoDatoContratacion) obj;
        if (!Objects.equals(this.edcPk, other.edcPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEstadoDatoContratacion[ edcPk=" + edcPk + " ]";
    }
    
}
