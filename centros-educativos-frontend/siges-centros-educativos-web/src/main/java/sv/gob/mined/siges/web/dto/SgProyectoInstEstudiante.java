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

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "piePk", scope = SgProyectoInstEstudiante.class)
public class SgProyectoInstEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long piePk;
    
    private SgProyectoInstitucionalSede pieProyectoInstitucional;
    
    private SgEstudiante pieEstudiante;
    
    private LocalDate pieFechaOtorgado;
    
    private String pieObservaciones;
    
    private LocalDateTime pieUltModFecha;
    
    private String pieUltModUsuario;
    
    private Integer pieVersion;
    

    public SgProyectoInstEstudiante() {
    }

    public SgProyectoInstEstudiante(Long piePk) {
        this.piePk = piePk;
    }

    public Long getPiePk() {
        return piePk;
    }

    public void setPiePk(Long piePk) {
        this.piePk = piePk;
    }

    public SgProyectoInstitucionalSede getPieProyectoInstitucional() {
        return pieProyectoInstitucional;
    }

    public void setPieProyectoInstitucional(SgProyectoInstitucionalSede pieProyectoInstitucional) {
        this.pieProyectoInstitucional = pieProyectoInstitucional;
    }

    public SgEstudiante getPieEstudiante() {
        return pieEstudiante;
    }

    public void setPieEstudiante(SgEstudiante pieEstudiante) {
        this.pieEstudiante = pieEstudiante;
    }

    public LocalDate getPieFechaOtorgado() {
        return pieFechaOtorgado;
    }

    public void setPieFechaOtorgado(LocalDate pieFechaOtorgado) {
        this.pieFechaOtorgado = pieFechaOtorgado;
    }

    public String getPieObservaciones() {
        return pieObservaciones;
    }

    public void setPieObservaciones(String pieObservaciones) {
        this.pieObservaciones = pieObservaciones;
    }

    public LocalDateTime getPieUltModFecha() {
        return pieUltModFecha;
    }

    public void setPieUltModFecha(LocalDateTime pieUltModFecha) {
        this.pieUltModFecha = pieUltModFecha;
    }

    public String getPieUltModUsuario() {
        return pieUltModUsuario;
    }

    public void setPieUltModUsuario(String pieUltModUsuario) {
        this.pieUltModUsuario = pieUltModUsuario;
    }

    public Integer getPieVersion() {
        return pieVersion;
    }

    public void setPieVersion(Integer pieVersion) {
        this.pieVersion = pieVersion;
    }


    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (piePk != null ? piePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgProyectoInstEstudiante)) {
            return false;
        }
        SgProyectoInstEstudiante other = (SgProyectoInstEstudiante) object;
        if ((this.piePk == null && other.piePk != null) || (this.piePk != null && !this.piePk.equals(other.piePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgProyectoInstEstudiante[ piePk=" + piePk + " ]";
    }
    
}
