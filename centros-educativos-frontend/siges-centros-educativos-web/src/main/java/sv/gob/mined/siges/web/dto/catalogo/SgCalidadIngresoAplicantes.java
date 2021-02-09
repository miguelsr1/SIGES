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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ciaPk", scope = SgCalidadIngresoAplicantes.class)
public class SgCalidadIngresoAplicantes implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ciaPk;

    private String ciaCodigo;

    private String ciaNombre;

    private Boolean ciaHabilitado;

    private LocalDateTime ciaUltModFecha;

    private String ciaUltModUsuario;

    private Integer ciaVersion;
    
    
    public SgCalidadIngresoAplicantes() {
        this.ciaHabilitado = Boolean.TRUE;
    }

    public Long getCiaPk() {
        return ciaPk;
    }

    public void setCiaPk(Long ciaPk) {
        this.ciaPk = ciaPk;
    }

    public String getCiaCodigo() {
        return ciaCodigo;
    }

    public void setCiaCodigo(String ciaCodigo) {
        this.ciaCodigo = ciaCodigo;
    }

    public String getCiaNombre() {
        return ciaNombre;
    }

    public void setCiaNombre(String ciaNombre) {
        this.ciaNombre = ciaNombre;
    }

    public LocalDateTime getCiaUltModFecha() {
        return ciaUltModFecha;
    }

    public void setCiaUltModFecha(LocalDateTime ciaUltModFecha) {
        this.ciaUltModFecha = ciaUltModFecha;
    }

    public String getCiaUltModUsuario() {
        return ciaUltModUsuario;
    }

    public void setCiaUltModUsuario(String ciaUltModUsuario) {
        this.ciaUltModUsuario = ciaUltModUsuario;
    }

    public Integer getCiaVersion() {
        return ciaVersion;
    }

    public void setCiaVersion(Integer ciaVersion) {
        this.ciaVersion = ciaVersion;
    }

    
     public Boolean getCiaHabilitado() {
        return ciaHabilitado;
    }

    public void setCiaHabilitado(Boolean ciaHabilitado) {
        this.ciaHabilitado = ciaHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ciaPk != null ? ciaPk.hashCode() : 0);
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
        final SgCalidadIngresoAplicantes other = (SgCalidadIngresoAplicantes) obj;
        if (!Objects.equals(this.ciaPk, other.ciaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCalidadIngresoAplicantes[ ciaPk=" + ciaPk + " ]";
    }
    
}
