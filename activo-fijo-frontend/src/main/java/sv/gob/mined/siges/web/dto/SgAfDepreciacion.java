/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgAfProyectos;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "depPk", scope = SgAfDepreciacion.class)
public class SgAfDepreciacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long depPk;
    private Integer depAnio;
    private Integer depMes;
    private BigDecimal depMontoDepreciacion;
    private Integer depNoDiasDepreciados;
    private LocalDateTime depFechaCalculo;
    private SgAfFuenteFinanciamiento depFuenteFinanciamientoFk;
    private SgAfProyectos depProyectoFk;
    private SgAfBienDepreciable depBienesDepreciablesFk;
    private LocalDateTime depUltModFecha;
    private String depUltModUsuario;
    private Integer depVersion;
    
    

    public SgAfDepreciacion() {
    }

    public SgAfDepreciacion(Long depPk) {
        this.depPk = depPk;
    }

    public Long getDepPk() {
        return depPk;
    }

    public void setDepPk(Long depPk) {
        this.depPk = depPk;
    }

    public Integer getDepAnio() {
        return depAnio;
    }

    public void setDepAnio(Integer depAnio) {
        this.depAnio = depAnio;
    }

    public Integer getDepMes() {
        return depMes;
    }

    public void setDepMes(Integer depMes) {
        this.depMes = depMes;
    }

    public BigDecimal getDepMontoDepreciacion() {
        return depMontoDepreciacion;
    }

    public void setDepMontoDepreciacion(BigDecimal depMontoDepreciacion) {
        this.depMontoDepreciacion = depMontoDepreciacion;
    }

    public Integer getDepNoDiasDepreciados() {
        return depNoDiasDepreciados;
    }

    public void setDepNoDiasDepreciados(Integer depNoDiasDepreciados) {
        this.depNoDiasDepreciados = depNoDiasDepreciados;
    }

    public LocalDateTime getDepFechaCalculo() {
        return depFechaCalculo;
    }

    public void setDepFechaCalculo(LocalDateTime depFechaCalculo) {
        this.depFechaCalculo = depFechaCalculo;
    }

    public SgAfFuenteFinanciamiento getDepFuenteFinanciamientoFk() {
        return depFuenteFinanciamientoFk;
    }

    public void setDepFuenteFinanciamientoFk(SgAfFuenteFinanciamiento depFuenteFinanciamientoFk) {
        this.depFuenteFinanciamientoFk = depFuenteFinanciamientoFk;
    }

    public SgAfProyectos getDepProyectoFk() {
        return depProyectoFk;
    }

    public void setDepProyectoFk(SgAfProyectos depProyectoFk) {
        this.depProyectoFk = depProyectoFk;
    }

    public LocalDateTime getDepUltModFecha() {
        return depUltModFecha;
    }

    public void setDepUltModFecha(LocalDateTime depUltModFecha) {
        this.depUltModFecha = depUltModFecha;
    }

    public String getDepUltModUsuario() {
        return depUltModUsuario;
    }

    public void setDepUltModUsuario(String depUltModUsuario) {
        this.depUltModUsuario = depUltModUsuario;
    }

    public Integer getDepVersion() {
        return depVersion;
    }

    public void setDepVersion(Integer depVersion) {
        this.depVersion = depVersion;
    }

    public SgAfBienDepreciable getDepBienesDepreciablesFk() {
        return depBienesDepreciablesFk;
    }

    public void setDepBienesDepreciablesFk(SgAfBienDepreciable depBienesDepreciablesFk) {
        this.depBienesDepreciablesFk = depBienesDepreciablesFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (depPk != null ? depPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfDepreciacion)) {
            return false;
        }
        SgAfDepreciacion other = (SgAfDepreciacion) object;
        if ((this.depPk == null && other.depPk != null) || (this.depPk != null && !this.depPk.equals(other.depPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfDepreciacion[ depPk=" + depPk + " ]";
    }
    
}
