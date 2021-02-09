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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gloPk", scope = SgGlosario.class)
public class SgGlosario implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long gloPk;

    private String gloCodigo;

    private String gloNombre;
    
    private String gloFuente;

    private Boolean gloHabilitado;

    private LocalDateTime gloUltModFecha;

    private String gloUltModUsuario;

    private Integer gloVersion;
    
    
    public SgGlosario() {
        this.gloHabilitado = Boolean.TRUE;
    }

    public Long getGloPk() {
        return gloPk;
    }

    public void setGloPk(Long gloPk) {
        this.gloPk = gloPk;
    }

    public String getGloCodigo() {
        return gloCodigo;
    }

    public void setGloCodigo(String gloCodigo) {
        this.gloCodigo = gloCodigo;
    }

    public String getGloNombre() {
        return gloNombre;
    }

    public void setGloNombre(String gloNombre) {
        this.gloNombre = gloNombre;
    }

    public LocalDateTime getGloUltModFecha() {
        return gloUltModFecha;
    }

    public void setGloUltModFecha(LocalDateTime gloUltModFecha) {
        this.gloUltModFecha = gloUltModFecha;
    }

    public String getGloUltModUsuario() {
        return gloUltModUsuario;
    }

    public void setGloUltModUsuario(String gloUltModUsuario) {
        this.gloUltModUsuario = gloUltModUsuario;
    }

    public Integer getGloVersion() {
        return gloVersion;
    }

    public void setGloVersion(Integer gloVersion) {
        this.gloVersion = gloVersion;
    }

    
     public Boolean getGloHabilitado() {
        return gloHabilitado;
    }

    public void setGloHabilitado(Boolean gloHabilitado) {
        this.gloHabilitado = gloHabilitado;
    }

    public String getGloFuente() {
        return gloFuente;
    }

    public void setGloFuente(String gloFuente) {
        this.gloFuente = gloFuente;
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gloPk != null ? gloPk.hashCode() : 0);
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
        final SgGlosario other = (SgGlosario) obj;
        if (!Objects.equals(this.gloPk, other.gloPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgGlosario[ gloPk=" + gloPk + " ]";
    }
    
}
