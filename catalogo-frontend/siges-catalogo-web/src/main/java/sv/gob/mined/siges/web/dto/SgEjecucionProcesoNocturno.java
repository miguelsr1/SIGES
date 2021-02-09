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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eprPk", scope = SgEjecucionProcesoNocturno.class)
public class SgEjecucionProcesoNocturno implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long eprPk;

    private LocalDateTime eprComienzoEjecucion;

    private LocalDateTime eprFinEjecucion;

    private Boolean eprEjecucionCorrecta;

    private SgProcesoNocturno eprProcesoNocturnoFk;

    private LocalDateTime eprUltModFecha;

    private String eprUltModUsuario;

    private Integer eprVersion;
    
    
    
    
    public SgEjecucionProcesoNocturno() {
   
    }

    public Long getEprPk() {
        return eprPk;
    }

    public void setEprPk(Long eprPk) {
        this.eprPk = eprPk;
    }

    public LocalDateTime getEprUltModFecha() {
        return eprUltModFecha;
    }

    public void setEprUltModFecha(LocalDateTime eprUltModFecha) {
        this.eprUltModFecha = eprUltModFecha;
    }

    public String getEprUltModUsuario() {
        return eprUltModUsuario;
    }

    public void setEprUltModUsuario(String eprUltModUsuario) {
        this.eprUltModUsuario = eprUltModUsuario;
    }

    public Integer getEprVersion() {
        return eprVersion;
    }

    public void setEprVersion(Integer eprVersion) {
        this.eprVersion = eprVersion;
    }

    public LocalDateTime getEprComienzoEjecucion() {
        return eprComienzoEjecucion;
    }

    public void setEprComienzoEjecucion(LocalDateTime eprComienzoEjecucion) {
        this.eprComienzoEjecucion = eprComienzoEjecucion;
    }

    public LocalDateTime getEprFinEjecucion() {
        return eprFinEjecucion;
    }

    public void setEprFinEjecucion(LocalDateTime eprFinEjecucion) {
        this.eprFinEjecucion = eprFinEjecucion;
    }

    public Boolean getEprEjecucionCorrecta() {
        return eprEjecucionCorrecta;
    }

    public void setEprEjecucionCorrecta(Boolean eprEjecucionCorrecta) {
        this.eprEjecucionCorrecta = eprEjecucionCorrecta;
    }

    public SgProcesoNocturno getEprProcesoNocturnoFk() {
        return eprProcesoNocturnoFk;
    }

    public void setEprProcesoNocturnoFk(SgProcesoNocturno eprProcesoNocturnoFk) {
        this.eprProcesoNocturnoFk = eprProcesoNocturnoFk;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eprPk != null ? eprPk.hashCode() : 0);
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
        final SgEjecucionProcesoNocturno other = (SgEjecucionProcesoNocturno) obj;
        if (!Objects.equals(this.eprPk, other.eprPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEjecucionProcesoNocturno[ eprPk=" + eprPk + " ]";
    }
    
}
