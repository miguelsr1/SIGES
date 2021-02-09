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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ttoPk", scope = SgTipoTelefono.class)
public class SgTipoTelefono implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ttoPk;

    private String ttoCodigo;

    private String ttoNombre;

    private Boolean ttoHabilitado;

    private LocalDateTime ttoUltModFecha;

    private String ttoUltModUsuario;

    private Integer ttoVersion;
    
    
    public SgTipoTelefono() {
        this.ttoHabilitado = Boolean.TRUE;
    }

    public Long getTtoPk() {
        return ttoPk;
    }

    public void setTtoPk(Long ttoPk) {
        this.ttoPk = ttoPk;
    }

    public String getTtoCodigo() {
        return ttoCodigo;
    }

    public void setTtoCodigo(String ttoCodigo) {
        this.ttoCodigo = ttoCodigo;
    }

    public String getTtoNombre() {
        return ttoNombre;
    }

    public void setTtoNombre(String ttoNombre) {
        this.ttoNombre = ttoNombre;
    }

    public LocalDateTime getTtoUltModFecha() {
        return ttoUltModFecha;
    }

    public void setTtoUltModFecha(LocalDateTime ttoUltModFecha) {
        this.ttoUltModFecha = ttoUltModFecha;
    }

    public String getTtoUltModUsuario() {
        return ttoUltModUsuario;
    }

    public void setTtoUltModUsuario(String ttoUltModUsuario) {
        this.ttoUltModUsuario = ttoUltModUsuario;
    }

    public Integer getTtoVersion() {
        return ttoVersion;
    }

    public void setTtoVersion(Integer ttoVersion) {
        this.ttoVersion = ttoVersion;
    }

    
     public Boolean getTtoHabilitado() {
        return ttoHabilitado;
    }

    public void setTtoHabilitado(Boolean ttoHabilitado) {
        this.ttoHabilitado = ttoHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ttoPk != null ? ttoPk.hashCode() : 0);
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
        final SgTipoTelefono other = (SgTipoTelefono) obj;
        if (!Objects.equals(this.ttoPk, other.ttoPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoTelefono[ ttoPk=" + ttoPk + " ]";
    }
    
}
