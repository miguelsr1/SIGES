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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "minPk", scope = SgMotivoInasistencia.class)
public class SgMotivoInasistencia implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long minPk;

    private String minCodigo;

    private String minNombre;

    private Boolean minHabilitado;
    
    private Boolean minMotivoJustificado;

    private LocalDateTime minUltModFecha;

    private String minUltModUsuario;

    private Integer minVersion;
    
    
    public SgMotivoInasistencia() {
        this.minHabilitado = Boolean.TRUE;
    }

    public Long getMinPk() {
        return minPk;
    }

    public void setMinPk(Long minPk) {
        this.minPk = minPk;
    }

    public String getMinCodigo() {
        return minCodigo;
    }

    public void setMinCodigo(String minCodigo) {
        this.minCodigo = minCodigo;
    }

    public String getMinNombre() {
        return minNombre;
    }

    public void setMinNombre(String minNombre) {
        this.minNombre = minNombre;
    }

    public Boolean getMinMotivoJustificado() {
        return minMotivoJustificado;
    }

    public void setMinMotivoJustificado(Boolean minMotivoJustificado) {
        this.minMotivoJustificado = minMotivoJustificado;
    }

    public LocalDateTime getMinUltModFecha() {
        return minUltModFecha;
    }

    public void setMinUltModFecha(LocalDateTime minUltModFecha) {
        this.minUltModFecha = minUltModFecha;
    }

    public String getMinUltModUsuario() {
        return minUltModUsuario;
    }

    public void setMinUltModUsuario(String minUltModUsuario) {
        this.minUltModUsuario = minUltModUsuario;
    }

    public Integer getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(Integer minVersion) {
        this.minVersion = minVersion;
    }

    
     public Boolean getMinHabilitado() {
        return minHabilitado;
    }

    public void setMinHabilitado(Boolean minHabilitado) {
        this.minHabilitado = minHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (minPk != null ? minPk.hashCode() : 0);
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
        final SgMotivoInasistencia other = (SgMotivoInasistencia) obj;
        if (!Objects.equals(this.minPk, other.minPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMotivoInasistencia[ minPk=" + minPk + " ]";
    }
    
}
