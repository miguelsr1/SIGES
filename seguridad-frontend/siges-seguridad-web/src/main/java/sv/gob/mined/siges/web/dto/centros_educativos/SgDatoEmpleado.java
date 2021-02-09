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
import java.util.ArrayList;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.centros_educativos.EnumEstadoDatoEmpleado;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "demPk", scope = SgDatoEmpleado.class)
public class SgDatoEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long demPk;
    
    private SgPersonalSedeEducativa demPersonalSede;
    
    private String demCodigoEmpleado;
    
    private EnumEstadoDatoEmpleado demEstado;
    
    private LocalDate demFechaIngresoGob;
    
    private LocalDate demFechaIngresoMined;
    
    
    private LocalDateTime demUltModFecha;
    
    private String demUltModUsuario;
    
    private Integer demVersion;
    
    private List<SgDatoContratacion> demDatoContratacion;
    

    public SgDatoEmpleado() {
        this.demDatoContratacion = new ArrayList<>();
    }

    public SgDatoEmpleado(Long demPk) {
        this.demPk = demPk;
    }

    public Long getDemPk() {
        return demPk;
    }

    public void setDemPk(Long demPk) {
        this.demPk = demPk;
    }

    public String getDemCodigoEmpleado() {
        return demCodigoEmpleado;
    }

    public void setDemCodigoEmpleado(String demCodigoEmpleado) {
        this.demCodigoEmpleado = demCodigoEmpleado;
    }

    public EnumEstadoDatoEmpleado getDemEstado() {
        return demEstado;
    }

    public void setDemEstado(EnumEstadoDatoEmpleado demEstado) {
        this.demEstado = demEstado;
    }

    public LocalDate getDemFechaIngresoGob() {
        return demFechaIngresoGob;
    }

    public void setDemFechaIngresoGob(LocalDate demFechaIngresoGob) {
        this.demFechaIngresoGob = demFechaIngresoGob;
    }

    public LocalDate getDemFechaIngresoMined() {
        return demFechaIngresoMined;
    }

    public void setDemFechaIngresoMined(LocalDate demFechaIngresoMined) {
        this.demFechaIngresoMined = demFechaIngresoMined;
    }

    public LocalDateTime getDemUltModFecha() {
        return demUltModFecha;
    }

    public void setDemUltModFecha(LocalDateTime demUltModFecha) {
        this.demUltModFecha = demUltModFecha;
    }

    public String getDemUltModUsuario() {
        return demUltModUsuario;
    }

    public void setDemUltModUsuario(String demUltModUsuario) {
        this.demUltModUsuario = demUltModUsuario;
    }

    public Integer getDemVersion() {
        return demVersion;
    }

    public void setDemVersion(Integer demVersion) {
        this.demVersion = demVersion;
    }

    public List<SgDatoContratacion> getDemDatoContratacion() {
        return demDatoContratacion;
    }

    public void setDemDatoContratacion(List<SgDatoContratacion> demDatoContratacion) {
        this.demDatoContratacion = demDatoContratacion;
    }


    public SgPersonalSedeEducativa getDemPersonalSede() {
        return demPersonalSede;
    }

    public void setDemPersonalSede(SgPersonalSedeEducativa demPersonalSede) {
        this.demPersonalSede = demPersonalSede;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (demPk != null ? demPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDatoEmpleado)) {
            return false;
        }
        SgDatoEmpleado other = (SgDatoEmpleado) object;
        if ((this.demPk == null && other.demPk != null) || (this.demPk != null && !this.demPk.equals(other.demPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDatoEmpleado[ demPk=" + demPk + " ]";
    }
    
}
