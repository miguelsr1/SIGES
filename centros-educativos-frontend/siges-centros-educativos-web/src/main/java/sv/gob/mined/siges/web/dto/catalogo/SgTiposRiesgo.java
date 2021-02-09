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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "triPk", scope = SgTiposRiesgo.class)
public class SgTiposRiesgo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long triPk;

    private String triCodigo;

    private String triNombre;

    private Boolean triHabilitado;

    private Boolean triRiesgoAmbiental;

    private Boolean triRiesgoSocial;

    private LocalDateTime triUltModFecha;

    private String triUltModUsuario;

    private Integer triVersion;
    
    
    public SgTiposRiesgo() {
        this.triHabilitado = Boolean.TRUE;
    }

    public Long getTriPk() {
        return triPk;
    }

    public void setTriPk(Long triPk) {
        this.triPk = triPk;
    }

    public String getTriCodigo() {
        return triCodigo;
    }

    public void setTriCodigo(String triCodigo) {
        this.triCodigo = triCodigo;
    }

    public String getTriNombre() {
        return triNombre;
    }

    public void setTriNombre(String triNombre) {
        this.triNombre = triNombre;
    }

    public LocalDateTime getTriUltModFecha() {
        return triUltModFecha;
    }

    public void setTriUltModFecha(LocalDateTime triUltModFecha) {
        this.triUltModFecha = triUltModFecha;
    }

    public String getTriUltModUsuario() {
        return triUltModUsuario;
    }

    public void setTriUltModUsuario(String triUltModUsuario) {
        this.triUltModUsuario = triUltModUsuario;
    }

    public Integer getTriVersion() {
        return triVersion;
    }

    public void setTriVersion(Integer triVersion) {
        this.triVersion = triVersion;
    }

    
     public Boolean getTriHabilitado() {
        return triHabilitado;
    }

    public void setTriHabilitado(Boolean triHabilitado) {
        this.triHabilitado = triHabilitado;
    }

    public Boolean getTriRiesgoAmbiental() {
        return triRiesgoAmbiental;
    }

    public void setTriRiesgoAmbiental(Boolean triRiesgoAmbiental) {
        this.triRiesgoAmbiental = triRiesgoAmbiental;
    }

    public Boolean getTriRiesgoSocial() {
        return triRiesgoSocial;
    }

    public void setTriRiesgoSocial(Boolean triRiesgoSocial) {
        this.triRiesgoSocial = triRiesgoSocial;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (triPk != null ? triPk.hashCode() : 0);
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
        final SgTiposRiesgo other = (SgTiposRiesgo) obj;
        if (!Objects.equals(this.triPk, other.triPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTiposRiesgo[ triPk=" + triPk + " ]";
    }
    
}
