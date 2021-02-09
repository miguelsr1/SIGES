/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgAfProyectos;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "lprPk", scope = SgAfLiquidacionProyecto.class)
public class SgAfLiquidacionProyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long lprPk;
    private LocalDate lprFechaLiquidacion;
    private SgAfFuenteFinanciamiento lprFuenteFinanciamientoOrigenFk;
    private SgAfFuenteFinanciamiento lprFuenteFinanciamientoDestinoFk;
    private SgAfProyectos lprProyectoFk;
    private LocalDateTime lprFechaCreacion;
    private LocalDateTime lprFechaInicioProcesamiento;
    private LocalDateTime lprFechaFinalProcesamiento;
    private String lprEstado;
    private LocalDateTime lprUltModFecha;
    private String lprUltModUsuario;
    private Integer lprVersion;

    public SgAfLiquidacionProyecto() {
    }

    public Long getLprPk() {
        return lprPk;
    }

    public void setLprPk(Long lprPk) {
        this.lprPk = lprPk;
    }

    public LocalDate getLprFechaLiquidacion() {
        return lprFechaLiquidacion;
    }

    public void setLprFechaLiquidacion(LocalDate lprFechaLiquidacion) {
        this.lprFechaLiquidacion = lprFechaLiquidacion;
    }

    public SgAfFuenteFinanciamiento getLprFuenteFinanciamientoOrigenFk() {
        return lprFuenteFinanciamientoOrigenFk;
    }

    public void setLprFuenteFinanciamientoOrigenFk(SgAfFuenteFinanciamiento lprFuenteFinanciamientoOrigenFk) {
        this.lprFuenteFinanciamientoOrigenFk = lprFuenteFinanciamientoOrigenFk;
    }

    public SgAfFuenteFinanciamiento getLprFuenteFinanciamientoDestinoFk() {
        return lprFuenteFinanciamientoDestinoFk;
    }

    public void setLprFuenteFinanciamientoDestinoFk(SgAfFuenteFinanciamiento lprFuenteFinanciamientoDestinoFk) {
        this.lprFuenteFinanciamientoDestinoFk = lprFuenteFinanciamientoDestinoFk;
    }

    public SgAfProyectos getLprProyectoFk() {
        return lprProyectoFk;
    }

    public void setLprProyectoFk(SgAfProyectos lprProyectoFk) {
        this.lprProyectoFk = lprProyectoFk;
    }

    public LocalDateTime getLprFechaCreacion() {
        return lprFechaCreacion;
    }

    public void setLprFechaCreacion(LocalDateTime lprFechaCreacion) {
        this.lprFechaCreacion = lprFechaCreacion;
    }

    public LocalDateTime getLprFechaInicioProcesamiento() {
        return lprFechaInicioProcesamiento;
    }

    public void setLprFechaInicioProcesamiento(LocalDateTime lprFechaInicioProcesamiento) {
        this.lprFechaInicioProcesamiento = lprFechaInicioProcesamiento;
    }

    public LocalDateTime getLprFechaFinalProcesamiento() {
        return lprFechaFinalProcesamiento;
    }

    public void setLprFechaFinalProcesamiento(LocalDateTime lprFechaFinalProcesamiento) {
        this.lprFechaFinalProcesamiento = lprFechaFinalProcesamiento;
    }

    public String getLprEstado() {
        return lprEstado;
    }

    public void setLprEstado(String lprEstado) {
        this.lprEstado = lprEstado;
    }

    public LocalDateTime getLprUltModFecha() {
        return lprUltModFecha;
    }

    public void setLprUltModFecha(LocalDateTime lprUltModFecha) {
        this.lprUltModFecha = lprUltModFecha;
    }

    public String getLprUltModUsuario() {
        return lprUltModUsuario;
    }

    public void setLprUltModUsuario(String lprUltModUsuario) {
        this.lprUltModUsuario = lprUltModUsuario;
    }

    public Integer getLprVersion() {
        return lprVersion;
    }

    public void setLprVersion(Integer lprVersion) {
        this.lprVersion = lprVersion;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lprPk != null ? lprPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfLiquidacionProyecto)) {
            return false;
        }
        SgAfLiquidacionProyecto other = (SgAfLiquidacionProyecto) object;
        if ((this.lprPk == null && other.lprPk != null) || (this.lprPk != null && !this.lprPk.equals(other.lprPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfLiquidacionProyecto[ lprPk=" + lprPk + " ]";
    }
    
}
