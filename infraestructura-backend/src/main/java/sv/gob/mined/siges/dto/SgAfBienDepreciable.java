/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfUnidadesAdministrativas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import sv.gob.mined.siges.persistencia.entidades.centros.SgSedeLite;

/**
 *
 * @author Sofis Solutions
 */
public class SgAfBienDepreciable implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bdePk;
    private String bdeDescripcion; 
    private BigDecimal bdeValorAdquisicion;
    private Boolean bdeEsValorEstimado; 
    private Boolean bdeDepreciado; 
    private SgAfEstadosCalidad bdeEstadoCalidad;
    private SgAfTipoBienes bdeTipoBien;
    private SgAfEstadosBienes bdeEstadosBienes;
    private SgSedeLite bdeSede;
    private SgAfUnidadesAdministrativas bdeUnidadesAdministrativas;
    private Long bdeInmueblePk;
    private Long bdeEdificioPk;
    private LocalDate bdeFechaAdquisicion;

    public String getBdePk() {
        return bdePk;
    }

    public void setBdePk(String bdePk) {
        this.bdePk = bdePk;
    }

    public String getBdeDescripcion() {
        return bdeDescripcion;
    }

    public void setBdeDescripcion(String bdeDescripcion) {
        this.bdeDescripcion = bdeDescripcion;
    }

    public BigDecimal getBdeValorAdquisicion() {
        return bdeValorAdquisicion;
    }

    public void setBdeValorAdquisicion(BigDecimal bdeValorAdquisicion) {
        this.bdeValorAdquisicion = bdeValorAdquisicion;
    }

    public Boolean getBdeEsValorEstimado() {
        return bdeEsValorEstimado;
    }

    public void setBdeEsValorEstimado(Boolean bdeEsValorEstimado) {
        this.bdeEsValorEstimado = bdeEsValorEstimado;
    }

    public Boolean getBdeDepreciado() {
        return bdeDepreciado;
    }

    public void setBdeDepreciado(Boolean bdeDepreciado) {
        this.bdeDepreciado = bdeDepreciado;
    }

    public SgAfEstadosCalidad getBdeEstadoCalidad() {
        return bdeEstadoCalidad;
    }

    public void setBdeEstadoCalidad(SgAfEstadosCalidad bdeEstadoCalidad) {
        this.bdeEstadoCalidad = bdeEstadoCalidad;
    }

    public SgAfTipoBienes getBdeTipoBien() {
        return bdeTipoBien;
    }

    public void setBdeTipoBien(SgAfTipoBienes bdeTipoBien) {
        this.bdeTipoBien = bdeTipoBien;
    }

    public SgAfEstadosBienes getBdeEstadosBienes() {
        return bdeEstadosBienes;
    }

    public void setBdeEstadosBienes(SgAfEstadosBienes bdeEstadosBienes) {
        this.bdeEstadosBienes = bdeEstadosBienes;
    }

    public SgSedeLite getBdeSede() {
        return bdeSede;
    }

    public void setBdeSede(SgSedeLite bdeSede) {
        this.bdeSede = bdeSede;
    }

    public SgAfUnidadesAdministrativas getBdeUnidadesAdministrativas() {
        return bdeUnidadesAdministrativas;
    }

    public void setBdeUnidadesAdministrativas(SgAfUnidadesAdministrativas bdeUnidadesAdministrativas) {
        this.bdeUnidadesAdministrativas = bdeUnidadesAdministrativas;
    }

    public Long getBdeInmueblePk() {
        return bdeInmueblePk;
    }

    public void setBdeInmueblePk(Long bdeInmueblePk) {
        this.bdeInmueblePk = bdeInmueblePk;
    }

    public Long getBdeEdificioPk() {
        return bdeEdificioPk;
    }

    public void setBdeEdificioPk(Long bdeEdificioPk) {
        this.bdeEdificioPk = bdeEdificioPk;
    }

    public LocalDate getBdeFechaAdquisicion() {
        return bdeFechaAdquisicion;
    }

    public void setBdeFechaAdquisicion(LocalDate bdeFechaAdquisicion) {
        this.bdeFechaAdquisicion = bdeFechaAdquisicion;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bdePk != null ? bdePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfBienDepreciable)) {
            return false;
        }
        SgAfBienDepreciable other = (SgAfBienDepreciable) object;
        if ((this.bdePk == null && other.bdePk != null) || (this.bdePk != null && !this.bdePk.equals(other.bdePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfBienesDepreciables{" + "bdePk=" + bdePk + '}';
    }
}
