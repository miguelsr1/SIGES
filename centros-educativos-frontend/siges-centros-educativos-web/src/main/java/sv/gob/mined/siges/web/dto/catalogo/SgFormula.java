/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumTipoFormula;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "fomPk", scope = SgFormula.class)
public class SgFormula implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long fomPk;

    private String fomCodigo;

    private String fomNombre;

    private Boolean fomHabilitado;

    private String fomTextoLargo;

    private String fomDescripcion;
    
    private EnumTipoFormula fomTipoFormula;

    private LocalDateTime fomUltModFecha;

    private String fomUltModUsuario;

    private Integer fomVersion;
    
    private List<SgFormula> fomSubFormula;
    
    
    private Boolean fomTienSubformula;
    
    
    public SgFormula() {
        this.fomHabilitado = Boolean.TRUE;
    }

    public Long getFomPk() {
        return fomPk;
    }

    public void setFomPk(Long fomPk) {
        this.fomPk = fomPk;
    }

    public String getFomCodigo() {
        return fomCodigo;
    }

    public void setFomCodigo(String fomCodigo) {
        this.fomCodigo = fomCodigo;
    }

    public String getFomNombre() {
        return fomNombre;
    }

    public void setFomNombre(String fomNombre) {
        this.fomNombre = fomNombre;
    }

    public LocalDateTime getFomUltModFecha() {
        return fomUltModFecha;
    }

    public void setFomUltModFecha(LocalDateTime fomUltModFecha) {
        this.fomUltModFecha = fomUltModFecha;
    }

    public String getFomUltModUsuario() {
        return fomUltModUsuario;
    }

    public void setFomUltModUsuario(String fomUltModUsuario) {
        this.fomUltModUsuario = fomUltModUsuario;
    }

    public Integer getFomVersion() {
        return fomVersion;
    }

    public void setFomVersion(Integer fomVersion) {
        this.fomVersion = fomVersion;
    }

    
     public Boolean getFomHabilitado() {
        return fomHabilitado;
    }

    public void setFomHabilitado(Boolean fomHabilitado) {
        this.fomHabilitado = fomHabilitado;
    }

    public String getFomTextoLargo() {
        return fomTextoLargo;
    }

    public void setFomTextoLargo(String fomTextoLargo) {
        this.fomTextoLargo = fomTextoLargo;
    }

    public String getFomDescripcion() {
        return fomDescripcion;
    }

    public void setFomDescripcion(String fomDescripcion) {
        this.fomDescripcion = fomDescripcion;
    }

    public EnumTipoFormula getFomTipoFormula() {
        return fomTipoFormula;
    }

    public void setFomTipoFormula(EnumTipoFormula fomTipoFormula) {
        this.fomTipoFormula = fomTipoFormula;
    }

    public List<SgFormula> getFomSubFormula() {
        return fomSubFormula;
    }

    public void setFomSubFormula(List<SgFormula> fomSubFormula) {
        this.fomSubFormula = fomSubFormula;
    }

    public Boolean getFomTienSubformula() {
        return fomTienSubformula;
    }

    public void setFomTienSubformula(Boolean fomTienSubformula) {
        this.fomTienSubformula = fomTienSubformula;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fomPk != null ? fomPk.hashCode() : 0);
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
        final SgFormula other = (SgFormula) obj;
        if (!Objects.equals(this.fomPk, other.fomPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgFormula[ fomPk=" + fomPk + " ]";
    }
    
}
