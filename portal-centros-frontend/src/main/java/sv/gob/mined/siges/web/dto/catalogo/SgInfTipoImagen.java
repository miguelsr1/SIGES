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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tiiPk", scope = SgInfTipoImagen.class)
public class SgInfTipoImagen implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tiiPk;

    private String tiiCodigo;

    private String tiiNombre;

    private Boolean tiiHabilitado;

    private LocalDateTime tiiUltModFecha;

    private String tiiUltModUsuario;

    private Integer tiiVersion;
    
    
    public SgInfTipoImagen() {
        this.tiiHabilitado = Boolean.TRUE;
    }

    public Long getTiiPk() {
        return tiiPk;
    }

    public void setTiiPk(Long tiiPk) {
        this.tiiPk = tiiPk;
    }

    public String getTiiCodigo() {
        return tiiCodigo;
    }

    public void setTiiCodigo(String tiiCodigo) {
        this.tiiCodigo = tiiCodigo;
    }

    public String getTiiNombre() {
        return tiiNombre;
    }

    public void setTiiNombre(String tiiNombre) {
        this.tiiNombre = tiiNombre;
    }

    public LocalDateTime getTiiUltModFecha() {
        return tiiUltModFecha;
    }

    public void setTiiUltModFecha(LocalDateTime tiiUltModFecha) {
        this.tiiUltModFecha = tiiUltModFecha;
    }

    public String getTiiUltModUsuario() {
        return tiiUltModUsuario;
    }

    public void setTiiUltModUsuario(String tiiUltModUsuario) {
        this.tiiUltModUsuario = tiiUltModUsuario;
    }

    public Integer getTiiVersion() {
        return tiiVersion;
    }

    public void setTiiVersion(Integer tiiVersion) {
        this.tiiVersion = tiiVersion;
    }

    
     public Boolean getTiiHabilitado() {
        return tiiHabilitado;
    }

    public void setTiiHabilitado(Boolean tiiHabilitado) {
        this.tiiHabilitado = tiiHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tiiPk != null ? tiiPk.hashCode() : 0);
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
        final SgInfTipoImagen other = (SgInfTipoImagen) obj;
        if (!Objects.equals(this.tiiPk, other.tiiPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgInfTipoImagen[ tiiPk=" + tiiPk + " ]";
    }
    
}
