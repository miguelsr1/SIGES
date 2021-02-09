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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "fadPk", scope = SgAfFormaAdquisicion.class)
public class SgAfFormaAdquisicion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long fadPk;

    private String fadCodigo;

    private String fadNombre;

    private Boolean fadHabilitado;

    private LocalDateTime fadUltModFecha;

    private String fadUltModUsuario;

    private Integer fadVersion;
    
    
    public SgAfFormaAdquisicion() {
        this.fadHabilitado = Boolean.TRUE;
    }

    public Long getFadPk() {
        return fadPk;
    }

    public void setFadPk(Long fadPk) {
        this.fadPk = fadPk;
    }

    public String getFadCodigo() {
        return fadCodigo;
    }

    public void setFadCodigo(String fadCodigo) {
        this.fadCodigo = fadCodigo;
    }

    public String getFadNombre() {
        return fadNombre;
    }

    public void setFadNombre(String fadNombre) {
        this.fadNombre = fadNombre;
    }

    public Boolean getFadHabilitado() {
        return fadHabilitado;
    }

    public void setFadHabilitado(Boolean fadHabilitado) {
        this.fadHabilitado = fadHabilitado;
    }

    public LocalDateTime getFadUltModFecha() {
        return fadUltModFecha;
    }

    public void setFadUltModFecha(LocalDateTime fadUltModFecha) {
        this.fadUltModFecha = fadUltModFecha;
    }

    public String getFadUltModUsuario() {
        return fadUltModUsuario;
    }

    public void setFadUltModUsuario(String fadUltModUsuario) {
        this.fadUltModUsuario = fadUltModUsuario;
    }

    public Integer getFadVersion() {
        return fadVersion;
    }

    public void setFadVersion(Integer fadVersion) {
        this.fadVersion = fadVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.fadPk);
        hash = 61 * hash + Objects.hashCode(this.fadCodigo);
        hash = 61 * hash + Objects.hashCode(this.fadNombre);
        hash = 61 * hash + Objects.hashCode(this.fadHabilitado);
        hash = 61 * hash + Objects.hashCode(this.fadUltModFecha);
        hash = 61 * hash + Objects.hashCode(this.fadUltModUsuario);
        hash = 61 * hash + Objects.hashCode(this.fadVersion);
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
        final SgAfFormaAdquisicion other = (SgAfFormaAdquisicion) obj;
        if (!Objects.equals(this.fadPk, other.fadPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgFormaAdquisicion[ etnPk=" + fadPk + " ]";
    }
    
}
