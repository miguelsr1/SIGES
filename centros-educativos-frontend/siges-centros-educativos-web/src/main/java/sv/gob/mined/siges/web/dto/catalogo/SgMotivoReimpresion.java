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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mriPk", scope = SgMotivoReimpresion.class)
public class SgMotivoReimpresion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mriPk;

    private String mriCodigo;

    private String mriNombre;

    private Boolean mriHabilitado;

    private LocalDateTime mriUltModFecha;

    private String mriUltModUsuario;

    private Integer mriVersion;
    
    
    public SgMotivoReimpresion() {
        this.mriHabilitado = Boolean.TRUE;
    }

    public Long getMriPk() {
        return mriPk;
    }

    public void setMriPk(Long mriPk) {
        this.mriPk = mriPk;
    }

    public String getMriCodigo() {
        return mriCodigo;
    }

    public void setMriCodigo(String mriCodigo) {
        this.mriCodigo = mriCodigo;
    }

    public String getMriNombre() {
        return mriNombre;
    }

    public void setMriNombre(String mriNombre) {
        this.mriNombre = mriNombre;
    }

    public LocalDateTime getMriUltModFecha() {
        return mriUltModFecha;
    }

    public void setMriUltModFecha(LocalDateTime mriUltModFecha) {
        this.mriUltModFecha = mriUltModFecha;
    }

    public String getMriUltModUsuario() {
        return mriUltModUsuario;
    }

    public void setMriUltModUsuario(String mriUltModUsuario) {
        this.mriUltModUsuario = mriUltModUsuario;
    }

    public Integer getMriVersion() {
        return mriVersion;
    }

    public void setMriVersion(Integer mriVersion) {
        this.mriVersion = mriVersion;
    }

    
     public Boolean getMriHabilitado() {
        return mriHabilitado;
    }

    public void setMriHabilitado(Boolean mriHabilitado) {
        this.mriHabilitado = mriHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mriPk != null ? mriPk.hashCode() : 0);
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
        final SgMotivoReimpresion other = (SgMotivoReimpresion) obj;
        if (!Objects.equals(this.mriPk, other.mriPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMotivoReimpresion[ mriPk=" + mriPk + " ]";
    }
    
}
