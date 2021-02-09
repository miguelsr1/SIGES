/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rpePk", scope = SgRelPersonalEspecialidad.class)
public class SgRelPersonalEspecialidad implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long rpePk;
   
    private SgPersonalSedeEducativa rpePersonal;
    
    private SgEspecialidad rpeEspecialidad;
    
    private LocalDateTime rpeUltModFecha;
    
    private String rpeUltModUsuario;
    
    private Integer rpeVersion;
    
    private LocalDate rpeFechaGraduacion;
    
    private Double rpeCum;

    public SgRelPersonalEspecialidad() {
    }
    
    @JsonIgnore
    public String getRelCompleto() {
        
        StringBuilder s = new StringBuilder();
        
        if (this.rpeFechaGraduacion!=null) {
            s.append(this.rpeFechaGraduacion).append(" - ");
        }
        if (this.rpeCum!=null) {
            s.append(this.rpeCum).append(" - ");
        }
        if (!StringUtils.isBlank(this.rpeEspecialidad.getEspNombre())) {
            s.append(this.rpeEspecialidad.getEspNombre());
        }
        
        return s.toString();
    }


    public Long getRpePk() {
        return rpePk;
    }

    public void setRpePk(Long rpePk) {
        this.rpePk = rpePk;
    }

    public SgPersonalSedeEducativa getRpePersonal() {
        return rpePersonal;
    }

    public void setRpePersonal(SgPersonalSedeEducativa rpePersonal) {
        this.rpePersonal = rpePersonal;
    }

    public SgEspecialidad getRpeEspecialidad() {
        return rpeEspecialidad;
    }

    public void setRpeEspecialidad(SgEspecialidad rpeEspecialidad) {
        this.rpeEspecialidad = rpeEspecialidad;
    }

    public LocalDateTime getRpeUltModFecha() {
        return rpeUltModFecha;
    }

    public void setRpeUltModFecha(LocalDateTime rpeUltModFecha) {
        this.rpeUltModFecha = rpeUltModFecha;
    }

    public String getRpeUltModUsuario() {
        return rpeUltModUsuario;
    }

    public void setRpeUltModUsuario(String rpeUltModUsuario) {
        this.rpeUltModUsuario = rpeUltModUsuario;
    }

    public Integer getRpeVersion() {
        return rpeVersion;
    }

    public void setRpeVersion(Integer rpeVersion) {
        this.rpeVersion = rpeVersion;
    }

    public LocalDate getRpeFechaGraduacion() {
        return rpeFechaGraduacion;
    }

    public void setRpeFechaGraduacion(LocalDate rpeFechaGraduacion) {
        this.rpeFechaGraduacion = rpeFechaGraduacion;
    }

    public Double getRpeCum() {
        return rpeCum;
    }

    public void setRpeCum(Double rpeCum) {
        this.rpeCum = rpeCum;
    }
    

    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rpePk != null ? rpePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRelPersonalEspecialidad)) {
            return false;
        }
        SgRelPersonalEspecialidad other = (SgRelPersonalEspecialidad) object;
        if ((this.rpePk == null && other.rpePk != null) || (this.rpePk != null && !this.rpePk.equals(other.rpePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelPersonalEspecialidad[ rpePk=" + rpePk + " ]";
    }
    
}
