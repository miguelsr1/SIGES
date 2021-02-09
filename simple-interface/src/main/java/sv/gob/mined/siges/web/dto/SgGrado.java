/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "graPk", scope = SgGrado.class)
public class SgGrado implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long graPk;

    private String graCodigo;

    private String graNombre;
    
    private String graDescripcion;
        
    private Integer graOrden;

    private Boolean graHabilitado;

    private Integer graVersion;
    
    
    
    public SgGrado() {
    }

    public Long getGraPk() {
        return graPk;
    }

    public void setGraPk(Long graPk) {
        this.graPk = graPk;
    }

    public String getGraCodigo() {
        return graCodigo;
    }

    public void setGraCodigo(String graCodigo) {
        this.graCodigo = graCodigo;
    }

    public String getGraNombre() {
        return graNombre;
    }

    public void setGraNombre(String graNombre) {
        this.graNombre = graNombre;
    }

    public String getGraDescripcion() {
        return graDescripcion;
    }

    public void setGraDescripcion(String graDescripcion) {
        this.graDescripcion = graDescripcion;
    }

    public Integer getGraOrden() {
        return graOrden;
    }

    public void setGraOrden(Integer graOrden) {
        this.graOrden = graOrden;
    }

    public Boolean getGraHabilitado() {
        return graHabilitado;
    }

    public void setGraHabilitado(Boolean graHabilitado) {
        this.graHabilitado = graHabilitado;
    }

    public Integer getGraVersion() {
        return graVersion;
    }

    public void setGraVersion(Integer graVersion) {
        this.graVersion = graVersion;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (graPk != null ? graPk.hashCode() : 0);
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
        final SgGrado other = (SgGrado) obj;
        if (!Objects.equals(this.graPk, other.graPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgGrado[ grdPk=" + graPk + " ]";
    }


}
