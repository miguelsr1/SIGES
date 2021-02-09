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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumEstadoHabilitacionPeriodoCalificacion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "hpcPk", scope = SgHabilitacionPeriodoCalificacion.class)
public class SgHabilitacionPeriodoCalificacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long hpcPk;

    private SgMatricula hpcMatriculaFk;
 
    private LocalDate hpcFechaSolicitud;    

    private EnumEstadoHabilitacionPeriodoCalificacion hpcEstado;

    private String hpcObservacion;

    private SgEstudiante hpcEstudianteFk;    

    private String hpcObservacionAprobacionRechazo;

    private LocalDateTime hpcUltModFecha;

    private String hpcUltModUsuario;

    private Integer hpcVersion;
    
    private List<SgRelPeriodosHabilitacionPeriodo> relPeriodosHabilitacionPeriodo;
    
    
    public SgHabilitacionPeriodoCalificacion() {  
    }

    public SgHabilitacionPeriodoCalificacion(Long hpcPk, Integer hpcVersion) {
        this.hpcPk = hpcPk;
        this.hpcVersion = hpcVersion;
    }
  
  

    public Long getHpcPk() {
        return hpcPk;
    }

    public void setHpcPk(Long hpcPk) {
        this.hpcPk = hpcPk;
    }

    public SgMatricula getHpcMatriculaFk() {
        return hpcMatriculaFk;
    }

    public void setHpcMatriculaFk(SgMatricula hpcMatriculaFk) {
        this.hpcMatriculaFk = hpcMatriculaFk;
    }


    public LocalDate getHpcFechaSolicitud() {
        return hpcFechaSolicitud;
    }

    public void setHpcFechaSolicitud(LocalDate hpcFechaSolicitud) {
        this.hpcFechaSolicitud = hpcFechaSolicitud;
    }

    public EnumEstadoHabilitacionPeriodoCalificacion getHpcEstado() {
        return hpcEstado;
    }

    public void setHpcEstado(EnumEstadoHabilitacionPeriodoCalificacion hpcEstado) {
        this.hpcEstado = hpcEstado;
    }

    public String getHpcObservacion() {
        return hpcObservacion;
    }

    public void setHpcObservacion(String hpcObservacion) {
        this.hpcObservacion = hpcObservacion;
    }

    public SgEstudiante getHpcEstudianteFk() {
        return hpcEstudianteFk;
    }

    public void setHpcEstudianteFk(SgEstudiante hpcEstudianteFk) {
        this.hpcEstudianteFk = hpcEstudianteFk;
    }

    public String getHpcObservacionAprobacionRechazo() {
        return hpcObservacionAprobacionRechazo;
    }

    public void setHpcObservacionAprobacionRechazo(String hpcObservacionAprobacionRechazo) {
        this.hpcObservacionAprobacionRechazo = hpcObservacionAprobacionRechazo;
    }


    public LocalDateTime getHpcUltModFecha() {
        return hpcUltModFecha;
    }

    public void setHpcUltModFecha(LocalDateTime hpcUltModFecha) {
        this.hpcUltModFecha = hpcUltModFecha;
    }

    public String getHpcUltModUsuario() {
        return hpcUltModUsuario;
    }

    public void setHpcUltModUsuario(String hpcUltModUsuario) {
        this.hpcUltModUsuario = hpcUltModUsuario;
    }

    public Integer getHpcVersion() {
        return hpcVersion;
    }

    public void setHpcVersion(Integer hpcVersion) {
        this.hpcVersion = hpcVersion;
    }

    public List<SgRelPeriodosHabilitacionPeriodo> getRelPeriodosHabilitacionPeriodo() {
        return relPeriodosHabilitacionPeriodo;
    }

    public void setRelPeriodosHabilitacionPeriodo(List<SgRelPeriodosHabilitacionPeriodo> relPeriodosHabilitacionPeriodo) {
        this.relPeriodosHabilitacionPeriodo = relPeriodosHabilitacionPeriodo;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hpcPk != null ? hpcPk.hashCode() : 0);
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
        final SgHabilitacionPeriodoCalificacion other = (SgHabilitacionPeriodoCalificacion) obj;
        if (!Objects.equals(this.hpcPk, other.hpcPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgHabilitacionPeriodoCalificacion[ hpcPk=" + hpcPk + " ]";
    }
    
}
