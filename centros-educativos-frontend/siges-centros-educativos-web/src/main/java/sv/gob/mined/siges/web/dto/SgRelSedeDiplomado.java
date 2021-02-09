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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rsdPk", scope = SgRelSedeDiplomado.class)
public class SgRelSedeDiplomado implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long rsdPk;
    
    private SgSede rsdSedeFk;

    private SgDiplomado rsdDiplomadoFk;

    private Boolean rsdHabilitado;
    
    private String rsdNumeroTramite;

    private LocalDateTime rsdUltModFecha;

    private String rsdUltModUsuario;

    private Integer rsdVersion;
    
    
    public SgRelSedeDiplomado() {
        this.rsdHabilitado = Boolean.TRUE;
    }

    public Long getRsdPk() {
        return rsdPk;
    }

    public void setRsdPk(Long rsdPk) {
        this.rsdPk = rsdPk;
    }

    public SgSede getRsdSedeFk() {
        return rsdSedeFk;
    }

    public void setRsdSedeFk(SgSede rsdSedeFk) {
        this.rsdSedeFk = rsdSedeFk;
    }

    public SgDiplomado getRsdDiplomadoFk() {
        return rsdDiplomadoFk;
    }

    public void setRsdDiplomadoFk(SgDiplomado rsdDiplomadoFk) {
        this.rsdDiplomadoFk = rsdDiplomadoFk;
    }

    public String getRsdNumeroTramite() {
        return rsdNumeroTramite;
    }

    public void setRsdNumeroTramite(String rsdNumeroTramite) {
        this.rsdNumeroTramite = rsdNumeroTramite;
    }


    public LocalDateTime getRsdUltModFecha() {
        return rsdUltModFecha;
    }

    public void setRsdUltModFecha(LocalDateTime rsdUltModFecha) {
        this.rsdUltModFecha = rsdUltModFecha;
    }

    public String getRsdUltModUsuario() {
        return rsdUltModUsuario;
    }

    public void setRsdUltModUsuario(String rsdUltModUsuario) {
        this.rsdUltModUsuario = rsdUltModUsuario;
    }

    public Integer getRsdVersion() {
        return rsdVersion;
    }

    public void setRsdVersion(Integer rsdVersion) {
        this.rsdVersion = rsdVersion;
    }

    
     public Boolean getRsdHabilitado() {
        return rsdHabilitado;
    }

    public void setRsdHabilitado(Boolean rsdHabilitado) {
        this.rsdHabilitado = rsdHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rsdPk != null ? rsdPk.hashCode() : 0);
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
        final SgRelSedeDiplomado other = (SgRelSedeDiplomado) obj;
        if (!Objects.equals(this.rsdPk, other.rsdPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelSedeDiplomado[ rsdPk=" + rsdPk + " ]";
    }
    
}
