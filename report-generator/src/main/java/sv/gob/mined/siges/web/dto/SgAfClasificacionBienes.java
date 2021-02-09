/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cbiPk", scope = SgAfClasificacionBienes.class)
public class SgAfClasificacionBienes implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long cbiPk;

    private String cbiCodigo;

    private String cbiNombre;

    private Boolean cbiHabilitado;
    
    private LocalDateTime cbiUltModFecha;

    private String cbiUltModUsuario;

    private Integer cbiVersion;
    
    
    public SgAfClasificacionBienes() {
        this.cbiHabilitado = Boolean.TRUE;
    }

    public Long getCbiPk() {
        return cbiPk;
    }

    public void setCbiPk(Long cbiPk) {
        this.cbiPk = cbiPk;
    }

    public String getCbiCodigo() {
        return cbiCodigo;
    }

    public void setCbiCodigo(String cbiCodigo) {
        this.cbiCodigo = cbiCodigo;
    }

    public String getCbiNombre() {
        return cbiNombre;
    }

    public void setCbiNombre(String cbiNombre) {
        this.cbiNombre = cbiNombre;
    }

    public Boolean getCbiHabilitado() {
        return cbiHabilitado;
    }

    public void setCbiHabilitado(Boolean cbiHabilitado) {
        this.cbiHabilitado = cbiHabilitado;
    }

    public LocalDateTime getCbiUltModFecha() {
        return cbiUltModFecha;
    }

    public void setCbiUltModFecha(LocalDateTime cbiUltModFecha) {
        this.cbiUltModFecha = cbiUltModFecha;
    }

    public String getCbiUltModUsuario() {
        return cbiUltModUsuario;
    }

    public void setCbiUltModUsuario(String cbiUltModUsuario) {
        this.cbiUltModUsuario = cbiUltModUsuario;
    }

    public Integer getCbiVersion() {
        return cbiVersion;
    }

    public void setCbiVersion(Integer cbiVersion) {
        this.cbiVersion = cbiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.cbiPk);
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
        final SgAfClasificacionBienes other = (SgAfClasificacionBienes) obj;
        if (!Objects.equals(this.cbiPk, other.cbiPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.sofis.entidades.SgClasificacionBienes[ cbiPk=" + cbiPk + " ]";
    }
    
}
