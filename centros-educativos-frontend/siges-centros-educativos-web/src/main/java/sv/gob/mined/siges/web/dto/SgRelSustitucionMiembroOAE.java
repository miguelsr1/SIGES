/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rsmPk", scope = SgRelSustitucionMiembroOAE.class)
public class SgRelSustitucionMiembroOAE implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long rsmPk;

    private SgSustitucionMiembroOAE rsmSustitucionMiembroFk;
    
    private SgPersonaOrganismoAdministracion rsmMiembroaSustituirFk;    
    
    private SgPersonaOrganismoAdministracion rsmMiembroSustituyenteFk;
 
    private LocalDateTime rsmUltModFecha;

    private String rsmUltModUsuario;

    private Integer rsmVersion;
    
    
    public SgRelSustitucionMiembroOAE() {
    }

    public Long getRsmPk() {
        return rsmPk;
    }

    public void setRsmPk(Long rsmPk) {
        this.rsmPk = rsmPk;
    }

    public SgSustitucionMiembroOAE getRsmSustitucionMiembroFk() {
        return rsmSustitucionMiembroFk;
    }

    public void setRsmSustitucionMiembroFk(SgSustitucionMiembroOAE rsmSustitucionMiembroFk) {
        this.rsmSustitucionMiembroFk = rsmSustitucionMiembroFk;
    }

    public SgPersonaOrganismoAdministracion getRsmMiembroaSustituirFk() {
        return rsmMiembroaSustituirFk;
    }

    public void setRsmMiembroaSustituirFk(SgPersonaOrganismoAdministracion rsmMiembroaSustituirFk) {
        this.rsmMiembroaSustituirFk = rsmMiembroaSustituirFk;
    }

    public SgPersonaOrganismoAdministracion getRsmMiembroSustituyenteFk() {
        return rsmMiembroSustituyenteFk;
    }

    public void setRsmMiembroSustituyenteFk(SgPersonaOrganismoAdministracion rsmMiembroSustituyenteFk) {
        this.rsmMiembroSustituyenteFk = rsmMiembroSustituyenteFk;
    }


    public LocalDateTime getRsmUltModFecha() {
        return rsmUltModFecha;
    }

    public void setRsmUltModFecha(LocalDateTime rsmUltModFecha) {
        this.rsmUltModFecha = rsmUltModFecha;
    }

    public String getRsmUltModUsuario() {
        return rsmUltModUsuario;
    }

    public void setRsmUltModUsuario(String rsmUltModUsuario) {
        this.rsmUltModUsuario = rsmUltModUsuario;
    }

    public Integer getRsmVersion() {
        return rsmVersion;
    }

    public void setRsmVersion(Integer rsmVersion) {
        this.rsmVersion = rsmVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rsmPk != null ? rsmPk.hashCode() : 0);
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
        final SgRelSustitucionMiembroOAE other = (SgRelSustitucionMiembroOAE) obj;
        if (!Objects.equals(this.rsmPk, other.rsmPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelSustitucionMiembroOAE[ rsmPk=" + rsmPk + " ]";
    }
    
}
