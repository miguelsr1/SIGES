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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tmaPk", scope = SgTipoManifestacion.class)
public class SgTipoManifestacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tmaPk;

    private String tmaCodigo;

    private String tmaNombre;
    
    private String tmaNombreBusqueda;

    private Boolean tmaHabilitado;

    private LocalDateTime tmaUltModFecha;

    private String tmaUltModUsuario;

    private Integer tmaVersion;
    
    
    public SgTipoManifestacion() {
        this.tmaHabilitado = Boolean.TRUE;
    }

    public Long getTmaPk() {
        return tmaPk;
    }

    public void setTmaPk(Long tmaPk) {
        this.tmaPk = tmaPk;
    }

    public String getTmaCodigo() {
        return tmaCodigo;
    }

    public void setTmaCodigo(String tmaCodigo) {
        this.tmaCodigo = tmaCodigo;
    }

    public String getTmaNombre() {
        return tmaNombre;
    }

    public void setTmaNombre(String tmaNombre) {
        this.tmaNombre = tmaNombre;
    }

    public String getTmaNombreBusqueda() {
        return tmaNombreBusqueda;
    }

    public void setTmaNombreBusqueda(String tmaNombreBusqueda) {
        this.tmaNombreBusqueda = tmaNombreBusqueda;
    }

    public Boolean getTmaHabilitado() {
        return tmaHabilitado;
    }

    public void setTmaHabilitado(Boolean tmaHabilitado) {
        this.tmaHabilitado = tmaHabilitado;
    }

    public LocalDateTime getTmaUltModFecha() {
        return tmaUltModFecha;
    }

    public void setTmaUltModFecha(LocalDateTime tmaUltModFecha) {
        this.tmaUltModFecha = tmaUltModFecha;
    }

    public String getTmaUltModUsuario() {
        return tmaUltModUsuario;
    }

    public void setTmaUltModUsuario(String tmaUltModUsuario) {
        this.tmaUltModUsuario = tmaUltModUsuario;
    }

    public Integer getTmaVersion() {
        return tmaVersion;
    }

    public void setTmaVersion(Integer tmaVersion) {
        this.tmaVersion = tmaVersion;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tmaPk != null ? tmaPk.hashCode() : 0);
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
        final SgTipoManifestacion other = (SgTipoManifestacion) obj;
        if (!Objects.equals(this.tmaPk, other.tmaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoManifestacion[ tmaPk=" + tmaPk + " ]";
    }
    
}
