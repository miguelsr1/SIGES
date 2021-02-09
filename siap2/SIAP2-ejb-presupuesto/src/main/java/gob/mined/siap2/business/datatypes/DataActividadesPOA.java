/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.datatypes;

import java.math.BigDecimal;

/**
 *
 * @author bruno
 */
public class DataActividadesPOA {
    private String nombrePOA;
    private String nombreUnidadTecnicaPOA;
    private String nombreUnidadTecnicaActividad;
    private String nombreMeta;
    private String nombreIndicador;
    private String nombreActividad;
    private Integer porcentajeAvance;
    private BigDecimal porcentajeCumplimiento;
    private BigDecimal pendienteCumplimiento;
    private String ultimoPeriodoReportado;
    private String color;
    public String getNombrePOA() {
        return nombrePOA;
    }

    public void setNombrePOA(String nombrePOA) {
        this.nombrePOA = nombrePOA;
    }

    public String getNombreMeta() {
        return nombreMeta;
    }

    public void setNombreMeta(String nombreMeta) {
        this.nombreMeta = nombreMeta;
    }

    public String getNombreIndicador() {
        return nombreIndicador;
    }

    public void setNombreIndicador(String nombreIndicador) {
        this.nombreIndicador = nombreIndicador;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public Integer getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(Integer porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    public String getUltimoPeriodoReportado() {
        return ultimoPeriodoReportado;
    }

    public void setUltimoPeriodoReportado(String ultimoPeriodoReportado) {
        this.ultimoPeriodoReportado = ultimoPeriodoReportado;
    }

    public String getNombreUnidadTecnicaPOA() {
        return nombreUnidadTecnicaPOA;
    }

    public void setNombreUnidadTecnicaPOA(String nombreUnidadTecnicaPOA) {
        this.nombreUnidadTecnicaPOA = nombreUnidadTecnicaPOA;
    }

    public String getNombreUnidadTecnicaActividad() {
        return nombreUnidadTecnicaActividad;
    }

    public void setNombreUnidadTecnicaActividad(String nombreUnidadTecnicaActividad) {
        this.nombreUnidadTecnicaActividad = nombreUnidadTecnicaActividad;
    }

    public BigDecimal getPorcentajeCumplimiento() {
        return porcentajeCumplimiento;
    }

    public void setPorcentajeCumplimiento(BigDecimal porcentajeCumplimiento) {
        this.porcentajeCumplimiento = porcentajeCumplimiento;
    }

    public BigDecimal getPendienteCumplimiento() {
        return pendienteCumplimiento;
    }

    public void setPendienteCumplimiento(BigDecimal pendienteCumplimiento) {
        this.pendienteCumplimiento = pendienteCumplimiento;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    
}
