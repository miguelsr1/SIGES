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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tanPk", scope = SgTituloAnterior.class)
public class SgTituloAnterior implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tanPk;

    private String tanCodigo;

    private String tanNombre;

    private Boolean tanHabilitado;

    private LocalDateTime tanUltModFecha;

    private String tanUltModUsuario;

    private Integer tanVersion;
    
    
    public SgTituloAnterior() {
        this.tanHabilitado = Boolean.TRUE;
    }

    public Long getTanPk() {
        return tanPk;
    }

    public void setTanPk(Long tanPk) {
        this.tanPk = tanPk;
    }

    public String getTanCodigo() {
        return tanCodigo;
    }

    public void setTanCodigo(String tanCodigo) {
        this.tanCodigo = tanCodigo;
    }

    public String getTanNombre() {
        return tanNombre;
    }

    public void setTanNombre(String tanNombre) {
        this.tanNombre = tanNombre;
    }

    public LocalDateTime getTanUltModFecha() {
        return tanUltModFecha;
    }

    public void setTanUltModFecha(LocalDateTime tanUltModFecha) {
        this.tanUltModFecha = tanUltModFecha;
    }

    public String getTanUltModUsuario() {
        return tanUltModUsuario;
    }

    public void setTanUltModUsuario(String tanUltModUsuario) {
        this.tanUltModUsuario = tanUltModUsuario;
    }

    public Integer getTanVersion() {
        return tanVersion;
    }

    public void setTanVersion(Integer tanVersion) {
        this.tanVersion = tanVersion;
    }

    
     public Boolean getTanHabilitado() {
        return tanHabilitado;
    }

    public void setTanHabilitado(Boolean tanHabilitado) {
        this.tanHabilitado = tanHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tanPk != null ? tanPk.hashCode() : 0);
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
        final SgTituloAnterior other = (SgTituloAnterior) obj;
        if (!Objects.equals(this.tanPk, other.tanPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTituloAnterior[ tanPk=" + tanPk + " ]";
    }
    
}
