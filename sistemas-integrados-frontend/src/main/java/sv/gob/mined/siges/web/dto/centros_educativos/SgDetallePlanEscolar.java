/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgRecurso;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dpePk", scope = SgDetallePlanEscolar.class)
public class SgDetallePlanEscolar implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long dpePk;
        
    private String dpeActividad;
    
    private String dpeResponsables;
        
    private Double dpeCostoEstimado;
    
    private SgFuenteFinanciamiento dpeFuenteFinanciamiento;
    
    private SgPlanEscolarAnual dpePlanEscolarAnual;
    
    private SgRecurso dpeRecursos;
    
    private LocalDate dpeFechaInicio;
    
    private LocalDate dpeFechaFin;
    
    private Boolean dpeValidado;
    
    private String dpeComentarioValido;
    
    private LocalDateTime dpeUltModFecha;
    
    private String dpeUltModUsuario;
    
    private Integer dpeVersion;
    
    private List<SgArchivo> dpeFotos;

    private Boolean dpeAplicaSistemasIntegrados;
    
    public SgDetallePlanEscolar() {
        this.dpeFotos = new ArrayList<>();
        this.dpeValidado = Boolean.FALSE;
        this.dpeAplicaSistemasIntegrados = Boolean.FALSE;
    }

    public SgDetallePlanEscolar(Long dpePk) {
        this.dpePk = dpePk;
    }

    public Long getDpePk() {
        return dpePk;
    }

    public void setDpePk(Long dpePk) {
        this.dpePk = dpePk;
    }

    public String getDpeActividad() {
        return dpeActividad;
    }

    public void setDpeActividad(String dpeActividad) {
        this.dpeActividad = dpeActividad;
    }

    public String getDpeResponsables() {
        return dpeResponsables;
    }

    public void setDpeResponsables(String dpeResponsables) {
        this.dpeResponsables = dpeResponsables;
    }

    public Double getDpeCostoEstimado() {
        return dpeCostoEstimado;
    }

    public void setDpeCostoEstimado(Double dpeCostoEstimado) {
        this.dpeCostoEstimado = dpeCostoEstimado;
    }

    public SgFuenteFinanciamiento getDpeFuenteFinanciamiento() {
        return dpeFuenteFinanciamiento;
    }

    public void setDpeFuenteFinanciamiento(SgFuenteFinanciamiento dpeFuenteFinanciamiento) {
        this.dpeFuenteFinanciamiento = dpeFuenteFinanciamiento;
    }

    public LocalDate getDpeFechaInicio() {
        return dpeFechaInicio;
    }

    public void setDpeFechaInicio(LocalDate dpeFechaInicio) {
        this.dpeFechaInicio = dpeFechaInicio;
    }

    public LocalDate getDpeFechaFin() {
        return dpeFechaFin;
    }

    public void setDpeFechaFin(LocalDate dpeFechaFin) {
        this.dpeFechaFin = dpeFechaFin;
    }

    public Boolean getDpeValidado() {
        return dpeValidado;
    }

    public void setDpeValidado(Boolean dpeValidado) {
        this.dpeValidado = dpeValidado;
    }

    public String getDpeComentarioValido() {
        return dpeComentarioValido;
    }

    public void setDpeComentarioValido(String dpeComentarioValido) {
        this.dpeComentarioValido = dpeComentarioValido;
    }

    public LocalDateTime getDpeUltModFecha() {
        return dpeUltModFecha;
    }

    public void setDpeUltModFecha(LocalDateTime dpeUltModFecha) {
        this.dpeUltModFecha = dpeUltModFecha;
    }

    public String getDpeUltModUsuario() {
        return dpeUltModUsuario;
    }

    public void setDpeUltModUsuario(String dpeUltModUsuario) {
        this.dpeUltModUsuario = dpeUltModUsuario;
    }

    public Integer getDpeVersion() {
        return dpeVersion;
    }

    public void setDpeVersion(Integer dpeVersion) {
        this.dpeVersion = dpeVersion;
    }

    public List<SgArchivo> getDpeFotos() {
        return dpeFotos;
    }

    public void setDpeFotos(List<SgArchivo> dpeFotos) {
        this.dpeFotos = dpeFotos;
    }

    public Boolean getDpeAplicaSistemasIntegrados() {
        return dpeAplicaSistemasIntegrados;
    }

    public void setDpeAplicaSistemasIntegrados(Boolean dpeAplicaSistemasIntegrados) {
        this.dpeAplicaSistemasIntegrados = dpeAplicaSistemasIntegrados;
    }

    public SgRecurso getDpeRecursos() {
        return dpeRecursos;
    }

    public void setDpeRecursos(SgRecurso dpeRecursos) {
        this.dpeRecursos = dpeRecursos;
    }

    public SgPlanEscolarAnual getDpePlanEscolarAnual() {
        return dpePlanEscolarAnual;
    }

    public void setDpePlanEscolarAnual(SgPlanEscolarAnual dpePlanEscolarAnual) {
        this.dpePlanEscolarAnual = dpePlanEscolarAnual;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dpePk != null ? dpePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDetallePlanEscolar)) {
            return false;
        }
        SgDetallePlanEscolar other = (SgDetallePlanEscolar) object;
        if ((this.dpePk == null && other.dpePk != null) || (this.dpePk != null && !this.dpePk.equals(other.dpePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDetallePlanEscolar[ dpePk=" + dpePk + " ]";
    }
    
}
