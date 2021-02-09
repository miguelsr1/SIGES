/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoRetiro;
import sv.gob.mined.siges.web.enumerados.centros_educativos.EnumMatriculaEstado;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "matPk", scope = SgMatricula.class)
public class SgMatricula implements Serializable {

    private Long matPk;
    
    private SgSeccion matSeccion;
    
    private SgEstudiante matEstudiante;
    
    private EnumMatriculaEstado matEstado;
    
    private LocalDate matFechaHasta;
  
    private String matObservaciones;
    
    private SgMotivoRetiro matMotivoRetiro;    
    
    private LocalDateTime matUltModFecha;
    
    private String matUltModUsuario;
    
    private Integer matVersion;
    
    private Boolean matProvisional;
    


    public SgMatricula() {
    }

    public SgMatricula(Long matPk) {
        this.matPk = matPk;
    }

    public Long getMatPk() {
        return matPk;
    }

    public void setMatPk(Long matPk) {
        this.matPk = matPk;
    }

    public SgSeccion getMatSeccion() {
        return matSeccion;
    }

    public void setMatSeccion(SgSeccion matSeccion) {
        this.matSeccion = matSeccion;
    }

    public SgEstudiante getMatEstudiante() {
        return matEstudiante;
    }

    public void setMatEstudiante(SgEstudiante matEstudiante) {
        this.matEstudiante = matEstudiante;
    }

    public EnumMatriculaEstado getMatEstado() {
        return matEstado;
    }

    public void setMatEstado(EnumMatriculaEstado matEstado) {
        this.matEstado = matEstado;
    }

    public LocalDate getMatFechaHasta() {
        return matFechaHasta;
    }

    public void setMatFechaHasta(LocalDate matFechaHasta) {
        this.matFechaHasta = matFechaHasta;
    }

    public SgMotivoRetiro getMatMotivoRetiro() {
        return matMotivoRetiro;
    }

    public void setMatMotivoRetiro(SgMotivoRetiro matMotivoRetiro) {
        this.matMotivoRetiro = matMotivoRetiro;
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

    public Boolean getMatProvisional() {
        return matProvisional;
    }

    public void setMatProvisional(Boolean matProvisional) {
        this.matProvisional = matProvisional;
    }

    public String getMatObservaciones() {
        return matObservaciones;
    }

    public void setMatObservaciones(String matObservaciones) {
        this.matObservaciones = matObservaciones;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matPk != null ? matPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgMatricula)) {
            return false;
        }
        SgMatricula other = (SgMatricula) object;
        if ((this.matPk == null && other.matPk != null) || (this.matPk != null && !this.matPk.equals(other.matPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMatricula[ matPk=" + matPk + " ]";
    }
    
}
