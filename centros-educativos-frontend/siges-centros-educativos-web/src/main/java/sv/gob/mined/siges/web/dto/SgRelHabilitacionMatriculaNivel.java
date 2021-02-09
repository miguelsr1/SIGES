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
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rhnPk", scope = SgRelHabilitacionMatriculaNivel.class)
public class SgRelHabilitacionMatriculaNivel implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long rhnPk;

    private SgNivel rhnNivelFk;
 
    private SgModalidadAtencion rhnModalidadAtencionFk;
    
    private SgSubModalidadAtencion rhnSubmodaliadadFk;
   
    private SgHabilitacionPeriodoMatricula rhnHabilitacionPeriodoMatriculaFk;

    private LocalDateTime rhnUltModFecha;

    private String rhnUltModUsuario;

    private Integer rhnVersion;
    
    
    public SgRelHabilitacionMatriculaNivel() {
    }

    public Long getRhnPk() {
        return rhnPk;
    }

    public void setRhnPk(Long rhnPk) {
        this.rhnPk = rhnPk;
    }

    public SgNivel getRhnNivelFk() {
        return rhnNivelFk;
    }

    public void setRhnNivelFk(SgNivel rhnNivelFk) {
        this.rhnNivelFk = rhnNivelFk;
    }

    public SgModalidadAtencion getRhnModalidadAtencionFk() {
        return rhnModalidadAtencionFk;
    }

    public void setRhnModalidadAtencionFk(SgModalidadAtencion rhnModalidadAtencionFk) {
        this.rhnModalidadAtencionFk = rhnModalidadAtencionFk;
    }

    public SgSubModalidadAtencion getRhnSubmodaliadadFk() {
        return rhnSubmodaliadadFk;
    }

    public void setRhnSubmodaliadadFk(SgSubModalidadAtencion rhnSubmodaliadadFk) {
        this.rhnSubmodaliadadFk = rhnSubmodaliadadFk;
    }

    public SgHabilitacionPeriodoMatricula getRhnHabilitacionPeriodoMatriculaFk() {
        return rhnHabilitacionPeriodoMatriculaFk;
    }

    public void setRhnHabilitacionPeriodoMatriculaFk(SgHabilitacionPeriodoMatricula rhnHabilitacionPeriodoMatriculaFk) {
        this.rhnHabilitacionPeriodoMatriculaFk = rhnHabilitacionPeriodoMatriculaFk;
    }

    public LocalDateTime getRhnUltModFecha() {
        return rhnUltModFecha;
    }

    public void setRhnUltModFecha(LocalDateTime rhnUltModFecha) {
        this.rhnUltModFecha = rhnUltModFecha;
    }

    public String getRhnUltModUsuario() {
        return rhnUltModUsuario;
    }

    public void setRhnUltModUsuario(String rhnUltModUsuario) {
        this.rhnUltModUsuario = rhnUltModUsuario;
    }

    public Integer getRhnVersion() {
        return rhnVersion;
    }

    public void setRhnVersion(Integer rhnVersion) {
        this.rhnVersion = rhnVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.rhnPk);
        hash = 53 * hash + Objects.hashCode(this.rhnNivelFk);
        hash = 53 * hash + Objects.hashCode(this.rhnModalidadAtencionFk);
        hash = 53 * hash + Objects.hashCode(this.rhnSubmodaliadadFk);
        hash = 53 * hash + Objects.hashCode(this.rhnHabilitacionPeriodoMatriculaFk);
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
        final SgRelHabilitacionMatriculaNivel other = (SgRelHabilitacionMatriculaNivel) obj;
        if (!Objects.equals(this.rhnPk, other.rhnPk)) {
            return false;
        }
        if (!Objects.equals(this.rhnNivelFk, other.rhnNivelFk)) {
            return false;
        }
        if (!Objects.equals(this.rhnModalidadAtencionFk, other.rhnModalidadAtencionFk)) {
            return false;
        }
        if (!Objects.equals(this.rhnSubmodaliadadFk, other.rhnSubmodaliadadFk)) {
            return false;
        }
        if (!Objects.equals(this.rhnHabilitacionPeriodoMatriculaFk, other.rhnHabilitacionPeriodoMatriculaFk)) {
            return false;
        }
        return true;
    }



   

    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelHabilitacionMatriculaNivel[ rhnPk=" + rhnPk + " ]";
    }
    
}
