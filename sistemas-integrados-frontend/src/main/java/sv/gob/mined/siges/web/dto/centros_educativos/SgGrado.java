/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    private Boolean graRequiereNIE;
    
    private Boolean graAplicaEstadisticas;
    
    private Integer graEdadMinima;

    private Integer graEdadMaxima;

    private LocalDateTime graUltModFecha;

    private String graUltModUsuario;

    private Integer graVersion;
    
    private SgGrado graGradoSiguiente;

    public SgGrado() {
        this.graHabilitado = Boolean.TRUE;
        graAplicaEstadisticas=Boolean.FALSE;
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

    public Boolean getGraRequiereNIE() {
        return graRequiereNIE;
    }

    public void setGraRequiereNIE(Boolean graRequiereNIE) {
        this.graRequiereNIE = graRequiereNIE;
    }

    public LocalDateTime getGraUltModFecha() {
        return graUltModFecha;
    }

    public void setGraUltModFecha(LocalDateTime graUltModFecha) {
        this.graUltModFecha = graUltModFecha;
    }

    public String getGraUltModUsuario() {
        return graUltModUsuario;
    }

    public void setGraUltModUsuario(String graUltModUsuario) {
        this.graUltModUsuario = graUltModUsuario;
    }

    public Integer getGraVersion() {
        return graVersion;
    }

    public void setGraVersion(Integer graVersion) {
        this.graVersion = graVersion;
    }


    public Integer getGraEdadMinima() {
        return graEdadMinima;
    }

    public void setGraEdadMinima(Integer graEdadMinima) {
        this.graEdadMinima = graEdadMinima;
    }

    public Integer getGraEdadMaxima() {
        return graEdadMaxima;
    }

    public Boolean getGraAplicaEstadisticas() {
        return graAplicaEstadisticas;
    }

    public void setGraAplicaEstadisticas(Boolean graAplicaEstadisticas) {
        this.graAplicaEstadisticas = graAplicaEstadisticas;
    }

    public void setGraEdadMaxima(Integer graEdadMaxima) {
        this.graEdadMaxima = graEdadMaxima;
    }

    public SgGrado getGraGradoSiguiente() {
        return graGradoSiguiente;
    }

    public void setGraGradoSiguiente(SgGrado graGradoSiguiente) {
        this.graGradoSiguiente = graGradoSiguiente;
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
        return "SgGrado{" + "graPk=" + graPk + ", graCodigo=" + graCodigo + ", graNombre=" + graNombre + ", graDescripcion=" + graDescripcion + ", graRelacionModalidad=" +"" + ", graOrden=" + graOrden + ", graHabilitado=" + graHabilitado + ", graRequiereNIE=" + graRequiereNIE + '}';
    }

   

}
