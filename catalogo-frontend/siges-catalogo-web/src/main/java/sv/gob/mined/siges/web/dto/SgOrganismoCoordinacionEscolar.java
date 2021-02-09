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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ocePk", scope = SgOrganismoCoordinacionEscolar.class)
public class SgOrganismoCoordinacionEscolar implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ocePk;

    private String oceCodigo;

    private String oceNombre;

    private Boolean oceHabilitado;

    private LocalDateTime oceUltModFecha;

    private String oceUltModUsuario;

    private Integer oceVersion;
    
    
    public SgOrganismoCoordinacionEscolar() {
        this.oceHabilitado = Boolean.TRUE;
    }

    public Long getOcePk() {
        return ocePk;
    }

    public void setOcePk(Long ocePk) {
        this.ocePk = ocePk;
    }

    public String getOceCodigo() {
        return oceCodigo;
    }

    public void setOceCodigo(String oceCodigo) {
        this.oceCodigo = oceCodigo;
    }

    public String getOceNombre() {
        return oceNombre;
    }

    public void setOceNombre(String oceNombre) {
        this.oceNombre = oceNombre;
    }

    public LocalDateTime getOceUltModFecha() {
        return oceUltModFecha;
    }

    public void setOceUltModFecha(LocalDateTime oceUltModFecha) {
        this.oceUltModFecha = oceUltModFecha;
    }

    public String getOceUltModUsuario() {
        return oceUltModUsuario;
    }

    public void setOceUltModUsuario(String oceUltModUsuario) {
        this.oceUltModUsuario = oceUltModUsuario;
    }

    public Integer getOceVersion() {
        return oceVersion;
    }

    public void setOceVersion(Integer oceVersion) {
        this.oceVersion = oceVersion;
    }

    
     public Boolean getOceHabilitado() {
        return oceHabilitado;
    }

    public void setOceHabilitado(Boolean oceHabilitado) {
        this.oceHabilitado = oceHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ocePk != null ? ocePk.hashCode() : 0);
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
        final SgOrganismoCoordinacionEscolar other = (SgOrganismoCoordinacionEscolar) obj;
        if (!Objects.equals(this.ocePk, other.ocePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgOrganismoCoordinacionEscolar[ ocePk=" + ocePk + " ]";
    }
    
}
