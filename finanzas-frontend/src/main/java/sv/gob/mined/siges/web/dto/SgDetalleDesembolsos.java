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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dedPk", scope = SgDetalleDesembolsos.class)
public class SgDetalleDesembolsos implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long dedPk;

    private String dedCodigo;

    private String dedNombre;

    private Boolean dedHabilitado;

    private LocalDateTime dedUltModFecha;

    private String dedUltModUsuario;

    private Integer dedVersion;
    
    
    public SgDetalleDesembolsos() {
        this.dedHabilitado = Boolean.TRUE;
    }

    public Long getDedPk() {
        return dedPk;
    }

    public void setDedPk(Long dedPk) {
        this.dedPk = dedPk;
    }

    public String getDedCodigo() {
        return dedCodigo;
    }

    public void setDedCodigo(String dedCodigo) {
        this.dedCodigo = dedCodigo;
    }

    public String getDedNombre() {
        return dedNombre;
    }

    public void setDedNombre(String dedNombre) {
        this.dedNombre = dedNombre;
    }

    public LocalDateTime getDedUltModFecha() {
        return dedUltModFecha;
    }

    public void setDedUltModFecha(LocalDateTime dedUltModFecha) {
        this.dedUltModFecha = dedUltModFecha;
    }

    public String getDedUltModUsuario() {
        return dedUltModUsuario;
    }

    public void setDedUltModUsuario(String dedUltModUsuario) {
        this.dedUltModUsuario = dedUltModUsuario;
    }

    public Integer getDedVersion() {
        return dedVersion;
    }

    public void setDedVersion(Integer dedVersion) {
        this.dedVersion = dedVersion;
    }

    
     public Boolean getDedHabilitado() {
        return dedHabilitado;
    }

    public void setDedHabilitado(Boolean dedHabilitado) {
        this.dedHabilitado = dedHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dedPk != null ? dedPk.hashCode() : 0);
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
        final SgDetalleDesembolsos other = (SgDetalleDesembolsos) obj;
        if (!Objects.equals(this.dedPk, other.dedPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgDetalleDesembolsos[ dedPk=" + dedPk + " ]";
    }
    
}
