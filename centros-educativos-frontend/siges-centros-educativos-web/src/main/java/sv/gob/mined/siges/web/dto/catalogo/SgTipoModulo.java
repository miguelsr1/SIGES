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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tmoPk", scope = SgTipoModulo.class)
public class SgTipoModulo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tmoPk;

    private String tmoCodigo;

    private String tmoNombre;

    private Boolean tmoHabilitado;

    private LocalDateTime tmoUltModFecha;

    private String tmoUltModUsuario;

    private Integer tmoVersion;
    
    
    public SgTipoModulo() {
        this.tmoHabilitado = Boolean.TRUE;
    }

    public Long getTmoPk() {
        return tmoPk;
    }

    public void setTmoPk(Long tmoPk) {
        this.tmoPk = tmoPk;
    }

    public String getTmoCodigo() {
        return tmoCodigo;
    }

    public void setTmoCodigo(String tmoCodigo) {
        this.tmoCodigo = tmoCodigo;
    }

    public String getTmoNombre() {
        return tmoNombre;
    }

    public void setTmoNombre(String tmoNombre) {
        this.tmoNombre = tmoNombre;
    }

    public LocalDateTime getTmoUltModFecha() {
        return tmoUltModFecha;
    }

    public void setTmoUltModFecha(LocalDateTime tmoUltModFecha) {
        this.tmoUltModFecha = tmoUltModFecha;
    }

    public String getTmoUltModUsuario() {
        return tmoUltModUsuario;
    }

    public void setTmoUltModUsuario(String tmoUltModUsuario) {
        this.tmoUltModUsuario = tmoUltModUsuario;
    }

    public Integer getTmoVersion() {
        return tmoVersion;
    }

    public void setTmoVersion(Integer tmoVersion) {
        this.tmoVersion = tmoVersion;
    }

    
     public Boolean getTmoHabilitado() {
        return tmoHabilitado;
    }

    public void setTmoHabilitado(Boolean tmoHabilitado) {
        this.tmoHabilitado = tmoHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tmoPk != null ? tmoPk.hashCode() : 0);
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
        final SgTipoModulo other = (SgTipoModulo) obj;
        if (!Objects.equals(this.tmoPk, other.tmoPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoModulo[ tmoPk=" + tmoPk + " ]";
    }
    
}
