/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosCalidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfTipoBienes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.persistencia.entidades.centroseducativos.SgSede;

/**
 *
 * @author Sofis Solutions
 */
public class SgAfBienDepreciableInmDTO implements Serializable {
    private Long bdePk;
    private String bdeDescripcion; 
    private BigDecimal bdeValorAdquisicion;
    private Boolean bdeEsValorEstimado; 
    private Boolean bdeDepreciado; 
    private SgAfEstadosCalidad bdeEstadoCalidad;
    private SgAfTipoBienes bdeTipoBien;
    private SgAfEstadosBienes bdeEstadosBienes;
    private SgSede bdeSede;
    private LocalDate bdeFechaAdquisicion;
    private SgAfUnidadesAdministrativas bdeUnidadesAdministrativas;
    private Long bdeInmueblePk;
    private Long bdeEdificioPk;
    
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

    public SgSede getBdeSede() {
        return bdeSede;
    }

    public void setBdeSede(SgSede bdeSede) {
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

    public Long getBdePk() {
        return bdePk;
    }

    public void setBdePk(Long bdePk) {
        this.bdePk = bdePk;
    }

    public LocalDate getBdeFechaAdquisicion() {
        return bdeFechaAdquisicion;
    }

    public void setBdeFechaAdquisicion(LocalDate bdeFechaAdquisicion) {
        this.bdeFechaAdquisicion = bdeFechaAdquisicion;
    }
}
