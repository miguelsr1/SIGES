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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ariPk", scope = SgAreaIndicador.class)
public class SgAreaIndicador implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ariPk;

    private String ariCodigo;

    private String ariNombre;

    private Boolean ariHabilitado;

    private LocalDateTime ariUltModFecha;

    private String ariUltModUsuario;

    private Integer ariVersion;
    
    
    public SgAreaIndicador() {
        this.ariHabilitado = Boolean.TRUE;
    }

    public Long getAriPk() {
        return ariPk;
    }

    public void setAriPk(Long ariPk) {
        this.ariPk = ariPk;
    }

    public String getAriCodigo() {
        return ariCodigo;
    }

    public void setAriCodigo(String ariCodigo) {
        this.ariCodigo = ariCodigo;
    }

    public String getAriNombre() {
        return ariNombre;
    }

    public void setAriNombre(String ariNombre) {
        this.ariNombre = ariNombre;
    }

    public LocalDateTime getAriUltModFecha() {
        return ariUltModFecha;
    }

    public void setAriUltModFecha(LocalDateTime ariUltModFecha) {
        this.ariUltModFecha = ariUltModFecha;
    }

    public String getAriUltModUsuario() {
        return ariUltModUsuario;
    }

    public void setAriUltModUsuario(String ariUltModUsuario) {
        this.ariUltModUsuario = ariUltModUsuario;
    }

    public Integer getAriVersion() {
        return ariVersion;
    }

    public void setAriVersion(Integer ariVersion) {
        this.ariVersion = ariVersion;
    }

    
     public Boolean getAriHabilitado() {
        return ariHabilitado;
    }

    public void setAriHabilitado(Boolean ariHabilitado) {
        this.ariHabilitado = ariHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ariPk != null ? ariPk.hashCode() : 0);
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
        final SgAreaIndicador other = (SgAreaIndicador) obj;
        if (!Objects.equals(this.ariPk, other.ariPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgAreaIndicador[ ariPk=" + ariPk + " ]";
    }
    
}
