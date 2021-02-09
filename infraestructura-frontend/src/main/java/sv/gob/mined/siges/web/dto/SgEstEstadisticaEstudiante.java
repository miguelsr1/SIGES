/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "esePk", scope = SgEstEstadisticaEstudiante.class)
public class SgEstEstadisticaEstudiante implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long esePk;

    private String eseCodigo;

    private String eseNombre;

    private Boolean eseHabilitado;

    private LocalDateTime eseUltModFecha;

    private String eseUltModUsuario;

    private Integer eseVersion;
    
    
    public SgEstEstadisticaEstudiante() {
        this.eseHabilitado = Boolean.TRUE;
    }

    public Long getEsePk() {
        return esePk;
    }

    public void setEsePk(Long esePk) {
        this.esePk = esePk;
    }

    public String getEseCodigo() {
        return eseCodigo;
    }

    public void setEseCodigo(String eseCodigo) {
        this.eseCodigo = eseCodigo;
    }

    public String getEseNombre() {
        return eseNombre;
    }

    public void setEseNombre(String eseNombre) {
        this.eseNombre = eseNombre;
    }

    public LocalDateTime getEseUltModFecha() {
        return eseUltModFecha;
    }

    public void setEseUltModFecha(LocalDateTime eseUltModFecha) {
        this.eseUltModFecha = eseUltModFecha;
    }

    public String getEseUltModUsuario() {
        return eseUltModUsuario;
    }

    public void setEseUltModUsuario(String eseUltModUsuario) {
        this.eseUltModUsuario = eseUltModUsuario;
    }

    public Integer getEseVersion() {
        return eseVersion;
    }

    public void setEseVersion(Integer eseVersion) {
        this.eseVersion = eseVersion;
    }

    
     public Boolean getEseHabilitado() {
        return eseHabilitado;
    }

    public void setEseHabilitado(Boolean eseHabilitado) {
        this.eseHabilitado = eseHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (esePk != null ? esePk.hashCode() : 0);
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
        final SgEstEstadisticaEstudiante other = (SgEstEstadisticaEstudiante) obj;
        if (!Objects.equals(this.esePk, other.esePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEstadisticaEstudiante[ esePk=" + esePk + " ]";
    }
    
}
