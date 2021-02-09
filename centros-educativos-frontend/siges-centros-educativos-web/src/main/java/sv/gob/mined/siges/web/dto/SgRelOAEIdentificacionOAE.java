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
import sv.gob.mined.siges.web.dto.catalogo.SgIdentificacionOAE;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rioPk", scope = SgRelOAEIdentificacionOAE.class)
public class SgRelOAEIdentificacionOAE implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long rioPk;

    private SgOrganismoAdministracionEscolar rioOrganismoAdministracionEscolarFk;
    
    private SgIdentificacionOAE rioIdentificacionOAEfk;
   
    private String rioValor;


    private LocalDateTime rioUltModFecha;

    private String rioUltModUsuario;

    private Integer rioVersion;
    
    
    public SgRelOAEIdentificacionOAE() {
    }

    public Long getRioPk() {
        return rioPk;
    }

    public void setRioPk(Long rioPk) {
        this.rioPk = rioPk;
    }


    public LocalDateTime getRioUltModFecha() {
        return rioUltModFecha;
    }

    public void setRioUltModFecha(LocalDateTime rioUltModFecha) {
        this.rioUltModFecha = rioUltModFecha;
    }

    public String getRioUltModUsuario() {
        return rioUltModUsuario;
    }

    public void setRioUltModUsuario(String rioUltModUsuario) {
        this.rioUltModUsuario = rioUltModUsuario;
    }

    public Integer getRioVersion() {
        return rioVersion;
    }

    public void setRioVersion(Integer rioVersion) {
        this.rioVersion = rioVersion;
    }

    public SgOrganismoAdministracionEscolar getRioOrganismoAdministracionEscolarFk() {
        return rioOrganismoAdministracionEscolarFk;
    }

    public void setRioOrganismoAdministracionEscolarFk(SgOrganismoAdministracionEscolar rioOrganismoAdministracionEscolarFk) {
        this.rioOrganismoAdministracionEscolarFk = rioOrganismoAdministracionEscolarFk;
    }

    public SgIdentificacionOAE getRioIdentificacionOAEfk() {
        return rioIdentificacionOAEfk;
    }

    public void setRioIdentificacionOAEfk(SgIdentificacionOAE rioIdentificacionOAEfk) {
        this.rioIdentificacionOAEfk = rioIdentificacionOAEfk;
    }

    public String getRioValor() {
        return rioValor;
    }

    public void setRioValor(String rioValor) {
        this.rioValor = rioValor;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rioPk != null ? rioPk.hashCode() : 0);
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
        final SgRelOAEIdentificacionOAE other = (SgRelOAEIdentificacionOAE) obj;
        if (!Objects.equals(this.rioPk, other.rioPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelOAEIdentificacionOAE[ rioPk=" + rioPk + " ]";
    }
    
}
