/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.math.BigDecimal;

/**
 * Datos de los insumos que se incluyen en el reporte PAC
 * @author Sofis Solutions
 */
public class DataReportePACInsumo {
    private String nroProceso;
    private String nombreInsumo;
    private String nombreMetodoAdquisicion;
    private BigDecimal montoPresupuestado;
    
    private String fechaRecepcionDeTDRoET;
    private String fechaRevisionDeTDRoET;
    private String fechaInvitacionOPublicacion;
    private String fechaRecepcionDeOfertas;
    private String fechaEvaluacion;
    private String fechaAdjudicacion;
    private String fechaContratacion;
    private String fechaContratado;
    private String fechaOrdenDeInicio;
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getNroProceso() {
        return nroProceso;
    }

    public void setNroProceso(String nroProceso) {
        this.nroProceso = nroProceso;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public String getNombreMetodoAdquisicion() {
        return nombreMetodoAdquisicion;
    }

    public void setNombreMetodoAdquisicion(String nombreMetodoAdquisicion) {
        this.nombreMetodoAdquisicion = nombreMetodoAdquisicion;
    }

    public BigDecimal getMontoPresupuestado() {
        return montoPresupuestado;
    }

    public void setMontoPresupuestado(BigDecimal montoPresupuestado) {
        this.montoPresupuestado = montoPresupuestado;
    }

    

    public String getFechaRecepcionDeOfertas() {
        return fechaRecepcionDeOfertas;
    }

    public void setFechaRecepcionDeOfertas(String fechaRecepcionDeOfertas) {
        this.fechaRecepcionDeOfertas = fechaRecepcionDeOfertas;
    }

    public String getFechaRecepcionDeTDRoET() {
        return fechaRecepcionDeTDRoET;
    }

    public void setFechaRecepcionDeTDRoET(String fechaRecepcionDeTDRoET) {
        this.fechaRecepcionDeTDRoET = fechaRecepcionDeTDRoET;
    }

    public String getFechaRevisionDeTDRoET() {
        return fechaRevisionDeTDRoET;
    }

    public void setFechaRevisionDeTDRoET(String fechaRevisionDeTDRoET) {
        this.fechaRevisionDeTDRoET = fechaRevisionDeTDRoET;
    }

    public String getFechaInvitacionOPublicacion() {
        return fechaInvitacionOPublicacion;
    }

    public void setFechaInvitacionOPublicacion(String fechaInvitacionOPublicacion) {
        this.fechaInvitacionOPublicacion = fechaInvitacionOPublicacion;
    }

    
    
    public String getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(String fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public String getFechaContratado() {
        return fechaContratado;
    }

    public void setFechaContratado(String fechaContratado) {
        this.fechaContratado = fechaContratado;
    }

    public String getFechaOrdenDeInicio() {
        return fechaOrdenDeInicio;
    }

    public void setFechaOrdenDeInicio(String fechaOrdenDeInicio) {
        this.fechaOrdenDeInicio = fechaOrdenDeInicio;
    }

    public String getFechaAdjudicacion() {
        return fechaAdjudicacion;
    }

    public void setFechaAdjudicacion(String fechaAdjudicacion) {
        this.fechaAdjudicacion = fechaAdjudicacion;
    }

    public String getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(String fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }
    // </editor-fold>
}
