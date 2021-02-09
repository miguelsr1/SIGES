/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "matPk", scope = SgModalidadAtencion.class) 
public class SgModalidadAtencion implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long matPk;

    private String matCodigo;

    private String matNombre;

    private String matNombreBusqueda;

    private String matDescripcion;

    private Boolean matHabilitado;

    private LocalDateTime matUltModFecha;
   
    private String matUltModUsuario;
   
    private Integer matVersion;

    private List<SgSubModalidadAtencion> matSubmodalidades;
    
    
    public SgModalidadAtencion() {
        this.matHabilitado = Boolean.TRUE;
    }

    public Long getMatPk() {
        return matPk;
    }

    public void setMatPk(Long matPk) {
        this.matPk = matPk;
    }

    public String getMatCodigo() {
        return matCodigo;
    }

    public void setMatCodigo(String matCodigo) {
        this.matCodigo = matCodigo;
    }

    public String getMatNombre() {
        return matNombre;
    }

    public void setMatNombre(String matNombre) {
        this.matNombre = matNombre;
    }

    public String getMatNombreBusqueda() {
        return matNombreBusqueda;
    }

    public void setMatNombreBusqueda(String matNombreBusqueda) {
        this.matNombreBusqueda = matNombreBusqueda;
    }

    public String getMatDescripcion() {
        return matDescripcion;
    }

    public void setMatDescripcion(String matDescripcion) {
        this.matDescripcion = matDescripcion;
    }

    public Boolean getMatHabilitado() {
        return matHabilitado;
    }

    public void setMatHabilitado(Boolean matHabilitado) {
        this.matHabilitado = matHabilitado;
    }

    public LocalDateTime getMatUltModFecha() {
        return matUltModFecha;
    }

    public void setMatUltModFecha(LocalDateTime matUltModFecha) {
        this.matUltModFecha = matUltModFecha;
    }

    public String getMatUltModUsuario() {
        return matUltModUsuario;
    }

    public void setMatUltModUsuario(String matUltModUsuario) {
        this.matUltModUsuario = matUltModUsuario;
    }

    public Integer getMatVersion() {
        return matVersion;
    }

    public void setMatVersion(Integer matVersion) {
        this.matVersion = matVersion;
    }

    public List<SgSubModalidadAtencion> getMatSubmodalidades() {
        return matSubmodalidades;
    }

    public void setMatSubmodalidades(List<SgSubModalidadAtencion> matSubmodalidades) {
        this.matSubmodalidades = matSubmodalidades;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matPk != null ? matPk.hashCode() : 0);
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
        final SgModalidadAtencion other = (SgModalidadAtencion) obj;
        if (!Objects.equals(this.matPk, other.matPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgModalidadAtencion[ matPk=" + matPk + " ]";
    }
    
}
