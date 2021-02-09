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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tprPk", scope = SgTipoProveedor.class)
public class SgTipoProveedor implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tprPk;

    private String tprCodigo;

    private String tprNombre;

    private Boolean tprHabilitado;

    private LocalDateTime tprUltModFecha;

    private String tprUltModUsuario;

    private Integer tprVersion;
    
    
    public SgTipoProveedor() {
        this.tprHabilitado = Boolean.TRUE;
    }

    public Long getTprPk() {
        return tprPk;
    }

    public void setTprPk(Long tprPk) {
        this.tprPk = tprPk;
    }

    public String getTprCodigo() {
        return tprCodigo;
    }

    public void setTprCodigo(String tprCodigo) {
        this.tprCodigo = tprCodigo;
    }

    public String getTprNombre() {
        return tprNombre;
    }

    public void setTprNombre(String tprNombre) {
        this.tprNombre = tprNombre;
    }

    public LocalDateTime getTprUltModFecha() {
        return tprUltModFecha;
    }

    public void setTprUltModFecha(LocalDateTime tprUltModFecha) {
        this.tprUltModFecha = tprUltModFecha;
    }

    public String getTprUltModUsuario() {
        return tprUltModUsuario;
    }

    public void setTprUltModUsuario(String tprUltModUsuario) {
        this.tprUltModUsuario = tprUltModUsuario;
    }

    public Integer getTprVersion() {
        return tprVersion;
    }

    public void setTprVersion(Integer tprVersion) {
        this.tprVersion = tprVersion;
    }

    
     public Boolean getTprHabilitado() {
        return tprHabilitado;
    }

    public void setTprHabilitado(Boolean tprHabilitado) {
        this.tprHabilitado = tprHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tprPk != null ? tprPk.hashCode() : 0);
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
        final SgTipoProveedor other = (SgTipoProveedor) obj;
        if (!Objects.equals(this.tprPk, other.tprPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoProveedor[ tprPk=" + tprPk + " ]";
    }
    
}
