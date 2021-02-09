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
import java.time.LocalTime;
import sv.gob.mined.siges.web.dto.catalogo.SgCargo;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoNombramiento;
import sv.gob.mined.siges.web.utilidades.ViewDto;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "dcoPk", scope = SgDatoContratacion.class)
public class SgDatoContratacion implements Serializable, ViewDto {

    private static final long serialVersionUID = 1L;
    
    private Long dcoPk;
    
    private SgDatoEmpleado dcoDatoEmpleado;
        
    private String dcoInstitucionPagaSalarioOtro;
   
    private LocalTime dcoHoraIngreso;
    
    private LocalTime dcoHoraSalida;
    
    private Double dcoSalarioMensual;
    
    private SgTipoNombramiento dcoTipoNombramiento;
        
    private String dcoEstadoContratacion;
    
    private SgSede dcoCentroEducativo;
    
    private LocalDate dcoDesde;
    
    private LocalDate dcoHasta;
    
    private SgCargo dcoCargo;
    
    private SgJornadaLaboral dcoJornada;
        
    private LocalDateTime dcoUltModFecha;
    
    private String dcoUltModUsuario;
    
    private Integer dcoVersion;
    
    private Boolean dcoPkForView;

    public SgDatoContratacion() {
    }

    public SgDatoContratacion(Long dcoPk) {
        this.dcoPk = dcoPk;
    }

    public Long getDcoPk() {
        return dcoPk;
    }

    public void setDcoPk(Long dcoPk) {
        this.dcoPk = dcoPk;
    }

    public LocalDate getDcoDesde() {
        return dcoDesde;
    }

    public void setDcoDesde(LocalDate dcoDesde) {
        this.dcoDesde = dcoDesde;
    }

    public LocalDate getDcoHasta() {
        return dcoHasta;
    }

    public void setDcoHasta(LocalDate dcoHasta) {
        this.dcoHasta = dcoHasta;
    }

    public SgSede getDcoCentroEducativo() {
        return dcoCentroEducativo;
    }

    public void setDcoCentroEducativo(SgSede dcoCentroEducativo) {
        this.dcoCentroEducativo = dcoCentroEducativo;
    }

    public SgCargo getDcoCargo() {
        return dcoCargo;
    }

    public void setDcoCargo(SgCargo dcoCargo) {
        this.dcoCargo = dcoCargo;
    }

    public SgJornadaLaboral getDcoJornada() {
        return dcoJornada;
    }

    public void setDcoJornada(SgJornadaLaboral dcoJornada) {
        this.dcoJornada = dcoJornada;
    }

    public LocalDateTime getDcoUltModFecha() {
        return dcoUltModFecha;
    }

    public void setDcoUltModFecha(LocalDateTime dcoUltModFecha) {
        this.dcoUltModFecha = dcoUltModFecha;
    }

    public String getDcoUltModUsuario() {
        return dcoUltModUsuario;
    }

    public void setDcoUltModUsuario(String dcoUltModUsuario) {
        this.dcoUltModUsuario = dcoUltModUsuario;
    }

    public Integer getDcoVersion() {
        return dcoVersion;
    }

    public void setDcoVersion(Integer dcoVersion) {
        this.dcoVersion = dcoVersion;
    }

    public SgDatoEmpleado getDcoDatoEmpleado() {
        return dcoDatoEmpleado;
    }

    public void setDcoDatoEmpleado(SgDatoEmpleado dcoDatoEmpleado) {
        this.dcoDatoEmpleado = dcoDatoEmpleado;
    }

    public Boolean getDcoPkForView() {
        return dcoPkForView;
    }

    public void setDcoPkForView(Boolean dcoPkForView) {
        this.dcoPkForView = dcoPkForView;
    }

    public String getDcoInstitucionPagaSalarioOtro() {
        return dcoInstitucionPagaSalarioOtro;
    }

    public void setDcoInstitucionPagaSalarioOtro(String dcoInstitucionPagaSalarioOtro) {
        this.dcoInstitucionPagaSalarioOtro = dcoInstitucionPagaSalarioOtro;
    }

    public LocalTime getDcoHoraIngreso() {
        return dcoHoraIngreso;
    }

    public void setDcoHoraIngreso(LocalTime dcoHoraIngreso) {
        this.dcoHoraIngreso = dcoHoraIngreso;
    }

    public LocalTime getDcoHoraSalida() {
        return dcoHoraSalida;
    }

    public void setDcoHoraSalida(LocalTime dcoHoraSalida) {
        this.dcoHoraSalida = dcoHoraSalida;
    }

    public Double getDcoSalarioMensual() {
        return dcoSalarioMensual;
    }

    public void setDcoSalarioMensual(Double dcoSalarioMensual) {
        this.dcoSalarioMensual = dcoSalarioMensual;
    }

    public SgTipoNombramiento getDcoTipoNombramiento() {
        return dcoTipoNombramiento;
    }

    public void setDcoTipoNombramiento(SgTipoNombramiento dcoTipoNombramiento) {
        this.dcoTipoNombramiento = dcoTipoNombramiento;
    }

    public String getDcoEstadoContratacion() {
        return dcoEstadoContratacion;
    }

    public void setDcoEstadoContratacion(String dcoEstadoContratacion) {
        this.dcoEstadoContratacion = dcoEstadoContratacion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dcoPk != null ? dcoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDatoContratacion)) {
            return false;
        }
        SgDatoContratacion other = (SgDatoContratacion) object;
        if ((this.dcoPk == null && other.dcoPk != null) || (this.dcoPk != null && !this.dcoPk.equals(other.dcoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDatoContratacion[ dcoPk=" + dcoPk + " ]";
    }

    @Override
    public Long getId() {
        return this.dcoPk;
    }

    @Override
    public void setId(Long id) {
        this.dcoPk = id;
    }

    @Override
    public Boolean getIdForView() {
        return this.dcoPkForView;
    }

    @Override
    public void setIdForView(Boolean valor) {
        this.dcoPkForView = valor;
    }
    
}
