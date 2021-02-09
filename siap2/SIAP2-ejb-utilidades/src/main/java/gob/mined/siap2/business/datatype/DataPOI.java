/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */


public class DataPOI extends DataReporteTemplate implements Serializable{
    
    private String tipoProyecto;
    private String planificacion;
    private String anio;
    private String programa;
    private String proyecto;
    private String unidadTecnica;
    private String indicador;
    private String pm1;
    private String pm2;
    private String pm3;
    private String pm4;
    private String ptotal;
    private String monto;

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(String unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public String getPm1() {
        return pm1;
    }

    public void setPm1(String pm1) {
        this.pm1 = pm1;
    }

    public String getPm2() {
        return pm2;
    }

    public void setPm2(String pm2) {
        this.pm2 = pm2;
    }

    public String getPm3() {
        return pm3;
    }

    public void setPm3(String pm3) {
        this.pm3 = pm3;
    }

    public String getPm4() {
        return pm4;
    }

    public void setPm4(String pm4) {
        this.pm4 = pm4;
    }

    public String getPtotal() {
        return ptotal;
    }

    public void setPtotal(String ptotal) {
        this.ptotal = ptotal;
    }



    public String getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(String planificacion) {
        this.planificacion = planificacion;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(String tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }
    
    
    
    
    
    
}
