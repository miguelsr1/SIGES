/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dpePk", scope = SgDetallePlanEscolar.class)
public class SgDetallePlanEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dpePk;

    private SgPlanEscolarAnual dpePlanEscolarAnual;

    private String dpeActividad;

    private String dpeResponsables;

    private Double dpeCostoEstimado;

    private LocalDate dpeFechaInicio;

    private LocalDate dpeFechaFin;

    private Boolean dpeValidado;

    private String dpeComentarioValido;

    private LocalDateTime dpeUltModFecha;

    private String dpeUltModUsuario;

    private Integer dpeVersion;

    public SgDetallePlanEscolar() {
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

    public SgPlanEscolarAnual getDpePlanEscolarAnual() {
        return dpePlanEscolarAnual;
    }

    public void setDpePlanEscolarAnual(SgPlanEscolarAnual dpePlanEscolarAnual) {
        this.dpePlanEscolarAnual = dpePlanEscolarAnual;
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
