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
import sv.gob.mined.siges.web.enumerados.EnumEstadoHabilitacionPeriodoMatricula;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "hmpPk", scope = SgHabilitacionPeriodoMatricula.class)
public class SgHabilitacionPeriodoMatricula implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long hmpPk;
    
    private LocalDate hmpFechaSolicitud;

    private EnumEstadoHabilitacionPeriodoMatricula hmpEstado;
    
    private String hmpObservacion;
    
    private LocalDate hmpFechaDesde;
    
    private LocalDate hmpFechaHasta;
    
    private String hmpObservacionAprobacionRechazo;    
    
    private LocalDateTime hmpUltModFecha;

    private String hmpUltModUsuario;

    private Integer hmpVersion;

    private SgSede hmpSedeFk;

    private List<SgRelHabilitacionMatriculaNivel> hmpRelHabilitacionMatriculaNivel;
    
    
    public SgHabilitacionPeriodoMatricula() {
    
    }

    public SgHabilitacionPeriodoMatricula(Long hmpPk, Integer hmpVersion) {
        this.hmpPk = hmpPk;
        this.hmpVersion = hmpVersion;
    }

    public Long getHmpPk() {
        return hmpPk;
    }

    public void setHmpPk(Long hmpPk) {
        this.hmpPk = hmpPk;
    }


    public LocalDateTime getHmpUltModFecha() {
        return hmpUltModFecha;
    }

    public void setHmpUltModFecha(LocalDateTime hmpUltModFecha) {
        this.hmpUltModFecha = hmpUltModFecha;
    }

    public String getHmpUltModUsuario() {
        return hmpUltModUsuario;
    }

    public void setHmpUltModUsuario(String hmpUltModUsuario) {
        this.hmpUltModUsuario = hmpUltModUsuario;
    }

    public Integer getHmpVersion() {
        return hmpVersion;
    }

    public void setHmpVersion(Integer hmpVersion) {
        this.hmpVersion = hmpVersion;
    }

    public LocalDate getHmpFechaSolicitud() {
        return hmpFechaSolicitud;
    }

    public void setHmpFechaSolicitud(LocalDate hmpFechaSolicitud) {
        this.hmpFechaSolicitud = hmpFechaSolicitud;
    }

    public EnumEstadoHabilitacionPeriodoMatricula getHmpEstado() {
        return hmpEstado;
    }

    public void setHmpEstado(EnumEstadoHabilitacionPeriodoMatricula hmpEstado) {
        this.hmpEstado = hmpEstado;
    }

    public String getHmpObservacion() {
        return hmpObservacion;
    }

    public void setHmpObservacion(String hmpObservacion) {
        this.hmpObservacion = hmpObservacion;
    }

    public LocalDate getHmpFechaDesde() {
        return hmpFechaDesde;
    }

    public void setHmpFechaDesde(LocalDate hmpFechaDesde) {
        this.hmpFechaDesde = hmpFechaDesde;
    }

    public LocalDate getHmpFechaHasta() {
        return hmpFechaHasta;
    }

    public void setHmpFechaHasta(LocalDate hmpFechaHasta) {
        this.hmpFechaHasta = hmpFechaHasta;
    }

    public String getHmpObservacionAprobacionRechazo() {
        return hmpObservacionAprobacionRechazo;
    }

    public void setHmpObservacionAprobacionRechazo(String hmpObservacionAprobacionRechazo) {
        this.hmpObservacionAprobacionRechazo = hmpObservacionAprobacionRechazo;
    }

    public SgSede getHmpSedeFk() {
        return hmpSedeFk;
    }

    public void setHmpSedeFk(SgSede hmpSedeFk) {
        this.hmpSedeFk = hmpSedeFk;
    }

    public List<SgRelHabilitacionMatriculaNivel> getHmpRelHabilitacionMatriculaNivel() {
        return hmpRelHabilitacionMatriculaNivel;
    }

    public void setHmpRelHabilitacionMatriculaNivel(List<SgRelHabilitacionMatriculaNivel> hmpRelHabilitacionMatriculaNivel) {
        this.hmpRelHabilitacionMatriculaNivel = hmpRelHabilitacionMatriculaNivel;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hmpPk != null ? hmpPk.hashCode() : 0);
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
        final SgHabilitacionPeriodoMatricula other = (SgHabilitacionPeriodoMatricula) obj;
        if (!Objects.equals(this.hmpPk, other.hmpPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgHabilitacionPeriodoMatricula[ hmpPk=" + hmpPk + " ]";
    }
    
}
