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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "gafPk", scope = SgGradoAfectacion.class)
public class SgGradoAfectacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long gafPk;

    private String gafCodigo;

    private String gafNombre;

    private Boolean gafHabilitado;

    private LocalDateTime gafUltModFecha;

    private String gafUltModUsuario;

    private Integer gafVersion;
    
    
    public SgGradoAfectacion() {
        this.gafHabilitado = Boolean.TRUE;
    }

    public Long getGafPk() {
        return gafPk;
    }

    public void setGafPk(Long gafPk) {
        this.gafPk = gafPk;
    }

    public String getGafCodigo() {
        return gafCodigo;
    }

    public void setGafCodigo(String gafCodigo) {
        this.gafCodigo = gafCodigo;
    }

    public String getGafNombre() {
        return gafNombre;
    }

    public void setGafNombre(String gafNombre) {
        this.gafNombre = gafNombre;
    }

    public LocalDateTime getGafUltModFecha() {
        return gafUltModFecha;
    }

    public void setGafUltModFecha(LocalDateTime gafUltModFecha) {
        this.gafUltModFecha = gafUltModFecha;
    }

    public String getGafUltModUsuario() {
        return gafUltModUsuario;
    }

    public void setGafUltModUsuario(String gafUltModUsuario) {
        this.gafUltModUsuario = gafUltModUsuario;
    }

    public Integer getGafVersion() {
        return gafVersion;
    }

    public void setGafVersion(Integer gafVersion) {
        this.gafVersion = gafVersion;
    }

    
     public Boolean getGafHabilitado() {
        return gafHabilitado;
    }

    public void setGafHabilitado(Boolean gafHabilitado) {
        this.gafHabilitado = gafHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gafPk != null ? gafPk.hashCode() : 0);
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
        final SgGradoAfectacion other = (SgGradoAfectacion) obj;
        if (!Objects.equals(this.gafPk, other.gafPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgGradoAfectacion[ gafPk=" + gafPk + " ]";
    }
    
}
