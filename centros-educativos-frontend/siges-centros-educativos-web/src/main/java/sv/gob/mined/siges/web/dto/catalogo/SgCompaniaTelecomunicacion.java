/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ctePk", scope = SgCompaniaTelecomunicacion.class)
public class SgCompaniaTelecomunicacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ctePk;

    private String cteCodigo;

    private String cteNombre;

    private Boolean cteHabilitado;

    private LocalDateTime cteUltModFecha;

    private String cteUltModUsuario;

    private Integer cteVersion;
    
    
    public SgCompaniaTelecomunicacion() {
        this.cteHabilitado = Boolean.TRUE;
    }

    public Long getCtePk() {
        return ctePk;
    }

    public void setCtePk(Long ctePk) {
        this.ctePk = ctePk;
    }

    public String getCteCodigo() {
        return cteCodigo;
    }

    public void setCteCodigo(String cteCodigo) {
        this.cteCodigo = cteCodigo;
    }

    public String getCteNombre() {
        return cteNombre;
    }

    public void setCteNombre(String cteNombre) {
        this.cteNombre = cteNombre;
    }

    public LocalDateTime getCteUltModFecha() {
        return cteUltModFecha;
    }

    public void setCteUltModFecha(LocalDateTime cteUltModFecha) {
        this.cteUltModFecha = cteUltModFecha;
    }

    public String getCteUltModUsuario() {
        return cteUltModUsuario;
    }

    public void setCteUltModUsuario(String cteUltModUsuario) {
        this.cteUltModUsuario = cteUltModUsuario;
    }

    public Integer getCteVersion() {
        return cteVersion;
    }

    public void setCteVersion(Integer cteVersion) {
        this.cteVersion = cteVersion;
    }

    
     public Boolean getCteHabilitado() {
        return cteHabilitado;
    }

    public void setCteHabilitado(Boolean cteHabilitado) {
        this.cteHabilitado = cteHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ctePk != null ? ctePk.hashCode() : 0);
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
        final SgCompaniaTelecomunicacion other = (SgCompaniaTelecomunicacion) obj;
        if (!Objects.equals(this.ctePk, other.ctePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCompaniaTelecomunicacion[ ctePk=" + ctePk + " ]";
    }
    
}
