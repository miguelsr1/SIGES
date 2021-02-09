/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "epaPk", scope = SgEspecialidadesPersonalAlAplicar.class)
public class SgEspecialidadesPersonalAlAplicar implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long epaPk;

    private SgAplicacionPlaza epaAplicacionPlazaFk;
 
    private SgEspecialidad epaEspecialidad;
    
    private LocalDateTime epaUltModFecha;
   
    private String epaUltModUsuario;
    
    private Integer epaVersion;
 
    private LocalDate epaFechaGraduacion;    

    private Double epaCum;

    public SgEspecialidadesPersonalAlAplicar() {
    }

    public Long getEpaPk() {
        return epaPk;
    }

    public void setEpaPk(Long epaPk) {
        this.epaPk = epaPk;
    }

    public SgAplicacionPlaza getEpaAplicacionPlazaFk() {
        return epaAplicacionPlazaFk;
    }

    public void setEpaAplicacionPlazaFk(SgAplicacionPlaza epaAplicacionPlazaFk) {
        this.epaAplicacionPlazaFk = epaAplicacionPlazaFk;
    }

    public SgEspecialidad getEpaEspecialidad() {
        return epaEspecialidad;
    }

    public void setEpaEspecialidad(SgEspecialidad epaEspecialidad) {
        this.epaEspecialidad = epaEspecialidad;
    }

    public LocalDateTime getEpaUltModFecha() {
        return epaUltModFecha;
    }

    public void setEpaUltModFecha(LocalDateTime epaUltModFecha) {
        this.epaUltModFecha = epaUltModFecha;
    }

    public String getEpaUltModUsuario() {
        return epaUltModUsuario;
    }

    public void setEpaUltModUsuario(String epaUltModUsuario) {
        this.epaUltModUsuario = epaUltModUsuario;
    }

    public Integer getEpaVersion() {
        return epaVersion;
    }

    public void setEpaVersion(Integer epaVersion) {
        this.epaVersion = epaVersion;
    }

    public LocalDate getEpaFechaGraduacion() {
        return epaFechaGraduacion;
    }

    public void setEpaFechaGraduacion(LocalDate epaFechaGraduacion) {
        this.epaFechaGraduacion = epaFechaGraduacion;
    }

    public Double getEpaCum() {
        return epaCum;
    }

    public void setEpaCum(Double epaCum) {
        this.epaCum = epaCum;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.epaPk);
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
        final SgEspecialidadesPersonalAlAplicar other = (SgEspecialidadesPersonalAlAplicar) obj;
        if (!Objects.equals(this.epaPk, other.epaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEspecialidadesPersonalAlAplicar{" + "epaPk=" + epaPk + ", epaAplicacionPlazaFk=" + epaAplicacionPlazaFk + ", epaEspecialidad=" + epaEspecialidad + ", epaUltModFecha=" + epaUltModFecha + ", epaUltModUsuario=" + epaUltModUsuario + ", epaVersion=" + epaVersion + ", epaFechaGraduacion=" + epaFechaGraduacion + ", epaCum=" + epaCum + '}';
    }
    
    
    
    
}
