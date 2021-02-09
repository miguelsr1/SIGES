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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rgpPk", scope = SgRelGradoPlanEstudio.class)
public class SgRelGradoPlanEstudio implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long rgpPk;

    private SgGrado rgpGradoFk;

    private SgPlanEstudio rgpPlanEstudioFk;   

    private Boolean rgpHabilitado;

    private LocalDateTime rgpUltModFecha;

    private String rgpUltModUsuario;

    private Integer rgpVersion;
    
    private Boolean rgpConsiderarOrden;
    
    
    public SgRelGradoPlanEstudio() {
        this.rgpHabilitado = Boolean.TRUE;
        this.rgpConsiderarOrden = Boolean.FALSE;
    }

    public Long getRgpPk() {
        return rgpPk;
    }

    public void setRgpPk(Long rgpPk) {
        this.rgpPk = rgpPk;
    }

    public SgGrado getRgpGradoFk() {
        return rgpGradoFk;
    }

    public void setRgpGradoFk(SgGrado rgpGradoFk) {
        this.rgpGradoFk = rgpGradoFk;
    }

    public SgPlanEstudio getRgpPlanEstudioFk() {
        return rgpPlanEstudioFk;
    }

    public void setRgpPlanEstudioFk(SgPlanEstudio rgpPlanEstudioFk) {
        this.rgpPlanEstudioFk = rgpPlanEstudioFk;
    }


    public LocalDateTime getRgpUltModFecha() {
        return rgpUltModFecha;
    }

    public void setRgpUltModFecha(LocalDateTime rgpUltModFecha) {
        this.rgpUltModFecha = rgpUltModFecha;
    }

    public String getRgpUltModUsuario() {
        return rgpUltModUsuario;
    }

    public void setRgpUltModUsuario(String rgpUltModUsuario) {
        this.rgpUltModUsuario = rgpUltModUsuario;
    }

    public Integer getRgpVersion() {
        return rgpVersion;
    }

    public void setRgpVersion(Integer rgpVersion) {
        this.rgpVersion = rgpVersion;
    }

    
     public Boolean getRgpHabilitado() {
        return rgpHabilitado;
    }

    public void setRgpHabilitado(Boolean rgpHabilitado) {
        this.rgpHabilitado = rgpHabilitado;
    }

    public Boolean getRgpConsiderarOrden() {
        return rgpConsiderarOrden;
    }

    public void setRgpConsiderarOrden(Boolean rgpConsiderarOrden) {
        this.rgpConsiderarOrden = rgpConsiderarOrden;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rgpPk != null ? rgpPk.hashCode() : 0);
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
        final SgRelGradoPlanEstudio other = (SgRelGradoPlanEstudio) obj;
        if (!Objects.equals(this.rgpPk, other.rgpPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelGradoPlanEstudio[ rgpPk=" + rgpPk + " ]";
    }
    
}
